package isa.mr.inference.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import isa.testcases.TestCase;
import isa.testcases.TestParameter;
import isa.util.Diff;
import isa.util.TestCaseDiff;

/** Heuristic-driven priorization: In progress
 * 
 * @author Sergio Segura
 *
 */
public class MetamorphicRelation {

	private String identifier;						// MR identifier
	private String domain;							// MR domain (e.g. YouTube REST API)
	private MetamorphicRelationType type;			// MR pattern
	private TestCase sourceTestCase;				// Source test case
	private List<TestCase> followUpTestCases;		// Follow-up test cases
	
	public MetamorphicRelation(TestCase sourceTestCase) {
		this.sourceTestCase = sourceTestCase;
		followUpTestCases = new ArrayList<TestCase>();
	}
	
	public MetamorphicRelation(String id, String domain, TestCase sourceTestCase, List<TestCase> followUpTestCases) {
		this.identifier = id;
		this.domain = domain;
		this.sourceTestCase = sourceTestCase;
		this.followUpTestCases = followUpTestCases;
	}
	
	public MetamorphicRelation(String domain, TestCase sourceTestCase, List<TestCase> followUpTestCases) {
		this.domain = domain;
		this.sourceTestCase = sourceTestCase;
		this.followUpTestCases = followUpTestCases;
	}

	public TestCase getSourceTestCase() {
		return sourceTestCase;
	}

	public void setSourceTestCase(TestCase testCase) {
		this.sourceTestCase = testCase;
	}

	public Collection<TestCase> getFollowUpTestCases() {
		return followUpTestCases;
	}

	public void setFollowUpTestCases(List<TestCase> testCases) {
		this.followUpTestCases = testCases;
	}
	
	public void addFollowUpTestCase(TestCase testCase) {
		this.followUpTestCases.add(testCase);
	}
	
	public void addFollowUpTestCases(Collection<TestCase> testCases) {
		this.followUpTestCases.addAll(testCases);
	}

//	public Boolean containsSupersets(List<MetamorphicRelation> mrs){
//		// same type
//		// same domain
//		// same sourceTestCase parameters (source testcase id)
//		// A superset of follow up test cases (getFutcsAsStrings)
//
//		return mrs.stream()
//				.filter(x-> x.getType().equals(this.type)
////						&& x.getDomain().equals(this.domain)
//						&& x.getSourceTestCase().getId().equals(this.sourceTestCase.getId()))
//
//				.anyMatch(x-> x.getFutcAsStrings().containsAll(this.getFutcAsStrings()));
//	}

//	public static void removeSubsets(List<MetamorphicRelation> mrs, MetamorphicRelation mr){
//		// Same type
//		// Same domain
//		// Same sourceTestCase parameters (source testcase id)
//		// A superset of follow up test cases (getFutcsAsStrings)
//		List<MetamorphicRelation> mrsToRemove = mrs.stream().filter(x-> x.getType().equals(mr.getType())
////					&& x.getDomain().equals(mr.getDomain())
//					&& x.getSourceTestCase().getId().equals(mr.getSourceTestCase().getId())
//					&& mr.getFutcAsStrings().containsAll(x.getFutcAsStrings()))
//				.collect(Collectors.toList());
//
//		mrs.removeAll(mrsToRemove);
//	}
	
	
	public String printSimpleFormat() {
		String res ="";
		res += "Metamorphic relation " + identifier + ": " + type + "\n"
			+  "===============================================\n";
		
		res += sourceTestCase.toString();
		
		for (TestCase futc: followUpTestCases)
			res += futc.toString();
		
		return res;
		
	}
	
	/**
	 * 
	 * @return MR following the template proposed by Segura et al. (MET 2017)
	 */
	public String toString() {
		
		
		String res = printPreamble();
		
		res += printMR();
		
	
		 		
		return res;
	}

//	private List<String> getFutcAsStrings(){
//
//		List<String> res = new ArrayList<>();
//
//		TestCase tc = sourceTestCase;
//		for(TestCase futc: followUpTestCases) {
//			Diff diff = TestCaseDiff.testCaseDiff(tc, futc);
//			if (!diff.getAddedParameters().isEmpty())
//				res.add(printParameters(diff.getAddedParameters()));
//
//			if (!diff.getRemovedParameters().isEmpty())
//				res.add(printParameterNames(diff.getRemovedParameters()));
//
//			if (!diff.getChangedParameters().isEmpty())
//				res.add(printParameters(diff.getChangedParameters()));
//
//			tc = futc;
//
//		}
//
//		return res;
//
//	}

	
	// Print MR in natural language (based on Segura et al's template, MET 2017)
	private String printMR() {
		
		// if
		String res = "if a source test case is run with the inputs " + printSourceTestCaseParameters() + "\n";
				
		TestCase tc = sourceTestCase;
		for(TestCase futc: followUpTestCases) {
			Diff diff = TestCaseDiff.testCaseDiff(tc, futc);
			if (!diff.getAddedParameters().isEmpty())
				res += "and a follow-up test case is constructed by adding the following parameters " + printParameters(diff.getAddedParameters());
			
			if (!diff.getRemovedParameters().isEmpty())
				res += "and a follow-up test case is constructed by removing the following parameters " + printParameterNames(diff.getRemovedParameters());
			
			if (!diff.getChangedParameters().isEmpty())
				res += "and a follow-up test case is constructed by changing the value of the following parameters " + printParameters(diff.getChangedParameters());
			
			tc = futc;
			res += "\n";
		}
			
		// Then
		res += "then ";
		
		switch (type) {
		case CONJUNCTIVE_CONTRACT:
			res += "the follow-up output(s) should be a (non-strict) superset of the source test output.";
			break;
		case CONJUNCTIVE_EXTEND:
			res += "the follow-up output(s) should be a (non-strict) subset of the source test output.";
			break;
		case DISJUNCTIVE_CONTRACT:
			res += "the follow-up output(s) should be a (non-strict) subset of the source test output.";
			break;
		case DISJUNCTIVE_EXTEND:
			res += "the follow-up output(s) should be a (non-strict) superset of the source test output.";
			break;
		case SHUFFLING_CONTRACT:
			res += "the source and the follow-up output(s) should have the same items, regardless of their order.";
			break;
		case SHUFFLING_EXTEND:
			res += "the source and the follow-up output(s) should have the same items, regardless of their order.";
			break;
		case DISJOINT_EXTEND:
			res += "the source and the follow-up output(s) should be disjoint.";
			break;
		case COMPLETE_EXTEND:
			res += "the union of the follow-up outputs should have the same items as the source output, regardless of their order.";
			break;
		case EQUIVALENCE_EXTEND:
			res += "the source and the follow-up output(s) should have the same items in the same order.";
		default:
			
		}
				
		 res += "\n";
		 
		 return res;
	}

