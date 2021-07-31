package inference;

import java.util.NavigableSet;
import java.util.TreeSet;

public class GeneInfered {
	final private int idGene;
	final private NavigableSet<Integer> predictors;
	final private long[] boolean_functions;
	final private long[] state_functions;
	final private long[] state_notObserved;
	final private double critery_function_values;
	final private int[] global_boolean_values;
	
	public GeneInfered(int idGene, NavigableSet<Integer> predictors, long[] boolean_functions, long[] state_functions,
			long[] state_notObserved, double critery_function_values, int[] global_boolean_values) {
		this.idGene = idGene;
		this.predictors = predictors;
		this.boolean_functions = boolean_functions;
		this.state_functions = state_functions;
		this.state_notObserved = state_notObserved;
		this.critery_function_values = critery_function_values;
		this.global_boolean_values = global_boolean_values;
	}
	public static GeneInfered ZeroGeneInfered(int idGene) {
		return new GeneInfered(idGene,
				new TreeSet<>(),
				null,
				null,
				null,
				0.0, 
				null);
	}
	public GeneInfered cloneZeroGeneInfered() {
		return ZeroGeneInfered(this.idGene);
	}
	public int getIdGene() {
		return idGene;
	}

	public NavigableSet<Integer> getPredictors() {
		return predictors;
	}

	public long[] getBoolean_functions() {
		return boolean_functions;
	}

	public long[] getState_functions() {
		return state_functions;
	}

	public long[] getState_notObserved() {
		return state_notObserved;
	}

	public double getCritery_function_values() {
		return critery_function_values;
	}

	public int[] getGlobal_boolean_values() {
		return global_boolean_values;
	}

	private int sizeMetric(long[] metric) {
		int sizeFunction = 1 << this.predictors.size();
		int size = 0;
		for (int i = 0; i < sizeFunction; i++) {
			size += ((1L << (i % 64) & metric[(i / 64)]) == 0) ? 1 : 0;
		}
		return size;
	}

	public int sizeZerosFunction() {
		return sizeMetric(state_notObserved);
	}

	public int sizeEqualsFunction() {
		return sizeMetric(state_functions);
	}
}
