package isa.mr.inference.generators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.mr.inference.generators.RandomMRGenerator;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class RandomMRGeneratorTest {

	static SUT sut;
	static List<TestCase> sourceTestCases;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// Test case 1
		TestCase tc1 = new TestCase("Test-01", "GetIncidents");
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));
		
		// Test case 2
		TestCase tc2 = new TestCase("Test-02", "GetIncidents");
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		
		// Test case 3
		TestCase tc3 = new TestCase("Test-03", "GetIncidents");
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "crash"));
		
		// Test case 4
		TestCase tc4 = new TestCase("Test-04", "GetIncidents");
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "New York"));
		
		// Test case 5
		TestCase tc5 = new TestCase("Test-05", "GetIncidents");
		tc5.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Accident"));
		tc5.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		
		// Test case 6
		TestCase tc6 = new TestCase("Test-01", "GetIncidents");
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("order"), "date"));
		
		sourceTestCases = new ArrayList<TestCase>();
		sourceTestCases.add(tc1);
		sourceTestCases.add(tc2);
		sourceTestCases.add(tc3);
		sourceTestCases.add(tc4);
		sourceTestCases.add(tc5);
		sourceTestCases.add(tc6);
	}

	@Test
	public void testGenerateMR() {
		RandomMRGenerator generator = new RandomMRGenerator();
		Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
		assertTrue("No metamorphic relations generated", mrs.size()!=0);
		for(MetamorphicRelation mr: mrs) {
			System.out.println(mr);
		}
	}
	
	@Test
	public void testGenerateMRWithSeed() {
		RandomMRGenerator generator = new RandomMRGenerator();
		Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
		int numMRs = mrs.size();
		long seed = generator.getSeed();
		
		for(int i=0;i<10;i++) {
			generator = new RandomMRGenerator();
			generator.setSeed(seed);
			mrs = generator.generateMR(sut, sourceTestCases);
			assertTrue("Seed not working. Different number of MRs generated", mrs.size()==numMRs);
		}
	}
	
	
	@Test
	public void testGenerateMRWithConstraints() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-constraints.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// Test case 1
		TestCase tc1 = new TestCase("Test-01", "GetIncidents");
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));
		
		// Test case 2
		TestCase tc2 = new TestCase("Test-02", "GetIncidents");
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		
		// Test case 3
		TestCase tc3 = new TestCase("Test-03", "GetIncidents");
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "crash"));
		
		// Test case 4
		TestCase tc4 = new TestCase("Test-04", "GetIncidents");
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "New York"));
		
		// Test case 5
		TestCase tc5 = new TestCase("Test-05", "GetIncidents");
		tc5.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Accident"));
		tc5.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		
		// Test case 6
		TestCase tc6 = new TestCase("Test-01", "GetIncidents");
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc6.addTestParameter(new TestParameter(sut.getParameterByName("order"), "date"));
		
		sourceTestCases = new ArrayList<TestCase>();
		sourceTestCases.add(tc1);
		sourceTestCases.add(tc2);
		sourceTestCases.add(tc3);
		sourceTestCases.add(tc4);
		sourceTestCases.add(tc5);
		sourceTestCases.add(tc6);
		
		RandomMRGenerator generator = new RandomMRGenerator();
		Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
		assertTrue("No metamorphic relations generated", mrs.size()!=0);
		for(MetamorphicRelation mr: mrs) {
			System.out.println(mr);
		}
	}
	
	

}
