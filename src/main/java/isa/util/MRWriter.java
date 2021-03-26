package isa.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.testcases.TestCase;
import org.junit.Test;

import static isa.mr.inference.generators.MetamorphicRelation.printParameterNames;
import static isa.mr.inference.generators.MetamorphicRelation.printParameters;

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
		
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		
		for(MetamorphicRelation mr: mrs) {
			Map<String,String> mrData = new LinkedHashMap<String,String>();

			TestCase sourceTestCase = mr.getSourceTestCase();
			List<TestCase> followUpTestCases = (List<TestCase>) mr.getFollowUpTestCases();

			mrData.put("MR", mr.getIdentifier());
			mrData.put("type", mr.getType().name());
			mrData.put("STC_id", sourceTestCase.getId());
			mrData.put("STC_n_parameters", Integer.toString(sourceTestCase.getParameters().size()));
			mrData.put("number_of_FUTCs", Integer.toString(followUpTestCases.size()));
			mrData.put("local_distance", Integer.toString(mr.localDistance()));
			mrData.put("source_parameters_list", mr.printSourceTestCaseParameters());

//			int i = 0;

			for(int i = 0; i < maxFutcs; i++){

				if(followUpTestCases.size() > i){
					TestCase futc = followUpTestCases.get(i);
					Diff diff = TestCaseDiff.testCaseDiff(sourceTestCase, futc);

					if (!diff.getAddedParameters().isEmpty()) {
						mrData.put("added_parameters_futc_" + (i+1), printParameters(diff.getAddedParameters()));
					}else{
						mrData.put("added_parameters_futc_" + (i+1), "[]");
					}


					if (!diff.getRemovedParameters().isEmpty()) {
						mrData.put("removed_parameters_futc_" + (i+1), printParameterNames(diff.getRemovedParameters()));
					}else{
						mrData.put("removed_parameters_futc_" + (i+1), "[]");
					}

					if (!diff.getChangedParameters().isEmpty()) {
						mrData.put("changed_parameters_futc_" + (i+1), printParameters(diff.getChangedParameters()));
					}else{
						mrData.put("changed_parameters_futc_" + (i+1), "[]");
					}
					sourceTestCase = futc;

				}else{
					mrData.put("added_parameters_futc_" + (i+1), "[]");
					mrData.put("removed_parameters_futc_" + (i+1), "[]");
					mrData.put("changed_parameters_futc_" + (i+1), "[]");
				}
			}

//			for(TestCase futc: followUpTestCases){
////				i = i + 1;
//				Diff diff = TestCaseDiff.testCaseDiff(sourceTestCase, futc);
//
//				if (!diff.getAddedParameters().isEmpty()) {
//					mrData.put("added_parameters_futc_" + i, printParameters(diff.getAddedParameters()));
//				}else{
//					mrData.put("added_parameters_futc_" + i, "[]");
//				}
//
//
//				if (!diff.getRemovedParameters().isEmpty()) {
//					mrData.put("removed_parameters_futc_" + i, printParameterNames(diff.getRemovedParameters()));
//				}else{
//					mrData.put("removed_parameters_futc_" + i, "[]");
//				}
//
//				if (!diff.getChangedParameters().isEmpty()) {
//					mrData.put("changed_parameters_futc_" + i, printParameters(diff.getChangedParameters()));
//				}else{
//					mrData.put("changed_parameters_futc_" + i, "[]");
//				}
//				sourceTestCase = futc;
//
//			}
			
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
