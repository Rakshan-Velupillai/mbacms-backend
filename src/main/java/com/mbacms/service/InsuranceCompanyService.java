package com.mbacms.service;

import com.mbacms.DTO.InsuranceCompanyReqDto;
import com.mbacms.DTO.InsuranceCompanyRespDto;
import com.mbacms.DTO.UserReqDto;
import com.mbacms.DTO.UserRespDto;
import com.mbacms.enums.Role;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.InsuranceCompanyMapper;
import com.mbacms.model.InsuranceCompany;
import com.mbacms.model.User;
import com.mbacms.repository.InsuranceCompanyRepository;
import com.mbacms.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InsuranceCompanyService {


    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthService authService;
    private final InsuranceCompanyMapper companyMapper;
    private final InsuranceCompanyRepository companyRepository;
    private final UserRepository userRepository;

    public void completeProfile(@Valid InsuranceCompanyReqDto dto) {

        User user=new User();
        user.setFullName(dto.fullName());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(Role.INSURANCE_COMPANY);

        UserRespDto userRespDTO=userService.entityToDto(user);
//        UserReqDto userReqDTO=companyMapper.insuranceDtoToUserDto(userRespDTO);

        user=authService.registerActorUser(userRespDTO);

        InsuranceCompany insuranceCompany=new InsuranceCompany();

        insuranceCompany.setCompanyName(dto.companyName());
        insuranceCompany.setRegNo(dto.regNo());
        insuranceCompany.setAddress(dto.address());
        insuranceCompany.setUser(user);

        companyRepository.save(insuranceCompany);
    }

    public InsuranceCompanyRespDto getProfile(String username) {
        InsuranceCompany company = companyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company profile not found");
        }
        return companyMapper.entityToDTO(company);
    }

    public InsuranceCompanyRespDto updateProfile(String username, InsuranceCompanyRespDto dto) {
        InsuranceCompany company = companyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company profile not found");
        }
        User user = company.getUser();

        if (dto.fullName() != null) user.setFullName(dto.fullName());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.phoneNumber() != null) user.setPhoneNumber(dto.phoneNumber());

        if (dto.companyName() != null) company.setCompanyName(dto.companyName());
        if (dto.regNo() != null) company.setRegNo(dto.regNo());
        if (dto.address() != null) company.setAddress(dto.address());

        companyRepository.save(company);
        userRepository.save(user);

        return companyMapper.entityToDTO(company);
    }
}


