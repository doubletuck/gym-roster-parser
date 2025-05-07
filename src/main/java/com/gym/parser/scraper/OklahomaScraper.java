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

public class OklahomaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(OklahomaScraper.class);

    public OklahomaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.OKLAHOMA;
    }

    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://soonersports.com/sports/womens-gymnastics/roster",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Document getPageDocument() {
        return getPageDocumentWithButtonClick();
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
        int nameIndex = 0;
        int eventIndex = 1;
        int academicYearIndex = 3;
        int hometownIndex = 4;

        if (this.year <= 2022) {
            eventIndex = -1;
            academicYearIndex = 2;
            hometownIndex = 3;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            if (eventIndex >= 0) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownCells = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownCells.length > 0 ? hometownCells[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
