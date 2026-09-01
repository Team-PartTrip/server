package com.example.PartTrip.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PlannerInviteLinkFactory {

    private final String frontendBaseUrl;

    public PlannerInviteLinkFactory(
            @Value("${app.frontend-base-url:${FRONTEND_BASE_URL:http://localhost:5173}}")
            String frontendBaseUrl
    ) {
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
