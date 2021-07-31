package inference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import geneticNetwork.BooleanGeneExpressionData;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.qualifyingFunction.Details;
import inference.searchAlgoritm.AbstractSearchAlgoritm;
import resources.Resources;

public class PreInferedNetwork extends AbstractNetworkInfered {
	final public Map<Integer, List<Details>> gensInfered;
	// inference parameters
	final private ID_EVALUATION_METRIC evaluationMetric;
	final private ID_QUALIFYING_FUNCTION qualifyingFunction;
	final private ID_SEARCH_ALGORITM searchAlgoritm;
	// source deg parmeteres
	final private int sizeSamples;

	public PreInferedNetwork(Map<Integer, List<Details>> gensInfered, ID_EVALUATION_METRIC evaluationMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction, ID_SEARCH_ALGORITM searchAlgoritm, int sizeSamples) {
		this.gensInfered = gensInfered;
		this.evaluationMetric = evaluationMetric;
		this.qualifyingFunction = qualifyingFunction;
		this.searchAlgoritm = searchAlgoritm;
		this.sizeSamples = sizeSamples;
	}

	public PreInferedNetwork(ID_EVALUATION_METRIC evaluationMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ID_SEARCH_ALGORITM searchAlgoritm,
			int sizeSamples) {
		this(new ConcurrentHashMap<Integer, List<Details>>(),
				evaluationMetric,
				qualifyingFunction,
				searchAlgoritm,
				sizeSamples);
	}

	public PreInferedNetwork(ID_EVALUATION_METRIC evaluationMetric,
			ID_QUALIFYING_FUNCTION qualifyingFunction,
			ID_SEARCH_ALGORITM searchAlgoritm) {
		this(evaluationMetric, qualifyingFunction, searchAlgoritm, 0);
	}

	public static PreInferedNetwork maskEvolutionNetwork(PreInferedNetwork networkBase) {
		PreInferedNetwork preInferedNetwork = new PreInferedNetwork(
				networkBase.cloneGensInfered(),
				networkBase.evaluationMetric,
				networkBase.qualifyingFunction,
				networkBase.searchAlgoritm,
				networkBase.sizeSamples);
		return preInferedNetwork;
	}

	private Map<Integer, List<Details>> cloneGensInfered() {
		// TODO Auto-generated method stub
		return null;
	}

	public synchronized void addGenInfered(int idGene, List<Details> listDetails) {
		if (!this.gensInfered.containsKey(idGene))
			this.gensInfered.put(idGene, listDetails);
		else
			System.err.println("Warmy: Intento de agregar gen ya existente:"+ idGene+" "+listDetails);
	}

	public Map<Integer, List<Details>> getGensInfered() {
		return gensInfered;
	}

	public ID_EVALUATION_METRIC getEvaluationMetric() {
		return evaluationMetric;
	}

	public ID_QUALIFYING_FUNCTION getQualifyingFunction() {
		return qualifyingFunction;
	}

	public ID_SEARCH_ALGORITM getSearchAlgoritm() {
		return searchAlgoritm;
	}

	public int getSizeSamples() {
		return sizeSamples;
	}

	public Integer indexToId(int index) {
		Set<Integer> set = this.gensInfered.keySet();
		int i = 0;
		for (Integer key : set) {
			if (i == index)
				return key;
			i++;
		}
		return null;
	}

	@Override
	public GeneInfered getGeneInferedById(int idGen) {
		return this.gensInfered.get(idGen).get(0).toGeneInfered();
	}

	@Override
	protected GeneInfered getGeneInferedByIndex(int nGen) {
		return this.gensInfered.get(indexToId(nGen)).get(0).toGeneInfered();
	}

	public List<Details> getDetailsByIndex(int nGen) {
		return this.gensInfered.get(indexToId(nGen));
	}

	public String detailsPredictors(int iGene) {
		StringBuilder sb = new StringBuilder();
		List<Details> details = this.getDetailsByIndex(iGene);
		sb.append(details.size()).append(" empates\n");
		for (Details detail : details) {
			sb.append("Preditores:").append(detail.toPreditorsString());
		}
		return sb.toString();
	}

