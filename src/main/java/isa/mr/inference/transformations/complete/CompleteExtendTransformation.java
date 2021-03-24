package isa.mr.inference.transformations.complete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import isa.mr.inference.transformations.AbstractInputExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.CompleteFilter;
import isa.sut.specification.pojo.DisjointFilter;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Input transformation - Complete partition Metamorphic Relation Pattern
 * 
 * Pre-conditions:
 * 
 * 		1. The SUT should include at least one complete filter not included in the test ((with empty as default value) or the test should
 * 			include a complete filter with the value to return all results (e.g. "all" "any").
 * 
 * 		2. The number of results should not be limited, either explicitly or implicitly, i.e. the test case should not include the limit or offset parameter
 * 		   and the limit parameter (if any) should have no default value.
 * 
 * 		2. Only parameters with a number of values less or equal than "maxValues" are considered.
 * 
 * This transformation creates: 
 * 
 * 		1. A follow-up test case for each of the values of a disjoint and complete filter randomly selected (from those not included in the source test case).
 * 		   It is assumed that not including the parameter returns all possible values.
 * 			Example: (a, b, c) -> (a, b, c, d={small}), (a, b, c, d={medium}), (a, b, c, d={large})
 * 
 *  	2. A follow-up test case for each of the values of a disjoint and complete filter randomly selected (from those included in the source test case 
 *         with the default value to return all possible items).
 *         Example: (a, b, c, d={all}) -> (a, b, c, d={small}), (a, b, c, d={medium}), (a, b, c, d={large})
 *         
 * 
 * @author Sergio Segura
 *
 */
public class CompleteExtendTransformation extends AbstractInputExtendTransformation {

	private int maxValues = 20; 		// This is the maximum number of values of the parameters to be considered as selectable (i.e., number of follow-up test cases). 
										// Used to avoid MRs with hundreds of follow-up test cases.

	public CompleteExtendTransformation(SUT sut) {
		super(sut);
	}

	// Return the complete filters not included in the test (with empty as default value) and those included with the value to return all results (e.g. "all" "any").
	// Only parameters with a number of values less or equal than "maxValues" are considered.
	protected List<TestParameter> getSelectableParameters(TestCase testCase) {
		
		List<TestParameter> selectableParameters = new ArrayList<TestParameter>();
		
		// Precondition: The number of results should not be limited either explicitly or implicitly: the test case should not include 
		// the limit or offset parameter and the limit parameter (if any) should have no default value.
		if (sut.restrictedNumberOfResultsByDefault() || testCase.getParameters().contains(sut.getLimitParameter()) || testCase.getParameters().contains(sut.getOffsetParameter()))
			return selectableParameters;
		
				
		for(CompleteFilter cf: sut.getCompleteFilters()) {
			TestParameter testParam = testCase.getTestParameterByName(cf.getParameter());
			Parameter param =sut.getParameterByName(cf.getParameter());
			
			if (param.getEnum().size() <= maxValues ) {	// Only parameters with a number of values less or equal than "maxValues" are considered
			
			// Parameter included in the test case with "all" value (all items are returned)
			if (testParam!=null && testParam.getValues().get(0).equals(cf.getAllValue())) {
				
				List<String> values = new ArrayList<String>();
				values.addAll(testParam.getParameter().getEnum());
				
				// Remove the parameter's value used in testCase
				values.removeAll(testParam.getValues());
				
				selectableParameters.add(new TestParameter(testParam.getParameter(),values));
			} else		// Parameter not included in the test case. All elements returned by default.
				if (testParam==null && cf.getAllValue().equals("empty") ) {
					List<String> values = new ArrayList<String>();
					values.addAll(param.getEnum());

					selectableParameters.add(new TestParameter(param,values));
				}
			}
		}
		
		return selectableParameters;
	}
	
	protected List<TestCase> doTransform(TestCase testCase) {
		List<TestCase> followUps = new ArrayList<TestCase>();
		
		// Order parameters randomly
		Collections.shuffle(selectableParameters,random);
		
		// Select disjoint filter parameter
		TestParameter testParameter = selectableParameters.get(0);
		
		int nFollowUps = testParameter.getValues().size();
		for(int i=0;i<nFollowUps;i++) {
			TestCase followUp = testCase.clone();
			followUp.setId(followUp.getId() + "FU-" + i);
			
			// Replace parameter value
			TestParameter tp = followUp.getTestParameterByName(testParameter.getParameter().getName());
			if (tp!=null) 			// Parameter included in the test case
				tp.setValue(testParameter.getValues().get(i));
			else {					// Parameter not included in the test case
				tp = new TestParameter(testParameter.getParameter(),testParameter.getValues().get(i));
				followUp.addTestParameter(tp);
			}
				
			// Add new follow-up test case to collection
			followUps.add(followUp);
		}

		return followUps;
	}

	public int getMaxValues() {
		return maxValues;
	}

	public void setMaxValues(int maxValues) {
		this.maxValues = maxValues;
	}
}
