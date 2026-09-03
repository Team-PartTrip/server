package com.example.PartTrip.global.config;

import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * 응답 gzip 압축.
 *
 * 관광지 목록이 한 번에 60KB 나가는데, 그중 41% 가 구글 사진 주소다
 * (한 줄에 210자쯤 된다). 같은 문자열이 반복돼서 압축이 잘 먹는다.
 * 재보니 60,334 → 24,673 바이트로 60% 가 줄었다.
 *
 * application.properties 는 gitignore 대상이라 거기 적으면 각자 넣어야 한다.
 * 팀 전체에 그대로 적용되도록 코드에 둔다.
 */
@Configuration
public class CompressionConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    /** 이보다 작은 응답은 압축해도 이득이 없다. 헤더가 더 붙는다 */
    private static final DataSize MIN_RESPONSE_SIZE = DataSize.ofKilobytes(1);

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        Compression compression = new Compression();
        compression.setEnabled(true);
        compression.setMimeTypes(new String[] {
                "application/json",
                "text/plain",
                "text/html",
                "text/css",
                "application/javascript",
        });
        compression.setMinResponseSize(MIN_RESPONSE_SIZE);
        factory.setCompression(compression);
    }
}
