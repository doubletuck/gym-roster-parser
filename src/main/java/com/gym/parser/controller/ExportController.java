package com.gym.parser.controller;

import com.gym.parser.exporter.CsvRosterExporter;
import com.gym.parser.model.Athlete;
import com.gym.parser.scraper.ArkansasScraper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/roster/export/athletes")
public class ExportController {

    @PostMapping
    public ResponseEntity<Void> exportAthletesToFile(@RequestBody ExportParameters params) {
        File exportFile = new File(params.getFileName());
        try {
            switch (params.getCollege()) {
                case ARKANSAS:
                    exportArkansas(params.getYear(), exportFile);
                    break;
                case AUBURN:
                    break;
                case CLEMSON:
                    break;
                case IOWA:
                    break;
                case KENTUCKY:
                    break;
                case LSU:
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    private void exportArkansas(int year, File exportFile) throws IOException {
        ArkansasScraper scraper = new ArkansasScraper(year);
        List<Athlete> athletes = scraper.parseAthletes();
        CsvRosterExporter.writeToFile(athletes, exportFile);
    }
}
