package isa.mr.inference.generators;

import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static isa.mr.inference.generators.MetamorphicRelation.removeSubsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DuplicateDetectionTest {

	static SUT sut;
	static List<TestCase> sourceTestCases;
	static TestCase tc1;
	static MetamorphicRelation mrSubset;
	static MetamorphicRelation mrSuperset;

	@BeforeClass
	public static void setUp() throws Exception {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		sut = new SUT(spec,"GetIncidents");
		
		// Source test case
		tc1 = new TestCase("Test-01", "GetIncidents");
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("query"), "Bike"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity"), "Chicago"));
		tc1.addTestParameter(new TestParameter(sut.getParameterByName("proximity_square"), "200"));
		
		sourceTestCases = new ArrayList<TestCase>();
		sourceTestCases.add(tc1);

		// MR SUBSET
		// Follow up test case of mrSubset
		TestCase futc1 = tc1.clone();
		futc1.setId("MR1");
		futc1.setOperationId("GetIncidents");
		futc1.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));

		// Create mrSubset
		mrSubset = new MetamorphicRelation("MR1", "Bikewise", tc1, Collections.singletonList(futc1));
		mrSubset.setType(MetamorphicRelationType.CONJUNCTIVE_EXTEND);


		// MR SUPERSET
		// Follow up test case of mrSuperset
		TestCase futc2 = tc1.clone();
		futc2.addTestParameter(new TestParameter(sut.getParameterByName("occurred_before"), "200"));

		TestCase futc3 = futc2.clone();
		futc3.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));

		List<TestCase> futcMR2 = new ArrayList<>();
		futcMR2.add(futc2);
		futcMR2.add(futc3);

		// Create mrSuperset
		mrSuperset = new MetamorphicRelation("MR2", "Bikewise", tc1, futcMR2);
		mrSuperset.setType(MetamorphicRelationType.CONJUNCTIVE_EXTEND);

	}
	
	@Test
	public void testPermutations() {
		// Two MR containing the same futcs in different orders are consireded equivalent

		// Metamorphic Relation 1 (mr1)
		// Follow up test cases of mr1
		TestCase futc1 = tc1.clone();
		futc1.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));

		TestCase futc2 = futc1.clone();
		futc2.addTestParameter(new TestParameter(sut.getParameterByName("occurred_before"), "200"));

		// Add futcs to list
		List<TestCase> futcMR1 = new ArrayList<>();
		futcMR1.add(futc1);
		futcMR1.add(futc2);

		// Create mr1
		MetamorphicRelation mr1 = new MetamorphicRelation("MR1", "Bikewise", tc1, futcMR1);
		mr1.setType(MetamorphicRelationType.CONJUNCTIVE_EXTEND);

		// Metamorphic Relation 2 (mr2)
		// Follow up test cases of mr2
		TestCase futc3 = tc1.clone();
		futc3.addTestParameter(new TestParameter(sut.getParameterByName("occurred_before"), "200"));

		TestCase futc4 = futc3.clone();
		futc4.addTestParameter(new TestParameter(sut.getParameterByName("incident_type"), "all"));


		// Add futcs to list
		List<TestCase> futcMR2 = new ArrayList<>();
		futcMR2.add(futc3);
		futcMR2.add(futc4);

		// Create mr2
		MetamorphicRelation mr2 = new MetamorphicRelation("MR2", "Bikewise", tc1, futcMR2);
		mr2.setType(MetamorphicRelationType.CONJUNCTIVE_EXTEND);

//		System.out.println(mr1);
//		System.out.println(mr2);

		// Create a list of mrs and add only mr1
		List<MetamorphicRelation> mrs = new ArrayList<>();
		mrs.add(mr1);

		// mr1 and mr2 must be considered equals
		assertEquals("Two identical MRs with the futcs in different order must be considered as equals", mr1, mr2);
		assertTrue("Two identical MRs with the futcs in different order must be considered as equals", mrs.contains(mr2));

	}

	@Test
	public void testRemoveSubsets(){
		// After adding a new MR to the list of MRs, all the subsets of that MR are removed
		List<MetamorphicRelation> mrs = new ArrayList<>();
		mrs.add(mrSubset);

//		System.out.println(mrSubset);
//		System.out.println(mrSuperset);

		// After calling this function, mrs should not contain mrSubset
		removeSubsets(mrs, mrSuperset);
		assertEquals("Subset not deleted properly", 0, mrs.size());

	}

	@Test
	public void testCheckSupersets(){
		// If a new MR is created, it will not be added to the list of MRs if it is a subset of another MR already present in the list
		assertTrue("Superset not detected", mrSubset.containsSupersets(Collections.singletonList(mrSuperset)));

	}

}
