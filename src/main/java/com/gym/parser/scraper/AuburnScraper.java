package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
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

        int nameIndex = 0;
        int positionIndex = 1;
        int classIndex = 2;
        int locationIndex = 3;

        if (this.year == 2024) {
            classIndex = 4;
            locationIndex = 5;
        } else if (this.year <= 2023) {
            nameIndex = 1;
            positionIndex = 2;
            classIndex = 5;
            locationIndex = 6;
        }

        Elements cells = row.select("th, td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));
            athlete.setCollegeClass(CollegeClass.find(cells.get(classIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(locationIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
