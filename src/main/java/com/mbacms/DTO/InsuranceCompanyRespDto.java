package com.mbacms.DTO;

public record InsuranceCompanyRespDto(
        int id,
        String username,
        String email,
        String companyName,
        String regNo,
        String address,
        String fullName,
        String phoneNumber
) {
}
