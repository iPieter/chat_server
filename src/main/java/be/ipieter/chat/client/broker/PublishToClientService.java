package be.ipieter.chat.client.broker;

import be.ipieter.chat.message.SimpleMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This service posts all given messages to RabbitMQ asynchronously and terminates itself afterwards.
 *
 * @author Pieter
 * @version 1.0
 */
public class PublishToClientService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger( PublishToClientService.class );

    private Channel channel;
    private Exchange exchange;
    private String routingKey;

    private List<SimpleMessage> sendingMessages;

    /**
     * Create a new SendingService, this should happen for every atomic task.
     *
     * @param channel    The rabbitMQ channel to publish the messages to.
     * @param exchange   The exchange to publish to.
     * @param routingKey The key as a String, not needed if the type of exchange doesn't require it.
     */
    public PublishToClientService( Channel channel,
                                   Exchange exchange,
                                   String routingKey ) {
        this.channel = channel;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void setSendingMessages( List<SimpleMessage> sendingMessages ) {
        this.sendingMessages = sendingMessages;
    }

    public void addMessage( SimpleMessage message ) {

        if ( sendingMessages == null ) {
            sendingMessages = new ArrayList<>();
        }

        this.sendingMessages.add( message );
    }

    /**
     * Publishes all messages to rabbitMQ.
     */
    public void run() {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer       = objectMapper.writerFor(SimpleMessage.class);

        Iterator<? extends SimpleMessage> messagesIterator = sendingMessages.iterator();

        while ( messagesIterator.hasNext() ) {

            try {


                channel.basicPublish( exchange.getExchangeName(), routingKey, null, writer.writeValueAsBytes(messagesIterator.next()) );

            } catch ( IOException e ) {
                LOGGER.error( "Error publishing to channel {}: {}", channel, e.getMessage() );
            }
        }

        LOGGER.info( "Finished sending {} messages to exchange {} with key {}.",
                sendingMessages.size(),
                exchange,
                routingKey );

    }

    /**
     * @param exchange
     * @throws IOException
     */
    public void declareExchange( Exchange exchange ) throws IOException {

        if ( channel == null || !channel.isOpen() ) {

            LOGGER.error( "Channel is either null or not open. Threw error." );

            throw new IOException( "Could not connect to channel. Check if it's declared." );
        }

        channel.exchangeDeclare( exchange.getExchangeName(), exchange.getExchangeType(), exchange.isDurable() );
    }
}
