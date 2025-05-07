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

public class SouthernUtahScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(SouthernUtahScraper.class);

    public SouthernUtahScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.SOUTHERNUTAH;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://suutbirds.com/sports/womens-gymnastics/roster",
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

        int nameIndex = 1;
        int positionIndex = 3;
        int academicYearIndex = 2;
        int hometownIndex = 5;
        int clubIndex = 6;

        if (this.year <= 2017) {
            positionIndex = 2;
            academicYearIndex = 4;
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

            athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            if (clubIndex >= 0) {
                athlete.setClub(cells.get(clubIndex).text().trim());
            }

            String[] hometownCells = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownCells.length > 0 ? hometownCells[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
