package isa.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import isa.testcases.TestParameter;
/**
 * A diff object represent the difference between two test cases TC1 and TC2 in terms of:
 * 
 * - Added parameters: Parameters included in TC1, but not in TC2.
 * - Removed parameters: Parameters included in TC2, but not in TC1.
 * - Changed parameters: Parameters included in both test cases, but with a different value.
 * 
 * @author Sergio Segura
 *
 */
public class Diff {

	
	private List<TestParameter> addedParameters;
	private List<TestParameter> removedParameters;
	private List<TestParameter> changedParameters;
	
	
	public Diff() {
		addedParameters = new ArrayList<TestParameter>();
		removedParameters = new ArrayList<TestParameter>();
		changedParameters = new ArrayList<TestParameter>();
	}

	public List<TestParameter> getAddedParameters() {
		return addedParameters;
	}
	
	public void addAddedParameter(TestParameter p) {
		addedParameters.add(p);
	}

	public List<TestParameter> getRemovedParameters() {
		return removedParameters;
	}
	
	public void addRemovedParameter(TestParameter p) {
		removedParameters.add(p);
	}

	public List<TestParameter> getChangedParameters() {
		return changedParameters;
	}
	
	public void addChangedParameter(TestParameter p) {
		changedParameters.add(p);
	}
	
	public int getHammingDistance() {
		return addedParameters.size() + removedParameters.size() + changedParameters.size();
	}
	
}
