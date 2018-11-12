package be.ipieter.chat.message;

import lombok.Data;

import java.util.Set;

/**
 * @author Pieter
 * @version 1.0
 */
@Data
public class ReceivedMessageGroup
{
    private String sendingToken;

    private Set<SimpleMessage> messages;

    public ReceivedMessageGroup()
    {
    }

}
