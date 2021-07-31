package inference.searchAlgoritm;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.DiscreteGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.AbstractNetworkInfered;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.qualifyingFunction.*;
import javafx.util.Pair;

public class IncrementalExhaustiveSearchWithSFS extends IncrementalExhaustiveSearch {
	private int limitSearch;

	public IncrementalExhaustiveSearchWithSFS(int limitIncrement, int limitSearch) {
		super(limitIncrement);
		this.limitSearch = limitSearch;
	}

	public IncrementalExhaustiveSearchWithSFS(int[] limits) {
		super(limits[0]);
		this.limitSearch = limits[1];
	}

	@Override
	public Pair<List<Details>, Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution) {
		return searchPreditorsDetailsBody(
				geneExpressionData,
				geneExpressionData,
				targetGene,
				function,
				additionalMetric,
				printSteps,
				stopEvolution);
	}

	public Pair<List<Details>, Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionDataExaustive,
			GeneExpressionData geneExpressionDataSFS,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution) {
		Pair<List<Details>, Deque<Double>[]> listBest = super.searchPreditorsDetailsBody(
				geneExpressionDataExaustive,
				targetGene,
				function,
				additionalMetric,
				printSteps,
				stopEvolution);
		List<Details> ld = listBest.getKey();
//		System.out.println("Fin Eux ["+targetGene+"]->"+listBest.getKey().size());
//		if (ld.get(0).preditors.size() < getLimitIncrement()||
//				ld.get(0).qualityValue==1.0)
		if (stopEvolution) {
			// recrear la lista en base al nuevos datos
			if (geneExpressionDataExaustive != geneExpressionDataSFS
					&& geneExpressionDataExaustive instanceof BooleanGeneExpressionData) {
				ld = AbstractNetworkInfered.buildDetailsFromCluster(ld,
						((BooleanGeneExpressionData) geneExpressionDataExaustive));
				if (geneExpressionDataSFS instanceof DiscreteGeneExpressionData) {
					ld = AbstractSearchAlgoritm
							.reducePreditorsDetails((DiscreteGeneExpressionData) geneExpressionDataSFS, function, ld);
					if (ld.get(0).qualityValue == 1.0)
						return listBest;
					else if (ld.get(0).preditors.size() < limitIncrement) {
						System.out.println("sai->" + targetGene + "(" + ld.get(0).preditors.size() + "):"
								+ ld.get(0).qualityValue);
					}
				}
			} else if (ld.get(0).preditors.size() < getLimitIncrement() || ld.get(0).qualityValue == 1.0)
				return listBest;
		}
//		ArrayList<Details> ld = reducePreditorsDetails(function, listBest.getKey());

		Deque<Double>[] preStr = listBest.getValue();
		SequentialForwardSelection sfs = new SequentialForwardSelection(limitSearch);
		Pair<List<Details>, Deque<Double>[]> r = 
				sfs.searchPreditorsDetails(geneExpressionDataSFS,
						targetGene,
						function,
						additionalMetric,
						ld,
						printSteps,
						stopEvolution);
		if (printSteps) {
			preStr[0].addAll(r.getValue()[0]);
			preStr[1].addAll(r.getValue()[1]);
		}
		return new Pair<>(r.getKey(), (printSteps) ? preStr : null);

	}

	private Map<Integer, List<Details>> searchPreditorsDetailsBodyBD(
			BooleanGeneExpressionData geneExpressionDataExa,
			DiscreteGeneExpressionData geneExpressionDataSFS,
			int idGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric) {
		Map<Integer, List<Details>> result = new TreeMap<>();
		List<Details> ld = super.searchPreditorsDetailsBody(geneExpressionDataExa,
				idGene,
				function,
				additionalMetric,
				false, true)
				.getKey();
//		ld.forEach(a->System.out.println(idGene+"\t"+ a.preditors));
		// recrear la lista en base al nuevos datos
		ld = AbstractNetworkInfered.buildDetailsFromCluster(ld, geneExpressionDataExa);
		List<Integer> cluster = geneExpressionDataExa.getCluster(idGene);
		for (Integer currentGene : cluster) {
			List<Details> cloneList=Details.cloneListof(ld, currentGene);
			List<Details> dlist =
					AbstractSearchAlgoritm.reducePreditorsDetails(geneExpressionDataSFS,
							function,
							cloneList
					);
//			if(iGene==idGene)
//				dlist.forEach(a->System.out.println("D:"+idGene+"\t"+ a.preditors));
			if (dlist.get(0).qualityValue == 1.0)
				result.put(currentGene, dlist);
			else {
				SequentialForwardSelection sfs = new SequentialForwardSelection(limitSearch);
				//if(function instanceof LinearGrouping) LinearGrouping.groupsDiscrete.clear();
				List<Details> r = sfs
						.searchPreditorsDetails(geneExpressionDataSFS,
								currentGene,
								function,
								additionalMetric,
								dlist, false,
								true).getKey();
				result.put(currentGene, r);
			}
		}
		return result;
	}

	@Override
	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.IES_SFS;
	}

	@Override
	public List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function, Integer fixedDimention) {
		if (fixedDimention <= this.limitSearch) {
			int bckLimit = this.limitSearch;
			int bckIncrement = this.limitIncrement;
			this.limitSearch = fixedDimention;
			if (fixedDimention < this.limitIncrement) {
				this.limitIncrement = fixedDimention;
			}
			List<Details> listPreditors = searchPreditorsDetailsBody(geneExpressionData, targetGene, function,
					false, false).getKey();
			this.limitSearch = bckLimit;
			this.limitIncrement = bckIncrement;
			return listPreditors;
		} else
			return null;
	}
	

	@Override
	public Details[] searchPreditorsDetails(GeneExpressionData geneExpressionData, Integer targetGene, Integer countTop,
			QualifyingFunction function) {
//		NDimencionExhaustiveSearch ndsearch=new NDimencionExhaustiveSearch(limitSearch);
//		ArrayList<Details> detailsList=ndsearch.searchPreditorsDetails(geneExpressionData, targetGene, function);
//		double minQF = Double.MAX_VALUE;
//		Details[] listTest = new Details[countTop];
//		int ntest=0;
//		int postMin=0;
////		SequentialForwardSelection sfs = new SequentialForwardSelection(limitSearch);
////		
////		for (int i = 0; i < details.length; i++) {
////			Details detail = details[i];
////			ArrayList<Details> ld = new ArrayList<Details>();
////			ld.add(detail);
////			
////			Pair<ArrayList<Details>, LinkedList<Double>[]> r = sfs.searchPreditorsDetails(geneExpressionData, targetGene,
////					function, ld, false, true);
////			detailR[i]=r.getKey().get(0);
////		}
////		sortDetails(detailR);
////		return detailR;
//		return listTest;
		throw new UnsupportedOperationException();
	}

	public Map<Integer, List<Details>> searchPreditorsMultipleDetailsBD(
			BooleanGeneExpressionData geneExpressionDataExa,
			DiscreteGeneExpressionData geneExpressionDataSFS,
			int idGene, 
			QualifyingFunction function,
			EvaluationMetric additionalMetric) {
		Map<Integer, List<Details>> ts = searchPreditorsDetailsBodyBD(geneExpressionDataExa,
				geneExpressionDataSFS,
				idGene,
				function,
				additionalMetric);
		Set<Integer> keys=ts.keySet();
		for (Integer currentGene : keys) {
			ts.put(currentGene,reducePreditorsDetails(function, ts.get(currentGene)));
		}
		return ts;
	}

	public Map<Integer, List<Details>> searchPreditorsMultipleDetailReduce(
			BooleanGeneExpressionData geneExpressionDataSource, DiscreteGeneExpressionData geneExpressionDataReduce,
			int idGene, 
			QualifyingFunction function,
			EvaluationMetric additionalMetric) {
		List<Details> ts = searchPreditorsDetailsBody(geneExpressionDataSource, geneExpressionDataSource, idGene,
				function,
				additionalMetric,
				false, true).getKey();
		List<Integer> cluster = geneExpressionDataSource.getCluster(idGene);
		List<Details> details = AbstractNetworkInfered.buildDetailsFromCluster(ts,
				geneExpressionDataSource);
		Map<Integer, List<Details>> result = new TreeMap<>();
		for (Integer currentGene : cluster) {
			result.put(currentGene,
					reducePreditorsDetails(geneExpressionDataReduce, function, Details.cloneListof(details, currentGene)));
		}
		return result;
	}

	public List<Details> searchPreditorsMultipleDetailRE(
			BooleanGeneExpressionData geneExpressionDataSource,
			DiscreteGeneExpressionData geneExpressionDataReduce,
			int idGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric) {
		List<Details> ts = searchPreditorsDetailsBody(geneExpressionDataSource, geneExpressionDataSource, idGene,
				function,
				additionalMetric,
				false, true).getKey();
		return reducePreditorsDetails(geneExpressionDataReduce, function, AbstractNetworkInfered.buildDetailsFromCluster(ts,
				geneExpressionDataSource));
	}
	public List<Details> searchPreditorsMultipleDetailRE(
			BooleanGeneExpressionData geneExpressionDataSource,
			DiscreteGeneExpressionData geneExpressionDataReduce,
			int idGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			int fixedDegree) {
//		GeneExpressionData geneExpressionData, Integer targetGene,
//		QualifyingFunction function, Integer fixedDimention
		List<Details> ts = searchPreditorsDetails(geneExpressionDataSource, idGene,
				function,
				fixedDegree);
		return reducePreditorsDetails(geneExpressionDataReduce, function, AbstractNetworkInfered.buildDetailsFromCluster(ts,
				geneExpressionDataSource));
	}
}
