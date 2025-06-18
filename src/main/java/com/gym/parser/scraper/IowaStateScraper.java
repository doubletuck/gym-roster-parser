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

public class IowaStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(IowaStateScraper.class);

    public IowaStateScraper(Integer year) {
        super(year);
    }

    @Override
    public College getCollege() {
        return College.IOWASTATE;
    }

    String buildRosterUrl() {
        return (this.year >=  2000) ?
            String.format("%s/%d",
                    "https://cyclones.com/sports/womens-gymnastics/roster",
                    this.year) :
            String.format("%s/%d-%02d",
                    "https://cyclones.com/sports/womens-gymnastics/roster",
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
        int eventIndex = 1;
        int academicYearIndex = 3;
        int hometownIndex = 4;
        int clubIndex = 5;

        if (this.year == 2015) {
            clubIndex = -1;
        } else if (this.year <= 2014) {
            nameIndex = 1;
            eventIndex = 3;
            academicYearIndex = 2;
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

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
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
