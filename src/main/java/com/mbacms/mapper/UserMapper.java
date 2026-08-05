package com.mbacms.mapper;

import com.mbacms.DTO.UserPageDto;
import com.mbacms.DTO.UserPageElementDto;
import com.mbacms.DTO.UserRespDto;
import com.mbacms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserRespDto entityToDto(User user){

  return new UserRespDto(
          user.getFullName(),
          user.getUsername(),
          user.getEmail(),
          user.getPassword(),
          user.getPhoneNumber(),
          user.getRole()
  );



    }

    public UserPageDto mapEntityToDto(Page<User> pages){


        long totalElements=pages.getTotalElements();
        int totalPages=pages.getTotalPages();

        List<User> list=pages.getContent();

        List<UserPageElementDto> userPageElementDto=list.stream()
                .map(this::changeToDto)
                .toList();

return new UserPageDto(
        totalElements,
        totalPages,
        userPageElementDto
);
    }


    public UserPageElementDto changeToDto(User user){
        return new UserPageElementDto(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
