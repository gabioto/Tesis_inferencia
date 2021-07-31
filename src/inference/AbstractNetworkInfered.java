package inference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;

import java.util.TreeSet;
import java.util.stream.IntStream;

import geneticNetwork.AbstractNetwork;
import geneticNetwork.BooleanGeneExpressionData;
import inference.qualifyingFunction.Details;
import javafx.util.Pair;
import resources.Resources;

public abstract class AbstractNetworkInfered extends AbstractNetwork {
	public abstract GeneInfered getGeneInferedById(int idGene);
	public abstract List<Integer> getIntegerGens();
	
	@Override
	public NavigableSet<Integer> getPredictorsByGene(int idGene) {
		return getGeneInferedById(idGene).getPredictors();
	}

	/**
	 * returm pair<bolean,Boolean> key boolean stato de aplicar la funcion value
	 * Booleam null determinitico true zero false equals
	 */
	public Pair<Boolean, Boolean> aplicatedFunction(NavigableSet<Integer> preditores, long[] function,
			boolean[] previunState, long[] state_functions, long[] state_notobserved, int[] globalFrequency) {
		boolean state;
		Boolean probabilisticValue = null;
		// GED ged = new GED();
		int nbits = possitonTable(preditores, previunState);
		long posBit = (1L << (nbits % 64));
		if ((state_functions[nbits / 64] & posBit) != 0)
			state = aplicatedFunction(function, nbits);
		else {// estate equals
//			int randon = Resources.random.nextInt(globalFrequency[0] + globalFrequency[1]);
//			ged.state = (globalFrequency[0] < randon);
			state = (globalFrequency[1] > globalFrequency[0]);
			if ((globalFrequency[1] == globalFrequency[0]))
				state = Resources.random.nextBoolean();
			probabilisticValue = ((state_notobserved[nbits / 64] & posBit) == 0);// true zero false equal
		}
		return new Pair<>(state, probabilisticValue);
	}

	public Pair<Boolean, Boolean> aplicatedFunction(GeneInfered gene, boolean[] previunState) {
		if (gene.getPredictors().size()==0)
			return new Pair<>(Resources.random.nextBoolean(), null);
		else
			return aplicatedFunction(gene.getPredictors(), gene.getBoolean_functions(), previunState,
					gene.getState_functions(), gene.getState_notObserved(), gene.getGlobal_boolean_values());
	}

	/**
	 * Genera el sgte estado de la red dado el estado previo
	 * 
	 * @param previunState Estado previo
	 * @param nextState    Variable de salida donde se almacena el estado sgte
	 * @return arreglo de dos enteros (pos0:valores sorteados por 0 observaciones,
	 *         pos1: valores sorteados por observaciones iguales)
	 */
//	class MakeGEDState {
//		boolean[] state;
//		int[] probabilisticValues;
//	}
	public Pair<boolean[], Boolean[]> makeGeneExpresionState(boolean[] previunState) {
//		MakeGEDState makeGED = new MakeGEDState();
		boolean[] state;
		Boolean[] probabilisticValues;
		probabilisticValues = new Boolean[previunState.length];
		state = new boolean[previunState.length];
		for (int nGen = 0; nGen < getSizeGenes(); nGen++) {
			GeneInfered geneInfered=this.getGeneInferedByIndex(nGen);
			int idGene=geneInfered.getIdGene();
			Pair<Boolean, Boolean> ged = aplicatedFunction(geneInfered, previunState);
			state[idGene] = ged.getKey();
			probabilisticValues[idGene] = ged.getValue();
		}
		return new Pair<>(state, probabilisticValues);
	}
	public boolean[] makeGeneExpresionState(boolean[] previunState, List<Integer> genes) {
//		MakeGEDState makeGED = new MakeGEDState();
		boolean[] state;
		state = new boolean[genes.size()];
		for (int nGen = 0; nGen < genes.size(); nGen++) {
			GeneInfered geneInfered=this.linealSearchGene(genes.get(nGen));
			Pair<Boolean, Boolean> ged = aplicatedFunction(geneInfered, previunState);
			state[nGen] = ged.getKey();
		}
		return state;
	}
	
