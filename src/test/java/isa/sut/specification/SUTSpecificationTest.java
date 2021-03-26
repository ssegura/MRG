package isa.sut.specification;

import static org.junit.Assert.*;
import org.junit.Test;

import isa.sut.specification.SUTSpecification;
import isa.sut.specification.pojo.Specification;

public class SUTSpecificationTest {

	@Test
	public void readSpecificationTest() {
		Specification spec = SUTSpecification.readSpecification("src/test/resources/Bikewise/spec.yml");
		assertEquals("Wrong number of parameters", 8, spec.getFeatures().get(0).getParameters().size());
	}

}
