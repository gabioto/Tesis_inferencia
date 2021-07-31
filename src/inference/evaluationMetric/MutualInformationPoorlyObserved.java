package inference.evaluationMetric;

import inference.ConfigInference.ID_EVALUATION_METRIC;
import resources.Resources;

public class MutualInformationPoorlyObserved extends MutualInformation {
	//private Resources Resources=new Resources();
	private final int minObservations;

	public MutualInformationPoorlyObserved(int sizeTable) {
		super(sizeTable);
		this.minObservations = 1;
	}

	public MutualInformationPoorlyObserved(int minObservations,int sizeTable) {
		super(sizeTable);
		this.minObservations = minObservations;
	}

	@Override
	public double quantifyFromFrequencyTable(int[][] frequencyTable) {
		double entropyYX = 0;
		// contagem de 0s y 1s
		int[] countAll = new int[] { 0, 0 };
		// calcular H(Y) entropia de Y={0,1} total
		for (int[] line : frequencyTable) {
			countAll[0] += line[0];
			countAll[1] += line[1];
		}
//		int size = countAll[0] + countAll[1];
		double fullEntropy = Resources.entropy((double) countAll[0] / this.sizeTable, 2)
				+ Resources.entropy((double) countAll[1] / this.sizeTable, 2);
		if ( Resources.compareDouble(fullEntropy , 0)!=0) {//(fullEntropy != 0) {
			// recorer tabla y calcular entropias parciales
			for (int[] line : frequencyTable) {
				double partialEntropy = 0;
				// nro de predicciones por linea
				int sum = line[0] + line[1];
				// calcular H(Y|x) entropia de Y dado el estado x
				if (sum != 0) {
					if (sum > this.minObservations)
						partialEntropy = Resources.entropy((double) line[0] / sum, 2)// entropia
																						// H(Y|x=0)
								+ Resources.entropy((double) line[1] / sum, 2);// entropia
																				// H(Y|x=1)
					else
						partialEntropy = 1;
				}
				//
				entropyYX += partialEntropy * sum;
			}
			entropyYX /= (double) (this.sizeTable);
			return fullEntropy - entropyYX;
		} else {
			return 0;
		}
	}
	@Override
	public String toShortString() {
		return "PO"+minObservations;
	}

	@Override
	public ID_EVALUATION_METRIC getId() {
		// TODO Auto-generated method stub
		return ID_EVALUATION_METRIC.PO;
	}

}
