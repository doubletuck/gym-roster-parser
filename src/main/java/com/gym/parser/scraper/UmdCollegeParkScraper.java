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

public class UmdCollegeParkScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(UmdCollegeParkScraper.class);

    public UmdCollegeParkScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UMDCOLLEGEPARK;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                    "https://umterps.com/sports/womens-gymnastics/roster",
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

        // 2025 - 2023
        // name = 0, event = -1, class = 1, hometown = 3
        // 2022
        // name = 0, event = 1, class = 3, hometown = 5
        // 2021 - 2018
        // name = 0, event = 1, class = 3, hometown = 4
        // 2017 - 2007
        // name = 1, event = 2, class = 4, hometown = 5
        int nameIndex = 0;
        int eventIndex = -1;
        int academicYearIndex = 1;
        int hometownIndex = 3;
        if (this.year == 2022) {
            eventIndex = 1;
            academicYearIndex = 3;
            hometownIndex = 5;
        } else if (this.year <= 2021 && this.year > 2017) {
            eventIndex = 1;
            academicYearIndex = 3;
            hometownIndex = 4;
        } else if (this.year <= 2017) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            if (eventIndex >= 0) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

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
