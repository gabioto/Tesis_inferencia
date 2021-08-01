package inference;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import geneticNetwork.GeneticNetwork;
import inference.ConfigInference.*;

public class NetworkInfered extends AbstractNetworkInfered {
	final private GeneInfered[] gens;
	// inference parameters
	final private ID_EVALUATION_METRIC evaluationMetric;
	final private ID_QUALIFYING_FUNCTION qualifyingFunction;
	final private ID_SEARCH_ALGORITM searchAlgoritm;
	// source deg parmeteres
	final private int sizeSamples;

	public NetworkInfered(ID_EVALUATION_METRIC evaluationMetric, ID_QUALIFYING_FUNCTION qualifyingFunction,
			ID_SEARCH_ALGORITM searchAlgoritm, int sizeSamples, GeneInfered[] gens) {
		this.evaluationMetric = evaluationMetric;
		this.qualifyingFunction = qualifyingFunction;
		this.searchAlgoritm = searchAlgoritm;
		this.sizeSamples = sizeSamples;
		this.gens = gens;
	}

	public GeneInfered[] getGens() {
		return gens;
	}
	public List<Integer> getIntegerGens(){
		return Stream.of(this.gens).map(a->Integer.valueOf(a.getIdGene())).collect(Collectors.toList());
	}
	public int getSizeSamples() {
		return sizeSamples;
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

	@Override
	public GeneInfered getGeneInferedById(int idGene) {
		int inf=0,max=gens.length-1;
		while (inf<=max) {
			int med =(inf+max)/2;
			int getId=gens[med].getIdGene();
			if(getId==idGene)
				return gens[med];
			else
				if(getId<idGene)
					inf=med+1;
				else
					max=med-1;
		}
		return null;
	}

	/**
	 * FileFormat evaluationMetric|qualifyingfunction|searchAlgoritm
	 * sizeGenes|sizeSamples|type_Dynamic
	 * preditor11,preditor12,...,preditor1N|long_table11,long_table12...|...
	 * ...state_function11,state_function12...|state_notObserver11,state_notObserver12...|...
	 * ...count0, count1|critery_function_value1
	 * 
	 * @param pathFile
	 * @return
	 * @throws IOException
	 */
	public static NetworkInfered readFromFile(String pathFile) throws IOException {
		if (!pathFile.endsWith(".ylu"))
			pathFile += ".ylu";
		File file = new File(pathFile);
		int iGene = -1;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		String[] configLine = line.split("\\|");
		// inference parameters

		ID_EVALUATION_METRIC eMetric = ID_EVALUATION_METRIC.valueOf(configLine[0]);
		ID_QUALIFYING_FUNCTION qFunction = ID_QUALIFYING_FUNCTION.valueOf(configLine[1]);
		ID_SEARCH_ALGORITM sAlgoritm = ID_SEARCH_ALGORITM.valueOf(configLine[2]);
		// deg parameters
		line = in.readLine();
		configLine = line.split("\\|");
		int sizeGenes = Integer.parseInt(configLine[0]);
		int sizeSamples = Integer.parseInt(configLine[1]);

		NetworkInfered gn = new NetworkInfered(eMetric, qFunction, sAlgoritm, sizeSamples,
				new GeneInfered[sizeGenes]);

		// read and process gene's data
		int ngA=0;
		while ((line = in.readLine()) != null) {
			iGene++;
//			if (line.equals("NN")) {
//				gn.getGens()[iGene] = null;
//			} else {				
				String[] lineSplit = line.split("\\|");
				if(lineSplit.length==6) {
					line=ngA+"|"+line;
					lineSplit = line.split("\\|");
					ngA++;
				}					
				int idGene = Integer.parseInt(lineSplit[0]);
				if(lineSplit.length==1)
				{
					gn.getGens()[iGene]=GeneInfered.ZeroGeneInfered(idGene);
					continue;
				}
				String[] preds = lineSplit[1].split(",");
				String[] function = lineSplit[2].split(",");
				String[] state = lineSplit[3].split(",");
				String[] stateNo = lineSplit[4].split(",");
				String[] boolvalues = lineSplit[5].split(",");
				// read predictors
				TreeSet<Integer> preditors_ids = new TreeSet<Integer>();
				if (!preds[0].equals("")) {
					for (int iPred = 0; iPred < preds.length; iPred++) {
						preditors_ids.add(Integer.valueOf(preds[iPred]));
					}
				}
				// read functions
				long[] boolean_function_gen = new long[function.length];
				long[] state_function_gen = new long[function.length];
				long[] state_no_gen = new long[function.length];
				for (int i = 0; i < function.length; i++) {
					boolean_function_gen[i] = Long.parseLong(function[i]);
					state_function_gen[i] = Long.parseLong(state[i]);
					state_no_gen[i] = Long.parseLong(stateNo[i]);
				}
				gn.getGens()[iGene] = new GeneInfered(idGene, preditors_ids, boolean_function_gen, state_function_gen,
						state_no_gen, Double.parseDouble(lineSplit[6]),
						new int[] { Integer.parseInt(boolvalues[0]), Integer.parseInt(boolvalues[1]) });
//			}
			
		}
		in.close();
		return gn;
	}

	public void writeInFile(String filePath) throws FileNotFoundException {
		if (!filePath.endsWith("ylu"))
			filePath += ".ylu";
		writeInFile(new File(filePath));
	}

	/**
	 * Escribe en una archivo la red inferida
	 * 
	 * @param file Objeto file del archivo
	 * @throws FileNotFoundException
	 */
	public void writeInFile(File file) throws FileNotFoundException {
		if (!file.getName().endsWith("ylu")) {
			file = new File(file.getAbsolutePath() + ".ylu");
		}
		PrintWriter writer = new PrintWriter(file);
		writer.println(getEvaluationMetric() + "|" + getQualifyingFunction() + "|" + getSearchAlgoritm());
		writer.println(getGens().length + "|" + getSizeSamples());// + "|" + getType_Dynamic());
		StringBuilder line = new StringBuilder();
		for (GeneInfered geneInfered : gens) {
//			if (geneInfered == null) {
//				line.append("NN\n");
//				continue;
//			}
			line.append(geneInfered.getIdGene()).append('|');
			Set<Integer> preditorsGene = geneInfered.getPredictors();
			if (preditorsGene.size() > 0) {
				for (Object gene : preditorsGene) {
					line.append(gene).append(",");
				}
				line.deleteCharAt(line.length() - 1);
			} else {
				line.append("\n");
				continue;
//				line.append("NN");
			}
			line.append("|");
			for (Long func : geneInfered.getBoolean_functions()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|");
			for (Long func : geneInfered.getState_functions()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|");
			for (Long func : geneInfered.getState_notObserved()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|").append(geneInfered.getGlobal_boolean_values()[0])
					.append(",").append(geneInfered.getGlobal_boolean_values()[1]).append("|")
					.append(geneInfered.getCritery_function_values()).append("\n");
		}
		writer.print(line);
		writer.close();
	}

	public void show() {
		PrintStream writer = System.out;
		writer.println(getEvaluationMetric() + "|" + getQualifyingFunction() + "|" + getSearchAlgoritm());
		writer.println(getGens().length + "|" + getSizeSamples());// + "|" + getType_Dynamic());
		StringBuilder line = new StringBuilder();
		for (GeneInfered geneInfered : gens) {
			if (geneInfered == null) {
				line.append("NN\n");
				continue;
			}
			line.append(geneInfered.getIdGene()).append('|');
			Set<Integer> preditorsGene = geneInfered.getPredictors();
			if (preditorsGene.size() > 0) {
				for (Object gene : preditorsGene) {
					line.append(gene).append(",");
				}
				line.deleteCharAt(line.length() - 1);
			} else
				line.append("NN");
			line.append("|");
			for (Long func : geneInfered.getBoolean_functions()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|");
			for (Long func : geneInfered.getState_functions()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|");
			for (Long func : geneInfered.getState_notObserved()) {
				line.append(func).append(",");
			}
			line.deleteCharAt(line.length() - 1).append("|").append(geneInfered.getGlobal_boolean_values()[0])
					.append(",").append(geneInfered.getGlobal_boolean_values()[1]).append("|")
					.append(geneInfered.getCritery_function_values()).append("\n");
		}
		writer.print(line);
	}

	public double getMeanDegree() {
		double degree = 0;
		for (GeneInfered geneInfered : this.getGens()) {
			degree += (geneInfered == null) ? 0 : geneInfered.getPredictors().size();
		}
		return degree / this.getGens().length;
	}

	@Override
	public int getSizeGenes() {
		return gens.length;
	}

	public static boolean existsFile(String pathFile) {
		if (!pathFile.endsWith(".ylu"))
			pathFile += ".ylu";
		File file = new File(pathFile);
		return file.exists() && file.length() > 0;
	}

	@Override
	public GeneInfered getGeneInferedByIndex(int nGen) {
		return this.gens[nGen];
	}
	public static int[] validateFPFNTPTN(GeneticNetwork originalNetwork, NetworkInfered testNetwork, int[] genesTest) {
	int[] FPFNTPTN = new int[] { 0, 0, 0, 0 };
	for (int genInTest = 0; genInTest < originalNetwork.getPredictors().length; genInTest++) {
		// verificar filtro
		if (genesTest[genInTest] == 0)
			continue;
		for (int testGen = 0; testGen < originalNetwork.getPredictors().length; testGen++) {
			boolean genInTestNetwork = (testNetwork.getGens()[genInTest] == null) ? false
					: testNetwork.getGens()[genInTest].getPredictors().contains(testGen);
			boolean genInOriginalNetwork = originalNetwork.getPredictors()[genInTest].contains(testGen);

			if (!genInOriginalNetwork && genInTestNetwork)//FP
			{
				FPFNTPTN[0]++;
			} else if (genInOriginalNetwork && !genInTestNetwork)//FN
			{
				FPFNTPTN[1]++;
			} else if (genInOriginalNetwork && genInTestNetwork)//TP
			{
				FPFNTPTN[2]++;
			} else // (!geneEmGabarito && !geneEmRede) TN
			{
				FPFNTPTN[3]++;
			}
		}
	}
	return FPFNTPTN;
}
}