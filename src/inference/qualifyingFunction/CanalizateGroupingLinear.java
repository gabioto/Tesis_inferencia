package inference.qualifyingFunction;

import java.util.TreeSet;
import geneticNetwork.BooleanGeneExpressionData;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CanalizateGroupingLinear extends CanalizateGrouping {
//	private int currentGene = -1;
//	private TreeSet<Integer> canalizingGens;

	public CanalizateGroupingLinear(EvaluationMetric evaluationMetric) {
		super(evaluationMetric);
	}

	//@Override
	public Details quantifyDetails(TreeSet<Integer> preditors, BooleanGeneExpressionData booleanGeneExpressionData, int geneTarget) {
//		if (geneTarget != currentGene) {
//			canalizingGens = AbstractNetwork.canalizingByDEGByGene(booleanGeneExpressionData, geneTarget);
//			currentGene=geneTarget;
//		}
//		if (!canalizingGens.isEmpty() && Collections.disjoint(canalizingGens, preditors)) {
//			Details d = new Details();
//			d.idGene=geneTarget;
//			d.qualityValue = -10.0;
//			return d;
//		}
//		return super.quantifyDetails(preditors, booleanGeneExpressionData, geneTarget);
		throw new NotImplementedException();
	}
	@Override
	public String getName() {
		return "Canalize Group Linear";
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		// TODO Auto-generated method stub
		return ID_QUALIFYING_FUNCTION.CGL;
	}
}
