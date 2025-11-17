package com.gym.parser.scraper;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.College;
import com.gym.parser.model.Athlete;
import com.gym.parser.util.EventParser;
import com.gym.parser.util.LocationParser;
import com.gym.parser.util.NameParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtahStateScraper extends AbstractScraper {

    public final static Logger logger = LoggerFactory.getLogger(UtahStateScraper.class);

    public UtahStateScraper(Integer year) {
        super(year);
    }

    public College getCollege() {
        return College.UTAHSTATE;
    }

    String buildRosterUrl() {
        return (this.year >= 2026) ?
            String.format("%s/%d-%02d",
                "https://utahstateaggies.com/sports/womens-gymnastics/roster",
                this.year-1,
                this.year%100) :
            String.format("%s/%d",
                "https://utahstateaggies.com/sports/womens-gymnastics/roster",
                this.year);
    }

    Logger getLogger() {
        return logger;
    }

    void selectOptionOnPage(WebDriver driver) {
        getLogger().info("{} - Selecting the value `2` on the `sidearm-roster-select-template` drop-down element to get to the grid view.", getCollege());
        WebElement dropdown = driver.findElement(By.id("sidearm-roster-select-template"));
        Select select = new Select(dropdown);
        select.selectByValue("2");
    }

    Document getPageDocument() {
        return getPageDocumentWithButtonClick("sidearm-roster-select-template-button");
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

        int nameIndex = 0;
        int eventIndex = 2;
        int academicYearIndex = 3;
        int hometownIndex = 4;

        if (this.year <= 2017) {
            nameIndex = 1;
            academicYearIndex = 4;
            hometownIndex = 5;
        }

        Elements cells = tableRowElement.select("th, td");
        if (cells.size() > 1) {
            athlete = new Athlete();
            athlete.setCollege(getCollege());
            athlete.setYear(this.year);

            String[] names = NameParser.parse(cells.get(nameIndex).text());
            athlete.setFirstName(names[0]);
            athlete.setLastName(names[1]);

            athlete.setEvent(EventParser.parse(cells.get(eventIndex).text()));
            athlete.setAcademicYear(AcademicYear.find(cells.get(academicYearIndex).text()));

            String[] hometownClubCell = cells.get(hometownIndex).text().split("/");
            LocationParser locationParser = new LocationParser(hometownClubCell.length > 0 ? hometownClubCell[0] : null);
            locationParser.parse();
            athlete.setHomeTown(locationParser.getTown());
            athlete.setHomeState(locationParser.getState());
            athlete.setHomeCountry(locationParser.getCountry());
        }

        return athlete;
    }
}
