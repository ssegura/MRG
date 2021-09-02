package isa.mr.inference.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import isa.mr.inference.transformations.complete.CompleteExtendTransformation;
import isa.mr.inference.transformations.conjunctive.ConjunctiveContractTransformation;
import isa.mr.inference.transformations.conjunctive.ConjunctiveExtendTransformation;
import isa.mr.inference.transformations.disjoint.DisjointExtendTransformation;
import isa.mr.inference.transformations.disjunctive.DisjunctiveContractTransformation;
import isa.mr.inference.transformations.disjunctive.DisjunctiveExtendTransformation;
import isa.mr.inference.transformations.equivalence.EquivalenceExtendTransformation;
import isa.mr.inference.transformations.shuffling.ShufflingContractTransformation;
import isa.mr.inference.transformations.shuffling.ShufflingExtendTransformation;
import isa.sut.SUT;
import isa.testcases.TestCase;

//import static isa.mr.inference.generators.MetamorphicRelation.removeSubsets;

/**
 * Random generation (i.e. inference) of MRs
 * 
 * @author Sergio Segura
 *
 */
public class RandomMRGenerator {

	private String domain; 					// System under test (required for MR printing)
	private int searchStrength = 5; 		// For each MRP, this factor determines the number of MR generation tries:
											// searchStrength * number of relevant parameters
	private int maxFutcs = 10;				// Maximum number of follow-up test cases. Default value 10
	private int maxMRPerTestCase=-1;		// Maximum number of MRs of each type to be generated for each source test case. Default: -1 (unlimited)
	private long seed = -1;
	private Random random;

	public RandomMRGenerator() {
		random = new Random();
		if (seed!=-1)
			random.setSeed(seed);
		else {
			seed = random.nextLong();
			random.setSeed(seed);
		}
	}

