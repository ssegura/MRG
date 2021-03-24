package isa.main;

import java.util.Collection;
import java.util.List;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.mr.inference.generators.RandomMRGenerator;
import isa.mr.prioritisation.GlobalMRPrioritisation;
import isa.mr.prioritisation.LocalMRPrioritisation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.util.MRWriter;
import isa.util.TestCaseReader;

public class Bikewise {

	static String dirPath = "results/Bikewise/";
	static String sutName = "Bikewise";
	static String featureId = "GetIncidents";
	static int searchStrengh = 10;			// For each MRP, this factor determines the number of MR generation tries (searchStrength * number of relevant parameters)
	static int maxFutcs = 2;				// Maximum number of follow-up test cases
	
	public static void main(String[] args) {
		
		// Read SUT specification
		System.out.println("Reading specification...");
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		SUT sut = new SUT(spec,"GetIncidents");
		
		// Read source test cases
		System.out.print("Reading test cases: ");
		List<TestCase> sourceTestCases = TestCaseReader.readTestCases("src/test/resources/Bikewise/TestCases-GetIncidents.csv", sut);
		System.out.println(sourceTestCases.size() + " test cases read");
		
		// ===== GENERATION (I.E. INFERENCE) OF METAMORPHIC RELATIONS ======
		
		// Random generation
		System.out.print("Infering MRs: ");
		RandomMRGenerator generator = new RandomMRGenerator();
		generator.setDomain(sutName + " (" + featureId + ")");
		generator.setSearchStrength(searchStrengh);		
		generator.setMaxFutcs(maxFutcs);
		Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
		System.out.println(mrs.size() + " MRs infered");
		
		if (!mrs.isEmpty()) {
			// Write MRs to CSV
			System.out.println("Writing MRs to CSV");
			MRWriter.writeMRsToCSV(dirPath + "MRs-random.csv", (List<MetamorphicRelation>) mrs);
			
			// Write MRs to Text
			System.out.println("Writing MRs to text");
			MRWriter.writeMRsToFile(dirPath + "MRs-random.txt", (List<MetamorphicRelation>) mrs);
			
			
			// ====== PRIORITISATION (LOCAL DISTANCE) ======
			
			System.out.println("\nOrdering MRs based on local distance");
			List<MetamorphicRelation> orderedMRsLocal = LocalMRPrioritisation.prioritise(mrs);
			
			// Write MRs to CSV
			System.out.println("Writing prioritised MRs to CSV");
			MRWriter.writeMRsToCSV(dirPath + "priortisedMRs-localDistance.csv", (List<MetamorphicRelation>) orderedMRsLocal);
			
			// Write MRs to Text
			System.out.println("Writing prioritised MRs to text");
			MRWriter.writeMRsToFile(dirPath + "priortisedMRs-localDistance.txt", (List<MetamorphicRelation>) orderedMRsLocal);
			
	
			// ====== PRIORITISATION (GLOBAL DISTANCE) ======
			
			System.out.println("\nOrdering MRs based on global distance");
			List<MetamorphicRelation> orderedMRsGlobal = GlobalMRPrioritisation.prioritise(mrs);
			
			// Write MRs to CSV
			System.out.println("Writing prioritised MRs to CSV");
			MRWriter.writeMRsToCSV(dirPath + "priortisedMRs-globalDistance.csv", (List<MetamorphicRelation>) orderedMRsGlobal);
			
			// Write MRs to Text
			System.out.println("Writing prioritised MRs to text");
			MRWriter.writeMRsToFile(dirPath + "priortisedMRs-globalDistance.txt", (List<MetamorphicRelation>) orderedMRsGlobal);
		}
	}

}
