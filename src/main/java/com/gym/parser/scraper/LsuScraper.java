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

public class LsuScraper extends AbstractScraper{

    private final static Logger logger = LoggerFactory.getLogger(LsuScraper.class);

    public LsuScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.LSU;
    }

    String buildRosterUrl() {
        return String.format("%s%d",
                "https://lsusports.net/sports/gm/roster/season/",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element element = document.getElementById("players-table");
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
            athlete.setCollege(College.LSU);
            athlete.setYear(year);

            String[] names = NameParser.parse(cells.get(0).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(1).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(3).text()));
            athlete.setClub(cells.get(5).text());

            LocationParser locationParser = new LocationParser(cells.get(6).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
