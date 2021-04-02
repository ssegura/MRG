package isa.main;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

public class main {

	// IMDb
//	static String sutName = "IMDb";
//	static String featureId = "SearchTitle";
	
	// Prestashop
	//static String sutName = "Prestashop";
	//static String featureId = "ListOrders";
	
	// SkyScanner
//	static String sutName = "SkyScanner";
//	static String featureId = "searchFlights";
	
	// YouTube Mock
	//static String sutName = "Bikewise";
	//static String featureId = "GetIncidents";
	
	// YouTube
	//static String sutName = "YouTube";
	//static String featureId = "search";

	// Kickstarter
	static String sutName = "Kickstarter";
	static String featureId = "DiscoverProjects";

	// Transfermarkt
//	static String sutName = "Transfermarkt";
//	static String featureId = "PersonalData";


//	// Steam
//	static String sutName = "Steam";
//	static String featureId = "SearchTheStore";

//	// iTunes
//	static String sutName = "ITunes";
//	static String featureId = "Search";

	// Slideshare
//	static String sutName = "Slideshare";
//	static String featureId = "Explore";

	// CommentsAPI
	//static String sutName = "CommentsAPI";
	//static String featureId = "GetAllComments";
	
	// Travel API
	//static String sutName = "TravelAPI";
	//static String featureId = "GetUserTrips";
	
	
	static String inputDirPath = "src/test/resources/";
	static String outputDirPath = "results/";
	static int searchStrengh = 1;				// For each MRP, this factor determines the number of MR generation tries (searchStrength * number of relevant parameters)
	static int maxMRsPerTestCase = -1;			// Maximum number of MRs of each type generated for each source test case.
	static int maxFutcs = 6;					// Maximum number of follow-up test cases
	static int numExecutions = 5;
	static long seed = 1;
	
