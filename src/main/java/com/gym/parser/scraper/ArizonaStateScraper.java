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

public class ArizonaStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(ArizonaStateScraper.class);

    public ArizonaStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ARIZONASTATE;
    }

    String buildRosterUrl() {
        return (this.year <= 2021) ?
                String.format("%s/%d?view=2",
                        "https://thesundevils.com/sports/womens-gymnastics/roster",
                        this.year) :
                String.format("%s/%d-%02d?view=2",
                        "https://thesundevils.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100);
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
        int nameIndex = 0;
        int eventIndex = 3;
        int academicYearIndex = 5;
        int hometownIndex = 1;

        if (this.year <= 2021) {
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

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
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
