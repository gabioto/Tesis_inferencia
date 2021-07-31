package inference.searchAlgoritm;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.TreeSet;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.qualifyingFunction.Details;
import inference.qualifyingFunction.QualifyingFunction;
import resources.Resources;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CanalizeSearch extends NDimencionExhaustiveSearch {

	public CanalizeSearch(int dimencion) {
		super(dimencion);
	}
	@Override
	public ArrayList<Details> searchPreditorsDetailsBody(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function) {
//		Collection<Integer> canalizants = AbstractNetwork.canalizateByDEGByGene(geneExpressionData, targetGene);
//		if (canalizants.isEmpty()) {// sin canalizadores
//			// this.dimencion--;
//			// ArrayList<Details> bestLast =
//			return super.searchPreditorsDetailsBody(geneExpressionData, targetGene, function);
////			canalizants = IntStream.range(0, geneExpressionData.getSizeGens()).boxed().collect(Collectors.toList());
////			this.dimencion++;
////			return improveListDetails(geneExpressionData, targetGene, function, bestLast, canalizants);
//		} else {// tiene canalizadores
//			return searchPreditorsDetailsBody(geneExpressionData, targetGene, function, (TreeSet<Integer>) canalizants);
//		}
		throw new NotImplementedException();

	}

	public ArrayList<Details> searchPreditorsDetailsBody(GeneExpressionData geneExpressionData, Integer targetGene,
			QualifyingFunction function, TreeSet<Integer> gensCanalizants) {
		double maxQF = Double.NEGATIVE_INFINITY;
		ArrayList<Details> listTest = new ArrayList<Details>();
		int[] setTest = new int[this.dimencion - 1];
		for (int i = 0; i < this.dimencion - 1; i++) {
			setTest[i] = i;
		}
		while (true) {
			// calcular function criterio
			cabalizant_loop: 
			for (Iterator<Integer> iterator = gensCanalizants.iterator(); iterator.hasNext();) {
				Integer iCanalizant = iterator.next();

				TreeSet<Integer> treeSetTest = new TreeSet<Integer>();
				treeSetTest.add(iCanalizant);
				for (int i = 0; i < setTest.length; i++) {
					if (!treeSetTest.add(setTest[i]))
						continue cabalizant_loop;
				}

				Details detailsTest = function.quantifyDetails(treeSetTest, geneExpressionData, targetGene);
				// verificar si mejora la funcion
				if (Resources.compareDouble(detailsTest.qualityValue, maxQF) >= 0) {// (detailsTest.qualityValue >=
																					// maxQF) {
					if (Resources.compareDouble(detailsTest.qualityValue, maxQF) > 0) {
						// actualizar valor de maxima
						listTest.clear();
						maxQF = detailsTest.qualityValue;
					}
					listTest.add(detailsTest);
				}
			}
			// ir al sigte conjunto de preditores
			if ((setTest = nextSetPreditors(setTest, geneExpressionData)) == null) {
				break;// fin de preditores
			}
		}
		return listTest;
	}

	public ID_SEARCH_ALGORITM getId() {
		return ID_SEARCH_ALGORITM.CS;
	}
}
