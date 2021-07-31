package inference.evaluationMetric;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import inference.ConfigInference.ID_EVALUATION_METRIC;

public class InvGaussianError implements EvaluationMetric {
	private final int sizeTable;
	public InvGaussianError(int sizeTable) {
		this.sizeTable=sizeTable;
	}

	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable) {
		int ms=Stream.of(frequencyTable)
				.mapToInt(a->IntStream.of(a)
							.max().getAsInt())
				.sum();
		return (double)ms/this.sizeTable;
	}

	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable, double baseEmtropy) {
		throw(new UnsupportedOperationException());
	}

	@Override
	public String toShortString() {
		return getId().toString();
	}

	@Override
	public ID_EVALUATION_METRIC getId() {
		return ID_EVALUATION_METRIC.IGE;
	}

}
