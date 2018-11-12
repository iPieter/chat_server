package be.ipieter.chat.client;

import be.ipieter.chat.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * This object is a minimalist representation of a client, along with
 * some encrypted metadata that is only readable for the user.
 *
 * @author Pieter
 * @version 1.0
 */
@Entity
@Data
public class Client
{
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    /**
     * This is a randomly generated identifier.
     */
    private String identifierKey;

    /**
     * This is the public key from the client identity keypair.
     */
    private String identityKey;

    @JsonIgnore
    private String metadata;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<PreKey> preKeyList;

    public Client()
    {
    }

    public Client( String identityKey )
    {
        this.identityKey = identityKey;
    }

    public Client( User user, String identityKey, String metadata )
    {
        this.user = user;
        this.identityKey = identityKey;
        this.metadata = metadata;
    }
}
