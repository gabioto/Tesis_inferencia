package inference.searchAlgoritm;

import java.util.Deque;
import java.util.List;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.qualifyingFunction.Details;
import inference.qualifyingFunction.QualifyingFunction;
import javafx.util.Pair;
public class SequentialBackwardSelection extends AbstractSearchAlgoritm{
//	private Resources Resources=new Resources();
//	private int limit;

//	public SequentialBackwardSelection(int limit) {
//		this.limit = limit;
//	}

//	@Override
//	public Pair<List<Details>,Deque<Double>[]> searchPreditorsDetailsBody(GeneExpressionData geneExpressionData, Integer targetGene,
//			QualifyingFunction function, boolean printSteps, boolean stopEvolution) {
//		NDimencionExhaustiveSearch nd = new NDimencionExhaustiveSearch(this.limit);
//		List<Details> listBest =nd.searchPreditorsDetails(geneExpressionData, targetGene, function);
//		double maxQF=-1;
//			maxQF = listBest.get(0).qualityValue;
//		int dimencion = this.limit;
//		Deque<Double>[] mutualInfomationDim=new LinkedList[] { new LinkedList<>(),new LinkedList<>()};
//		while (true) {
//			if (--dimencion < 0)
//				break;
//			List<Details> listTest = new ArrayList<Details>();
//			Details details = (listBest.size() > 1) ? listBest.get(Resources.random.nextInt(listBest.size())):
//					 listBest.get(0);
//			for (Iterator<Integer> it = details.preditors.iterator(); it.hasNext();) {
//				Integer genSub = it.next();
//				NavigableSet<Integer> preditorsTest = new TreeSet<Integer>();
//				preditorsTest.addAll(details.preditors);
//				if (preditorsTest.remove(genSub)) {
//					Details detailsTest = function.quantifyDetails(preditorsTest, geneExpressionData, targetGene);
//					if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>=0) {//(detailsTest.qualityValue >= maxQF) {
//						if (Resources.compareDouble(detailsTest.qualityValue , maxQF)>0) {
//							// actualizar valor de maxima
//							listTest.clear();
//							maxQF = detailsTest.qualityValue;
//							listTest.add(detailsTest);
//						} else if (!Details.existDetails(listTest, preditorsTest))
//							listTest.add(detailsTest);
//					}
//				}
//			}
//
//			if (!listTest.isEmpty()){
//        		if (printSteps==true)
////        			sb.append(dimencion+1).append(":\t").append(listBest).append("\n")
////        			.append("\tQf:").append(listBest.get(0).qualityValue);
//        			mutualInfomationDim[0].add(listBest.get(0).qualityValue);
//				listBest = reducePreditorsDetails(function, listTest);
//			}
//			else
//				break;
//		}
//		return new Pair<>(listBest, (printSteps)?mutualInfomationDim:null);
//	}
	@Override
	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.SBS;
	}

	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function, Integer fixedDimention) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Details[] searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene, Integer countTop,
			QualifyingFunction function) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Pair<List<Details>, Deque<Double>[]> searchPreditorsDetailsBody(GeneExpressionData geneExpressionData,
			Integer targetGene, QualifyingFunction mainFunction, EvaluationMetric additionalMetric, boolean printSteps,
			boolean stopEvolution) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public LinkedList<Double>[] evolutionQualityFunction(BooleanGeneExpressionData ged, Integer geneTarget,
//			QualifyingFunction function) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
