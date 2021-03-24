package isa.testcases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import isa.sut.specification.pojo.Parameter;


/** Domain-independent test case
 * 
 * @author Sergio Segura
 *
 */
public class TestCase {
	
	private String id;												// Test identifier
	private String operationId;										// Id of the operation (ex. getAlbums)
	private List<TestParameter>  testParameters;					// Test parameters (parameter:values)
	
	
	public TestCase(String id) {
		this.id = id;
		this.testParameters = new ArrayList<TestParameter>();
	}
	
	
	public TestCase(String id, String operationId) {
		this(id);
		this.operationId = operationId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public List<TestParameter> getTestParameters() {
		return testParameters;
	}
	
	public TestParameter getTestParameterByName(String name) {
		return testParameters.stream().filter(p -> p.getParameter().getName().equals(name)).findAny().orElse(null);
	}

	public void setTestParameters(List<TestParameter> testParameters) {
		this.testParameters = testParameters;
	}
	
	public void addTestParameter(TestParameter testParameter) {
		testParameters.add(testParameter);
	}
	
	// Remove a test parameter by name
	public void removeTestParameter(String name) {
		testParameters.removeIf(p -> p.getParameter().getName().equals(name));
	}
	
	// Get the list of parameters (without values)
	public List<Parameter> getParameters () {
		
		if (testParameters==null)
			return null;
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(TestParameter p: testParameters)
			parameters.add(p.getParameter());
		
		return parameters;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public TestCase clone() {
		TestCase clone = new TestCase(id,operationId);
		
		// Parameters
		List<TestParameter>  params = new ArrayList<TestParameter>();
		Iterator<TestParameter> it = testParameters.iterator();
		while (it.hasNext()) {
			TestParameter testParam = it.next();
			
			// Copy values
			List<String> values = null;
			if (testParam.getValues()!=null) {
				values = new ArrayList<String>();
				for(String v : testParam.getValues())
					values.add(v);
			} 
			
			params.add(new TestParameter(testParam.getParameter(),values));
		}
		
		clone.setTestParameters(params);
		
		return clone;
	}
	
	public String toString() {
		String res="Test: " + id + " \n";
		for(TestParameter testParam: testParameters) {
			res += testParam.toString();
		}
		
		res += "\n";
		
		return res;
	}
}
