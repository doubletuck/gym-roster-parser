package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UCBerkeleyScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(UCBerkeleyScraper.class);

    public UCBerkeleyScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UCBERKELEY;
    }

    String buildRosterUrl() {
        return (this.year > 2024) ?
                String.format("%s/%d?view=2",
                        "https://calbears.com/sports/womens-gymnastics/roster",
                        this.year) :
                String.format("%s/%d-%02d?view=2",
                        "https://calbears.com/sports/womens-gymnastics/roster",
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

        int indexName = this.year > 2019 ? 0 : 1;
        int indexPosition = this.year > 2019 ? -1 : 2;
        int indexClass = this. year > 2019 ? 1 : 4;
        int indexHometownClub = this.year > 2019 ? 3 : 5;
        boolean hometownHasClub = this.year > 2019;

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(indexName).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            if (indexPosition >= 0) {
                athlete.setPosition(PositionParser.parse(cells.get(indexPosition).text()));
            }

            athlete.setCollegeClass(CollegeClass.find(cells.get(indexClass).text()));

            String[] hometownClubCell = cells.get(indexHometownClub).text().split("/");

            LocationParser locationParser = new LocationParser(hometownClubCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            if (hometownClubCell.length >= 2 && hometownHasClub) athlete.setClub(hometownClubCell[1].trim());
        }
        return athlete;
    }
}
