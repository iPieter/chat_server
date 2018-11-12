package be.ipieter.chat.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Pieter
 * @version 1.0
 */
@Entity
@Data
public class Channel
{
    @Id
    @JsonIgnore
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    private String key;

    private String name;

    @JsonIgnore
    @Temporal( TemporalType.TIMESTAMP )
    private Date creationDate;

    @Enumerated( EnumType.STRING )
    private ChannelType channelType;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    private List <ChannelMember> members;

    public Channel()
    {
    }

    public void addMember( ChannelMember channelMember )
    {
        members.add( channelMember );
    }
}
