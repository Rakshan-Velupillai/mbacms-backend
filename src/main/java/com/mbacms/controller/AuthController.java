package com.mbacms.controller;


import com.mbacms.DTO.*;
import com.mbacms.model.User;
import com.mbacms.service.*;
import com.mbacms.util.JwtUtility;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

    private final JwtUtility jwtUtility;
    private final UserService userService;
    private final AuthService authService;



    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserReqDto dto){

        authService.registerUser(dto);
    }

    @PostMapping("/admin/signup")
    public void adminSignup(@Valid @RequestBody UserReqDto dto) {
        authService.adminSignup(dto);
    }



    @GetMapping("/login")
    public TokenDto login(Principal principal){
        String username = principal.getName();
        String token = jwtUtility.generateToken(username);
        return new TokenDto(username,token);
    }

    @GetMapping("/user-details")
    public LoginRespDto getUserDetails(Principal principal){

        String loggedInUsername=principal.getName();
        User user=(User)userService.loadUserByUsername(loggedInUsername);

        return new LoginRespDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

    }



    @PatchMapping("/change-password")
    public void changePassword(Principal principal, @Valid @RequestBody ChangePasswordReqDto dto) {
        authService.changePassword(principal.getName(), dto);
    }

    @DeleteMapping("/soft-delete/{id}")
    public void softDelete(@PathVariable int id){
        authService.softDelete(id);
    }

    @GetMapping("/all-users")
    public UserPageDto getAllUsers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir){
        return authService.getAllUsers(page, size, search, role, sortBy, sortDir);
    }


    @GetMapping("/user-stat")
    public UserStatDto getAllUserStat(){
        return authService.getAllUserStat();
    }

    @GetMapping("/user-stat/v1")
    public UserStatDto getAllUserStatV1(){
        return authService.getAllUserStat();
    }

    @GetMapping("/user-stat/v2")
    public UserStatDto getActiveUserStatV2(){
        return authService.getActiveUserStat();
    }

    @GetMapping("/admin-stats/v1")
    public GeneralStatDto getAdminStatsV1(){
        return authService.getAdminStatsV1();
    }

    @GetMapping("/admin-stats/v2")
    public GeneralStatDto getAdminStatsV2(){
        return authService.getAdminStatsV2();
    }
}
