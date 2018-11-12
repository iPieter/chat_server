package be.ipieter.chat.message;

import lombok.Builder;
import lombok.Data;

@Data
public class SimpleMessage
{
    private String identifier;
    private String message;

    public SimpleMessage()
    {
    }

    @Builder
    public SimpleMessage( String identifier, String message )
    {
        this.identifier = identifier;
        this.message = message;
    }
}
