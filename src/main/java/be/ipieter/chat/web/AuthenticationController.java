package be.ipieter.chat.web;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.client.ClientComponent;
import be.ipieter.chat.user.AccountAlreadyExistsException;
import be.ipieter.chat.user.InvalidUsernameOrPasswordException;
import be.ipieter.chat.user.User;
import be.ipieter.chat.user.UserComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.Token;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
public class AuthenticationController
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AuthenticationController.class );

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ClientComponent clientComponent;

    public AuthenticationController()
    {
    }

    @RequestMapping( value = "/signup", method = RequestMethod.POST )
    public ResponseEntity signup( @RequestParam String displayName,
                                  @RequestParam String email,
                                  @RequestParam String password ) throws AccountAlreadyExistsException
    {
        if ( StringUtils.isEmpty( displayName ) || StringUtils.isEmpty( email ) || StringUtils.isEmpty( password ) )
        {
            return ResponseEntity.badRequest().build();
        }

        LOGGER.info( "Received request to create user" );

        userComponent.createUser( displayName, email, password );

        return ResponseEntity.ok().build();

    }


    @RequestMapping( value = "/login", method = RequestMethod.POST )
    public Token registerClient( @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String identityKey,
                                 @RequestParam( required = false ) String clientMetadata ) throws InvalidUsernameOrPasswordException
    {
        LOGGER.info( "Requesting token for {}", email );

        User user = userComponent.getUserForEmailWithPasswordCheck( email, password );

        if ( StringUtils.isEmpty( clientMetadata ) )
        {
            LOGGER.info( "Metadata is empty. Proceeding as normal." );
        }
        else
        {
            LOGGER.info( "Metadata is not empty. Persisting/updating and afterwards proceeding as normal." );
        }

        //registering client
        Client client = clientComponent.createClient( user, identityKey, clientMetadata );

        //obtaining token
        return clientComponent.createTokenForClient( client );

    }

    @RequestMapping( value = "/password/forgot" )
    public ResponseEntity passwordForgot( @RequestParam String email )
    {
        LOGGER.info( "Requesting password forgot email for {}", email );
        return ResponseEntity.ok().build();
    }
}