	// Print MR preamble
	private String printPreamble() {
		String res ="In the domain of " + domain + "\n";
		
		res += "the following metamorphic relation should hold\n"
		    +  identifier + " (" + type.name() + "):\n";
		
		return res;
	}

	// Returns parameters' names
	private String printParameterNames(List<TestParameter> params) {
		String res="[";
		
		for(TestParameter param:params) {
			res += param.getParameter().getName();
			res +=", ";
		}
		
		// Remove last comma
		res = res.substring(0, res.length() - 2) + "]";
		
		return res;
	}

	// Returns parameters' names and values (excluding those with undefined value: "<TBD>")
	private String printParameters(Collection<TestParameter> params) {
		String res="[";
		
		for(TestParameter param:params) {
			res += param.getParameter().getName();
			if (!param.getValues().get(0).equals("<TBD>"))
				res += ":" + param.getValues().get(0);
				
			res +=", ";
		}
		
		// Remove last comma
		res = res.substring(0, res.length() - 2) + "]";
		
		return res;
	}
	
	// Returns source test parameters. Values are only specified when relevant for the MR (i.e. they are changed in the follow-up test case).
	private String printSourceTestCaseParameters() {
		String res = "[";
		
		TestCase followUpTestCase = followUpTestCases.get(0);
		Diff diff = TestCaseDiff.testCaseDiff(sourceTestCase, followUpTestCase);
		for(TestParameter p: sourceTestCase.getTestParameters()) {
			// If the parameter has the same value in source and follow-up test case: print just name
			if (!diff.getChangedParameters().contains(followUpTestCase.getTestParameterByName(p.getParameter().getName())))
				res += p.getParameter().getName();
			else // If the parameter has different values in the source and follow-up test case: print name and value
				res += p.getParameter().getName() + ":" + p.getValues().get(0);
			
			res += ", ";
		}
		// Remove last comma
		res = res.equals("[") ? "[]" : res.substring(0, res.length() - 2) + "]";
		
		return res;
	}
	

	/**
	 * Return true if both MR are equals: source and follow-up test cases has exactly the same parameters and the same input transformations.
	 */
	public boolean equals (Object o) {
		MetamorphicRelation mr = (MetamorphicRelation) o;

		return (this.printMR().equals(mr.printMR()));
	}

//	public boolean equals (Object o) {
//		MetamorphicRelation mr = (MetamorphicRelation) o;
//
//		Boolean res = false;
//
//		// Same source test case and type
//		if(this.printSourceTestCaseParameters().equals(mr.printSourceTestCaseParameters()) && this.type.equals(mr.type)){
//			List<String> futcs = this.getFutcAsStrings();
//			List<String> futcsToCompare = mr.getFutcAsStrings();
//
//			// Same futcs (in different order)
//			if(futcs.size() == futcsToCompare.size() && futcs.containsAll(futcsToCompare)){
//				res = true;
//			}
//
//		}
//
//		return res;
//	}

	public MetamorphicRelationType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return the diff objects each each test case and the subsequent test case (stc - ftc1, ftc1 - ftc2,...)
	 */
	public List<Diff> getDiffs() {
		
		List<Diff> diffs = new ArrayList<Diff>();
		
		TestCase currentTestCase = sourceTestCase;
		for(TestCase followUpTestCase:followUpTestCases) {
			diffs.add(TestCaseDiff.testCaseDiff(currentTestCase, followUpTestCase));
			currentTestCase = followUpTestCase;
		}
		return diffs;
	}
	
	/**
	 * @return 	Local distance. It is calculated by adding the distance between each test case i and the subsequest follow-up test case j. 
	 * Distance between two test cases is calculated as the sum of added, removed and changed parameters.
	 */
	public int localDistance() {
		int distance = 0;
		
		for(Diff d: getDiffs())
			distance += d.getHammingDistance();
		
		return distance;
	}

	public void setType(MetamorphicRelationType type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
