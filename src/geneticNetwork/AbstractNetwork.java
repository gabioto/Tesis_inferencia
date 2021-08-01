package geneticNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

//import inference.qualifyingFunction.Details;
import javafx.util.Pair;
import resources.Resources;

public abstract class AbstractNetwork {
	public abstract int[] getArrayPreditorsByGene(int idGene);
	public abstract long[] getMainFunctionByGene(int idGene);
	public abstract Pair<int[][], boolean[]> dinamicGeneDetails(int nGene, BooleanGeneExpressionData ged);
	public abstract int getSizeGenes();
	//public abstract byte[] getFunctionByteByGene(int nGene);
	public abstract NavigableSet<Integer> getPredictorsByGene(int nGene);
	
	public int[][] frequencyTableGene(int nGene, GeneExpressionData ged){
		return buildFrequencyTable(getPredictorsByGene(nGene), ged, nGene);
	}
	public static int[][] buildFrequencyTable(NavigableSet<Integer> preditors,
			GeneExpressionData geneExpressionData,
			int geneTarget) {
		int[][] table = new int[Resources.pow(geneExpressionData.sizeDiscreteValues(),preditors.size())][geneExpressionData.sizeDiscreteValues()];
		// contagem de 0s y 1s
		for (int j = 0; j < geneExpressionData.getSizeData() - 1; j++) {
			table[indexPreditorsPos(geneExpressionData, j, preditors)][geneExpressionData.getData(j + 1,geneTarget)]++;
		}
		return table;
	}
//	public static int[][] buildFrequencyTable(TreeSet<Integer> preditors, BooleanGeneExpressionData booleanGeneExpressionData, int geneTarget) {
//		// percurso no gene no tempo t e nos preditores tempo t+1
//		int[][] table = new int[1 << preditors.size()][2];
//		
//		// contagem de 0s y 1s
//		for (int j = 0; j < booleanGeneExpressionData.getSizeData() - 1; j++) {
//			table[indexPreditorsPos(booleanGeneExpressionData, j, preditors)][booleanGeneExpressionData.getData(j + 1,geneTarget)]++;
//		}
//		return table;
//	}
//	public static int[][] buildFrequencyTable(TreeSet<Integer> preditors, DiscreteGeneExpressionData geneExpresionDiscrete, int geneTarget) {
//		
//	}
	private static int indexPreditorsPos(GeneExpressionData geneExpressionData, int indexData, NavigableSet<Integer> preditors) {
		if(geneExpressionData instanceof BooleanGeneExpressionData)
			return indexPreditorsPosBool((BooleanGeneExpressionData)geneExpressionData, indexData, preditors);
		else
			return indexPreditorsPosDisc((DiscreteGeneExpressionData)geneExpressionData, indexData, preditors);
					
	}
	private static int indexPreditorsPosBool(BooleanGeneExpressionData geneExpressionData, int indexData, NavigableSet<Integer> preditors) {
		int i = 0;
		int index = 0;
		for (Iterator<Integer> it = preditors.iterator(); it.hasNext(); i++) {
			int preditor = it.next();
			index += ((geneExpressionData.getBooleanData(indexData,preditor)) ? (1 << i) : 0);
		}
		return index;
	}
	
	private static int indexPreditorsPosDisc(DiscreteGeneExpressionData geneDiscreteData, int indexData, NavigableSet<Integer> preditors) {
		int i = 0;
		int index = 0;
		for (Iterator<Integer> it = preditors.iterator(); it.hasNext(); i++) {
			int preditor = it.next();
			index += Resources.pow(3,i)*(geneDiscreteData.getData(indexData,preditor));
		}
		return index;
	}
	public int[] getDegrees() {
		int[] degrees=new int[this.getSizeGenes()];
		for(int i=0;i<this.getSizeGenes();i++)
			degrees[i]=getPredictorsByGene(i).size();
		return degrees;
	}
	public int[] getDegrees(List<Integer> validGens) {
		int[] degrees=new int[this.getSizeGenes()];
		for(int i=0;i<this.getSizeGenes();i++) {
			NavigableSet<Integer> predictors=getPredictorsByGene(i);
			int n=0;
			for (Integer p : predictors) {
				if(validGens.contains(p)) n++;
			}
			
			degrees[i]=n;
		}
		return degrees;
	}
	public byte[] getFunctionByteByGene(int nGene) {
		byte[] function=new byte[1<<getPredictorsByGene(nGene).size()];
		long[] booleanFunction=getMainFunctionByGene(nGene);
		if(booleanFunction==null)
			return null;
		for (int i = 0; i < function.length; i++) {
//			function[i]= ((booleanFunction[i/64] & 1L<<(i%64))==0)?Details.ZERO:Details.ONE;
			function[i]= (byte)(((booleanFunction[i/64] & 1L<<(i%64))==0)?0:1);
		}
		return function;
	}
	
//	public String detailsGeneExpression(int nGene, BooleanGeneExpressionData ged) {
//		StringBuilder sb=new StringBuilder("Gene:");
//		sb.append(nGene).append("\n");
//		TreeSet<Integer> preditors=getPredictorsByGene(nGene);
//		sb.append(preditors).append("\n");
//		//int nPreditors=preditors.size();
//		byte[] function= getFunctionByteByGene(nGene);
//		//sb.append(Resources.functionString(function, nPreditors)).append("\n");
//		sb.append(Resources.frequencyTableString(preditors, frequencyTableGene(nGene, ged), function));
//		return sb.toString();
//	}

