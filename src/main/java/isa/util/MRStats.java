package isa.util;

import java.util.Collection;

import isa.mr.inference.generators.MetamorphicRelation;

public class MRStats {

	private int numConjunctiveContract = 0;
	private int numConjunctiveExtend = 0;
	private int numDisjunctiveContract = 0;
	private int numDisjunctiveExtend = 0;
	private int numDisjointExtend = 0;
	private int numShufflingContract =0;
	private int numShufflingExtend = 0;
	private int numCompleteExtend = 0;
	private int numEquivalenceExtend = 0;
	private int minNumFutcs = Integer.MAX_VALUE;
	private int maxNumFutcs = Integer.MIN_VALUE;
	private double avgNumFutcs = 0;
	
	
	public MRStats(Collection<MetamorphicRelation> mrs)  {
		
		for(MetamorphicRelation mr: mrs) {
			switch(mr.getType()) {
			case CONJUNCTIVE_CONTRACT:
				numConjunctiveContract++;
				break;
			case CONJUNCTIVE_EXTEND:
				numConjunctiveExtend++;
				break;
			case DISJUNCTIVE_CONTRACT:
				numDisjunctiveContract++;
				break;
			case DISJUNCTIVE_EXTEND:
				numDisjunctiveExtend++;
				break;
			case SHUFFLING_CONTRACT:
				numShufflingContract++;
				break;
			case SHUFFLING_EXTEND:
				numShufflingExtend++;
				break;
			case DISJOINT_EXTEND:
				numDisjointExtend++;
				break;
			case COMPLETE_EXTEND:
				numCompleteExtend++;
				break;
			case EQUIVALENCE_EXTEND:
				numEquivalenceExtend++;
			default:
				break;
				
			}
			
			if (mr.getFollowUpTestCases().size() < minNumFutcs)
				minNumFutcs = mr.getFollowUpTestCases().size();
			
			if (mr.getFollowUpTestCases().size() > maxNumFutcs)
				maxNumFutcs = mr.getFollowUpTestCases().size();
			
			avgNumFutcs += mr.getFollowUpTestCases().size();
			
		}
		
		avgNumFutcs = avgNumFutcs/mrs.size();
		
	}
	
	
	public void printStats() {
		
	}


	public int getNumConjunctiveContract() {
		return numConjunctiveContract;
	}


	public int getNumConjunctiveExtend() {
		return numConjunctiveExtend;
	}


	public int getNumDisjunctiveContract() {
		return numDisjunctiveContract;
	}


	public int getNumDisjunctiveExtend() {
		return numDisjunctiveExtend;
	}


	public int getNumDisjointExtend() {
		return numDisjointExtend;
	}


	public int getNumShufflingContract() {
		return numShufflingContract;
	}


	public int getNumShufflingExtend() {
		return numShufflingExtend;
	}


	public int getNumCompleteExtend() {
		return numCompleteExtend;
	}


	public int getNumEquivalenceExtend() {
		return numEquivalenceExtend;
	}


	public int getMinNumFutcs() {
		return minNumFutcs;
	}


	public int getMaxNumFutcs() {
		return maxNumFutcs;
	}


	public double getAvgNumFutcs() {
		return avgNumFutcs;
	}
	
}
