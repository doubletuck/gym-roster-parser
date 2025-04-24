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

public class MissouriScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(MissouriScraper.class);

    public MissouriScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.MISSOURI;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://mutigers.com/sports/womens-gymnastics/roster",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        // There are multiple tables on the page with similar/same class
        // values and no ids. Athlete roster does have a caption value.
        // Using the caption to help identify the correct table.
        Elements tables = document.select("table");
        if (!tables.isEmpty()) {
            for (Element table : tables) {
                Element caption = table.selectFirst("caption");
                if (caption != null && caption.text().toLowerCase().contains("gymnastics roster")) {
                    return table.select("tbody tr");
                }
            }
        }
        return null;
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

            athlete.setPosition(PositionParser.parse(cells.get(3).text()));

            String[] hometownHsCell = cells.get(4).text().split("/");

            LocationParser locationParser = new LocationParser(hometownHsCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