	public static void main(String[] args) {
		
		
		// Delete the content of the output directory
		IOUtil.deleteDirContent(outputDirPath + sutName);
		
		// Overall times
		double generalGenerationTime = 0;
		long initialSeed = seed;
		
		// Overall statistics variables
		double numMetamorphicRelations = 0;
		double numConjunctiveContract = 0;
		double numConjunctiveExtend = 0;
		double numDisjunctiveContract = 0;
		double numDisjunctiveExtend = 0;
		double numDisjointExtend = 0;
		double numShufflingContract =0;
		double numShufflingExtend = 0;
		double numCompleteExtend = 0;
		double numEquivalenceExtend = 0;
		int minNumFutcs = Integer.MAX_VALUE;			// Min number of follow-up test cases
		int maxNumFutcs = Integer.MIN_VALUE;			// Max number of follow-up test cases
		double avgNumFutcs = 0;		
		
		
		if (seed == -1) {
			Random rand = new Random();
			seed = rand.nextLong();
			initialSeed = seed;
		}
			
		// Read SUT specification
		System.out.println("Reading specification...");
		Specification spec = SUTSpecification.readSpecification(inputDirPath + sutName + "/spec.yml");
		SUT sut = new SUT(spec,featureId);
		
		// Read source test cases
		System.out.print("\nReading test cases: ");
		List<TestCase> sourceTestCases = TestCaseReader.readTestCases(inputDirPath + sutName + "/testCases.csv", sut);
		System.out.println(sourceTestCases.size() + " test cases read");

		/*
		for(TestCase tc: sourceTestCases)
			System.out.println("Test case: " + tc);
		*/
		
		// Check the validity of the source test cases
		if (ConstraintChecker.checkConstraints(sut, sourceTestCases))
			System.out.println("All test cases are valid");
		else
			throw new IllegalArgumentException("Invalid source test cases. Constraints violated");
		
		for(int i=0;i<numExecutions;i++)
		{
		
			long generationTime = -1;
			

			// ===== IDENTIFICATION OF METAMORPHIC RELATIONS ======
			
			// Random generation
			System.out.print("Identifying MRs: ");
			RandomMRGenerator generator = new RandomMRGenerator();
			generator.setDomain(sutName + " (" + featureId + ")");
			generator.setSearchStrength(searchStrengh);
			generator.setMaxFutcs(maxFutcs);
			generator.setSeed(seed);
			
			if (maxMRsPerTestCase!=-1)
				generator.setMaxMRPerTestCase(maxMRsPerTestCase);
				
			long start = System.currentTimeMillis();
			Collection<MetamorphicRelation> mrs = generator.generateMR(sut, sourceTestCases);
			generationTime = System.currentTimeMillis() - start;
			System.out.println(mrs.size() + " MRs identified");
			
			if (!mrs.isEmpty()) {
				
				// Shuffle MRs
				Collections.shuffle((List)mrs,generator.getRandom());
				
				// Write MRs to CSV
				// System.out.println("Writing MRs to CSV");
				MRWriter.writeMRsToCSV(outputDirPath + sutName + "/MRs-random-" + (i+1) + ".csv", (List<MetamorphicRelation>) mrs);

				// Write MRs to extended CSV
				// System.out.println("Writing MRs to extended CSV");
				MRWriter.writeMRsToExtendedCSV(outputDirPath + sutName + "/MRs-random-extended-" + (i+1) + ".csv", (List<MetamorphicRelation>) mrs, maxFutcs);
				
				// Write MRs to Text
				// System.out.println("Writing MRs to text");
				MRWriter.writeMRsToFile(outputDirPath + sutName +  "/MRs-random-"  + (i+1) + ".txt", (List<MetamorphicRelation>) mrs);
				
			}
			
			
			// Compute stats
			if (!mrs.isEmpty()) {
				numMetamorphicRelations += mrs.size();
				MRStats stats = new MRStats(mrs);
				numCompleteExtend += stats.getNumCompleteExtend();
				numConjunctiveContract += stats.getNumConjunctiveContract();
				numConjunctiveExtend += stats.getNumConjunctiveExtend();
				numDisjointExtend += stats.getNumDisjointExtend();
				numDisjunctiveContract += stats.getNumDisjunctiveContract();
				numDisjunctiveExtend += stats.getNumDisjunctiveExtend();
				numEquivalenceExtend += stats.getNumEquivalenceExtend();
				numShufflingContract += stats.getNumShufflingContract();
				numShufflingExtend += stats.getNumShufflingExtend();
				
				if (stats.getMinNumFutcs() < minNumFutcs)
					minNumFutcs = stats.getMinNumFutcs();
				
				if (stats.getMaxNumFutcs() > maxNumFutcs)
					maxNumFutcs = stats.getMaxNumFutcs();
				
				avgNumFutcs += stats.getAvgNumFutcs();
			}

			
			generalGenerationTime += generationTime;
			
			seed += 10000;
		
		}
		
		// Print global statistics
		System.out.println("\nNumber of executions: " + numExecutions);
		System.out.println("Average generation time: " + generalGenerationTime/numExecutions);
		
		System.out.println("\nAverage number of MRs: " + numMetamorphicRelations/numExecutions);
		System.out.println("Average number of follow-up test cases: " + avgNumFutcs/numExecutions);
		System.out.println("Minimum number of follow-up test cases: " + minNumFutcs);
		System.out.println("Maximum number of follow-up test cases: " + maxNumFutcs);
		System.out.println("Average number of ConjunctiveContract MRs: " + numConjunctiveContract/numExecutions);
		System.out.println("Average number of ConjunctiveExtend MRs: " + numConjunctiveExtend/numExecutions);
		System.out.println("Average number of DisjunctiveContract MRs: " + numDisjunctiveContract/numExecutions);
		System.out.println("Average number of DisjunctiveExtend MRs: " + numDisjunctiveExtend/numExecutions);
		System.out.println("Average number of DisjointExtend MRs: " + numDisjointExtend/numExecutions);
		System.out.println("Average number of CompleteExtend MRs: " + numCompleteExtend/numExecutions);
		System.out.println("Average number of ShufflingContract MRs: " + numShufflingContract/numExecutions);
		System.out.println("Average number of ShufflingExtend MRs: " + numShufflingExtend/numExecutions);
		System.out.println("Average number of EquivalenceExtend MRs: " + numEquivalenceExtend/numExecutions);
		
		System.out.println("Seed: " + initialSeed);
		
		// Sut stats
		System.out.println();
		sut.printStatistics();
		
	}

}
