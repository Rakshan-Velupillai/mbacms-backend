package com.mbacms.service;


import com.mbacms.DTO.UserRespDto;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.UserMapper;
import com.mbacms.model.User;
import com.mbacms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[" + username + "]");
        System.out.println("Length = " + username.length());
        logger.info("Fetching user details by given username {}", username);
        User user=userRepository.findByUsername(username).orElseThrow(
                ()->new ResourceNotFoundException("Invalid Credentials!")
        );

        logger.info("User Details fetched for user {}", user.getUsername());
        return user;
    }


    public UserRespDto entityToDto(User user) {
        return userMapper.entityToDto(user);
    }
}
