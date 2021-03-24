package isa.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;

public class CSVWriter {

	CsvWriter writer;
	
	public CSVWriter(String filePath) {
		open(filePath);
	}
	

	public void open(String filePath) {
	
		// Create CSV file
		File csvFile;
		FileWriter fwriter;
		try {
			csvFile = new File(filePath);
			fwriter = new FileWriter(csvFile,false);
			writer = new CsvWriter(fwriter, ';');
		} catch (IOException e) {
			System.err.println("Error opening CSV file: " + filePath);
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		writer.close();
	}
	
	
	/**
	 * Write a collection of data. Maps contain pairs of <Header,Value>
	 * @param data
	 */
	public void write(List<Map<String,String>> data) {
	
		try {
			
			if (data==null || data.isEmpty())
				throw new IllegalArgumentException("No data to print");
			
			// Headers
			Map<String,String> firstRow = data.get(0);
			for(String header: firstRow.keySet())
				writer.write(header);
			
		    writer.flush();
		    writer.endRecord();
			
			// Data
			for (Map<String,String> row: data) {
				for(String cellValue:row.values())
					writer.write(cellValue);
			
				writer.flush();
				writer.endRecord();
			}
				
	
			writer.close();
			
		
		}catch(Exception e) {
			System.err.println("Error writing results in CSV file: " + e.getMessage());
			e.printStackTrace();
			writer.close();
			
		}
		
	}
}
