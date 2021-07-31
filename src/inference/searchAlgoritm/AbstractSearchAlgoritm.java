package inference.searchAlgoritm;

import java.util.*;
import java.util.stream.Collectors;
import geneticNetwork.DiscreteGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.evaluationMetric.EvaluationMetric;
import inference.qualifyingFunction.*;
import javafx.util.Pair;
import resources.Resources;

public abstract class AbstractSearchAlgoritm implements SearchAlgoritm {
	
	private static List<Details> reduceByDiscreteData(DiscreteGeneExpressionData gdd,
			QualifyingFunction function, List<Details> preditorsList) {
		List<Details> d=new ArrayList<Details>();
		double maxIM=0;
		for (Details details : preditorsList) {
			Details ddge=function.quantifyDetails(details.preditors, gdd, details.idGene);
			if(Resources.compareDouble(ddge.qualityValue, maxIM)>=0) {
				if(Resources.compareDouble(ddge.qualityValue, maxIM)>0) {
					d.clear();
					maxIM=ddge.qualityValue;
				}
				d.add(ddge);
			}
		}
		return d;
	}

	public static List<Details> reducePreditorsDetails(
			DiscreteGeneExpressionData gdd,
			QualifyingFunction function,
			List<Details> listDetails) {
		if(gdd == null && listDetails.size()==1)
			return listDetails;
//		System.out.println("Sem Red:"+listDetails.size());
		if(gdd!=null) {
			listDetails = reduceByDiscreteData(gdd, function, listDetails);
//			System.out.println(listDetails.stream().map(Details::toPreditorsString).collect(Collectors.joining("\n")));
			if(listDetails.size()==1)
				return listDetails;
			else
				return reduceByFrequency(listDetails);
		}
		return reduceByFrequency(listDetails);
//		if(listDetails.get(0).frequencyTable[0].length==2) {
//			ArrayList<Details> nvaList = new ArrayList<>();
//			boolean isReducible = true;
//			int nFunctionNoReducibles = Integer.MAX_VALUE;
//			for (Details details : listDetails) {
//				function.buildGroupingDetails(details);
//				if (isReducible && details.isReductora)
//					nvaList.add(details);
//				else if (isReducible) {
//					nvaList.clear();
//					isReducible = false;
//				}
//				if (!details.isReductora)
//					if (details.function.length < nFunctionNoReducibles) {
//						nFunctionNoReducibles = details.function.length;
//						nvaList.clear();
//						nvaList.add(details);
//					} else if (details.function.length == nFunctionNoReducibles)
//						nvaList.add(details);
//			}
//			return reduceByFrequency(function, nvaList);
//		}
//		else
//			return reduceByFrequency(function, listDetails);
	}
	public List<Details> reducePreditorsDetails(QualifyingFunction function, List<Details> listDetails) {
		return reducePreditorsDetails(null, function, listDetails);
	}

