package com.gym.parser.util;

import com.doubletuck.gym.common.model.Event;

public class EventParser {

    public static String parse(String text) {

        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        // Split the event text using delimiters "," and "/".
        String[] eventList = text.split("[,/]");

        boolean hasAA = false, hasVT = false, hasUB = false, hasBB = false, hasFX = false;

        for (String eventSegment : eventList) {
            Event event = Event.find(eventSegment);
            if (event != null) {
                switch (event) {
                    case AA -> hasAA = true;
                    case VT -> hasVT = true;
                    case UB -> hasUB = true;
                    case BB -> hasBB = true;
                    case FX -> hasFX = true;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if (hasAA) sb.append(Event.AA.name());
        if (hasVT) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Event.VT.name());
        }
        if (hasUB) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Event.UB.name());
        }
        if (hasBB) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Event.BB.name());
        }
        if (hasFX) {
            if (!sb.isEmpty()) sb.append(',');
            sb.append(Event.FX.name());
        }

        return sb.isEmpty() ? null : sb.toString();
    }
}
