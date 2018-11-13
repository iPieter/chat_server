package be.ipieter.chat.channel;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.client.ClientComponent;
import be.ipieter.chat.client.InvalidTokenException;
import be.ipieter.chat.client.PreKey;
import be.ipieter.chat.config.StorageStrategy;
import be.ipieter.chat.user.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
@RequestMapping( "/channels" )
public class ChannelController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ChannelController.class );

    @Autowired
    private ChannelComponent channelComponent;

    @Autowired
    private ClientComponent clientComponent;

    @Autowired
    private StorageStrategy imageStorage;

    public ChannelController()
    {
    }

    @RequestMapping( value = "/{channelKey}/join", method = RequestMethod.POST )
    public ResponseEntity joinChannel( @PathVariable String channelKey,
                                       @RequestParam String username,
                                       @RequestParam String token ) throws InvalidTokenException, ChannelNotFoundException, UsernameNotFoundException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "Client {} of user {} wants {} to join channel {}", client.getId(), client.getUser().getDisplayName(), username, channelKey );

        channelComponent.addUserToChannel( username, channelKey );

        return ResponseEntity.ok().build();

    }

    @RequestMapping( value = "/{channelKey}/clients", method = RequestMethod.POST )
    public ResponseEntity getListOfClients( @PathVariable String channelKey,
                                            @RequestParam String token ) throws InvalidTokenException, ChannelNotFoundException, UsernameNotFoundException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "{} requested list of clients.", client.getUser().getEmail() );

        return ResponseEntity.ok().body( channelComponent.getListClientsForChannel( channelKey ) );

    }

    @RequestMapping( value = "/group", method = RequestMethod.POST )
    public ResponseEntity createChannel( @RequestParam String token,
                                         @RequestParam String name ) throws InvalidTokenException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "Client {} of user {} wants create a channel with name {}.", client.getId(), client.getUser().getDisplayName(), name );

        Channel channel = channelComponent.createGroupChannel( name, client.getUser() );

        return ResponseEntity.ok().body( channel );

    }


    @RequestMapping( value = "/private", method = RequestMethod.POST )
    public ResponseEntity createPrivateChannel( @RequestParam String token,
                                                @RequestParam String username ) throws InvalidTokenException, UsernameNotFoundException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "Client {} of user {} wants create a private channel with {}.", client.getId(), client.getUser().getDisplayName(), username );

        Channel channel = channelComponent.createDirectChannel( client.getUser(), username );

        return ResponseEntity.ok().body( channel );

    }

    @RequestMapping( value = "/all", method = RequestMethod.POST )
    public Iterable <Channel> getChannels( @RequestParam String token ) throws InvalidTokenException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        LOGGER.info( "{} requested a list of channels.", client.getUser().getEmail() );

        return channelComponent.getChannelsForUser( client.getUser() );
    }

    @RequestMapping( value = "/{channelKey}/users/other", method = RequestMethod.POST )
    public ResponseEntity getOtherUserForDirectChannel( @RequestParam String token,
                                                        @PathVariable String channelKey )
            throws InvalidTokenException, ChannelNotFoundException, ChannelNotDirectException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        return ResponseEntity.ok( channelComponent.getOtherUserOfDirectChannel( client.getUser(), channelKey ) );
    }

    @RequestMapping( value = "/{channelKey}/users/other/image", method = RequestMethod.POST )
    public ResponseEntity getOtherUserImageForDirectChannel( @RequestParam String token,
                                                        @PathVariable String channelKey )
            throws InvalidTokenException, ChannelNotFoundException, ChannelNotDirectException, FileNotFoundException
    {
        Client client = clientComponent.obtainClientFromToken( token );

        String image = channelComponent.getOtherUserOfDirectChannel( client.getUser(), channelKey ).getProfileImage();

        return ResponseEntity.ok(imageStorage.getInputStream( image ));
    }


}
