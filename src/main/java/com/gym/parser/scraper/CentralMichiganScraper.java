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

public class CentralMichiganScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(CentralMichiganScraper.class);

    public CentralMichiganScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.CENTRALMICHIGAN;
    }

    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://cmuchippewas.com/sports/womens-gymnastics/roster",
                this.year);
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
        int eventIndex = 1;
        int academicYearIndex = 3;
        int hometownIndex = 4;
        int clubIndex = 5;

        if (this.year == 2023) {
            clubIndex = -1;
        } else if (this.year <= 2022 && this.year > 2017) {
            academicYearIndex = 2;
            hometownIndex = 3;
            clubIndex = -1;
        } else if (this.year <= 2017) {
            nameIndex = 1;
            academicYearIndex = 2;
            eventIndex = 3;
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            if (clubIndex != -1) {
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
