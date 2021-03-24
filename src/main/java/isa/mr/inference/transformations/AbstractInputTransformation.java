package isa.mr.inference.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import isa.sut.SUT;
import isa.sut.constraints.Constraint;
import isa.sut.constraints.ExcludeConstraint;
import isa.sut.constraints.RequireConstraint;
import isa.testcases.TestCase;
import isa.util.ConstraintChecker;

/** Abstract input transformation
 * 
 * @author Sergio Segura
 *
 */
public abstract class AbstractInputTransformation {

	protected SUT sut;
	protected Random random;
	protected long seed=-1;
	
	public AbstractInputTransformation(SUT sut) {
		this.sut = sut;
		
		random = new Random();
		seed =  random.nextLong();
		random.setSeed(seed);
	}
	
	// Return true if the transformation is applicable in the current source test case (input constraints are not considered at this point)
	abstract boolean match(TestCase testCase);
	
	/**
	 * @param testCase Source test case
	 * @return the set of valid follow-up test cases (or null if none exist)
	 */
	public List<TestCase> transform(TestCase testCase) {
		
		List<TestCase> potentialFollowUps = null;
		List<TestCase> followUps = new ArrayList<TestCase>();
		
		// Generate all potential follow-up test cases (ignoring input constraints)
		if (match(testCase)) 
			potentialFollowUps = doTransform(testCase);
		
		// Select valid test cases only, i.e. those satisfying the input constraints defined in the specification
		if (potentialFollowUps!=null) {
			for(TestCase futc: potentialFollowUps)
				if (valid(futc))
					followUps.add(futc);
		}
		
		if (followUps.isEmpty())
			followUps = null;
			
		return followUps;	
	}

	// Return true if the input test case satisfy all input constraint defined in the SUT specification
	private boolean valid(TestCase tc) {
		
		boolean valid = true;
		
		Iterator<RequireConstraint> itr = sut.getRequireConstraints().iterator();
		while (itr.hasNext() && valid) {
			RequireConstraint rc = itr.next();
			valid = ConstraintChecker.checkConstraint(sut, rc, tc);
			if (!valid)
				System.err.println("Violated constraint: " + rc + "\nTest case: " + tc);
		}
		
		Iterator<ExcludeConstraint> ite = sut.getExcludeConstraints().iterator();
		while (ite.hasNext() && valid) {
			ExcludeConstraint ec = ite.next();
			valid = ConstraintChecker.checkConstraint(sut, ec, tc);
			if (!valid)
				System.err.println("Violated constraint: " + ec + "\nTest case: " + tc);
		}
		
		return valid;
	}

	abstract List<TestCase> doTransform(TestCase testCase);

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		random.setSeed(seed);
	}
	
}
