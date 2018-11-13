package be.ipieter.chat.config;

import lombok.Data;

/**
 * Small data structure to contain all endpoints needed to allow chatting.
 *
 * @author Pieter
 * @version 1.0
 */
@Data
public class Broker
{

    private String hostname;
    private int amqpPort;
    private String webstomp;
    private String mqtt;

    public Broker( String hostname, int amqpPort, String webstomp, String mqtt )
    {
        this.hostname = hostname;
        this.amqpPort = amqpPort;
        this.webstomp = webstomp;
        this.mqtt = mqtt;
    }
}