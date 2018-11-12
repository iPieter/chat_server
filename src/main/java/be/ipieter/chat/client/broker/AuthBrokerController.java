package be.ipieter.chat.client.broker;

import be.ipieter.chat.client.Client;
import be.ipieter.chat.client.ClientComponent;
import be.ipieter.chat.client.InvalidTokenException;
import be.ipieter.chat.user.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
@RequestMapping(path = "/users/auth", method = { RequestMethod.GET, RequestMethod.POST })
public class AuthBrokerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthBrokerController.class);

    @Autowired
    private ClientComponent clientComponent;

    public AuthBrokerController()
    {
    }

    @RequestMapping("user")
    public String user(@RequestParam("username") String clientkey,
                       @RequestParam("password") String token) {
        LOGGER.info("Trying to authenticate user {}", clientkey);

        try
        {
            Client client = clientComponent.obtainClientFromToken( token );
            return "allow " + collectionToDelimitedString(asList("administrator", "management"), " ");

        }
        catch ( InvalidTokenException e )
        {
            return "deny";
        }
    }

    @RequestMapping("vhost")
    public String vhost(VirtualHostCheck check) {
        LOGGER.info("Checking vhost access with {}", check);
        return "allow";
    }

    @RequestMapping("resource")
    public String resource(ResourceCheck check) {
        LOGGER.info("Checking resource access with {}", check);
        return "allow";
    }

    @RequestMapping("topic")
    public String topic(TopicCheck check) {
        LOGGER.info("Checking topic access with {}", check);
        return check.getRouting_key().startsWith("a") ? "allow" : "deny";
    }
}
