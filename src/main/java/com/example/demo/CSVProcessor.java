package com.example.demo;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVProcessor {

    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    public void processCsvFile(String inputFile, String outputFile) throws
            IOException, CsvValidationException, ParseException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(inputFile)).withSkipLines(1).build();
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            String[] outputHeader = {"Date", "Type", "Min", "Max", "Avg"};
            writer.writeNext(outputHeader);

            processCsvData(reader, writer);

        }
    }
    private Map<String, Map<String, List<Double>>> readCsvData(CSVReader reader) throws
            IOException, CsvValidationException, ParseException {
        Map<String, Map<String, List<Double>>> dataMap = new HashMap<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            String date = nextLine[0];
            String description = nextLine[1];
            double withdrawal = parseWithdrawal(nextLine[3]);

            String formattedDate = formatDateString(date);

            dataMap.computeIfAbsent(formattedDate, k -> new HashMap<>());
            dataMap.get(formattedDate).computeIfAbsent(description, k -> new ArrayList<>());
            dataMap.get(formattedDate).get(description).add(withdrawal);
        }

        return dataMap;
    }
    private void writeCsvResults(CSVWriter writer,
                                 Map<String, Map<String, List<Double>>> dataMap) {
        for (Map.Entry<String, Map<String, List<Double>>> entry : dataMap.entrySet()) {
            String currentDate = entry.getKey();
            Map<String, List<Double>> descriptionMap = entry.getValue();

            List<Double> totalWithdrawalsList = new ArrayList<>();

            for (Map.Entry<String, List<Double>> descriptionEntry : descriptionMap.entrySet()) {
                String currentDescription = descriptionEntry.getKey();
                List<Double> withdrawalsList = descriptionEntry.getValue();

                writeResultsToFile(writer, currentDate, currentDescription, withdrawalsList);

                totalWithdrawalsList.addAll(withdrawalsList);
            }

            writeTotalResultsToFile(writer, currentDate, totalWithdrawalsList);
        }
    }


    private void writeResultsToFile(CSVWriter writer, String currentDate, String currentDescription, List<Double> withdrawalsList) {
        double minWithdrawal = Collections.min(withdrawalsList);
        double maxWithdrawal = Collections.max(withdrawalsList);
        double avgWithdrawal = calculateAverage(withdrawalsList);

        CsvRecord record = new CsvRecord(currentDate, currentDescription, minWithdrawal, maxWithdrawal, avgWithdrawal);
        writer.writeNext(record.toArrayWithDescription());
    }

    private void writeTotalResultsToFile(CSVWriter writer, String currentDate, List<Double> totalWithdrawalsList) {
        double totalMinWithdrawal = Collections.min(totalWithdrawalsList);
        double totalMaxWithdrawal = Collections.max(totalWithdrawalsList);
        double totalAvgWithdrawal = calculateAverage(totalWithdrawalsList);

        CsvRecord totalRecord = new CsvRecord(currentDate, "", totalMinWithdrawal, totalMaxWithdrawal, totalAvgWithdrawal);
        writer.writeNext(totalRecord.toArray());
    }


    private double parseWithdrawal(String withdrawal) {
        return Double.parseDouble(withdrawal.replaceAll("\"", "").replace(",", ""));
    }
    private void processCsvData(CSVReader reader, CSVWriter writer)
            throws IOException, CsvValidationException, ParseException {
        Map<String, Map<String, List<Double>>> dataMap = readCsvData(reader);
        writeCsvResults(writer, dataMap);
    }

    protected double calculateAverage(List<Double> list) {
        return list.parallelStream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(00.00);
    }

    private String formatDateString(String inputDate) throws ParseException {
        Date parsedDate = inputDateFormat.parse(inputDate);
        return outputDateFormat.format(parsedDate);
    }
}

