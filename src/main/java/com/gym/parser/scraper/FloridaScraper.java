package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.EventParser;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloridaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(FloridaScraper.class);

    public FloridaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.FLORIDA;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://floridagators.com/sports/womens-gymnastics/roster",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Elements tables = document.select("table");
        if (!tables.isEmpty()) {
            return tables.first().select("tbody tr");
        }
        return null;
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int eventIndex = 1;
        int academicYearIndex = 3;
        int hometownIndex = 4;

        if (this.year <= 2014) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownClubCell = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownClubCell.length > 0 ? hometownClubCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (hometownClubCell.length >= 2) athlete.setClub(hometownClubCell[1].trim());
        }
        return athlete;
    }
}