	@SuppressWarnings("unchecked")
	public NavigableSet<Integer>[] canalizeGenes() {
		NavigableSet<Integer>[] canalize = new TreeSet[getSizeGenes()];
		for (int i = 0; i < canalize.length; i++) {
			canalize[i] = new TreeSet<>();
		}
		for (int i = 0; i < canalize.length; i++) {
			int[] gens = getArrayPreditorsByGene(i);
			List<Integer> indexCan = indexCanalizante(getMainFunctionByGene(i), gens.length);
			for (Iterator<Integer> iterator = indexCan.iterator(); iterator.hasNext();) {
				canalize[gens[iterator.next()]].add(i);
			}
		}
		return canalize;
	}

	public static int[][] canalizeMatriz(NavigableSet<Integer>[] canalizeGens) {
		int[][] matriz = new int[canalizeGens.length][canalizeGens.length];
		for (int i = 0; i < matriz.length; i++) {
			NavigableSet<Integer> can = canalizeGens[i];
			for (Iterator<Integer> iterator = can.iterator(); iterator.hasNext();) {
				matriz[i][iterator.next()] = 1;
			}
		}
		return matriz;
	}

	public static boolean isCanalizate(boolean[][] deg) {
		final int faixa=2;
		int[][] countCanal = new int[2][2];
		for (int i = 0; i < deg[0].length - 1; i++) {
	//		int intState = (deg[0][i]) ? 1 : 0;
			countCanal[(deg[0][i]) ? 1 : 0][(deg[1][i+1]) ? 1 : 0]++;
//			//iniciar el canal
//			if (canal[intState] == null) {
//				canal[intState] = deg[1][i + 1];
//				isCanal[intState] = true;
//			}
//			if (isCanal[intState] && !(canal[intState] == deg[1][i + 1]))
//				isCanal[intState] = false;
		}
		if(countCanal[0][0]>faixa && countCanal[0][1]==0)
			return countCanal[1][1]>faixa;
		if(countCanal[0][1]>faixa && countCanal[0][0]==0)
			return countCanal[1][0]>faixa;
		if (countCanal[1][0] > faixa && countCanal[1][1] == 0)
			return countCanal[0][1] > faixa;
		if (countCanal[1][1] > faixa && countCanal[1][0] == 0)
			return countCanal[0][0] > faixa;	
		return false;
		
	}

//	public static TreeSet<Integer> canalizateByDEGByGene(GeneExpressionData ged, int idGene) {
//		TreeSet<Integer> can = new TreeSet<>();
//		for (int i = 0; i < ged.getSizeGens(); i++) {
//			if (isCanalizate(new boolean[][] { ged.getGeneExpressionByGene(idGene), ged.getGeneExpressionByGene(i) }))
//				can.add(i);
//		}
//		return can;
//	}
//	public static TreeSet<Integer> canalizingByDEGByGene(GeneExpressionData geneExpressionData, int idGene) {
//		TreeSet<Integer> can = new TreeSet<>();
//		for (int i = 0; i < geneExpressionData.sizeGens; i++) {
//			if (isCanalizate(new boolean[][] { geneExpressionData.getGeneExpressionByGene(i), geneExpressionData.getGeneExpressionByGene(idGene) }))
//				can.add(i);
//		}
//		return can;
//	}
//	@SuppressWarnings("unchecked")
//	public static TreeSet<Integer>[] canalizateByDEG(BooleanGeneExpressionData ged) {
//		TreeSet<Integer>[] can = new TreeSet[ged.sizeGens];
//		for (int i = 0; i < ged.sizeGens; i++) {
//			can[i] = canalizateByDEGByGene(ged, i);
//		}
//		return can;
//	}
//	@SuppressWarnings("unchecked")
//	public static TreeSet<Integer>[] canalizingByDEG(BooleanGeneExpressionData ged) {
//		TreeSet<Integer>[] can = new TreeSet[ged.sizeGens];
//		for (int i = 0; i < ged.sizeGens; i++) {
//			can[i] = canalizingByDEGByGene(ged, i);
//		}
//		return can;
//	}
	public static List<Integer> indexCanalizante(long[] function, int sizegens) {
		List<Integer> indexs = new ArrayList<>();
		for (int j = 0; j < sizegens; j++) {
			long[] mask = Resources.maskArrayCanalizant(sizegens, j);
//			System.out.println("inp "+Resources.toTrueTableFormat(function[0], 1<<sizegens));
//			System.out.println("mas "+Resources.toTrueTableFormat(mask[0], 1<<sizegens));
			long[] resultLog = Resources.andFunction(mask, function);
//			System.out.println("and \t" +Resources.toTrueTableFormat(resultLog[0], 1<<sizegens));
			if (Arrays.equals(mask, resultLog)) {
				indexs.add(j);
				continue;
			}
			resultLog = Resources.orFunction(mask, function);
//			System.out.println("or \t" +Resources.toTrueTableFormat(resultLog[0], 1<<sizegens));
			if (Arrays.equals(mask, resultLog)) {
				indexs.add(j);
				continue;
			}
			mask = Resources.notFunction(mask, sizegens);
//			System.out.println(Resources.toTrueTableFormat(mask[0], 1<<sizegens));
//			System.out.println("not\t"+Long.toBinaryString(mask[0]));
//			
			resultLog = Resources.andFunction(mask, function);
//			System.out.println("and \t" +Resources.toTrueTableFormat(resultLog[0], 1<<sizegens));
			if (Arrays.equals(mask, resultLog)) {
				indexs.add(j);
				continue;
			}
			resultLog = Resources.orFunction(mask, function);
//			System.out.println("or \t" +Resources.toTrueTableFormat(resultLog[0], 1<<sizegens));
			if (Arrays.equals(mask, resultLog)) {
				indexs.add(j);
				continue;
			}
		}
		return indexs;
	}

