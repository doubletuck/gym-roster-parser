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

public class UcDavisScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(UcDavisScraper.class);

    public UcDavisScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UCDAVIS;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://ucdavisaggies.com/sports/womens-gymnastics/roster",
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

        int nameIndex = 0;
        int eventIndex = -1;
        int academicYearIndex = 2;
        int hometownIndex = 3;
        int clubIndex = 4;

        if (this.year <= 2017) {
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

            if (eventIndex != -1) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }
            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text().trim());
            }
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownClubCell = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownClubCell.length > 0 ? hometownClubCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
