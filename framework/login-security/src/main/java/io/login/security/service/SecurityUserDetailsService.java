package io.login.security.service;

import io.login.client.models.UserAuthContext;
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
            loginUser = userRepository.getUserByUsername(username);
        } catch (Exception throwables) {
           throw new RuntimeException(throwables);
        }

        if (loginUser != null) {
            UserDetails userDetails = new User(loginUser.getUsername(),loginUser.getPassword(),
                    new ArrayList<>());
            System.out.println("loginUSer - "+loginUser.getUsername()+"pass - "+loginUser.getPassword());
            return new UserAuthContext(loginUser,userDetails);
        } else {
            throw new UsernameNotFoundException("LoginUser not found with username: " + username);
        }
    }
}
