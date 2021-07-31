package inference.qualifyingFunction;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;

public class LinearSelectiveGrouping extends LinearGrouping {
	private int minNotGrouping;
	// public LinearSelectiveGrouping(EvaluationMetric evaluationMetric) {
	// super(evaluationMetric);
	// this.minNotGrouping=0;
	// }

	public LinearSelectiveGrouping(EvaluationMetric evaluationMetric, int minNotGrouping) {
		super(evaluationMetric);
		this.minNotGrouping = minNotGrouping;
	}

	@Override
	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		int[][][] tables = new int[groups.length][][];
		// int[][][] newGroups =(isWhitFrequencytable)?new int[groups.length][][]: null;
		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
			boolean[] needGruping = new boolean[groups[iGrouping].length];
			int nlines = 0;
			loop_group: for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
				for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
					if ((frequencyTable[groups[iGrouping][iGroup][iElement]][0]
							+ frequencyTable[groups[iGrouping][iGroup][iElement]][1]) < this.minNotGrouping) {
						needGruping[iGroup] = true;
						nlines++;
						continue loop_group;
					}
				}
				nlines += groups[iGrouping][iGroup].length;// llega aqui solo cuando no se necesita agrupar (divide el
															// grupo)
			}
			// iniciar las tablas
			tables[iGrouping] = new int[nlines][2];
			// iniciar la tabla de genes por grupo
			// if(isWhitFrequencytable) newGroups[iGrouping] = new int[nlines][];
			// recorrer y llenar las tablas y los genes por grupo
			int line = 0;
			for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
				if (needGruping[iGroup]) {
					for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
						tables[iGrouping][line][0] += frequencyTable[groups[iGrouping][iGroup][iElement]][0];
						tables[iGrouping][line][1] += frequencyTable[groups[iGrouping][iGroup][iElement]][1];
					}
					// mantener el grupo
					// if(isWhitFrequencytable)
					// newGroups[iGrouping][line]=groups[iGrouping][iGroup];
					line++;
				} else {// separacion del grupo
					for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
						tables[iGrouping][line][0] = frequencyTable[groups[iGrouping][iGroup][iElement]][0];
						tables[iGrouping][line][1] = frequencyTable[groups[iGrouping][iGroup][iElement]][1];
						// un grupo por gen
						// if(isWhitFrequencytable) newGroups[iGrouping][line]= new
						// int[]{groups[iGrouping][iGroup][iElement]};
						line++;
					}
				}
			}
		}
		// if(isWhitFrequencytable) aplicatedGroups=newGroups;
		return tables;
	}

//	@Override
//	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int sizePreditors) {
//		if (sizeGroup != sizePreditors) {
//			sizeGroup = sizePreditors;
//			if (sizeGroup > 1) {
//				groups = buildGroups(sizePreditors);
//			}
//		}
//		int[][][] tables = new int[groups.length][][];
//		int[][][] newGroups = new int[groups.length][][];
//		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
//			boolean[] needGruping = new boolean[groups[iGrouping].length];
//			int nlines = 0;
//			loop_group: for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
//				for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
//					if ((frequencyTable[groups[iGrouping][iGroup][iElement]][0]
//							+ frequencyTable[groups[iGrouping][iGroup][iElement]][1]) < this.minNotGrouping) {
//						needGruping[iGroup] = true;
//						nlines++;
//						continue loop_group;
//					}
//				}
//				nlines += groups[iGrouping][iGroup].length;// llega aqui solo cuando no se necesita agrupar (divide el
//															// grupo)
//			}
//			// iniciar las tablas
//			tables[iGrouping] = new int[nlines][2];
//			// iniciar la tabla de genes por grupo
//			newGroups[iGrouping] = new int[nlines][];
//			// recorrer y llenar las tablas y los genes por grupo
//			int line = 0;
//			for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
//				if (needGruping[iGroup]) {
//					for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
//						tables[iGrouping][line][0] += frequencyTable[groups[iGrouping][iGroup][iElement]][0];
//						tables[iGrouping][line][1] += frequencyTable[groups[iGrouping][iGroup][iElement]][1];
//					}
//					// mantener el grupo
//					newGroups[iGrouping][line] = groups[iGrouping][iGroup];
//					line++;
//				} else {// separacion del grupo
//					for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
//						tables[iGrouping][line][0] = frequencyTable[groups[iGrouping][iGroup][iElement]][0];
//						tables[iGrouping][line][1] = frequencyTable[groups[iGrouping][iGroup][iElement]][1];
//						// un grupo por gen
//						newGroups[iGrouping][line] = new int[] { groups[iGrouping][iGroup][iElement] };
//						line++;
//					}
//				}
//			}
//		}
//		aplicatedGroups = newGroups;
//		return tables;
//	}

	@Override
	public String getName() {
		return "Lineal Selectivo Grupo";
	}

	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		// TODO Auto-generated method stub
		return ID_QUALIFYING_FUNCTION.LSG;
	}
}