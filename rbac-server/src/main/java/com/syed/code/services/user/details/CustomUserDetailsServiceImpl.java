package com.syed.code.services.user.details;

import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.entities.user.User;
import com.syed.code.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getVerificationLoadedUser(username);
        CustomUserDetails userDetails = null;
        if (user != null) {
            userDetails = new CustomUserDetails();
            userDetails.setId(user.getId());
            userDetails.setKey(user.getKey());
            userDetails.setUsername(user.getUsername());
            userDetails.setPassword(user.getUserVerificationInfo().getHash());
            userDetails.setRoles(user.getRoles());
        }
        return userDetails;
    }
}
