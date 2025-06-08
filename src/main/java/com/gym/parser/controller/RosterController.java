package com.gym.parser.controller;

import com.doubletuck.gym.common.model.College;
import com.gym.parser.exporter.CsvRosterExporter;
import com.gym.parser.model.Athlete;
import com.gym.parser.scraper.*;
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
        List<Athlete> athletes;
        switch (college) {
            case ALABAMA -> athletes = new AlabamaScraper(year).parseAthletes();
            case ARIZONA -> athletes = new ArizonaScraper(year).parseAthletes();
            case ARIZONASTATE -> athletes = new ArizonaStateScraper(year).parseAthletes();
            case ARKANSAS -> athletes = new ArkansasScraper(year).parseAthletes();
            case AUBURN -> athletes = new AuburnScraper(year).parseAthletes();
            case CLEMSON -> athletes = new ClemsonScraper(year).parseAthletes();
            case DENVER -> athletes = new DenverScraper(year).parseAthletes();
            case FLORIDA -> athletes = new FloridaScraper(year).parseAthletes();
            case GEORGIA -> athletes = new GeorgiaScraper(year).parseAthletes();
            case IOWA -> athletes = new IowaScraper(year).parseAthletes();
            case KENTUCKY -> athletes = new KentuckyScraper(year).parseAthletes();
            case LSU -> athletes = new LsuScraper(year).parseAthletes();
            case MICHIGAN -> athletes = new MichiganScraper(year).parseAthletes();
            case MICHIGANSTATE -> athletes = new MichiganStateScraper(year).parseAthletes();
            case MINNESOTA -> athletes = new MinnesotaScraper(year).parseAthletes();
            case MISSOURI -> athletes = new MissouriScraper(year).parseAthletes();
            case NEBRASKA -> athletes = new NebraskaScraper(year).parseAthletes();
            case NCSTATE -> athletes = new NCStateScraper(year).parseAthletes();
            case OHIOSTATE -> athletes = new OhioStateScraper(year).parseAthletes();
            case OKLAHOMA -> athletes = new OklahomaScraper(year).parseAthletes();
            case OREGONSTATE -> athletes = new OregonStateScraper(year).parseAthletes();
            case PENNSTATE -> athletes = new PennStateScraper(year).parseAthletes();
            case SOUTHERNUTAH -> athletes = new SouthernUtahScraper(year).parseAthletes();
            case STANFORD -> athletes = new StanfordScraper(year).parseAthletes();
            case UCBERKELEY -> athletes = new UCBerkeleyScraper(year).parseAthletes();
            case UCLA -> athletes = new UclaScraper(year).parseAthletes();
            case UMDCOLLEGEPARK -> athletes = new UMDCollegeParkScraper(year).parseAthletes();
            case UNCCHAPELHILL -> athletes = new UNCChapelHillScraper(year).parseAthletes();
            case UTAH -> athletes = new UtahScraper(year).parseAthletes();
            default -> {
                logger.error("The given college '{}' is not supported.", college);
                throw new RuntimeException(String.format("The given college '%s' is not supported.", college));
            }
        }
        return athletes;
    }
}
