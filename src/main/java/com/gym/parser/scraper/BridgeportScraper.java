package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BridgeportScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(BridgeportScraper.class);

    public BridgeportScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.BRIDGEPORT;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d/roster?view=list",
                        "https://ubknights.com/sports/wgym",
                        this.year-1,
                        this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element table = document.selectFirst("div.roster-data table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int academicYearIndex = 1;
        int hometownIndex = 3;

        Elements cells = tableRowElement.select("th, td");
        // Ignore rows with advertisements
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            // Using specific selectors to avoid retrieving text from print-specific anchor elements.
            String[] names = NameParser.parse(cells.get(nameIndex).select("a").get(1).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            // Using `ownText` to avoid retrieving text from print-specific span elements.
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).ownText()));

            // Using `ownText` to avoid retrieving text from print-specific span elements.
            // <location> / <high school> / <gym>
            String[] hometownClubCell = cells.get(hometownIndex).ownText().split("/");
            LocationParser locationParser = new LocationParser(hometownClubCell.length > 0 ? hometownClubCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (hometownClubCell.length > 2) {
                String club = hometownClubCell[2];
                if (!club.isBlank()) athlete.setClub(club.trim());
            }
        }
        return athlete;
    }
}
