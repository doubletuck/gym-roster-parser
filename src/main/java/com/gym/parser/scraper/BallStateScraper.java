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

public class BallStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(BallStateScraper.class);

    public BallStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.BALLSTATE;
    }

    String buildRosterUrl() {
        return (this.year >=  2023) ?
                String.format("%s/%d",
                        "https://ballstatesports.com/sports/womens-gymnastics/roster",
                        this.year) :
                String.format("%s/%d-%02d",
                        "https://ballstatesports.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Document getPageDocument() {
        return getPageDocumentWithButtonClick();
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element table = document.selectFirst("div#rosterListPrint table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int hometownIndex = 1;
        int academicYearIndex = 2;
        int clubIndex = 3;
        int eventIndex = -1;

        if (this.year == 2020) {
            nameIndex = -1;
            hometownIndex = 0;
        } else if (this.year <= 2019) {
            nameIndex = 1;
            eventIndex = 3;
            hometownIndex = 4;
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("td");
        // Ignore rows with advertisements
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            if (nameIndex != -1) {
                String[] names = NameParser.parse(cells.get(nameIndex).text());
                athlete.setFirstName(names[0]);
                athlete.setLastName(names[1]);
            }

            if (eventIndex != -1) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text());
            }

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
