package inference;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.DiscreteGeneExpressionData;
//import geneticNetwork.DiscreteGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.*;
import inference.InferenceNetwork.TYPE_STEAPS;
import inference.qualifyingFunction.Details;
import javafx.util.Pair;

public class CustomGenesInference {
	final private int[] customGenes;
	final private GeneExpressionData geneExpressionData;
	final private GeneExpressionData geneExpressionDataAdd;
	private int currentGensInfered;
	final private PreInferedNetwork preInferedNetwork;
	final private PreInferedNetwork tempPreInferedNetwork;
	final private String nameNetwork;
	final private ID_SEARCH_ALGORITM searchAlgoritm;
	final private ID_EVALUATION_METRIC evaluationMetric;
	final private ID_EVALUATION_METRIC additionalMetric;
	final private ID_QUALIFYING_FUNCTION qualifyingFunction;
	final private ConfigInference configInference;
	final private int nGens;
	final StringBuffer bufferSteps;
	final TYPE_STEAPS typeSteaps;
	
	public CustomGenesInference(int[] customGenes,
			GeneExpressionData geneExpressionData,
			GeneExpressionData geneExpressionDataAdd,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm,
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ConfigInference configInference,
			TYPE_STEAPS typeSteaps,
			boolean override) throws NumberFormatException, IOException {
		this.customGenes = customGenes;
		this.geneExpressionData = geneExpressionData;
		this.geneExpressionDataAdd=geneExpressionDataAdd;
		this.nameNetwork = nameNetwork;
		this.searchAlgoritm = searchAlgoritm;
		this.evaluationMetric = evaluationMetric;
		this.additionalMetric=additionalMetric;
		this.qualifyingFunction = qualifyingFunction;
		this.configInference = configInference;
		this.currentGensInfered = 0;
		this.nGens = customGenes.length;
		if (NetworkInfered.existsFile(nameNetwork) && !override) {
			this.preInferedNetwork = PreInferedNetwork.readFromFile(nameNetwork);
//			if(customGenes.length>this.preInferedNetwork.getSizeGenes())
//				this.preInferedNetwork.completeClusterPreditors((BooleanGeneExpressionData)geneExpressionData);
		}
		else
			this.preInferedNetwork = new PreInferedNetwork(
					this.evaluationMetric,
					this.qualifyingFunction,
					this.searchAlgoritm,
					this.geneExpressionData.getSizeData());
		this.bufferSteps = new StringBuffer();
		this.typeSteaps = typeSteaps;
		this.tempPreInferedNetwork= new PreInferedNetwork(
				this.evaluationMetric,
				this.qualifyingFunction,
				this.searchAlgoritm,
				this.geneExpressionData.getSizeData());
	}
	public CustomGenesInference(int[] customGenes,
			GeneExpressionData geneExpressionData,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm,
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ConfigInference configInference,
			TYPE_STEAPS typeSteaps,
			boolean override) throws IOException {
		
		this(customGenes
				,geneExpressionData
				,geneExpressionData
				,nameNetwork
				,searchAlgoritm
				,evaluationMetric
				,additionalMetric
				,qualifyingFunction
				,configInference
				,typeSteaps
				,override);
	}

	public CustomGenesInference(GeneExpressionData geneExpressionData,
			String nameNetwork,
			PreInferedNetwork preInferedNetwork,
			ConfigInference configInference,
			ID_EVALUATION_METRIC additionalMetric,
			TYPE_STEAPS typeSteaps)
			throws NumberFormatException, IOException {

		this.geneExpressionData = geneExpressionData;
		this.geneExpressionDataAdd=geneExpressionData;
		this.nameNetwork = nameNetwork;
		this.preInferedNetwork = PreInferedNetwork.maskEvolutionNetwork(preInferedNetwork);
		this.customGenes = preInferedNetwork.allGens().stream().mapToInt(Integer::intValue).toArray();
		this.searchAlgoritm = this.preInferedNetwork.getSearchAlgoritm();
		this.evaluationMetric = this.preInferedNetwork.getEvaluationMetric();
		this.additionalMetric=additionalMetric;
		this.qualifyingFunction = this.preInferedNetwork.getQualifyingFunction();
		this.configInference = configInference;
		this.currentGensInfered = 0;
		this.nGens = customGenes.length;
		this.bufferSteps = new StringBuffer();
		this.typeSteaps = typeSteaps;
		this.tempPreInferedNetwork= new PreInferedNetwork(
				this.evaluationMetric,
				this.qualifyingFunction,
				this.searchAlgoritm,
				this.geneExpressionData.getSizeData());
	}

