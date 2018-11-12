package be.ipieter.chat.client;

import be.ipieter.chat.user.AccountAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
@RequestMapping( "/clients" )
public class ClientController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ClientController.class );

    @Autowired
    private ClientComponent clientComponent;

    public ClientController()
    {
    }

    @RequestMapping( value = "/prekey", method = RequestMethod.POST )
    public ResponseEntity registerPrekey( @RequestParam String token,
                                          @RequestParam Long prekeyID,
                                          @RequestParam String key,
                                          @RequestParam String signature ) throws InvalidTokenException
    {

        PreKey preKey = new PreKey( prekeyID, key, signature );

        clientComponent.persistPrekeyForClientToken( token, preKey );

        LOGGER.info( "Added signed prekey to keystore." );

        return ResponseEntity.ok().build();

    }

    @RequestMapping( value = "/logout", method = RequestMethod.POST )
    public ResponseEntity logout( @RequestParam String token ) throws InvalidTokenException
    {

        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "{} logged out from client {}.", client.getUser().getEmail(), client.getIdentifierKey() );

        clientComponent.deleteClient( client );

        return ResponseEntity.ok().build();

    }

    @RequestMapping( value = "/{clientKey}/user", method = RequestMethod.POST )
    public ResponseEntity getUser( @PathVariable String clientKey,
                                   @RequestParam String token ) throws InvalidTokenException
    {

        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "Client {} ({}) requested user information for client {}.",
                client.getIdentifierKey(),
                client.getUser().getEmail(),
                clientKey );

        return ResponseEntity.ok( clientComponent.obtainUserFromClientKey( clientKey ) );

    }

}
