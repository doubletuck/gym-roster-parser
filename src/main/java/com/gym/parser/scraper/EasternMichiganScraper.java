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

public class EasternMichiganScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(EasternMichiganScraper.class);

    public EasternMichiganScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.EASTERNMICHIGAN;
    }

    Logger getLogger() {
        return logger;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                "https://emueagles.com/sports/womens-gymnastics/roster",
                this.year);
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
        int eventIndex = 2;
        int academicYearIndex = 4;
        int hometownIndex = 5;

        if (this.year <= 2014 && this.year > 2007) {
            eventIndex = 1;
            academicYearIndex = 3;
            hometownIndex = 4;
        } else if (this.year <= 2007) {
            nameIndex = 1;
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
