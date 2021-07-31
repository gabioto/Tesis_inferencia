package inference;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import geneticNetwork.*;
import inference.ConfigInference.*;
import javafx.util.Pair;

public class InferenceNetwork {
	public enum TYPE_STEAPS {
		HY, HYT, ALL, NONE;
	}

	final private GeneExpressionData geneExpressionData;
	private int currentGensInfered;
	final private NetworkInfered networkInfered;
	final private String nameNetwork;
	final private ID_SEARCH_ALGORITM searchAlgoritm;
	final private ID_EVALUATION_METRIC evaluationMetric;
	final private ID_EVALUATION_METRIC additionalMetric;
	final private ID_QUALIFYING_FUNCTION qualifyingFunction;
	final private ConfigInference configInference;
	final private int nGens;
	final StringBuffer bufferSteps;
	final TYPE_STEAPS typeSteaps;

	public InferenceNetwork(GeneExpressionData geneExpressionData, String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm, ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction) throws IOException {
		this(geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,additionalMetric,
				qualifyingFunction,
				new ConfigInference(geneExpressionData.getSizeData()-1));
//		this.geneExpressionData = geneExpressionData;
//		this.nameNetwork = nameNetwork;
//		this.searchAlgoritm = searchAlgoritm;
//		this.evaluationMetric = evaluationMetric;
//		this.qualifyingFunction = qualifyingFunction;
//
//		this.currentGensInfered = 0;
//		this.networkInfered = new NetworkInfered2(this.evaluationMetric, this.qualifyingFunction, this.searchAlgoritm,
//				this.geneExpressionData.getSizeData(), new GeneInfered[geneExpressionData.getSizeGens()]);
//		this.configInference = new ConfigInference();
//		this.nGens = geneExpressionData.getSizeGens();
	}

	public InferenceNetwork(GeneExpressionData geneExpressionData, String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm, ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction, ConfigInference configInference) throws IOException {
		this(geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,additionalMetric, qualifyingFunction, configInference,
				TYPE_STEAPS.NONE);
	}

	public InferenceNetwork(GeneExpressionData geneExpressionData, String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm, ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction, ConfigInference configInference, TYPE_STEAPS typeSteaps)
			throws IOException {
		this.geneExpressionData = geneExpressionData;
		this.nameNetwork = nameNetwork;
		this.searchAlgoritm = searchAlgoritm;
		this.evaluationMetric = evaluationMetric;
		this.additionalMetric=additionalMetric;
		this.qualifyingFunction = qualifyingFunction;
		this.configInference = configInference;
		this.currentGensInfered = 0;
		this.nGens = geneExpressionData.getValidGens().size();
		if (NetworkInfered.existsFile(nameNetwork))
			this.networkInfered = NetworkInfered.readFromFile(nameNetwork);
		else
			this.networkInfered = new NetworkInfered(this.evaluationMetric,
					this.qualifyingFunction,
					this.searchAlgoritm,
					this.geneExpressionData.getSizeData(),
					new GeneInfered[this.nGens]);
		this.bufferSteps = new StringBuffer();
		this.typeSteaps = typeSteaps;
	}

	public GeneExpressionData getGeneExpressionData() {
		return geneExpressionData;
	}

	public NetworkInfered getNetworkInfered() {
		return networkInfered;
	}

	public String getNameNetwork() {
		return nameNetwork;
	}

	public ID_SEARCH_ALGORITM getSearchAlgoritm() {
		return searchAlgoritm;
	}

	public ID_EVALUATION_METRIC getEvaluationMetric() {
		return evaluationMetric;
	}

	public ID_QUALIFYING_FUNCTION getQualifyingFunction() {
		return qualifyingFunction;
	}

	public ConfigInference getConfigInference() {
		return configInference;
	}

	public void inferedGene(int iGene) throws IOException {
		inferedGene(iGene, "");
	}

