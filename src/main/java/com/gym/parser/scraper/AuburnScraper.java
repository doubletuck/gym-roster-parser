package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.PositionParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuburnScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(AuburnScraper.class);

    public AuburnScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.AUBURN;
    }

    String buildRosterUrl() {
        return String.format("%s%d%s",
                "https://auburntigers.com/sports/gymnastics/roster/season/",
                this.year,
                "?view=table");
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        // The athletes table is the first one on the page.
        Elements tables = document.select("table");
        if (tables.isEmpty()) {
            return null;
        }

        return tables.get(0).select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;

        int index_name = 0;
        int index_position = 1;
        int index_class = 2;
        int index_location = 3;

        if (this.year == 2024) {
            index_class = 4;
            index_location = 5;
        } else if (this.year <= 2023) {
            index_name = 1;
            index_position = 2;
            index_class = 5;
            index_location = 6;
        }

        Elements cells = row.select("th, td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(index_name).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setPosition(PositionParser.parse(cells.get(index_position).text()));
            athlete.setCollegeClass(CollegeClass.find(cells.get(index_class).text()));

            LocationParser locationParser = new LocationParser(cells.get(index_location).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
