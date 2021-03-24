package isa.mr.inference.transformations.conjunctive;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.conjunctive.ConjunctiveExtendTransformation;
import isa.mr.inference.transformations.disjoint.DisjointExtendTransformation;
import isa.mr.inference.transformations.equivalence.EquivalenceExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class ConjunctiveExtendTransformationTest {

	static SUT sut;
	static TestCase testCase;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// TestCase
		testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "New York"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
	}

	@Test
	public void testGetSelectableParameters() {
		ConjunctiveExtendTransformation transformation = new ConjunctiveExtendTransformation(sut);
		List<TestParameter> selectableParameters = transformation.getSelectableParameters(testCase);
		assertEquals("Wrong number of parameters", 3, selectableParameters.size());
		System.out.println("Selectable parameters: " + selectableParameters + "\n");
	}

	@Test
	public void testMatch() {
		ConjunctiveExtendTransformation transformation = new ConjunctiveExtendTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		ConjunctiveExtendTransformation transformation = new ConjunctiveExtendTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Follow-up test case has less parameters than the source test case", testCase.getTestParameters().size() < followUps.get(0).getTestParameters().size());
		System.out.println(followUps.get(0));
	}
}
