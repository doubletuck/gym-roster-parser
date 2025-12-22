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

public class TexasWomansScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(TexasWomansScraper.class);

    public TexasWomansScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.TEXASWOMANS;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://twuathletics.com/sports/womens-gymnastics/roster",
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
        int academicYearIndex = 2;
        int eventIndex = 3;
        int hometownIndex = 4;
        int clubIndex = 6;

        if (this.year < 2019 && this.year >= 2012) {
            clubIndex = 5;
        } else if (this.year == 2011) {
            nameIndex = 0;
            academicYearIndex = 1;
            eventIndex = 2;
            hometownIndex = 3;
            clubIndex = 5;
        } else if (this.year < 2011 && this.year >= 1992) { // Missing data between 1992 and 2008
            nameIndex = 0;
            academicYearIndex = 2;
            eventIndex = 3;
            hometownIndex = 4;
            clubIndex = 6;
        } else if (this.year < 1992) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
            clubIndex = -1;
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
