package be.ipieter.chat.config;

import lombok.Data;

@Data
public class Endpoints {

    public Endpoints(Broker broker) {
        this.broker = broker;
    }

    private Broker broker;
}
