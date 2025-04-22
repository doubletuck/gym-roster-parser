package com.gym.parser.scraper;

import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.model.CollegeClass;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import com.gym.parser.util.PositionParser;
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

public class OklahomaScraper extends AbstractScraper{

    private final static Logger logger = LoggerFactory.getLogger(OklahomaScraper.class);

    public OklahomaScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.OKLAHOMA;
    }

    String buildRosterUrl() {
        return String.format("%s/%d",
                "https://soonersports.com/sports/womens-gymnastics/roster",
                this.year);
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
        Elements tables = document.select("table");
        if (tables.isEmpty()) {
            return null;
        }

        return tables.get(0).select("tbody tr");
    }

    Athlete parseAthleteRow(Element tableRowElement) {
        Athlete athlete = null;

        int index_name = 0;
        int index_position = 1;
        int index_class = 3;
        int index_location = 4;

        if (this.year <= 2022) {
            index_position = -1;
            index_class = 2;
            index_location = 3;
        }

        Elements cells = tableRowElement.select("td");
        if (!cells.isEmpty()) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(index_name).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            if (index_position >= 0) {
                athlete.setPosition(PositionParser.parse(cells.get(index_position).text()));
            }

            athlete.setCollegeClass(CollegeClass.find(cells.get(index_class).text()));

            String hometownCell = cells.get(index_location).text();
            if (!hometownCell.isEmpty()) {
                LocationParser locationParser = new LocationParser(hometownCell.trim().split("/")[0]);
                locationParser.parse();
                athlete.setHomeTown(locationParser.getTown());
                athlete.setHomeState(locationParser.getState());
                athlete.setHomeCountry(locationParser.getCountry());
            }
        }
        return athlete;
    }
}
