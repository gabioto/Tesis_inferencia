package inference.qualifyingFunction;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import resources.Resources;

public class GroupingLatticeSFS extends AbstractGrouping {
	//private Resources Resources = new Resources();
	private int minNotGrouping;

	// public GroupingLatticeSFS(EvaluationMetric evaluationMetric) {
	// super(evaluationMetric);
	// this.minNotGrouping = 2;
	// }

	public GroupingLatticeSFS(EvaluationMetric evaluationMetric, int minNotGrouping) {
		super(evaluationMetric);
		this.minNotGrouping = minNotGrouping;
	}

	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		FrequecySortTable ft = new FrequecySortTable(frequencyTable);
		while (ft.size() > 1 && ft.get(ft.size() - 1).sum < this.minNotGrouping) {// mientras todos los grupos no tienen
																				// minimo minNotGrouping observaciones
			int limSup = ft.size() - 2;// penultimo indice
			int indexLm = limSup;
			double minEntropy = Double.MAX_VALUE;
			LineTable ul = ft.remove(ft.size() - 1);// remover el ultimo de la tabla para mesclar
			while (limSup >= 0 && Resources.compareDouble(minEntropy, 0) > 0) {// procurar por el primer grupo que
																				// mejore su entropia con el nuevo
																				// miembro
				LineTable lt = ft.get(limSup);
				// double ltEntropy= Resources.entropyLine(lt.line,lt.sum );// calcular minha
				// entropia
				double et = Resources.entropyLine(Resources.sumArray(lt.line, ul.line), lt.sum + ul.sum);// calcular la
																											// entropia
																											// del
																											// candidato
				if (Resources.compareDouble(et, minEntropy) < 0) {// (et<minEntropy) {// si la entropia mejora (es
																	// menor)
					indexLm = limSup;
					minEntropy = et;
				}
				limSup--;
			}
			// mesclar las lineas correspondientes
			LineTable lt = ft.remove(indexLm);
			ft.add(new LineTable(lt, ul));
		}
		int[][] tg = new int[ft.size()][];
		// int[][] aplicatedGroup = (isWhitFrequencytable) ? new int[tg.length][] :
		// null;

