package be.ipieter.chat.user;

import be.ipieter.chat.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * @author Pieter
 * @version 1.0
 */
@Component
public class UserComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UserComponent.class );

    private UserRepository userRepository;

    @Autowired
    public UserComponent( UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }

    public void saveUser( User user )
    {
        userRepository.save( user );
    }

    /**
     * This function can update the password for an {@link User}. This requires a few checks:
     * <ul>
     * <li>Check if the old password is correct.</li>
     * <li>Check if the old password is not the same as the new password.</li>
     * </ul>
     * When calling this method, all those checks are done and if the password is changed,
     * the method will return true. If the password could not be updated for some reason,
     * it will return false.
     *
     * @param user        The subjected employee.
     * @param oldPassword The old password as a string.
     * @param newPassword The new password as a string.
     * @return True if the password is changed, false otherwise.
     */
    public boolean updatePassword( User user, String oldPassword, String newPassword )
    {

        //Check if the old password is valid
        if ( !BCrypt.checkpw( oldPassword, user.getPassword() ) )
        {
            return false;
        }

        //assert if the old password is the same as the new one.
        Assert.isTrue( !oldPassword.equals( newPassword ), "Old and new password should not be the same." );

        //update the password
        user.setPassword( BCrypt.hashpw( newPassword, BCrypt.gensalt() ) );

        userRepository.save( user );

        return true;
    }

    /**
     * Checks the password for a given user.
     *
     * @param email
     * @param password
     * @return The {@link User} object if found and verified the password.
     * @throws InvalidUsernameOrPasswordException when the user is not found or the password is wrong.
     */
    public User getUserForEmailWithPasswordCheck( String email, String password ) throws InvalidUsernameOrPasswordException
    {
        User user = userRepository.findByEmailIgnoreCase( email );

        if ( user == null || !BCrypt.checkpw( password, user.getPassword() ) )
        {
            throw new InvalidUsernameOrPasswordException();
        }

        return user;
    }

    public List <Client> getClientsForEmail( String email )
    {
        return null;
    }

    public void createUser( String displayName, String email, String password )
            throws AccountAlreadyExistsException
    {
        if ( userRepository.findByEmailIgnoreCase( email ) != null )
        {
            LOGGER.info( "Account with email {} already found.", email );
            throw new AccountAlreadyExistsException( "This username is already registered." );

        }
        User user = new User();
        user.setDisplayName( displayName );
        user.setEmail( email );
        user.setUserRoles( Collections.singleton( UserRole.USER ) );
        user.setPassword( BCrypt.hashpw( password, BCrypt.gensalt() ) );
        user.setProfileImage( "bob" );
        user.setStatus( ":bob: Hi there!" );

        userRepository.save( user );

        LOGGER.info( "Persisted {}.", user );

    }

    /**
     * This procedure adds a client to a user.
     *
     * @param user
     * @param client
     */
    public void registerClient( User user, Client client )
    {
        user.getClientList().add( client );
        userRepository.save( user );
    }

    public User findUserByUsername( String username ) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmailIgnoreCase( username );

        if ( user == null )
            throw new UsernameNotFoundException();

        return user;
    }

    public void deleteClient( Client client )
    {
        client.getUser().getClientList().remove( client );

        userRepository.save( client.getUser() );
    }

    /**
     * Add a new profile image to a {@link User}.
     *
     * @param identifier The identifier of the picture.
     * @param user The {@link User} object of the subjected user.
     */
    public void updateProfileImage( String identifier, User user ) throws UsernameNotFoundException
    {
        LOGGER.info( "Set profile image of {} to {}.", user.getEmail(), identifier );

        user.setProfileImage( identifier );

        userRepository.save( user );
    }
}
