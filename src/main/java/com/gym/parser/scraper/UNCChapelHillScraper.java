package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNCChapelHillScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(UNCChapelHillScraper.class);

    public UNCChapelHillScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UNCCHAPELHILL;
    }

    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://goheels.com/sports/womens-gymnastics/roster",
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

        // 2025 - 2017
        // name = 0, position = 1, class = 2, hometown = 3
        // 2016 - 2003
        // name = 1, position = 3, class = 2, hometown = 4
        int nameIndex = 0;
        int positionIndex = 1;
        int classIndex = 2;
        int hometownIndex = 3;
        if (this.year < 2017) {
            nameIndex = 1;
            positionIndex = 3;
            classIndex = 2;
            hometownIndex = 4;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));
            athlete.setCollegeClass(CollegeClass.find(cells.get(classIndex).text()));

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
