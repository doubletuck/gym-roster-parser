package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.ScrapingUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IowaScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(IowaScraper.class);

    public IowaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.IOWA;
    }

    String buildRosterUrl() {
        return String.format("%s%d-%02d?view=table",
                "https://hawkeyesports.com/sports/wgym/roster/season/",
                this.year-1,
                this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Elements selectAthleteTableRowsFromPage(Document document) {
        // There should be two tables on the page. The first one will
        // contain the athletes.
        Elements tables = document.select("table");
        if (tables.isEmpty()) {
            return null;
        }

        return tables.get(0).select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;
        int index_name = this.year >= 2025 ? 0 : 1;
        int index_position = this.year >= 2025 ? 1 : 2;;
        int index_class = this.year >= 2025 ? 2 : 5;;
        int index_location = this.year >= 2025 ? 3 : 6;

        Elements cells = row.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = ScrapingUtil.parseName(cells.get(index_name).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(index_class).text()));
            athlete.setPosition(cells.get(index_position).text());

            LocationParser locationParser = new LocationParser(cells.get(index_location).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
