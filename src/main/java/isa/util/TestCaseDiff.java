package isa.util;

import java.util.Comparator;

import isa.mr.inference.generators.MetamorphicRelation;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class TestCaseDiff {

	/** 
	 * @param tc1 Test case 1
	 * @param tc2 Test case 2
	 * @return differences of tc2 with respect to tc2 (parameters ordered alphabetically)
	 */
	public static Diff testCaseDiff(TestCase tc1, TestCase tc2) {
		Diff diff = new Diff();
		
		// Parameters included in tc1 not included in tc2
		for(TestParameter tp: tc1.getTestParameters())
			if (!tc2.getParameters().contains(tp.getParameter()))
				diff.addRemovedParameter(tp);
		
		// Parameters included in tc2 not included in tc1
		for(TestParameter tp: tc2.getTestParameters())
			if (!tc1.getParameters().contains(tp.getParameter()))
				diff.addAddedParameter(tp);
		
		// Parameters included in both test cases but with different values
		for(TestParameter tp: tc2.getTestParameters())
			if (!tc1.getTestParameters().contains(tp) && tc1.getParameters().contains(tp.getParameter()))
				diff.addChangedParameter(tp);
		
		// Orders parameters alphabetically
		diff.getAddedParameters().sort(Comparator.comparing(TestParameter::getParameterName));
		diff.getRemovedParameters().sort(Comparator.comparing(TestParameter::getParameterName));
		diff.getChangedParameters().sort(Comparator.comparing(TestParameter::getParameterName));
		
		return diff;
	}

}
