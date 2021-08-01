package inference.evaluationMetric;


import java.util.stream.IntStream;

import inference.ConfigInference.ID_EVALUATION_METRIC;
import resources.Resources;

public class MutualInformation implements EvaluationMetric {
	protected final int sizeTable;
	public MutualInformation(int sizeTable) {
		this.sizeTable=sizeTable;
	}
	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable, double baseEmtropy) {
		if (baseEmtropy != 0)
			//	return fullEntropy - conditionalEntropy(frequencyTable);
			 return (baseEmtropy - conditionalEntropy(frequencyTable))/baseEmtropy;
			else
				return 0.0;
	}
	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable) {
		double fullEntropy=fullEntropy(frequencyTable);
		return quantifyFromFrequencyTable(frequencyTable,fullEntropy);
	}

	public double fullEntropy(int[][] frequencyTable) {
		int sizeDiscreteValues=frequencyTable[0].length;
		// contagem de 0s y 1s
		int[] countAll = new int[sizeDiscreteValues] ;
		// calcular H(Y) entropia de Y={0,1} total
//		int size=0;
		for (int[] line : frequencyTable) {
			for (int i = 0; i < sizeDiscreteValues; i++) {
				countAll[i]+=line[i];
//				size+=line[i];
			}
//			countAll[0] += line[0];
//			countAll[1] += line[1];
		}
//		int size = countAll[0] + countAll[1];
//		double fullEntropy = Resources.entropy((double) countAll[0] / size, 2)
//				+ Resources.entropy((double) countAll[1] / size, 2);
		double fullEntropy =0;
		for (int i = 0; i < sizeDiscreteValues; i++) {
			fullEntropy+=Resources.entropy((double) countAll[i] / this.sizeTable, sizeDiscreteValues);
		}
		return fullEntropy;
	}

	public double conditionalEntropy(int[][] frequencyTable) {
		double entropyYX = 0;
//		int size = 0;
		int sizeDiscreteValues=frequencyTable[0].length;
		// recorer tabla y calcular entropias parciales
		for (int[] line : frequencyTable) {
			double partialEntropy = 0;
			// nro de predicciones por linea
			//int sum = line[0] + line[1];
			int sum=Resources.sum(line);
			// calcular H(Y|x) entropia de Y dado el estado x
			if (sum != 0) {
				partialEntropy =0;
				for (int i = 0; i < sizeDiscreteValues; i++) {
					partialEntropy+=Resources.entropy((double) line[i] / sum, sizeDiscreteValues);
				}
//				partialEntropy=IntStream.of(line).mapToDouble(l->Resources.entropy((double)l / sum, sizeDiscreteValues)).sum();
//				Resources.entropy((double) line[0] / sum, 2)// entropia
//																				// H(Y|x=0)
//						+ Resources.entropy((double) line[1] / sum, 2);// entropia
//																		// H(Y|x=1)
//				size += sum;
				entropyYX += partialEntropy * sum;
			}
		}
		entropyYX /= (double) (this.sizeTable);
		return entropyYX;
	}
	public double conditionalEntropy2(int[][] frequencyTable) {
		double entropyYX = 0;
//		int size = 0;
		final int sizeDiscreteValues=frequencyTable[0].length;
		// recorer tabla y calcular entropias parciales
		for (int[] line : frequencyTable) {
			int sum=IntStream.of(line).sum();
			if(sum!=0) {
				entropyYX+= (sum *
					IntStream.of(line)
					.mapToDouble(l->Resources.entropy((double)l / sum, sizeDiscreteValues))
					.sum());
//				size += sum;
			}
		}
		entropyYX /= (double) (this.sizeTable);
		return entropyYX;
	}

	@Override
	public String toShortString() {
		return "IM";
	}
	@Override
	public ID_EVALUATION_METRIC getId() {
		return ID_EVALUATION_METRIC.IM;
	}
}
