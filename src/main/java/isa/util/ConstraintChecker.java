package isa.util;

import java.util.Iterator;
import java.util.List;

import isa.sut.SUT;
import isa.sut.constraints.Constraint;
import isa.sut.constraints.ExcludeConstraint;
import isa.sut.constraints.RequireConstraint;
import isa.sut.specification.pojo.Parameter;
import isa.testcases.TestCase;
import isa.testcases.TestParameter;

public class ConstraintChecker {

	
	/**
	 * 
	 * @param sut SUT specification
	 * @param c Constraint
	 * @param tc Test Case
	 * @return true if test case tc satisfy the constraint c
	 */
	public static boolean checkConstraint(SUT sut, Constraint c, TestCase tc) {
		boolean sat = true;
		
		if (c instanceof RequireConstraint)
			sat = checkRequireConstraint(sut,(RequireConstraint) c,tc);
		else if (c instanceof ExcludeConstraint)
			sat = checkExcludeConstraint(sut,(ExcludeConstraint) c,tc);
		else
			throw new IllegalArgumentException("Unsoported constraint type");
		
		// Uncomment for debugging
		/*
		if (!sat && c instanceof RequireConstraint)
			System.out.println("Constraint violated in test case " + tc.getId() + ": " + (RequireConstraint)c);
		else if (!sat && c instanceof ExcludeConstraint)
			System.out.println("Constraint violated in test case " + tc.getId() + ": " + (ExcludeConstraint)c);
		*/
		
		return sat;
	}
	
	
	/**
	 * 
	 * @param sut SUT specification
	 * @param tcs List of test cases
	 * @return true if the all test cases satisfy the constraints in sut
	 */
	public static boolean checkConstraints(SUT sut, List<TestCase> tcs) {
		
		boolean sat = true;
	
		Iterator<RequireConstraint> itr = sut.getRequireConstraints().iterator();
		while (itr.hasNext() && sat) {
			RequireConstraint rc = itr.next();
			Iterator<TestCase> ittc = tcs.iterator();
			while (ittc.hasNext() && sat)
				sat = ConstraintChecker.checkConstraint(sut, rc, ittc.next());
		}
		
		Iterator<ExcludeConstraint> ite = sut.getExcludeConstraints().iterator();
		while (ite.hasNext() && sat) {
			ExcludeConstraint ec = ite.next();
			Iterator<TestCase> ittc = tcs.iterator();
			while (ittc.hasNext() && sat)
				sat = ConstraintChecker.checkConstraint(sut, ec, ittc.next());
		}
		
		
		return sat;
	}
	
	private static boolean checkExcludeConstraint(SUT sut, ExcludeConstraint c, TestCase tc) {
		boolean sat;
		
		// (a and b)
		sat = checkCondition(c.getLeftParameter(),c.getLeftParemeterValue(),tc)
				&& checkCondition(c.getRightParameter(),c.getRightParameterValue(),tc);
		
		return !sat;
	}

	
	
	private static boolean checkRequireConstraint(SUT sut, RequireConstraint c, TestCase tc) {
		boolean sat = true;
		
		sat = !checkCondition(c.getLeftParameter(),c.getLeftParemeterValue(),tc)
			|| checkCondition(c.getRightParameter(),c.getRightParameterValue(),tc);
		
		
		return sat;
	}
	
	// Check if a single condition is satisfied.
	// cp: Parameter included in the condition
	// v: Parameter value in the condition (if any)
	// tc: Test case
	private static boolean checkCondition(Parameter cp, String value, TestCase tc) {
		boolean sat = true;
		
		// Get the corresponding parameter from the test case or null if its not included.
		TestParameter testParameter = null;
		
		if (tc.getParameters().contains(cp))
			testParameter = tc.getTestParameterByName(cp.getName());
			
		// If the parameter  of the constraint is not included in the test case the constraint is NOT satisfied.
		if (testParameter==null)
			sat=false;
		else	// If the equality is not satisfied the constraint is NOT satisfied
			if (value!=null && !testParameter.getValues().get(0).equals(value))
				return false;
		
		return sat;
	}
}
