package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
            getLogger().debug("{} - Start parsing row: {}", getCollege(), row.text());
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

    Document getPageDocumentWithButtonClick() {
        getLogger().debug("{} - Using selenium chrome driver to access roster data because a button needs to be clicked to dynamically display the roster in a table format.", getCollege());
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String pageSource;

        try {
            driver.get(buildRosterUrl());
            getLogger().debug("{} - Pause 2000ms to make sure web page is fully loaded.", getCollege());
            Thread.sleep(2000);

            getLogger().debug("{} - Clicking button id = `_viewType_table` to view table of athletes on the roster.", getCollege());
            WebElement tableViewButton = driver.findElement(By.id("_viewType_table"));
            tableViewButton.click();

            getLogger().debug("{} - Pause 1000ms to make sure dynamic roster table loads prior to parsing it.", getCollege());
            Thread.sleep(1000);

            pageSource = driver.getPageSource();
            if (pageSource == null) {
                getLogger().error("{} - No page source was found when connecting to web page url {}.", getCollege(), getRosterUrl());
                return null;
            }
        } catch (Exception e) {
            getLogger().error("{} - An error occurred when connecting to web page url {}.", getCollege(), getRosterUrl());
            getLogger().error("{} - ", getCollege(), e);
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }

        return Jsoup.parse(pageSource);
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
