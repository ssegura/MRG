package isa.mr.inference.transformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import isa.sut.SUT;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public abstract class AbstractInputContractTransformation extends AbstractInputTransformation {

	List<TestParameter> unselectableParameters;

	public AbstractInputContractTransformation(SUT sut) {
		super(sut);
	}

	/*  Transformation applicable if:
	 * 	1. There is at least one unselectable parameter
	*/
	public boolean match(TestCase testCase) {
		unselectableParameters = getUnselectableParameters(testCase);
		return (unselectableParameters.size() > 0);
	}

	@Override
	List<TestCase> doTransform(TestCase testCase) {
		
		List<TestCase> followUps = new ArrayList<TestCase>();
		
		// Order parameters randomly
		Collections.shuffle(unselectableParameters,random);
		
		TestCase followUp = testCase.clone();
		int nParameters = random.nextInt(unselectableParameters.size());
		for(int i=0;i<=nParameters;i++) 
			followUp.removeTestParameter(unselectableParameters.get(i).getParameter().getName());
		
		
		followUps.add(followUp);
		
		return followUps;
	}

	protected abstract List<TestParameter> getUnselectableParameters(TestCase testCase);

}
