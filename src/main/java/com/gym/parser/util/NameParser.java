package com.gym.parser.util;

/**
 * Parses text for a first and last name.
 */
public class NameParser {

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

        if (nameText == null) return firstAndLastNameArray;

        nameText = nameText.trim();
        if (!nameText.isEmpty()) {
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
        }

        return firstAndLastNameArray;
    }
}
