package isa.mr.inference.transformations.disjunctive;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import isa.mr.inference.transformations.AbstractInputContractTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Input transformation - Disjunctive conditions Metamorphic Relation Pattern
 *  
 * Pre-conditions:
 * 	
 * 		1. The test case should include at least one optional disjunctive filter.
 * 
 * 		2. The number of results should not be limited, either explicitly or implicitly, i.e. the test case should not include the limit or offset parameter
 * 		   and the limit parameter (if any) should have no default value.
 * 
 * This transformation adds a single follow-up test case by removing a random number of disjunctive filters to the source test case.
 * 
 * Example: (a, b, c, filter1, filter2) -> (a, b, c, filter1)
 * 
 * @author Sergio Segura
 *
 */
public class DisjunctiveContractTransformation extends AbstractInputContractTransformation {

	public DisjunctiveContractTransformation(SUT sut) {
		super(sut);
	}

	// Returns the optional filters included in the test case
	protected List<TestParameter> getUnselectableParameters(TestCase testCase) {
		List<TestParameter> unselectableParameters = new ArrayList<TestParameter>(); 

		// Precondition: The number of results should not be limited either explicitly or implicitly: the test case should not include 
		// the limit or offset parameter and the limit parameter (if any) should have no default value.
		if (sut.restrictedNumberOfResultsByDefault() || testCase.getParameters().contains(sut.getLimitParameter()) || testCase.getParameters().contains(sut.getOffsetParameter()))
			return unselectableParameters;
		
		// Selectable parameters
		List<Parameter> tcParameters = testCase.getParameters();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(sut.getDisjunctiveFilters().stream().filter(p -> (tcParameters.contains(p) && !p.getRequired()))
				.collect(Collectors.toList()));
		
		// Selectable parameters and values (test parameters)
		if (parameters!=null) {
			for(Parameter p: parameters) {
				TestParameter tp = new TestParameter(p);
				unselectableParameters.add(tp);
			}
		}

		return unselectableParameters;
	}

}
