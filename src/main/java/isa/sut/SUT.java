package isa.sut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import isa.sut.constraints.ExcludeConstraint;
import isa.sut.constraints.RequireConstraint;
import isa.sut.specification.pojo.CompleteFilter;
import isa.sut.specification.pojo.Constraint;
import isa.sut.specification.pojo.DisjointFilter;
import isa.sut.specification.pojo.Feature;
import isa.sut.specification.pojo.Parameter;
import isa.sut.specification.pojo.Specification;

/**
 * SUT specification
 * 
 * @author Sergio Segura
 *
 */
public class SUT {

	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<Parameter> mandatoryParameters = new ArrayList<Parameter>();
	private List<Parameter> optionalParameters = new ArrayList<Parameter>();
	private List<Parameter> parametersWithDefaultValues = new ArrayList<Parameter>();
	private List<Parameter> orderingParameters = new ArrayList<Parameter>();
	private List<Parameter> conjunctiveFilters = new ArrayList<Parameter>();
	private List<Parameter> disjunctiveFilters = new ArrayList<Parameter>();
	private List<DisjointFilter> disjointFilters = new ArrayList<DisjointFilter>();
	private List<CompleteFilter> completeFilters = new ArrayList<CompleteFilter>();
	private Parameter limitParameter;
	private Parameter offsetParameter;
	private List<RequireConstraint> requireConstraints = new ArrayList<RequireConstraint>();
	private List<ExcludeConstraint> excludeConstraints = new ArrayList<ExcludeConstraint>();

	public SUT(Specification spec, String featureId) {

		Feature feature = spec.getFeatures().stream().filter(f -> f.getId().equalsIgnoreCase(featureId)).findFirst()
				.orElse(null);

		if (feature == null)
			throw new IllegalArgumentException("Feature with id \'" + featureId + "\' not found in the specification.");

		// Parameters
		parameters = feature.getParameters();

		// Mandatory parameters
		mandatoryParameters = feature.getParameters().stream().filter(p -> p.getRequired())
				.collect(Collectors.toList());

		// Optional parameters
		optionalParameters = feature.getParameters().stream().filter(p -> !p.getRequired()).collect(Collectors.toList());

		// Parameters with default values
		parametersWithDefaultValues = feature.getParameters().stream().filter(p -> p.getDefault() != null)
				.collect(Collectors.toList());
		
	
		// Ordering parameters
		List<String> orderingParametersNames = feature.getConfiguration().getOrderingParameters();
		if (orderingParametersNames != null) {
			orderingParameters.addAll(parameters.stream().filter(p -> orderingParametersNames.contains(p.getName()))
					.collect(Collectors.toList()));
		}
		
		// Conjunctive filters
		List<String> conjunctiveFiltersNames = feature.getConfiguration().getConjunctiveFilters();
		if (conjunctiveFiltersNames != null) {
			conjunctiveFilters.addAll(parameters.stream().filter(p -> conjunctiveFiltersNames.contains(p.getName()))
					.collect(Collectors.toList()));
		}

		// Disjunctive filters
		List<String> disjunctiveFiltersNames = feature.getConfiguration().getDisjunctiveFilters();
		if (disjunctiveFiltersNames != null) {
			disjunctiveFilters.addAll(parameters.stream().filter(p -> disjunctiveFiltersNames.contains(p.getName()))
					.collect(Collectors.toList()));
		}
		
		// Disjoint filters
		if (feature.getConfiguration().getDisjointFilters()!=null)
			disjointFilters = feature.getConfiguration().getDisjointFilters();
		
		// Complete filters
		if (feature.getConfiguration().getCompleteFilters()!=null)
			completeFilters = feature.getConfiguration().getCompleteFilters();
		
		// Limit parameter
		if (feature.getConfiguration().getLimitParameter()!=null)
			limitParameter = feature.getParameters().stream().filter(p -> p.getName().equalsIgnoreCase(feature.getConfiguration().getLimitParameter()))
					.findFirst().orElse(null);
		
		// Offset parameter
		if (feature.getConfiguration().getOffsetParameter()!=null)
			offsetParameter = feature.getParameters().stream().filter(p -> p.getName().equalsIgnoreCase(feature.getConfiguration().getOffsetParameter()))
					.findFirst().orElse(null);
		
		// Constraints
		if (feature.getConstraints()!=null)
			for(Constraint c:feature.getConstraints()) {
				if (c.getConstraint().contains("excludes"))
					excludeConstraints.add(new ExcludeConstraint(this, c.getConstraint()));
				else if (c.getConstraint().contains("requires"))
					requireConstraints.add(new RequireConstraint(this, c.getConstraint()));
				else
					throw new IllegalArgumentException("Wrong format in constraint: " + c.getConstraint());
			}
	}
	
	
	public String toString() {
		String res="";
		
		// Parameters
		res += "Parameters: \n";
		for(Parameter p: parameters) { 
			res += p.getName() + " (" + p.getType() + ")";
			if (parametersWithDefaultValues.contains(p))
				res += ", Default value: " + p.getDefault();
			if (optionalParameters.contains(p))
				res += ", Optional";
			else
				res += ", Mandatory";
			res += "\n";
		}
		
		// Ordering parameters
		if (!orderingParameters.isEmpty()) {
			res += "\nOrdering parameters: \n";
			for(Parameter p: orderingParameters) 
				res += p.getName() + "\n";
		}
			
		// Conjunctive filters
		if (!conjunctiveFilters.isEmpty()) {
			res += "\nConjunctive filters: \n";
			for(Parameter p: conjunctiveFilters)
				res += p.getName() + "\n";
		}
		
		// Disjunctive filters
		if (!disjunctiveFilters.isEmpty()) {
			res += "\nDisjunctive filters: \n";
			for(Parameter p: disjunctiveFilters)
				res += p.getName() + "\n";
		}
		
		// Disjoint filters
		if (!disjointFilters.isEmpty()) {
			res += "\nDisjoint filters: \n";
			for(DisjointFilter f: disjointFilters)
				res += f.getParameter() + "\n";
		}
		
		// Complete filters
		if (!completeFilters.isEmpty()) {
			res += "\nComplete filters: \n";
			for(CompleteFilter f: completeFilters)
				res += f.getParameter() +  " (All value: " + f.getAllValue() + ")\n";
		}
		
		// Require constraints
		if (!requireConstraints.isEmpty()) {
			res += "\nRequire constraints: \n";
			for(RequireConstraint rc: requireConstraints)
				res += rc.toString();
		}
		
		// Exclude constraints
		if (!excludeConstraints.isEmpty()) {
			res += "\nExclude constraints: \n";
			for(ExcludeConstraint ec: excludeConstraints)
				res += ec.toString();
		}
		
		
		return res;
	}
	
