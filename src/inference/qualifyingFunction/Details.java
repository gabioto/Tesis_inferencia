package inference.qualifyingFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import inference.GeneInfered;
import resources.Resources;

public class Details {
	public NavigableSet<Integer> preditors;
	public int[][] frequencyTable;
	public Double qualityValue;
	public byte[] function;
	public boolean isReductora;
	public double entropyX;
	public int idGene;
	public static final byte ZERO  = 0;// 000;
	public static final byte ONE   = 1;// 001;
	public static final byte IZERO = 2;// 010;
	public static final byte IONE  = 3;// 011;
	public static final byte NONE  = 7;// 111;
	// Resources Resources = new Resources();

	public String toPreditorsString() {
		StringBuilder sb = new StringBuilder();
		sb.append(preditors.size()).append(":").
			append(preditors).append("\n").append("qf:").append(qualityValue).append("\n").append(frequencyTableString());
		return sb.toString();
	}

	public String frequencyTableString() {
		return frequencyTableString(preditors, this.frequencyTable, function);
	}



//	protected String simbTab(int pos, long[][] functions) {
//		int valInt = (((1L << (pos % 64)) & functions[0][pos / 64]) == 0) ? 0 : 1;
//		for (int i = 1; i < functions.length; i++) {
//			int valf = (((1L << (pos % 64)) & functions[i][pos / 64]) == 0) ? 0 : 1;
//			if (valInt != valf)
//				return "?";
//		}
//		return String.valueOf(valInt);
//	}



	public GeneInfered toGeneInfered() {
		int nlinhas = 1 << this.preditors.size();
//		int indexTable=Resources.random.nextInt(this.frequencyTable.length);
//		int[][] frequencyTable = details.frequencyTable[indexTable];
		long[] booleanTable = new long[nlinhas / 64 + 1];
		long[] stateTable = new long[nlinhas / 64 + 1];
		long[] stateZ = new long[nlinhas / 64 + 1];
		for (int i = 0; i < booleanTable.length; i++) {
			stateTable[i] = -1L;
			stateZ[i] = -1L;
		}
		// gene.setGlobal_boolean_values(resources.sumFrequency(frequencyTable));
		// if(frequencyTable.length==nlinhas) {// nao tem agrupamento
//		if(this.function.length==1)
//			booleanTable=this.function[0];
//		else
		int[] sft = Resources.sumFrequency(frequencyTable);
		try {
		for (int i = 0; i < frequencyTable.length; i++) {
			if (function[i] == ONE || function[i] == IONE)
				booleanTable[i / 64] = booleanTable[i / 64] | ((long) 1 << i);
			else if (function[i] == NONE) {
				stateTable[i / 64] = stateTable[i / 64] ^ ((long) 1 << i);
				if (frequencyTable[i][1] == 0)
					stateZ[i / 64] = stateZ[i / 64] ^ ((long) 1 << i);
				if (sft[1] > sft[0])
					booleanTable[i / 64] = booleanTable[i / 64] | ((long) 1 << i);
			}
		}
		}catch(NullPointerException ne) {
			System.out.println(preditors);
			System.out.println(idGene);
			System.out.println(qualityValue);
			throw ne;
		}
		return new GeneInfered(this.idGene, this.preditors, booleanTable, stateTable, stateZ, this.qualityValue, sft);
	}

	public static double distanceTables(int[][] table1, int[][] table2) {
		int dif = 0, tot = 0;
		for (int i = 0; i < table1.length; i++) {
			for (int j = 0; j < table1[i].length; j++) {
				tot += table1[i][j] + table2[i][j];
				dif += Math.abs(table1[i][j] - table2[i][j]);
			}
		}
		return (double) dif / tot;
	}

	@Override
	public int hashCode() {
		int i = 0;
		int p = 0;
		for (Iterator<Integer> iterator = preditors.iterator(); iterator.hasNext(); p += 2) {
			i += iterator.next() * Math.pow(10, p);
		}
		return i;
	}
	//static method
	public static boolean existDetails(List<Details> listTest, NavigableSet<Integer> preditorsTest) {
		for (Details details : listTest) {
			if (details.preditors.equals(preditorsTest))
				return true;
		}
		return false;
	}
	public static String frequencyTableString(NavigableSet<Integer>predictors,
			int[][] frequencyTable,
			byte[] function) {
		StringBuilder sb = new StringBuilder();
		int sizePreditors = predictors.size();
		int sizeDiscreteValues = frequencyTable[0].length;
		StringBuilder sbTitleTable = new StringBuilder();
		for (Integer is : predictors.descendingSet()) {
			sbTitleTable.append(is).append("\t");
		}
		sbTitleTable.append(IntStream.range(0, sizeDiscreteValues)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining("s\t")))
		        .append("s\n");
		sb.append(sbTitleTable);
		// long tb = 0;
		for (int i = 0; i < frequencyTable.length; i++) {
			sb.append(Resources.toTrueTableFormat(i, sizePreditors,sizeDiscreteValues)
					.replaceAll("([0-"+sizeDiscreteValues+"])", "$1\t"))
			  //.append(frequencyTable[i][0]).append("\t").append(frequencyTable[i][1]);
			.append(IntStream.of(frequencyTable[i]).mapToObj(String::valueOf).collect(Collectors.joining("\t")));
			if(sizeDiscreteValues<3)
			if (frequencyTable[i][1] == frequencyTable[i][0]) {
				// long v = functions[0][0][0] & (1L << i);
				if (function != null && function.length > 0)
				if (function[i] != Details.NONE)
					sb.append("\t--> ").append(function[i] & (byte) 1);//append((function[i]==IZERO)?0:1);
				else
					sb.append("\t--> ?");
			}
			sb.append("\n");
		}
		if (function != null && function.length > 0)
			sb.insert(0, "\n").insert(0, functionString(function, sizePreditors));
		return sb.toString();
	}
	public static String functionString(byte[] function, int nPreditors) {
		StringBuilder sb = new StringBuilder();
		for (int i = (1 << nPreditors) - 1; i >= 0; i--) {
			if (function[i] == Details.NONE)
				sb.append("?");
			else {
				if ((function[i] & (1 << 1)) != 0)
					sb.append("_");
				sb.append(function[i] & (byte) 1);
			}
		}
		return sb.toString();
	}
	
	public static Details cloneof(Details original) {
		Details clone=new Details();
		if(original.preditors==null)
		clone.preditors=null;
		else {
			clone.preditors=new TreeSet<Integer>();
			clone.preditors.addAll(original.preditors);
		}
		clone.frequencyTable=(original.frequencyTable==null)?null:original.frequencyTable.clone();
		clone.qualityValue=original.qualityValue;
		clone.function=(original.function==null)?null:original.function.clone();
		clone.isReductora=original.isReductora;
		clone.entropyX=original.entropyX;
		clone.idGene=original.idGene;
		return clone;
	}
	public static Details cloneof(Details original, Integer idGene) {
		Details clone=Details.cloneof(original);
		clone.idGene=idGene;
		return clone;
	}
	public static List<Details> cloneListof(List<Details> original, Integer idGene) {
		List<Details> clone=new ArrayList<>();
		for (Details details : original) {
			clone.add(Details.cloneof(details, idGene));
		}
		return clone;
	}
}
