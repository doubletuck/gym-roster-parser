package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractScraper {

    final Integer year;
    final String rosterUrl;

    protected AbstractScraper(Integer year) {
        this.year = year;
        this.rosterUrl = buildRosterUrl();
    }

    public List<Athlete> parseAthletes() {
        ArrayList<Athlete> athleteList = new ArrayList<>();

        getLogger().info("{} - Commence scraping for the athlete roster for season {} using the web page url {}.", getCollege(), getYear(), getRosterUrl());

        Document document = getPageDocument();

        Elements tableRowElements = selectAthleteTableRowsFromPage(document);
        if (tableRowElements == null) {
            getLogger().error("{} - The expected identifying DOM tableRowElements were not found when parsing for the athlete roster on the web page.", getCollege());
            throw new RuntimeException("The web page could not be parsed as expected.");
        }

        for (Element row : tableRowElements) {
            Athlete athlete = parseAthleteRow(row);
            if (athlete != null) {
                athleteList.add(athlete);
                getLogger().debug("{} - Added athlete to list: {}", getCollege(), athlete);
            }
        }

        getLogger().info("{} - Finished scraping the athlete roster for season {}. Found {} athletes.", getCollege(), getYear(), athleteList.size());
        return athleteList;
    }

    Document getPageDocument() {
        Connection connection = Jsoup.connect(getRosterUrl());
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            getLogger().error("{} - Could not connect to web page url {}.", getCollege(), getRosterUrl());
            getLogger().error("{} - ", getCollege(), e);
            throw new RuntimeException(e);
        }
        return document;
    }

    public abstract College getCollege();
    abstract String buildRosterUrl();
    abstract Logger getLogger();

    /**
     * Given a Document that is returned from connecting the roster URL
     * and contains the web corresponding web page, return the table
     * Elements (i.e., "<td>") that will contain the athletes.
     *
     * @param   document The web page containing the team roster where
     *          the athletes are listed.
     * @return  The elements that contain the list of athletes that can
     *          be parsed into individual Athlete objects. Return null
     *          if the expected HTML elements containing the athletes is
     *          not found.
     * @see     #parseAthleteRow(Element)
     */
    abstract Elements selectAthleteTableRowsFromPage(Document document);

    /**
     * Give the Elements selected from #selectAthleteTableRowsFromPage
     *
     * @param   tableRowElement An html element that contains the
     *          information about a single element, typically a table
     *          row.
     * @return  An instance of Athlete containing the information
     *          parsed from the tableRowElement.
     */
    abstract Athlete parseAthleteRow(Element tableRowElement);
}
