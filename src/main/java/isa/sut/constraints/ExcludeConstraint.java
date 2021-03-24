package isa.sut.constraints;

import isa.sut.SUT;
import isa.sut.constraints.*;

public class ExcludeConstraint extends Constraint {

	public ExcludeConstraint(SUT sut, String constraint) {
		
		String[] params = constraint.split("excludes", 2);
		
		if (params.length!=2)
			throw new IllegalArgumentException("Wrong constraint: " + constraint);
		
		// Parameter on the left
		String[] leftParam = params[0].split("=", 2);
		leftParameter =sut.getParameterByName(removeBlankSpaces(leftParam[0]));
		
		if (leftParam.length > 1)		// Parameter value
			leftParemeterValue = removeBlankSpaces(leftParam[1]);

		// Parameter on the right
		String[] rightParam = params[1].split("=", 2);
		rightParameter = sut.getParameterByName(removeBlankSpaces(rightParam[0]));
		
		if (rightParam.length > 1)		// Parameter value
			rightParameterValue = removeBlankSpaces(rightParam[1]);
		
		if (getLeftParameter()==null || getRightParameter()==null)
			throw new IllegalArgumentException("Invalid parameter in constraint: " + constraint);
	}
	
	
	public String toString() {
		String res="";
		
		res += getLeftParameter().getName();
		
		if (getLeftParemeterValue()!=null)
			res +=" = " + getLeftParemeterValue();
		
		res += " <-> ";
		
		res += getRightParameter().getName();
		
		if (getRightParameterValue()!=null)
			res +=" = " + getRightParameterValue();
		
		res += "\n";
		
		
		return res;
	}

}
