package inference.evaluationMetric;

import inference.ConfigInference.ID_EVALUATION_METRIC;

public interface EvaluationMetric{
	
	double quantifyFromFrequencyTable(int[][] frequencyTable);
	double quantifyFromFrequencyTable(int[][] frequencyTable, double baseEmtropy);
	String toShortString();
	ID_EVALUATION_METRIC getId();
	
}
