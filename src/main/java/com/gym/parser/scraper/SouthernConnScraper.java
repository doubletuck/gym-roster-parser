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

import java.util.HashMap;

public class SouthernConnScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(SouthernConnScraper.class);

    public SouthernConnScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.SOUTHERNCONN;
    }

    String buildRosterUrl() {
        String url = "https://scsuowls.com/sports/gymnastics/roster";
        switch (this.year) {
            case 2025:
            case 2024:
            case 2023:
            case 2022:
            case 2020:
            case 2010:
                return String.format("%s/%d-%02d?view=2", url, this.year - 1, this.year % 100);
            case 2021:
            case 2009:
            case 2008:
                return String.format("%s/%d-%d?view=2", url, this.year - 1, this.year);
            default: // 2026, 2025, 2019 - 2011
                return String.format("%s/%d?view=2", url, this.year);
        }
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

        if (this.year < 2026 && this.year >= 2020) {
            nameIndex = 0;
            academicYearIndex = 1;
            eventIndex = 2;
            hometownIndex = 3;
        } else if (this.year == 2019) {
            nameIndex = 0;
            academicYearIndex = 1;
            hometownIndex = 2;
            eventIndex = 3;
        } else if (this.year < 2019) {
            nameIndex = 0;
            academicYearIndex = 1;
            hometownIndex = 2;
            eventIndex = -1;
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
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownCell = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownCell.length > 0 ? hometownCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
