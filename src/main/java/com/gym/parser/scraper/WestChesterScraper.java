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

public class WestChesterScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(WestChesterScraper.class);

    public WestChesterScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.WESTCHESTER;
    }

    String buildRosterUrl() {
        if (this.year == 2009 || this.year == 2007) {
            return String.format("%s/%d-%02d?view=2",
                    "https://wcupagoldenrams.com/sports/womens-gymnastics/roster",
                    this.year-1,
                    this.year%100);
        }
        return String.format("%s/%d",
                "https://wcupagoldenrams.com/sports/womens-gymnastics/roster",
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
                if (caption != null && caption.text().toLowerCase().contains("roster")) {
                    return table.select("tbody tr");
                }
            }
        }
        return null;
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 1;
        int eventIndex = 2;
        int academicYearIndex = 4;
        int hometownIndex = 5;
        int clubIndex = -1;

        if (this.year <= 2019 && this.year > 2016) {
            nameIndex = 0;
            eventIndex = 1;
            academicYearIndex = 2;
            hometownIndex = 3;
        } else if (this.year <= 2016 && this.year > 2008) {
            nameIndex = 0;
            eventIndex = 1;
            academicYearIndex = 3;
            hometownIndex = 4;
        } else if (this.year <= 2008) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
        }

        Elements cells = tableRowElement.select("th, td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            if (eventIndex != -1) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }
            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text());
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