	public String detailsPredictors(int iGene, BooleanGeneExpressionData ged) {
		StringBuilder sb = new StringBuilder();
		List<Details> details = this.getDetailsByIndex(iGene);
		sb.append(details.size()).append(" empates\n");
		for (Details detail : details) {
			sb.append(detail.toPreditorsString());
			sb.append("dinamica ").append(ged.sizeData).append(" amostras\n").append(Details.frequencyTableString(
					detail.preditors, buildFrequencyTable(detail.preditors, ged, iGene), detail.function));
		}
		return sb.toString();
	}

	public String detailsPredictors(int iGene, BooleanGeneExpressionData ged, boolean[] predictionGabarito) {
		StringBuilder sb = new StringBuilder();
		List<Details> details = this.getDetailsByIndex(iGene);
		sb.append(details.size()).append(" empates\n");
		for (Details detail : details) {
//			Pair<int[][], boolean[]> dd=dinamicGeneDetails(detail.toGeneInfered(), ged);
//			BooleanGeneExpressionData gedDetail=new BooleanGeneExpressionData(new boolean[][] {dd.getValue()});
			// int[][] tableDinamic=dd.frequencyTable;
			sb.append(detail.toPreditorsString());
//			sb.append("dinamica ").append(ged.sizeData).append(" amostras\n")
//			.append(Resources.frequencyTableString(detail.preditors, dd.getKey(),detail.function))
//					.append("\nDistance:").append(1-gedDetail.distHaming(new boolean[][] { predictionGabarito}, new int[predictionGabarito.length]).get(-1));
		}
		return sb.toString();
	}

