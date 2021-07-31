package inference.searchAlgoritm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import geneticNetwork.AbstractNetwork;
import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.evaluationMetric.MutualInformation;
import inference.qualifyingFunction.*;
import javafx.util.Pair;
import resources.Resources;
@SuppressWarnings("unchecked")
public class SequentialForwardSelection extends AbstractSearchAlgoritm {
//	private Resources Resources=new Resources();
	private int limit;

	public SequentialForwardSelection() {
		this.limit = Integer.MAX_VALUE;
	}

	public SequentialForwardSelection(int limit) {
		this.limit = limit;
	}

	@Override
	public Pair<List<Details>,Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution) {
	//	System.out.println("Gene->"+targetGene);
		Details bestDetailsPreditor =
				(function.getClass().isAssignableFrom(AbstractGrouping.class))?
				new DetailsGroup():new Details();
		bestDetailsPreditor.idGene=targetGene;
		bestDetailsPreditor.preditors = new TreeSet<Integer>();
		bestDetailsPreditor.qualityValue = 0.0;
		bestDetailsPreditor.frequencyTable = AbstractNetwork.buildFrequencyTable(bestDetailsPreditor.preditors, geneExpressionData, targetGene);
		if(bestDetailsPreditor instanceof DetailsGroup)
			((DetailsGroup)bestDetailsPreditor).frequencyTableGroup=new int[][][] {bestDetailsPreditor.frequencyTable};
//				new int[][] { { (geneExpressionData.getData(1, targetGene)) ? 0 : 1,
//				(geneExpressionData.getData(1, targetGene)) ? 1 : 0 } };
		ArrayList<Details> listBest = new ArrayList<Details>();
		listBest.add(bestDetailsPreditor);
		return searchPreditorsDetails(geneExpressionData, targetGene, function,
				additionalMetric,listBest, printSteps,stopEvolution);

	}
