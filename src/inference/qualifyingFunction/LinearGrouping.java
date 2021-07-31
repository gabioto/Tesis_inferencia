package inference.qualifyingFunction;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import resources.Resources;

public class LinearGrouping extends AbstractGrouping {
	public static Integer keyGruopDiscrete(int sizePredictors,int sizeDiscrete) {
		return sizePredictors*10 +sizeDiscrete;	
	}
	public static final ConcurrentHashMap<Integer, int[][][]> groupsDiscrete=new ConcurrentHashMap<>();
	
//	public static final int[] coefients = { -2, -1, 1, 2 };
	public static final int[] coefients = { 2,4 };

	public LinearGrouping(EvaluationMetric evaluationMetric) {
		super(evaluationMetric);
	}

	@Override
	public Details quantifyDetails(NavigableSet<Integer> preditors, GeneExpressionData geneExpressionData, Integer geneTarget) {
		int key= keyGruopDiscrete(preditors.size(), geneExpressionData.sizeDiscreteValues());
		if (preditors.size() > 1 && !LinearGrouping.groupsDiscrete.containsKey(key)) {
//			System.out.println("inB "+ key);
				LinearGrouping.buildGroups(preditors.size(), geneExpressionData.sizeDiscreteValues());
//			System.out.println("outB "+ key);
		}
		return super.quantifyDetails(preditors, geneExpressionData, geneTarget);
	}
	@Override
	public Double quantifyDiscrete(NavigableSet<Integer> preditors, GeneExpressionData gdd, Integer geneTarget) {
		int key= keyGruopDiscrete(preditors.size(), gdd.sizeDiscreteValues());
		if (preditors.size() > 1 && !LinearGrouping.groupsDiscrete.containsKey(key)) {
//			System.out.println("inD "+ key);
			LinearGrouping.buildGroups(preditors.size(), gdd.sizeDiscreteValues());
//			System.out.println("outD "+ key);
		}
		return super.quantifyDiscrete(preditors, gdd, geneTarget);
	}
	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int sizePreditors) {
		int key= keyGruopDiscrete(sizePreditors, frequencyTable[0].length);
		if (sizePreditors > 1 && !LinearGrouping.groupsDiscrete.containsKey(key)) {
			LinearGrouping.buildGroups(sizePreditors, frequencyTable[0].length);
		}
		aplicatedGroups = LinearGrouping.groupsDiscrete.get(key);
		return buildTablesGruping(frequencyTable, sizePreditors);
	}

//	private int[][] buildCoefficientGroup(int sizePredictors) {
//		int nroCoef = 1 << (sizePredictors - 1);// 2 al nro de predites -1
////		int nroCoef = 1 << sizePredictors;// 2 al nro de predites 
//		int[][] coeficientes = new int[nroCoef][sizePredictors];
//		for (int i = 0; i < nroCoef; i++) {
//			int resto = i;
//			for (int j = 0; j < sizePredictors; j++) {
//				coeficientes[i][j] = (resto % 2 == 0) ? 1 : -1;
//				resto /= 2;
//			}
//		}
//		return coeficientes;
//	}

	private static int[][] buildCoefficientGroup(int sizePredictors) {
		int base = coefients.length;
		int nroCoef = (int) Math.pow(base, sizePredictors);
//		int nroCoef = 1 << sizePredictors;// 2 al nro de predites 
		int[][] coeficientes = new int[nroCoef][sizePredictors];
		for (int i = 0; i < nroCoef; i++) {
			int resto = i;
			for (int j = 0; j < sizePredictors; j++) {
				coeficientes[i][j] = coefients[resto % base];
				// coeficientes[i][j] = (resto % 2 == 0) ? 1 : -1;
				if (resto < base)
					coeficientes[i][j] = coefients[resto];
				resto /= base;
			}
		}
		return coeficientes;
	}

	/* int[#de agrupacion][#nro de grupo][integrentes del grupo] */
