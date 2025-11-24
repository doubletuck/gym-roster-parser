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

public class BrighamYoungScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(BrighamYoungScraper.class);

    public BrighamYoungScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.BRIGHAMYOUNG;
    }

    String buildRosterUrl() {
        return (this.year >= 2026) ?
                String.format("%s%d-%02d%s",
                        "https://byucougars.com/sports/womens-gymnastics/roster/season/",
                        this.year - 1,
                        this.year,
                        "?view=table") :
                String.format("%s%d%s",
                        "https://byucougars.com/sports/womens-gymnastics/roster/season/",
                        this.year,
                        "?view=table");
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        // The athletes table is the first one on the page.
        Elements tables = document.select("table");
        if (tables.isEmpty()) {
            return null;
        }

        return tables.get(0).select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;

        int nameIndex = 1;
        int eventIndex = 2;
        int academicYearIndex = 4;
        int locationIndex = 5;

        if (this.year <= 2025 && this.year > 2022) {
            nameIndex = 0;
            eventIndex = 1;
            academicYearIndex = 3;
            locationIndex = 4;
        } else if (this.year <= 2022) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 5;
            locationIndex = 6;
        }

        Elements cells = row.select("th, td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(locationIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