//	
//	public LinkedList<Double>[] evolutionQualityFunction(BooleanGeneExpressionData geneExpressionData, Integer targetGene,
//			QualifyingFunction function){
//		return searchPreditorsDetailsBody(geneExpressionData, targetGene, function, true, false).getValue();
//	}
	public Pair<List<Details>,Deque<Double>[]> searchPreditorsDetails(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			List<Details> listBest,
			boolean printSteps,
			boolean stopEvolution) {
		Details details=listBest.get(0);
		int dimencion = details.preditors.size();
		List<Integer> listAdd =(geneExpressionData.getValidGens()==null)? IntStream.range(0, geneExpressionData.getSizeGens()).boxed().collect(Collectors.toList()):
			geneExpressionData.getValidGens();
		//StringBuilder sb=new StringBuilder();
		Deque<Double>[] mutualInfomationDim=null;
        MutualInformation mi=null;// (MutualInformation)function.getEvaluationMetric();
        int[][] frequencyTable=null;
        double HYX2=0;
        if (function.getEvaluationMetric() instanceof MutualInformation &&printSteps) {
        	mi = (MutualInformation)function.getEvaluationMetric();
        	mutualInfomationDim=new LinkedList[] { new LinkedList<>(),new LinkedList<>()};
        	frequencyTable=details.frequencyTable;
			if(details instanceof DetailsGroup) {
				if(((DetailsGroup)details).frequencyTableGroup!=null)
					frequencyTable=((DetailsGroup)details).frequencyTableGroup[0];
			}
	        HYX2=mi.conditionalEntropy(frequencyTable);
        }
//        HYX2=mi.conditionalEntropy(AbstractNetwork.buildFrequencyTable(listBest.get(0).preditors, geneExpressionData, targetGene));
		while (true) {
//			System.out.println(listBest.get(0).toPreditorsString());
			if (++dimencion > limit)
				break;
			List<Details> listTest = improveListDetails(geneExpressionData, targetGene, function,additionalMetric, listBest, listAdd,stopEvolution);	
			if (listTest.isEmpty())
				break;
			else {
				if (function.getEvaluationMetric() instanceof MutualInformation
						&& geneExpressionData instanceof BooleanGeneExpressionData
						&& printSteps) {
            		details=listTest.get(0);
            		function.buildGroupingDetails(details);
            		frequencyTable=details.frequencyTable;
            		if(details instanceof DetailsGroup) {
            			if(((DetailsGroup)details).frequencyTableGroup!=null)
            				frequencyTable=((DetailsGroup)details).frequencyTableGroup[0];
            		}
            		mutualInfomationDim[0].add((double)listTest.size());
            		mutualInfomationDim[1].add(mi.quantifyFromFrequencyTable(frequencyTable,HYX2));
            		HYX2=mi.conditionalEntropy(frequencyTable);
            	}
//				System.out.println(listBest.get(0).toPreditorsString());
				listBest = listTest;
//				System.out.println("\tDim:"+dimencion+"("+listBest.size()+")");
//				System.out.println("\t\t"+listBest.stream().map((a)->a.preditors.toString()).collect(Collectors.toList()));
			}
		}
//		listBest.forEach(a->System.out.println("SFS:"+targetGene+"\t"+ a.preditors));
//		sb.append(formatListDetails(listBest));
		return new Pair<>(listBest, (printSteps)?mutualInfomationDim:null);
	}

	public List<Details> improveListDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			List<Details> listBest,
			Collection<Integer> listAdds,
			boolean stopEvolution) {
		double maxQF = listBest.get(0).qualityValue;
		Double additionalMetricValue=0.0;
		Double additionalMetricTest=1.0;
		if(additionalMetric!=null) {
			additionalMetricValue=(listBest.get(0) instanceof DetailsGroup)?
				additionalMetric.quantifyFromFrequencyTable(((DetailsGroup)listBest.get(0)).frequencyTableGroup[0])
				:additionalMetric.quantifyFromFrequencyTable(listBest.get(0).frequencyTable);
		}
		if(!stopEvolution)
			maxQF=-1;
		Set<Integer> detailsEvaluate = new HashSet<>();
		List<Details> listTest = new ArrayList<>();
		for (Iterator<Details> iterator = listBest.iterator(); iterator.hasNext();) {
			Details details = iterator.next();
			for (Iterator<Integer> itGene = listAdds.iterator(); itGene.hasNext();) {
				int genAdd = itGene.next();
				Details detailsTest = new Details();
				detailsTest.preditors = new TreeSet<Integer>();
				detailsTest.preditors.addAll(details.preditors);
				if (detailsTest.preditors.add(genAdd)) {
					int hashDetails = detailsTest.hashCode();
					if (!detailsEvaluate.contains(hashDetails)) {
						detailsTest = function.quantifyDetails(detailsTest.preditors, geneExpressionData, targetGene);
						if (Resources.compareDouble(detailsTest.qualityValue, maxQF) >= 0) {// (detailsTest.qualityValue
																							// >= maxQF) {
							if (Resources.compareDouble(detailsTest.qualityValue, maxQF) > 0) {
								// actualizar valor de maxima
								listTest.clear();
								maxQF = detailsTest.qualityValue;
								listTest.add(detailsTest);
							}
							else if (!listTest.isEmpty())
								listTest.add(detailsTest);
						}
						detailsEvaluate.add(hashDetails);
					}
				}
			}
		}
		if(additionalMetric!=null) {
			List<Details> rl=new ArrayList<>();
			for (Details details : listTest) {
				additionalMetricTest=(details instanceof DetailsGroup)?
					additionalMetric.quantifyFromFrequencyTable(((DetailsGroup)details).frequencyTableGroup[0])
					:additionalMetric.quantifyFromFrequencyTable(details.frequencyTable);
				if(additionalMetricTest.compareTo(additionalMetricValue)>0)
					rl.add(details);
			}
			return rl;
		}
		return listTest;
	}

