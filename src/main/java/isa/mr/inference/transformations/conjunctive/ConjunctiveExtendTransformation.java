package isa.mr.inference.transformations.conjunctive;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import isa.mr.inference.transformations.AbstractInputExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Input transformation - Conjunctive conditions Metamorphic Relation Pattern
 * 
 * Pre-conditions:
 * 	
 * 		1. There should exist at least one optional conjunctive filter not included in the test case.
 * 
 * 		2. The number of results should not be limited, either explicitly or implicitly, i.e. the test case should not include the limit or offset parameter
 * 		   and the limit parameter (if any) should have no default value.
 * 
 * This transformation creates a single follow-up test case by adding a random number of filters to the source test case.
 * 
 * Example: (a, b, c) -> (a, b, c, filter1, filter2)
 * 
 * @author Sergio Segura
 *
 */
public class ConjunctiveExtendTransformation extends AbstractInputExtendTransformation {

	public ConjunctiveExtendTransformation(SUT sut) {
		super(sut);
	}

	// Returns the filters not included in the test
	protected List<TestParameter> getSelectableParameters(TestCase testCase) {

		List<TestParameter> selectableParameters = new ArrayList<TestParameter>();
		
		// Precondition: The number of results should not be limited either explicitly or implicitly: the test case should not include 
		// the limit or offset parameter and the limit parameter (if any) should have no default value.
		if (sut.restrictedNumberOfResultsByDefault() || testCase.getParameters().contains(sut.getLimitParameter()) || testCase.getParameters().contains(sut.getOffsetParameter()))
			return selectableParameters;
		
		// Selectable parameters
		List<Parameter> tcParameters = testCase.getParameters();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(sut.getConjunctiveFilters().stream().filter(p -> !tcParameters.contains(p))
				.collect(Collectors.toList()));
		
		// Selectable parameters and values (test parameters)
		if (parameters!=null) {
			for(Parameter p: parameters) {
				List<String> values = new ArrayList<String>();
				values.add("<TBD>");
				TestParameter tp = new TestParameter(p, values);
				selectableParameters.add(tp);
			}
		}

		return selectableParameters;
	}
	

}
