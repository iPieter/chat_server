package be.ipieter.chat.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Pieter
 * @version 1.0
 */
@ResponseStatus( HttpStatus.FORBIDDEN )
public class InvalidTokenException extends Exception
{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public InvalidTokenException()
    {
        super( "Token is either invalid or expired. Create an account or log in." );
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidTokenException( String message )
    {
        super( message );
    }
}
