package inference.searchAlgoritm;

import java.util.*;
import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.qualifyingFunction.*;
import javafx.util.Pair;
import resources.Resources;

public class NDimencionExhaustiveSearch extends AbstractSearchAlgoritm {
//	private Resources Resources=new Resources();
	protected int dimencion;

	//public NDimencionExhaustiveSearch() {
	//	this.dimencion = 1;
	//}

	public NDimencionExhaustiveSearch(int dimencion) {
		//	super();
		this.dimencion = dimencion;
	}

	public int getDimencion() {
		return dimencion;
	}

	public void setDimencion(int dimencion) {
		this.dimencion = dimencion;
	}

//	protected ArrayList<Details> searchPreditorsDetailsBody(BooleanGeneExpressionData geneExpressionData, Integer targetGene,
//			QualifyingFunction function){
//		return searchPreditorsDetailsBody(geneExpressionData,targetGene,function,false,false).getKey();
//	}
	@Override
	public Pair<List<Details>,Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction mainFunction,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution) {
		double maxQF = Double.NEGATIVE_INFINITY;
		ArrayList<Details> listTest = new ArrayList<Details>();
		int[] setTest = new int[this.dimencion];
		for (int i = 0; i < this.dimencion; i++) {
			setTest[i] = i;
		}
//		int ntest=0;
//		System.out.println( geneExpressionData.getValidGens().size());
		while (true) {
			// calcular function criterio
			TreeSet<Integer> treeSetTest = new TreeSet<Integer>();
			for (int i = 0; i < setTest.length; i++) {
				treeSetTest.add(geneExpressionData.getValidGens().get(setTest[i]));
			}
			Details detailsTest = mainFunction.quantifyDetails(treeSetTest, geneExpressionData, targetGene);
			// verificar si mejora la funcion
			if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>=0) {//(detailsTest.qualityValue >= maxQF) {
				if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>0) {
					// actualizar valor de maxima
					listTest.clear();
					maxQF = detailsTest.qualityValue;
				}
				listTest.add(detailsTest);
			}
			// ir al sigte conjunto de preditores
			if ((setTest = nextSetPreditors(setTest, geneExpressionData)) == null) {
				break;// fin de preditores
			}
//			if(this.dimencion==2) {
//				ntest++;
////				System.out.println(ntest+":"+treeSetTest);
//			}
				
		}
		return new Pair<>(listTest,null);
	}
//	@Override
//	public Pair<ArrayList<Details>,LinkedList<Double>[]> searchPreditorsDetailsBody(
//			BooleanGeneExpressionData booleanGeneExpressionData,
//			Integer targetGene,
//			QualifyingFunction function,
//			boolean printSteps,
//			boolean stopEvolution) {
//		double maxQF = Double.NEGATIVE_INFINITY;
//		ArrayList<Details> listTest = new ArrayList<Details>();
//		int[] setTest = new int[this.dimencion];
//		for (int i = 0; i < this.dimencion; i++) {
//			setTest[i] = i;
//		}
////		int ntest=0;
////		System.out.println( geneExpressionData.getValidGens().size());
//		while (true) {
//			// calcular function criterio
//			TreeSet<Integer> treeSetTest = new TreeSet<Integer>();
//			for (int i = 0; i < setTest.length; i++) {
//				if(booleanGeneExpressionData.getValidGens()==null)
//					treeSetTest.add(setTest[i]);
//				else
//					treeSetTest.add(booleanGeneExpressionData.getValidGens().get(setTest[i]));
//			}
//			Details detailsTest = function.quantifyDetails(treeSetTest, booleanGeneExpressionData, targetGene);
//			// verificar si mejora la funcion
//			if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>=0) {//(detailsTest.qualityValue >= maxQF) {
//				if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>0) {
//					// actualizar valor de maxima
//					listTest.clear();
//					maxQF = detailsTest.qualityValue;
//				}
//				listTest.add(detailsTest);
//			}
//			// ir al sigte conjunto de preditores
//			if ((setTest = nextSetPreditors(setTest, booleanGeneExpressionData)) == null) {
//				break;// fin de preditores
//			}
////			if(this.dimencion==2) {
////				ntest++;
//////				System.out.println(ntest+":"+treeSetTest);
////			}
//				
//		}
//		return new Pair<>(listTest,null);
//	}

	protected int[] nextSetPreditors(int[] setPreditors, GeneExpressionData geneExpressionData) {
		int[] newSet = new int[setPreditors.length];
		System.arraycopy(setPreditors, 0, newSet, 0, setPreditors.length);
		int pointer = setPreditors.length - 1;
		int sizeValidGens=geneExpressionData.getValidGens().size();
		while (true) {
			if (newSet[pointer] < sizeValidGens+ pointer - setPreditors.length) {
				newSet[pointer++]++;
				while (pointer < setPreditors.length) {
					newSet[pointer] = newSet[pointer - 1] + 1;
					pointer++;
				}
				return newSet;
			} else {
				pointer--;
				if (pointer < 0) {
					return null;
				}
			}
		}
	}

	@Override
	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.NDES;
	}
	public static void main(String[] a) {
		int[] ints= {0,1,2};
		while(ints!=null) {
			System.out.println(Arrays.toString(ints));
//			ints=nextSetPreditors(ints, 20);
		}
	}

	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			Integer fixedDimention) {
		if(fixedDimention!=this.dimencion)
		return null;
		else
			return searchPreditorsDetails(geneExpressionData, targetGene, function);
	}
	

	@Override
	public Details[] searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			Integer countTop, 
			QualifyingFunction function) {
		int[] setTest = new int[this.dimencion];
		for (int i = 0; i < this.dimencion; i++) {
			setTest[i] = i;
		}
		double minQF = Double.MAX_VALUE;
		Details[] listTest = new Details[countTop];
		int ntest=0;
		int postMin=0;
		while (true) {
			// calcular function criterio
			TreeSet<Integer> treeSetTest = new TreeSet<Integer>();
			for (int i = 0; i < setTest.length; i++) {
				if(geneExpressionData.getValidGens()==null)
					treeSetTest.add(setTest[i]);
				else
					treeSetTest.add(geneExpressionData.getValidGens().get(setTest[i]));
			}
			Details detailsTest = function.quantifyDetails(treeSetTest, geneExpressionData, targetGene);
			if(ntest<countTop) {
				listTest[ntest]= detailsTest;
				if (Resources.compareDouble(detailsTest.qualityValue , minQF)<0) {
					minQF=detailsTest.qualityValue;
					postMin=ntest;
				}
					
			}
			// verificar si mejora la funcion
			else if (Resources.compareDouble(detailsTest.qualityValue , minQF)>0) {//(detailsTest.qualityValue >= maxQF) {
				listTest[postMin]=detailsTest;
				postMin=indexOfmin(listTest);
				minQF = listTest[postMin].qualityValue;
			}
			// ir al sigte conjunto de preditores
			if ((setTest = nextSetPreditors(setTest, geneExpressionData)) == null) {
				break;// fin de preditores
			}
			ntest++;
		}
		sortDetails(listTest);
		return listTest;
	}

	private int indexOfmin(Details[] listTest) {
		int index=-1;
		double min=Double.MAX_VALUE;
		for (int i = 0; i < listTest.length; i++) {
			if(listTest[i].qualityValue<min) {
				min=listTest[i].qualityValue;
				index=i;
			}
		}
		return index;
	}

	

//	@Override
//	public LinkedList<Double>[] evolutionQualityFunction(BooleanGeneExpressionData ged, Integer geneTarget,
//			QualifyingFunction function) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
