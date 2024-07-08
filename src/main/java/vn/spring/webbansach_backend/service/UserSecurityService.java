package vn.spring.webbansach_backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserSecurityService implements IUserSecurityService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);

        if(username == null){
            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),roleToAuthorities(user.getRoleList()));
    }

    private Collection<? extends GrantedAuthority> roleToAuthorities(Collection<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

}
