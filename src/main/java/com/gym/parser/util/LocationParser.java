package com.gym.parser.util;

import com.gym.parser.model.Country;
import com.gym.parser.model.State;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Parses a single string that contains a location a containing combination of
 * city, state and country. For example:
 *
 * <ul>
 *     <li>San Jose, Calif.</li>
 *     <li>Toronto, Canada</li>
 *     <li>Whitby, Ontario, Canada</li>
 * </ul>
 *
 * The location segments are parsed and assigned to one or more of the town,
 * state and country class variables.
 *
 * The parsing makes the following assumptions:
 * <ul>
 *     <li>The comma is used as a separator to distinguish the different
 *     location segments.</li>
 *     <li>If a country value exists in the location string, then it will
 *     follow any given town or state value.</li>
 *     <li>If a state value exists in the location string, then it will
 *     follow any given town value.</li>
 * </ul>
 */
public class LocationParser {

    @Getter
    private String town;
    @Getter
    private State state;
    @Getter
    private Country country;
    @Getter
    private String location;
    private List<String> locationSegments;

    /**
     * Creates a location string parser instance.
     *
     * @param location The string containing a combination of town, state and
     *                 country, separated by a comma. Examples: "Houston, TX"
     *                 "Budapest, Hungary", or "Whitby, Ontario, CAN".
     */
    public LocationParser(String location) {
        if (location != null && !location.trim().isEmpty()) {
            this.location = location.trim();
            this.locationSegments = Arrays.asList(location.split(","));
        }
    }

    /**
     * Parses the location string assigned to this instance. The location will
     * be parsed into and assigned to town, state and country values where
     * appropriate.
     * If a location's state or country cannot be determined, then it will be
     * placed in the town value.
     * Use the town, state and country setter methods to access the parsed
     * values.
     */
    public void parse() {

        if (location == null) return;

        for (int i = locationSegments.size() - 1; i >= 0 && this.town == null; i--) {
            String location = locationSegments.get(i).trim();

            boolean locationSegmentAssigned = evaluateCountry(location);
            if (!locationSegmentAssigned) {
                locationSegmentAssigned = evaluateState(location);
            }
            if (!locationSegmentAssigned) {
                this.town = joinRemainingSegments(i);
            }
        }
    }

    private String joinRemainingSegments(int endSegmentIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= endSegmentIndex; i++) {
            if (!builder.isEmpty()) builder.append(", ");
            builder.append(locationSegments.get(i).trim());
        }
        return builder.toString();
    }

    private boolean evaluateCountry(String location) {
        // If the country or state is already set then reevaluating the
        // location could falsely interpret a town as a country if the
        // town has the same name as a country.
        if (this.country == null && this.state == null) {
            Country country = Country.find(location);
            if (country != null) {
                this.country = country;
                return true;
            }
        }
        return false;
    }

    private boolean evaluateState(String location) {
        if (this.state == null &&
                (this.country == null || Country.USA.equals(this.country))) {
            State state = State.find(location);
            if (state != null) {
                this.state = state;
                this.country = Country.USA;
                return true;
            }
        }
        return false;
    }
}
