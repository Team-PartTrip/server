#!/usr/bin/env python3
"""MySQL(Aiven)에 있는 데이터를 PostgreSQL(Supabase)용 INSERT 로 뽑는다.

스키마는 옮기지 않는다. JPA(ddl-auto=update)가 Postgres 에서도 테이블을 만들어 주므로,
서버를 새 DB 로 한 번 띄운 뒤 여기서 나온 SQL 로 데이터만 넣으면 된다.

  python3 export_from_mysql.py > data.sql

접속 정보는 ../../src/main/resources/application.properties 에서 읽는다.
"""

import re
import subprocess
import sys
from pathlib import Path

PROPS = Path(sys.argv[1]) if len(sys.argv) > 1 else (
    Path(__file__).resolve().parents[2] / "src/main/resources/application.properties"
)

# 외래키 순서다. 부모 먼저 넣지 않으면 들어가지 않는다.
TABLES = [
    "country_info",
    "user_manage",
    "travel_group",
    "group_member",
    "group_travel_plan",
    "vote",
    "vote_option",
    "festival",
    "tour_place",
    "notification",
    "recent_search",
]

# 로그인 재발급용 단기 데이터라 옮기지 않는다. 옮겨봐야 곧 만료된다.
SKIP = {"refresh_token", "email_verification", "pending_signup"}


def props() -> dict:
    out = {}
    for line in PROPS.read_text().splitlines():
        if line.startswith("spring.datasource."):
            key, _, value = line.partition("=")
            out[key.strip()] = value.strip()
    url = out["spring.datasource.url"]
    host, port, db = re.match(r"jdbc:mysql://([^:]+):(\d+)/(\w+)", url).groups()
    return {
        "host": host,
        "port": port,
        "db": db,
        "user": out["spring.datasource.username"],
        "password": out["spring.datasource.password"],
    }


def query(cfg: dict, sql: str) -> list:
    """탭 구분으로 받는다. NULL 은 \\N, 값 안의 탭·개행은 \\t \\n 으로 온다."""
    result = subprocess.run(
        ["mysql", "-N", "-B", "--default-character-set=utf8mb4",
         "-h", cfg["host"], "-P", cfg["port"], "-u", cfg["user"],
         f"-p{cfg['password']}", "--ssl-mode=REQUIRED", cfg["db"], "-e", sql],
        capture_output=True, text=True,
    )
    if result.returncode:
        sys.exit(result.stderr)
    return [line.split("\t") for line in result.stdout.splitlines()]


def literal(value: str) -> str:
    # mysql --batch 는 널을 NULL 로, mysqldump 는 \\N 으로 쓴다. 둘 다 받는다.
    if value in ("\\N", "NULL"):
        return "NULL"
    # Postgres 는 기본(standard_conforming_strings)에서 역슬래시를 글자로 본다.
    # 작은따옴표만 두 번 쓰면 된다.
    value = value.replace("\\t", "\t").replace("\\n", "\n").replace("\\\\", "\\")
    return "'" + value.replace("'", "''") + "'"


def main() -> None:
    cfg = props()
    print("-- MySQL(Aiven) → PostgreSQL(Supabase) 데이터 이전")
    print("-- 테이블은 JPA 가 먼저 만들어 둔 상태여야 한다.")
    print("BEGIN;\n")

    for table in TABLES:
        described = query(cfg, f"SHOW COLUMNS FROM {table}")
        columns = [row[0] for row in described]
        # bit(1) 은 배치 출력에서 날바이트로 나와 글자가 깨진다. 숫자로 바꿔 뽑는다.
        # Postgres boolean 은 '0' · '1' 을 그대로 받는다.
        selected = ", ".join(
            f"{name}+0" if row_type.startswith(("bit(", "tinyint(1)")) else name
            for name, row_type in ((row[0], row[1]) for row in described)
        )
        rows = query(cfg, f"SELECT {selected} FROM {table}")
        if not rows:
            continue
        print(f"-- {table} {len(rows)}건")
        column_list = ", ".join(columns)
        for row in rows:
            values = ", ".join(literal(v) for v in row)
            print(f"INSERT INTO {table} ({column_list}) VALUES ({values});")
        print()

    # 아이디를 그대로 넣었으니 시퀀스를 마지막 값 뒤로 밀어 둔다.
    # 안 하면 다음 INSERT 가 1번부터 시작해 중복 키로 터진다.
    print("-- 시퀀스 보정")
    for table in TABLES:
        keys = query(cfg, f"SHOW KEYS FROM {table} WHERE Key_name = 'PRIMARY'")
        if not keys:
            continue
        pk = keys[0][4]
        extra = query(cfg, f"SHOW COLUMNS FROM {table} LIKE '{pk}'")
        if not extra or "auto_increment" not in extra[0][5]:
            continue
        print(
            f"SELECT setval(pg_get_serial_sequence('{table}', '{pk}'), "
            f"COALESCE((SELECT MAX({pk}) FROM {table}), 1));"
        )

    print("\nCOMMIT;")


if __name__ == "__main__":
    main()
