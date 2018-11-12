package be.ipieter.chat.channel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Pieter
 * @version 1.0
 */
@ResponseStatus( HttpStatus.NOT_FOUND )
public class ChannelNotDirectException extends Exception
{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ChannelNotDirectException()
    {
        super( "This channel is not a direct channel." );
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ChannelNotDirectException( String message )
    {
        super( message );
    }
}
