package isa.mr.prioritisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import isa.mr.inference.generators.MetamorphicRelation;

/**
 * Heuristic-driven priorization: In progress
 * @author Sergio Segura
 *
 */
public class LocalMRPrioritisation {

	
	/**
	 * 
	 * @param mrs Unordered collection of MRs
	 * @return Ordered list based on local distance.
	 */
	public static List<MetamorphicRelation> prioritise (Collection<MetamorphicRelation> mrs) {

		if (mrs==null || mrs.isEmpty())
			throw new IllegalArgumentException("Metamorphic relations set null or empty");
		
		// Copy metamorphic relations to a new list
		List<MetamorphicRelation> orderedMRs = new ArrayList<MetamorphicRelation>();
		orderedMRs.addAll(mrs);
		
		// Order list
		orderedMRs.sort(Comparator.comparing(MetamorphicRelation::localDistance).reversed());

		return orderedMRs;
	}
	
	

	
}