	public void saveDetailsPreditors(int iGene, String sufFile) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(iGene + "_" + sufFile), true));
		printWriter.append(detailsPredictors(iGene));
		printWriter.close();
	}

	public String frequencyTableGeneString(int nGene, BooleanGeneExpressionData ged) {
		GeneInfered geneInfered = this.getGeneInferedByIndex(nGene);
		return Details.frequencyTableString(geneInfered.getPredictors(), frequencyTableGene(nGene, ged),
				this.getDetailsByIndex(nGene).get(0).function);
	}

	@Override
	public int getSizeGenes() {
		return gensInfered.size();
	}

	public String detailsPredictors() {
		StringBuilder sb = new StringBuilder();
		NavigableSet<Integer>[] can = this.canalizeGenes();
		for (int i = 0; i < can.length; i++) {
			sb.append("Gene:").append(i).append("\n").append("Canaliza a ->").append(can[i]).append("\n")
					.append(this.detailsPredictors(i));
		}
		return sb.toString();
	}

	public static PreInferedNetwork readFromFile(File file) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		String[] configLine = line.split("\\|");

		ID_EVALUATION_METRIC eMetric = ID_EVALUATION_METRIC.valueOf(configLine[0]);
		ID_QUALIFYING_FUNCTION qFunction = ID_QUALIFYING_FUNCTION.valueOf(configLine[1]);
		ID_SEARCH_ALGORITM sAlgoritm = ID_SEARCH_ALGORITM.valueOf(configLine[2]);
		// deg parameters
		line = in.readLine();
		configLine = line.split("\\|");
		int sizeSamples = Integer.parseInt(configLine[1]);

		PreInferedNetwork gn = new PreInferedNetwork(eMetric, qFunction, sAlgoritm, sizeSamples);

		// read and process gene's data
		int idGene = -1;
		double qv = 0;
		while ((line = in.readLine()) != null) {
			String[] lineSplit;
			if (!line.startsWith("|")) {
				lineSplit = line.split("\\|");
				idGene = Integer.parseInt(lineSplit[0]);
				qv = Double.parseDouble(lineSplit[1]);
				gn.getGensInfered().put(idGene, new ArrayList<>());
			} else {
				lineSplit = line.substring(1).split(",");
				TreeSet<Integer> pred = new TreeSet<>();
				if (line.length() > 1)
					for (String is : lineSplit) {
						pred.add(Integer.parseInt(is));
					}
				Details detail = new Details();
				detail.idGene = idGene;
				detail.preditors = pred;
				detail.qualityValue = qv;
				gn.getGensInfered().get(idGene).add(detail);
			}
		}
		in.close();
		return gn;
	}

	public static PreInferedNetwork readFromFile(String pathFile) throws NumberFormatException, IOException {
		if (!pathFile.endsWith(".ylu"))
			pathFile += ".ylu";
		File file = new File(pathFile);
		return readFromFile(file);
	}

	public void writeInFile(String filePath) throws FileNotFoundException {
		if (!filePath.endsWith("ylu"))
			filePath += ".ylu";
		writeInFile(new File(filePath));
	}

	public void writeInFile(File file) throws FileNotFoundException {
		if (!file.getName().endsWith("ylu")) {
			file = new File(file.getAbsolutePath() + ".ylu");
		}
		PrintWriter writer = new PrintWriter(file);
		writer.println(getEvaluationMetric() + "|" + getQualifyingFunction() + "|" + getSearchAlgoritm());
		writer.println(getGensInfered().size() + "|" + getSizeSamples());// + "|" + getType_Dynamic());
		StringBuilder line = new StringBuilder();
		Set<Integer> set = this.gensInfered.keySet();
		for (Integer key : set) {
			List<Details> details = this.gensInfered.get(key);
			if(details.isEmpty())
				line.append(key).append("|").append(0.0).append("\n|\n");
			else {
				Details detail = details.get(0);
				line.append(key).append("|").append(detail.qualityValue).append('\n');
				for (Details d : details) {
					line.append("|").append(d.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(",")))
							.append('\n');
				}
			}
		}
		writer.print(line);
		writer.close();
	}
	public void writeRadioFormatGraph(String filePath,
			ArrayList<Integer> customGens,
			int profFrom,
			int profTo) throws FileNotFoundException {
		PreInferedNetwork sub = subPreInferedNetwork(customGens, profFrom, 0);
		evoluirFrente(sub, customGens, profTo);
		sub.writeFormatGraph(filePath);		
	}

	public void writeFormatGraph(String filePath) throws FileNotFoundException {
		if (!filePath.endsWith("txt"))
			filePath += ".txt";
		PrintWriter writer = new PrintWriter(filePath);
		StringBuilder line = new StringBuilder();
		Set<Integer> set = this.gensInfered.keySet();
		for (Integer key : set) {
			List<Details> details = this.gensInfered.get(key);
			Details detail = details.get(0);
			line.append(key).append(" ");
			NavigableSet<Integer> pred = new TreeSet<>();
			for (Details d : details) {
				pred.addAll(d.preditors);
			}
			line.append(pred.stream().map(o -> o.toString()).collect(Collectors.joining(" "))).append(' ')
					.append(detail.qualityValue).append('\n');
		}
		writer.print(line);
		writer.close();
	}

	public int countAllgens() {
		return this.allGens().size();
	}

	public NavigableSet<Integer> customGens() {
		return new TreeSet<>(this.gensInfered.keySet());
	}

	public NavigableSet<Integer> allGens() {
		NavigableSet<Integer> pred = new TreeSet<>();
		Set<Integer> set = this.gensInfered.keySet();
		for (Integer key : set) {
			List<Details> details = this.gensInfered.get(key);
			pred.add(key);
			for (Details d : details) {
				pred.addAll(d.preditors);
			}
		}
		return pred;
	}

	public void show() {
		PrintStream writer = System.out;
		writer.println(getEvaluationMetric() + "|" + getQualifyingFunction() + "|" + getSearchAlgoritm());
		writer.println(getGensInfered().size() + "|" + getSizeSamples());// + "|" + getType_Dynamic());
		StringBuilder line = new StringBuilder();
		Set<Integer> set = this.gensInfered.keySet();
		for (Integer key : set) {
			List<Details> details = this.gensInfered.get(key);
			Details detail = details.get(0);
			line.append(key).append("|").append(detail.qualityValue).append('\n');
			for (Details d : details) {
				line.append("|").append(d.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(",")))
						.append('\n');
			}
		}
		writer.print(line);
	}

	public void replazeInference(int idGene, List<Details> detailsInferenceGens) {
		this.gensInfered.put(idGene, detailsInferenceGens);
	}

	public void replazeInferenceByIndex(int index, List<Details> detailsInferenceGens) {
		this.replazeInference(indexToId(index), detailsInferenceGens);
	}

	public void toZeroInference(int idGene) {
		this.gensInfered.put(idGene, new ArrayList<>());
	}