	public void printStatistics() {
		System.out.println("Number of parameters: " + parameters.size());
		System.out.println("Number of mandatory parameters: " + mandatoryParameters.size());
		System.out.println("Number of optional parameters: " + optionalParameters.size());
		System.out.println("Number of parameters with default values: " + parametersWithDefaultValues.size());
		System.out.println("Number of ordering parameters: " + orderingParameters.size());
		System.out.println("Number of conjunctive filters: " + conjunctiveFilters.size());
		System.out.println("Number of disjunctive filters: " + disjunctiveFilters.size());
		System.out.println("Number of disjoint filters: " + disjointFilters.size());
		System.out.println("Number of complete filters: " + completeFilters.size());
		System.out.println("Requires constraints: " + requireConstraints.size());
		System.out.println("Excludes constraints: " + excludeConstraints.size());
		
	}


	public List<Parameter> getParameters() {
		return parameters;
	}


	public List<Parameter> getMandatoryParameters() {
		return mandatoryParameters;
	}

	public List<Parameter> getOptionalParameters() {
		return optionalParameters;
	}


	public List<Parameter> getParametersWithDefaultValues() {
		return parametersWithDefaultValues;
	}


	public List<Parameter> getOrderingParameters() {
		return orderingParameters;
	}


	public List<Parameter> getConjunctiveFilters() {
		return conjunctiveFilters;
	}


	public List<Parameter> getDisjunctiveFilters() {
		return disjunctiveFilters;
	}


	public List<DisjointFilter> getDisjointFilters() {
		return disjointFilters;
	}

	public List<CompleteFilter> getCompleteFilters() {
		return completeFilters;
	}
	
	public Parameter getParameterByName(String name) {
		Parameter param = parameters.stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);
		return param;
	}


	public String getDefaultValue(Parameter p) {
		return getParameterByName(p.getName()).getDefault();
	}


	public List<RequireConstraint> getRequireConstraints() {
		return requireConstraints;
	}

	public List<ExcludeConstraint> getExcludeConstraints() {
		return excludeConstraints;
	}


	public Parameter getLimitParameter() {
		return limitParameter;
	}
	

	public Parameter getOffsetParameter() {
		return offsetParameter;
	}
	
	public boolean restrictedNumberOfResultsByDefault() {
		return (limitParameter!=null && limitParameter.getDefault()!=null);
	}
}
