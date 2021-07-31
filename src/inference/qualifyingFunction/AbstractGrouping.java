package inference.qualifyingFunction;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableSet;

import geneticNetwork.AbstractNetwork;
import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.evaluationMetric.EvaluationMetric;
import javafx.util.Pair;
import resources.Resources;

public abstract class AbstractGrouping extends WithoutGrouping {
	protected int[][][] aplicatedGroups;
	public int[][][] groups;
	

	public AbstractGrouping(EvaluationMetric evaluationMetric) {
		super(evaluationMetric);
	}

	@Override
	public Details quantifyDetails(NavigableSet<Integer> preditors,
			GeneExpressionData geneExpressionData,
			Integer geneTarget) {
		if (preditors.size() <= 1)
			return super.quantifyDetails(preditors, geneExpressionData, geneTarget);
		DetailsGroup details = new DetailsGroup();
		details.idGene=geneTarget;
		details.preditors = preditors;
		int[][] frequencyTable = AbstractNetwork.buildFrequencyTable(preditors, geneExpressionData, geneTarget);
		details.frequencyTable = frequencyTable;
		double quantifyValue = 0;
		int[][][] tables =(geneExpressionData instanceof BooleanGeneExpressionData)? 
				buildTablesGruping(frequencyTable, preditors.size())
				:buildTablesGrupingDiscret(frequencyTable, preditors.size());
		for (int i = 0; i < tables.length; i++) {
			// int[][] frequencyGroups = ;
			double testQuantify = evaluationMetric.quantifyFromFrequencyTable(tables[i]);
			if (Resources.compareDouble(testQuantify, quantifyValue) > 0) {// (testQuantify > quantifyValue) {
				quantifyValue = testQuantify;
				details.frequencyTableGroup= new int[][][]{tables[i]};
			}
		}
		details.qualityValue = quantifyValue;
		return details;
	}
	@Override
	public Double quantifyDiscrete(NavigableSet<Integer> preditors, GeneExpressionData gdd, Integer geneTarget) {
		if (preditors.size() <= 1)
			return super.quantifyDiscrete(preditors, gdd, geneTarget);
		int[][] frequencyTable = AbstractNetwork.buildFrequencyTable(preditors, gdd, geneTarget);
		double quantifyValue = 0;
//		int[][][] groups=buildGroups()
		int[][][] tables = buildTablesGrupingDiscret(frequencyTable, preditors.size());
		for (int i = 0; i < tables.length; i++) {
			// int[][] frequencyGroups = ;
			double testQuantify = evaluationMetric.quantifyFromFrequencyTable(tables[i]);
			if (Resources.compareDouble(testQuantify, quantifyValue) > 0) {// (testQuantify > quantifyValue) {
				quantifyValue = testQuantify;
			}
		}
		return quantifyValue;
//		return evaluationMetric.quantifyFromFrequencyTable(frequencyTable);	
	}
	@SuppressWarnings("unchecked")
	@Override
	public final void buildGroupingDetails(Details details) {
		if (details.preditors.size() <= 1) {
			super.buildGroupingDetails(details);
			return;
		}
		double quantifyValue = -1;
		// verificar grupos con la mayor funcion criterio
		// guardar la entropia de X
		details.entropyX = Resources.entropyLine(Resources.mergeTable(details.frequencyTable));
		int[][][] tables = buildSaveTablesGruping(details.frequencyTable, details.preditors.size());
		List<Integer> indexGrouping = new ArrayList<>();
		//escojer la tabla con mayor funcion criterio
		for (int i = 0; i < tables.length; i++) {
			double testQuantify = evaluationMetric.quantifyFromFrequencyTable(tables[i]);
			if (Resources.compareDouble(testQuantify, quantifyValue) > 0) {// (testQuantify > quantifyValue) {
				quantifyValue = testQuantify;
				indexGrouping.clear();
				indexGrouping.add(i);
			} else if (Resources.compareDouble(testQuantify, quantifyValue) == 0) {// (testQuantify > quantifyValue) {
				indexGrouping.add(i);
			}
		}

		// guardar la configuracion de la tabla, la tabla de frequencias y las funciones
		int[][][] fequencyTablesAux = new int[indexGrouping.size()][][];
		int[][][] groupsFAux = new int[fequencyTablesAux.length][][];
		Pair<Boolean, byte[]>[] functionsAux = new Pair[fequencyTablesAux.length];
		for (int i = 0; i < fequencyTablesAux.length; i++) {
			fequencyTablesAux[i] = tables[indexGrouping.get(i)];
			groupsFAux[i] = aplicatedGroups[indexGrouping.get(i)];
/////////////////////////////////////////////			
			functionsAux[i] = buildFunctions(details.preditors.size(), fequencyTablesAux[i], groupsFAux[i]);
/////////////////////////////////////////////			
		}
		// filtrar las funciones
		List<Integer> indexFilters = new ArrayList<>();
		boolean isReducible = true;
		int maxfunction = Integer.MAX_VALUE;
		for (int i = 0; i < groupsFAux.length; i++) {
			if (!functionsAux[i].getKey())
				isReducible = false;
			if (isReducible)
				indexFilters.add(i);
			else {
				if (!functionsAux[i].getKey()) {
					int count=countNone(functionsAux[i].getValue());
					if (count < maxfunction) {
						maxfunction = count;
						indexFilters.clear();
						indexFilters.add(i);
					} else if (count == maxfunction)
						indexFilters.add(i);
				}
			}
		}
		int[][][] fequencyTables = new int[indexFilters.size()][][];
		int[][][] groupsF = new int[fequencyTables.length][][];
		byte[][] functions = new byte[fequencyTables.length][];
		for (int i = 0; i < groupsF.length; i++) {
			fequencyTables[i] = fequencyTablesAux[indexFilters.get(i)];
			groupsF[i] = groupsFAux[indexFilters.get(i)];
			functions[i] = functionsAux[indexFilters.get(i)].getValue();
		}
		// filtrar funciones repetidas
//		ArrayList<long[]> repFunctions = new ArrayList<>();
//		for (int i = 0; i < functions.length; i++) {
//			for (int j = 0; j < functions[i].length; j++) {
//				if (!Resources.containsArray(repFunctions, functions[i][j])) {
//					repFunctions.add(functions[i][j]);
//				}
//			}
//		}
//		details.function=new long[repFunctions.size()][];
//		for (int i = 0; i <details.function.length; i++) {
//			details.function[i]=repFunctions.get(i);
//		}
		details.function=functions[0];
		((DetailsGroup) details).frequencyTableGroup = fequencyTables;
		((DetailsGroup) details).groupsTable = groupsF;
		((DetailsGroup) details).functionsGroup = functions;
		//details.frequencyTable=fequencyTables[0];
		details.isReductora = isReducible;
	}

