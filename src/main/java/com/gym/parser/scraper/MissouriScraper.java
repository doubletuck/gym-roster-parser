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

public class MissouriScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(MissouriScraper.class);

    public MissouriScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.MISSOURI;
    }

    String buildRosterUrl() {
        return (this.year <= 2009 && this.year > 2006) ?
                String.format("%s/%d-%02d?view=2",
                        "https://mutigers.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100) :
                String.format("%s/%d?view=2",
                        "https://mutigers.com/sports/womens-gymnastics/roster",
                        this.year);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        // There are multiple tables on the page with similar/same class
        // values and no ids. Athlete roster does have a caption value.
        // Using the caption to help identify the correct table.
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

        int nameIndex = 0;
        int academicYearIndex = 1;
        int eventIndex = 3;
        int hometownIndex = 4;

        if (this.year == 2015) {
            academicYearIndex = 2;
            eventIndex = -1;
            hometownIndex = 3;
        } else if (this.year <= 2014) {
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

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            if (eventIndex >= 0) {
                athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            }

            String[] hometownHsCell = cells.get(hometownIndex).text().split("/");

            LocationParser locationParser = new LocationParser(hometownHsCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