	public CustomGenesInference(String nameNetwork,
			PreInferedNetwork preInferedNetwork)
			throws NumberFormatException, IOException {

		this.geneExpressionData = null;
		this.geneExpressionDataAdd=geneExpressionData;
		this.nameNetwork = nameNetwork;
		this.preInferedNetwork = preInferedNetwork;
		this.customGenes = this.preInferedNetwork.customGens().stream().mapToInt(Integer::intValue).toArray();
		this.searchAlgoritm = this.preInferedNetwork.getSearchAlgoritm();
		this.evaluationMetric = this.preInferedNetwork.getEvaluationMetric();
		this.qualifyingFunction = this.preInferedNetwork.getQualifyingFunction();
		this.additionalMetric=null;
		this.configInference = new ConfigInference(-1);
		this.currentGensInfered = 0;
		this.nGens = customGenes.length;
		this.bufferSteps = new StringBuffer();
		this.typeSteaps = TYPE_STEAPS.NONE;
		this.tempPreInferedNetwork= new PreInferedNetwork(
				this.evaluationMetric,
				this.qualifyingFunction,
				this.searchAlgoritm,
				this.geneExpressionData.getSizeData());
	}

	public CustomGenesInference(int[] customGenes,
			GeneExpressionData geneExpressionData,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm,
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction) throws IOException {
		this(customGenes, geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,
				additionalMetric, qualifyingFunction,
				new ConfigInference(geneExpressionData.getSizeData()-1));
	}

	public CustomGenesInference(int[] customGenes,
			GeneExpressionData geneExpressionData,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm,
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			boolean override) throws IOException {
		this(customGenes, geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,
				additionalMetric,qualifyingFunction,
				new ConfigInference(geneExpressionData.getSizeData()-1), TYPE_STEAPS.ALL, override);
	}

	public CustomGenesInference(int[] customGenes,
			GeneExpressionData geneExpressionData,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm, 
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ConfigInference configInference) throws IOException {
		this(customGenes, geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,
				additionalMetric, qualifyingFunction,
				configInference, TYPE_STEAPS.NONE, false);
	}

	public CustomGenesInference(int[] customGenes, 
			GeneExpressionData geneExpressionData,
			String nameNetwork,
			ID_SEARCH_ALGORITM searchAlgoritm,
			ID_EVALUATION_METRIC evaluationMetric,
			ID_EVALUATION_METRIC additionalMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ConfigInference configInference,
			TYPE_STEAPS typeSteaps)
			throws IOException {
		this(customGenes, geneExpressionData, nameNetwork, searchAlgoritm, evaluationMetric,
				additionalMetric, qualifyingFunction,
				configInference, typeSteaps, false);
	}
	

	public GeneExpressionData getGeneExpressionData() {
		return geneExpressionData;
	}

	public PreInferedNetwork getPreInferedNetwork() {
		return preInferedNetwork;
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
		inferedGenebyIndex(iGene, "");
	}