//	private ArrayList<Details> buildDetailsFromCluster(ArrayList<Details> detailsList, TreeMap<Integer, ArrayList<Integer>> clusters) {
//		ArrayList<Details> detailsResult=new ArrayList<>();
//		for (Details details : detailsList) {
//			ArrayList<TreeSet<Integer>> preds= mappingPred(pairClusterByPreditors(details.preditors, clusters));
//			for (TreeSet<Integer> p : preds) {
//				Details d=Details.cloneof(details);
//				d.preditors=p;
//				detailsResult.add(d);
//			}
//		}
//		return detailsResult;
//	}
	public void addClusterPreditors(BooleanGeneExpressionData booleanGeneExpressionData) {
		Set<Integer> set = this.gensInfered.keySet();
		for (Integer key : set) {
			this.gensInfered.put(key, buildDetailsFromCluster(this.gensInfered.get(key), booleanGeneExpressionData));
		}
	}

	public void completeClusterPreditors(BooleanGeneExpressionData bged) {
		NavigableSet<Integer> set = new TreeSet<>(this.gensInfered.keySet());
		for (Integer key : set) {
			if (bged.containsClusterKey(key) &&
					bged.getCluster(key).size() > 1) {
				List<Integer> clus = bged.getCluster(key);
				for (Integer idGene : clus) {
					if (idGene != key && !this.gensInfered.containsKey(idGene)) {
						List<Details> origenDetails = this.gensInfered.get(key);
						List<Details> nvoDetails = new ArrayList<Details>();
						for (Details od : origenDetails) {
							Details nvo = Details.cloneof(od);
							nvo.idGene = idGene;
							nvoDetails.add(nvo);
						}
						this.gensInfered.put(idGene, nvoDetails);
					}
				}
			}
		}
	}

	public PreInferedNetwork subPreInferedNetwork(List<Integer> customGens, int profFrom, int profTo) {
		PreInferedNetwork sub = new PreInferedNetwork(getEvaluationMetric(), getQualifyingFunction(),
				getSearchAlgoritm(), getSizeSamples());
		NavigableSet<Integer> from = new TreeSet<>(customGens);
		NavigableSet<Integer> to = new TreeSet<>(customGens);
		gensListCustoms(from, profFrom, true);
		gensListCustoms(to, profTo, false);
		for (Integer gen : from) {
			if (!sub.gensInfered.containsKey(gen))
				sub.gensInfered.put(gen, this.gensInfered.get(gen));
		}
		for (Integer gen : to) {
			if (!sub.gensInfered.containsKey(gen))
				sub.gensInfered.put(gen, this.gensInfered.get(gen));
		}
		return sub;
	}
	public PreInferedNetwork subPreInferedNetwork(List<Integer> customGens) {
		PreInferedNetwork sub = new PreInferedNetwork(getEvaluationMetric(), getQualifyingFunction(),
				getSearchAlgoritm(), getSizeSamples());
		for (Integer gen : customGens) {
				sub.gensInfered.put(gen, this.gensInfered.get(gen));
		}
		return sub;
	}
	public void evoluirFrente(PreInferedNetwork baseNet,List<Integer> customGens, int profundidad) {
		if(profundidad<=0)return;
		else {
			List<Integer> folhasFrente=new ArrayList<>();
			for (Integer gen : customGens) {
				Set<Integer> keys=this.gensInfered.keySet();
				for (Integer k : keys) {
					if(this.gensInfered.get(k).get(0).preditors.contains(gen)) {//gen predice a k
						folhasFrente.add(k);
						if(baseNet.gensInfered.containsKey(k))
							baseNet.gensInfered.get(k).get(0).preditors.add(k);
						else {
							ArrayList<Details> al=new ArrayList<>();
							al.add(new Details());
							Details d =al.get(0);
							d.qualityValue=0.0;
							d.preditors=new TreeSet<Integer>();
							d.preditors.add(gen);
							baseNet.gensInfered.put(k, al);
						}
					}
						
				}
			}
			evoluirFrente(baseNet, folhasFrente, profundidad-1);
		}
	}

	private void gensListCustoms(NavigableSet<Integer> customGens, int prof, boolean fromto) {
		if (fromto) {// profundidad de preitores
			if (prof > 1) {
				NavigableSet<Integer> pre = new TreeSet<>();
				for (Integer gen : customGens) {
					List<Details> details = this.gensInfered.get(gen);
					for (Details d : details)
						pre.addAll(d.preditors);
				}
				customGens.addAll(pre);
				gensListCustoms(customGens, prof - 1, fromto);
			}
		} else {// profuncidad de predecidosfolhasFrente
			if (prof > 0) {
				NavigableSet<Integer> pre = new TreeSet<>();
				Set<Integer> gens = this.gensInfered.keySet();
				for (Integer gen : gens) {
					List<Details> details = this.gensInfered.get(gen);
					for (Details d : details) {
						if (!Collections.disjoint(customGens, d.preditors)) {
							pre.add(gen);
							break;
						}
					}
				}
				customGens.addAll(pre);
				gensListCustoms(customGens, prof - 1, fromto);
			}
		}
	}

	public void clearTiesforRepetPreditors() {
		Set<Integer> genes = this.gensInfered.keySet();
		for (Integer gene : genes) {
			if (this.gensInfered.get(gene).size() > 1) {
				Map<Integer, Integer> counts = new TreeMap<>();
				for (Details detPreditors : this.gensInfered.get(gene)) {
					detPreditors.preditors.forEach(a -> {
						if (counts.containsKey(a))
							counts.put(a, counts.get(a) + 1);
						else
							counts.put(a, 1);
					});
				}
				int max = counts.values().stream().max(Integer::compareTo).get();
				NavigableSet<Integer> pred = counts.entrySet().stream().filter(e -> e.getValue() == max)
						.map(Map.Entry::getKey).collect(Collectors.toCollection(TreeSet::new));
				Details nd = Details.cloneof(this.gensInfered.get(gene).get(0));
				nd.preditors = pred;
				List<Details> nl = new ArrayList<>();
				nl.add(nd);
				this.gensInfered.put(gene, nl);
			}
		}
	}
	public PreInferedNetwork toReduceCountPreditor() {
		PreInferedNetwork reduce = new PreInferedNetwork(getEvaluationMetric(), getQualifyingFunction(),
				getSearchAlgoritm(), getSizeSamples());
		this.gensInfered.forEach((k, v) -> {
			reduce.gensInfered.put(k,
					Details.cloneListof(AbstractSearchAlgoritm.reduceByFrequency(v),k));
		});
		return reduce;
	}