	/**
	 * 
	 * @param sut Specification of the system under test
	 * @param sourceTestCases Source test cases
	 * @return A collection of metamorphic relation (for each source test case and
	 *         MRP, a MR is created, removing possible duplicates)
	 */
	public Collection<MetamorphicRelation> generateMR(SUT sut, Collection<TestCase> sourceTestCases) {

		int numTries = 0;
		List<MetamorphicRelation> mrs = new ArrayList<MetamorphicRelation>();

		int id = 0;
		int iteration = 1;
		for (TestCase stc : sourceTestCases) {

			System.out.println("Generating MRs for test case " + iteration++ + " (" + mrs.size() + " MRs)");

			// CONJUNCTIVE_CONTRACT
			int nMRs = 0;
			for (int i = 0; i < sut.getConjunctiveFilters().size() * searchStrength; i++) {

				MetamorphicRelation mr = generateConjunctiveContractMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)){
						// Remove subsets
//						removeSubsets(mrs, mr);
					mr.setIdentifier("MR" + id++);
					mrs.add(mr);
					nMRs++;
//					}

				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// CONJUNCTIVE_EXTEND
			nMRs = 0;
			for (int i = 0; i < sut.getConjunctiveFilters().size() * searchStrength; i++) {

				MetamorphicRelation mr = generateConjunctiveExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
					mr.setIdentifier("MR" + id++);
					mrs.add(mr);
					nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// DISJUNCTIVE_CONTRACT
			nMRs = 0;
			for (int i = 0; i < sut.getDisjunctiveFilters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateDisjunctiveContractMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
					mr.setIdentifier("MR" + id++);
					mrs.add(mr);
					nMRs++;
//					}
				}

				numTries++;	
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// DISJUNCTIVE_EXTEND
			nMRs=0;
			for (int i = 0; i < sut.getDisjunctiveFilters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateDisjunctiveExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
					mr.setIdentifier("MR" + id++);
					mrs.add(mr);
					nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// SHUFFLING_CONTRACT
			nMRs=0;
			for (int i = 0; i < sut.getOrderingParameters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateShufflingContractMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
						mr.setIdentifier("MR" + id++);
						mrs.add(mr);
						nMRs++;
//					}
				}

				numTries++;	
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// SHUFFLING_EXTEND
			nMRs=0;
			for (int i = 0; i < sut.getOrderingParameters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateShufflingExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
						mr.setIdentifier("MR" + id++);
						mrs.add(mr);
						nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// DISJOINT_EXTEND
			nMRs=0;
			for (int i = 0; i < sut.getDisjointFilters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateDisjointExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
						mr.setIdentifier("MR" + id++);
						mrs.add(mr);
						nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// COMPLETE_EXTEND
			nMRs=0;
			for (int i = 0; i < sut.getCompleteFilters().size() * searchStrength; i++) {
				
				MetamorphicRelation mr = generateCompleteExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
					mr.setIdentifier("MR" + id++);
					mrs.add(mr);
					nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

			// EQUIVALENCE_EXTEND
			nMRs=0;
			for (int i = 0; i < sut.getParametersWithDefaultValues().size() * searchStrength; i++) {
				
				
				MetamorphicRelation mr = generateEquivalenceExtendMR(sut, stc);

				if (mr != null && !mrs.contains(mr)) {
					// If mrs does not contain a superset of mr
//					if (!mr.containsSupersets(mrs)) {
						// Remove subsets
//						removeSubsets(mrs, mr);
						mr.setIdentifier("MR" + id++);
						mrs.add(mr);
						nMRs++;
//					}
				}

				numTries++;
				
				if (nMRs==maxMRPerTestCase)
					break;
			}

		}

		System.out.println("Number of tries: " + numTries);
		System.out.println("Number of MRs: " + mrs.size());
		System.out.println("Number of duplicated MRs (discarded): " + (numTries - mrs.size()));

		return mrs;
	}

	//Generate a MR by applying a "Equivalence Extend Transformation"
	private MetamorphicRelation generateEquivalenceExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		EquivalenceExtendTransformation eet = new EquivalenceExtendTransformation(sut);
		eet.setSeed(random.nextLong());
		followUpTestCases = eet.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.EQUIVALENCE_EXTEND);

			// Generate new follow-up test cases while possible
			while (followUpTestCases != null && mr.getFollowUpTestCases().size() < maxFutcs) {
				followUpTestCases = eet.transform(followUpTestCases.get(0));
				if (followUpTestCases != null)
					mr.addFollowUpTestCase(followUpTestCases.get(0));
			}
		}
		return mr;
	}

	//Generate a MR by applying a "Complete Extend Transformation"
	private MetamorphicRelation generateCompleteExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		CompleteExtendTransformation cetr = new CompleteExtendTransformation(sut);
		cetr.setSeed(random.nextLong());
		cetr.setMaxValues(maxFutcs); 		// This determines the maximum number of follow-up test cases
		followUpTestCases = cetr.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.COMPLETE_EXTEND);
		}
		return mr;
	}

	//Generate a MR by applying a "Disjoint Extend Transformation"
	private MetamorphicRelation generateDisjointExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		DisjointExtendTransformation detr = new DisjointExtendTransformation(sut);
		detr.setMaxFutcs(maxFutcs); 		// Maximum number of follow-up test cases
		detr.setSeed(random.nextLong());
		followUpTestCases = detr.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.DISJOINT_EXTEND);
		}
		return mr;
	}

	//Generate a MR by applying a "Shuffling Extend Transformation"
	private MetamorphicRelation generateShufflingExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		ShufflingExtendTransformation set = new ShufflingExtendTransformation(sut);
		set.setMaxFutcs(maxFutcs); 		// Maximum number of follow-up test cases
		set.setSeed(random.nextLong());
		followUpTestCases = set.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.SHUFFLING_EXTEND);
		}
		return mr;
	}

	//Generate a MR by applying a "Shuffling Contract Transformation"
	private MetamorphicRelation generateShufflingContractMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		ShufflingContractTransformation sct = new ShufflingContractTransformation(sut);
		sct.setSeed(random.nextLong());
		followUpTestCases = sct.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.SHUFFLING_CONTRACT);
		}
		return mr;
	}

	//Generate a MR by applying a "Disjunctive Extend Transformation"
	private MetamorphicRelation generateDisjunctiveExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		DisjunctiveExtendTransformation det = new DisjunctiveExtendTransformation(sut);
		det.setSeed(random.nextLong());
		followUpTestCases = det.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.DISJUNCTIVE_EXTEND);

			// Generate new follow-up test cases while possible
			while (followUpTestCases != null && mr.getFollowUpTestCases().size() < maxFutcs) {
				followUpTestCases = det.transform(followUpTestCases.get(0));
				if (followUpTestCases != null)
					mr.addFollowUpTestCase(followUpTestCases.get(0));
			}
		}
		return mr;
	}

	//Generate a MR by applying a "Disjunctive Contract Transformation"
	private MetamorphicRelation generateDisjunctiveContractMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		DisjunctiveContractTransformation dct = new DisjunctiveContractTransformation(sut);
		dct.setSeed(random.nextLong());
		followUpTestCases = dct.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.DISJUNCTIVE_CONTRACT);

			// Generate new follow-up test cases while possible
			while (followUpTestCases != null && mr.getFollowUpTestCases().size() < maxFutcs) {
				followUpTestCases = dct.transform(followUpTestCases.get(0));
				if (followUpTestCases != null)
					mr.addFollowUpTestCase(followUpTestCases.get(0));
			}
		}
		return mr;
	}

	// Generate a MR by applying a "Conjunctive Extend Transformation"
	private MetamorphicRelation generateConjunctiveExtendMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		ConjunctiveExtendTransformation cet = new ConjunctiveExtendTransformation(sut);
		cet.setSeed(random.nextLong());
		followUpTestCases = cet.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.CONJUNCTIVE_EXTEND);

			// Generate new follow-up test cases while possible
			while (followUpTestCases != null && mr.getFollowUpTestCases().size() < maxFutcs) {
				followUpTestCases = cet.transform(followUpTestCases.get(0));
				if (followUpTestCases != null)
					mr.addFollowUpTestCase(followUpTestCases.get(0));
			}
		}

		return mr;
	}

	// Generate a MR by applying a "Conjunctive Contract Transformation"
	private MetamorphicRelation generateConjunctiveContractMR(SUT sut, TestCase stc) {
		MetamorphicRelation mr = null;
		List<TestCase> followUpTestCases = null;
		ConjunctiveContractTransformation cct = new ConjunctiveContractTransformation(sut);
		cct.setSeed(random.nextLong());
		followUpTestCases = cct.transform(stc);
		if (followUpTestCases != null) {
			mr = new MetamorphicRelation(domain, stc, followUpTestCases);
			mr.setType(MetamorphicRelationType.CONJUNCTIVE_CONTRACT);

			// Generate new follow-up test cases while possible
			while (followUpTestCases != null && mr.getFollowUpTestCases().size() < maxFutcs) {
				followUpTestCases = cct.transform(followUpTestCases.get(0));
				if (followUpTestCases != null)
					mr.addFollowUpTestCase(followUpTestCases.get(0));
			}
		}

		return mr;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getSearchStrength() {
		return searchStrength;
	}

	public void setSearchStrength(int searchStrength) {
		this.searchStrength = searchStrength;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		random.setSeed(seed);
	}

	public int getMaxFutcs() {
		return maxFutcs;
	}

	public void setMaxFutcs(int maxFutcs) {
		this.maxFutcs = maxFutcs;
	}

	public Random getRandom() {
		return random;
	}

	public int getMaxMRPerTestCase() {
		return maxMRPerTestCase;
	}

	public void setMaxMRPerTestCase(int maxMRPerTestCase) {
		this.maxMRPerTestCase = maxMRPerTestCase;
	}
}
