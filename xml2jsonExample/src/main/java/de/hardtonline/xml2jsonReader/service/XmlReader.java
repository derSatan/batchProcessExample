package de.hardtonline.xml2jsonReader.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class XmlReader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${file.inFolder}")
	private String inFolder;
	
	private static final int PRETTY_PRINT_INDENT_FACTOR = 4;
	
	public String getFile(String fileName) throws JSONException, IOException {
		String filePath = inFolder + fileName;
		
		// First, we read the file at whole
		// TODO: memory optimization, since it is all loaded into memory
		logger.debug("Reading >" + filePath + "<");
		String xml = new String(Files.readAllBytes(Paths.get(filePath)));
		
	    // Then we create the json
		JSONObject xmlJSONObj = XML.toJSONObject(xml);
		
		// And for now, we print it out
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        System.out.println(jsonPrettyPrintString);
        
		return "Reading >" + inFolder + File.separator + fileName + "<";
	}
}
