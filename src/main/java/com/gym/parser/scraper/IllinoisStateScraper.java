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

public class IllinoisStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(IllinoisStateScraper.class);

    public IllinoisStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ILLINOISSTATE;
    }

    Logger getLogger() {
        return logger;
    }

    String buildRosterUrl() {
        return String.format("%s/%d?view=2",
                        "https://goredbirds.com/sports/womens-gymnastics/roster",
                        this.year);
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        String captionText = String.format("%d %s", this.year, this.year > 2014 ? "gymnastics roster" : "women's gymnastics");
        Elements tables = document.select("table");
        if (!tables.isEmpty()) {
            for (Element table : tables) {
                Element caption = table.selectFirst("caption");
                if (caption != null && caption.text().toLowerCase().contains(captionText)) {
                    return table.select("tbody tr");
                }
            }
        }
        return null;
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int nameIndex = 1;
        int eventIndex = 3;
        int academicYearIndex = 4;
        int hometownIndex = 5;
        int clubIndex = -1;
        boolean lastNameFirst = true;

        if (this.year > 2023) {
            clubIndex = 6;
        }

        if (this.year == 2013 || this.year == 2011) {
            eventIndex = 2;
            lastNameFirst = false;
        } else if (this.year <= 2015 && this.year > 2010 ) {
            nameIndex = 0;
            eventIndex = 2;
            academicYearIndex = 3;
            hometownIndex = 4;
        } else if (this.year <= 2010) {
            eventIndex = 2;
            lastNameFirst = false;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String name = cells.get(nameIndex).text();
            String[] names = lastNameFirst ? NameParser.parseLastNameFirst(name) : NameParser.parse(name);
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

            if (clubIndex != -1) {
                athlete.setClub(cells.get(clubIndex).text());
            }
        }
        return athlete;
    }
}
