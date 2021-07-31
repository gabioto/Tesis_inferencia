package inference.evaluationMetric;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import inference.ConfigInference.ID_EVALUATION_METRIC;
import resources.Resources;

public class MutualInformationNotObservedPenality extends MutualInformation {
	private double alpha;

	public MutualInformationNotObservedPenality(double alpha,int sizeTable) {
		super(sizeTable);
		this.alpha = alpha;
	}

	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable) {
		double entropyYX = 0;
		final int sizeDiscreteValues=frequencyTable[0].length;
		double fullEntropy = fullEntropy(frequencyTable);

		if (Resources.compareDouble(fullEntropy, 0) != 0) {// (fullEntropy != 0) {
			// recorer tabla y calcular entropias parciales
			for (int[] line : frequencyTable) {
				double partialEntropy = 0;
				// nro de predicciones por linea
				int sum = IntStream.of(line).sum();
				// calcular H(Y|x) entropia de Y dado el estado x
				if (sum != 0) {
					partialEntropy = IntStream.of(line)
								.mapToDouble(a->Resources.entropy((double)a/sum, sizeDiscreteValues))
								.sum();
				} else
					partialEntropy = fullEntropy;
				entropyYX += partialEntropy * (sum + this.alpha);
			}
			entropyYX /= (double) ((this.alpha * frequencyTable.length) + this.sizeTable);
			return fullEntropy - entropyYX;
		} else {
			return 0;
		}
	}

	@Override
	public double conditionalEntropy(int[][] frequencyTable) {
		double entropyYX = 0;
		final int sizeDiscreteValues=frequencyTable[0].length;
		double fullEntropy = fullEntropy(frequencyTable);
		// recorer tabla y calcular entropias parciales
		for (int[] line : frequencyTable) {
			double partialEntropy = 0;
			// nro de predicciones por linea
			int sum = IntStream.of(line).sum();
			// calcular H(Y|x) entropia de Y dado el estado x
			if (sum != 0) {
				partialEntropy = IntStream.of(line)
						.mapToDouble(a->Resources.entropy((double)a/sum, sizeDiscreteValues))
						.sum();
			} else
				partialEntropy = fullEntropy;
			entropyYX += partialEntropy * (sum + this.alpha);
		}
		entropyYX /= (double) ((this.alpha * frequencyTable.length) + this.sizeTable);
		return entropyYX;
	}

	@Override
	public String toShortString() {
		return "NO" + alpha;
	}

	@Override
	public ID_EVALUATION_METRIC getId() {
		// TODO Auto-generated method stub
		return ID_EVALUATION_METRIC.NO;
	}
	public static void main(String[] arg) {
		int[][] table= {{1,2,3},{4,0,0},{0,0,0},{8,2,0}
					   ,{2,0,0},{0,0,0},{0,0,8},{4,6,0}};
		int size=Stream.of(table).mapToInt(a->IntStream.of(a).sum()).sum();
		MutualInformation m=new MutualInformation(size);
		System.out.println(m.quantifyFromFrequencyTable(table));
		MutualInformationNotObservedPenality mno=new MutualInformationNotObservedPenality(0.0, size);
		System.out.println(mno.quantifyFromFrequencyTable(table));
	}
}
