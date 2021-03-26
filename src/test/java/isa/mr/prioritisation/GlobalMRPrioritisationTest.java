package isa.mr.prioritisation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.mr.inference.generators.RandomMRGenerator;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.util.TestCaseReader;

public class GlobalMRPrioritisationTest {

	static SUT sut;
	static List<TestCase> sourceTestCases;
	static List<MetamorphicRelation> mrs;
	
	@BeforeClass
	public static void setUp() throws Exception {
		// Read SUT specification
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		sut = new SUT(spec,"GetIncidents");
				
		// Read source test cases
		sourceTestCases = TestCaseReader.readTestCases("src/test/resources/Bikewise/TestCases-GetIncidents.csv", sut);
		
		// Generate MRs
		RandomMRGenerator generator = new RandomMRGenerator();
		generator.setSeed(2500);
		mrs = new ArrayList<MetamorphicRelation>(generator.generateMR(sut, sourceTestCases));
		System.out.println("Generated MRs: " + mrs.size());
	}

	@Test
	public void testPrioritise() {
		List<MetamorphicRelation> orderedMRs = GlobalMRPrioritisation.prioritise(mrs);
		assertEquals("Prioritisation should not change the number of MRs",mrs.size(), orderedMRs.size());
	}
	
	@Test
	public void testCalculateGlobalDistance() {
		int distance = GlobalMRPrioritisation.calculateGlobaldistance(mrs.get(0),mrs.get(12));
		System.out.println(mrs.get(0).printSimpleFormat());
		System.out.println(mrs.get(12).printSimpleFormat());
		assertEquals("Wrong distance",14, distance);
	}

}
