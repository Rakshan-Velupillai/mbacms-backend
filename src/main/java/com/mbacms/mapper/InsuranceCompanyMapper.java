package com.mbacms.mapper;

import com.mbacms.DTO.InsuranceCompanyRespDto;
import com.mbacms.DTO.UserReqDto;
import com.mbacms.DTO.UserRespDto;
import com.mbacms.model.InsuranceCompany;
import org.springframework.stereotype.Component;

@Component
public class InsuranceCompanyMapper {

    public InsuranceCompanyRespDto entityToDTO(InsuranceCompany insuranceCompany) {
        return new InsuranceCompanyRespDto(
                insuranceCompany.getId(),
                insuranceCompany.getUser().getUsername(),
                insuranceCompany.getUser().getEmail(),
                insuranceCompany.getCompanyName(),
                insuranceCompany.getRegNo(),
                insuranceCompany.getAddress(),
                insuranceCompany.getUser().getFullName(),
                insuranceCompany.getUser().getPhoneNumber()
        );
    }
    public UserReqDto insuranceDtoToUserDto(UserRespDto dto){

        return new UserReqDto(
                dto.fullName(),
                dto.username(),
                dto.email(),
                dto.password(),
                dto.phoneNumber()
        );
    }
}
