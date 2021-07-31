package inference;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;

public class InferenceTopologyNetwork {
	final private GeneExpressionData booleanGeneExpressionData;
	private int currentGensInfered;
	private NavigableSet<Integer>[][] networkTopology;
	private ID_SEARCH_ALGORITM searchAlgoritm;
	private ID_EVALUATION_METRIC evaluationMetric;
	private ID_QUALIFYING_FUNCTION qualifyingFunction;
	private ConfigInference configInference;
	
	
	@SuppressWarnings("unchecked")
	public InferenceTopologyNetwork(GeneExpressionData booleanGeneExpressionData,
			String nameNetwork, ID_SEARCH_ALGORITM searchAlgoritm, ID_EVALUATION_METRIC evaluationMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction) {
		super();
		this.booleanGeneExpressionData = booleanGeneExpressionData;
		this.searchAlgoritm = searchAlgoritm;
		this.evaluationMetric = evaluationMetric;
		this.qualifyingFunction = qualifyingFunction;
		
		this.currentGensInfered = 0;
		this.currentGensInfered=0;
		this.networkTopology=new TreeSet[booleanGeneExpressionData.getSizeGens()][];
		this.configInference=new ConfigInference(booleanGeneExpressionData.getSizeData()-1);
	}


	public GeneExpressionData getGeneExpressionData() {
		return booleanGeneExpressionData;
	}


	public NavigableSet<Integer>[][] getNetworkTopology() {
		return networkTopology;
	}

	public void setNetworkTopology(NavigableSet<Integer>[][] networkTopology) {
		this.networkTopology = networkTopology;
	}

	public ID_SEARCH_ALGORITM getSearchAlgoritm() {
		return searchAlgoritm;
	}

	public void setSearchAlgoritm(ID_SEARCH_ALGORITM searchAlgoritm) {
		this.searchAlgoritm = searchAlgoritm;
	}

	public ID_EVALUATION_METRIC getEvaluationMetric() {
		return evaluationMetric;
	}

	public void setEvaluationMetric(ID_EVALUATION_METRIC evaluationMetric) {
		this.evaluationMetric = evaluationMetric;
	}

	public ID_QUALIFYING_FUNCTION getQualifyingFunction() {
		return qualifyingFunction;
	}

	public void setQualifyingFunction(ID_QUALIFYING_FUNCTION qualifyingFunction) {
		this.qualifyingFunction = qualifyingFunction;
	}

	public ConfigInference getConfigInference() {
		return configInference;
	}

	public void setConfigInference(ConfigInference configInference) {
		this.configInference = configInference;
	}

	public void inferedGene(int iGene) {
		InferenceGene ig=new InferenceGene(configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric), configInference.getSearchAlgoritmbyId(searchAlgoritm),null);
		this.networkTopology[iGene]= ig.listPreditorsGene(booleanGeneExpressionData, iGene);
		synchronized(this)  {
			if(++this.currentGensInfered==this.booleanGeneExpressionData.getSizeGens())
				this.show();
		}
	}

	private void show() {
		for (int i = 0; i < this.networkTopology.length; i++) {
			System.out.println(i+":\t"+Arrays.toString(this.networkTopology[i]));
		}
	}
}