	public void inferedGene(int iGene, String otherInformation) throws IOException {
		InferenceGene ig = new InferenceGene(
				configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
				configInference.getSearchAlgoritmbyId(searchAlgoritm),
				configInference.getEvaluationMetricById(additionalMetric));
		int idGene = geneExpressionData.getValidGens().get(iGene);
		Pair<GeneInfered, Deque<Double>[]> infGene = ig.inferenceGene(geneExpressionData, idGene,
				(typeSteaps != TYPE_STEAPS.NONE));
		this.networkInfered.getGens()[iGene] = infGene.getKey();
		synchronized (this.nameNetwork) {
		//	System.out.println("Inf Gene:" +idGene + " <- "+infGene.getKey().getPredictors());
			if (typeSteaps != TYPE_STEAPS.NONE)
				addBuffer(infGene.getValue(), otherInformation +" "+ idGene);
			if (++this.currentGensInfered < this.nGens) {
				return;
			}
		}
		long i = Thread.currentThread().getId();
		this.networkInfered.writeInFile(nameNetwork);
		if (typeSteaps != TYPE_STEAPS.NONE) {
				BufferedWriter out = new BufferedWriter(new FileWriter(nameNetwork+".csv"));
				out.write(bufferSteps.toString());
				out.flush();
				out.close();
		}
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}

