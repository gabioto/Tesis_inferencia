package inference;

import java.io.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import geneticNetwork.*;
import inference.evaluationMetric.EvaluationMetric;
import inference.evaluationMetric.MutualInformation;
import inference.qualifyingFunction.*;
import inference.searchAlgoritm.IncrementalExhaustiveSearchWithSFS;
import inference.searchAlgoritm.SearchAlgoritm;
import javafx.util.Pair;
import resources.Resources;

public class InferenceGene {
	final private QualifyingFunction qualifyingFunction;
	final private SearchAlgoritm searchAlgoritm;
	final private EvaluationMetric additionalMetric;

	public InferenceGene(QualifyingFunction qualifyingFunction, SearchAlgoritm searchAlgoritm,
			EvaluationMetric additionalMetric) {
		this.qualifyingFunction = qualifyingFunction;
		this.searchAlgoritm = searchAlgoritm;
		this.additionalMetric = additionalMetric;
	}

	public GeneInfered inferenceGene(GeneExpressionData geneExpressionData, int iGene) {
		// GeneInfered gene = new GeneInfered();
		List<Details> listDetails = searchAlgoritm.searchPreditorsDetails(geneExpressionData, iGene, qualifyingFunction,
				additionalMetric);
		int iRandomPred = (listDetails.size() > 1) ? Resources.random.nextInt(listDetails.size()) : 0;
		Details details = listDetails.get(iRandomPred);
//		// refazer os calculos os detalles
//		//qualifyingFunction.buildGroupingDetails(details);
//		return rebuildGene(details);
//		if(qualifyingFunction instanceof CanalizateGroupingLinear) {
		int[][] frequencyTable = AbstractNetwork.buildFrequencyTable(details.preditors, geneExpressionData, iGene);
		double ecm = ((MutualInformation) qualifyingFunction.getEvaluationMetric()).conditionalEntropy(frequencyTable);
		if (details.qualityValue < ecm)
			return null;
//		}
		return listDetails.get(iRandomPred).toGeneInfered();
	}

	public Pair<List<Details>, Deque<Double>[]> inferenceGeneAll(GeneExpressionData geneExpressionData, int idGene,
			boolean printSteps) {
		// GeneInfered gene = new GeneInfered();
		Pair<List<Details>, Deque<Double>[]> listDetV = searchAlgoritm.searchPreditorsDetails(geneExpressionData,
				idGene, qualifyingFunction, additionalMetric, printSteps);
		List<Details> listDetails = listDetV.getKey();
		Deque<Double>[] steps = null;
		if (printSteps) {
			steps = listDetV.getValue();// +details.toPreditorsString();
			steps[0].addFirst(Double.valueOf(listDetails.size()));
			steps[1].addFirst(Double.valueOf(listDetails.size()));
		}
		return new Pair<>(listDetails, steps);
	}

//	public Pair<ArrayList<Details>, LinkedList<Double>[]> 
//						inferenceGeneAll(BooleanGeneExpressionData geneExpressionData,
//										DiscreteGeneExpressionData gdd,
//										int idGene,
//										boolean printSteps) {
//		// GeneInfered gene = new GeneInfered();
//		Pair<ArrayList<Details>, LinkedList<Double>[]> listDetV = searchAlgoritm
//				.searchPreditorsDetails(geneExpressionData,gdd, idGene, qualifyingFunction, printSteps);
//		ArrayList<Details> listDetails = listDetV.getKey();
//		LinkedList<Double>[] steps = null;
//		if (printSteps) {
//			steps = listDetV.getValue();// +details.toPreditorsString();
//			steps[0].addFirst(Double.valueOf(listDetails.size()));
//			steps[1].addFirst(Double.valueOf(listDetails.size()));
//		}
//		return new Pair<>(listDetails, steps);
//	}
	public Pair<GeneInfered, Deque<Double>[]> inferenceGene(GeneExpressionData geneExpressionData, int idGene,
			boolean printSteps) {
		// GeneInfered gene = new GeneInfered();
		Pair<List<Details>, Deque<Double>[]> listDetV = this.inferenceGeneAll(geneExpressionData, idGene, printSteps);
		List<Details> listDetails = listDetV.getKey();
		int iRandomPred = (listDetails.size() > 1) ? Resources.random.nextInt(listDetails.size()) : 0;
		Details details = listDetails.get(iRandomPred);
		this.qualifyingFunction.buildGroupingDetails(details);
		return new Pair<>(details.toGeneInfered(), listDetV.getValue());
	}