//	protected Map<Integer, TreeSet<Integer>>[] buildMapGroups(int sizePredictors) {
//		// definir coeficientes
//		int[][] coefficientGroup = buildCoefficientGroup(sizePredictors);
//		Map<Integer, TreeSet<Integer>>[] groups = new Map[coefficientGroup.length];
//		for (int iGroup = 0; iGroup < coefficientGroup.length; iGroup++) {
//			// indices de grupos
//			groups[iGroup]= new TreeMap<>();
//			int[] coefficient = coefficientGroup[iGroup];
//			
//			for (int i = 0; i < (1<<sizePredictors); i++) {
//				int val = 0;
//				for (int j = 0; j < coefficient.length; j++) {
//					val += (((1 << j) & (i)) != 0) ? coefficient[j] : 0;
//				}
//				if(!groups[iGroup].containsKey(val))
//					groups[iGroup].put(val, new TreeSet<>());
//				groups[iGroup].get(val).add(i);			
//			}
//		}
//	
//		return groups;
//	}
//	protected static HashSet<TreeMap<Integer, TreeSet<Integer>>> 
//		buildSetGroups(int sizePredictors,int sizeDiscrete) {
//		// definir coeficientes
//		int[][] coefficientGroup = buildCoefficientGroup(sizePredictors);
//		HashSet<TreeMap<Integer, TreeSet<Integer>>> groups = new HashSet<>();
//		HashSet<HashSet<TreeSet<Integer>>> groupsSet = new HashSet<>();
//		for (int iGroup = 0; iGroup < coefficientGroup.length; iGroup++) {
//			// indices de grupos
//			TreeMap<Integer, TreeSet<Integer>> tm = new TreeMap<>();
//			int[] coefficient = coefficientGroup[iGroup];
//			int nStates = Resources.pow(sizeDiscrete, sizePredictors);
//			for (int i = 0; i < nStates; i++) {
//				int val = 0;
//				for (int j = 0; j < coefficient.length; j++) {
//					val += (((1 << j) & (i)) != 0) ? coefficient[j] : 0;
//				}
//				if (!tm.containsKey(val))
//					tm.put(val, new TreeSet<>());
//				tm.get(val).add(i);
//			}
//
//			HashSet<TreeSet<Integer>> hs = new HashSet<TreeSet<Integer>>();
//			hs.addAll(tm.values());
//			if (groupsSet.add(hs))
//				groups.add(tm);
//		}
//		return groups;
//	}

	protected static HashSet<TreeMap<Integer, TreeSet<Integer>>> buildSetGroups(int sizePredictors, int sizeDiscretes) {
		// definir coeficientes
		int[][] coefficientGroup = buildCoefficientGroup(sizePredictors);
		HashSet<TreeMap<Integer, TreeSet<Integer>>> groups = new HashSet<>();
		HashSet<HashSet<TreeSet<Integer>>> groupsSet = new HashSet<>();
		int[] nStatesBase = Resources.statesBase(sizeDiscretes, sizePredictors);
		int nStates = nStatesBase.length;
		for (int iGroup = 0; iGroup < coefficientGroup.length; iGroup++) {
			// indices de grupos
			TreeMap<Integer, TreeSet<Integer>> tm = new TreeMap<>();
			int[] coefficient = coefficientGroup[iGroup];
			for (int i = 0; i < nStates; i++) {
				int val = 0;
				for (int j = 0; j < coefficient.length; j++) {
					int d = Resources.pow(10, j);
					val += coefficient[j] * ((nStatesBase[i] / d) % 10);
				}
				if (!tm.containsKey(val))
					tm.put(val, new TreeSet<>());
				tm.get(val).add(i);
			}

			HashSet<TreeSet<Integer>> hs = new HashSet<TreeSet<Integer>>();
			hs.addAll(tm.values());
			if (groupsSet.add(hs))
				groups.add(tm);
		}
		return groups;
	}