	public void inferedGenebyIndex(int iGene, String otherInformation) throws IOException {
		InferenceGene ig = new InferenceGene(
				configInference.getQualifyingFunctionById(qualifyingFunction,evaluationMetric),
				configInference.getSearchAlgoritmbyId(searchAlgoritm),
				configInference.getEvaluationMetricById(additionalMetric));
		int idGene = customGenes[iGene];
		Pair<List<Details>, Deque<Double>[]> infGene = ig.inferenceGeneAll(geneExpressionData, idGene,
				(typeSteaps != TYPE_STEAPS.NONE));
		this.preInferedNetwork.getGensInfered().put(idGene, infGene.getKey());
		synchronized (this.nameNetwork) {
			// System.out.println("Inf Gene:" +idGene + " <-
			// "+infGene.getKey().getPredictors());
			if (typeSteaps != TYPE_STEAPS.NONE)
				addBuffer(infGene.getValue(), otherInformation + " " + idGene);
			if (++this.currentGensInfered < this.nGens) {
				return;
			}
		}
		long i = Thread.currentThread().getId();
		this.preInferedNetwork.writeInFile(nameNetwork);
		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (typeSteaps != TYPE_STEAPS.NONE) {
			BufferedWriter out = new BufferedWriter(new FileWriter(nameNetwork + ".csv"));
			out.write(bufferSteps.toString());
			out.flush();
			out.close();
		}
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}

