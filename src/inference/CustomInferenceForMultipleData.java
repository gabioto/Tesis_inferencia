package inference;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.DiscreteGeneExpressionData;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.qualifyingFunction.Details;
import inference.searchAlgoritm.IncrementalExhaustiveSearchWithSFS;

public class CustomInferenceForMultipleData {
	final private int[] customGenes;
	final private BooleanGeneExpressionData booleanGeneExpressionData;
	final private DiscreteGeneExpressionData discreteGeneExpressionData;
	private int currentGensInfered;
	final private PreInferedNetwork preInferedNetwork;
	final private String nameNetwork;
	final private ID_SEARCH_ALGORITM searchAlgoritm;
	final private ID_EVALUATION_METRIC evaluationMetric;
	final private ID_EVALUATION_METRIC additionalMetric;
	final private ID_QUALIFYING_FUNCTION qualifyingFunction;
	final private ConfigInference configInference;

	public CustomInferenceForMultipleData(int[] customGenes, BooleanGeneExpressionData booleanGeneExpressionData,
			DiscreteGeneExpressionData discreteGeneExpressionData, String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm, 
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction, ConfigInference configInference, boolean override)
			throws IOException {

		this.customGenes = customGenes;
		this.booleanGeneExpressionData = booleanGeneExpressionData;
		this.discreteGeneExpressionData = discreteGeneExpressionData;
		this.nameNetwork = nameNetwork;
		this.searchAlgoritm = searchAlgoritm;
		this.evaluationMetric = evaluationMetric;
		this.additionalMetric=additionalMetric;
		this.qualifyingFunction = qualifyingFunction;
		this.configInference = configInference;
		this.currentGensInfered = 0;
		if (NetworkInfered.existsFile(nameNetwork) && !override)
			this.preInferedNetwork = PreInferedNetwork.readFromFile(nameNetwork);
		else
			this.preInferedNetwork = new PreInferedNetwork(new ConcurrentHashMap<>(), this.evaluationMetric,
					this.qualifyingFunction, this.searchAlgoritm, this.booleanGeneExpressionData.getSizeData());
	}

	public void inferedGene(int iGene) throws IOException {
		inferedGenebyIndex(iGene, "");
	}

	public void inferedGenebyIndex(int iGene, String mode) throws IOException {
		int idGene = customGenes[iGene];
		Map<Integer, List<Details>> igs=null;
		if (mode.equals("BD")) {
			igs= ((IncrementalExhaustiveSearchWithSFS)configInference.getSearchAlgoritmbyId(searchAlgoritm))
					.searchPreditorsMultipleDetailsBD(
							booleanGeneExpressionData,
							discreteGeneExpressionData,
							idGene,
							configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
							configInference.getEvaluationMetricById(additionalMetric));
		}
		if (mode.equals("RE")) {
//			if(!booleanGeneExpressionData.clusterForRepeat.containsKey(idGene)) {
//				++this.currentGensInfered;
//				return;
//			}
			igs= ((IncrementalExhaustiveSearchWithSFS)configInference.getSearchAlgoritmbyId(searchAlgoritm))
			.searchPreditorsMultipleDetailReduce(
					booleanGeneExpressionData,
					discreteGeneExpressionData,
					idGene,
					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
					configInference.getEvaluationMetricById(additionalMetric));
		}
		igs.forEach((k,v)->{
				this.preInferedNetwork.getGensInfered().put(k, v);
		});
		synchronized (this.nameNetwork) {
			if (++this.currentGensInfered < this.customGenes.length) {
				return;
			}
		}
		long i = Thread.currentThread().getId();
		this.preInferedNetwork.writeInFile(nameNetwork);
//		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}

//	public Pair<ArrayList<Details>, LinkedList<Double>[]> inferenceGeneAllBD(int idGene) {
//		// GeneInfered gene = new GeneInfered();
//		Pair<ArrayList<Details>, LinkedList<Double>[]> listDetV = ((IncrementalExhaustiveSearchWithSFS) configInference
//				.getSearchAlgoritmbyId(searchAlgoritm)).searchPreditorsMultipleDetails(booleanGeneExpressionData,
//						discreteGeneExpressionData, idGene,
//						configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric));
//		ArrayList<Details> listDetails = listDetV.getKey();
//		return new Pair<>(listDetails, null);
//	}
}