//	protected int[][][] buildGroups(int sizePredictors) {
//		// definir coeficientes
//		int[][] coefficientGroup = buildCoefficientGroup(sizePredictors);
//		int[][][] groups = new int[coefficientGroup.length][sizePredictors + 1][];
////		int[][][] groups = new int[1 << sizePredictors][sizePredictors + 1][];
//		// iniciar grupos
//		for (int i = 0; i < groups.length; i++) {
//			for (int j = 0; j < groups[i].length; j++) {
//				groups[i][j] = new int[(int) Resources.binomial(sizePredictors, j)];
//			}
//		}
//		
//
//		for (int iGroup = 0; iGroup < coefficientGroup.length; iGroup++) {
//			// indices de grupos
//			int[] indexGroup = new int[sizePredictors + 1];
//			// recuperar coeficientes
//			int[] coefficient = coefficientGroup[iGroup];
//			// calcular el valor del primero grupo para desplasar a la posicion
//			// 0 en el arreglo
//			int base = 0;
//			for (int i = 0; i < coefficient.length; i++) {
//				base += (coefficient[i] < 0) ? coefficient[i] : 0;
//			}
//			// evaluar y colocar cada indice en su lugar
//			int sizeTable = 1 << sizePredictors;
//			for (int i = 0; i < sizeTable; i++) {
//				int val = 0;
//				for (int j = 0; j < coefficient.length; j++) {
//					val += (((1 << j) & (i)) != 0) ? coefficient[j] : 0;
//				}
//				groups[iGroup][val - base][indexGroup[val - base]] = i;
//				indexGroup[val - base]++;
//			}
//		}
//		return groups;
//	}

	protected static int[][][] buildGroups(int sizePredictors, int sizeDiscretes) {
		// definir coeficientes
		synchronized (LinearGrouping.groupsDiscrete) {
			int key=keyGruopDiscrete(sizePredictors, sizeDiscretes);
			if (!LinearGrouping.groupsDiscrete.containsKey(key)){
				HashSet<TreeMap<Integer, TreeSet<Integer>>> setGroups = buildSetGroups(sizePredictors, sizeDiscretes);
				int[][][] groups = new int[setGroups.size()][][];
				int i = 0;
				for (TreeMap<Integer, TreeSet<Integer>> setGroup : setGroups) {
					groups[i] = new int[setGroup.size()][];
					int j = 0;
					for (Entry<Integer, TreeSet<Integer>> group : setGroup.entrySet()) {
						groups[i][j] = new int[group.getValue().size()];
						int z = 0;
						for (Integer e : group.getValue()) {
							groups[i][j][z++] = e;
						}
						j++;
					}
					i++;
				}
//				System.out.println("put "+key);
				LinearGrouping.groupsDiscrete.put(key, groups);
				return groups;
			}
			else
				return LinearGrouping.groupsDiscrete.get(key);
		}
	}

	@Override
	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		int key=keyGruopDiscrete(sizePreditors, frequencyTable[0].length);
		int[][][] groups=LinearGrouping.groupsDiscrete.get(key);
		if(groups==null)
			System.out.println("=======tgB "+key);
		int[][][] tables = new int[groups.length][][];
		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
			tables[iGrouping] = new int[groups[iGrouping].length][];
			for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
				tables[iGrouping][iGroup] = new int[frequencyTable[0].length];
				for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
					for (int iDiscrete = 0; iDiscrete < frequencyTable[0].length; iDiscrete++) {
						tables[iGrouping][iGroup][iDiscrete] += frequencyTable[groups[iGrouping][iGroup][iElement]][iDiscrete];
						//tables[iGrouping][iGroup][1] += frequencyTable[groups[iGrouping][iGroup][iElement]][1];
					}
				}
			}
		}
		// aplicatedGroups=groups;
		return tables;
	}
	@Override
	protected int[][][] buildTablesGrupingDiscret(int[][] frequencyTable, int sizePreditors) {
		int key=keyGruopDiscrete(sizePreditors, frequencyTable[0].length);
		int[][][] groups=LinearGrouping.groupsDiscrete.get(key);
		if(groups==null)
			System.out.println("=======tgD "+key);
		int[][][] tables = new int[groups.length][][];
		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
			tables[iGrouping] = new int[groups[iGrouping].length][];
			for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
				tables[iGrouping][iGroup] = new int[frequencyTable[0].length];
				for (int iElement = 0; iElement < groups[iGrouping][iGroup].length; iElement++) {
					for (int iDiscrete = 0; iDiscrete < frequencyTable[0].length; iDiscrete++) {
						tables[iGrouping][iGroup][iDiscrete] += frequencyTable[groups[iGrouping][iGroup][iElement]][iDiscrete];
						//tables[iGrouping][iGroup][1] += frequencyTable[groups[iGrouping][iGroup][iElement]][1];
					}
				}
			}
		}
		// aplicatedGroups=groups;
		return tables;
	}
	public long functionGroupLD(int[][] group, int nFunction) {
		long val = 0L;
		for (int i = 0; i < group.length; i++) {
			if (((1 << i) & nFunction) != 0) {
				for (int j = 0; j < group[i].length; j++)
					val = val | (1L << group[i][j]);
			}
		}
		return val;
	}

	public long[] functionGroup(TreeMap<Integer, TreeSet<Integer>> group, int nFunction, int countPreditors) {
		long[] val = new long[1 << ((countPreditors <= 6) ? 0 : countPreditors - 6)];
		int i = 0;
		for (TreeSet<Integer> lineGroup : group.values()) {
			if (((1 << i) & nFunction) != 0) {
				for (Integer v : lineGroup) {
					long pos = 1L << (v % 64);
					val[v / 64] = val[v / 64] | pos;
				}
			}
			i++;
		}
		return val;
	}

	@Override
	public String getName() {
		return "Agrupamento Linear";
	}

	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		return ID_QUALIFYING_FUNCTION.LG;
	}

	public static void main(String[] arg) {
//		LinearGrouping lg = new LinearGrouping(null);
//		long[][] testFunctions=lg.buildFunctions(8, 2);
//		for(int i=0;i<2;i++)
//			System.out.println(Resources.toTrueTableFormat(testFunctions[i], (1<<7)));
		int[] nPs= IntStream.rangeClosed(2, 10).toArray();
		for (int nP:nPs) {
		int[][] cg = LinearGrouping.buildCoefficientGroup(nP);
//		System.out.println(cg.length);
//		for (int[] is : cg) {
//			System.out.println(Arrays.toString(is));
//		}
//		System.out.println();
		HashSet<TreeMap<Integer, TreeSet<Integer>>> groups = LinearGrouping.buildSetGroups(nP,2);
//		System.out.println(Arrays.deepToString(groups));
		Set<Integer> sizesG=new HashSet<>();
		for (TreeMap<Integer, TreeSet<Integer>> hashSet : groups) {
			sizesG.add(hashSet.size());
//			System.out.println(hashSet.toString());
		}
		
		System.out.println(nP+"\t"+cg.length+"\t"+sizesG+"\t"+ groups.size());
		}
//		SizesPreditorsDiscretes sizes=new SizesPreditorsDiscretes(nP, 3);
//		LinearGrouping.buildGroups(sizes);
//		int[][][] ga = LinearGrouping.groupsDiscrete.get(sizes);
//		System.out.println(ga.length);
//		for (int[][] is : ga) {
//			System.out.println(Arrays.deepToString(is));
//		}
//		int[][] ft=new int[][] {
//			{2,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,0,1},
//			{0,5,1}
//		
//		};
////		System.out.println(Resources.arrayToString(Resources.mergeTable(ft),"\n"));
//		lg.groups=ga;
//		int[][][] gall=lg.buildTablesGruping(ft, 3);
//		System.out.println();
//		for (int[][] is : gall) {
//			System.out.println(Arrays.deepToString(is));
//		}
////		TreeSet<Long> tf=lg.thFunctions(nP);
////		System.out.println();
////		System.out.println(tf.size());
////		System.out.println();
//////		for (Long l : tf) {
//////			System.out.println(Resources.toTrueTableFormat(l, (1<<nP)));
//////		}
////		TreeSet<Long> af=lg.allFunctions(nP);
////		System.out.println();
////		System.out.println(af.size());
////		System.out.println();
//////		for (Long l : af) {
//////			System.out.println(Resources.toTrueTableFormat(l, (1<<nP)));
//////		}
//////		for(int g=0;g<groups.length;g++) {
//////			System.out.println(groups[g].toString());
//////		}
//////		System.out.println(groups[0].equals(groups[groups.length-1]));
////		// for(int i=0;i<(1<<(nP+1));i++) {
//////			long f=lg.functionGroup(groups[g], i)[0];
//////			System.out.println(Resources.toTrueTableFormat(f, (1<<nP))+"\t"+f+"\t"+Resources.isReducible(nP,f));
////		}
//		System.out.println();
//		}
	}

	public long[][] buildFunctions(int countPredictors, int countFunctions) {
		HashSet<TreeMap<Integer, TreeSet<Integer>>> groups = buildSetGroups(countPredictors, 2);
		long[][] functions = new long[countFunctions][];
		for (int i = 0; i < countFunctions; i++) {
			TreeMap<Integer, TreeSet<Integer>> group = Resources.choice(groups, Resources.random);
			int ifunction = Resources.random.nextInt((1 << group.size()) - 2) + 1;
			functions[i] = functionGroup(group, ifunction, countFunctions);
		}
		return functions;
	}

	public TreeSet<Long> thFunctions(int countPredictors) {
//		int[][][] groups = buildGroups(countPredictors);
		int[][][] groups = LinearGrouping.groupsDiscrete.get(keyGruopDiscrete(countPredictors, 2));
		TreeSet<Long> functions = new TreeSet<>();
		for (int[][] is : groups) {
			for (int i = 0; i < is.length; i++) {
				long[] fg = thFunction(is, i);
				for (int j = 0; j < fg.length; j++) {
					functions.add(fg[j]);
				}
			}
		}
		return functions;
	}

	public TreeSet<Long> allFunctions(int countPredictors) {
//		int[][][] groups = buildGroups(countPredictors);
		int[][][] groups = LinearGrouping.groupsDiscrete.get(keyGruopDiscrete(countPredictors, 2));
		TreeSet<Long> functions = new TreeSet<>();
		for (int[][] is : groups) {
			for (int i = 0; i < is.length; i++) {
				long[] fg = allFunction(is);
				for (int j = 0; j < fg.length; j++) {
					functions.add(fg[j]);
				}
			}
		}
		return functions;
	}

	private long[] allFunction(int[][] is) {
		int nFunctions = 1 << is.length;
		long[] funcitons = new long[nFunctions - 2];
		for (int i = 1; i < nFunctions - 2; i++) {
			funcitons[i - 1] = functionGroupLD(is, i);
		}
		return funcitons;
	}

	private long[] thFunction(int[][] group, int index) {
		long[] l = { 0L, 0L, 0L, 0L };
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < group[i].length; j++) {
				l[0] = l[0] | (1L << group[i][j]);
			}
		}
		for (int i = index; i < group.length; i++) {
			for (int j = 0; j < group[i].length; j++) {
				l[1] = l[1] | (1L << group[i][j]);
			}
		}
		for (int i = 0; i <= index; i++) {
			for (int j = 0; j < group[i].length; j++) {
				l[2] = l[2] | (1L << group[i][j]);
			}
		}
		for (int i = index + 1; i < group.length; i++) {
			for (int j = 0; j < group[i].length; j++) {
				l[3] = l[3] | (1L << group[i][j]);
			}
		}
		return l;
	}
}

