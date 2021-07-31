package inference.qualifyingFunction;

import java.util.ArrayList;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import resources.Resources;

public class LinearSelectiveParcial extends LinearGrouping {
//	private Resources Resources=new Resources();
	private int minNotGrouping;

	// public LinearSelectiveParcial(EvaluationMetric evaluationMetric) {
	// super(evaluationMetric);
	// this.minNotGrouping=2;
	// }
	public LinearSelectiveParcial(EvaluationMetric evaluationMetric, int minNotGrouping) {
		super(evaluationMetric);
		this.minNotGrouping = minNotGrouping;
	}

	@Override
	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		int[][][] tables = new int[groups.length][][];
//		int[][][] newGroups = new int[groups.length][][];
		// definir tamano de lineas por grupo
		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
			int[][] agrupamento=groups[iGrouping];
			//definir lista de nuevos grupos
			ArrayList<LineTable> listGroup= new ArrayList<>();
			for (int[] elementsGroup : agrupamento) {
				if(elementsGroup.length==1) {// grupo de un solo elemento
					int index=elementsGroup[0];
					listGroup.add(new LineTable(frequencyTable[index], index));
				}
				else //grupo de mas de un elemento intentar separar
					listGroup.addAll(separarGrupo(elementsGroup,frequencyTable));
			}
			// colocar los nuevos grupos en el arreglo
			tables[iGrouping]=new int[listGroup.size()][];
	//		newGroups[iGrouping]=new int[listGroup.size()][];
			for (int i = 0; i < tables[iGrouping].length; i++) {
				LineTable lt=listGroup.get(i);
				tables[iGrouping][i]=lt.line;
		//		newGroups[iGrouping][i]=Resources.listToArrayInt(lt.originalIndexs);
			}
		}
	//	aplicatedGroups = newGroups;
		return tables;
	}
	private ArrayList<LineTable> separarGrupo(int[] elementsGroup, int[][] frequencyTable) {
		//agregar todos los elementos del grupo a una lista ordenada de lineas
		FrequecySortTable fst=new FrequecySortTable();
		for (int index : elementsGroup) {
			fst.add(new LineTable(frequencyTable[index], index));
		}
		//mesclar elementos de la lista hasta q cumplan el minimo de elementos por grupo
		while (fst.size() > 1 && fst.get(fst.size() - 1).sum < minNotGrouping) {
			int limSup = fst.size() - 2;// penultimo indice
			int indexLm = limSup;
			double minEntropy=Double.MAX_VALUE;
			LineTable ul = fst.remove(fst.size() - 1);// remover el ultimo de la tabla para mesclar
			while (limSup >= 0 && Resources.compareDouble(minEntropy,0)>0 ) {// procurar por el primer grupo que mejore su entropia con el nuevo miembro
				LineTable lt = fst.get(limSup);
				//double ltEntropy= Resources.entropyLine(lt.line,lt.sum );// calcular minha entropia
				double et = Resources.entropyLine(Resources.sumArray(lt.line, ul.line), lt.sum + ul.sum);// calcular la entropya
																											// del candidato
				if (Resources.compareDouble(et,minEntropy)<0) {//(et<minEntropy) {// si la entropia mejora (es menor)
					indexLm=limSup;
					minEntropy=et;
				}
				limSup--;
			}
			// mesclar las lineas correspondientes
			LineTable lt = fst.remove(indexLm);
			fst.add(new LineTable(lt, ul));
		}
		fst.get(fst.size()-1).originalIndexs.addAll(fst.indexs0s);
		return fst;
	}
