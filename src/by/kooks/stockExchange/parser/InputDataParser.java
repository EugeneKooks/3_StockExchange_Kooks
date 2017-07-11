package by.kooks.stockExchange.parser;

import by.kooks.stockExchange.exception.WrongDataException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputDataParser {

    static final Logger LOGGER = LogManager.getLogger(InputDataParser.class);
    public static Map<String, Integer> parseList (List<String> companiesList) throws WrongDataException {
        Map<String, Integer> companies = new HashMap<>();
        for (String s : companiesList) {
            String [] readedLine = s.split(" ");
            if (readedLine.length==2){
                if (readedLine[0]!=null&&readedLine[1]!=null){
                    String key = readedLine[0].trim();
                    Integer value = Integer.parseInt(readedLine[1].trim());
                    companies.put(key,value);
                } else {
                    LOGGER.log(Level.ERROR, "Invalid data in the file.");
                }
            }
            else {
                LOGGER.log(Level.ERROR, "Wrong data quantity in the file.");
            }
        }
        return companies;
    }
}