	public int[] sizeCanalizeGenes() {
		int[] canalize = new int[getSizeGenes()];
		for (int i = 0; i < canalize.length; i++) {
			int[] gens = getArrayPreditorsByGene(i);
			for (int j = 0; j < gens.length; j++) {
				long[] mask = Resources.maskArrayCanalizant(gens.length, j);

				if (Arrays.equals(mask, Resources.andFunction(mask, getMainFunctionByGene(i)))) {
					canalize[gens[j]]++;
					continue;
				}
				if (Arrays.equals(mask, Resources.orFunction(mask, getMainFunctionByGene(i)))) {
					canalize[gens[j]]++;
					continue;
				}
				mask = Resources.notFunction(mask);
				if (Arrays.equals(mask, Resources.andFunction(mask, getMainFunctionByGene(i)))) {
					canalize[gens[j]]++;
					continue;
				}
				if (Arrays.equals(mask, Resources.orFunction(mask, getMainFunctionByGene(i)))) {
					canalize[gens[j]]++;
					continue;
				}
			}
		}
		return canalize;
	}

	public boolean aplicatedFunction(long[] function, int inputLogic) {
		long posBit = ((long) 1 << (inputLogic % 64));
		return ((function[inputLogic / 64] & posBit) != 0);
	}

	public int possitonTable(NavigableSet<Integer> preditores, boolean[] state) {
		int nbits = 0, i = 0;
		for (Iterator<Integer> it = preditores.iterator(); it.hasNext(); i++) {
			nbits += (state[it.next()]) ? (int) (1 << i) : 0;
		}
		return nbits;
	}

	public boolean aplicatedFunction(NavigableSet<Integer> preditores, long[] function, boolean[] previunState) {
		return aplicatedFunction(function, possitonTable(preditores, previunState));
	}

	public boolean[] makeGeneExpresionState(NavigableSet<Integer>[] preditores, long[][] functions, boolean[] previunState) {
		boolean[] nextState = new boolean[previunState.length];
		for (int nGen = 0; nGen < previunState.length; nGen++) {
			// para cada gen
			if (functions[nGen] != null) {
				nextState[nGen] = aplicatedFunction(preditores[nGen], functions[nGen], previunState);
			} else
				nextState[nGen] = Resources.random.nextBoolean();
		}
		return nextState;
	}
}