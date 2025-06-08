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

public class NebraskaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(NebraskaScraper.class);

    public NebraskaScraper(Integer year) {
        super(year);
    }

    @Override
    public College getCollege() {
        return College.NEBRASKA;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d",
                "https://huskers.com/sports/womens-gymnastics/roster/season",
                this.year-1,
                this.year%100
        );
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element table = document.selectFirst("div.roster-players table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int eventIndex = 2;
        int academicYearIndex = 3;
        int locationIndex = 4;

        if (this.year <= 2023 && this.year > 2017) {
            nameIndex = 1;
            eventIndex = 3;
            academicYearIndex = 4;
            locationIndex = 5;
        } else if (this.year <= 2017) {
            nameIndex = 1;
            academicYearIndex = 5;
            locationIndex = 6;
        }

        Elements cells = tableRowElement.select("th, td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(locationIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