//	public PreInferedNetwork toReduceEmpates() {
//		PreInferedNetwork reduce = new PreInferedNetwork(getEvaluationMetric(), getQualifyingFunction(),
//				getSearchAlgoritm(), getSizeSamples());
//		this.gensInfered.forEach((k, v) -> {
//			reduce.gensInfered.put(k,
//					suMetodo(v,k));
//		});
//		return reduce;
//	}
	public PreInferedNetwork toUniquePreditor() {
		PreInferedNetwork unique = new PreInferedNetwork(getEvaluationMetric(), getQualifyingFunction(),
				getSearchAlgoritm(), getSizeSamples());

		this.gensInfered.forEach((k, v) -> {
			unique.gensInfered.put(k, new ArrayList<>());
			if (!v.isEmpty()) {
				List<Details> listDet= AbstractSearchAlgoritm.reducePreditorsDetails(null, null, v);
				NavigableSet<Integer> preds = new TreeSet<>();
				Map<Integer, Integer> count = new TreeMap<>();
				listDet.forEach((Details d) -> {
					d.preditors.forEach(e -> {
						if (count.containsKey(e))
							count.put(e, count.get(e) + 1);
						else
							count.put(e, 1);
					});

				});

				final Integer top = count.values().stream().max(Integer::compareTo).orElse(0);
//			preds[k]=
				count.forEach((ck, cv) -> {
					if (cv.equals(top))
						preds.add(ck);
				});
//			stream()
//				.filter(key-> count.get(key).equals(max))
//				.collect(Collectors.toSet(TreeSet<Integer>::new));

				unique.gensInfered.get(k).add(listDet.get(0));
				unique.gensInfered.get(k).get(0).preditors = preds;
			}
		});
		return unique;

	}

	public int[][] adjacencyMatrix() {
		int[][] matrix = new int[this.gensInfered.size()][this.gensInfered.size()];
		adjacencyMatrix(matrix);
		return matrix;
	}

	public void adjacencyMatrix(int[][] matrix) {
		// int[][] matrix=new int[this.gensInfered.size()][this.gensInfered.size()];
		this.gensInfered.forEach((k, v) -> {
			if (!v.isEmpty())
				v.get(0).preditors.forEach(x -> matrix[k][x] = matrix[x][k] = 1);
		});
		// return matrix;
	}

	public static double clusteringCoefficient(int ngen, int[][] adjMatrix) {
		int grau = IntStream.of(adjMatrix[ngen]).sum();
		int ct = 0;
		for (int i = 0; i < adjMatrix.length; i++) {
			if (adjMatrix[i][ngen] == 1)
				for (int j = i + 1; j < adjMatrix.length; j++) {
					if (adjMatrix[j][ngen] == 1 && adjMatrix[i][j] == 1)
						ct++;
				}
		}
		double coef = (grau > 1) ? ((double) (2 * ct)) / (grau * (grau - 1)) : 0;
		return coef;
	}

	public int[][] distanceMatrix() {
		final int size = this.gensInfered.size();
		final int MAX = Integer.MAX_VALUE / 4;
		int[][] matrix = new int[size][size];
		Stream.of(matrix).forEach(a -> Arrays.fill(a, MAX));
		adjacencyMatrix(matrix);
		for (int i = 0; i < size; i++) {
			matrix[i][i] = 0;
		}
		for (int k = 0; k < size; k++)
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++) {
					int dt = matrix[i][k] + matrix[k][j];
					if (matrix[i][j] > dt)
						matrix[i][j] = dt;
				}
		return matrix;
	}

	public Map<Integer, Integer> distanceFrequency() {
		final int MAX = Integer.MAX_VALUE / 4;
		Map<Integer, Integer> dist = new TreeMap<>();
		int[][] distMatrix = distanceMatrix();
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = i+1; j < distMatrix.length; j++) {
				int d = distMatrix[i][j];
				if (d != 0 && d != MAX)
					if (dist.containsKey(d))
						dist.put(d, dist.get(d) + 1);
					else
						dist.put(d, 1);
			}
		}
		return dist;
	}
	public Map<Integer, Integer> distanceFrequency(int[][] distMatrix) {
		final int MAX = Integer.MAX_VALUE / 4;
		Map<Integer, Integer> dist = new TreeMap<>();
//		int[][] distMatrix = distanceMatrix();
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = i+1; j < distMatrix.length; j++) {
				int d = distMatrix[i][j];
				if (d != 0 && d != MAX)
					if (dist.containsKey(d))
						dist.put(d, dist.get(d) + 1);
					else
						dist.put(d, 1);
			}
		}
		return dist;
	}
	public double[] distMedDP(int[][] distMatrix) {
		double med=0;
		int c=0;
		final int MAX = Integer.MAX_VALUE / 4;
//		int[][] distMatrix = distanceMatrix();
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = i+1; j < distMatrix.length; j++) {
				if(distMatrix[i][j]==MAX) continue;
				med += distMatrix[i][j];
				c++;
			}
		}
		med=med/c;
		double d=0;
		for (int i = 0; i < distMatrix.length; i++) {
			for (int j = i+1; j < distMatrix.length; j++) {
				if(distMatrix[i][j]==MAX) continue;
				d += Math.pow((med - distMatrix[i][j]) , 2);
			}
		}
		d=Math.sqrt(d/c);
		return new double[]{med,d};
	}
	public double[] distMedDP(int[][] distMatrix, int[] sementes) {
		double med=0;
		int c=0;
		final int MAX = Integer.MAX_VALUE / 4;
//		int[][] distMatrix = distanceMatrix();
		for (int i = 0; i < sementes.length; i++) {
			for (int j = 0; j < sementes.length; j++) {
				if(distMatrix[sementes[i]][sementes[j]]==MAX) continue;
				med +=distMatrix[sementes[i]][sementes[j]];
				c++;
			}
		}
		med=med/c;
		double d=0;
		for (int i = 0; i < sementes.length; i++) {
			for (int j = 0; j < sementes.length; j++) {
				if(distMatrix[sementes[i]][sementes[j]]==MAX) continue;
				d += Math.pow((med - distMatrix[sementes[i]][sementes[j]]) , 2);
			}
		}
		d=Math.sqrt(d/c);
		return new double[]{med,d};
	}
	public Map<Integer, Integer> distanceFrequency(int[][] distMatrix, int[] sementes) {
		final int MAX = Integer.MAX_VALUE / 4;
		Map<Integer, Integer> dist = new TreeMap<>();
//		int[][] distMatrix = distanceMatrix();
		for (int i = 0; i < sementes.length; i++) {
			for (int j = 0; j < sementes.length; j++) {
				int d = distMatrix[sementes[i]][sementes[j]];
				if (d != 0 && d != MAX)
					if (dist.containsKey(d))
						dist.put(d, dist.get(d) + 1);
					else
						dist.put(d, 1);
			}
		}
		return dist;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		String dir="/home/fernando/MALARIA_TEST/knnEndTest/tranfer/50NC/";
		ID_QUALIFYING_FUNCTION[] metodos=new ID_QUALIFYING_FUNCTION [] {ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG
				,ID_QUALIFYING_FUNCTION.CG
				,ID_QUALIFYING_FUNCTION.GLSFS};
//		for (ID_QUALIFYING_FUNCTION qf : metodos) {
//			PreInferedNetwork pin = PreInferedNetwork
//					.readFromFile(dir+"allRE__"+qf);
//			BooleanGeneExpressionData bge= new BooleanGeneExpressionData("/home/fernando/MALARIA_TEST/malaria");
//			NetworkInfered ni= pin.toNetworkInfered(new ConfigInference(47), bge);
//			ni.writeInFile(dir+"gn_"+qf);
//		}
		BooleanGeneExpressionData malData= new BooleanGeneExpressionData("/home/fernando/MALARIA_TEST/malaria");
		for (ID_QUALIFYING_FUNCTION qf : metodos) {
			NetworkInfered g=NetworkInfered.readFromFile(dir+"gn_"+qf);
			BooleanGeneExpressionData predic=g.makeGeneExpresionState(malData).getKey();
			System.out.println(qf+":\t"+ malData.taxaAcertos(predic, 1));
		}
		//ni.writeInFile("fmal");
//		TreeMap<Integer, Integer> dist= pin.toUniquePreditor().distanceFrequency();
//		dist.forEach((k,v)->System.out.println(k+"->"+v));
//		PreInferedNetwork unique = pin.toUniquePreditor();
//		int[][] adj = unique.adjacencyMatrix();
//		int[] sementes = new int[] { 4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550 };
//		System.out.println(
//				unique.gensInfered.keySet().parallelStream().mapToDouble(o -> clusteringCoefficient(o, adj)).sum()
//						/ unique.getSizeGenes());
//		System.out.println(
//				IntStream.of(sementes).mapToDouble(o -> clusteringCoefficient(o, adj)).sum() / sementes.length);
	}

	@Override
	public List<Integer> getIntegerGens() {
		return new ArrayList<>(gensInfered.keySet());
	}
	public NetworkInfered toNetworkInfered(ConfigInference cf, BooleanGeneExpressionData bge) {
		
		InferenceGene ig=new InferenceGene(cf.getQualifyingFunctionById(qualifyingFunction, evaluationMetric),
				cf.getSearchAlgoritmbyId(searchAlgoritm),
				cf.getEvaluationMetricById(evaluationMetric));
		
		GeneInfered[] gi=new GeneInfered[gensInfered.size()];
		AtomicInteger index= new AtomicInteger(0);
		gensInfered.forEach(
				(idGene,pred)->{
//					gi[index.get()]=ig.reInferenceGene(bge, idGene, pred.get(0).preditors);
					gi[index.get()]=ig.reInferenceGene(bge, idGene, pred.get(Resources.random.nextInt(pred.size())).preditors);
					index.incrementAndGet();
				});
		
		return new NetworkInfered(getEvaluationMetric(), getQualifyingFunction(), getSearchAlgoritm(), getSizeSamples(),
				gi);
	}

}
