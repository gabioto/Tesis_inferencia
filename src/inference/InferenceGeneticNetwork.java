package inference;

public class InferenceGeneticNetwork {
	//private Resources resources=new Resources();
//	private QualifyingFunction qualifyingFunction;
//	private SearchAlgoritm searchAlgoritm;
//
//	public InferenceGeneticNetwork(QualifyingFunction qualifyingFunction, SearchAlgoritm searchAlgoritm) {
//		this.qualifyingFunction = qualifyingFunction;
//		this.searchAlgoritm = searchAlgoritm;
//	}
//	public NetworkInfered inferenceNetwork(BooleanGeneExpressionData geneExpressionData) {
//		NetworkInfered gn = new NetworkInfered();
//		gn.setEvaluationMetric(qualifyingFunction.getEvaluationMetric().toShortString());
//		gn.setQualifyingFunction(qualifyingFunction.getShortName());
//		gn.setSearchAlgoritm(searchAlgoritm.getShortName());
//		
//		int sizeGenes=geneExpressionData.getSizeGens();
//		gn.setSizeSamples(geneExpressionData.getSizeData());
//		@SuppressWarnings("unchecked")
//		TreeSet<Integer>[] preditors = new TreeSet[sizeGenes];
//		Long[][] boolean_functions = new Long[sizeGenes][];
//		Long[][] state_functions = new Long[sizeGenes][];
//		for (int i = 0; i < sizeGenes; i++) {
//			ArrayList<Details> listDetails = searchAlgoritm.searchPreditorsDetails(geneExpressionData, i,
//					qualifyingFunction);
//			int iRandomPred = (listDetails.size() > 1) ? resources.random.nextInt(listDetails.size()) : 0;
//			preditors[i] = listDetails.get(iRandomPred).preditors;
//			boolean_functions[i] =new Long[] {0L};
//			state_functions[i] =new Long[] {0L};
//		}
//		gn.setPredictors(preditors);
//		gn.setBoolean_functions(boolean_functions);
//		gn.setState_functions(state_functions);
//		gn.setCritery_function_values(new double[sizeGenes]);
//		gn.setGlobal_boolean_values(new int[sizeGenes][2]);
//		return gn;
//	}
//	
//	public GeneticNetwork inferenceTopologyNetwork(BooleanGeneExpressionData geneExpressionData) {
//		GeneticNetwork gn = new GeneticNetwork();
//	//	gn.setTopology(Topology_Network.INDETERMINATE);
//		@SuppressWarnings("unchecked")
//		TreeSet<Integer>[] preditors = new TreeSet[geneExpressionData.getSizeGens()];
//		Long[][][] boolean_functions = new Long[geneExpressionData.getSizeData()][1][];
//		for (int i = 0; i < preditors.length; i++) {
////			System.out.println("Pred ini"+i);
//			ArrayList<Details> listDetails = searchAlgoritm.searchPreditorsDetails(geneExpressionData, i,
//					qualifyingFunction);
////			System.out.println("Pred fin"+i);
//			int iRandomPred = (listDetails.size() > 1) ? resources.random.nextInt(listDetails.size()) : 0;
//			preditors[i] = listDetails.get(iRandomPred).preditors;
//			
//			boolean_functions[i][0] =new Long[] {1L};
//		}
//		gn.setPredictors(preditors);
//		gn.setProbability(1.0);
//		gn.setBoolean_functions(boolean_functions);
//		return gn;
//	}
//	public void inferenceGeneNetworkV(BooleanGeneExpressionData geneExpressionData, int gene) {
//		@SuppressWarnings("unchecked")
//		TreeSet<Integer>[] preditors = new TreeSet[geneExpressionData.getSizeGens()];
//			ArrayList<Details> listDetails = searchAlgoritm.searchPreditorsDetails(geneExpressionData, gene,
//					qualifyingFunction);
//			int iRandomPred = (listDetails.size() > 1) ? resources.random.nextInt(listDetails.size()) : 0;
//			preditors[gene] = listDetails.get(iRandomPred).preditors;
//	}
//	
//	public void buildNetworkFunctions(GeneticNetwork gn,BooleanGeneExpressionData geneExpressionData) {
//		Long[][][] boolean_functions = new Long[geneExpressionData.getSizeGens()][1][];
//		for (int i = 0; i < gn.getPredictors().length; i++) {
//			Details details= qualifyingFunction.quantifyDetails(gn.getPredictors()[i], geneExpressionData, i);
//			int[][] ft=details.frequencyTable;
//			int nlinhas=1 << details.preditors.size();
//			boolean_functions[i][0] =(nlinhas==ft.length)?buildBooleanFunction(ft): buildBooleanFunction(ft,details.groupsTable, nlinhas);
//		}
//		gn.setBoolean_functions(boolean_functions);
//	}
//	public void buildNetworkFunctions(NetworkInfered networkInfered,BooleanGeneExpressionData geneExpressionData) {
//		for (int i = 0; i < networkInfered.getPredictors().length; i++) {
//			Details details= qualifyingFunction.quantifyDetails(networkInfered.getPredictors()[i], geneExpressionData, i);
//			int[][] ft=details.frequencyTable;
//			int nlinhas=1 << details.preditors.size();
//			if(nlinhas==ft.length)
//				buildBooleanFunction(networkInfered,i,ft);
//			else
//				buildBooleanFunction(networkInfered,i,ft,details.groupsTable, nlinhas);
//		}
//	}
//	//corregir funcion booleana
//	public GeneticNetwork reInferenceNetwork(BooleanGeneExpressionData geneExpressionData, GeneticNetwork inferedNetwork) {
//		GeneticNetwork gn = new GeneticNetwork();
//		//gn.setTopology(Topology_Network.INDETERMINATE);
//		TreeSet<Integer>[] preditors = inferedNetwork.getPredictors();
//		Long[][][] boolean_functions = new Long[geneExpressionData.getSizeGens()][1][];
//		for (int i = 0; i < preditors.length; i++) {
//			Details details=qualifyingFunction.quantifyDetails(preditors[i], geneExpressionData, i);
//			int[][] ft=details.frequencyTable;
//			int nlinhas=1 << preditors[i].size();
//			boolean_functions[i][0] =(nlinhas==ft.length)?buildBooleanFunction(ft): buildBooleanFunction(ft,details.groupsTable, nlinhas);
//		}
//		gn.setPredictors(preditors);
//		gn.setProbability(1.0);
//		gn.setBoolean_functions(boolean_functions);
//		return gn;
//	}
//	public NetworkInfered reInferenceNetwork2(BooleanGeneExpressionData geneExpressionData, GeneticNetwork inferedNetwork) {
//		NetworkInfered networkInfered = new NetworkInfered();
//		networkInfered.setEvaluationMetric(qualifyingFunction.getEvaluationMetric().toShortString());
//		networkInfered.setQualifyingFunction(qualifyingFunction.getShortName());
//		networkInfered.setSearchAlgoritm(searchAlgoritm.getShortName());
//		
//		int sizeGenes=geneExpressionData.getSizeGens();
//		networkInfered.setSizeSamples(geneExpressionData.getSizeData());
//		
//		TreeSet<Integer>[] preditors = inferedNetwork.getPredictors();
//		networkInfered.setPredictors(preditors);
//		Long[][] boolean_functions = new Long[sizeGenes][];
//		networkInfered.setBoolean_functions(boolean_functions);
//		networkInfered.setState_functions(new Long[sizeGenes][]);
//		networkInfered.setState_notObserved(new Long[sizeGenes][]);
//		networkInfered.setGlobal_boolean_values(new int[sizeGenes][2]);
//		networkInfered.setCritery_function_values(new double[sizeGenes]);
//		for (int i = 0; i < preditors.length; i++) {
//			Details details=qualifyingFunction.quantifyDetails(preditors[i], geneExpressionData, i);
//			int[][] ft=details.frequencyTable;
//			networkInfered.getCritery_function_values()[i]=details.qualityValue;
//			int nlinhas=1 << preditors[i].size();
//			//if(nlinhas==ft.length)
//			if(qualifyingFunction.getShortName().equals("SA") || preditors[i].size()<2)
//				buildBooleanFunction(networkInfered,i,ft);
//			else
//				buildBooleanFunction(networkInfered,i,ft,details.groupsTable, nlinhas);
//		}
//		return networkInfered;
//	}
//
//	private Long[] buildBooleanFunction(int[][] frequencyTable) {
//		Long[] booleanTable = new Long[frequencyTable.length / 64 + 1];
//		int[] fa=resources.sumFrequency(frequencyTable);
//		boolean isOne=(fa[1]>fa[0])?true:((fa[1]==fa[0])?resources.random.nextBoolean():false);
//		for (int i = 0; i < booleanTable.length; i++) {
//			booleanTable[i]=0L;
//		}
//		for (int i = 0; i < frequencyTable.length; i++) {
//			if ((frequencyTable[i][1] > frequencyTable[i][0])||((frequencyTable[i][1] == frequencyTable[i][0])&&isOne))
//				booleanTable[i / 64] = booleanTable[i / 64] | ((long)1<< i);
//		}
//		return booleanTable;
//	}
//	private void buildBooleanFunction(NetworkInfered ni, int gene , int[][] frequencyTable) {	
//		Long[] booleanTable = ni.getBoolean_functions()[gene] = new Long[frequencyTable.length / 64 + 1];
//		Long[] stateTable = ni.getState_functions()[gene] = new Long[frequencyTable.length / 64 + 1];
//		Long[] stateZ = ni.getState_notObserved()[gene] = new Long[frequencyTable.length / 64 + 1];
//		ni.getGlobal_boolean_values()[gene]=resources.sumFrequency(frequencyTable);
//		//boolean isOne=(fa[1]>fa[0])?true:((fa[1]==fa[0])?resources.random.nextBoolean():false);
//		for (int i = 0; i < booleanTable.length; i++) {
//			booleanTable[i]=0L;
//			stateTable[i]=-1L;
//			stateZ[i]=-1L;
//		}
//		for (int i = 0; i < frequencyTable.length; i++) {
//			if ((frequencyTable[i][1] > frequencyTable[i][0]))
//				booleanTable[i / 64] = booleanTable[i / 64] | ((long)1<< i);
//			else if(frequencyTable[i][1] == frequencyTable[i][0]) {
//				stateTable[i / 64] = stateTable[i / 64] ^ ((long)1<< i);
//				if(frequencyTable[i][1] ==0)
//					stateZ[i / 64] = stateZ[i / 64] ^ ((long)1<< i);
//		}
//		}
//	}
//	/* frequencitable: tabla agrupada
//	 * grouptable: grupos por linea
//	 * sizetable nro de lineas de la tabla de frecuencia */
//	public void buildBooleanFunction(NetworkInfered ni, int gene,int[][] frequencyTable, int[][]groupTable, int sizeTable) {
//		if (sizeTable==1) buildBooleanFunction(ni,gene,frequencyTable);
//		Long[] booleanTable = ni.getBoolean_functions()[gene] = new Long[sizeTable / 64 + 1];
//		Long[] stateTable = ni.getState_functions()[gene] = new Long[sizeTable / 64 + 1];
//		Long[] stateZ = ni.getState_notObserved()[gene] = new Long[sizeTable / 64 + 1];
//		ni.getGlobal_boolean_values()[gene]=resources.sumFrequency(frequencyTable);
//		//boolean isOne=(fa[1]>fa[0])?true:((fa[1]==fa[0])?resources.random.nextBoolean():false);
//		for (int i = 0; i < booleanTable.length; i++) {
//			booleanTable[i]=0L;
//			stateTable[i]=-1L;
//			stateZ[i]=-1L;
//		}
//		for (int i = 0; i < frequencyTable.length; i++) {
//			if ((frequencyTable[i][1] > frequencyTable[i][0])) {
//				for (int j = 0; j < groupTable[i].length; j++) {
//					int index=groupTable[i][j];
//					booleanTable[index / 64] = booleanTable[index / 64] | ((long)1<< index);
//				}
//			}
//			else if (frequencyTable[i][1] == frequencyTable[i][0]) {
//				for (int j = 0; j < groupTable[i].length; j++) {
//					int index=groupTable[i][j];
//					stateTable[index / 64] = stateTable[index / 64] ^ ((long)1<< index);
//					if(frequencyTable[i][1] ==0)
//						stateZ[index / 64] = stateZ[index / 64] ^ ((long)1<< index);
//				}
//			}
//		}
//	}
//	/* frequencitable: tabla agrupada
//	 * grouptable: grupos por linea
//	 * sizetable nro de lineas de la tabla de frecuencia */
//	public Long[] buildBooleanFunction(int[][] frequencyTable, int[][]groupTable, int sizeTable) {
//		if (sizeTable==1)return buildBooleanFunction(frequencyTable);
//		Long[] booleanTable = new Long[sizeTable / 64 + 1];
//		int[] fa=resources.sumFrequency(frequencyTable);
//		boolean isOne=(fa[1]>fa[0])?true:((fa[1]==fa[0])?resources.random.nextBoolean():false);
//		for (int i = 0; i < booleanTable.length; i++) {
//			booleanTable[i]=0L;
//		}
//		for (int i = 0; i < frequencyTable.length; i++) {
//			if ((frequencyTable[i][1] > frequencyTable[i][0])||((frequencyTable[i][1] == frequencyTable[i][0])&&isOne)) {
//				for (int j = 0; j < groupTable[i].length; j++) {
//					int index=groupTable[i][j];
//					booleanTable[index / 64] = booleanTable[index / 64] | ((long)1<< index);
//				}
//			}
//		}
//		return booleanTable;
//	}
}