//	@Override
//	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int sizePreditors) {
//		if (sizeGroup != sizePreditors) {
//			sizeGroup = sizePreditors;
//			if (sizeGroup > 1) {
//				groups = buildGroups(sizePreditors);
//			}
//		}
//		int[][][] tables = new int[groups.length][][];
//		int[][][] newGroups = new int[groups.length][][];
//		// definir tamano de lineas por grupo
//		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
//			int[][] agrupamento=groups[iGrouping];
//			//definir lista de nuevos grupos
//			ArrayList<LineTable> listGroup= new ArrayList<>();
//			for (int[] elementsGroup : agrupamento) {
//				if(elementsGroup.length==1) {// grupo de un solo elemento
//					int index=elementsGroup[0];
//					listGroup.add(new LineTable(frequencyTable[index], index));
//				}
//				else //grupo de mas de un elemento intentar separar
//					listGroup.addAll(separarGrupo(elementsGroup,frequencyTable));
//			}
//			// colocar los nuevos grupos en el arreglo
//			tables[iGrouping]=new int[listGroup.size()][];
//			newGroups[iGrouping]=new int[listGroup.size()][];
//			for (int i = 0; i < tables[iGrouping].length; i++) {
//				LineTable lt=listGroup.get(i);
//				tables[iGrouping][i]=lt.line;
//				newGroups[iGrouping][i]=Resources.listToArrayInt(lt.originalIndexs);
//			}
//		}
//		aplicatedGroups = newGroups;
//		return tables;
//	}
//	@SuppressWarnings("unchecked")
//	protected int[][][] buildTablesGruping2(int[][] frequencyTable, int sizePreditors) {
//		int[][][] tables = new int[groups.length][][];
//	//	int[][][] newGroups = (isWhitFrequencytable) ? new int[groups.length][][] : null;
//		// definir tamano de lineas por grupo
//		for (int iGrouping = 0; iGrouping < groups.length; iGrouping++) {
//			int sizeLines = 0;
//			// lineas de cada grupo
//			ArrayList<int[]>[] linesGroups = new ArrayList[groups[iGrouping].length];
////			ArrayList<ArrayList<Integer>>[] linesGroupsIndexs = (isWhitFrequencytable)
////					? new ArrayList[linesGroups.length]
////					: null;
//			for (int iGroup = 0; iGroup < groups[iGrouping].length; iGroup++) {
//				// line grouping default
//				linesGroups[iGroup] = new ArrayList<int[]>();
//				linesGroups[iGroup].add(new int[] { 0, 0 });
//
////				if (isWhitFrequencytable) {
////					linesGroupsIndexs[iGroup] = new ArrayList<>();
////					linesGroupsIndexs[iGroup].add(new ArrayList<>());
////				}
//				boolean needLineGrouping = false;
//				int indexMinGroup = -1;
//				int indexMinFreq = -1;
//				int minValue = Integer.MAX_VALUE;
//				int groupValue = 0;
//				for (int indexFreq : groups[iGrouping][iGroup]) {
//					int totalLine = 0;
//					if ((totalLine = frequencyTable[indexFreq][0]
//							+ frequencyTable[indexFreq][1]) < this.minNotGrouping) {
//						needLineGrouping = true;
//						linesGroups[iGroup].get(0)[0] += frequencyTable[indexFreq][0];
//						linesGroups[iGroup].get(0)[1] += frequencyTable[indexFreq][1];
//						groupValue += totalLine;
////						if (isWhitFrequencytable)
////							linesGroupsIndexs[iGroup].get(0).add(indexFreq);
//					} else {
//						if (totalLine < minValue) {
//							minValue = totalLine;
//							indexMinGroup = linesGroups[iGroup].size();
//							indexMinFreq = indexFreq;
//						}
//						linesGroups[iGroup].add(frequencyTable[indexFreq]);
////						if (isWhitFrequencytable) {
////							ArrayList<Integer> e = new ArrayList<>();
////							e.add(indexFreq);
////							linesGroupsIndexs[iGroup].add(e);
////						}
//					}
//				}
//				if (needLineGrouping) {
//					if (groupValue < this.minNotGrouping && indexMinFreq >= 0) {
//						linesGroups[iGroup].get(0)[0] += frequencyTable[indexMinFreq][0];
//						linesGroups[iGroup].get(0)[1] += frequencyTable[indexMinFreq][1];
//						linesGroups[iGroup].remove(indexMinGroup);
////						if (isWhitFrequencytable) {
////							linesGroupsIndexs[iGroup].get(0).add(indexMinFreq);
////							linesGroupsIndexs[iGroup].remove(indexMinGroup);
////						}
//					}
//				}
//				sizeLines += linesGroups[iGroup].size();
//			}
//			tables[iGrouping] = new int[sizeLines][2];
////			if (isWhitFrequencytable)
////				newGroups[iGrouping] = new int[sizeLines][];
//			int it = 0;
//			for (int i = 0; i < linesGroups.length; i++) {
//				for (int[] lineGroup : linesGroups[i]) {
//					tables[iGrouping][it] = lineGroup;
//					it++;
//				}
//			}
////			if (isWhitFrequencytable) {
////				it = 0;
////				for (int i = 0; i < linesGroups.length; i++) {
////					for (ArrayList<Integer> lineIndex : linesGroupsIndexs[i]) {
////						newGroups[iGrouping][it] = new int[lineIndex.size()];
////						for (int j = 0; j < newGroups[iGrouping][it].length; j++) {
////							newGroups[iGrouping][it][j] = lineIndex.get(j);
////						}
////						it++;
////					}
////				}
////			}
//		}
////		if (isWhitFrequencytable)
////			aplicatedGroups = newGroups;
//		return tables;
//	}

	@Override
	public String getName() {
		return "Lineal Selectivo Parcial";
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		return ID_QUALIFYING_FUNCTION.LSP;
	}

}
