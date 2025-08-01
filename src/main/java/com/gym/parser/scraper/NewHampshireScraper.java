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

public class NewHampshireScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(NewHampshireScraper.class);

    public NewHampshireScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.NEWHAMPSHIRE;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d",
                "https://unhwildcats.com/sports/womens-gymnastics/roster",
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
        int academicYearIndex = 1;
        int eventIndex = 2;
        int clubIndex = 3;
        int hometownIndex = 4;

        if (this.year <= 2024 && this.year > 2019) {
            nameIndex = 1;
            academicYearIndex = 3;
            eventIndex = 5;
            clubIndex = -1;
            hometownIndex = 6;
        } else if (this.year <= 2019 && this.year > 2015) {
            nameIndex = 1;
            academicYearIndex = 2;
            eventIndex = 3;
            hometownIndex = 5;
            clubIndex = -1;
        } else if (this.year == 2015) {
            clubIndex = -1;
        } else if (this.year <= 2014) {
            nameIndex = 1;
            academicYearIndex = 2;
            eventIndex = 3;
            clubIndex = -1;
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
            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text());
            }

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
