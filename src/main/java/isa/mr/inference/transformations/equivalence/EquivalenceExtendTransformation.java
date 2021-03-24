package isa.mr.inference.transformations.equivalence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import isa.mr.inference.transformations.AbstractInputExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Input transformation - Equivalence Metamorphic Relation Pattern
 * 
 * Pre-conditions:
 * 	
 * 		1. The test case should include at least one parameter with a default value in the SUT specification.
 * 
 * This transformation creates follow-up test cases by adding new parameters and their default values to the source test case
 * 
 * Example: (a, b, c) -> (a, b, c , d={default})
 * 
 * @author Sergio Segura
 *
 */
public class EquivalenceExtendTransformation extends AbstractInputExtendTransformation {
	
	public EquivalenceExtendTransformation(SUT sut) {
		super(sut);
	}

	// Return the parameters with default values not included in the test case
	protected List<TestParameter> getSelectableParameters(TestCase testCase) {
		
		List<TestParameter> selectableParameters = new ArrayList<TestParameter>();
		List<Parameter> tcParameters = testCase.getParameters();

		// Selectable parameters
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(sut.getParametersWithDefaultValues().stream().filter(p -> !tcParameters.contains(p))
				.collect(Collectors.toList()));
		
		// Selectable parameters and values (test parameters)
		if (parameters!=null) {
			for(Parameter p: parameters) {
				List<String> values = new ArrayList<String>();
				values.add(sut.getDefaultValue(p));
				TestParameter tp = new TestParameter(p, values);
				selectableParameters.add(tp);
			}
		}
		
		return selectableParameters;
	}
}
