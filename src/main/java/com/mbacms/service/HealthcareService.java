package com.mbacms.service;

import com.mbacms.DTO.HealthcareReqDto;
import com.mbacms.DTO.HealthcareRespDto;
import com.mbacms.DTO.UserReqDto;
import com.mbacms.DTO.UserRespDto;
import com.mbacms.enums.Role;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.HealthcareMapper;
import com.mbacms.model.Healthcare;
import com.mbacms.model.User;
import com.mbacms.repository.HealthcareRepository;
import com.mbacms.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HealthcareService {
    private final HealthcareRepository healthcareRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final HealthcareMapper healthcareMapper;
    private final UserRepository userRepository;
    private final AuthService authService;

    public void healthcareProfile(@Valid HealthcareReqDto dto) {
        User user=new User();
        user.setFullName(dto.fullName());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(Role.HEALTHCARE);

        UserRespDto userRespDTO=userService.entityToDto(user);
//        UserReqDto userReqDTO=healthcareMapper.healthcareDtoToUserDto(userRespDTO);

        user=authService.registerActorUser(userRespDTO);

        Healthcare healthcare=new Healthcare();

        healthcare.setHealthcareName(dto.healthcareName());
        healthcare.setSpecialization(dto.specialization());
        healthcare.setLicenseNumber(dto.licenseNumber());
        healthcare.setAddress(dto.address());

        healthcare.setUser(user);

        healthcareRepository.save(healthcare);


    }


    public HealthcareRespDto getHealthcare(String name) {

        Healthcare healthcare=healthcareRepository.findByUserUsername(name).orElseThrow(() ->
                new RuntimeException("Healthcare profile not found"));

        return healthcareMapper.entityToDto(healthcare);
    }

    public Healthcare getHealthcareByName(String name) {
        Healthcare healthcare = healthcareRepository.findByUserUsername(name).orElseThrow(
                ()->new ResourceNotFoundException("Healthcare profile not found")
        );
        return healthcare;
    }

    public HealthcareRespDto updateProfile(String username, HealthcareRespDto dto) {
        Healthcare healthcare = healthcareRepository.findByUserUsername(username).orElseThrow(
                ()->new ResourceNotFoundException("Healthcare profile not found")
        );
        User user = healthcare.getUser();

        if (dto.fullName() != null) user.setFullName(dto.fullName());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.phoneNumber() != null) user.setPhoneNumber(dto.phoneNumber());

        if (dto.healthcareName() != null) healthcare.setHealthcareName(dto.healthcareName());
        if (dto.specialization() != null) healthcare.setSpecialization(dto.specialization());
        if (dto.licenseNumber() != null) healthcare.setLicenseNumber(dto.licenseNumber());
        if (dto.address() != null) healthcare.setAddress(dto.address());

        healthcareRepository.save(healthcare);
        userRepository.save(user);

        return healthcareMapper.entityToDto(healthcare);
    }
}
