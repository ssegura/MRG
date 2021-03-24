package isa.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;
import isa.util.Diff;
import isa.util.TestCaseDiff;

public class TestCaseDiffTest {

	static SUT sut;
	static TestCase tc1,tc2,tc3,tc4;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// Test case 1
		tc1 = new TestCase("Test-01", "GetIncidents");
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));
		
		// Test case 2
		tc2 = new TestCase("Test-02", "GetIncidents");
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc2.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		
		// Test case 3
		tc3 = new TestCase("Test-03", "GetIncidents");
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		tc3.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "crash"));
		
		// Test case 4
		tc4 = new TestCase("Test-04", "GetIncidents");
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc4.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "New York"));
	}

	@Test
	public void testTestCaseDiffRemovedParameters() {
		Diff diff = TestCaseDiff.testCaseDiff(tc1, tc2);
		assertTrue("Incorrect number of removed parameters", diff.getRemovedParameters().size()==1);
		assertTrue("Incorrect number of added parameters", diff.getAddedParameters().isEmpty());
		assertTrue("Incorrect number of changed parameters", diff.getChangedParameters().isEmpty());
		System.out.println("Removed parameters: " + diff.getRemovedParameters());
	}
	
	@Test
	public void testTestCaseDiffAddedParameters() {
		Diff diff = TestCaseDiff.testCaseDiff(tc2, tc3);
		assertTrue("Incorrect number of removed parameters", diff.getRemovedParameters().isEmpty());
		assertTrue("Incorrect number of added parameters", diff.getAddedParameters().size()==1);
		assertTrue("Incorrect number of changed parameters", diff.getChangedParameters().isEmpty());
		System.out.println("Added parameters: " + diff.getAddedParameters());
	}
	
	@Test
	public void testTestCaseDiffChangedParameters() {
		Diff diff = TestCaseDiff.testCaseDiff(tc1, tc3);
		assertTrue("Incorrect number of removed parameters", diff.getRemovedParameters().isEmpty());
		assertTrue("Incorrect number of added parameters", diff.getAddedParameters().isEmpty());
		assertTrue("Incorrect number of changed parameters", diff.getChangedParameters().size()==1);
		System.out.println("Changed parameters: " + diff.getChangedParameters());
	}
	
	@Test
	public void testTestCaseDiffAddedAndChangedParameters() {
		Diff diff = TestCaseDiff.testCaseDiff(tc1, tc4);
		assertTrue("Incorrect number of removed parameters", diff.getRemovedParameters().size()==2);
		assertTrue("Incorrect number of added parameters", diff.getAddedParameters().isEmpty());
		assertTrue("Incorrect number of changed parameters", diff.getChangedParameters().size()==1);
		System.out.println("Removed parameters: " + diff.getRemovedParameters());
		System.out.println("Changed parameters: " + diff.getChangedParameters());
	}

}
