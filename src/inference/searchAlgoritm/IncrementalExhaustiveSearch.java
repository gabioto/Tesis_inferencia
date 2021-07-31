package inference.searchAlgoritm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import geneticNetwork.AbstractNetwork;
import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.evaluationMetric.MutualInformation;
import inference.qualifyingFunction.*;
import javafx.util.Pair;
import resources.Resources;

@SuppressWarnings("unchecked")
public class IncrementalExhaustiveSearch extends NDimencionExhaustiveSearch {
//	private Resources Resources=new Resources();
	protected int limitIncrement;

	public IncrementalExhaustiveSearch() {
		super(1);
		this.limitIncrement = Integer.MAX_VALUE;
	}

	public IncrementalExhaustiveSearch(int limit) {
		super(1);
		this.limitIncrement = limit;
	}

	public int getLimitIncrement() {
		return limitIncrement;
	}

	public void setLimitIncrement(int limitIncrement) {
		this.limitIncrement = limitIncrement;
	}
	
//	@Override
//	public ArrayList<Details> searchPreditorsDetailsBody(BooleanGeneExpressionData geneExpressionData, Integer targetGene, QualifyingFunction function) {
//		Details bestDetailsPreditor=new Details();
//		bestDetailsPreditor.preditors = new TreeSet<Integer>();
//		bestDetailsPreditor.qualityValue=0.0;
//		bestDetailsPreditor.frequencyTable=new int[][]{{(geneExpressionData.getData(1,targetGene))?0:1,(geneExpressionData.getData(1,targetGene))?1:0}};
//        ArrayList<Details> listBest = new ArrayList<Details>();
//        listBest.add(bestDetailsPreditor);
//        this.setDimencion(1);
//        while (true) {
//            ArrayList<Details> listTest= super.searchPreditorsDetailsBody(geneExpressionData, targetGene, function);
//            if(Resources.compareDouble(listBest.get(0).qualityValue,listTest.get(0).qualityValue)<0){//(listBest.get(0).qualityValue<listTest.get(0).qualityValue){
//            	listBest=listTest;
//            	if(this.getDimencion()>=this.limitIncrement)
//            		break;
//            	else
//            		this.setDimencion(this.getDimencion()+1);
//            }else
//                break;
//        }
//        return listBest;
//	
	@Override
	public Pair<List<Details>, Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution) {
		//iniciar valores basicos
		Details bestDetailsPreditor = new Details();
		bestDetailsPreditor.idGene = targetGene;
		bestDetailsPreditor.preditors = new TreeSet<Integer>();
		bestDetailsPreditor.qualityValue = 0.0;
		bestDetailsPreditor.frequencyTable = new int[1][geneExpressionData.sizeDiscreteValues()];
		bestDetailsPreditor.frequencyTable[0][geneExpressionData.getData(0, targetGene)]=1;
		List<Details> listBest = new ArrayList<Details>();
		listBest.add(bestDetailsPreditor);
		Double additionalMetricValue=0.0;
		this.setDimencion(1);
		double HYX1=0,HYX2=0;
		MutualInformation mi=null;
		// StringBuilder sb=new StringBuilder();#
		Deque<Double>[] mutualInfomationDim = new LinkedList[] { new LinkedList<>(), new LinkedList<>() };
		if(function.getEvaluationMetric() instanceof MutualInformation && printSteps) {
			mi = (MutualInformation) function.getEvaluationMetric();
			HYX1 = mi.fullEntropy(
					AbstractNetwork.buildFrequencyTable(bestDetailsPreditor.preditors, geneExpressionData, targetGene));
			HYX2 = HYX1;
		}
		while (true) {
			List<Details> listTest = super.searchPreditorsDetailsBody(geneExpressionData,
					targetGene,
					function,
					additionalMetric,
					printSteps, stopEvolution).getKey();
			Double additionalMetricTest=additionalMetricValue+1;
			if(additionalMetric!=null) {
				additionalMetricTest=(listTest.get(0) instanceof DetailsGroup)?
					additionalMetric.quantifyFromFrequencyTable(((DetailsGroup)listTest.get(0)).frequencyTableGroup[0])
					:additionalMetric.quantifyFromFrequencyTable(listTest.get(0).frequencyTable);
			}
			if (!stopEvolution || 
					((Resources.compareDouble(listBest.get(0).qualityValue, listTest.get(0).qualityValue) < 0)
					 && additionalMetricTest.compareTo(additionalMetricValue)>0)){// (listBest.get(0).qualityValue<listTest.get(0).qualityValue){
				listBest = listTest;
				additionalMetricValue=additionalMetricTest;
				if (function.getEvaluationMetric() instanceof MutualInformation && printSteps) {
					Details details = listBest.get(0);
//        			sb.append(this.getDimencion()).append(":\t")
//        			.append(listBest.stream().map(d-> d.preditors.toString()).collect(Collectors.toList())).append("\n")
//        			.append("\tQf:").append(listBest.get(0).qualityValue).append("\n");
					function.buildGroupingDetails(details);
					int[][] frequencyTable = details.frequencyTable;
					if (details instanceof DetailsGroup) {
						if (((DetailsGroup) details).frequencyTableGroup != null)
							frequencyTable = ((DetailsGroup) details).frequencyTableGroup[0];
					}
					mutualInfomationDim[0].add((double)listBest.size());
					mutualInfomationDim[1].add(mi.quantifyFromFrequencyTable(frequencyTable, HYX2));
					HYX2 = mi.conditionalEntropy(frequencyTable);
				}
				if (this.getDimencion() >= this.limitIncrement)
					break;
				else {
					this.setDimencion(this.getDimencion() + 1);
				}
			} else {
					break;
//				else 
//				{
//					if (this.getDimencion() >= this.limitIncrement)
//						break;
//					else {
//						this.setDimencion(this.getDimencion() + 1);
//					}
//				}
			}
		}
	//	System.out.println(listBest.stream().map(Details::toPreditorsString).collect(Collectors.joining("\n")));
		return new Pair<>(listBest, (printSteps) ? mutualInfomationDim : null);
	}

