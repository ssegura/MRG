
package isa.sut.specification.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ordering_parameters",
    "conjunctive_filters",
    "disjunctive_filters",
    "disjoint_filters",
    "complete_filters",
    "limit_parameter",
    "offset_parameter"
})
public class Configuration {

    @JsonProperty("ordering_parameters")
    private List<String> orderingParameters = null;
    @JsonProperty("conjunctive_filters")
    private List<String> conjunctiveFilters = null;
    @JsonProperty("disjunctive_filters")
    private List<String> disjunctiveFilters = null;
    @JsonProperty("disjoint_filters")
    private List<DisjointFilter> disjointFilters = null;
    @JsonProperty("complete_filters")
    private List<CompleteFilter> completeFilters = null;
    @JsonProperty("limit_parameter")
    private String limitParameter = null;
    @JsonProperty("offset_parameter")
    private String offsetParameter = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ordering_parameters")
    public List<String> getOrderingParameters() {
        return orderingParameters;
    }

    @JsonProperty("ordering_parameters")
    public void setOrderingParameters(List<String> orderingParameters) {
        this.orderingParameters = orderingParameters;
    }

    @JsonProperty("conjunctive_filters")
    public List<String> getConjunctiveFilters() {
        return conjunctiveFilters;
    }

    @JsonProperty("conjunctive_filters")
    public void setConjunctiveFilters(List<String> conjunctiveFilters) {
        this.conjunctiveFilters = conjunctiveFilters;
    }

    @JsonProperty("disjunctive_filters")
    public List<String> getDisjunctiveFilters() {
        return disjunctiveFilters;
    }

    @JsonProperty("disjunctive_filters")
    public void setDisjunctiveFilters(List<String> disjunctiveFilters) {
        this.disjunctiveFilters = disjunctiveFilters;
    }

    @JsonProperty("disjoint_filters")
    public List<DisjointFilter> getDisjointFilters() {
        return disjointFilters;
    }

    @JsonProperty("disjoint_filters")
    public void setDisjointFilters(List<DisjointFilter> disjointFilters) {
        this.disjointFilters = disjointFilters;
    }

    @JsonProperty("complete_filters")
    public List<CompleteFilter> getCompleteFilters() {
        return completeFilters;
    }

    @JsonProperty("complete_filters")
    public void setCompleteFilters(List<CompleteFilter> completeFilters) {
        this.completeFilters = completeFilters;
    }
    
    @JsonProperty("limit_parameter")
	public String getLimitParameter() {
		return limitParameter;
	}

    @JsonProperty("limit_parameter")
	public void setLimitParameter(String limitParameter) {
		this.limitParameter = limitParameter;
	}
    
    @JsonProperty("offset_parameter")
	public String getOffsetParameter() {
		return offsetParameter;
	}

    @JsonProperty("offset_parameter")
	public void setOffsetParameter(String offsetParameter) {
		this.offsetParameter = offsetParameter;
	}

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
