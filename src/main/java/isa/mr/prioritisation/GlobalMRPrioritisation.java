package isa.mr.prioritisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import isa.mr.inference.generators.MRComparator;
import isa.mr.inference.generators.MetamorphicRelation;
import isa.mr.inference.generators.MetamorphicRelationType;
import isa.testcases.TestCase;
import isa.util.Diff;
import isa.util.TestCaseDiff;

public class GlobalMRPrioritisation {

	/**
	 * 
	 * @param mrs Unordered collection of MRs
	 * @return Ordered list based on global distance, i.e. distance between each MR and the rest.
	 */
	public static List<MetamorphicRelation> prioritise (Collection<MetamorphicRelation> mrs) {

		List<MetamorphicRelation> orderedMRs = new ArrayList<MetamorphicRelation>();		// Ordered MRs to be returned
		List<MetamorphicRelation> originalMRs = new ArrayList<MetamorphicRelation>(mrs);	// MR collection as a list
		int[][] distanceMatrix = new int[mrs.size()][mrs.size()];							// Distance matrix (used for faster computation)
		List<Integer> indices = new ArrayList<Integer>();									// List of indices of the original MRs (used for faster computation)
		List<Integer> orderedIndices = new ArrayList<Integer>();							// List of indices of the ordered MRs (used for faster computation)
		MetamorphicRelationType typeLastMR = null;											// Type of the last MR added to the set
		
		
		// Order original MRs to make the algorithm deterministic (when the distance between two MR is equal, a different MR can be selected depending on the original order)
		originalMRs.sort(new MRComparator());

		for (int i=0;i<originalMRs.size();i++)
			indices.add(i);
		
		if (mrs==null || mrs.isEmpty())
			throw new IllegalArgumentException("Metamorphic relation set null or empty");
		
		// Add the pair of MRs with a maximum global distance and remove them from the initial original set of MRs
		// Distances are stored in the distances matrix
		// Mrs of different types are given priority over those of the same type.
		//System.out.println("Adding the pair of MRs with a maximum global distance");
		int max =0;
		int maxIndex1=0, maxIndex2=0;		// Variables to store the indices of the two MRs with a maximum global distance
		for (int i=0 ; i<originalMRs.size()-1 ; i++)
			for (int j=i+1; j<originalMRs.size(); j++ ) {
				int d = calculateGlobaldistance(originalMRs.get(i), originalMRs.get(j));
				distanceMatrix[i][j] = d;
				distanceMatrix[j][i] = d;
				System.out.println("Distance MRs (" + i + "," + j + "): " + d);
				if (d > max || (d == max && !originalMRs.get(i).getType().equals(originalMRs.get(j).getType()))) {
					max = d;
					maxIndex1 = i;
					maxIndex2 = j;
				}
			}
		
		//System.out.println("Adding relations " + maxIndex1 + " and " + maxIndex2 + " . Distance: " + distanceMatrix[maxIndex1][maxIndex2]);
		//System.out.println("MR " + maxIndex1 + ": " + originalMRs.get(maxIndex1));
		//System.out.println("MR " + maxIndex2 + ": " + originalMRs.get(maxIndex2));
		
		orderedIndices.add(maxIndex1);
		orderedIndices.add(maxIndex2);
		
		// Remove the selected relation from the list of indices so they are not processed again.
		indices.remove(new Integer(maxIndex1));
		indices.remove(new Integer(maxIndex2));
		
		// Iteratively add the MRs with the maximum distance to all the MRs already included in the prioritised set:
		// for each MR of the original set, we sum the individual distances with the other MRs included in the prioritised set, thus giving a value for the set. 
		// Then the maximum is obtained by comparing these set values.
		while (indices.size()>=2) {
			int maxDistance =0;
			int indexSelectedMR= -1; 	// index of the MR with a maximum distance (to be included in this iteration)
			for(int i:indices) {
				int d = distanceToSet(i,originalMRs,orderedIndices,distanceMatrix);
				//System.out.println("Distance of " + originalMRs.get(i).getIdentifier() + " to set: " + d);
				if (d > maxDistance) {
					maxDistance = d;
					indexSelectedMR = i;
				}
			}
			
			//System.out.println("Adding MR " + originalMRs.get(indexSelectedMR).getIdentifier());
			orderedIndices.add(indexSelectedMR);
			
			// Save type of the last MR added to the set
			typeLastMR = originalMRs.get(indexSelectedMR).getType();
			
			//System.out.println("Number of MRs prioritised so far: " + orderedIndices.size());
			indices.remove(new Integer(indexSelectedMR));
				
		}
		
		// Add last relation
		orderedIndices.add(indices.get(0));
		
		// Load final list of ordered MRs
		for(int i:orderedIndices)
			orderedMRs.add(originalMRs.get(i));
		
		return orderedMRs;
	}
	
	
	// Sum the individual distances of the mr with the MRs (of different type) included in the set of MRs. Indices are used for faster computation.
	// Distances are read from the distance matrix to avoid calculating them on each call.
	private static int distanceToSet(int indexMR, List<MetamorphicRelation> mrs, List<Integer> orderedIndices, int[][] distanceMatrix) {
		
		int distance = 0;
		
		for(int k:orderedIndices) {
			//if (mrs.get(indexMR).getType()!=mrs.get(k).getType())
				distance += distanceMatrix[indexMR][k];
		}
			
		return distance;
	}


	/*
	 * Calculate the distance between two MRs as the sum of the hamming distance of each pair of test cases: STC <-> STC', FUTC1 <-> FUTC'1...
	 * If the number of follow-up test cases is different, the number of parameters of the unpair tests are summed to the global distance.
	 */
	public static int calculateGlobaldistance(MetamorphicRelation mr1, MetamorphicRelation mr2) {
		
		int d =0;
		
		// Difference between source test cases
		Diff diff = TestCaseDiff.testCaseDiff(mr1.getSourceTestCase(), mr2.getSourceTestCase());
		d += diff.getHammingDistance();
		
		
		// Difference between follow-up test cases
		List<TestCase> mr1Futcs = (List<TestCase>) mr1.getFollowUpTestCases();
		List<TestCase> mr2Futcs = (List<TestCase>) mr2.getFollowUpTestCases();
		int nCommonFutcs = Math.min(mr1Futcs.size(), mr2Futcs.size());		// Number of pairs of FUTCs in both MRs
		for (int i=0;i<nCommonFutcs; i++) {
			TestCase futc1 = mr1Futcs.get(i);
			TestCase futc2 = mr2Futcs.get(i);
			
			diff = TestCaseDiff.testCaseDiff(futc1,futc2);
			d += diff.getHammingDistance();
		}
		
		/*
		// If the follow-up test case(s) exist in mr1 only we sum the number of parameters of the test case(s) to the difference.
		if (mr1Futcs.size() > nCommonFutcs)
			for(int i=nCommonFutcs;i<mr1Futcs.size();i++)
				d += mr1Futcs.get(i).getTestParameters().size();
		
		// If the follow-up test case(s) exist in mr2 only we sum the number of parameters of the test case(s) to the difference.
		if (mr2Futcs.size() > nCommonFutcs)
			for(int i=nCommonFutcs;i<mr2Futcs.size();i++)
				d += mr2Futcs.get(i).getTestParameters().size();
		 
		 */
		return d;
	}
}
