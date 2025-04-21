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

public class ArkansasScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(ArkansasScraper.class);

    public ArkansasScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ARKANSAS;
    }

    String buildRosterUrl() {
        return String.format("%s%d-%02d",
                "https://arkansasrazorbacks.com/sport/w-gym/roster/?season=",
                this.year-1,
                this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element element = document.getElementById("roster");
        if (element == null) {
            return null;
        }
        return element.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;
        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(0).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(1).text()));

            LocationParser locationParser = new LocationParser(cells.get(2).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            athlete.setClub(cells.get(3).text());
        }
        return athlete;
    }
}