//	public Pair<ArrayList<Details>, LinkedList<Double>[]> searchPreditorsDetailsBody(
//			BooleanGeneExpressionData geneExpressionData,
//			Integer targetGene,
//			QualifyingFunction function,
//			boolean printSteps) {
//		Details bestDetailsPreditor = new Details();
//		bestDetailsPreditor.idGene = targetGene;
//		bestDetailsPreditor.preditors = new TreeSet<Integer>();
//		bestDetailsPreditor.qualityValue = 0.0;
//		bestDetailsPreditor.frequencyTable = new int[][] { { (geneExpressionData.getData(1, targetGene)) ? 0 : 1,
//				(geneExpressionData.getData(1, targetGene)) ? 1 : 0 } };
//		ArrayList<Details> listBest = new ArrayList<Details>();
//		listBest.add(bestDetailsPreditor);
//		this.setDimencion(1);
//		// StringBuilder sb=new StringBuilder();
//		LinkedList<Double>[] mutualInfomationDim = new LinkedList[] { new LinkedList<>(), new LinkedList<>() };
//		MutualInformation mi = (MutualInformation) function.getEvaluationMetric();
//		double HYX1 = mi.fullEntropy(
//				AbstractNetwork.buildFrequencyTable(bestDetailsPreditor.preditors, geneExpressionData, targetGene));
//		double HYX2 = HYX1;
//		while (true) {
//			ArrayList<Details> listTest = super.searchPreditorsDetailsBody(geneExpressionData, targetGene, function,
//					false).getKey();
//			if (Resources.compareDouble(listBest.get(0).qualityValue, listTest.get(0).qualityValue) < 0) {// (listBest.get(0).qualityValue<listTest.get(0).qualityValue){
//				listBest = listTest;
//				if (printSteps == true) {
//					Details details = listBest.get(0);
////        			sb.append(this.getDimencion()).append(":\t")
////        			.append(listBest.stream().map(d-> d.preditors.toString()).collect(Collectors.toList())).append("\n")
////        			.append("\tQf:").append(listBest.get(0).qualityValue).append("\n");
//					function.buildGroupingDetails(details);
//					int[][] frequencyTable = details.frequencyTable;
//					if (details instanceof DetailsGroup) {
//						if (((DetailsGroup) details).frequencyTableGroup != null)
//							frequencyTable = ((DetailsGroup) details).frequencyTableGroup[0];
//					}
//					mutualInfomationDim[0].add(mi.quantifyFromFrequencyTable(frequencyTable, HYX1));
//					mutualInfomationDim[1].add(mi.quantifyFromFrequencyTable(frequencyTable, HYX2));
//					HYX2 = mi.conditionalEntropy(frequencyTable);
//				}
//				if (this.getDimencion() >= this.limitIncrement)
//					break;
//				else {
//					this.setDimencion(this.getDimencion() + 1);
//				}
//			} else
//				break;
//		}
//		return new Pair<>(listBest, (printSteps) ? mutualInfomationDim : null);
//	}

	@Override
	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.IES;
	}
	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function, Integer fixedDimention) {
		int fix=Math.abs(fixedDimention);
		if(fix<=this.limitIncrement) {
			setDimencion(fix);
		return super.searchPreditorsDetails(geneExpressionData, targetGene, function, fixedDimention);
		}
		else return null;
	}

}