	protected abstract GeneInfered getGeneInferedByIndex(int nGen);
	public GeneInfered linealSearchGene(Integer idGene) {
		for (int nGen = 0; nGen < this.getSizeGenes(); nGen++) {
			GeneInfered g= this.getGeneInferedByIndex(nGen);
			if(g.getIdGene()==idGene)
				return g;
		}
		return null;
	}
	public Pair<BooleanGeneExpressionData, Boolean[][]> makeGeneExpresionState(BooleanGeneExpressionData ged) {
//		MakeGEDState makeGED = new MakeGEDState();
		boolean[][] states;
		Boolean[][] probabilisticValues;
		probabilisticValues = new Boolean[ged.sizeData][];
		states = new boolean[ged.sizeData][];
		for (int ndata = 0; ndata < ged.sizeData; ndata++) {
			Pair<boolean[], Boolean[]> statProbs = makeGeneExpresionState(ged.getCopyData(ndata));
			states[ndata] = statProbs.getKey();
			probabilisticValues[ndata] = statProbs.getValue();
		}
		return new Pair<>(new BooleanGeneExpressionData(states), probabilisticValues);
	}

	public Pair<boolean[], int[]> makeProportionGeneExpresionState(boolean[] previunState) {
		boolean[] state;
		int[] probabilisticValues;
		probabilisticValues = new int[] { 0, 0 };
		state = new boolean[previunState.length];
		for (int nGen = 0; nGen < this.getSizeGenes(); nGen++) {
			GeneInfered geneInfered=this.getGeneInferedByIndex(nGen);
			int idGene=geneInfered.getIdGene();
			Pair<Boolean, Boolean> ged = aplicatedFunction(geneInfered, previunState);
			state[idGene] = ged.getKey();
			if (ged.getValue() != null) {
				if (ged.getValue())
					probabilisticValues[0]++;
				probabilisticValues[1]++;
			}
		}
		return new Pair<>(state, probabilisticValues);
	}

	/**
	 * Genera el sgte estado de la red dado el estado previo
	 * 
	 * @param previunState          estado previo
	 * @param nextState             espacio donde almacena el sgte estado
	 * @param sizePreditorsGabarito //arreglo con el numero de preditores de todos
	 *                              los genes
	 * @return Matriz de proporciones de predicciones no observadas y empates divido
	 *         por tamaño de predictores
	 */
//	class MakeGEDState2 {
//		boolean[] state;
//		double[][] probabilisticValues;
//	}

	public Pair<boolean[], double[][]> makeProportionGeneExpresionState(boolean[] previunState,
			int[] sizePreditorsGabarito) {
		// MakeGEDState2 mGed2 = new MakeGEDState2();
		boolean[] state;
		double[][] probabilisticValues;
		state = new boolean[previunState.length];
		int maxDegree = Resources.max(sizePreditorsGabarito);
		int[][] countProbValues = new int[maxDegree + 2][];
		int[] sizeDegreeValues = new int[maxDegree + 2];
		for (int i = 0; i < countProbValues.length; i++) {
			countProbValues[i] = new int[] { 0, 0 };
		}
		for (int nGen = 0; nGen < this.getSizeGenes(); nGen++) {
			GeneInfered geneInfered=this.getGeneInferedByIndex(nGen);
			int idGene=geneInfered.getIdGene();
//			Pair<Boolean, Boolean> ged = aplicatedFunction(gene.getPredictors(), gene.getBoolean_functions(),
//					previunState, gene.getState_functions(), gene.getState_notObserved(),
//					gene.getGlobal_boolean_values());
			Pair<Boolean, Boolean> ged = aplicatedFunction(geneInfered, previunState);
			state[idGene] = ged.getKey();
			if (ged.getValue() != null) {
				if (ged.getValue()) {
					countProbValues[sizePreditorsGabarito[idGene]][0]++;
					countProbValues[maxDegree + 1][0]++;
				}
				countProbValues[sizePreditorsGabarito[idGene]][1]++;
				countProbValues[maxDegree + 1][1]++;
			}
			sizeDegreeValues[sizePreditorsGabarito[idGene]]++;
		}
		sizeDegreeValues[maxDegree + 1] = previunState.length;
		probabilisticValues = Resources.divArrayFrequency(countProbValues, sizeDegreeValues);
		return new Pair<>(state, probabilisticValues);
	}

//	public class MakeGED {
//		public BooleanGeneExpressionData geneExpressionData;
//		public double[][] probabilisticValues;
//	}

