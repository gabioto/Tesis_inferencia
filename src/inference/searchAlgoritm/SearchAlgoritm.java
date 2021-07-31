package inference.searchAlgoritm;

import java.util.Deque;
import java.util.List;

import geneticNetwork.GeneExpressionData;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.evaluationMetric.EvaluationMetric;
import inference.qualifyingFunction.Details;
import inference.qualifyingFunction.QualifyingFunction;
import javafx.util.Pair;

public interface SearchAlgoritm {
	//ArrayList<TreeSet<Integer>> searchPreditors(boolean[][] geneExpressionData, Integer targetGene, QualifyingFunction function);
	List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			Integer fixedDimention);
	List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function);
	List<Details> searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction mainFunction,
			EvaluationMetric additionalMetric);
//	ArrayList<Details> searchPreditorsDetails(BooleanGeneExpressionData booleanGeneExpressionData,
//			Integer targetGene,
//			QualifyingFunction function,
//			int fixedDimention);
//	ArrayList<Details> searchPreditorsDetails(BooleanGeneExpressionData booleanGeneExpressionData,
//			Integer targetGene,
//			QualifyingFunction function);
//	String getShortName();
	ID_SEARCH_ALGORITM getId();
//	Pair<ArrayList<Details>, LinkedList<Double>[]> searchPreditorsDetails(
//			BooleanGeneExpressionData booleanGeneExpressionData,
//			Integer targetGene,
//			QualifyingFunction function,
//			boolean printSteps);
	Pair<List<Details>, Deque<Double>[]> searchPreditorsDetails(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction function,
			EvaluationMetric additionalMetric,
			boolean printSteps);
	Pair<List<Details>,Deque<Double>[]> searchPreditorsDetailsBody(
			GeneExpressionData geneExpressionData,
			Integer targetGene,
			QualifyingFunction mainFunction,
			EvaluationMetric additionalMetric,
			boolean printSteps,
			boolean stopEvolution);
//	Pair<ArrayList<Details>, LinkedList<Double>[]> searchPreditorsDetails(
//			DiscreteGeneExpressionData gdd,
//			Integer targetGene,
//			QualifyingFunction function,
//			boolean printSteps);
//	Pair<ArrayList<Details>, LinkedList<Double>[]> searchPreditorsDetails(
//			BooleanGeneExpressionData geneExpressionData,
//			DiscreteGeneExpressionData gdd,
//			Integer targetGene,
//			QualifyingFunction function,
//			boolean printSteps);
	Details[] searchPreditorsDetails(GeneExpressionData geneExpressionData,
			Integer targetGene,
			Integer countTop,
			QualifyingFunction function);
	Deque<Double>[] evolutionQualityFunction(GeneExpressionData ged, Integer geneTarget,
			QualifyingFunction function, boolean stopEvolution);
//	Details[] searchPreditorsDetails(BooleanGeneExpressionData booleanGeneExpressionData,
//			Integer targetGene,
//			Integer countTop,
//			QualifyingFunction function);
//	LinkedList<Double>[] evolutionQualityFunction(BooleanGeneExpressionData ged, Integer geneTarget,
//			QualifyingFunction function, boolean stopEvolution);
	
}
