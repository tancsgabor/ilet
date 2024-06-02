package com.taga.ilet.helper;

public class LogProcessingConfiguration {

    // configObject ID to set column name in timeline display
    private String columnName;

    // columnIndex: should be unique, determines column order in display
    private int columnIndex;

    // the path to the analysed log file
    private String logFileFullPath;

    // a word which can identify the type of the log line
    private String[] lineMarkerKeywords;

    // a word which is placed to the timeline to represent the event type of the log line
    private String[] lineTimelineKeyword;

    // should the line type to be counted from first to last occurrence and represent with the first and last
    // occurrence in the timeline. Last occurrence representation contains the count.
    private Boolean[] countFromFirstToLast;

    // format of the timestamps in the logfile
    private String logDateTimeFormat;

    private String locale = "en";

    private int timestampStart;
    private int timestampEnd;

    public String getColumnName() {
        return columnName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public String getLogFileFullPath() {
        return logFileFullPath;
    }

    public String[] getLineMarkerKeywords() {
        return lineMarkerKeywords;
    }

    public String[] getLineTimelineKeyword() {
        return lineTimelineKeyword;
    }

    public Boolean[] getCountFromFirstToLast() {
        return countFromFirstToLast;
    }

    public String getLogDateTimeFormat() {
        return logDateTimeFormat;
    }

    public int getTimestampStart() {
        return timestampStart;
    }

    public int getTimestampEnd() {
        return timestampEnd;
    }

    public String getLocale() {
        return locale;
    }
}
