package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MichiganScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(MichiganScraper.class);

    public MichiganScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.MICHIGAN;
    }


    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://mgoblue.com/sports/womens-gymnastics/roster",
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

        // 2025 - 2010
        // name = 0, class = 1, hometown = 2
        int nameIndex = 0;
        int classIndex = 1;
        int hometownIndex = 2;

        // name = 0, class = 2, hometown = 3
        if (this.year < 2010) {
            classIndex = 2;
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

            athlete.setCollegeClass(CollegeClass.find(cells.get(classIndex).text()));

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
