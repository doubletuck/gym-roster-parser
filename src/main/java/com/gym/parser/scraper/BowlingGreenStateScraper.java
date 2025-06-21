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

public class BowlingGreenStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(BowlingGreenStateScraper.class);

    public BowlingGreenStateScraper(Integer year) {
        super(year);
    }

    @Override
    public College getCollege() {
        return College.BOWLINGGREENSTATE;
    }

    String buildRosterUrl() {
        return (this.year >=  2015 || this.year <= 2009) ?
                String.format("%s/%d",
                        "https://bgsufalcons.com/sports/womens-gymnastics/roster",
                        this.year) :
                String.format("%s/%d-%02d",
                        "https://bgsufalcons.com/sports/womens-gymnastics/roster",
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
        int academicYearIndex = 1;
        int hometownIndex = 2;
        int eventIndex = -1;

        if (this.year <=2015) {
            eventIndex = 2;
            hometownIndex = 3;
        }

        Elements cells = tableRowElement.select("td");
        // Ignore rows with advertisements
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
