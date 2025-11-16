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

public class PennStateScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(PennStateScraper.class);

    public PennStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.PENNSTATE;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=table",
                "https://gopsusports.com/sports/womens-gymnastics/roster/season",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Elements tables = document.select("table");
        if (tables.isEmpty()) {
            return null;
        }

        return tables.get(0).select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 1;
        int eventIndex = 2;
        int academicYearIndex = 5;
        int hometownIndex = 6;
        int clubIndex = 8;

        if (this.year <= 2018) {
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("th, td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(hometownIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (clubIndex >= 0) {
                athlete.setClub(cells.get(clubIndex).text());
            }
        }
        return athlete;
    }
}
