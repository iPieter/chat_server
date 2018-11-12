package be.ipieter.chat.config;

import be.ipieter.chat.user.User;
import be.ipieter.chat.user.UserComponent;
import be.ipieter.chat.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Collections;

/**
 * @author Pieter
 * @version 1.0
 */
@Configuration
public class DataConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DataConfig.class );

    private UserComponent userComponent;

    @Autowired
    public DataConfig( UserComponent userComponent )
    {
        this.userComponent = userComponent;
    }
}
