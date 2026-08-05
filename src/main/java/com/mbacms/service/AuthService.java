package com.mbacms.service;


import com.mbacms.DTO.*;
import com.mbacms.enums.ClaimStatus;
import com.mbacms.enums.Role;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.UserMapper;
import com.mbacms.model.User;
import com.mbacms.repository.ClaimRepository;
import com.mbacms.repository.InvoiceRepository;
import com.mbacms.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(UserReqDto dto) {
        if (existsByUsername(dto.username()))
            throw new ResourceNotFoundException("Username already registered in DB!");

        if (existsByEmail(dto.email()))
            throw new ResourceNotFoundException("Email already registered in DB!");


        //1.Extract the password from the dto
        String password = dto.password();

        //2.Encode the password and assign the role
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setFullName(dto.fullName());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(Role.PATIENT);

        //3.save the user in db
        userRepository.save(user);


    }

    public User registerActorUser(UserRespDto dto) {
        if (existsByUsername(dto.username()))
            throw new ResourceNotFoundException("Username already registered in DB!");

        if (existsByEmail(dto.email()))
            throw new ResourceNotFoundException("Email already registered in DB!");



        User user = new User();
        user.setFullName(dto.fullName());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());

        //3.save the user in db
        return userRepository.save(user);


    }

    public void changePassword(String name, @Valid ChangePasswordReqDto dto) {

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword()))
            throw new ResourceNotFoundException("Old password is incorrect");

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    public void adminSignup(@Valid UserReqDto dto) {
        User user = new User();
        user.setFullName(dto.fullName());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }


    public boolean existsByUsername(@NotNull @NotBlank String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(@NotNull @NotBlank String email) {
        return userRepository.existsByEmail(email);
    }


    public void softDelete(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        user.setActive(false);
        userRepository.save(user);
    }


    public UserPageDto getAllUsers(int page, int size, String search, String roleStr, String sortBy, String sortDir) {
        String resolvedSortBy = "createdAt";
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            if ("username".equals(sortBy)) {
                resolvedSortBy = "username";
            } else if ("updatedAt".equals(sortBy)) {
                resolvedSortBy = "updatedAt";
            } else if ("createdAt".equals(sortBy)) {
                resolvedSortBy = "createdAt";
            }
        }

        Sort.Direction direction="desc".equalsIgnoreCase(sortDir)?Sort.Direction.DESC:Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,resolvedSortBy));

        Role role = null;
        if (roleStr != null && !roleStr.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleStr)) {
            try {
                role = Role.valueOf(roleStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore
            }
        }

        Page<User> pages = userRepository.getAllActiveWithSearchAndFilter(
                true,
                search != null ? search.trim() : "",
                role,
                pageable
        );

        return userMapper.mapEntityToDto(pages);
    }




    public UserStatDto getAllUserStat() {

        List<UserRoleCountDto> stats = userRepository.getUserRoleStats();

        List<Role> roles = stats.stream()
                .map(UserRoleCountDto::role)
                .toList();

        List<Long> counts = stats.stream()
                .map(UserRoleCountDto::count)
                .toList();

        return new UserStatDto(roles, counts);
    }

    private final ClaimRepository claimRepository;
    private final InvoiceRepository invoiceRepository;
    public UserStatDto getActiveUserStat() {

        List<UserRoleCountDto> stats = userRepository.getActiveUserRoleStats();

        List<Role> roles = stats.stream()
                .map(UserRoleCountDto::role)
                .toList();

        List<Long> counts = stats.stream()
                .map(UserRoleCountDto::count)
                .toList();


        return new UserStatDto(roles, counts);
    }

    public GeneralStatDto getAdminStatsV1() {
        long claimsCount = claimRepository.count();
        long invoicesCount = invoiceRepository.count();

        List<String> labels = List.of("Claims", "Invoices");
        List<Long> counts = List.of(claimsCount, invoicesCount);

        return new GeneralStatDto(labels, counts);
    }

    public GeneralStatDto getAdminStatsV2() {
        long submittedCount = claimRepository.countByClaimStatus(ClaimStatus.SUBMITTED);
        long rejectedCount = claimRepository.countByClaimStatus(ClaimStatus.REJECTED);
        long approvedCount = claimRepository.countByClaimStatus(ClaimStatus.APPROVED);

        List<String> labels = List.of("Submitted", "Rejected", "Approved");
        List<Long> counts = List.of(submittedCount, rejectedCount, approvedCount);

        return new GeneralStatDto(labels, counts);
    }
}
