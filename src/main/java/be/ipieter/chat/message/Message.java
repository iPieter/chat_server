package be.ipieter.chat.message;

import be.ipieter.chat.channel.ChannelMember;
import be.ipieter.chat.client.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * A Message is a single exchange between a sender, a {@link ChannelMember}, and
 * a receiver, a {@link Client}.
 *
 * @author Pieter
 * @version 1.0
 */
@Data
@Entity
public class Message
{
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private ChannelMember sender;

    @ManyToOne
    @JsonIgnore
    private Client receiver;

    private String message;

    public Message()
    {
    }

    @Builder
    public Message( ChannelMember sender, Client receiver, String message )
    {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    @Transient
    public String getSenderDisplayName()
    {
        if (sender == null)
            return "Unknown sender";

        return sender.getUser().getDisplayName();
    }
}
