package inference.qualifyingFunction;

import java.util.*;

import geneticNetwork.AbstractNetwork;
import geneticNetwork.GeneExpressionData;
import inference.AbstractNetworkInfered;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import resources.Resources;

public class WithoutGrouping implements QualifyingFunction {
//	private Resources Resources=new Resources();
	protected EvaluationMetric evaluationMetric;
	//protected boolean isWhitFrequencytable;
	public WithoutGrouping(EvaluationMetric evaluationMetric) {
		super();
		this.evaluationMetric = evaluationMetric;
		//this.isWhitFrequencytable=isWhitFrequencytable;
	}


	//@Override
	//public Double quantify(TreeSet<Integer> preditors, boolean[][] geneExpressionData, int geneTarget) {
		//return quantifyDetails(preditors, geneExpressionData, geneTarget).qualityValue;
	//}

//	@Override
	public Details quantifyDetails(NavigableSet<Integer> preditors, GeneExpressionData geneExpressionData, Integer geneTarget) {
		Details details = new Details();
		details.idGene=geneTarget;
		int[][] frequencyTable = AbstractNetwork.buildFrequencyTable(preditors, geneExpressionData, geneTarget);
		details.preditors = preditors;
		details.qualityValue=evaluationMetric.quantifyFromFrequencyTable(frequencyTable);
		details.frequencyTable=frequencyTable;
//		details.groupsTable=null;
		return details;
	}
//	@Override
	public Double quantifyDiscrete(NavigableSet<Integer> preditors, GeneExpressionData gdd, Integer geneTarget) {
		int[][] frequencyTable=AbstractNetworkInfered.buildFrequencyTable(preditors,
				gdd, geneTarget);
		return evaluationMetric.quantifyFromFrequencyTable(frequencyTable);	
	}
	public void showDetail(Details details) {
		System.out.println(this.getName());
		System.out.println("Preditors:" +details.preditors);
		System.out.println("CF ("+ evaluationMetric.toShortString() +"):"+details.qualityValue);
		System.out.print("Frequency table:");
		System.out.println(details.frequencyTableString());
	}
	public String getName() {
		return "Sem Agrupar";
	}

	public String toShortString() {
		return getId()+"_"+evaluationMetric.toShortString();
	}

	@Override
	public EvaluationMetric getEvaluationMetric() {
		return evaluationMetric;
	}

	@Override
	public void buildGroupingDetails(Details details) {
//		details.functions= new long[details.frequencyTable.length][][];
//		int[][] frequencyTable=details.frequencyTable[0];
		//construir funciones booleanas
		byte[] function=new byte[details.frequencyTable.length];
		long functionLong=0L;
		List<Integer> positionsNone=new ArrayList<>();
		for (int i = 0; i < details.frequencyTable.length; i++) {
			if (details.frequencyTable[i][1] > details.frequencyTable[i][0]) {
				function[i]=Details.ONE;
				functionLong=functionLong|(1L<<i);
			}
			else if(details.frequencyTable[i][1] < details.frequencyTable[i][0])
				function[i]=Details.ZERO;
			else if (details.frequencyTable[i][1] == details.frequencyTable[i][0]) {
				function[i]=Details.NONE;
				positionsNone.add(i);
					
//				int lbt=booleanTables.size();
//				for (int j = 0; j < lbt; j++) {
//					long[]bt =Arrays.copyOf(booleanTables.get(j),nf);
//					bt[i / 64] = bt[i / 64] | ((long) 1 << i);
//					booleanTables.add(bt);
//				}
			}
		}
		if(details.preditors.size()>Resources.MAX_REDUCIBLE) {
			details.function=function;
			details.isReductora=false;
		}	
		else {
			ArrayList<Long> functionsNotR=new ArrayList<>();
			long mask=0L;
			do {
				long testF=functionLong|mask;
				if(!Resources.isReducible(details.preditors.size(),testF ))
					functionsNotR.add(testF);
				mask=maskFunction(positionsNone, mask);
			}while(mask!=0L);
			
			//verificar estado de los NONES
			for (Iterator<Integer> it = positionsNone.iterator(); it.hasNext();) {
				Integer i=it.next();
				function[i]=stateBitFunction(functionsNotR, i);
			}
			details.function=function;
			details.isReductora=functionsNotR.isEmpty();
		}
//		//verificar funciones reducibles y asignar peso
//		for (int j = 0; j < booleanTables.size(); j++) {
//			if(booleanTables.get(j).length==1)
//			if(Resources.isReducible(details.preditors.size(), booleanTables.get(j)[0])) {
//				booleanTables.remove(j);
//				j--;
//			}
//		}
//		details.function=new long[booleanTables.size()][];
//		for (int i = 0; i < booleanTables.size(); i++) {
//			details.function[i]=booleanTables.get(i);	
//		}
		
		details.entropyX= Resources.entropyLine(Resources.mergeTable(details.frequencyTable));
	}
	private static long maskFunction(List<Integer>positions, long mask)
	{
		for (int i = 0; i < positions.size(); i++) {
			//si tem um zero
			long nvaMask;
			long bitPos=(1L<<positions.get(i));
			if((nvaMask=(mask | bitPos))!=mask)
				return nvaMask;
			else//tiene un uno volver 0
				mask=mask^bitPos;
		}
		return mask;
	}
	private byte stateBitFunction(List<Long> functionsNotR, Integer nbit) {
		byte state =Details.NONE;
		long bitState=1L<<nbit;
		for (Iterator<Long> it = functionsNotR.iterator(); it.hasNext();) {
			if((it.next()&bitState)==bitState) {
				if(state==Details.IZERO)
					return Details.NONE;
				state=Details.IONE;
			}else {
				if(state==Details.IONE)
					return Details.NONE;
				state=Details.IZERO;
			}
		}
		return state;
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		return ID_QUALIFYING_FUNCTION.SA;
	}
//	public static void main(String[] arg) {
//		long mask=0L;
//		ArrayList<Integer> pos=new ArrayList<>();
//		pos.add(0);pos.add(3);pos.add(4);
//		
//		do {
//			System.out.println(Long.toBinaryString(mask));
//			mask=maskFunction(pos, mask);
//		}while(mask!=0l);		
//	}
}
