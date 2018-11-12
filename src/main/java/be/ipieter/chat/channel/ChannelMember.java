package be.ipieter.chat.channel;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an agglomeration of {@link User} and their {@link Client} objects.
 *
 * @author Pieter
 * @version 1.0
 */
@Entity
@Data
public class ChannelMember
{
    @Id
    @JsonIgnore
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToMany( fetch = FetchType.EAGER )
    private List <Client> clients;

    private String channelKey;

    public ChannelMember()
    {
    }

    public ChannelMember( User user, String channelKey )
    {
        this.user = user;
        this.channelKey = channelKey;
        this.clients = new ArrayList <>();
    }

    @Override
    public String toString()
    {
        return "ChannelMember{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }

    public void addClient( Client c )
    {
        if ( clients == null )
            clients = new ArrayList <>();

        clients.add( c );
    }
}
