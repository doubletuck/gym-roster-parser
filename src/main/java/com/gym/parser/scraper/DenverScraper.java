package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
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
        int nameIndex = 0;
        int academicYearIndex = 2;
        int hometownIndex = 3;
        int positionIndex = 4;
        int clubIndex = 5;
        boolean locationHasSlash = false;
        if (this.year <= 2017 && this.year > 2014) {
            // name = 1, class = 4, home = 5, position = 2, club = -1
            nameIndex = 1;
            positionIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
            clubIndex = -1;
            locationHasSlash = true;
        } else if ((this.year <= 1987 && this.year > 1985) || this.year == 1983 || this.year == 1982) {
            // name = 0, class = 2, home = 3, position = -1, club = -1
            hometownIndex = 1;
            positionIndex = -1;
            clubIndex = -1;
        } else if (this.year == 1985 || this.year == 1984) {
            // name = 0, class = 1, home = 2, position = -1, club = -1
            academicYearIndex = 1;
            hometownIndex = 2;
            positionIndex = -1;
            clubIndex = -1;
        } else if ((this.year <= 1981 && this.year > 1978) || this.year == 1976) {
            // name = 0, class = 2, home = 3, position = 4, club = -1
            clubIndex = -1;
        } else if (this.year == 1978) {
            // name = 0, class = 2, home = 1, position = -1, club = -1
            hometownIndex = 1;
            positionIndex = -1;
            clubIndex = -1;
        } else if (this.year == 1977) {
            // name = 0, class = 3, home = 2, position = -1, club = -1
            academicYearIndex = 3;
            hometownIndex = 2;
            positionIndex = -1;
            clubIndex = -1;
        } else if (this.year == 1975) {
            // name = 0, class = 1, home = 2, position = -1, club = -1
            academicYearIndex = 1;
            hometownIndex = 2;
            positionIndex = -1;
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String location = cells.get(hometownIndex).text();
            if (locationHasSlash) location = location.replace("/","");
            LocationParser locationParser = new LocationParser(location.trim());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (positionIndex >= 0) {
                athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));
            }

            if (clubIndex >= 0) {
                athlete.setClub(cells.get(clubIndex).text());
            }
        }
        return athlete;
    }
}
