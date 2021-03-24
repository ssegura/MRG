package isa.testcases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import isa.sut.specification.pojo.Parameter;

public class TestParameter {

	private Parameter parameter;				// Parameter
	private List<String> values;				// Values
	
	public TestParameter(Parameter parameter, List<String> values) {
		this.parameter = parameter;
		this.values = values;
	}
	
	public TestParameter(Parameter parameter, String value) {
		this.parameter = parameter;
		this.values = new ArrayList<String>();
		this.values.add(value);
	}
	
	public TestParameter(Parameter parameter) {
		this.parameter = parameter;
		this.values = null;
	}

	public Parameter getParameter() {
		return parameter;
	}
	
	public String getParameterName() {
		return parameter.getName();
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void setValue(String value) {
		values.clear();
		values.add(value);
	}
	
	/**
	 * 
	 * @param tp Test parameter
	 * @return True if both test parameter are equal (they refer to the same input parameter and have the same values)
	 */
	public boolean equals(Object o) {
		TestParameter tp = (TestParameter) o;
		boolean equal = true;
		
		// Check if the refer to the same input parameter
		if (parameter!=tp.getParameter())
			equal = false;
		
		// Check if they have the same value
		if (!values.get(0).equals(tp.getValues().get(0)))
			equal = false;
		
		return equal;
	}
	
	public String toString() {
		String res= parameter.getName();
		
		if (values!=null)
			res += " " + values + "\n";
		else
			res += " []\n";
		
		return res;
	}

}
