package be.ipieter.chat.client.broker;

import com.rabbitmq.client.BuiltinExchangeType;

/**
 * @author Pieter
 * @version 1.0
 */
public class Exchange
{

    private String exchangeName;

    private BuiltinExchangeType exchangeType;

    private boolean durable;

    public Exchange() {
    }

    public Exchange( String exchangeName, BuiltinExchangeType exchangeType, boolean durable ) {
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.durable = durable;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName( String exchangeName ) {
        this.exchangeName = exchangeName;
    }

    public BuiltinExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType( BuiltinExchangeType exchangeType ) {
        this.exchangeType = exchangeType;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable( boolean durable ) {
        this.durable = durable;
    }
}
