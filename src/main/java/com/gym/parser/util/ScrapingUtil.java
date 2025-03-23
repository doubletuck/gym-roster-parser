package com.gym.parser.util;

public class ScrapingUtil {

    public static String[] parseName(String nameText) {
        String[] firstAndLastNameArray = new String[2];
        if (nameText != null && !nameText.isEmpty()) {
            String[] tempArray = nameText.split(" ");
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
