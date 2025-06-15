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

public class BoiseStateScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(BoiseStateScraper.class);

    public BoiseStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.BOISESTATE;
    }

    String buildRosterUrl() {
        return String.format("%s%d",
                "https://broncosports.com/sports/womens-gymnastics/roster/",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Document getPageDocument() {
        return getPageDocumentWithButtonClick();
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element table = document.selectFirst("div#rosterListPrint table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;

        int nameIndex = -1;
        int firstNameIndex = 0;
        int lastNameIndex = 1;
        int eventIndex = 2;
        int academicYearIndex = 4;
        int locationIndex = 5;
        int clubIndex = 7;

        if (this.year == 2022) {
            nameIndex = 0;
            academicYearIndex = 1;
            locationIndex = 4;
        } else if (this.year <= 2017) {
            nameIndex = 1;
            academicYearIndex = 2;
            eventIndex = 3;
            locationIndex = 4;
            clubIndex = -1;
        }

        Elements cells = row.select("th, td");
        // Some rows have a single cell with an advertisement. Ignore these.
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            if (nameIndex == -1) {
                athlete.setFirstName(cells.get(firstNameIndex).text());
                athlete.setLastName(cells.get(lastNameIndex).text());
            } else {
                String[] names = NameParser.parse(cells.get(nameIndex).text());
                athlete.setFirstName(names[0]);
                athlete.setLastName(names[1]);
            }

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(locationIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (clubIndex >= 0) {
                athlete.setClub(cells.get(clubIndex).text().trim());
            }
        }
        return athlete;
    }
}
