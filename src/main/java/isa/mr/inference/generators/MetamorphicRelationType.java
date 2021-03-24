package isa.mr.inference.generators;

import java.util.Random;

public enum MetamorphicRelationType {

	CONJUNCTIVE_CONTRACT,
	CONJUNCTIVE_EXTEND,
    DISJUNCTIVE_CONTRACT,
    DISJUNCTIVE_EXTEND,
    SHUFFLING_CONTRACT,
	SHUFFLING_EXTEND,
	DISJOINT_EXTEND,
	COMPLETE_EXTEND,
	EQUIVALENCE_EXTEND;

    /**
     * Pick a random value of the MetamorphicRelationType enum.
     * @return a random MetamorphicRelationType.
     */
    public static MetamorphicRelationType getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
