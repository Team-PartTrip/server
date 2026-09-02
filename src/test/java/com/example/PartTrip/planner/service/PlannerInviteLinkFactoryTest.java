package com.example.PartTrip.planner.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

// 초대 링크는 앱·웹이 그대로 열어야 해서, 주소가 어긋나면 초대 자체가 막힌다.
class PlannerInviteLinkFactoryTest {

    @Test
    void 기준주소와_초대코드를_합친다() {
        PlannerInviteLinkFactory factory =
                new PlannerInviteLinkFactory("http://localhost:5173", new MockEnvironment());

        assertEquals(
                "http://localhost:5173/planner/group?inviteCode=OSK-4821",
                factory.create("OSK-4821"));
    }

    // 실물 기기 시연에서는 맥 IP 로 덮어쓴다. 포트가 붙어도 깨지면 안 된다.
    @Test
    void 아이피와_포트가_들어와도_그대로_쓴다() {
        PlannerInviteLinkFactory factory =
                new PlannerInviteLinkFactory("http://192.168.0.10:5173", new MockEnvironment());

        assertEquals(
                "http://192.168.0.10:5173/planner/group?inviteCode=ABC",
                factory.create("ABC"));
    }

    // 설정값 끝에 / 가 붙어 오면 //planner/group 이 되어 라우트가 안 잡힌다.
    @Test
    void 끝의_슬래시는_떼어낸다() {
        PlannerInviteLinkFactory factory =
                new PlannerInviteLinkFactory("http://localhost:5173///", new MockEnvironment());

        assertEquals(
                "http://localhost:5173/planner/group?inviteCode=ABC",
                factory.create("ABC"));
    }

    @Test
    void 운영환경에서는_https만_허용한다() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("prod");

        assertThatThrownBy(() -> new PlannerInviteLinkFactory(
                "http://parttrip.example", environment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("HTTPS");
    }

    @Test
    void 운영환경의_https주소도_정규화한다() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("production");

        PlannerInviteLinkFactory factory =
                new PlannerInviteLinkFactory("https://parttrip.example///", environment);

        assertEquals(
                "https://parttrip.example/planner/group?inviteCode=ABC",
                factory.create("ABC"));
    }
}
