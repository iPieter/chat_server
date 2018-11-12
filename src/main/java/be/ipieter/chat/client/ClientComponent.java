package be.ipieter.chat.client;

import be.ipieter.chat.channel.ChannelComponent;
import be.ipieter.chat.client.broker.BrokerService;
import be.ipieter.chat.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Pieter
 * @version 1.0
 */
@Component
public class ClientComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ClientComponent.class );

    private SecureRandomFactoryBean randomFactory = new SecureRandomFactoryBean();

    private KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private ChannelComponent channelComponent;

    public ClientComponent() throws Exception
    {
        tokenService.setServerSecret( "Bob" );
        tokenService.setServerInteger( 97 );
        tokenService.setSecureRandom( randomFactory.getObject() );
        tokenService.setSecureRandom( new SecureRandom() );
    }

    public Client createClient( User user, String identityKey, String metadata )
    {
        Client client = new Client();
        client.setIdentityKey( identityKey );
        client.setMetadata( metadata );
        client.setUser( user );

        client.setIdentifierKey( ChannelComponent.generateRandomToken() );

        clientRepository.save( client );

        LOGGER.info( "Client {} created, now creating channel on broker.", client.getIdentifierKey() );

        brokerService.createQueueForClient( client.getIdentifierKey() );

        //update client lists
        channelComponent.updateChannelMemberClientLists( user );

        return client;
    }

    public Token createTokenForClient( Client client )
    {
        return tokenService.allocateToken( client.getIdentifierKey() );
    }

    public Client obtainClientFromToken( String token ) throws InvalidTokenException
    {
        //find client
        String clientKey = tokenService.verifyToken( token ).getExtendedInformation();
        Client client    = clientRepository.findClientByIdentifierKey( clientKey );

        if ( client == null )
        {
            throw new InvalidTokenException();
        }

        return client;
    }

    public void persistPrekeyForClientToken( String token, PreKey preKey ) throws InvalidTokenException
    {
        Client client = obtainClientFromToken( token );

        client.getPreKeyList().add( preKey );

        clientRepository.save( client );
    }

    public void deleteClient( Client client ) throws InvalidTokenException
    {
        //delete queue from broker
        brokerService.deleteQeueuForClient( client.getIdentifierKey() );

        //delete client from every user and ChannelMember
        channelComponent.deleteClientFromChannelMembers(client);

        clientRepository.delete( client );
    }

    public User obtainUserFromClientKey( String clientKey )
    {
        return clientRepository.findClientByIdentifierKey( clientKey ).getUser();
    }
}
