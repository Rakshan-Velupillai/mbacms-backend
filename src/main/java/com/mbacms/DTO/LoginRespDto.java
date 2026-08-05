package com.mbacms.DTO;

import com.mbacms.enums.Role;

public record LoginRespDto(
        int id,
        String username,
        Role role
) {
}