	private void addBuffer(Deque<Double>[] bufferValue, String otherInformation) {
		String[] prefValues = nameNetwork.substring(nameNetwork.lastIndexOf('/')).split("__");
		StringBuilder prefSteps = new StringBuilder();
		switch (typeSteaps) {
		case NONE:
			return;
		case HY:
		case HYT:
		case ALL:
			prefSteps.append(prefValues[1]).append(" ").append(prefValues[5]).append(" ").append(prefValues[6])
					.append(" ").append(prefValues[8])
					//.append(" ").append(prefValues[9])
					.append(" ").append(otherInformation);
			break;
		default:
			return;
		}
		if (typeSteaps == TYPE_STEAPS.HY || typeSteaps == TYPE_STEAPS.HYT)
			bufferSteps.append(prefSteps).append(" ").append(typeSteaps).append(" ").append(
					bufferValue[typeSteaps.ordinal()].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n");
		else// typeSteaps==TYPE_STEAPS.ALL
			bufferSteps.append(prefSteps).append(" ").append("EM").append(" ")
					.append(bufferValue[0].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n").append(prefSteps).append(" ").append("HYT").append(" ")
					.append(bufferValue[1].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n");

	}

	public void reInferedGene(int iGene, boolean printSteps) throws FileNotFoundException {
		InferenceGene ig = new InferenceGene(
				configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
				configInference.getSearchAlgoritmbyId(searchAlgoritm),
				configInference.getEvaluationMetricById(additionalMetric));
		int idGene = geneExpressionData.getValidGens().get(iGene);
		this.networkInfered.getGens()[iGene] = ig.reInferenceGene(geneExpressionData, idGene,
				this.networkInfered.getGens()[iGene].getPredictors());
		synchronized (this.nameNetwork) {
			// System.out.print(i + " LOOK(" + this.currentGensInfered + ") ");
			if (++this.currentGensInfered < this.nGens) {
				// System.out.println("UNLOOK(" + this.currentGensInfered + "):" + iGene + "\t"
				// + this.nameNetwork);
				return;
			}
			// System.out.println("\tUNLOOK(" + this.currentGensInfered + "):" + iGene +
			// "\t" + this.nameNetwork);
		}
		long i = Thread.currentThread().getId();
		this.networkInfered.writeInFile(new File(nameNetwork));
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}
	public void reInferedGene(int iGene, int fixedDegree) throws FileNotFoundException {
		if(this.networkInfered.getGeneInferedByIndex(iGene).getPredictors().size()!=fixedDegree) {
			if(fixedDegree!=0) {
			InferenceGene ig = new InferenceGene(
					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
					configInference.getSearchAlgoritmbyId(searchAlgoritm),
					configInference.getEvaluationMetricById(additionalMetric));
			this.networkInfered.getGens()[iGene] = ig.reInferenceGene(geneExpressionData, iGene,
					fixedDegree);
			}
			else
				this.networkInfered.getGens()[iGene]=this.networkInfered.getGens()[iGene].cloneZeroGeneInfered();
		}
		synchronized (this.nameNetwork) {
			// System.out.print(i + " LOOK(" + this.currentGensInfered + ") ");
			if (++this.currentGensInfered < this.nGens) {
				// System.out.println("UNLOOK(" + this.currentGensInfered + "):" + iGene + "\t"
				// + this.nameNetwork);
				return;
			}
			// System.out.println("\tUNLOOK(" + this.currentGensInfered + "):" + iGene +
			// "\t" + this.nameNetwork);
		}
		long i = Thread.currentThread().getId();
		this.networkInfered.writeInFile(new File(nameNetwork));
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}
	public void reInferedGeneMenor(int iGene, int fixedDegree) throws FileNotFoundException {
		if(this.networkInfered.getGeneInferedByIndex(iGene).getPredictors().size()>fixedDegree) {
			if(fixedDegree!=0) {
			InferenceGene ig = new InferenceGene(
					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
					configInference.getSearchAlgoritmbyId(searchAlgoritm),
					configInference.getEvaluationMetricById(additionalMetric));
			this.networkInfered.getGens()[iGene] = ig.reInferenceGene(geneExpressionData, iGene,
					fixedDegree);
			}
			else
				this.networkInfered.getGens()[iGene]=this.networkInfered.getGens()[iGene].cloneZeroGeneInfered();
		}
		synchronized (this.nameNetwork) {
			// System.out.print(i + " LOOK(" + this.currentGensInfered + ") ");
			if (++this.currentGensInfered < this.nGens) {
				// System.out.println("UNLOOK(" + this.currentGensInfered + "):" + iGene + "\t"
				// + this.nameNetwork);
				return;
			}
			// System.out.println("\tUNLOOK(" + this.currentGensInfered + "):" + iGene +
			// "\t" + this.nameNetwork);
		}
		long i = Thread.currentThread().getId();
		this.networkInfered.writeInFile(new File(nameNetwork));
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " m " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " m " + nameNetwork);
	}
//	public Object inferedGene(int iGene,boolean printSteps){
//		InferenceGene ig = new InferenceGene(
//				configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
//				configInference.getSearchAlgoritmbyId(searchAlgoritm));
//		Pair<GeneInfered, Object> igv=ig.inferenceGene(geneExpressionData, iGene,printSteps);
//		this.networkInfered.getGens()[iGene] = igv.getKey();
//		return igv.getValue();
//	}

	public NetworkInfered inferedNetwork() throws IOException {
		this.currentGensInfered = 0;
		// ArrayList<Integer> validsGens = geneExpressionData.getValidGens();
		for (int i = 0; i < nGens; i++) {
			this.inferedGene(i);
		}
		return this.networkInfered;
	}

	public Pair<NetworkInfered, StringBuffer> inferedNetwork(String prefSteps) throws IOException {
		this.currentGensInfered = 0;
		List<Integer> validsGens = geneExpressionData.getValidGens();
//		Object[] stepsString = new Object[nGens];
		for (int i = 0; i < nGens; i++) {
			this.inferedGene(validsGens.get(i));
//			stepsString[i]+=this.networkInfered.detailsGeneExpression(i, geneExpressionData);
		}
		return new Pair<>(this.networkInfered, this.bufferSteps);
	}

//	public PreInferedNetwork inferedPreNetwork() {
//		PreInferedNetwork preInferedNetwork = new PreInferedNetwork(evaluationMetric, qualifyingFunction,
//				searchAlgoritm, geneExpressionData.getSizeData());
//		ArrayList<Details>[] details = preInferedNetwork.getGensInfered();
//		for (int iGene = 0; iGene < details.length; iGene++) {
//			InferenceGene inferenceGene = new InferenceGene(
//					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
//					configInference.getSearchAlgoritmbyId(searchAlgoritm));
//			details[iGene] = inferenceGene.listDetailsPreditors(geneExpressionData, iGene);
//		}
//		return preInferedNetwork;
//	}
//
//	public static PreInferedNetwork inferedPreNetwork(NetworkInfered networkBase,
//			QualifyingFunction qualifyingFunction, BooleanGeneExpressionData geneExpressionData) {
//		PreInferedNetwork preInferedNetwork = new PreInferedNetwork(networkBase.getSizeGenes(),
//				qualifyingFunction.getEvaluationMetric().getId(), qualifyingFunction.getId(), null,
//				geneExpressionData.getSizeData());
//		ArrayList<Details>[] details = preInferedNetwork.getGensInfered();
//		for (int iGene = 0; iGene < details.length; iGene++) {
//			details[iGene] =new ArrayList<>();
//			details[iGene].add(qualifyingFunction.quantifyDetails(
//					networkBase.getGeneInferedById(iGene).getPredictors(), geneExpressionData, iGene));
//			qualifyingFunction.buildGroupingDetails(details[iGene].get(0));
//
//		} // inferenceGene.listDetailsPreditors(geneExpressionData, iGene);}
//		return preInferedNetwork;
//	}


}
