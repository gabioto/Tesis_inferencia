package inference.qualifyingFunction;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;

public class LinearMix extends LinearGrouping {

	public LinearMix(EvaluationMetric evaluationMetric) {
		super(evaluationMetric);
	}
//	@Override
//	protected int[][][] buildGroups(int sizePredictors) {
//		int[][][] superGroups=super.buildGroups(sizePredictors);
//		int[][][] newGroups= new int[superGroups.length+1][][];
//		System.arraycopy(superGroups, 0, newGroups, 0,superGroups.length);
//		int sizefq=1 << sizePredictors;
//		int[][] fg=new int[sizefq][1];
//		for (int i = 0; i < fg.length; i++) {
//			fg[i][0]=i;
//		}
//		newGroups[superGroups.length]=fg;
//		//if(isWhitFrequencytable) aplicatedGroups=newGroups;
//		return newGroups;
//	}
//	@Override
//	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
//		int[][][] bt= super.buildTablesGruping(frequencyTable, sizePreditors);
//		int[][][] btadd = new int[bt.length+1][][];
//		//nuevogrupo aplicado
//		int[][][] newAppGroups = new int[bt.length+1][][];
//		System.arraycopy(bt, 0, btadd, 0, bt.length);
//		System.arraycopy(groups, 0, newAppGroups, 0,groups.length);
//		newAppGroups[bt.length]=new int[frequencyTable.length][2];
//		for (int i = 0; i < newAppGroups[bt.length][frequencyTable.length-1].length; i++) {
//			newAppGroups[bt.length][frequencyTable.length-1][i]=i;
//		}
//		//agregar la configuracion del nuevo grupo
//		aplicatedGroups=newAppGroups;
//		btadd[bt.length] = frequencyTable;
//		return btadd;
//	}
	@Override
	public String getName() {
		return "Lineal Mixto";
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		// TODO Auto-generated method stub
		return ID_QUALIFYING_FUNCTION.LM;
	}
}
