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

public class ClemsonScraper {
    private final static String BASE_ROSTER_URL = "https://clemsontigers.com/sports/gymnastics/roster/season";
    private final Integer year;
    private final String rosterUrl;

    public ClemsonScraper(Integer year) {
        this.year = year;
        // Clemson indicates that their season starts with the Fall academic
        // year. Thus, passing in 2025 will be for 2024-2025. The URL uses
        // the Fall academic year.
        this.rosterUrl = String.format("%s/%d", BASE_ROSTER_URL, year-1);
    }

    public List<Athlete> parseAthletes() {
        List<Athlete> athleteList = new ArrayList<>();

        Connection connection = Jsoup.connect(rosterUrl);
        try {
            Document doc = connection.get();
            Elements rows = doc.getElementById("person__list_all").select("tbody tr");
            for (Element row : rows) {
                Athlete athlete = parseRow(row);
                if (athlete != null) {
                    athleteList.add(athlete);
                }
            }
        } catch (IOException e) {
            String errorMessage = MessageFormat.format("Accessing the Clemson roster website is not successful. The page response code is {0} for url {1}.",
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
            athlete.setCollege(College.CLEMSON);
            athlete.setYear(year);

            String[] names = ScrapingUtil.parseName(cells.get(0).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setHeight(cells.get(1).text());
            athlete.setCollegeClass(CollegeClass.find(cells.get(2).text()));

            LocationParser locationParser = new LocationParser(cells.get(3).text());
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }

        return athlete;
    }
}
