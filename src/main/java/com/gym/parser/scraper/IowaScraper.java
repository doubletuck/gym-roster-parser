package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.PositionParser;
import com.gym.parser.util.NameParser;
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
        Element table = document.selectFirst("div.roster-players table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element row) {
        Athlete athlete = null;
        int nameIndex = this.year >= 2025 ? 0 : 1;
        int positionIndex = this.year >= 2025 ? 1 : 2;;
        int academicYearIndex = this.year >= 2025 ? 2 : 5;;
        int locationIndex = this.year >= 2025 ? 3 : 6;

        Elements cells = row.select("th, td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));
            athlete.setPosition(PositionParser.parse(cells.get(positionIndex).text()));

            LocationParser locationParser = new LocationParser(cells.get(locationIndex).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
