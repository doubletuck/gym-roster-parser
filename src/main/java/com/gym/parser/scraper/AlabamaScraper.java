package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlabamaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(AlabamaScraper.class);

    public AlabamaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ALABAMA;
    }

    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://rolltide.com/sports/womens-gymnastics/roster",
                this.year);
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

        int index_name = 0;
        int index_position = 1;
        int index_class = 3;
        int index_hometown = 4;

        if (this.year <= 2016) {
            index_name = 1;
            index_class = 2;
            index_position = 3;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(index_name).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(index_class).text()));

            String[] hometownCells = cells.get(index_hometown).text().split("/");
            LocationParser locationParser = new LocationParser(hometownCells[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            athlete.setPosition(PositionParser.parse(cells.get(index_position).text()));
        }
        return athlete;
    }
}
