package com.example.demo;

public class CsvRecord {
    private String date;
    private String description;
    private double min;
    private double max;
    private double avg;

    public CsvRecord(String date, String description, double min, double max, double avg) {
        this.date = date;
        this.description = description;
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    public String[] toArrayWithDescription() {
        return new String[]{date, "Description=\"" + description + "\"", String.valueOf(min), String.valueOf(max), String.valueOf(avg)};
    }

    public String[] toArray() {
        return new String[]{date, description, String.valueOf(min), String.valueOf(max), String.valueOf(avg)};
    }

}
