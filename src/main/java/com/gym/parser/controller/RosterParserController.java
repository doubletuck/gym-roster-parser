package com.gym.parser.controller;

import com.gym.parser.model.Athlete;
import com.gym.parser.scraper.LsuScraper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/roster/parse")
public class RosterParserController {

    @PostMapping("/lsu")
    public ResponseEntity<List<Athlete>> parse(@RequestBody RosterParserParameters params) {
        LsuScraper scraper = new LsuScraper(params.getYear());
        List<Athlete> athletes = scraper.parse();
        return ResponseEntity.ok(athletes);
    }
}
