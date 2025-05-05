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

public class OhioStateScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(OhioStateScraper.class);

    public OhioStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.OHIOSTATE;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d",
                "https://ohiostatebuckeyes.com/sports/womens-gymnastics/roster",
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
        int classIndex = 2;
        int hometownIndex = 3;

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty() && cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(classIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(hometownIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
