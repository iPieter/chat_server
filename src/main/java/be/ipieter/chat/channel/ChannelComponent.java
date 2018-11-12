package be.ipieter.chat.channel;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.user.User;
import be.ipieter.chat.user.UserComponent;
import be.ipieter.chat.user.UsernameNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pieter
 * @version 1.0
 */
@Component
public class ChannelComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ChannelComponent.class );

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private UserComponent userComponent;

    public ChannelComponent()
    {
    }

    public void addUserToChannel( User user, String channelKey ) throws ChannelNotFoundException
    {
        Channel channel = channelRepository.findChannelByKey( channelKey );

        //first check if the user is not already in the channel
        for (ChannelMember ch : channelMemberRepository.findAllByUser( user ))
        {
            if (channel.getMembers().contains( ch ))
                return;
        }

        ChannelMember channelMember = new ChannelMember( user, channelKey );

        for ( Client c : user.getClientList() )
            channelMember.addClient( c );

        LOGGER.info( "Created channelmember: {} for {}", channelMember, channelKey );


        if ( channel == null )
        {
            LOGGER.warn( "Channel {} not found.", channelKey );
            throw new ChannelNotFoundException();
        }

        channelMemberRepository.save( channelMember );

        channel.addMember( channelMember );
        channelRepository.save( channel );

        LOGGER.info( "{} added to channel id = {}", user.getDisplayName(), channel.getId() );
    }

    public void addUserToChannel( String username, String channelKey ) throws ChannelNotFoundException, UsernameNotFoundException
    {
        User user = userComponent.findUserByUsername( username );

        addUserToChannel( user, channelKey );
    }

    /**
     * Generate a Base64 string.
     *
     * @return
     */
    public static String generateRandomToken()
    {
        SecureRandom random = new SecureRandom();
        byte[]       bytes  = new byte[ 24 ];

        random.nextBytes( bytes );

        return Base64Utils.encodeToUrlSafeString( bytes );
    }

    /**
     * Creates a private group channel, generates a key and returns it.
     *
     * @param name
     * @param creator
     * @return
     */
    public Channel createGroupChannel( String name, User creator )
    {
        Channel channel = createChannel( name, ChannelType.PRIVATE, creator );

        return channel;
    }

    public Channel createDirectChannel( User creator, String otherUsername ) throws UsernameNotFoundException
    {
        User user = userComponent.findUserByUsername( otherUsername );

        Channel channel = createChannel( creator.getDisplayName() + " and " + user.getDisplayName(), ChannelType.DIRECT, creator );

        //check if the user wants to talk to himself
        if (user.equals( creator )) {
            LOGGER.info( "User want to create channel with him/herself" );
            channel.setName( creator.getDisplayName() );
            channel.setChannelType( ChannelType.SELF );
        } else
        {
            try
            {
                addUserToChannel( user, channel.getKey() );
            }
            catch ( ChannelNotFoundException e )
            {
                LOGGER.error( "Just created channel not found. Something very wrong... {}", e.getMessage() );
            }
        }

        return channel;
    }

    public Iterable <Channel> getAllChannels()
    {
        return channelRepository.findAll();
    }

    private Channel createChannel( String name, ChannelType type, User creator )
    {
        Channel channel = new Channel();
        channel.setChannelType( type );
        channel.setName( name );
        channel.setCreationDate( new Date() );

        channel.setMembers( new ArrayList <>() );

        String key = generateRandomToken();
        channel.setKey( key );

        channelRepository.save( channel );

        try
        {
            LOGGER.info( "Trying to add creator to channel." );
            addUserToChannel( creator, key );
        }
        catch ( ChannelNotFoundException e )
        {
            LOGGER.error( "Just created channel not found. Something very wrong... {}", e.getMessage() );
        }

        return channel;
    }

    public List <Client> getListClientsForChannel( String channelKey ) throws ChannelNotFoundException
    {
        try
        {
            List <ChannelMember> members = channelRepository.findChannelByKey( channelKey ).getMembers();

            LOGGER.info( "Generating clients list for channel {}", channelKey );

            //remove excludeUser and return list of others
            List <Client> clients = new ArrayList <>();

            for ( ChannelMember cm : members )
            {
                clients.addAll( cm.getClients() );
            }

            return clients;

        }
        catch ( NullPointerException e )
        {
            LOGGER.warn( "Channel {} not found.", channelKey );
            throw new ChannelNotFoundException();
        }
    }

    public Map <String, Client> getAllClientsForChannel( String channelKey, User excludeUser ) throws ChannelNotFoundException
    {
        try
        {
            List <ChannelMember> members = channelRepository.findChannelByKey( channelKey ).getMembers();

            LOGGER.info( "Generating clients list for channel {}", channelKey );

            //remove excludeUser and return list of others
            List <Client> clients = new ArrayList <>();

            for ( ChannelMember cm : members )
            {
                if ( !cm.getUser().equals( excludeUser ) )
                {
                    clients.addAll( cm.getClients() );
                }
            }

            return clients.stream()
                    .collect( Collectors.toMap( client -> client.getId().toString(), c -> c ) );

        }
        catch ( NullPointerException e )
        {
            LOGGER.warn( "Channel {} not found.", channelKey );
            throw new ChannelNotFoundException();
        }


    }

    public List <Channel> getChannelsForUser( User user )
    {
        List <Channel> channels = new ArrayList <>();

        channelMemberRepository.findAllByUser( user ).forEach( channelMember ->
        {
            channels.add( channelRepository.findChannelByKey( channelMember.getChannelKey() ) );
        } );

        return channels;
    }


    /**
     * When a new client is added, the client list should be updated for all
     * the channels that are linked to that client. Therefore, this updates
     * all those lists.
     *
     * @param user
     */
    public void updateChannelMemberClientLists( User user )
    {
        LOGGER.info( "Updating clients for user {}", user.getEmail() );
        channelMemberRepository.findAllByUser( user ).forEach( channelMember ->
        {
            LOGGER.info( "Updating channel member {} for user {}.", channelMember.getId(), user.getEmail() );

            channelMember.setClients( new ArrayList <>() );

            for ( Client c : user.getClientList() )
            {
                LOGGER.info( "Adding client {} to {}", c.getIdentifierKey(), channelMember.getId() );
                channelMember.addClient( c );
            }
            channelMemberRepository.save( channelMember );

        } );
    }

    public void deleteClientFromChannelMembers( Client client )
    {
        channelMemberRepository.findAllByUser( client.getUser() ).forEach( channelMember ->
        {
            channelMember.getClients().remove( client );
            channelMemberRepository.save( channelMember );
        } );

        userComponent.deleteClient( client );
    }

    public User getOtherUserOfDirectChannel( User user, String channelKey ) throws ChannelNotDirectException, ChannelNotFoundException
    {
        LOGGER.info( "{} requested the user info of the other user of channel {}.", user.getEmail(), channelKey );

        Channel channel = channelRepository.findChannelByKey( channelKey );

        if ( channel.getChannelType() != ChannelType.DIRECT )
        {
            throw new ChannelNotDirectException();
        }

        User other = null;

        for ( ChannelMember channelMember : channel.getMembers() )
        {
            if ( other == null && !channelMember.getUser().equals( user ) )
            {
                other = channelMember.getUser();
            }
            else if ( other != null && !channelMember.getUser().equals( user ) )
            {
                throw new ChannelNotFoundException();
            }
        }

        return user; //:sensible-chuckle:
    }
}
