package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtahScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(UtahScraper.class);

    public UtahScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UTAH;
    }

    String buildRosterUrl() {
        return (this.year < 2022) ?
            String.format("%s/%d",
                "https://utahutes.com/sports/womens-gymnastics/roster",
                this.year) :
            String.format("%s/%d-%02d",
                "https://utahutes.com/sports/womens-gymnastics/roster",
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
        Element table = document.selectFirst("div#rosterListPrint table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int academicYearIndex = 2;
        int locationIndex = 3;

        if (this.year < 2022) {
            nameIndex = 1;
            academicYearIndex = 3;
            locationIndex = 4;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownHsCell = cells.get(locationIndex).text().split("/");

            LocationParser locationParser = new LocationParser(hometownHsCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
