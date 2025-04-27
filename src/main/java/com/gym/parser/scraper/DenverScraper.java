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

public class DenverScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(DenverScraper.class);

    public DenverScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.DENVER;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://denverpioneers.com/sports/womens-gymnastics/roster",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
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

        // name = 0, class = 2, home = 3, position = 4, club = 5
        int indexName = 0;
        int indexClass = 2;
        int indexHometown = 3;
        int indexPosition = 4;
        int indexClub = 5;
        boolean locationHasSlash = false;
        if (this.year <= 2017 && this.year > 2014) {
            // name = 1, class = 4, home = 5, position = 2, club = -1
            indexName = 1;
            indexPosition = 2;
            indexClass = 4;
            indexHometown = 5;
            indexClub = -1;
            locationHasSlash = true;
        } else if ((this.year <= 1987 && this.year > 1985) || this.year == 1983 || this.year == 1982) {
            // name = 0, class = 2, home = 3, position = -1, club = -1
            indexHometown = 1;
            indexPosition = -1;
            indexClub = -1;
        } else if (this.year == 1985 || this.year == 1984) {
            // name = 0, class = 1, home = 2, position = -1, club = -1
            indexClass = 1;
            indexHometown = 2;
            indexPosition = -1;
            indexClub = -1;
        } else if ((this.year <= 1981 && this.year > 1978) || this.year == 1976) {
            // name = 0, class = 2, home = 3, position = 4, club = -1
            indexClub = -1;
        } else if (this.year == 1978) {
            // name = 0, class = 2, home = 1, position = -1, club = -1
            indexHometown = 1;
            indexPosition = -1;
            indexClub = -1;
        } else if (this.year == 1977) {
            // name = 0, class = 3, home = 2, position = -1, club = -1
            indexClass = 3;
            indexHometown = 2;
            indexPosition = -1;
            indexClub = -1;
        } else if (this.year == 1975) {
            // name = 0, class = 1, home = 2, position = -1, club = -1
            indexClass = 1;
            indexHometown = 2;
            indexPosition = -1;
            indexClub = -1;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(indexName).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(indexClass).text()));

            String location = cells.get(indexHometown).text();
            if (locationHasSlash) location = location.replace("/","");
            LocationParser locationParser = new LocationParser(location.trim());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (indexPosition >= 0) {
                athlete.setPosition(PositionParser.parse(cells.get(indexPosition).text()));
            }

            if (indexClub >= 0) {
                athlete.setClub(cells.get(indexClub).text());
            }
        }
        return athlete;
    }
}
