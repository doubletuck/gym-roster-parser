package com.gym.parser.exporter;

import com.gym.parser.model.Athlete;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public class CsvRosterExporter {

    private final static Logger logger = LoggerFactory.getLogger(CsvRosterExporter.class);

    public enum Headers {
        COLLEGE_CODE_NAME,
        YEAR,
        FIRST_NAME, LAST_NAME,
        COLLEGE_CLASS,
        HOME_TOWN, HOME_STATE, HOME_COUNTRY,
        CLUB, POSITION;
    }

    public static void writeToFile(Collection<Athlete> athletes, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("A file name must be provided to export the roster.");
        }
        writeToFile(athletes, new File(fileName));
    }

    public static void writeToFile(Collection<Athlete> athletes, File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("A file path and name must be provided to export the roster.");
        }

        if (file.exists()) {
            throw new IOException(String.format("The file '%s' already exists. Cannot proceed with the export to avoid overwriting the existing file.", file.getAbsolutePath()));
        }

        try {
            Writer fileWriter = new FileWriter(file);
            CSVFormat csvFormat = CSVFormat.RFC4180.builder()
                    .setHeader(Headers.class)
                    .setRecordSeparator("\n")
                    .get();
            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat);
            for (Athlete athlete : athletes) {
                csvPrinter.printRecord(
                        athlete.getCollege().name(),
                        athlete.getYear(),
                        athlete.getFirstName(),
                        athlete.getLastName(),
                        athlete.getAcademicYear() == null ? null : athlete.getAcademicYear().name(),
                        athlete.getHomeTown(),
                        athlete.getHomeState(),
                        athlete.getHomeCountry(),
                        athlete.getClub(),
                        athlete.getPosition()
                );
                logger.debug("Writing the following row to file '{}': {}", file.getAbsolutePath(), athlete);
            }
            csvPrinter.flush();
            logger.info("Roster export to '{}' completed.", file.getAbsolutePath());
        } catch (IOException e) {
            logger. error("An error occurred while writing the college roster athletes to file '{}'.", file.getAbsolutePath(), e);
            throw e;
        }
    }
}
