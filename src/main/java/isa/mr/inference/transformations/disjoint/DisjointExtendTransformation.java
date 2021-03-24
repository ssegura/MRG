package isa.mr.inference.transformations.disjoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import isa.mr.inference.transformations.AbstractInputExtendTransformation;
import isa.sut.SUT;
import isa.sut.specification.pojo.DisjointFilter;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

/**
 * Input transformation - Disjoint partition Metamorphic Relation Pattern
 * 
 * Pre-conditions:
 * 	
 * 		1. The test include should include at least one disjoint filter.
 * 
 * This transformation creates a follow-up test case for each of the values of a disjoint filter randomly selected 
 * (from those included in the source test case)
 * 
 * Example: (a, b, c, d={small}, e={open}) -> (a, b, c, d={medium}, e={open}), (a, b, c, d={large}, e={open}) 
 * 
 * @author Sergio Segura
 *
 */
public class DisjointExtendTransformation extends AbstractInputExtendTransformation {

	private int maxFutcs = 20; 		// Maximum number of follow-up test cases
	
	public DisjointExtendTransformation(SUT sut) {
		super(sut);
	}

	// Return the disjoint filters included in the test case (and their unselected values)
	protected List<TestParameter> getSelectableParameters(TestCase testCase) {
		
		List<TestParameter> selectableParameters = new ArrayList<TestParameter>();
		
		List<Parameter> tcParameters = testCase.getParameters();			// TestCase's parameters		
		for(DisjointFilter df: sut.getDisjointFilters()) {
			Parameter param = sut.getParameterByName(df.getParameter());
			
			// Get possible values
			List<String> values = new ArrayList<String>();
			if (df.getValues()!=null)		// Disjoint values
				values.addAll(df.getValues());
			else							// All parameter's values are disjoint
				values.addAll(param.getEnum());
			
			// If the parameter is included and its value is one of the disjoint values, add the parameter to the list of selectable
			TestParameter tcParam = testCase.getTestParameterByName(param.getName());
			if (tcParameters.contains(param) && values.containsAll(tcParam.getValues())) {

				// Remove the parameter's value used in testCase
				values.removeAll(tcParam.getValues());
				
				selectableParameters.add(new TestParameter(param,values));
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
		
		//int nFollowUps = random.nextInt(testParameter.getValues().size());
		int nFollowUps = random.nextInt(Math.min(testParameter.getValues().size(), maxFutcs));
		
		//Collections.shuffle(testParameter.getValues(),random);		// NOTE: This leads to the generation of very similar MRs: those with same follow-up test cases in different order
		for(int i=0;i<=nFollowUps;i++) {
			TestCase followUp = testCase.clone();
			followUp.setId(followUp.getId() + "FU-" + i);
			
			// Replace parameter value
			TestParameter tp = followUp.getTestParameterByName(testParameter.getParameter().getName());
			tp.setValue(testParameter.getValues().get(i));
			
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
