package com.mbacms.DTO;

import com.mbacms.enums.Role;

import java.util.List;

public record UserStatDto(
        List<Role> role,
        List<Long> count
) {
}
