package be.ipieter.chat.client.broker;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.config.Endpoints;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Pieter
 * @version 1.0
 */
@Service
public class BrokerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( BrokerService.class );

    private static final Exchange MESSAGES_EXCHANGE = new Exchange( "messages", BuiltinExchangeType.DIRECT, true );

    private Channel channel;


    @Autowired
    public BrokerService(Endpoints endpoints)
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        LOGGER.info( "Broker: {}:{}", endpoints.getBroker().getHostname(), endpoints.getBroker().getAmqpPort() );
        connectionFactory.setHost( endpoints.getBroker().getHostname() );
        connectionFactory.setPort( endpoints.getBroker().getAmqpPort() );
        Connection connection = null;
        try
        {
            connection = connectionFactory.newConnection();
            LOGGER.info( "Created connection: {}", connection.getAddress() );

            channel = connection.createChannel();
            channel.exchangeDeclare(
                    MESSAGES_EXCHANGE.getExchangeName(),
                    MESSAGES_EXCHANGE.getExchangeType(),
                    MESSAGES_EXCHANGE.isDurable() );

            LOGGER.info( "Declared exchange {}", MESSAGES_EXCHANGE.getExchangeName() );

        }
        catch ( IOException | TimeoutException e )
        {
            LOGGER.error( "Failed to connect to broker." );

            e.printStackTrace();
        }
    }

    /**
     * This function creates a queue for a client on the broker.
     *
     * @param clientIdentifier The key (not id) of the client.
     */
    public void createQueueForClient( String clientIdentifier )
    {
        try
        {
            declareQueueWithRoutingKey( MESSAGES_EXCHANGE, clientIdentifier );
        }
        catch ( IOException e )
        {
            LOGGER.error( "Could not declare queue for user." );
        }
    }

    private void declareQueueWithRoutingKey( Exchange exchange, String routingKey ) throws IOException
    {

        String queueName = "";

        try
        {
            queueName = channel.queueDeclare( routingKey, true, false, false, null ).getQueue();
        }
        catch ( IOException e )
        {
            LOGGER.error( "Failed to declare RabbitMQ queue: {}", e.getMessage() );
        }

        try
        {
            channel.queueBind( queueName, exchange.getExchangeName(), routingKey );
        }
        catch ( IOException e )
        {
            LOGGER.error( "Failed to bind key {} to exchange {} with error message: {}",
                    routingKey,
                    exchange.getExchangeName(),
                    e.getMessage() );
        }
    }

    public void deleteQeueuForClient( String clientKey )
    {
        try
        {
            channel.queueDelete( clientKey );
            LOGGER.info( "Queue for client {} correctly deleted.", clientKey );
        }
        catch ( IOException e )
        {
            LOGGER.error( "Failed to delete RabbitMQ queue {}: {}", clientKey, e.getMessage() );
        }
    }

}
