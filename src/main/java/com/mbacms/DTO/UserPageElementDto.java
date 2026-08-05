package com.mbacms.DTO;

import com.mbacms.enums.Role;

import java.time.Instant;

public record UserPageElementDto(
        int id,
        String fullName,
        String username,
        String email,
        String phoneNumber,
        Role role,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
}
