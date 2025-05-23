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

public class ClemsonScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(ClemsonScraper.class);

    public ClemsonScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.CLEMSON;
    }

    String buildRosterUrl() {
        // The season on the website starts with the Fall academic
        // year. For example, passing in 2025 will be for 2024-2025.
        // But, the URL uses will need 2024 to represent 2024-2025.
        return String.format("%s%d",
                "https://clemsontigers.com/sports/gymnastics/roster/season/",
                this.year-1);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element element = document.getElementById("person__list_all");
        if (element == null) {
            return null;
        }

        return element.select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;
        Elements cells = row.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(0).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(2).text()));

            LocationParser locationParser = new LocationParser(cells.get(3).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
