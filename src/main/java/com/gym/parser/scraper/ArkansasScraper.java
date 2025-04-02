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

public class ArkansasScraper {
    private final static String BASE_ROSTER_URL = "https://arkansasrazorbacks.com/sport/w-gym/roster/?season=";
    private final Integer year;
    private final String rosterUrl;

    public ArkansasScraper(Integer year) {
        this.year = year;
        this.rosterUrl = String.format("%s%d-%02d", BASE_ROSTER_URL, year-1, year%100);
    }
    public List<Athlete> parseAthletes() {
        ArrayList<Athlete> athleteList = new ArrayList<>();

        Connection connection = Jsoup.connect(rosterUrl);
        try {
            Document doc = connection.get();
            Elements rows = doc.getElementById("roster").select("tbody tr");
            for (Element row : rows) {
                Athlete athlete = parseRow(row);
                if (athlete != null) {
                    athleteList.add(athlete);
                }
            }
        } catch (IOException e) {
            String errorMessage = MessageFormat.format("Accessing the Auburn roster website is not successful. The page response code is {0} for url {1}.",
                    connection.response().statusCode(),
                    rosterUrl);
            throw new RuntimeException(e);
        }

        return athleteList;
    }

    private Athlete parseRow(Element row) {
        Athlete athlete = null;

        Elements cells = row.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(College.ARKANSAS);
            athlete.setYear(year);

            String[] names = ScrapingUtil.parseName(cells.get(0).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(1).text()));

            LocationParser locationParser = new LocationParser(cells.get(2).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());

            athlete.setClub(cells.get(3).text());
        }

        return athlete;
    }
}
