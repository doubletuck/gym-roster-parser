package com.gym.parser.controller;

import com.gym.parser.exporter.CsvRosterExporter;
import com.gym.parser.model.Athlete;
import com.gym.parser.model.College;
import com.gym.parser.scraper.ArkansasScraper;
import com.gym.parser.scraper.AuburnScraper;
import com.gym.parser.scraper.ClemsonScraper;
import com.gym.parser.scraper.IowaScraper;
import com.gym.parser.scraper.KentuckyScraper;
import com.gym.parser.scraper.LsuScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/roster")
public class RosterController {

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    @PostMapping("/athletes/scrape")
    public ResponseEntity<List<Athlete>> scrapeAthleteRoster(@RequestBody RosterParameters params) {
        List<Athlete> athletes = scrapeAthleteRosterFromWebsite(params.getYear(), params.getCollege());
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/athletes/export")
    public ResponseEntity<String> exportAthletesToFile(@RequestBody ExportParameters params) {
        File exportFile = new File(params.getFileName());
        List<Athlete> athletes = scrapeAthleteRosterFromWebsite(params.getYear(), params.getCollege());
        try {
            CsvRosterExporter.writeToFile(athletes, exportFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok()
                .body(String.format("Exported %d athletes to file %s.", athletes.size(), exportFile.getAbsolutePath()));
    }

    private List<Athlete> scrapeAthleteRosterFromWebsite(Integer year, College college) {
        List<Athlete> athletes = null;
        switch (college) {
            case ARKANSAS:
                athletes = new ArkansasScraper(year).parseAthletes();
                break;
            case AUBURN:
                athletes = new AuburnScraper(year).parseAthletes();
                break;
            case CLEMSON:
                athletes = new ClemsonScraper(year).parseAthletes();
                break;
            case IOWA:
                athletes = new IowaScraper(year).parseAthletes();
                break;
            case KENTUCKY:
                athletes = new KentuckyScraper(year).parseAthletes();
                break;
            case LSU:
                athletes = new LsuScraper(year).parseAthletes();
                break;
            default:
                logger.error("The college given '{}' is unknown or unsupported.", college);
        }
        return athletes;
    }
}
