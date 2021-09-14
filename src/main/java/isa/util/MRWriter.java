package isa.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.testcases.TestCase;
import org.junit.Test;

import static isa.mr.inference.generators.MetamorphicRelation.printParameterNames;
import static isa.mr.inference.generators.MetamorphicRelation.printParameters;
import static isa.mr.inference.generators.MetamorphicRelationType.*;

//import static isa.mr.inference.generators.MetamorphicRelation.printParameterNames;


public class MRWriter {

	/**
	 * 
	 * @param filePath File path of CVS file (rewritten if it already exists)
	 * @param mrs Metamorphic relations
	 */
	public static void writeMRsToCSV(String filePath, List<MetamorphicRelation> mrs) {

		List<Map<String,String>> data = new ArrayList<Map<String,String>>();

		for(MetamorphicRelation mr: mrs) {
			Map<String,String> mrData = new LinkedHashMap<String,String>();

			mrData.put("MR", mr.getIdentifier());
			mrData.put("Type", mr.getType().name());
			mrData.put("STC", mr.getSourceTestCase().getId());
			mrData.put("FUTCs", Integer.toString(mr.getFollowUpTestCases().size()));
			mrData.put("LocalDistance", Integer.toString(mr.localDistance()));

			data.add(mrData);
		}

		CSVWriter writer = new CSVWriter(filePath);
		writer.write(data);

	}

	/**
	 *
	 * @param filePath File path of extended CVS file (rewritten if it already exists)
	 * @param mrs Metamorphic relations
	 */
	public static void writeMRsToExtendedCSV(String filePath, List<MetamorphicRelation> mrs, int maxFutcs) {

		List<Map<String,String>> data = new ArrayList<>();

		for(MetamorphicRelation mr: mrs) {
			Map<String,String> mrData = new LinkedHashMap<>();

			TestCase sourceTestCase = mr.getSourceTestCase();
			List<TestCase> followUpTestCases = (List<TestCase>) mr.getFollowUpTestCases();

			mrData.put("MR", mr.getIdentifier());
			mrData.put("type", mr.getType().name());
			mrData.put("STC_id", sourceTestCase.getId());
			mrData.put("STC_n_parameters", Integer.toString(sourceTestCase.getParameters().size()));
			mrData.put("number_of_FUTCs", Integer.toString(followUpTestCases.size()));
			mrData.put("local_distance", Integer.toString(mr.localDistance()));
			mrData.put("STC", mr.printSourceTestCaseParameters());


			for(int i = 0; i < maxFutcs; i++){

				if(followUpTestCases.size() > i) {
					TestCase futc = followUpTestCases.get(i);
					Diff diff = TestCaseDiff.testCaseDiff(sourceTestCase, futc);

					if(mr.getType().equals(CONJUNCTIVE_EXTEND) || mr.getType().equals(DISJUNCTIVE_EXTEND) || mr.getType().equals(EQUIVALENCE_EXTEND)) {				// Added parameters
						mrData.put("FUTC_" + (i+1), printParameters(diff.getAddedParameters()));
					}else if(mr.getType().equals(CONJUNCTIVE_CONTRACT) || mr.getType().equals(DISJUNCTIVE_CONTRACT) || mr.getType().equals(SHUFFLING_CONTRACT)){	// Removed parameters
						mrData.put("FUTC_" + (i+1), printParameterNames(diff.getRemovedParameters()));
					} else if(mr.getType().equals(COMPLETE_EXTEND) || mr.getType().equals(SHUFFLING_EXTEND)) {		// The first futc of a COMPLETE_EXTEND MR consists on adding a parameter, the rest consist on changing its value
						if(diff.getChangedParameters().isEmpty()) {		// Added parameter
							mrData.put("FUTC_" + (i+1), printParameters(diff.getAddedParameters()));
						} else {										// Changed parameter
							mrData.put("FUTC_" + (i+1), printParameters(diff.getChangedParameters()));
						}
					} else {	// Only Changed parameters (DISJOINT_EXTEND)
						mrData.put("FUTC_" + (i+1), printParameters(diff.getChangedParameters()));
					}

					sourceTestCase = futc;

				} else {
					mrData.put("FUTC_" + (i+1), "[]");
				}



			}

			data.add(mrData);
		}

		CSVWriter writer = new CSVWriter(filePath);
		writer.write(data);

	}
	
	/**
	 * @param filePath TXT file path. Metamorphic relations are written using the template by Segura et al.
	 * @param mrs Metamorphic relations
	 */
	public static void writeMRsToFile(String filePath, List<MetamorphicRelation> mrs) {
		 
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(filePath);
		
			for(MetamorphicRelation mr: mrs)
				fileWriter.write(mr.toString() + "\n");
				
			
			fileWriter.flush();
			fileWriter.close();
		
		} catch (IOException e) {
			System.err.println("Error writing in file " + filePath + " :" + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
}
