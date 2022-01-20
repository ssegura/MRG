package isa.main;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.mr.inference.generators.RandomMRGenerator;
import isa.mr.prioritisation.GlobalMRPrioritisation;
import isa.mr.prioritisation.LocalMRPrioritisation;
import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;
import isa.testcases.TestCase;
import isa.util.ConstraintChecker;
import isa.util.IOUtil;
import isa.util.MRStats;
import isa.util.MRWriter;
import isa.util.TestCaseReader;

public class MainOneIteration {


//	// Travel API
//	static String sutName = "TravelAPI";
//	static String featureId = "GetUserTrips";

	// Travel API Random
	//static String sutName = "TravelAPIRandom";
	//static String featureId = "GetUserTrips";
	
	// YouTube Mock
	static String sutName = "YouTubeMock";
	static String featureId = "search";
	
//	// Events API
	//static String sutName = "EventsAPI";
	//static String featureId = "GetAllEvents";

//	// CommentsAPI
	//static String sutName = "CommentsAPI";
	//static String featureId = "GetAllComments";

//	// MemesAPI
//	static String sutName = "MemesAPI";
//	static String featureId = "GetAllMemes";
	
	
	static String inputDirPath = "src/test/resources/";
	static String outputDirPath = "results/";
	static int searchStrengh = 1;			// For each MRP, this factor determines the number of MR generation tries (searchStrength * number of relevant parameters)
	static int maxMRsPerTestCase = 1;			// Maximum number of MRs of each type generated for each source test case.
	static int maxFutcs = 6;				// Maximum number of follow-up test cases
	static boolean prioritisation = true;
	static int numRandomOrderings = 10;		// Number of random orderings (output files) to be generated.
	static long seed = -5790795521924900565L;
	
	public static void main(String[] args) {
		
		
		// Delete the content of the output directory
		IOUtil.deleteDirContent(outputDirPath + sutName);
		

		// Read SUT specification
		System.out.println("Reading specification...");
		Specification spec = SUTSpecification.readSpecification(inputDirPath + sutName + "/spec.yml");
		SUT sut = new SUT(spec,featureId);
		
		// Read source test cases
		System.out.print("\nReading test cases: ");
		List<TestCase> sourceTestCases = TestCaseReader.readTestCases(inputDirPath + sutName + "/testCases.csv", sut);
		System.out.println(sourceTestCases.size() + " test cases read");
		
		// Check the validity of the source test cases
		if (ConstraintChecker.checkConstraints(sut, sourceTestCases))
			System.out.println("All test cases are valid");
		else
			throw new IllegalArgumentException("Invalid source test cases. Constraints violated");
		
		
			// ===== IDENTIFICATION OF METAMORPHIC RELATIONS ======
			
			// Random generation
			System.out.print("Identifying MRs: ");
			RandomMRGenerator generator = new RandomMRGenerator();
			generator.setDomain(sutName + " (" + featureId + ")");
			generator.setSearchStrength(searchStrengh);
			generator.setMaxFutcs(maxFutcs);
			generator.setMaxMRPerTestCase(maxMRsPerTestCase);
			if (seed!=-1)
				generator.setSeed(seed);
			else
				seed = generator.getSeed();
			Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
			System.out.println(mrs.size() + " MRs identified");
			
			if (!mrs.isEmpty()) {
				
				
				for(int i=0;i<numRandomOrderings;i++)
				{
				
					// Shuffle MRs
					Collections.shuffle((List)mrs,generator.getRandom());
					
					// Write MRs to CSV
					//System.out.println("Writing MRs to CSV");
					MRWriter.writeMRsToCSV(outputDirPath + sutName + "/MRs-random-" + (i+1) + ".csv", (List<MetamorphicRelation>) mrs);
					
					// Write MRs to Text
					//System.out.println("Writing MRs to text");
					MRWriter.writeMRsToFile(outputDirPath + sutName +  "/MRs-random-"  + (i+1) + ".txt", (List<MetamorphicRelation>) mrs);
				}
				
			}
			
			// Prioritisation
			if (!mrs.isEmpty() && prioritisation) {
				
				// ====== PRIORITISATION ======
				
				System.out.println("\nOrdering MRs");
				List<MetamorphicRelation> orderedMRsGlobal = GlobalMRPrioritisation.prioritise(mrs);
				
				// Write MRs to CSV
				//System.out.println("Writing prioritised MRs to CSV");
				MRWriter.writeMRsToCSV(outputDirPath + sutName + "/priortisedMRsGlobal.csv", (List<MetamorphicRelation>) orderedMRsGlobal);
				
				// Write MRs to Text
				//System.out.println("Writing prioritised MRs to text");
				MRWriter.writeMRsToFile(outputDirPath + sutName +  "/priortisedMRsGlobal.txt", (List<MetamorphicRelation>) orderedMRsGlobal);
			}
			
			System.out.println("Seed: " + seed);
		
	}

}
