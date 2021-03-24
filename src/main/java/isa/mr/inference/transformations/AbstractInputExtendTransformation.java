package isa.mr.inference.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import isa.sut.SUT;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public abstract class AbstractInputExtendTransformation extends AbstractInputTransformation {

	protected List<TestParameter> selectableParameters;
	
	public AbstractInputExtendTransformation(SUT sut) {
		super(sut);
	}

	/*
	 * Transformation applicable if: 
	 * 1. There is at least one selectable parameter
	 */
	public boolean match(TestCase testCase) {
		selectableParameters = getSelectableParameters(testCase);
		return (selectableParameters.size() > 0);
	}

	@Override
	protected List<TestCase> doTransform(TestCase testCase) {
		List<TestCase> followUps = new ArrayList<TestCase>();
		
		// Order parameters randomly
		Collections.shuffle(selectableParameters,random);
		
		TestCase followUp = testCase.clone();
		int nParameters = random.nextInt(selectableParameters.size());
		for(int i=0;i<=nParameters;i++)
			followUp.addTestParameter(selectableParameters.get(i));
		
		followUps.add(followUp);
		
		return followUps;
	}

	protected abstract List<TestParameter> getSelectableParameters(TestCase testCase);

}
