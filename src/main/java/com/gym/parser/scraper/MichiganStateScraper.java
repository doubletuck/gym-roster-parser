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

public class MichiganStateScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(MichiganStateScraper.class);

    public MichiganStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.MICHIGANSTATE;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d",
                "https://msuspartans.com/sports/womens-gymnastics/roster",
                this.year-1,
                this.year%100);
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
        int academicYearIndex = 2;
        int hometownIndex = 3;
        int eventIndex = 4;

        if (this.year <= 2019 && this.year > 2017) {
            eventIndex = -1;
        } else if (this.year <= 2017) {
            nameIndex = 1;
            eventIndex = 3;
            hometownIndex = 4;
        }

        Elements cells = tableRowElement.select("td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownCells = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownCells.length > 0 ? hometownCells[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (eventIndex >= 0) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }
        }
        return athlete;
    }
}