	public Pair<BooleanGeneExpressionData, double[][]> makeProportionGeneExpresionState(BooleanGeneExpressionData ged, int[] genesInc,
			int[] degrees) {
		BooleanGeneExpressionData booleanGeneExpressionData;
		double[][] probabilisticValues;
//		MakeGED makeGED = new MakeGED();
		probabilisticValues = new double[degrees.length + 1][2];
		boolean[][] gedBol = new boolean[ged.sizeData][];
		for (int i = 0; i < ged.sizeData; i++) {
			Pair<boolean[], double[][]> mgs = makeProportionGeneExpresionState(ged.getCopyData(i), genesInc);
			gedBol[i] = mgs.getKey();
			Resources.mergeAddMatrix(probabilisticValues, mgs.getValue(), degrees);
		}
		Resources.divMatrix(probabilisticValues, ged.sizeData);
		booleanGeneExpressionData = new BooleanGeneExpressionData(gedBol);
		return new Pair<>(booleanGeneExpressionData, probabilisticValues);
	}

//	@Override
//	public int[][] frequencyTableGene(int nGene, BooleanGeneExpressionData ged) {
//		GeneInfered geneInfered = this.getGeneInferedById(nGene);
//		return frequencyTableGene(geneInfered, ged);
//	}

//	public int[][] frequencyTableGene(GeneInfered geneInfered, BooleanGeneExpressionData ged) {
//		int[][] table = new int[(1 << geneInfered.getPredictors().size())][2];
//		for (int i = 0; i < ged.sizeData; i++) {
//			int pos = possitonTable(geneInfered.getPredictors(), ged.getData(i));
//			table[pos][aplicatedFunction(geneInfered, ged.getData(i)).getKey() ? 1 : 0]++;
//		}
//		return table;
//	}

	@Override
	public Pair<int[][], boolean[]> dinamicGeneDetails(int nGene, BooleanGeneExpressionData ged) {
		GeneInfered geneInfered = this.getGeneInferedById(nGene);
		return dinamicGeneDetails(geneInfered, ged);
	}

	public Pair<int[][], boolean[]> dinamicGeneDetails(GeneInfered geneInfered, BooleanGeneExpressionData ged) {
//		DinamicDetails dd = new DinamicDetails();
		int[][] frequencyTable = new int[(1 << geneInfered.getPredictors().size())][2];
		boolean[] prediction = new boolean[ged.sizeData];
		for (int i = 0; i < ged.sizeData; i++) {
			int pos = possitonTable(geneInfered.getPredictors(), ged.getCopyData(i));
			prediction[i] = aplicatedFunction(geneInfered, ged.getCopyData(i)).getKey();
			frequencyTable[pos][prediction[i] ? 1 : 0]++;
		}
		return new Pair<>(frequencyTable, prediction);
	}

	@Override
	public long[] getMainFunctionByGene(int idGene) {
		GeneInfered g = getGeneInferedById(idGene);
		if (g == null)
//			return new long[] {};
			return null;
		return g.getBoolean_functions();
	}