		for (int i = 0; i < tg.length; i++) {
			LineTable lt = ft.get(i);
			tg[i] = lt.line;
			// if (isWhitFrequencytable) {
			// aplicatedGroup[i] = new int[lt.originalIndexs.size()];
			// for (int j = 0; j < aplicatedGroup[i].length; j++) {
			// aplicatedGroup[i][j] = lt.originalIndexs.get(j);
			// }
			// }
			// z0s+=lt.line[0];o1s+=lt.line[1];
		}
		// int indexMenor=aplicatedGroup.length-1;
		// if (isWhitFrequencytable) {
		// int[] menorGroup = new int[aplicatedGroup[indexMenor].length +
		// ft.indexs0s.size()];
		// System.arraycopy(aplicatedGroup[indexMenor], 0, menorGroup, 0,
		// aplicatedGroup[indexMenor].length);
		// for (int i = 0; i < ft.indexs0s.size(); i++) {
		// menorGroup[aplicatedGroup[indexMenor].length + i] = ft.indexs0s.get(i);
		// }
		// aplicatedGroup[indexMenor] = menorGroup;
		// aplicatedGroups = new int[][][] { aplicatedGroup };
		// }
		// if(fttot!=49)
		// fttot/=ftg;
		return new int[][][] { tg };
	}

	@Override
	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int size) {
		FrequecySortTable ft = new FrequecySortTable(frequencyTable);
		while (ft.size() > 1 && ft.get(ft.size() - 1).sum < minNotGrouping) {// mientras todos los grupos no tienen
																				// minimo minNotGrouping observaciones
			int limSup = ft.size() - 2;// penultimo indice
			int indexLm = limSup;
			double minEntropy = Double.MAX_VALUE;
			LineTable ul = ft.remove(ft.size() - 1);// remover el ultimo de la tabla para mesclar
			while (limSup >= 0 && Resources.compareDouble(minEntropy, 0) > 0) {// procurar por el primer grupo que
																				// mejore su entropia con el nuevo
																				// miembro
				LineTable lt = ft.get(limSup);
				// double ltEntropy= Resources.entropyLine(lt.line,lt.sum );// calcular minha
				// entropia
				double et = Resources.entropyLine(Resources.sumArray(lt.line, ul.line), lt.sum + ul.sum);// calcular la
																											// entropia
																											// del
																											// candidato
				if (Resources.compareDouble(et, minEntropy) < 0) {// (et<minEntropy) {// si la entropia mejora (es
																	// menor)
					indexLm = limSup;
					minEntropy = et;
				}
				limSup--;
			}
			// mesclar las lineas correspondientes
			LineTable lt = ft.remove(indexLm);
			ft.add(new LineTable(lt, ul));
		}
		int[][] tg = new int[ft.size()][];
		int[][] aplicatedGroup = new int[tg.length][];

		for (int i = 0; i < tg.length; i++) {
			LineTable lt = ft.get(i);
			tg[i] = lt.line;
			aplicatedGroup[i] = new int[lt.originalIndexs.size()];
			int j = 0;
			for (Integer originalIndex : lt.originalIndexs) {
				aplicatedGroup[i][j] = originalIndex;
				j++;
			}
//			for (int j = 0; j < aplicatedGroup[i].length; j++) {
//				
//			}

			// z0s+=lt.line[0];o1s+=lt.line[1];
		}
		int indexMenor = aplicatedGroup.length - 1;
		int[] menorGroup = new int[aplicatedGroup[indexMenor].length + ft.indexs0s.size()];
		System.arraycopy(aplicatedGroup[indexMenor], 0, menorGroup, 0, aplicatedGroup[indexMenor].length);
		for (int i = 0; i < ft.indexs0s.size(); i++) {
			menorGroup[aplicatedGroup[indexMenor].length + i] = ft.indexs0s.get(i);
		}
		aplicatedGroup[indexMenor] = menorGroup;
		aplicatedGroups = new int[][][] { aplicatedGroup };
		// if(fttot!=49)
		// fttot/=ftg;
		return new int[][][] { tg };
	}
	// protected int[][][] buildTablesGruping2(int[][] frequencyTable, int
	// sizePreditors) {
	// FrequecySortTable ft = new FrequecySortTable(frequencyTable);
	// while (ft.size() > 1 && ft.get(ft.size() - 1).sum < minNotGrouping) {//
	// mientras todos los grupos no tienen
	// // minimo minNotGrouping observaciones
	// int limSup = ft.size() - 2;// penultimo indice
	// int indexLm = limSup;
	// LineTable lt = ft.get(limSup);// linea del penultimo indice
	// int sizeTop = lt.sum;// elementos en el penultimo indice
	// LineTable ul = ft.remove(ft.size() - 1);// remover el ultimo de la tabla para
	// mesclar
	// double el = Resources.entropyLine(Resources.sumArray(lt.line, ul.line),
	// lt.sum + ul.sum);// calcular la
	// // entropia de
	// // la mescla
	// limSup--;// disminuir el limite superior para buscar por posibles mejores
	// mesclas
	// while (limSup >= 0 && sizeTop == ft.get(limSup).sum) {// procurar en todos
	// los que tienen igual numero de
	// // observacione sque el maior
	// lt = ft.get(limSup);
	// double et = Resources.entropyLine(Resources.sumArray(lt.line, ul.line),
	// lt.sum + ul.sum);// calcular la
	// // entropia
	// // del
	// // candidato
	// if (Resources.compareDouble(et, el)<0) {//(et < el) {// si la entropia ser
	// menor escojer al candidato
	// indexLm = limSup;
	// el = et;
	// }
	// limSup--;
	// }
	// // mesclar las lineas correspondientes
	// lt = ft.remove(indexLm);
	// ft.add(new LineTable(lt, ul));
	// }
	// int[][] tg = new int[ft.size()][];
	//// int[][] aplicatedGroup = (isWhitFrequencytable) ? new int[tg.length][] :
	// null;
	//
	// for (int i = 0; i < tg.length; i++) {
	// LineTable lt = ft.get(i);
	// tg[i] = lt.line;
	//// if (isWhitFrequencytable) {
	//// aplicatedGroup[i] = new int[lt.originalIndexs.size()];
	//// for (int j = 0; j < aplicatedGroup[i].length; j++) {
	//// aplicatedGroup[i][j] = lt.originalIndexs.get(j);
	//// }
	//// }
	// // z0s+=lt.line[0];o1s+=lt.line[1];
	// }
	//// if (isWhitFrequencytable) {
	//// int[] menorGroup = new int[aplicatedGroup[0].length + ft.indexs0s.size()];
	//// System.arraycopy(aplicatedGroup[0], 0, menorGroup, 0,
	// aplicatedGroup[0].length);
	//// for (int i = 0; i < ft.indexs0s.size(); i++) {
	//// menorGroup[aplicatedGroup[0].length + i] = ft.indexs0s.get(i);
	//// }
	//// aplicatedGroup[0] = menorGroup;
	//// aplicatedGroups = new int[][][] { aplicatedGroup };
	//// }
	// // if(fttot!=49)
	// // fttot/=ftg;
	// return new int[][][] { tg };
	// }
	// public int[][] buildFrequencyTable(TreeSet<Integer> preditors, boolean[][]
	// geneExpresionData, int geneTarget) {

	// }
	@Override
	public String getName() {
		return "Recorrido SFS Reticulado";
	}

	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		return ID_QUALIFYING_FUNCTION.GLSFS;
	}

	@Override
	protected int[][][] buildTablesGrupingDiscret(int[][] frequencyTable, int sizePreditors) {
		FrequecySortTable ft = new FrequecySortTable(frequencyTable);
		while (ft.size() > 1 && 
				ft.get(ft.size() - 1).sum < this.minNotGrouping) {// mientras todos los grupos no tienen
																				// minimo minNotGrouping observaciones
			int limSup = ft.size() - 2;// penultimo indice
			int indexLm = limSup;
			double minEntropy = Double.MAX_VALUE;
			LineTable ul = ft.remove(ft.size() - 1);// remover el ultimo de la tabla para mesclar
			while (limSup >= 0 && Resources.compareDouble(minEntropy, 0) > 0) {// procurar por el primer grupo que
																				// mejore su entropia con el nuevo
																				// miembro
				LineTable lt = ft.get(limSup);
				// double ltEntropy= Resources.entropyLine(lt.line,lt.sum );// calcular minha
				// entropia
				double et = Resources.entropyLine(Resources.sumArray(lt.line, ul.line), lt.sum + ul.sum);// calcular la
																											// entropia
																											// del
																											// candidato
				if (Resources.compareDouble(et, minEntropy) < 0) {// (et<minEntropy) {// si la entropia mejora (es
																	// menor)
					indexLm = limSup;
					minEntropy = et;
				}
				limSup--;
			}
			// mesclar las lineas correspondientes
			LineTable lt = ft.remove(indexLm);
			ft.add(new LineTable(lt, ul));
		}
		int[][] tg = new int[ft.size()][];
	
		for (int i = 0; i < tg.length; i++) {
			LineTable lt = ft.get(i);
			tg[i] = lt.line;
		}
		return new int[][][] { tg };
	}
	public static void main(String arg[]) {
		int[][] ft=new int[][] {
		   //0 1
			{0,1}, //000
			{0,1}, //001
			{0,3}, //010
			{0,1}, //011
			{3,0}, //100
			{3,0}, //101
			{0,2}, //110
			{0,3}  //111
			};
			GroupingLatticeSFS gl =new GroupingLatticeSFS(null, 2);
			System.out.println(Stream.of(gl.buildTablesGruping(ft, 3))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			System.out.println(Stream.of(gl.buildTablesGrupingDiscret(ft, 3))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			
			int[][] ft3=new int[][] {
				{0,0,1}, //00
				{0,0,1}, //01
				{0,2,0}, //02
				{0,0,1}, //10
				{0,0,1}, //11
				{0,0,2}, //12
				{0,0,1}, //20
				{0,1,0}, //21
				{0,0,1}  //22
//				,
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,5,1}
			};
			System.out.println(Stream.of(gl.buildTablesGrupingDiscret(ft3, 2))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			
	}
}
