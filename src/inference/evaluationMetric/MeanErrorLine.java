package inference.evaluationMetric;

import java.util.stream.IntStream;

import inference.ConfigInference.ID_EVALUATION_METRIC;

public class MeanErrorLine implements EvaluationMetric {

	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable) {
//		int lines = 0;
		double sumerro = 0;
		for (int[] is : frequencyTable) {
			int sum = IntStream.of(is).sum();
			if (sum != 0) {
//				lines++;
				sumerro += 1 - (double) IntStream.of(is).max().getAsInt() / sum;
			}
		}
		return 1 - (sumerro) / frequencyTable.length;
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
		return ID_EVALUATION_METRIC.MEL;
	}

}