//@Override
//public final void buildGroupingDetails(Details details) {
//	// contruir indices de grupos
//	if (sizeGroup != details.preditors.size()) {
//		sizeGroup = details.preditors.size();
//		if (sizeGroup > 1) {
//			groups = buildGroups(details.preditors.size());
//		}
//	}
//	if (details.preditors.size() <= 1)
//		return;
//	double quantifyValue = -1;
//	// verificar grupos con la mayor funcion criterio
//	// guardar la entropia de X
//	details.entropyX = Resources.entropyLine(Resources.mergeTable(details.frequencyTable[0]));
//	int[][][] tables = buildSaveTablesGruping(details.frequencyTable[0], details.preditors.size());
//	ArrayList<Integer> indexGrouping = new ArrayList<>();
//	for (int i = 0; i < tables.length; i++) {
//		double testQuantify = evaluationMetric.quantifyFromFrequencyTable(tables[i]);
//		if (Resources.compareDouble(testQuantify, quantifyValue) > 0) {// (testQuantify > quantifyValue) {
//			quantifyValue = testQuantify;
//			indexGrouping.clear();
//			indexGrouping.add(i);
//		} else if (Resources.compareDouble(testQuantify, quantifyValue) == 0) {// (testQuantify > quantifyValue) {
//			indexGrouping.add(i);
//		}
//	}
//
//	// guardar la configuracion de la tabla, la tabla de frequencias y las funciones
//	int[][][] fequencyTablesAux = new int[indexGrouping.size()][][];
//	int[][][] groupsFAux = new int[fequencyTablesAux.length][][];
//	long[][][] functionsAux = new long[fequencyTablesAux.length][][];
//	for (int i = 0; i < groupsFAux.length; i++) {
//		fequencyTablesAux[i] = tables[indexGrouping.get(i)];
//		groupsFAux[i] = aplicatedGroups[indexGrouping.get(i)];
//		functionsAux[i] = buildFunctions(details.preditors.size(), fequencyTablesAux[i], groupsFAux[i]);
//	}
//	// filtrar las funciones
//	ArrayList<Integer> indexFilters = new ArrayList<>();
//	boolean isReducible = true;
//	int maxfunction = Integer.MAX_VALUE;
//	for (int i = 0; i < groupsFAux.length; i++) {
//		if (functionsAux[i].length > 0)
//			isReducible = false;
//		if (isReducible)
//			indexFilters.add(i);
//		else {
//			if (functionsAux[i].length > 0) {
//				if (functionsAux[i].length < maxfunction) {
//					maxfunction = functionsAux[i].length;
//					indexFilters.clear();
//					indexFilters.add(i);
//				} else if (functionsAux[i].length == maxfunction)
//					indexFilters.add(i);
//			}
//		}
//	}
//	int[][][] fequencyTables = new int[indexFilters.size()][][];
//	int[][][] groupsF = new int[fequencyTables.length][][];
//	long[][][] functions = new long[fequencyTables.length][][];
//	for (int i = 0; i < groupsF.length; i++) {
//		fequencyTables[i] = fequencyTablesAux[indexFilters.get(i)];
//		groupsF[i] = groupsFAux[indexFilters.get(i)];
//		functions[i] = functionsAux[indexFilters.get(i)];
//	}
//	details.frequencyTable = fequencyTables;
//	details.groupsTable = groupsF;
//	details.functions = functions;
//	details.isReductora = isReducible;
//}

