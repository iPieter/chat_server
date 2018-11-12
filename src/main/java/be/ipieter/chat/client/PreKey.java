package be.ipieter.chat.client;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Pieter
 * @version 1.0
 */
@Entity
@Data
public class PreKey
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long clientID;

    private String key;
    private String signature;

    public PreKey()
    {
    }

    public PreKey( Long clientID, String key, String signature )
    {
        this.clientID = clientID;
        this.key = key;
        this.signature = signature;
    }
}
