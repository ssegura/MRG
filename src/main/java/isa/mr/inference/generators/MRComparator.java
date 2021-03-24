package isa.mr.inference.generators;

import java.util.Comparator;

public class MRComparator implements Comparator<MetamorphicRelation> {

	

	    @Override
	    public int compare(MetamorphicRelation mr1, MetamorphicRelation mr2) {
	        return mr1.getIdentifier().compareToIgnoreCase(mr2.getIdentifier());
	    }
	
}
