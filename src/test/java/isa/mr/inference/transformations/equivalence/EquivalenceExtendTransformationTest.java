package isa.mr.inference.transformations.equivalence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.equivalence.EquivalenceExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class EquivalenceExtendTransformationTest {

	static SUT sut;
	static TestCase testCase;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// TestCase
		testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Accident"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
	}
	
	@Test
	public void testGetSelectableParameters() {
		EquivalenceExtendTransformation transformation = new EquivalenceExtendTransformation(sut);
		List<TestParameter> selectableParameters = transformation.getSelectableParameters(testCase);
		assertEquals("Wrong number of parameters", 1, selectableParameters.size());
		System.out.println("Selectable parameters: " + selectableParameters + "\n");
	}

	@Test
	public void testMatch() {
		EquivalenceExtendTransformation transformation = new EquivalenceExtendTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}
	
	@Test
	public void testNotApplicableMatch() {
		EquivalenceExtendTransformation transformation = new EquivalenceExtendTransformation(sut);
		
		// TestCase
		TestCase testCase = new TestCase("Test-01", "GetIncidents");
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Accident"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		testCase.addTestParameter(new TestParameter(sut.getParameterByName("page"), "1"));
		
		assertFalse("Transformation should not be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		EquivalenceExtendTransformation transformation = new EquivalenceExtendTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Follow-up test case has less parameters than the source test case", testCase.getParameters().size() < followUps.get(0).getParameters().size());
		System.out.println(followUps.get(0));
	}

}
