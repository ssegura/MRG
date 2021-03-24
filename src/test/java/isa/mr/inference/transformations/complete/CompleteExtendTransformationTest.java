package isa.mr.inference.transformations.complete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.transformations.complete.CompleteExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class CompleteExtendTransformationTest {

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
	}
	
	@Test
	public void testGetSelectableParameters() {
		CompleteExtendTransformation transformation = new CompleteExtendTransformation(sut);
		List<TestParameter> selectableParameters = transformation.getSelectableParameters(testCase);
		assertEquals("Wrong number of parameters", 1, selectableParameters.size());
		assertEquals("Wrong number of values", 6, selectableParameters.get(0).getValues().size());
		System.out.println("Selectable parameters: " + selectableParameters + "\n");
	}
	
	@Test
	public void testMatch() {
		CompleteExtendTransformation transformation = new CompleteExtendTransformation(sut);
		assertTrue("Transformation should be applicable",transformation.match(testCase));
	}

	@Test
	public void testTransform() {
		CompleteExtendTransformation transformation = new CompleteExtendTransformation(sut);
		List<TestCase> followUps = transformation.transform(testCase);
		assertNotNull("Null follow-up test case", followUps);
		assertTrue("Wrong number of follow-up test cases", followUps.size()> 0 && followUps.size() <= 6);
		System.out.println(followUps);
	}
}
