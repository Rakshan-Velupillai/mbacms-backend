package com.mbacms.DTO;

import com.mbacms.enums.Role;

public record UserRoleCountDto(
        Role role,
        Long count
) {
}