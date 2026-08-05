package com.mbacms.repository;

import com.mbacms.DTO.UserRoleCountDto;
import com.mbacms.DTO.UserStatDto;
import com.mbacms.enums.Role;
import com.mbacms.model.Healthcare;
import com.mbacms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

//find by fullname
    Optional<User> findByFullName(String fullname);

    //finding and checking the user by username
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    //finding and checking the user by email
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    @Query("""
select u from User u where u.isActive=?1
order by u.createdAt DESC
""")
    Page<User> getAllActive(boolean b, Pageable pageable);


    @Query("""
       select u.role, count(u)
       from User u
       group by u.role
       """)
    List<UserRoleCountDto> getUserRoleStats();

    @Query("""
       select u from User u where u.isActive=?1
       and (?2 is null or ?2 = '' or lower(u.username) like lower(concat('%', ?2, '%')))
       and (?3 is null or u.role = ?3)
       """)
    Page<User> getAllActiveWithSearchAndFilter(boolean isActive, String username, Role role, Pageable pageable);

    @Query("select u.role, count(u) from User u where u.isActive = true group by u.role")
    List<UserRoleCountDto> getActiveUserRoleStats();
}