	public void inferedEvGenebyIndex(int iGene, String otherInformation) throws IOException {
		InferenceGene ig = new InferenceGene(
				configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
				configInference.getSearchAlgoritmbyId(searchAlgoritm),
				configInference.getEvaluationMetricById(additionalMetric));
		int idGene = customGenes[iGene];
		if (this.preInferedNetwork.getGensInfered().containsKey(idGene)) {
			synchronized (this.nameNetwork) {
				if (++this.currentGensInfered < this.nGens) {
					return;
				}
			}
		} else {
			Pair<List<Details>, Deque<Double>[]> infGene = ig.inferenceGeneAll(geneExpressionData, idGene,
					(typeSteaps != TYPE_STEAPS.NONE));
			this.preInferedNetwork.addGenInfered(idGene, infGene.getKey());
			synchronized (this.nameNetwork) {
				// System.out.println("Inf Gene:" +idGene + " <-
				// "+infGene.getKey().getPredictors());
				if (typeSteaps != TYPE_STEAPS.NONE)
					addBuffer(infGene.getValue(), otherInformation + " " + idGene);
				if (++this.currentGensInfered < this.nGens) {
					return;
				}
			}
		}
		long i = Thread.currentThread().getId();
		this.preInferedNetwork.writeInFile(nameNetwork);
		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (typeSteaps != TYPE_STEAPS.NONE) {
			BufferedWriter out = new BufferedWriter(new FileWriter(nameNetwork + ".csv"));
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
		StringBuilder prefSteps = new StringBuilder(otherInformation);
		if (typeSteaps == TYPE_STEAPS.HY || typeSteaps == TYPE_STEAPS.HYT)
			bufferSteps.append(prefSteps).append(" ").append(typeSteaps).append(" ").append(
					bufferValue[typeSteaps.ordinal()].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n");
		else// typeSteaps==TYPE_STEAPS.ALL
			bufferSteps.append(prefSteps).append(" ").append("HY").append(" ")
					.append(bufferValue[0].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n").append(prefSteps).append(" ").append("HYt").append(" ")
					.append(bufferValue[1].stream().map(o -> o.toString()).collect(Collectors.joining(" ")))
					.append("\n");

	}

//	public void reInferedGene(int iGene, boolean printSteps) throws FileNotFoundException {
//		InferenceGene ig = new InferenceGene(
//				configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
//				configInference.getSearchAlgoritmbyId(searchAlgoritm));
//		int idGene = customGenes[iGene];
//		this.preInferedNetwork.getGensInfered()[iGene] = 
//				ig.reInferenceGene(geneExpressionData, idGene,
//				this.preInferedNetwork.getGensInfered()[iGene].getPredictors());
//		synchronized (this.nameNetwork) {
//			// System.out.print(i + " LOOK(" + this.currentGensInfered + ") ");
//			if (++this.currentGensInfered < this.nGens) {
//				// System.out.println("UNLOOK(" + this.currentGensInfered + "):" + iGene + "\t"
//				// + this.nameNetwork);
//				return;
//			}
//			// System.out.println("\tUNLOOK(" + this.currentGensInfered + "):" + iGene +
//			// "\t" + this.nameNetwork);
//		}
//		long i = Thread.currentThread().getId();
//		this.networkInfered.writeInFile(new File(nameNetwork));
//		if (nameNetwork.lastIndexOf('/') != -1)
//			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
//		else
//			System.out.println(i + " " + nameNetwork);
//	}
	public void reInferedGeneById(int idGene, int fixedDegree) throws FileNotFoundException {
		if (this.preInferedNetwork.getGensInfered().get(idGene).get(0).preditors.size() != fixedDegree) {
			System.out.println(qualifyingFunction +":"+idGene + "<-"+fixedDegree 
					+ " "+this.preInferedNetwork.getGensInfered().get(idGene).get(0).preditors.size());
			if (fixedDegree != 0) {
				InferenceGene ig = new InferenceGene(
						configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
						configInference.getSearchAlgoritmbyId(searchAlgoritm),
						configInference.getEvaluationMetricById(additionalMetric));
				this.preInferedNetwork.replazeInference(idGene,
						ig.reInferenceGeneAll(
								(BooleanGeneExpressionData)geneExpressionData,
								(DiscreteGeneExpressionData)geneExpressionDataAdd, idGene, fixedDegree));
			}
			System.out.println(qualifyingFunction +":"+idGene+"->"+fixedDegree 
					+ " "+this.preInferedNetwork.getGensInfered().get(idGene).get(0).preditors.size());
//			else
//				this.preInferedNetwork.getGensInfered()[iGene]=this.preInferedNetwork.getGens()[iGene].cloneZeroGeneInfered();
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
		this.preInferedNetwork.writeInFile(nameNetwork);
		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " " + nameNetwork);
	}

	public void reInferedGeneByIndex(int iGene, int fixedDegree) throws FileNotFoundException {
		reInferedGeneById(customGenes[iGene], fixedDegree);
	}

	public void reInferedGeneMenorById(int idGene, int fixedDegree) throws FileNotFoundException {
		if (this.preInferedNetwork.getGensInfered().get(idGene).get(0).preditors.size() > fixedDegree) {
//			System.out.println(qualifyingFunction +":"+idGene + " "+fixedDegree);
//			if (fixedDegree != 0) {
//				InferenceGene ig = new InferenceGene(
//						configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
//						configInference.getSearchAlgoritmbyId(searchAlgoritm),
//						configInference.getEvaluationMetricById(additionalMetric));
//				this.preInferedNetwork.replazeInference(idGene,
//						ig.reInferenceGeneAll(geneExpressionData, idGene, fixedDegree));
//			} else
//				this.preInferedNetwork.toZeroInference(idGene);// this.preInferedNetwork.getGensInfered()[iGene].cloneZeroGeneInfered();
//			System.out.println(qualifyingFunction +":"+idGene);
			reInferedGeneById(idGene,fixedDegree);
			return;
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
		this.preInferedNetwork.writeInFile(nameNetwork);
//		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " m " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " m " + nameNetwork);
	}

	public void reTiesFromDiscreteById(int idGene) throws FileNotFoundException {
		BooleanGeneExpressionData bged=(BooleanGeneExpressionData)geneExpressionData;
		DiscreteGeneExpressionData gdd=(DiscreteGeneExpressionData)geneExpressionDataAdd;
//		System.out.println("in:"+idGene);		
			InferenceGene ig = new InferenceGene(
					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
					configInference.getSearchAlgoritmbyId(searchAlgoritm),
					configInference.getEvaluationMetricById(additionalMetric));
			this.tempPreInferedNetwork.addGenInfered(idGene
							,ig.reTiesGeneFromDiscrete(//bged,
									gdd,
									idGene,
									Details.cloneListof(this.preInferedNetwork.gensInfered.get(idGene), idGene)
									));
			List<Integer> lc=bged.getCluster(idGene);
			for (int i = 1; i < lc.size(); i++) {
				this.tempPreInferedNetwork.addGenInfered(lc.get(i),
						ig.reTiesGeneFromDiscrete(//bged,
								gdd,
								lc.get(i),
								Details.cloneListof(this.preInferedNetwork.gensInfered.get(idGene), lc.get(i))));
			}
//		System.out.println("end:"+idGene);
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
		this.tempPreInferedNetwork.writeInFile(nameNetwork.replaceAll(".ylu","_e"));
//		this.preInferedNetwork.writeFormatGraph(nameNetwork);
		if (nameNetwork.lastIndexOf('/') != -1)
			System.out.println(i + " m " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
		else
			System.out.println(i + " m " + nameNetwork);
	}

//	public void reTiesFromDiscreteByIdQf(int idGene, DiscreteGeneExpressionData gdd) throws FileNotFoundException {
//		if (this.preInferedNetwork.getGensInfered().get(idGene).size() > 1) {
//			InferenceGene ig = new InferenceGene(
//					configInference.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
//					configInference.getSearchAlgoritmbyId(searchAlgoritm));
//			this.preInferedNetwork.replazeInference(idGene, ig.reTiesGeneFromDiscreteQf(gdd, idGene,
//					this.preInferedNetwork.getGensInfered().get(idGene)));
//		}
//		synchronized (this.nameNetwork) {
//			// System.out.print(i + " LOOK(" + this.currentGensInfered + ") ");
//			if (++this.currentGensInfered < this.nGens) {
//				// System.out.println("UNLOOK(" + this.currentGensInfered + "):" + iGene + "\t"
//				// + this.nameNetwork);
//				return;
//			}
//			// System.out.println("\tUNLOOK(" + this.currentGensInfered + "):" + iGene +
//			// "\t" + this.nameNetwork);
//		}
//		long i = Thread.currentThread().getId();
//		this.preInferedNetwork.writeInFile(nameNetwork+"_eqf");
////		this.preInferedNetwork.writeFormatGraph(nameNetwork);
//		if (nameNetwork.lastIndexOf('/') != -1)
//			System.out.println(i + " m " + nameNetwork.substring(nameNetwork.lastIndexOf('/')));
//		else
//			System.out.println(i + " m " + nameNetwork);
//	}
//	public PreInferedNetwork inferedPreNetworkTies(DiscreteGeneExpressionData gdd) throws IOException {
//		this.currentGensInfered = 0;
//		// ArrayList<Integer> validsGens = geneExpressionData.getValidGens();
//		for (int i = 0; i < nGens; i++) {
////			int idGene = this.preInferedNetwork.getDetailsByIndex(i).get(0).idGene;
//			int idGene = this.customGenes[i];
//			this.reTiesFromDiscreteById(idGene, gdd);
////			System.out.println(idGene);
//		}
//		return this.preInferedNetwork;
//	}
//	public PreInferedNetwork inferedPreNetworkTiesQf(DiscreteGeneExpressionData gdd) throws IOException {
//		this.currentGensInfered = 0;
//		// ArrayList<Integer> validsGens = geneExpressionData.getValidGens();
//		for (int i = 0; i < nGens; i++) {
////			int idGene = this.preInferedNetwork.getDetailsByIndex(i).get(0).idGene;
//			int idGene = this.customGenes[i];
//			this.reTiesFromDiscreteByIdQf(idGene, gdd);
////			System.out.println(idGene);
//		}
//		return this.preInferedNetwork;
//	}

	public void reInferedGeneMenorByIndex(int iGene, int fixedDegree) throws FileNotFoundException {
		reInferedGeneMenorById(customGenes[iGene], fixedDegree);
	}

	public PreInferedNetwork inferedPreNetwork(String otherInformation) throws IOException {
		this.currentGensInfered = 0;
		// ArrayList<Integer> validsGens = geneExpressionData.getValidGens();
		for (int i = 0; i < nGens; i++) {
			this.inferedGenebyIndex(i, otherInformation);
		}
		return this.preInferedNetwork;
	}

}
