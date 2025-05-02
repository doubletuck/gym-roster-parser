package com.gym.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses text for a first and last name.
 */
public class NameParser {

    private final static Logger logger = LoggerFactory.getLogger(NameParser.class);

    /**
     * Given a string, parses it for a first and last name.
     *
     * The name is assumed to be formatted as first name followed by
     * the last name with white space delimiting them from one another.
     * The result will be returned in a two element String array. The
     * first element contains the first name and the second element
     * contains the last name.
     *
     * @param   nameText The string to parse for the first and last
     *          name.
     * @return  A two element string array. The first element is the
     *          first name and the second element is the last name.
     *          If nameText is null or empty, then the returned two
     *          element array will have null values. If only one name
     *          is found, then that value will be placed in the first
     *          name element and the last name element will be null.
     */
    public static String[] parse(String nameText) {
        String[] firstAndLastNameArray = new String[2];

        if (nameText == null || nameText.isBlank()) return firstAndLastNameArray;

        nameText = nameText.trim();
        String[] tempArray = nameText.split("\\s+");
        if (tempArray.length == 1) {
            firstAndLastNameArray[0] = tempArray[0];
        } else if (tempArray.length == 2) {
            firstAndLastNameArray = tempArray;
        } else {
            StringBuilder builder = new StringBuilder();
            for(int i=0; i<tempArray.length-1; i++) {
                if (i > 0) builder.append(" ");
                builder.append(tempArray[i]);
            }
            firstAndLastNameArray[0] = builder.toString();
            firstAndLastNameArray[1] = tempArray[tempArray.length - 1];
        }

        return firstAndLastNameArray;
    }

    public static String[] parseLastNameFirst(String nameText) {
        String[] firstAndLastNameArray = new String[2];

        if (nameText == null || nameText.isBlank()) return firstAndLastNameArray;

        String[] tempArray = nameText.split(",");
        if (tempArray.length == 1) {
            firstAndLastNameArray[0] = tempArray[0].trim();
        } else {
            firstAndLastNameArray[0] = tempArray[1].trim();
            firstAndLastNameArray[1] = tempArray[0].trim();
            if (tempArray.length > 2) {
                logger.warn("The name passed in is: '{}'. The name format is expected to be: '<lastName>, <firstName>'. Because the name had more delimiters than expected some values are ignored. Setting first name = '{}' and last name = '{}'.",
                        nameText,
                        firstAndLastNameArray[0],
                        firstAndLastNameArray[1]);
            }
        }
        return firstAndLastNameArray;
    }
}
