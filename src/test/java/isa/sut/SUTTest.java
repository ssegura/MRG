package isa.sut;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import isa.sut.SUT;
import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;

public class SUTTest {


	@Test
	public void testSUTBikewise() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-complete.yml");
		SUT sut = new SUT(spec,"GetIncidents");
		assertEquals("Wrong number of parameters", 9, sut.getParameters().size());
		assertEquals("Wrong number of mandatory parameters", 1, sut.getMandatoryParameters().size());
		assertEquals("Wrong number of optional parameters", 8, sut.getOptionalParameters().size());
		assertEquals("Wrong number of parameters with default values", 3, sut.getParametersWithDefaultValues().size());
		assertEquals("Wrong number of ordering parameters", 1, sut.getOrderingParameters().size());
		assertEquals("Wrong number of conjunctive filters", 4, sut.getConjunctiveFilters().size());
		assertEquals("Wrong number of disjunctive filters", 0, sut.getDisjunctiveFilters().size());
		assertEquals("Wrong number of disjoint filters", 1, sut.getDisjointFilters().size());
		assertEquals("Wrong number of complete filters", 1, sut.getCompleteFilters().size());
		assertEquals("Wrong number of requires constraints", 1, sut.getRequireConstraints().size());
		assertEquals("Wrong number of excludes constraints", 0, sut.getExcludeConstraints().size());
		//System.out.println(sut.toString());
	}
	
	@Test
	public void testConstrainedSUTBikewise() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec-constraints.yml");
		SUT sut = new SUT(spec,"GetIncidents");
		assertEquals("Wrong number of requires constraints", 2, sut.getRequireConstraints().size());
		assertEquals("Wrong number of excludes constraints", 2, sut.getExcludeConstraints().size());
		System.out.println(sut.toString());
	}
}
