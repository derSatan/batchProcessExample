package de.hardtonline.xml2jsonReader.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hardtonline.xml2jsonReader.service.XmlReader;

@RestController
public class Controller {
    private static final String template = "Hello, this is a simple REST service writing a xml file as json into a db";
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private XmlReader xr;
    
    @RequestMapping("/info")
    public String info() {
    	logger.debug("Started info");
        return String.format(template);
    }
    
    @RequestMapping("/readXml")
    public String readXml(@RequestParam(name="fileName")String fileName) {
    	logger.debug("Started readXml");
    	String result = "";
    	try {
			result = xr.getFile(fileName);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			result = "WRONG! - Look at log!";
		}
    	
        return result;
    }
}
