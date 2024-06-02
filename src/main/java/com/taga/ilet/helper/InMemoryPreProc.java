package com.taga.ilet.helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InMemoryPreProc {

    private static final String COLUMN_INDENT = "                                           ";

    private List<List<String>> partialResults = new ArrayList<>();
    private List<List<Date>> partialResultDates = new ArrayList<>();
    private List<List<LogProcessingConfiguration>> partialConfigOrder = new ArrayList<>();

    private List<Date> dates = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private List<LogProcessingConfiguration> configs = new ArrayList<>();

    private String loadLogFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    private String determineLineSeparator(String currentLog) {
        if (currentLog.contains("\r\n")) {
            return "\r\n";
        } else return "\n";
    }

    private Date getTimestamp(String currentLogLine, int from, int to, String dateFormat, String locale) throws ParseException {
        Locale localeObject=new Locale(locale);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat,localeObject);
        String datePart = currentLogLine.substring(from, to);
        return simpleDateFormat.parse(datePart);
    }

    /*
      - reads log lines and store info about them in "partial" collections the same order as they are in the logs
      - parses timestamp from a line and stores java util date value instead of string
      - filters lines with marker words, everything else is thrown away
      - stores timeline words from the config instead of the marker word
    */
    public void preprocess(LogProcessingConfiguration config) throws IOException, ParseException {
        String currentLog = loadLogFile(config.getLogFileFullPath());
        String separator = determineLineSeparator(currentLog);
        String[] lines = currentLog.split(separator);
        currentLog = null;
        List<String> matchingLines = new ArrayList<>();
        List<Date> matchingLineDates = new ArrayList<>();
        List<LogProcessingConfiguration> matchingLinesConfigs = new ArrayList<>();

        for (String line : lines) {
            for (int x = 0; x < config.getLineMarkerKeywords().length; x++) {
                if (line.contains(config.getLineMarkerKeywords()[x])) {
                    matchingLines.add(config.getLineTimelineKeyword()[x]);
                    matchingLineDates.add(getTimestamp(line, config.getTimestampStart(), config.getTimestampEnd(), config.getLogDateTimeFormat(), config.getLocale()));
                    matchingLinesConfigs.add(config);
                }
            }
        }
        partialResults.add(matchingLines);
        partialResultDates.add(matchingLineDates);
        partialConfigOrder.add(matchingLinesConfigs);
    }

    // displays all partial processing
    public void getPartialResultDisplay() {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,SSS");
        for (int log = 0; log < partialResultDates.size(); log++) {
            for (int line = 0; line < partialResultDates.get(log).size(); line++) {
                String dateOfLine = displayDateFormat.format(partialResultDates.get(log).get(line));
                System.out.println(dateOfLine + " " + partialResults.get(log).get(line));
            }
        }
    }

    // displays timeline in single column
    public void getTimelineFlowResultDisplay() {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,SSS");
        for (int line = 0; line < dates.size(); line++) {
            String dateOfLine = displayDateFormat.format(dates.get(line));
            System.out.println(dateOfLine + " " + texts.get(line) + " " + configs.get(line).getColumnName());
        }
    }

    private String getTabString(int columnIndex) {
        String tab = "";
        for (int tabCount = 0; tabCount < columnIndex; tabCount++) {
            tab += COLUMN_INDENT;
        }
        return tab;
    }

    private String getHeader(List<LogProcessingConfiguration> configStorage){
        String[] columnNamesInOrder = new String[configStorage.size()];
        for (LogProcessingConfiguration config : configStorage){
            columnNamesInOrder[config.getColumnIndex()]=config.getColumnName();
        }
        String header = "";
        for (String columnName : columnNamesInOrder){
            header = header + columnName + COLUMN_INDENT;
        }
        return header;
    }

    // displays timeline in multiple columns
    public void getTimelineColumnResultDisplay(List<LogProcessingConfiguration> configStorage) {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,SSS");
        System.out.println(getHeader(configStorage));
        for (int line = 0; line < dates.size(); line++) {
            String dateOfLine = displayDateFormat.format(dates.get(line));
            String tabs = getTabString(configs.get(line).getColumnIndex());
            System.out.println(tabs + dateOfLine + " " + texts.get(line));
        }
    }

    // returns the config index of the oldest date in the current processed logs
    // only index zeroes should be compared as partial result lists are ordered and increasing in time
    // because contain log lines which are naturally have this feature
    private int getOldestLineIndex() {
        int minimumDateIndex = -1;
        Date currentMinimumDate = new Date(Long.MAX_VALUE);
        for (int i = 0; i < partialResultDates.size(); i++) {
            if (partialResultDates.get(i).size() == 0) {
                continue;
            }
            Date currentCandidate = partialResultDates.get(i).getFirst();
            if (currentCandidate.before(currentMinimumDate) || currentCandidate.equals(currentMinimumDate)) {
                minimumDateIndex = i;
                currentMinimumDate = currentCandidate;
            }
        }
        return minimumDateIndex;
    }

    // process "partial" collections line by line and removes the processed lines until all collection is empty
    // stores the line in date order in "dates", "texts" and configs
    public void createTimeline() {
        int nextLineWithMinimumDate = 0;
        while (true) {
            nextLineWithMinimumDate = getOldestLineIndex();
            if (nextLineWithMinimumDate == -1) {
                break;
            }
            if (partialResultDates.get(nextLineWithMinimumDate).size() > 0)
                dates.add(partialResultDates.get(nextLineWithMinimumDate).getFirst());

            if (partialResults.get(nextLineWithMinimumDate).size() > 0)
                texts.add(partialResults.get(nextLineWithMinimumDate).getFirst());

            if (partialConfigOrder.get(nextLineWithMinimumDate).size() > 0)
                configs.add(partialConfigOrder.get(nextLineWithMinimumDate).getFirst());

            if (partialResultDates.get(nextLineWithMinimumDate).size() > 0)
                partialResultDates.get(nextLineWithMinimumDate).remove(0);

            if (partialResults.get(nextLineWithMinimumDate).size() > 0)
                partialResults.get(nextLineWithMinimumDate).remove(0);

            if (partialConfigOrder.get(nextLineWithMinimumDate).size() > 0)
                partialConfigOrder.get(nextLineWithMinimumDate).remove(0);
        }
    }

}
