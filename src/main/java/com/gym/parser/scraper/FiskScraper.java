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

public class FiskScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(FiskScraper.class);

    public FiskScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.FISK;
    }

    String buildRosterUrl() {
        return String.format("%s/%d-%02d/roster",
                        "https://fiskathletics.com/sports/wgym",
                        this.year-1,
                        this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        Elements tables = document.select("table");
        if (!tables.isEmpty()) {
            // The athlete roster table is always the first table on the page.
            Element table = tables.first();
            return table.select("tbody tr");
        }
        return null;
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 0;
        int academicYearIndex = 1;
        int hometownIndex = 3;

        Elements cells = tableRowElement.select("th, td");
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
            String[] hometownCell = cells.get(hometownIndex).ownText().split("/");
            LocationParser locationParser = new LocationParser(hometownCell.length > 0 ? hometownCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
