package geneticNetwork;

import resources.*;
import java.io.*;
import java.util.*;
import javafx.util.Pair;
//import inference.NetworkInfered2;
//import inference.qualifyingFunction.LinearGrouping;
/**
 * @author fernandito
 *
 */
public class GeneticNetwork extends AbstractNetwork {
	public enum Type_Dynamic {
		BOOLEAN,// sin ruido
		PROBABILISTIC_BOOLEAN //con ruido
	}
	
	public enum Type_Function{
		C,//Canalization
		NC,//Nested canalizing
		T,//Threshold
		R//Random
	}
	
	public enum Topology_Network {
		SCALE_FREE,
		RANDOM,
		FIXED
	}

	private TreeSet<Integer>[] predictors;
	private Type_Function[] type_Functions;
	private long[][][] boolean_functions;
	private Double probability;
	private Topology_Network topology;

	public GeneticNetwork() {
	}

	public void setPredictors(TreeSet<Integer>[] ids_predictors) {
		this.predictors = ids_predictors;
	}

	public TreeSet<Integer>[] getPredictors() {
		return predictors;
	}

	public TreeSet<Integer> getPredictorsByGene(int nGene) {
		return predictors[nGene];
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public Topology_Network getTopology() {
		return topology;
	}

	public void setTopology(Topology_Network topology) {
		this.topology = topology;
	}

	public long[][][] getBoolean_functions() {
		return boolean_functions;
	}

	public void setBoolean_functions(long[][][] boolean_functions) {
		this.boolean_functions = boolean_functions;
	}
	
	public Type_Function[] getType_Functions() {
		return type_Functions;
	}

	public void setType_Functions(Type_Function[] type_Functions) {
		this.type_Functions = type_Functions;
	}

	@SuppressWarnings("unchecked")
	public static GeneticNetwork readFromFile(File file) throws IOException {
		if (!file.getPath().endsWith(".ylu"))
			file = new File(file.getPath() + ".ylu");
		GeneticNetwork gn = new GeneticNetwork();
		int iGene = 0;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		String[] configLine = line.split("\\|");
		// header values
		gn.setTopology(Topology_Network.valueOf(configLine[0]));
		gn.setPredictors(new TreeSet[Integer.parseInt(configLine[1])]);
		gn.setBoolean_functions(new long[Integer.parseInt(configLine[1])][][]);
		gn.setProbability(Double.parseDouble(configLine[2]));
		gn.setType_Functions(new Type_Function[Integer.parseInt(configLine[1])]);
		// read and process gene's data
		while ((line = in.readLine()) != null) {
			TreeSet<Integer> preditors_ids = new TreeSet<Integer>();
			long[][] boolean_function_gen = null;
			if (!line.equals("NN")) {
				configLine = line.split("\\|");
				String[] preds = configLine[0].split(",");
				String[] functions = configLine[1].split(":");
				if(configLine.length>2)
					gn.getType_Functions()[iGene]=Type_Function.valueOf(configLine[2]);
				// read predictors

				if (!preds[0].equals("NN")) {
					for (int iPred = 0; iPred < preds.length; iPred++) {
						preditors_ids.add(Integer.valueOf(preds[iPred]));
					}
				}
				// read functions
				boolean_function_gen = new long[functions.length][];
				for (int i = 0; i < functions.length; i++) {
					String textFunction[] = functions[i].split(",");
					long[] boolean_table = new long[textFunction.length];
					for (int j = 0; j < textFunction.length; j++) {
						boolean_table[j] = Long.parseLong(textFunction[j]);
					}
					boolean_function_gen[i] = boolean_table;
				}
			}
			gn.getBoolean_functions()[iGene] = boolean_function_gen;
			gn.getPredictors()[iGene] = preditors_ids;
			iGene++;
		}
		in.close();
		return gn;
	}

	/**
	 * FileFormat TOPOLOGY_NAME|sizeGenes|probability
	 * preditor11,preditor12,...,preditor1N|long_table11,long_table12...:
	 * long_table21,long_table22...
	 * 
	 * @param pathFile
	 * @return
	 * @throws IOException
	 */
	public static GeneticNetwork readFromFile(String pathFile) throws IOException {
		return GeneticNetwork.readFromFile(new File(pathFile));
	}

	public void writeInFile(String file) throws FileNotFoundException {
		writeInFile(new File(file));
	}

	public void writeInFile(File file) throws FileNotFoundException {
		if (!file.getName().endsWith("ylu")) {
			file = new File(file.getAbsolutePath() + ".ylu");
		}
		PrintWriter writer = new PrintWriter(file);
		writer.println(getTopology() + "|" + getPredictors().length + "|" + probability);
		int iGene = 0;

		StringBuilder line = new StringBuilder();
		for (Set<Integer> preditorsGene : predictors) {
			if (preditorsGene.size() > 0) {
				for (Object gene : preditorsGene) {
					line.append(gene).append(",");
				}
				line.deleteCharAt(line.length() - 1);

				line.append("|");
				for (long[] functionsGene : boolean_functions[iGene]) {
					for (Long func : functionsGene) {
						line.append(func).append(",");
					}
					line.deleteCharAt(line.length() - 1).append(":");
				}
			} else
				line.append("NN ");
			line.deleteCharAt(line.length() - 1).append("|").append(type_Functions[iGene]).append("\n");
			iGene++;
		}
		writer.print(line);
		writer.close();
	}
	public static GeneticNetwork buildNetwork(int sizeGens,
			int countFunctions,
			Double degree,
			Double probability,
			Topology_Network topology_Network) {
		HashMap<Type_Function, Double> fd=new HashMap<>();
		fd.put(Type_Function.R,1.0);
		return buildNetwork(sizeGens, countFunctions, degree, probability, topology_Network, fd);
	}

	public static GeneticNetwork buildNetwork(int sizeGens,
			int countFunctions,
			Double degree,
			Double probability,
			Topology_Network topology_Network,
			HashMap<Type_Function, Double> configFunction) {
		GeneticNetwork rbp = null;
		switch (topology_Network) {
		case SCALE_FREE:// barabasi
			rbp = GeneticNetwork.buildFreeScaleNetwork(sizeGens, countFunctions, degree, probability);
			break;
		case RANDOM: // erdos
			rbp = GeneticNetwork.buildRandomNetwork(sizeGens, countFunctions, degree, probability);
			break;
		case FIXED:
			rbp = GeneticNetwork.buildFixedNetwork(sizeGens, countFunctions, degree, probability);
			break;
		default:
			return null;
		}
		rbp.filterUndependecy();
		rbp.setProbability(probability);
//		rbp.buildLogicalFunctions(countFunctions);
//		rbp.buildCanalizingFunctions(countFunctions, 0.8);
		rbp.boolean_functions = new long[rbp.getPredictors().length][countFunctions][];
		rbp.setType_Functions(new Type_Function[sizeGens]);
		for(int iGene=0;iGene<rbp.getSizeGenes();iGene++) {
			double functionR=Resources.random.nextDouble();
			for (Map.Entry<Type_Function, Double> entry : configFunction.entrySet()) {
			    functionR-=entry.getValue();
			    if(functionR<=0) {
			    	switch (entry.getKey()) {
					case R:
						rbp.buildRandomFunctions(iGene);
						break;
					case C:
						rbp.buildCanalizingFunctionsForGene(iGene);
						break;
					case NC:
						rbp.buildNestedCanalizingFunctionForGene(iGene);
						break;
					case T:
						rbp.buildThresholdFunction(iGene);
						break;
					default:
						break;
					}
			    	rbp.getType_Functions()[iGene]=entry.getKey();
			    	break;
			    }
			}
//			rbp.buildLinearGroupingFunctions(iGene);
//			rbp.buildThresholdFunction(iGene);
			
		}
		return rbp;
	}

	@SuppressWarnings("unchecked")
	protected static GeneticNetwork buildRandomNetwork(int size, int countFunctions, Double degree,
			Double probability) {
		double prob = degree / (size);
		GeneticNetwork rbp = new GeneticNetwork();
		rbp.setTopology(Topology_Network.RANDOM);
		rbp.setPredictors(new TreeSet[size]);
		for (int i = 0; i < size; i++) {
			rbp.getPredictors()[i] = new TreeSet<Integer>();
			for (int j = 0; j < size; j++) {
				// add gene
				if (Resources.random.nextDouble() <= prob) {
					rbp.getPredictors()[i].add(j);
				}
			}
		}
		return rbp;
	}

	@SuppressWarnings("unchecked")
	protected static GeneticNetwork buildFixedNetwork(int size, int countFunctions, Double degree, Double probability) {
		GeneticNetwork rbp = new GeneticNetwork();
		rbp.setTopology(Topology_Network.FIXED);
		rbp.setPredictors(new TreeSet[size]);
		for (int i = 0; i < size; i++) {
			rbp.getPredictors()[i] = new TreeSet<Integer>();
			for (int j = 0; j < degree; j++) {
				// add gene
				if (!rbp.getPredictors()[i].add(Resources.random.nextInt(size)))
					j--;
			}
		}
		// zerar 20 genes
		TreeSet<Integer> z = new TreeSet<>();
		while (z.size() < 10) {
			int g = Resources.random.nextInt(size);
			if (z.add(g))
				rbp.getPredictors()[g].clear();
		}
		return rbp;
	}

	@SuppressWarnings("unchecked")
	protected static GeneticNetwork buildFreeScaleNetwork(int size, int countFunctions, Double degree,
			Double probability) {
		GeneticNetwork rbp = new GeneticNetwork();
		rbp.setTopology(Topology_Network.SCALE_FREE);
		rbp.setPredictors(new TreeSet[size]);
		//
		int[] degrees = new int[size];
		int sumDegrees = 0;
		int sizeBaseNetwork = (int) (degree * 2);
		double prob = (double) degree / (sizeBaseNetwork - 1);
		for (int i = 0; i < sizeBaseNetwork; i++) {
			rbp.getPredictors()[i] = new TreeSet<Integer>();
			for (int j = 0; j < sizeBaseNetwork; j++) {
				// sortear sizepreditores por gene
				if (Resources.compareDouble(Resources.random.nextDouble(), prob) <= 0) {// (resources.random.nextDouble()
																						// <= prob) {
					rbp.getPredictors()[i].add(j);
					degrees[i]++;
					sumDegrees++;
					degrees[j]++;
					sumDegrees++;
				}
			}
		}
		// agregar los restantes genes
		for (int i = sizeBaseNetwork; i < size; i++) {
			rbp.getPredictors()[i] = new TreeSet<>();
			int degreeProv = degree.intValue() + (Resources.random.nextDouble() < (degree - degree.intValue()) ? 1 : 0);
			bucleArco: for (int iesimoArco = 0; iesimoArco < degreeProv; iesimoArco++) {
				prob = Resources.random.nextDouble();
				for (int iGene = 0; iGene < size; iGene++) {
					// verificar la probabilidad
					prob -= ((double) degrees[iGene] / sumDegrees);
					if (Resources.compareDouble(prob, 0) < 0) {// (prob < 0) {
						boolean isAdd;
						if (Resources.random.nextBoolean()) {
							if (!(isAdd = rbp.getPredictors()[i].add(iGene))) {
								isAdd = rbp.getPredictors()[iGene].add(i);
							}
						} else {
							if (!(isAdd = rbp.getPredictors()[iGene].add(i))) {
								isAdd = rbp.getPredictors()[i].add(iGene);
							}
						}
						if (isAdd) {
							degrees[i]++;
							degrees[iGene]++;
							sumDegrees++;
							sumDegrees++;
						} else// ya existe el arco
						{
							iesimoArco--;
						}
						continue bucleArco;
					}
				}
			}
		}
		return rbp;
	}

	public void filterUndependecy() {
		// filtrar genes sin dependencia
		int size = this.predictors.length;
		for (int i = 0; i < size; i++) {
			if (predictors[i].isEmpty()) {
				predictors[i].add(Resources.random.nextInt(size));
				int pe = Resources.random.nextInt(size);
				while (predictors[pe].size() < 2)
					pe = Resources.random.nextInt(size);
				TreeSet<Integer> ped = predictors[pe];
				int igene = Resources.random.nextInt(ped.size());
				int ies = 0;
				for (Integer ivalue : ped) {
					if (ies == igene) {
						ped.remove(ivalue);
						break;
					}
					ies++;
				}
			}
		}
	}

	private void buildRandomFunctions(int iGene) {
//		this.boolean_functions = new long[this.getPredictors().length][countFunctions][];
		int countFunctions = this.boolean_functions[0].length;
		for (int f = 0; f < countFunctions; f++) {
			int countPredictors = this.predictors[iGene].size();
			int sizeAllfunctions = (1 << countPredictors);
			long[] table = new long[(countPredictors > 6) ? (1 << (countPredictors - 6)) : 1];
			for (int t = 0; t < table.length; t++) {
				table[t] = Resources.random.nextLong();
			}
			if (countPredictors < 6) {
				table[0] = ((table[0] >= 0) ? table[0] : -table[0]) % ((long) 1 << (1 << countPredictors));
			}
			if (this.predictors[iGene].size() <= 6 && Resources.isReducible(this.predictors[iGene].size(), table[0])) {
				f--;
				continue;
			}
			if ((existFunction(iGene, table) && !(iGene >= sizeAllfunctions))) {
				f--;
			} else {
				this.boolean_functions[iGene][f] = table;
			}
		}

	}
//	private void buildLinearGroupingFunctions(int iGene) {
////		this.boolean_functions = new long[this.getPredictors().length][countFunctions][];
//		int countPredictors = this.predictors[iGene].size();
//		if(countPredictors<3)
//			buildRandomFunctions(iGene);
//		else {
//			int countFunctions = this.boolean_functions[0].length;
//			LinearGrouping lg=new LinearGrouping(null);
//			this.boolean_functions[iGene]=lg.buildFunctions(countPredictors,countFunctions);
//		}
//	}
	public void buildNestedCanalizingFunctionForGene(int nGene) {
		
	}
	public void buildCanalizingFunctionsForGene(int nGene) {
		int countFunctions = this.boolean_functions[0].length;
		if (!this.predictors[nGene].isEmpty())
			for (int f = 0; f < countFunctions; f++) {
				int countPredictors = this.predictors[nGene].size();
				int sizeAllfunctions = (1 << countPredictors);
				long[] table = new long[(countPredictors > 6) ? (1 << (countPredictors - 6)) : 1];
				// escojer preditor canalizador
				int ican = Resources.random.nextInt(countPredictors);
				boolean canalizador = Resources.random.nextBoolean();
				boolean canalizado = Resources.random.nextBoolean();
				long[] mask = new long[table.length];
				long nbits = 1L << countPredictors;
				long nleng = 1L << ican;
				long d = 0;
				for (int j = 0; j < nbits; j++) {
					if (j % nleng == 0)
						d = (d + 1) % 2;
					mask[j / 64] = mask[j / 64] | (d << (j % 64));

					if (canalizador) {
						if (d == 1) {
							table[j / 64] = table[j / 64] | (((canalizado) ? 1 : 0) << (j % 64));
						} else
							table[j / 64] = table[j / 64] | ((Resources.random.nextInt(2)) << (j % 64));
					} else {
						if (d == 0) {
							table[j / 64] = table[j / 64] | (((canalizado) ? 1 : 0) << (j % 64));
						} else
							table[j / 64] = table[j / 64] | ((Resources.random.nextInt(2)) << (j % 64));
					}

				}
				if (this.predictors[nGene].size() <= 6
						&& Resources.isReducible(this.predictors[nGene].size(), table[0])) {
					f--;
					continue;
				}
				if ((existFunction(nGene, table) && !(nGene >= sizeAllfunctions))) {
					f--;
				} else {
					this.boolean_functions[nGene][f] = table;
				}
			}
	}
//
//	private void buildCanalizingFunctions(int countFunctions, double percentage) {
//		this.boolean_functions = new long[this.getPredictors().length][countFunctions][];
//		for (int i = 0; i < this.predictors.length; i++) {
//			if(Resources.random.nextDouble()<=percentage)
//				buildCanalizingFunctionsForGene(i);
//			else
//				buildRandomFunctions(i);
//		}
//	}
	private double[] hiperplane(int dimention) {
		double[] hiperplane=new double[dimention +1];
		double[] puntoPaso=new double[dimention];
		double[] vetorNormal=new double[dimention];
		double cota=0;
		for (int i = 0; i < vetorNormal.length; i++) {
			puntoPaso[i]= Resources.random.nextDouble();
			vetorNormal[i]= Resources.random.nextDouble()*(
					(Resources.random.nextBoolean())?1:-1);
			cota+=puntoPaso[i]*vetorNormal[i];
		}
		System.arraycopy(vetorNormal, 0, hiperplane, 0, dimention);
		hiperplane[dimention]=cota;
		return hiperplane;
	}
	private boolean clasifiquedByHiperplane(double[] hiperplane, int value) {
		int len=hiperplane.length-1;
		double valClass=0;
		for (int i=0; i<len;i++)
			if(((1<<i)&value)!=0)
				valClass+=hiperplane[i];
		return valClass>=hiperplane[len];
	}
	public long[] thresholdFunction(int dimention) {
		double[] hiperplane=hiperplane(dimention);
		int l=1<<dimention;
		long[] function=new long[(l/64)+((l%64==0)?0:1)];
		boolean p=Resources.random.nextBoolean();
		for(int i=0;i<l;i++) {
			if(p ^ clasifiquedByHiperplane(hiperplane, i))
				function[i/64]=function[i/64]|(1L<<(i%64));
		}
		return function;
	}
	private void buildThresholdFunction(int iGene) {
//		this.boolean_functions = new long[this.getPredictors().length][countFunctions][];
		int countPredictors = this.predictors[iGene].size();
		int countFunctions = this.boolean_functions[0].length;
		int sizeAllfunctions = (1 << countPredictors);
		for (int f = 0; f < countFunctions; f++) {
			long[] table=thresholdFunction(countPredictors);	
			if (countPredictors < 6) {
				table[0] = ((table[0] >= 0) ? table[0] : -table[0]) % ((long) 1 << (1 << countPredictors));
			}
			if (this.predictors[iGene].size() <= 6 && Resources.isReducible(this.predictors[iGene].size(), table[0])) {
				f--;
				continue;
			}
			if ((existFunction(iGene, table) && !(iGene >= sizeAllfunctions))) {
				f--;
			} else {
				this.boolean_functions[iGene][f] = table;
			}
		}
	}
	private boolean existFunction(int indexGene, long[] table) {
		boolean exist = false;
		for (long[] tableGene : this.boolean_functions[indexGene]) {
			if (exist = Arrays.equals(table, tableGene))
				break;
		}
		return exist;
	}
//type_dynamic com ruido y sem ruido Random probabilistic_random
	//withLoops true-aceita atratores
	public BooleanGeneExpressionData createGeneExpressionData(int size,
			int replays,
			Type_Dynamic type_dynamic,
			boolean[] initState,
			boolean withLoops) {
		boolean[][] ged = new boolean[size * replays][];
		// first state
		if (initState != null)
			ged[0] = initState;
		else
			ged[0] = BooleanGeneExpressionData.createRandonState(this.getPredictors().length);

		for (int iReplay = 0; iReplay < replays; iReplay++) {
			int dess = iReplay * size;
			if (dess > 0)
				ged[dess] = BooleanGeneExpressionData.createRandonState(this.getPredictors().length);
			for (int iData = 1; iData < size; iData++) {// predecir para cada
				// epoca
				int posEpoca = dess + iData;
				ged[posEpoca] = makeGeneExpresionState(type_dynamic, ged[posEpoca - 1]);
				if (!withLoops && Resources.existsArray(ged, posEpoca, dess)) {
					return null;
				}
			}
		}
		return new BooleanGeneExpressionData(ged);
	}

	public boolean[] makeGeneExpresionState(Type_Dynamic type_dynamic, boolean[] previunState) {
		// boolean[] nextState = new boolean[previunState.length];
		long[][] functions = new long[previunState.length][];
		// dinamica probabilistica
		for (int nGen = 0; nGen < previunState.length; nGen++) {
			// escoger probabilisticamente la funcion a ejecutar
			int nFunction = 0;
			if (type_dynamic == Type_Dynamic.PROBABILISTIC_BOOLEAN) {
				double prov = Resources.random.nextDouble();
				if (Resources.compareDouble((prov = (prov - this.probability)), 0) > 0) {
					double provF = (1 - this.probability) / (this.boolean_functions[0].length - 1);
					// escojer una tabelaVerdad para evaluar
					while (true) {
						nFunction++;
						if ((prov = (prov - provF)) < 0) {
							break;
						}
					}
				}
			}
			if (this.getBoolean_functions()[nGen] != null)
				functions[nGen] = this.getBoolean_functions()[nGen][nFunction];
		}
		return makeGeneExpresionState(getPredictors(), functions, previunState);
	}

	public BooleanGeneExpressionData makeGeneExpresionData(Type_Dynamic type_dynamic, BooleanGeneExpressionData gedBase) {
		boolean[][] ged = new boolean[gedBase.sizeData][gedBase.sizeGens];
		for (int i = 0; i < ged.length; i++) {
			ged[i] = makeGeneExpresionState(type_dynamic, gedBase.getCopyData(i));
		}
		return new BooleanGeneExpressionData(ged);
	}

	public int[] dinamicGene(int nGene) {
		Integer[] preditors = this.getPredictors()[nGene].toArray(new Integer[0]);
		int[] dinamic = new int[(1 << preditors.length) + 2];
		if (preditors.length > 0) {
			int nBooleanVals0 = (this.predictors[preditors[0]].isEmpty()) ? 2
					: (1 << this.predictors[preditors[0]].size());
			for (int i = 0; i < nBooleanVals0; i++) {
				int ival = i;
				if (!this.predictors[preditors[0]].isEmpty())
					ival = (aplicatedFunction(this.getBoolean_functions()[preditors[0]][0], i) ? 1 : 0);
				int nBooleanVals1 = (this.predictors[preditors[1]].isEmpty()) ? 2
						: (1 << this.predictors[preditors[1]].size());
				for (int j = 0; j < nBooleanVals1; j++) {
					int jval = ival + (j * 2);
					if (!this.predictors[preditors[1]].isEmpty())
						jval = ival + (aplicatedFunction(this.getBoolean_functions()[preditors[1]][0], j) ? 2 : 0);
					int nBooleanVals2 = (this.predictors[preditors[2]].isEmpty()) ? 2
							: (1 << this.predictors[preditors[2]].size());
					for (int k = 0; k < nBooleanVals2; k++) {
						int kval = jval + (k * 4);
						if (!this.predictors[preditors[2]].isEmpty())
							kval = jval + (aplicatedFunction(this.getBoolean_functions()[preditors[2]][0], k) ? 4 : 0);
						dinamic[kval]++;
						dinamic[8 + (aplicatedFunction(this.getBoolean_functions()[preditors[nGene]][0], kval) ? 1
								: 0)]++;
					}
				}
			}
		}
		return dinamic;
	}

	public String stringDinamicGene(int nGene) {
		StringBuilder sb = new StringBuilder("Gene:").append(nGene).append(this.predictors[nGene]);
		int[] dinamic = dinamicGene(nGene);
		sb.append("\n0:").append(dinamic[dinamic.length - 2]).append("\t1:").append(dinamic[dinamic.length - 1]);
		final int npred = this.getPredictors()[nGene].size();
		for (int i = 0; i < dinamic.length - 2; i++) {
			sb.append("\n").append(Resources.toTrueTableFormat(i, npred)).append("\t").append(dinamic[i]);
		}
		return sb.toString();
	}

	public void show() {
		System.out.println(getTopology() + "|" + getPredictors().length + "|" + probability);
		int iGene = 0;
		for (Set<Integer> preditorsGene : this.predictors) {
			StringBuilder line = new StringBuilder();
			line.append(iGene).append("|");
			if (preditorsGene.size() > 0) {
				for (Object gene : preditorsGene) {
					line.append(gene).append(",");
				}
				line.deleteCharAt(line.length() - 1);
				line.append("|");
				for (long[] functionsGene : this.boolean_functions[iGene]) {
					for (Long func : functionsGene) {
						line.append((preditorsGene.size() > 6) ? func : Long.toBinaryString(func)).append(",");
					}
					line.deleteCharAt(line.length() - 1).append(":");
				}
				System.out.println(line.deleteCharAt(line.length() - 1).append("|").append(type_Functions[iGene]));
			} else
				line.append("NN");

			iGene++;
		}
	}

	public void showBoolean() {
		System.out.println(getTopology() + "|" + getPredictors().length);
		int iGene = 0;
		for (Set<Integer> preditorsGene : this.predictors) {
			StringBuilder line = new StringBuilder();
			if (preditorsGene.size() > 0) {
				for (Object gene : preditorsGene) {
					line.append(gene).append(",");
				}
				line.deleteCharAt(line.length() - 1);
			} else
				line.append("NN");
			line.append("|");
			// for (long[] functionsGene : this.boolean_functions[iGene]) {
			if (this.boolean_functions[iGene] != null)

				for (Long func : this.boolean_functions[iGene][0]) {
					line.append((preditorsGene.size() > 6) ? func
							: Resources.toTrueTableFormat(func, 1 << preditorsGene.size()));
				}
			// line.deleteCharAt(line.length() - 1).append(":");
			// }
			System.out.println(line);
			iGene++;
		}
	}

	public static int[] validateFPFNTPTN(AbstractNetwork gabaritoNetwork, AbstractNetwork inferenceNetwork,
			List<Integer> validGens) {
		return validateFPFNTPTN(gabaritoNetwork, inferenceNetwork, 0, "", true, validGens
		// IntStream.range(0,
		// gabaritoNetwork.getSizeGenes()).boxed().collect(Collectors.toList())
		);
	}

	private static boolean testGrau(String test, int grau, int valTest) {
		switch (test) {
		case "eq":
			return (grau == valTest);
		case "ne":
			return (grau != valTest);
		case "me":
			return (grau >= valTest);
		case "le":
			return (grau <= valTest);
		case "m":
			return (grau > valTest);
		case "l":
			return (grau < valTest);
		default:
			break;
		}
		return false;
	}
	public static int[] validateFPFNTPTN(AbstractNetwork originalNetwork, AbstractNetwork testNetwork, int degreeTest,
			String simboloTest, boolean isOriginalNetwork, List<Integer> validGens) {
		int[] FPFNTPTN = new int[] { 0, 0, 0, 0 };
		for (int genInTest : validGens) {
			// verificar filtro
			if (isOriginalNetwork
					&& testGrau(simboloTest, originalNetwork.getPredictorsByGene(genInTest).size(), degreeTest))
				continue;
			if (!isOriginalNetwork
					&& testGrau(simboloTest, testNetwork.getPredictorsByGene(genInTest).size(), degreeTest))
				continue;
			for (int testGen : validGens) {
				boolean genInTestNetwork = testNetwork.getPredictorsByGene(genInTest).contains(testGen);
				boolean genInOriginalNetwork = originalNetwork.getPredictorsByGene(genInTest).contains(testGen);

				if (!genInOriginalNetwork && genInTestNetwork)// FP
				{
					FPFNTPTN[0]++;
				} else if (genInOriginalNetwork && !genInTestNetwork)// FN
				{
					FPFNTPTN[1]++;
				} else if (genInOriginalNetwork && genInTestNetwork)// TP
				{
					FPFNTPTN[2]++;
				} else // (!geneEmGabarito && !geneEmRede) TN
				{
					FPFNTPTN[3]++;
				}
			}
		}
		if (Arrays.equals(FPFNTPTN, new int[] { 0, 0, 0, 0 }))
			return null;
		return FPFNTPTN;
	}

	public double getMeanDegree() {
		double degree = 0;
		for (TreeSet<Integer> pred : predictors) {
			degree += pred.size();
		}
		return degree / predictors.length;
	}
	// gera dados de expressao em base a dados de expresao entrada (i.e. gerar dinamica completa)
	public BooleanGeneExpressionData createGeneExpresionData(BooleanGeneExpressionData gedBase) {
		boolean[][] ged = new boolean[gedBase.sizeData][gedBase.sizeGens];
		for (int i = 0; i < gedBase.sizeData; i++) {
			ged[i] = makeGeneExpresionState(Type_Dynamic.BOOLEAN, gedBase.getCopyData(i));
			
		}
		return new BooleanGeneExpressionData(ged);
	}
	public List<BooleanGeneExpressionData> getAttractors(){
		List<BooleanGeneExpressionData> listAttrator=new ArrayList<>();
		boolean[] visits=new boolean[1<<this.getSizeGenes()];
		int countVisit=0;
		while(countVisit<visits.length) {
			int posNoVistit=Resources.getPosition(visits,false);
			boolean[] init= Resources.toState(posNoVistit, this.getSizeGenes());
			//criar o posivel basia
			List<boolean[]> atrator=new ArrayList<>();
			atrator.add(init);
			visits[posNoVistit]=true;//marco como visitado
			countVisit++;
			while(true) {
				//evouir os dados de expressao
				boolean[] nextState = 
						this.makeGeneExpresionState(Type_Dynamic.BOOLEAN, init);
				int intState=(int)Resources.toLong(nextState);
				if(visits[intState]) {
					//verificar si o state esta dentro do atrator q estou olhanda
					int pos;
					//se ja for visitado e nao pertence ao attrator q estou olhando
					if((pos=Resources.getPosition(atrator, nextState))==-1)
						break; //peretence a alaguma basia
					else {
						//salvar o atrator 
						boolean[][] matrizAtrator=
						atrator
							.subList(pos, atrator.size())
							.toArray(new boolean[atrator.size()-pos][this.getSizeGenes()]);
						listAttrator.add(new BooleanGeneExpressionData(matrizAtrator));
						break;
					}
				}
				else {
					//acrecento ao atrator qu estou aolhando
					atrator.add(nextState);
					//marco como visitado
					visits[intState]=true;
					countVisit++;
					//avancar dentro da geracao de estados
					init=nextState;
				}
			}
		}
		return listAttrator;
	}

	public int[] sizePredictors() {
		int[] sizePredictors = new int[predictors.length];
		for (int iPreditor = 0; iPreditor < sizePredictors.length; iPreditor++) {
			sizePredictors[iPreditor] = predictors[iPreditor].size();
		}
		return sizePredictors;
	}

	public int[] sizePredictors(List<Integer> validGens) {
		int[] sizePredictors = new int[predictors.length];
		for (int iPreditor = 0; iPreditor < sizePredictors.length; iPreditor++) {
			sizePredictors[iPreditor] = 0;
			for (Iterator<Integer> iterator = predictors[iPreditor].iterator(); iterator.hasNext();) {
				Integer pred = iterator.next();
				if (validGens.contains(pred))
					sizePredictors[iPreditor]++;
			}
		}
		return sizePredictors;
	}

	public int[] predictorsVariacao(double variacao, BooleanGeneExpressionData booleanGeneExpressionData) {
		int[] pv = new int[predictors.length];
		double[][] propotionGED = booleanGeneExpressionData.proportionData();
		for (int iGene = 0; iGene < predictors.length; iGene++) {
			pv[iGene] = (propotionGED[iGene][0] >= variacao && propotionGED[iGene][1] >= variacao) ? 1 : 0;
		}
		return pv;
	}

//	@Override
//	public int[][] frequencyTableGene(int nGene, BooleanGeneExpressionData ged) {
//		int[][] table = new int[(1 << predictors[nGene].size())][2];
//		for (int i = 0; i < ged.sizeData; i++) {
//			int pos = possitonTable(predictors[nGene], ged.getData(i));
//			if (boolean_functions[nGene] != null)
//				table[pos][aplicatedFunction(boolean_functions[nGene][0], pos) ? 1 : 0]++;
//			else
//				table[pos][Resources.random.nextInt(2)]++;
//		}
//		return table;
//	}

	@Override
	public Pair<int[][], boolean[]> dinamicGeneDetails(int nGene, BooleanGeneExpressionData ged) {
//		DinamicDetails dd=new DinamicDetails();
		int[][] frequencyTable = new int[(1 << predictors[nGene].size())][2];
		boolean[] prediction = new boolean[ged.sizeData];
		for (int i = 0; i < ged.sizeData; i++) {
			int pos = possitonTable(predictors[nGene], ged.getCopyData(i));
			if (boolean_functions[nGene] != null)
				prediction[i] = aplicatedFunction(boolean_functions[nGene][0], pos);
			else
				prediction[i] = Resources.random.nextBoolean();
			frequencyTable[pos][prediction[i] ? 1 : 0]++;
		}
		return new Pair<>(frequencyTable, prediction);
	}

	@Override
	public long[] getMainFunctionByGene(int idGene) {
		return (boolean_functions[idGene] == null) ? null : boolean_functions[idGene][0];
	}

	@Override
	public int getSizeGenes() {
		return predictors.length;
	}

	@Override
	public int[] getArrayPreditorsByGene(int idGene) {
		int[] p = new int[predictors[idGene].size()];
		int i = 0;
		for (Iterator<Integer> iterator = predictors[idGene].iterator(); iterator.hasNext(); i++) {
			p[i] = iterator.next();
		}
		return p;
	}
	
	public static void main(String[] arg) throws FileNotFoundException {
		if(arg.length==0) {
			HashMap<Type_Function, Double> configFunctions=
					new HashMap<>();
			configFunctions.put(Type_Function.C,0.4);
			configFunctions.put(Type_Function.T,0.4);
			configFunctions.put(Type_Function.R,0.2);
			
			GeneticNetwork gn=GeneticNetwork.buildNetwork(
					10, //nro de genes
					2, //cuantas funcoes por gene sempre é dois
					3.0,//grau medio por gene
					0.98,//probabilidade da funçaão principal
					Topology_Network.SCALE_FREE,//topologya
					configFunctions);
			
			gn.show();
			
			List<BooleanGeneExpressionData> atratores=gn.getAttractors();
			atratores.forEach(o->{
				o.print();
				System.out.println();
			});
			
			//gn.writeInFile("gn");
		}
		
//		else {
//			GeneticNetwork gn=
//					GeneticNetwork.buildNetwork(
//							Integer.parseInt(arg[0]),//10,
//							Integer.parseInt(arg[1]),//2,
//							Double.parseDouble(arg[2]),//3.0,
//							Double.parseDouble(arg[3]),//0.98,
//							Topology_Network.valueOf(arg[4]));// Topology_Network.RANDOM);
//			gn.writeInFile(arg[5]);
//		}
//		
	}

}
