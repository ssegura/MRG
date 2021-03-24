package isa.sut.constraints;

import isa.sut.specification.pojo.Parameter;

public abstract class Constraint {
	
	protected Parameter leftParameter;
	protected Parameter rightParameter;
	protected String leftParemeterValue;
	protected String rightParameterValue;
	
	protected String removeBlankSpaces(String str) {
		return str.replaceAll("\\s+","");
	}

	public Parameter getLeftParameter() {
		return leftParameter;
	}

	public Parameter getRightParameter() {
		return rightParameter;
	}

	public String getLeftParemeterValue() {
		return leftParemeterValue;
	}
	
	public String getRightParameterValue() {
		return rightParameterValue;
	}

}