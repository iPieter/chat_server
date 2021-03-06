package be.ipieter.chat.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Pieter
 * @version 1.0
 */
@ResponseStatus( HttpStatus.NOT_FOUND )
public class UsernameNotFoundException extends Exception
{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UsernameNotFoundException()
    {
        super( "Username not found." );
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UsernameNotFoundException( String message )
    {
        super( message );
    }
}
