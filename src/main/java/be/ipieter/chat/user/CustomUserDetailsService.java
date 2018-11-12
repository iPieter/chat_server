package be.ipieter.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Pieter
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService
{
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService( UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername( String s )
    {
        //find the employee
        User user = userRepository.findByEmailIgnoreCase( s );

        return new org.springframework.security.core.userdetails.User( user.getEmail(), user.getPassword(), Collections.singletonList( UserRole.USER ) );
    }
}
