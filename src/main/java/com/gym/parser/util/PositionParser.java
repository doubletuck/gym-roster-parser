package com.gym.parser.util;

import com.gym.parser.model.Position;

public class PositionParser {

    public static String parse(String text) {

        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        // The following with split the position string using
        // the following delimiters: , and /
        String[] positionList = text.split("[,/]");

        boolean hasAA = false, hasVT = false, hasUB = false, hasBB = false, hasFL = false;

        for (String positionSegment : positionList) {
            Position position = Position.find(positionSegment);
            if (position != null) {
                switch (position) {
                    case AA -> hasAA = true;
                    case VT -> hasVT = true;
                    case UB -> hasUB = true;
                    case BB -> hasBB = true;
                    case FL -> hasFL = true;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if (hasAA) sb.append(Position.AA.name());
        if (hasVT) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Position.VT.name());
        }
        if (hasUB) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Position.UB.name());
        }
        if (hasBB) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Position.BB.name());
        }
        if (hasFL) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Position.FL.name());
        }

        return sb.isEmpty() ? null : sb.toString();
    }
}
