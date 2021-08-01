package inference;

import java.util.*;

import inference.evaluationMetric.*;
import inference.qualifyingFunction.*;
import inference.searchAlgoritm.*;

@SuppressWarnings("serial")
public class ConfigInference {
	public final int sizeData;
	public enum ID_EVALUATION_METRIC {
		IM, NO, PO, IGE, MEL
	}

	public enum ID_QUALIFYING_FUNCTION {
		SA, LG, LS, LM, LSG, LSP, GLSFS, CG, CGL
	}

	public enum ID_SEARCH_ALGORITM {
		SFS, SBS, NDES, IES, IES_SFS, CS
	}

	static final Map<ID_EVALUATION_METRIC, Object> defaultParamsEvaluationMetrics = Collections
			.unmodifiableMap(new HashMap<ID_EVALUATION_METRIC, Object>() {
				{
					put(ID_EVALUATION_METRIC.NO, new Double(1));
					put(ID_EVALUATION_METRIC.PO, new Integer(2));
				}
			});
	static final Map<ID_QUALIFYING_FUNCTION, Integer> defaultParamsQualifyingFunction = Collections
			.unmodifiableMap(new HashMap<ID_QUALIFYING_FUNCTION, Integer>() {
				{
					put(ID_QUALIFYING_FUNCTION.LS, 1);
					put(ID_QUALIFYING_FUNCTION.LM, 1);
					put(ID_QUALIFYING_FUNCTION.LSG, 2);
					put(ID_QUALIFYING_FUNCTION.LSP, 2);
					put(ID_QUALIFYING_FUNCTION.GLSFS, 2);
				}
			});

	static final Map<ID_SEARCH_ALGORITM, Object> defaultParamsSearchAlgoritm = Collections
			.unmodifiableMap(new HashMap<ID_SEARCH_ALGORITM, Object>() {
				{
					put(ID_SEARCH_ALGORITM.SFS, new Integer(10));
					put(ID_SEARCH_ALGORITM.SBS, new Integer(4));
					put(ID_SEARCH_ALGORITM.NDES, new Integer(3));
					put(ID_SEARCH_ALGORITM.IES, new Integer(4));
					put(ID_SEARCH_ALGORITM.IES_SFS, new int[] { 4, 10 });
					put(ID_SEARCH_ALGORITM.CS, new Integer(3));
				}
			});
	final Map<ID_EVALUATION_METRIC, Object> paramsEvaluationMetrics;
	final Map<ID_QUALIFYING_FUNCTION, Integer> paramsQualifyingFunction;
	final Map<ID_SEARCH_ALGORITM, Object> paramsSearchAlgoritm;

	public ConfigInference(int sizeData) {
		this(sizeData,null,null,null);
//		this.sizeData=sizeData;
//		this.paramsEvaluationMetrics = defaultParamsEvaluationMetrics;
//		this.paramsQualifyingFunction = defaultParamsQualifyingFunction;
//		this.paramsSearchAlgoritm = defaultParamsSearchAlgoritm;
	}

	public ConfigInference(int sizeData,Map<ID_EVALUATION_METRIC, Object> paramsEvaluationMetrics,
			Map<ID_QUALIFYING_FUNCTION, Integer> paramsQualifyingFunction,
			Map<ID_SEARCH_ALGORITM, Object> paramsSearchAlgoritm) {
		this.sizeData=sizeData;
		this.paramsEvaluationMetrics =(paramsEvaluationMetrics==null)?defaultParamsEvaluationMetrics: Collections.unmodifiableMap(paramsEvaluationMetrics);
		this.paramsQualifyingFunction =(paramsQualifyingFunction==null)?defaultParamsQualifyingFunction:Collections.unmodifiableMap( paramsQualifyingFunction);
		this.paramsSearchAlgoritm = (paramsSearchAlgoritm==null)?defaultParamsSearchAlgoritm:Collections.unmodifiableMap(paramsSearchAlgoritm);
	}

	public EvaluationMetric getEvaluationMetricById(ID_EVALUATION_METRIC idEvaluation) {
		//synchronized (paramsEvaluationMetrics) {
			switch (idEvaluation) {
			case IM:
				return new MutualInformation(this.sizeData);
			case NO:
				return new MutualInformationNotObservedPenality((Double) paramsEvaluationMetrics.get(idEvaluation),this.sizeData);
			case PO:
				return new MutualInformationPoorlyObserved((Integer) paramsEvaluationMetrics.get(idEvaluation),this.sizeData);
			case IGE:
				return new InvGaussianError(this.sizeData);
			case MEL:
				return new MeanErrorLine();
			default:
				return null;
			}
	//	}
	}

	public EvaluationMetric getEvaluationMetricById(ID_EVALUATION_METRIC idEvaluation, Object paramPenalization) {
		switch (idEvaluation) {
		case NO:
			return new MutualInformationNotObservedPenality((Double) paramPenalization,this.sizeData);
		case PO:
			return new MutualInformationPoorlyObserved((Integer) paramPenalization,this.sizeData);
		default:
			return null;
		}
	}

	public QualifyingFunction getQualifyingFunctionById(ID_QUALIFYING_FUNCTION idQualifying,
			ID_EVALUATION_METRIC idEvaluation) {
//		synchronized (paramsQualifyingFunction) {
			switch (idQualifying) {
			case SA:
				return new WithoutGrouping(getEvaluationMetricById(idEvaluation));
			case LG:
				return new LinearGrouping(getEvaluationMetricById(idEvaluation));
			case LS:
				return new LinearSelective(getEvaluationMetricById(idEvaluation),
						paramsQualifyingFunction.get(idQualifying));
			case LM:
				return new LinearMix(getEvaluationMetricById(idEvaluation));
			case LSG:
				return new LinearSelectiveGrouping(getEvaluationMetricById(idEvaluation),
						paramsQualifyingFunction.get(idQualifying));
			case LSP:
				return new LinearSelectiveParcial(getEvaluationMetricById(idEvaluation),
						paramsQualifyingFunction.get(idQualifying));
			case GLSFS:
				return new GroupingLatticeSFS(getEvaluationMetricById(idEvaluation),
						paramsQualifyingFunction.get(idQualifying));
			case CG:
				return new CanalizateGrouping(getEvaluationMetricById(idEvaluation));
			case CGL:
				return new CanalizateGroupingLinear(getEvaluationMetricById(idEvaluation));
			default:
				return null;
			}
	//	}
	}

	public SearchAlgoritm getSearchAlgoritmbyId(ID_SEARCH_ALGORITM idAlgoritm) {
	//	synchronized (paramsSearchAlgoritm) {
			switch (idAlgoritm) {
			case SFS:
				return new SequentialForwardSelection((Integer) paramsSearchAlgoritm.get(idAlgoritm));
//			case SBS:
//				return new SequentialBackwardSelection((Integer) paramsSearchAlgoritm.get(idAlgoritm));
			case NDES:
				return new NDimencionExhaustiveSearch((Integer) paramsSearchAlgoritm.get(idAlgoritm));
			case IES:
				return new IncrementalExhaustiveSearch((Integer) paramsSearchAlgoritm.get(idAlgoritm));
			case IES_SFS:
				return new IncrementalExhaustiveSearchWithSFS((int[]) paramsSearchAlgoritm.get(idAlgoritm));
			case CS:
				return new CanalizeSearch((Integer) paramsSearchAlgoritm.get(idAlgoritm));
			default:
				return null;
			}
	//	}
	}
}
