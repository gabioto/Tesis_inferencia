package inference.qualifyingFunction;

import java.util.NavigableSet;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;

public interface QualifyingFunction {
	
	EvaluationMetric getEvaluationMetric();
	String getName();
	//String getShortName();
	String toShortString();
	//Double quantify(TreeSet<Integer> preditors,boolean[][] geneExpressionData, int geneTarget);
//	Details quantifyDetails(TreeSet<Integer> preditors,BooleanGeneExpressionData booleanGeneExpressionData, int geneTarget);
//	Double quantifyDiscrete(TreeSet<Integer> preditors,DiscreteGeneExpressionData gdd, int geneTarget);
	Details quantifyDetails(NavigableSet<Integer> preditors,GeneExpressionData geneExpressionData, Integer geneTarget);
	void showDetail(Details details);
	void buildGroupingDetails(Details details);
	//String frequencyTableString(Details details);
	ID_QUALIFYING_FUNCTION getId();
//	Details quantifyDetails(TreeSet<Integer> treeSetTest, GeneExpressionData geneExpressionData, Integer targetGene);

}