//private long[][] buildFunctions(int nPreditors, int[][] frequencyTable, int[][] groupTable) {
//	// construir funciones booleanas
//	ArrayList<long[]> booleanTables = new ArrayList<>();
//	int nf = (1 << nPreditors) / 64 + 1;
//	booleanTables.add(new long[nf]);
//	for (int i = 0; i < frequencyTable.length; i++) {
//		if ((frequencyTable[i][1] > frequencyTable[i][0])) {
//			for (long[] bt : booleanTables) {
//				for (int j = 0; j < groupTable[i].length; j++) {
//					int index = groupTable[i][j];
//					bt[index / 64] = bt[index / 64] | ((long) 1 << index);
//				}
//			}
//		} else if (frequencyTable[i][1] == frequencyTable[i][0]) {
//			int lbt = booleanTables.size();
//			for (int j = 0; j < lbt; j++) {
//				long[] bt = Arrays.copyOf(booleanTables.get(j), nf);
//				for (int k = 0; k < groupTable[i].length; k++) {
//					int index = groupTable[i][k];
//					bt[index / 64] = bt[index / 64] | ((long) 1 << index);
//				}
//				booleanTables.add(bt);
//			}
//		}
//	}
//
//	// verificar funciones reducibles y asignar peso
//	for (int j = 0; j < booleanTables.size(); j++) {
//		if (booleanTables.get(j).length == 1)
//			if (Resources.isReducible(nPreditors, booleanTables.get(j)[0])) {
//				booleanTables.remove(j);
//				j--;
//			}
//	}
//	long[][] functions = new long[booleanTables.size()][];
//	for (int i = 0; i < booleanTables.size(); i++) {
//		functions[i] = booleanTables.get(i);
//	}
//	// details.isReductora= (booleanTables.size()==0);
//	return functions;
//}
//
