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

public class IllinoisScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(IllinoisScraper.class);

    public IllinoisScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ILLINOIS;
    }

    String buildRosterUrl() {
        return String.format("%s%d%s",
                "https://fightingillini.com/sports/womens-gymnastics/roster/",
                this.year,
                "?view=2");
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

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;

        int nameIndex = 0;
        int academicYearIndex = 1;
        int eventIndex = 3;
        int locationIndex = 4;
        boolean hasRandomNumberAfterName = this.year <= 2024 && this.year > 2016;

        if (this.year <= 2018 && this.year > 2016) {
            eventIndex = 1;
            academicYearIndex = 2;
            locationIndex = 3;
        } else if (this.year <= 2014) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            locationIndex = 5;
        }

        Elements cells = row.select("th, td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            if (hasRandomNumberAfterName) {
                names = NameParser.parse(names[0]);
            }
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownHsCell = cells.get(locationIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownHsCell.length > 0 ? hometownHsCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
