package isa.mr.inference.transformations.shuffling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import isa.mr.inference.transformations.AbstractInputExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Extend Input transformation - Shuffling Metamorphic Relation Pattern
 * 
 * Pre-conditions:
 * 
 * 		1. The SUT should include at least one ordering parameter not included in the test case.
 * 
 * 		2. The number of results should not be limited, either explicitly or implicitly, i.e. the test case should not include the limit or offset parameter
 * 		   and the limit parameter (if any) should have no default value.
 * 
 * This transformation creates follow-up test cases by adding ordering parameters to the test.
 * 
 * @author Sergio Segura
 *
 */
public class ShufflingExtendTransformation extends AbstractInputExtendTransformation {

	
	private int maxFutcs = 20; 		// Max number of follow-up test cases
	
	public ShufflingExtendTransformation(SUT sut) {
		super(sut);
	}

	// Returns the shuffling parameters (or values) not included in the test case
	protected List<TestParameter> getSelectableParameters(TestCase testCase) {
		
		List<TestParameter> selectableParameters = new ArrayList<TestParameter>();
		
		// Precondition: The number of results should not be limited either explicitly or implicitly: the test case should not include 
		// the limit or offset parameter and the limit parameter (if any) should have no default value.
		if (sut.restrictedNumberOfResultsByDefault() || testCase.getParameters().contains(sut.getLimitParameter()) || testCase.getParameters().contains(sut.getOffsetParameter()))
			return selectableParameters;
		
		// Precondition: The number of results should not be limited either explicitly or implicitly (through a default value)
		if (sut.restrictedNumberOfResultsByDefault() ||  testCase.getParameters().contains(sut.getLimitParameter()))
			return selectableParameters;
		
		List<Parameter> tcParameters = testCase.getParameters();			// TestCase's parameters		
		for(Parameter p: sut.getOrderingParameters()){
			TestParameter tp = new TestParameter(p);
			if (p.getEnum()!=null) {
				List<String> values = new ArrayList<String>();
				values.addAll(p.getEnum());
				tp.setValues(values);
			}
			
			// Ordering parameter already used in source test case (select other possible values)
			if (tcParameters.contains(p)) {
				TestParameter tcParam = testCase.getTestParameterByName(p.getName());
				tp.getValues().removeAll(tcParam.getValues());
			}
			

			selectableParameters.add(tp);
		}
	
		return selectableParameters;
	}
	
	protected List<TestCase> doTransform(TestCase testCase) {
		List<TestCase> followUps = new ArrayList<TestCase>();
		
		// Order parameters randomly
		Collections.shuffle(selectableParameters,random);
		
		// Select ordering parameter
		TestParameter testParameter = selectableParameters.get(0);
		
		//int nFollowUps = random.nextInt(testParameter.getValues().size());
		int nFollowUps = random.nextInt(Math.min(testParameter.getValues().size(), maxFutcs));
		//Collections.shuffle(testParameter.getValues(),random);		// NOTE: This leads to the generation of very similar MRs: those with same follow-up test cases in different order
		for(int i=0;i<=nFollowUps;i++) {
			TestCase followUp = testCase.clone();
			followUp.setId(followUp.getId() + "FU-" + i);
			
			TestParameter tp = null;
			// If the parameter is already included in the test case: replace parameter value
			if (testCase.getParameters().contains(testParameter.getParameter())) {
				tp = followUp.getTestParameterByName(testParameter.getParameter().getName());
				tp.setValue(testParameter.getValues().get(i));
			} else { // Parameter not included in the test case
				tp = new TestParameter(testParameter.getParameter(), testParameter.getValues().get(i));
				followUp.addTestParameter(tp);
			}
			
			// Add new follow-up test case to collection
			followUps.add(followUp);
		}

		return followUps;
	}

	public int getMaxFutcs() {
		return maxFutcs;
	}

	public void setMaxFutcs(int maxFutcs) {
		this.maxFutcs = maxFutcs;
	}
}
