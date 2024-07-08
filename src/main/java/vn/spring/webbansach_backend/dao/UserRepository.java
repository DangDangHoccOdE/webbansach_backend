package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.User;
import java.util.*;
@RepositoryRestResource(path="users")
public interface UserRepository extends JpaRepository<User,Integer> {
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);
    User findByUserName(String userName);
    User findByEmail(String email);
    User findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(u)>0 THEN TRUE ELSE false END FROM User  u WHERE u.userName=:username AND u.isActive=true")
    Boolean existsByUserNameAndActiveIsTrue(@Param("username") String username);
}
