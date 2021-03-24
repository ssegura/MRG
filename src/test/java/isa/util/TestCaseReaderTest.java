package isa.util;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;

public class TestCaseReaderTest {

	@Test
	public void testReadTestCases() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		SUT sut = new SUT(spec,"GetIncidents");
		List<TestCase> testCases = TestCaseReader.readTestCases("src/test/resources/Bikewise/TestCases-GetIncidents.csv", sut);
		assertEquals("Wrong number of test cases", testCases.size(), 6);
		for(TestCase tc: testCases)
			System.out.println(tc);
	}
	
	@Test
	public void testPICTIMDbReadTestCases() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/IMDb/spec.yml");
		SUT sut = new SUT(spec,"SearchTitle");
		List<TestCase> testCases = TestCaseReader.readTestCases("src/test/resources/IMDB/testCases.csv", sut);
		assertEquals("Wrong number of test cases", testCases.size(), 20);
		for(TestCase tc: testCases)
			System.out.println(tc);
	}
	
	@Test
	public void testPICTSkyScannerReadTestCases() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/SkyScanner/spec.yml");
		SUT sut = new SUT(spec,"SearchFlights");
		List<TestCase> testCases = TestCaseReader.readTestCases("src/test/resources/SkyScanner/testCases.csv", sut);
		assertEquals("Wrong number of test cases", testCases.size(), 12);
		for(TestCase tc: testCases)
			System.out.println(tc);
	}
	
	@Test
	public void testPICTYouTubeReadTestCases() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/YouTube/spec.yml");
		SUT sut = new SUT(spec,"search");
		List<TestCase> testCases = TestCaseReader.readTestCases("src/test/resources/YouTube/testCases.csv", sut);
		assertEquals("Wrong number of test cases", testCases.size(), 64);
		for(TestCase tc: testCases)
			System.out.println(tc);
	}

}