	public GeneInfered[] inferencePreGene(GeneExpressionData geneExpressionData, int iGene) {
		return rebuildGene(
				searchAlgoritm.searchPreditorsDetails(geneExpressionData, iGene, qualifyingFunction, additionalMetric));
	}

	public List<Details> listDetailsPreditors(GeneExpressionData geneExpressionData, int iGene) {
		return searchAlgoritm.searchPreditorsDetails(geneExpressionData, iGene, this.qualifyingFunction,
				this.additionalMetric);
	}

	public void saveDetailsPreditors(GeneExpressionData geneExpressionData, int iGene, String sufFile)
			throws FileNotFoundException {
		List<Details> details = listDetailsPreditors(geneExpressionData, iGene);
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(iGene + "_" + sufFile), true));
		printWriter.println(details.size() + " empates");
		for (Details detail : details) {
			printWriter.append(detail.toPreditorsString());
		}
		printWriter.close();
	}

	public void saveDetailsDinamicGene(GeneticNetwork network, int iGene, String sufFile) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(iGene + "_" + sufFile), true));
		printWriter.println(network.stringDinamicGene(iGene));
		printWriter.close();
	}

	public NavigableSet<Integer>[] listPreditorsGene(GeneExpressionData geneExpressionData, int iGene) {
		List<Details> listDetails = searchAlgoritm.searchPreditorsDetails(geneExpressionData, iGene, qualifyingFunction,
				this.additionalMetric);
		@SuppressWarnings("unchecked")
		NavigableSet<Integer>[] list = new TreeSet[listDetails.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = listDetails.get(i).preditors;
		}
		return list;
	}

	private GeneInfered[] rebuildGene(List<Details> details) {
		GeneInfered[] geneInfereds = new GeneInfered[details.size()];
		for (int iDetails = 0; iDetails < geneInfereds.length; iDetails++) {
			geneInfereds[iDetails] = details.get(iDetails).toGeneInfered();
		}
		return geneInfereds;
	}

	public GeneInfered reInferenceGene(GeneExpressionData geneExpressionData, int idGene,
			NavigableSet<Integer> predictors) {
		Details details = qualifyingFunction.quantifyDetails(predictors, geneExpressionData, idGene);
		qualifyingFunction.buildGroupingDetails(details);
		return details.toGeneInfered();
	}

	public GeneInfered reInferenceGene(GeneExpressionData geneExpressionData, int iGene, int fixedDegree) {
		List<Details> listDetails = this.reInferenceGeneAll(geneExpressionData, iGene, fixedDegree);
		int iRandomPred = (listDetails.size() > 1) ? Resources.random.nextInt(listDetails.size()) : 0;
		qualifyingFunction.buildGroupingDetails(listDetails.get(iRandomPred));
		return listDetails.get(iRandomPred).toGeneInfered();
	}

	public List<Details> reInferenceGeneAll(GeneExpressionData geneExpressionData, int iGene, int fixedDegree) {
		return searchAlgoritm.searchPreditorsDetails(geneExpressionData, iGene, qualifyingFunction, fixedDegree);
	}

	public List<Details> reInferenceGeneAll(BooleanGeneExpressionData geneExpressionData,
			DiscreteGeneExpressionData geneExpressionDataAdd, int idGene, int fixedDegree) {
		return ((IncrementalExhaustiveSearchWithSFS) searchAlgoritm).searchPreditorsMultipleDetailRE(geneExpressionData,
				geneExpressionDataAdd, idGene, qualifyingFunction, additionalMetric, fixedDegree);
	}

	public List<Details> reTiesGeneFromDiscrete(DiscreteGeneExpressionData gdd, int idGene, List<Details> detailsById) {
		List<Details> d = new ArrayList<Details>();
		double maxIM = 0;
		for (Details details : detailsById) {
			Details dt = this.qualifyingFunction.quantifyDetails(details.preditors, gdd, idGene);
			if (Resources.compareDouble(dt.qualityValue, maxIM) >= 0) {
				if (Resources.compareDouble(dt.qualityValue, maxIM) > 0) {
					d.clear();
					maxIM = dt.qualityValue;
				}
				d.add(details);
			}
		}
		return d;
	}

	public List<Details> reTiesGeneFromDiscrete(BooleanGeneExpressionData bged, DiscreteGeneExpressionData gdd,
			int idGene, List<Details> detailsById) {
		List<Details> d = new ArrayList<Details>();
		double maxIM = 0;
		for (Details detailsId : detailsById) {
			List<NavigableSet<Integer>> listPreditors = PreInferedNetwork.listPredClusters(detailsId.preditors, bged);
			for (NavigableSet<Integer> preditors : listPreditors) {
				Details dt = this.qualifyingFunction.quantifyDetails(preditors, gdd, idGene);
				if (Resources.compareDouble(dt.qualityValue, maxIM) >= 0) {
					if (Resources.compareDouble(dt.qualityValue, maxIM) > 0) {
						d.clear();
						maxIM = dt.qualityValue;
					}
					d.add(dt);
				}
			}
		}
		return d;
	}

}
//	private GeneInfered rebuildGene(Details details) {
//		// gene.setPredictors(details.preditors);
//		// gene.setCritery_function_values(details.qualityValue);
//		int nlinhas = 1 << details.preditors.size();
//		int indexTable=Resources.random.nextInt(details.frequencyTable.length);
//		int[][] frequencyTable = details.frequencyTable[indexTable];
//		long[] booleanTable = new long[nlinhas / 64 + 1];
//		long[] stateTable = new long[nlinhas / 64 + 1];
//		long[] stateZ = new long[nlinhas / 64 + 1];
//		for (int i = 0; i < booleanTable.length; i++) {
//			stateTable[i] = -1L;
//			stateZ[i] = -1L;
//		}
//		// gene.setGlobal_boolean_values(resources.sumFrequency(frequencyTable));
//		// if(frequencyTable.length==nlinhas) {// nao tem agrupamento
//		if(details.functions[indexTable].length==1)
//			booleanTable=details.functions[indexTable][0];
//		else
//		if (details.groupsTable == null) {
//			for (int i = 0; i < frequencyTable.length; i++) {
//				if ((frequencyTable[i][1] > frequencyTable[i][0]))
//					booleanTable[i / 64] = booleanTable[i / 64] | ((long) 1 << i);
//				else if (frequencyTable[i][1] == frequencyTable[i][0]) {
//					stateTable[i / 64] = stateTable[i / 64] ^ ((long) 1 << i);
//					if (frequencyTable[i][1] == 0)
//						stateZ[i / 64] = stateZ[i / 64] ^ ((long) 1 << i);
//				}
//			}
//		} else {
//			int[][] groupTable = details.groupsTable[indexTable];
//			for (int i = 0; i < frequencyTable.length; i++) {
//				if ((frequencyTable[i][1] > frequencyTable[i][0])) {
//					for (int j = 0; j < groupTable[i].length; j++) {
//						int index = groupTable[i][j];
//						booleanTable[index / 64] = booleanTable[index / 64] | ((long) 1 << index);
//					}
//				} else if (frequencyTable[i][1] == frequencyTable[i][0]) {
//					for (int j = 0; j < groupTable[i].length; j++) {
//						int index = groupTable[i][j];
//						stateTable[index / 64] = stateTable[index / 64] ^ ((long) 1 << index);
//						if (frequencyTable[i][1] == 0)
//							stateZ[index / 64] = stateZ[index / 64] ^ ((long) 1 << index);
//					}
//				}
//			}
//		}
//		return new GeneInfered(details.preditors, booleanTable, stateTable, stateZ, details.qualityValue,
//				Resources.sumFrequency(frequencyTable));
//		// gene.setBoolean_functions(booleanTable);
//		// gene.setState_functions(stateTable);
//		// gene.setState_notObserved(stateZ);
//	}
//}
