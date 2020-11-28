package com.ite5year.services;
import com.ite5year.models.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import com.ite5year.repositories.ApplicationUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserDetailsServiceImpl implements UserDetailsService {
    final private ApplicationUserRepository applicationUserRepository;
    final private AuthenticationService authenticationService;
    public ApplicationUserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository, AuthenticationService authenticationService) {
        this.applicationUserRepository = applicationUserRepository;
        this.authenticationService = authenticationService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        authenticationService.setLoggedInUser(user);
        return ApplicationUserDetailsImpl.build(user);
    }
}
