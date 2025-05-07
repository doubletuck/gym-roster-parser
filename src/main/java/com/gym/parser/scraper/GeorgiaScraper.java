package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeorgiaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(GeorgiaScraper.class);

    public GeorgiaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.GEORGIA;
    }
    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://georgiadogs.com/sports/womens-gymnastics/roster",
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

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int positionIndex = 1;
        int academicYearIndex = 2;
        int hometownIndex = 4;

        if (this.year < 2023) {
            hometownIndex = 5;
        }

        Elements cells = tableRowElement.select("td");
        // Some rows have a single cell with an advertisement. Ignore these.
        if (!cells.isEmpty() && cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));

            String[] hometownHsCell = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownHsCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