	@Override
	public int[] getArrayPreditorsByGene(int idGene) {
		GeneInfered g = getGeneInferedById(idGene);
		if (g == null)
			return new int[] {};
		int[] p = new int[g.getPredictors().size()];
		int i = 0;
		for (Iterator<Integer> iterator = g.getPredictors().iterator(); iterator.hasNext(); i++) {
			p[i] = iterator.next();
		}
		return p;
	}

	private double proportionFunction(String proportion) {
		long sizeBitsFunction = 0;
		long sizeProportionFunction = 0;
		List<Integer> ng= this.getIntegerGens();
		for (Integer i: ng) {
			GeneInfered geneInfered = getGeneInferedById(i);
			if (geneInfered.getPredictors().size()>0) {
				sizeBitsFunction += (1L << geneInfered.getPredictors().size());
				sizeProportionFunction = (proportion.equals("ZEROS")) ? geneInfered.sizeZerosFunction()
						: ((proportion.equals("EQUALS")) ? geneInfered.sizeEqualsFunction() : 0);
			}
		}
		return ((double) sizeProportionFunction) / sizeBitsFunction;
	}

	public double proportionZeros() {
		return proportionFunction("ZEROS");
	}

	public double proportionEquals() {
		return proportionFunction("EQUALS");
	}
	
	public static Pair<int[], List<Integer>[]> pairClusterByPreditors(
			NavigableSet<Integer> pred,
			BooleanGeneExpressionData geneExpressionDataSource){
		@SuppressWarnings("unchecked")
		List<Integer>[] cpred=new List[pred.size()];
		int[] sizes=new int[pred.size()];
		int index=0;
		for (Integer i : pred) {
			cpred[index]= geneExpressionDataSource.getCluster(i);
//			if(clusters.get(i)==null)
			sizes[index]=cpred[index].size();
			index++;
		}
		return new Pair<>(sizes, cpred);
	}
	private static int[][] indices(int[] sizes){
		int s=IntStream.of(sizes).reduce(1, (a,b)->a*b);
		int[][] indices=new int[s][sizes.length];
		int d=1;
		for (int j = 0; j < sizes.length; j++) {
			d*=(j>0)?sizes[j-1]:1;
			for (int i = 0; i < indices.length; i++) {
				indices[i][j]= (i/d)% sizes[j];
			}
		}
		return indices;
	}
	private static List<NavigableSet<Integer>> mappingPred(List<Integer>[] cpred, int[] sizes) {
		int[][] indices=indices(sizes);
		List<NavigableSet<Integer>> preds=new ArrayList<>();
		for (int i = 0; i < indices.length; i++) {
			int[] ip=indices[i];
			preds.add(new TreeSet<>());
			for (int j = 0; j < ip.length; j++) {
				preds.get(i).add(cpred[j].get(indices[i][j]));
			}
		}
		return preds;	
	}
	public static List<NavigableSet<Integer>> mappingPred(Pair<int[], List<Integer>[]> pair) {
		return mappingPred(pair.getValue(), pair.getKey());
	}
	public static List<NavigableSet<Integer>> listPredClusters(NavigableSet<Integer> preditors, BooleanGeneExpressionData ged) {
		return mappingPred(pairClusterByPreditors(preditors, ged));
	}
	public static List<Details> buildDetailsFromCluster(List<Details> detailsList,
			BooleanGeneExpressionData geneExpressionDataSource) {
		if(geneExpressionDataSource==null)
			return detailsList;
		List<Details> detailsResult=new ArrayList<>();
		for (Details details : detailsList) {
			List<NavigableSet<Integer>> preds= listPredClusters(details.preditors, geneExpressionDataSource);
			for (NavigableSet<Integer> p : preds) {
				Details d=Details.cloneof(details);
				d.preditors=p;
				detailsResult.add(d);
			}
		}
		return detailsResult;
	}
}