//	@SuppressWarnings("unchecked")
//	public ArrayList<Details> improveListDetails(BooleanGeneExpressionData geneExpressionData, Integer targetGene,
//			QualifyingFunction function, ArrayList<Details> listBest) {
//		
////		double maxQF = listBest.get(0).qualityValue;
////		ArrayList<Details> listTest = new ArrayList<Details>();
////		for (Iterator<Details> iterator = listBest.iterator(); iterator.hasNext();) {
////			Details details = iterator.next();
////			for (int genAdd = 0; genAdd < geneExpressionData.getSizeGens(); genAdd++) {
////				TreeSet<Integer> preditorsTest = (TreeSet<Integer>) details.preditors.clone();
////				if (preditorsTest.add(genAdd)) {
////					Details detailsTest = function.quantifyDetails(preditorsTest, geneExpressionData, targetGene);
////					if (Resources.compareDouble(detailsTest.qualityValue, maxQF) >= 0) {// (detailsTest.qualityValue >=
////																						// maxQF) {
////						if (Resources.compareDouble(detailsTest.qualityValue, maxQF) > 0) {
////							// actualizar valor de maxima
////							listTest.clear();
////							maxQF = detailsTest.qualityValue;
////							listTest.add(detailsTest);
////						} else if (!listTest.isEmpty() && !Resources.existDetails(listTest, preditorsTest))
////							listTest.add(detailsTest);
////					}
////				}
////			}
////		}
////		if (!listTest.isEmpty())
////			return reducePreditorsDetails(function, listTest);
////		else
////			return null;
//		
//	}

//	public void searchPreditorsDetails(boolean[][] geneExpressionData, Integer targetGene, QualifyingFunction function,
//			int trueSize) {
//
//	}

	public void searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			int trueSize,
			PrintWriter writer) {
		double maxQF = 0;
		Details bestDetailsPreditor = new Details();
		bestDetailsPreditor.idGene=targetGene;
		bestDetailsPreditor.preditors = new TreeSet<Integer>();
		bestDetailsPreditor.qualityValue = 0.0;
		bestDetailsPreditor.frequencyTable = new int[1][geneExpressionData.sizeDiscreteValues()];
		bestDetailsPreditor.frequencyTable[0][geneExpressionData.getData(0, targetGene)]=1;
		List<Details> listBest = new ArrayList<Details>();
		listBest.add(bestDetailsPreditor);
		int dimencion = 0;
		while (true) {
			if (++dimencion > limit)
				break;
			List<Details> listTest = new ArrayList<Details>();
			for (Iterator<Details> iterator = listBest.iterator(); iterator.hasNext();) {
				Details details = iterator.next();
				for (int genAdd = 0; genAdd < geneExpressionData.getSizeGens(); genAdd++) {
					NavigableSet<Integer> preditorsTest = new TreeSet<Integer>(); 
					preditorsTest.addAll(details.preditors);
					if (preditorsTest.add(genAdd)) {
						Details detailsTest = function.quantifyDetails(preditorsTest, geneExpressionData, targetGene);
						if (Resources.compareDouble(detailsTest.qualityValue, maxQF) >= 0) {// (detailsTest.qualityValue
																							// >= maxQF) {
							if (Resources.compareDouble(detailsTest.qualityValue, maxQF) > 0) {
								// actualizar valor de maxima
								listTest.clear();
								maxQF = detailsTest.qualityValue;
								listTest.add(detailsTest);
							} else if (!listTest.isEmpty() && !Details.existDetails(listTest, preditorsTest))
								listTest.add(detailsTest);
						}
					}
				}
			}
			if (!listTest.isEmpty())
				listBest = listTest;
			writer.println(trueSize + "," + dimencion + "," + maxQF);
		}
	}

	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.SFS;
	}

	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function, Integer fixedDimention) {
		if(fixedDimention<=this.limit) {
		int bckLimit=this.limit;
		this.limit=fixedDimention;
		List<Details> listPreditors=searchPreditorsDetailsBody(geneExpressionData, targetGene, function,false,false).getKey();
		this.limit=bckLimit;
		return listPreditors;
		}else return null;
	}

	@Override
	public Details[] searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene, Integer countTop,
			QualifyingFunction function) {
		throw (new UnsupportedOperationException());
	}
}
