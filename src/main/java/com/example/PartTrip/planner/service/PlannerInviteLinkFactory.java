package com.example.PartTrip.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Component
public class PlannerInviteLinkFactory {

    private final String frontendBaseUrl;

    public PlannerInviteLinkFactory(
            @Value("${app.frontend-base-url:${FRONTEND_BASE_URL:http://localhost:5173}}")
            String frontendBaseUrl,
            Environment environment
    ) {
        boolean production = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equals("prod") || profile.equals("production"));
        if (production
                && !"https".equalsIgnoreCase(URI.create(frontendBaseUrl).getScheme())) {
            throw new IllegalStateException("운영 환경의 프론트엔드 주소는 HTTPS여야 합니다.");
        }
        this.frontendBaseUrl = frontendBaseUrl.replaceAll("/+$", "");
    }

    public String create(String inviteCode) {
        return UriComponentsBuilder.fromUriString(frontendBaseUrl)
                .path("/planner/group")
                .queryParam("inviteCode", inviteCode)
                .build()
                .encode()
                .toUriString();
    }
}
