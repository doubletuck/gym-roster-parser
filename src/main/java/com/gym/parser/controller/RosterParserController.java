package com.gym.parser.controller;

import com.gym.parser.model.Athlete;
import com.gym.parser.scraper.ArkansasScraper;
import com.gym.parser.scraper.AuburnScraper;
import com.gym.parser.scraper.ClemsonScraper;
import com.gym.parser.scraper.IowaScraper;
import com.gym.parser.scraper.KentuckyScraper;
import com.gym.parser.scraper.LsuScraper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roster/parse/athletes")
public class RosterParserController {

    @PostMapping("/arkansas")
    public ResponseEntity<List<Athlete>> parseArkansasAthletes(@RequestBody RosterParserParameters params) {
        ArkansasScraper scraper = new ArkansasScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/auburn")
    public ResponseEntity<List<Athlete>> parseAuburnAthletes(@RequestBody RosterParserParameters params) {
        AuburnScraper scraper = new AuburnScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/clemson")
    public ResponseEntity<List<Athlete>> parseClemsonAthletes(@RequestBody RosterParserParameters params) {
        ClemsonScraper scraper = new ClemsonScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/iowa")
    public ResponseEntity<List<Athlete>> parseIowaAthletes(@RequestBody RosterParserParameters params) {
        IowaScraper scraper = new IowaScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/kentucky")
    public ResponseEntity<List<Athlete>> parseKentuckyAthletes(@RequestBody RosterParserParameters params) {
        KentuckyScraper scraper = new KentuckyScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }

    @PostMapping("/lsu")
    public ResponseEntity<List<Athlete>> parseLsuAthletes(@RequestBody RosterParserParameters params) {
        LsuScraper scraper = new LsuScraper(params.getYear());
        List<Athlete> athletes = scraper.parseAthletes();
        return ResponseEntity.ok(athletes);
    }
}
