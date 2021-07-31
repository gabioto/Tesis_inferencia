package inference.qualifyingFunction;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;

public class LinearSelective extends LinearGrouping {
	private int minNotGrouping;

	// public LinearSelective(EvaluationMetric evaluationMetric, boolean
	// isWhitFrequencytable) {
	// super(evaluationMetric, isWhitFrequencytable);
	// this.minNotGrouping=0;
	// }
	public LinearSelective(EvaluationMetric evaluationMetric, int minNotGrouping) {
		super(evaluationMetric);
		this.minNotGrouping = minNotGrouping;
	}

	@Override
	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		for (int[] is : frequencyTable) {
			if ((is[0] + is[1]) < minNotGrouping) {
				// grupos aplicados
				// if (isWhitFrequencytable)
				// aplicatedGroups = groups;
				return super.buildTablesGruping(frequencyTable, sizePreditors);
			}
		}
		// if (isWhitFrequencytable) {
		// int[][][] newGroups = new int[1][frequencyTable.length][1];
		// for (int i = 0; i < newGroups[0].length; i++) {
		// newGroups[0][i][0] = i;
		// }
		// aplicatedGroups = newGroups;
		// }
		return new int[][][] { frequencyTable };
	}

	@Override
	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int size) {
		for (int[] is : frequencyTable) {
			if ((is[0] + is[1]) < minNotGrouping) {
				//aplicatedGroups = groups;
				return super.buildSaveTablesGruping(frequencyTable, size);
			}
		}
		int[][][] newGroups = new int[1][frequencyTable.length][1];
		for (int i = 0; i < newGroups[0].length; i++) {
			newGroups[0][i][0] = i;
		}
		aplicatedGroups = newGroups;
		return new int[][][] { frequencyTable };
	}

	@Override
	public String getName() {
		return "Linear Selectivo";
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		// TODO Auto-generated method stub
		return ID_QUALIFYING_FUNCTION.LS;
	}
}
