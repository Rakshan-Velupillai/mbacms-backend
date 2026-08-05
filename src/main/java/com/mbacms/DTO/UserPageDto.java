package com.mbacms.DTO;

import com.mbacms.enums.Role;
import com.mbacms.model.User;

import java.util.List;

public record UserPageDto(

        long totalRecords,
        int totalPages,
        List<UserPageElementDto> data

) {
}
