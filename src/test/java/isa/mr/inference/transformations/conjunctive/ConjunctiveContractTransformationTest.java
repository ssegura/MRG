package isa.mr.inference.transformations.conjunctive;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.conjunctive.ConjunctiveContractTransformation;
import isa.mr.inference.transformations.equivalence.EquivalenceExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class ConjunctiveContractTransformationTest {

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
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "crash"));
	}
	
	@Test
	public void testGetUnselectableParameters() {
		ConjunctiveContractTransformation transformation = new ConjunctiveContractTransformation(sut);
		List<TestParameter> unselectableParameters = transformation.getUnselectableParameters(testCase);
		assertEquals("Wrong number of parameters", 2, unselectableParameters.size());
		System.out.println("Unselectable parameters: " + unselectableParameters + "\n");
	}

	@Test
	public void testMatch() {
		ConjunctiveContractTransformation transformation = new ConjunctiveContractTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}
	
	@Test
	public void testNotApplicableMatch() {
		ConjunctiveContractTransformation transformation = new ConjunctiveContractTransformation(sut);
		
		// Test case
		TestCase testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		
		assertFalse("Transformation should not be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		ConjunctiveContractTransformation transformation = new ConjunctiveContractTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Follow-up test case has more parameters than the source test case", testCase.getTestParameters().size() > followUps.get(0).getTestParameters().size());
		System.out.println(followUps.get(0));
	}
}
