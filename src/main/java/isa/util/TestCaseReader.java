package isa.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import isa.sut.SUT;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class TestCaseReader {

	public static List<TestCase> readTestCases(String filePath, SUT sut) {
		List<TestCase> testCases = new ArrayList<TestCase>();
		
		try {
			File file = new File(filePath);
			FileReader freader;
			freader = new FileReader(file);
			CsvReader reader = new CsvReader(freader, ';');
			
			// Skyp headers
			reader.readHeaders();
			
			// Read test cases
			while (reader.readRecord()) {
				String testId = reader.get(0);
				TestCase testCase = new TestCase(testId);
				for (int i=1; i< reader.getColumnCount(); i++) {
					String parameterName = reader.getHeader(i);
					String parameterValue = reader.get(i);
					if (parameterValue!="") {
						TestParameter testParameter = new TestParameter(sut.getParameterByName(parameterName), parameterValue);
						testCase.addTestParameter(testParameter);
					}	
				}
				testCases.add(testCase);
			}
			
		} catch (IOException e) {
			System.err.println("Error reading test cases: " + e.getMessage());
			e.printStackTrace();
		}
		
		return testCases;
	}
}