	public static List<Details> reduceByFrequency(List<Details> preditorsList) {
		//ArrayList<Details> preditorsList = reducePreditorsDetails(function, listDetails);
//		System.out.println("Red Gru:"+preditorsList.size());
		if (preditorsList.size() > 2) {
			List<Details> reducidos = new ArrayList<Details>();
			Map<Integer, Integer> contagens = new HashMap<Integer, Integer>();
			for (int i = 0; i < preditorsList.size(); i++) {
				for (Iterator<Integer> it = preditorsList.get(i).preditors.iterator(); it.hasNext();) {
					Integer gen = it.next();
					if (contagens.containsKey(gen))
						contagens.put(gen, contagens.get(gen) + 1);
					else
						contagens.put(gen, 1);
				}
			}
			int max = 0;
			for (int i = 0; i < preditorsList.size(); i++) {
				int valor = 0;
				for (Iterator<Integer> it = preditorsList.get(i).preditors.iterator(); it.hasNext();) {
					valor += contagens.get(it.next());
				}
				if (valor > max) {
					max = valor;
					reducidos.clear();
					reducidos.add(preditorsList.get(i));
				} else if (valor == max) {
					reducidos.add(preditorsList.get(i));
				}
			}
			return reducidos;
//			return reducePreditorsByEntropy(reducidos);
		} else {
			return preditorsList;
//			return reducePreditorsByEntropy(preditorsList);
		}
	}

	
//	private ArrayList<Details> reducePreditorsByEntropy(ArrayList<Details> listDetails) {
//		return listDetails;
//////		System.out.println("Red Fre:"+listDetails.size());
////		ArrayList<Details> nvaList = new ArrayList<>();
////		double entropyMax = -1;
////		for (Details details : listDetails) {
////			if (Resources.compareDouble(details.entropyX, entropyMax) >= 0) {
////				if (Resources.compareDouble(details.entropyX, entropyMax) > 0) {
////					nvaList.clear();
////					entropyMax = details.entropyX;
////				}
////				nvaList.add(details);
////			}
////		}
////	//	System.out.println("Inf->("+nvaList.size()+")"+nvaList.stream().map((a)->a.preditors.toString()).collect(Collectors.toList()));
////		return nvaList;
//	}
	protected static String formatListDetails(List<Details> listDetails) {
		StringBuilder sb=new StringBuilder();
		if(!listDetails.isEmpty())
			sb.append(listDetails.get(0).preditors.size()).append(":\t")//.append(listBest).append("\n")
    			.append(listDetails.stream().map(d-> d.preditors.toString()).collect(Collectors.toList())).append("\n")
    			.append("\tQf:").append(listDetails.get(0).qualityValue).append("\n");
		return sb.toString();
	}
//	public abstract Pair<List<Details>,Deque<Double>[]>
//								searchPreditorsDetailsBody(GeneExpressionData geneExpressionData,
//															Integer targetGene,
//															QualifyingFunction function,
//															boolean printSteps,
//															boolean stopEvolution);
	public final Pair<List<Details>,Deque<Double>[]> 
								searchPreditorsDetailsBody(GeneExpressionData geneExpressionData,
														  Integer targetGene,
														  QualifyingFunction function,
														  boolean printSteps){
		return searchPreditorsDetailsBody(geneExpressionData, targetGene, function, printSteps, true);
	}
	
	protected List<Details> searchPreditorsDetailsBody(GeneExpressionData geneExpressionData,
															Integer targetGene,
															QualifyingFunction function){
		return searchPreditorsDetailsBody(geneExpressionData, targetGene, function,false).getKey();
	}
	@Override
	public final Deque<Double>[] evolutionQualityFunction(GeneExpressionData ged, 
			Integer geneTarget,
			QualifyingFunction function, boolean stopEvolution){
		return 	searchPreditorsDetailsBody(ged, geneTarget, function, true, stopEvolution).getValue();
	}

	public final List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
															Integer targetGene,
															QualifyingFunction function) {
		return reducePreditorsDetails(function, searchPreditorsDetailsBody(geneExpressionData, targetGene, function));
	}
	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction mainFunction,
			EvaluationMetric additionalMetric) {
		return reducePreditorsDetails(mainFunction,
				searchPreditorsDetailsBody(geneExpressionData,
						targetGene,
						mainFunction,
						additionalMetric,
						false,
						true).getKey());
	}
	public final Pair<List<Details>,Deque<Double>[]>
								searchPreditorsDetails(GeneExpressionData geneExpressionData,
														Integer targetGene,
														QualifyingFunction function,
														boolean printSteps){
		Pair<List<Details>,Deque<Double>[]> ps = 
				searchPreditorsDetailsBody(geneExpressionData, targetGene, function, printSteps);
		List<Details> det=reducePreditorsDetails(function,ps.getKey());
		return new Pair<>(det,ps.getValue());
	}
//	@Override
	public final Pair<List<Details>, Deque<Double>[]> 
				 searchPreditorsDetailsBody(GeneExpressionData geneExpressionData,
											Integer targetGene,
											QualifyingFunction function,
											boolean printSteps,
											boolean stopEvolution) {
			return searchPreditorsDetailsBody(geneExpressionData,
											targetGene,
											function, 
											null,
											printSteps,
											stopEvolution);
	}	

	@Override
	public Pair<List<Details>, Deque<Double>[]> 
				searchPreditorsDetails(GeneExpressionData geneExpressionData,
										Integer targetGene,
										QualifyingFunction function,
										EvaluationMetric additionalMetric,
										boolean printSteps) {
					return searchPreditorsDetailsBody(geneExpressionData,
							targetGene,
							function, 
							additionalMetric,
							printSteps,
							true);
	}
	public void sortDetails(Details[] details) {
		for (int i = 0; i < (details.length-1); i++) {
			int index=i;
			double max=details[i].qualityValue;
			for (int j = i+1; j < details.length; j++) {
				if(details[j].qualityValue>max) {
					max=details[j].qualityValue;
					index=j;
				}
			}
			if(index!=i) {
				Details detailsAux=details[i];
				details[i]=details[index];
				details[index]=detailsAux;
			}
		}
	}
}