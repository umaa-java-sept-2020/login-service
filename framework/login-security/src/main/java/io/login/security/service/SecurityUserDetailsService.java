package io.login.security.service;

import io.login.client.models.UserStatus;
import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
@Primary
public class SecurityUserDetailsService implements UserDetailsService {

    private IUserRepository userRepository;

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser loginUser = null;
        try {
            loginUser = new LoginUser();//userRepository.getUserByUsername(username);
            loginUser.setUsername("user1");
            loginUser.setPassword("pass1");
            loginUser.setUserStatus(UserStatus.ACTIVE);
        } catch (Exception throwables) {
           throw new RuntimeException(throwables);
        }

        if (loginUser != null) {
            return new User(loginUser.getUsername(),loginUser.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("LoginUser not found with username: " + username);
        }
    }
}
