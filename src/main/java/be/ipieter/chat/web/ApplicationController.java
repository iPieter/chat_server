package be.ipieter.chat.web;

import be.ipieter.chat.client.ClientComponent;
import be.ipieter.chat.config.Endpoints;
import be.ipieter.chat.user.AccountAlreadyExistsException;
import be.ipieter.chat.user.UserComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
public class ApplicationController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ApplicationController.class );

    @Autowired
    private Endpoints endpoints;

    public ApplicationController()
    {
    }

    @RequestMapping( value = "/endpoints", method = RequestMethod.GET )
    public Endpoints getEndpoints()
    {

        return endpoints;

    }

}
