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

public class KentuckyScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(KentuckyScraper.class);

    public KentuckyScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.KENTUCKY;
    }

    String buildRosterUrl() {
        return String.format("%s%d",
                "https://ukathletics.com/sports/wgym/roster/season/",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element element = document.getElementById("players-table__general");
        if (element == null) {
            return null;
        }
        return element.select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;

        int nameIndex = 0;
        int academicYearIndex = 2;
        int hometownIndex = 3;

        if (this.year == 2018) {
            academicYearIndex = 3;
            hometownIndex = 4;
        } else if (this.year <= 2015) {
            hometownIndex = 4;
        }

        Elements cells = row.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(hometownIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
