package com.example.PartTrip.notification.event;

public record GroupInvitedEvent(Long groupId, String invitedUserId, String actorUserId) {}
