package isa.sut.specification;

import java.io.File;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import isa.sut.specification.pojo.Specification;

/** Input specification loader
 * 
 * @author Sergio Segura
 *
 */
public class SUTSpecification {

	public static Specification readSpecification(String path) {
		YAMLMapper mapper = new YAMLMapper();
		Specification spec = null;
		try {
			spec = mapper.readValue(new File(path), Specification.class);

			// Print with format
			//String prettyConf = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(spec);
			//System.out.println(prettyConf);
		} catch (Exception e) {
			System.err.println("Error parsing configuration file: " + e.getMessage());
			e.printStackTrace();
		}

		return spec;
	}

}
