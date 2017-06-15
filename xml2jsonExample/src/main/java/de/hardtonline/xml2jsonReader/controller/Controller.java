package de.hardtonline.xml2jsonReader.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service creating a pdf file with FOP";
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    @RequestMapping("/info")
    public String info() {
    	LOGGER.debug("Started info");
        return String.format(template);
    }
}
