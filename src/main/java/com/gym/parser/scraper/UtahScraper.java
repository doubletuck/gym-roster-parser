package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import io.github.bonigarcia.wdm.WebDriverManager;
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
import org.slf4j.LoggerFactory;

public class UtahScraper extends AbstractScraper {

    private final static Logger logger = LoggerFactory.getLogger(UtahScraper.class);

    public UtahScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UTAH;
    }

    String buildRosterUrl() {
        return (this.year < 2022) ?
            String.format("%s/%d",
                "https://utahutes.com/sports/womens-gymnastics/roster",
                this.year) :
            String.format("%s/%d-%02d",
                "https://utahutes.com/sports/womens-gymnastics/roster",
                this.year-1,
                this.year%100);
    }

    Logger getLogger() {
        return logger;
    }

    Document getPageDocument() {
        logger.debug("{} - Using selenium chrome driver to access roster data because a button needs to be clicked to dynamically display the roster in a table format.", getCollege());
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        String pageSource;

        try {
            driver.get(buildRosterUrl());
            logger.debug("{} - Pause 2000ms to make sure web page is fully loaded.", getCollege());
            Thread.sleep(2000);

            logger.debug("{} - Clicking button id = `_viewType_table` to view table of athletes on the roster.", getCollege());
            WebElement tableViewButton = driver.findElement(By.id("_viewType_table"));
            tableViewButton.click();

            logger.debug("{} - Pause 2000ms to make sure dynamic roster table loads prior to parsing it.", getCollege());
            Thread.sleep(2000);

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

    Elements selectAthleteTableRowsFromPage(Document document) {
        Element table = document.selectFirst("div#rosterListPrint table");
        if (table == null) {
            return null;
        }

        return table.select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int indexName = 0;
        int indexClass = 2;
        int indexLocation = 3;

        if (this.year < 2022) {
            indexName = 1;
            indexClass = 3;
            indexLocation = 4;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(indexName).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setCollegeClass(CollegeClass.find(cells.get(indexClass).text()));

            String[] hometownHsCell = cells.get(indexLocation).text().split("/");

            LocationParser locationParser = new LocationParser(hometownHsCell[0]);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }
        return athlete;
    }
}
