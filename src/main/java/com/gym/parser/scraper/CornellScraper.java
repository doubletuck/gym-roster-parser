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

public class CornellScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(CornellScraper.class);

    public CornellScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.CORNELL;
    }

    Logger getLogger() {
        return logger;
    }

    String buildRosterUrl() {
        return (this.year == 2000) ?
                String.format("%s/%d-%d?view=2",
                        "https://cornellbigred.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year) :
                String.format("%s/%d-%02d?view=2",
                        "https://cornellbigred.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100);

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

        int nameIndex = 0;
        int eventIndex = 1;
        int academicYearIndex = 2;
        int hometownIndex = 4;
        int clubIndex = 5;

        if (this.year <= 2008) {
            nameIndex = 1;
            eventIndex = 2;
            academicYearIndex = 4;
            hometownIndex = 5;
            clubIndex = -1;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String name = cells.get(nameIndex).text();
            String[] names = NameParser.parse(name);
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text().trim());
            }

            String[] hometown = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometown[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