	private Pair<Boolean,byte[]> buildFunctions(int nPreditors, int[][] frequencyTable, int[][] groupTable) {
		// construir funciones booleanas
		byte[] function = new byte[1 << nPreditors];
		long functionLong=0L;
		List<int[]> groupsNone=new ArrayList<>();
		for (int i = 0; i < frequencyTable.length; i++) {
			if ((frequencyTable[i][1] > frequencyTable[i][0])) {
				for (int j = 0; j < groupTable[i].length; j++) {
					int index = groupTable[i][j];
					function[index] = Details.ONE;
					functionLong=functionLong|(1L<<index);
				}
			}else if ((frequencyTable[i][1] < frequencyTable[i][0])) {
				for (int j = 0; j < groupTable[i].length; j++) {
					int index = groupTable[i][j];
					function[index] = Details.ZERO;
				}
			}
			else{
				for (int j = 0; j < groupTable[i].length; j++) {
					int index = groupTable[i][j];
					function[index] = Details.NONE;
				}
				groupsNone.add(groupTable[i]);
			}
		}
		if(nPreditors>Resources.MAX_REDUCIBLE) {
			return new Pair<>(false, function);
		}
		else {
			List<Long> functionsNotR=new ArrayList<>();
			long mask=0L;
			long premask=0L;
			do {
				long testF=functionLong|mask;
				if(!Resources.isReducible(nPreditors,testF ))
					functionsNotR.add(testF);
				long pMasks[]=maskFunction(groupsNone,premask, mask);
				premask=pMasks[0];
				mask=pMasks[1];
			}while(premask!=0L);
			if(functionsNotR.isEmpty())return new Pair<>(true, function);
			//verificar estado de los NONES
			for (Iterator<int[]> it = groupsNone.iterator(); it.hasNext();) {
				int[] i=it.next();
				function=stateBitFunction(function,functionsNotR, i);
			}
		}
//		// verificar funciones reducibles y asignar peso
//		for (int j = 0; j < booleanTables.size(); j++) {
//			if (booleanTables.get(j).length == 1)
//				if (Resources.isReducible(nPreditors, booleanTables.get(j)[0])) {
//					booleanTables.remove(j);
//					j--;
//				}
//		}
//		long[][] functions = new long[booleanTables.size()][];
//		for (int i = 0; i < booleanTables.size(); i++) {
//			functions[i] = booleanTables.get(i);
//		}
//		// details.isReductora= (booleanTables.size()==0);
		return new Pair<>(false, function);
	}

	private byte[] stateBitFunction(byte[] function, List<Long> functionsNotR, int[] indexs) {
		byte state =Details.NONE;
		long bitState=1L<<indexs[0];
		for (Iterator<Long> it = functionsNotR.iterator(); it.hasNext();) {
			if((it.next()&bitState)==bitState) {
				if(state==Details.IZERO)
					return function;
				state=Details.IONE;
			}else {
				if(state==Details.IONE)
					return function;
				state=Details.IZERO;
			}
		}
		for (int i = 0; i < indexs.length; i++) {
			function[i]=state;
		}
		return function;
	}

	private static long[] maskFunction(List<int[]> groupsNone, long premask, long mask) {
		for (int i = 0; i < groupsNone.size(); i++) {
			//si tem um zero
			long nvaMask;
			long bitPos=(1L<<i);
			if((nvaMask=(premask | bitPos))!=premask) {
				for (int index : groupsNone.get(i))
					mask=mask|(1L<<index);
				return new long[]{nvaMask,mask};
			}
			else{//tiene un uno volver 0
				premask=premask^bitPos;
				for (int index : groupsNone.get(i))
					mask=mask^(1L<<index);
			}
		}
		return new long[] {premask,mask};
	}
	private int countNone(byte[] function) {
		int count=0;
		for (int i = 0; i < function.length; i++) {
			if(function[i]==Details.NONE)count++;
		}
		return count;
	}

	protected abstract int[][][] buildSaveTablesGruping(int[][] frequencyTable, int size);

	protected abstract int[][][] buildTablesGruping(int[][] frequencyTable, int size);
	
	protected abstract int[][][] buildTablesGrupingDiscret(int[][] frequencyTable, int sizePreditors);

	public abstract String getName();

	public static void main(String[] arg) {
		long mask=0L;
		long premask=0L;
		List<int[]> pos=new ArrayList<>();
		pos.add(new int[]{0});pos.add(new int[]{1,2});pos.add(new int[]{3,5});
		
		do {
			System.out.println(Long.toBinaryString(mask)+ "\t"+Long.toBinaryString(premask));
			long[] m=maskFunction(pos,premask, mask);
			premask=m[0];
			mask=m[1];
		}while(mask!=0l);		
	}
}
