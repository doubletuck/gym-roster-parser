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

public class OregonStateScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(OregonStateScraper.class);

    public OregonStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.OREGONSTATE;
    }

    Logger getLogger() {
        return logger;
    }

    String buildRosterUrl() {
        return (this.year <= 2022 && this.year >= 2020) ?
                String.format("%s/%d-%02d",
                        "https://osubeavers.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100) :
                String.format("%s/%d",
                        "https://osubeavers.com/sports/womens-gymnastics/roster",
                        this.year);
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

        int nameIndex = 1;
        int eventIndex = 2;
        int academicYearIndex = 3;
        int hometownIndex = 4;
        int clubIndex = 6;

        if (this.year <= 2017) {
            eventIndex = 3;
            academicYearIndex = 2;
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("td");
        // Some rows have a single cell with an advertisement. Ignore these.
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(hometownIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (clubIndex >= 0) {
                athlete.setClub(cells.get(clubIndex).text());
            }
        }
        return athlete;
    }
}
