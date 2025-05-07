package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.doubletuck.gym.common.model.AcademicYear;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArizonaScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(ArizonaScraper.class);

    public ArizonaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.ARIZONA;
    }

    Logger getLogger() {
        return logger;
    }

    String buildRosterUrl() {
        return (this.year > 2018) ?
                String.format("%s/%d-%02d?view=2",
                        "https://arizonawildcats.com/sports/womens-gymnastics/roster",
                        this.year-1,
                        this.year%100)
                :
                String.format("%s/%d?view=2",
                        "https://arizonawildcats.com/sports/womens-gymnastics/roster",
                        this.year)
                ;
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

        // 2025, 24, 23, 22, 21, 20, 19, 18
        // name = 2, position = 3, class = 4, hometown = 5, lastNameFirst = true
        int nameIndex = 2;
        int positionIndex = 3;
        int academicYearIndex = 4;
        int hometownIndex = 5;
        boolean lastNameFirst = true;
        // 2017
        // name = 0, position = -1, class = 1, hometown = 2, lastNameFirst = true
        if (this.year == 2017) {
            nameIndex = 0;
            positionIndex = -1;
            academicYearIndex = 1;
            hometownIndex = 2;
        // 2016, 15, 14, 13, 12, 11, 10, 09
        // name = 1, position = 2, class = 4, hometown = 5, lastNameFirst = false
        } else if (this.year < 2017) {
            nameIndex = 1;
            positionIndex = 2;
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

            if (positionIndex >= 0) {
                athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));
            }

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
