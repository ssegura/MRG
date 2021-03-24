package isa.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import isa.mr.inference.generators.MetamorphicRelation;

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
