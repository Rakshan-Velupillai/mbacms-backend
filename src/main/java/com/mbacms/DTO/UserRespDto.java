package com.mbacms.DTO;


import com.mbacms.enums.Role;

public record UserRespDto(
       
        String fullName,
        String username,
        String email,
        String password,
        String phoneNumber,
        Role role
) {
}
