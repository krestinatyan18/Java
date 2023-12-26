package com.example.demo;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.text.ParseException;

public class  DemoApplication {

	public static void main(String[] args) throws ParseException, CsvValidationException, IOException {
		String inputFile = "/Users/krestinatyan/Desktop/Новая папка/5000000 BT Records.csv";
		String outputFile = "/Users/krestinatyan/Desktop/Новая папка/output.csv";

		long startTime = System.currentTimeMillis();
		CSVProcessor processor = new CSVProcessor();
		processor.processCsvFile(inputFile, outputFile);

		long endTime = System.currentTimeMillis();
		System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
		Runtime runtime = Runtime.getRuntime();
		long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Memory used: " + memoryUsed + " bytes");
	}
}


