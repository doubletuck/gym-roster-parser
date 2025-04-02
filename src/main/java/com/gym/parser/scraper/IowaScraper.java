package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.ScrapingUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class IowaScraper {
    private final static String BASE_ROSTER_URL = "https://hawkeyesports.com/sports/wgym/roster/season";
    private final Integer year;
    private final String rosterUrl;

    public IowaScraper(Integer year) {
        this.year = year;
        this.rosterUrl = String.format("%s/%d-%02d?view=table", BASE_ROSTER_URL, year-1, year%100);
    }

    public List<Athlete> parseAthletes() {
        List<Athlete> athleteList = new ArrayList<>();

        Connection connection = Jsoup.connect(this.rosterUrl);
        try {
            // There should be two tables on the page. The first one will
            // contain the athletes.
            Document doc = connection.get();
            Elements tables = doc.select("table");
            Elements rows = tables.get(0).select("tbody tr");
            for (Element row : rows) {
                Athlete athlete = parseRow(row);
                if (athlete != null) {
                    athleteList.add(athlete);
                }
            }
        } catch (IOException e) {
            String errorMessage = MessageFormat.format("Accessing the Iowa roster website is not successful. The page response code is {0} for url {1}.",
                    connection.response().statusCode(),
                    rosterUrl);
            throw new RuntimeException(e);
        }

        return athleteList;
    }

    private Athlete parseRow(Element row) {
        Athlete athlete = null;

        int index_name = this.year >= 2025 ? 0 : 1;
        int index_position = this.year >= 2025 ? 1 : 2;;
        int index_class = this.year >= 2025 ? 2 : 5;;
        int index_location = this.year >= 2025 ? 3 : 6;

        Elements cells = row.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(College.IOWA);
            athlete.setYear(year);

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
