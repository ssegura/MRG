package isa.mr.inference.transformations.shuffling;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.disjoint.DisjointExtendTransformation;
import isa.mr.inference.transformations.shuffling.ShufflingExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class ShufflingExtendTransformationTest {

	static SUT sut;
	static TestCase testCase;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// TestCase
		testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "crash"));
	}
	
	@Test
	public void testGetSelectableParametersNotIncludedInTC() {
		ShufflingExtendTransformation transformation = new ShufflingExtendTransformation(sut);
		List<TestParameter> selectableParameters = transformation.getSelectableParameters(testCase);
		assertEquals("Wrong number of parameters", 1, selectableParameters.size());
		assertEquals("Wrong number of values", 3, selectableParameters.get(0).getValues().size());
		System.out.println("Selectable parameters: " + selectableParameters + "\n");
	}
	
	@Test
	public void testGetSelectableParametersIncludedInTC() {
		ShufflingExtendTransformation transformation = new ShufflingExtendTransformation(sut);
		
		// TestCase
		TestCase testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("order"), "date"));

		List<TestParameter> selectableParameters = transformation.getSelectableParameters(testCase);
		assertEquals("Wrong number of parameters", 1, selectableParameters.size());
		assertEquals("Wrong number of values", 2, selectableParameters.get(0).getValues().size());
		System.out.println("Selectable parameters: " + selectableParameters + "\n");
	}
	
	@Test
	public void testMatch() {
		ShufflingExtendTransformation transformation = new ShufflingExtendTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		ShufflingExtendTransformation transformation = new ShufflingExtendTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Wrong number of follow-up test cases", followUps.size()> 0 && followUps.size() <= 3);
		System.out.println(followUps);
	}
	
	@Test
	public void testTransformReplaceValue() {
		ShufflingExtendTransformation transformation = new ShufflingExtendTransformation(sut);
		
		// TestCase
		TestCase testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("order"), "date"));

		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Wrong number of follow-up test cases", followUps.size()> 0 && followUps.size() <= 2);
		System.out.println(followUps);
	}
}
