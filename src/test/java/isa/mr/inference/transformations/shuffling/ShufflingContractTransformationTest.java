package isa.mr.inference.transformations.shuffling;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.conjunctive.ConjunctiveContractTransformation;
import isa.mr.inference.transformations.shuffling.ShufflingContractTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class ShufflingContractTransformationTest {

	static SUT sut;
	static TestCase testCase;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// TestCase
		testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("order"), "date"));
	}
	
	@Test
	public void testGetUnselectableParameters() {
		ShufflingContractTransformation transformation = new ShufflingContractTransformation(sut);
		List<TestParameter> unselectableParameters = transformation.getUnselectableParameters(testCase);
		assertEquals("Wrong number of parameters", 1, unselectableParameters.size());
		System.out.println("Unselectable parameters: " + unselectableParameters + "\n");
	}

	@Test
	public void testMatch() {
		ShufflingContractTransformation transformation = new ShufflingContractTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}
	
	@Test
	public void testMatchNotApplicable() {
		ShufflingContractTransformation transformation = new ShufflingContractTransformation(sut);
		
		TestCase testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		
		assertFalse("Transformation should not be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		ShufflingContractTransformation transformation = new ShufflingContractTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		System.out.println(followUps);
	}

}
