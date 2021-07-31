package inference.qualifyingFunction;

import inference.GeneInfered;
import resources.Resources;

public class DetailsGroup extends Details {
	public int[][][] frequencyTableGroup;
	public int[][][] groupsTable;
	public byte[][] functionsGroup;

	public String frequencyTableString() {
		StringBuilder sb = new StringBuilder();
		int sizePreditors = this.preditors.size();
		StringBuilder sbTitleTable = new StringBuilder("\n");
		for (Integer is : preditors.descendingSet()) {
			sbTitleTable.append(is).append("\t");
		}
		sbTitleTable.append("0s\t1s\n");

		for (int g = 0; g < this.groupsTable.length; g++) {
			// long tb = 0;
			StringBuilder sbGroup = new StringBuilder().append(sbTitleTable);
			for (int i = 0; i < this.frequencyTableGroup[g].length; i++) {
				for (int j = 0; j < this.groupsTable[g][i].length; j++) {
					int l = this.groupsTable[g][i][j];
					if (j > 0)
						sbGroup.append("\n");
					sbGroup.append(Resources.toTrueTableFormat(l, sizePreditors).replaceAll("([0-1])", "$1\t"));
					// tb += (this.frequencyTable[g][i][1] > this.frequencyTable[g][i][0]) ? ((long)
					// 1 << l) : 0L;
				}
				sbGroup.append(this.frequencyTableGroup[g][i][0]).append("\t")
						.append(this.frequencyTableGroup[g][i][1]);
				if (this.frequencyTableGroup[g][i][1] == this.frequencyTableGroup[g][i][0]) {
					if (functionsGroup[g][i]!=NONE) {
						// long v = functions[g][0][0] & (1L << this.groupsTable[g][i][0]);
//						sbGroup.append("\t--> ").append(simbTab(this.groupsTable[g][i][0], functionsGroup[g]));
						sbGroup.append("\t--> ").append(functionsGroup[g][i] & (byte) 1);
					} else
						sbGroup.append("\t--> ?");
				}
				sbGroup.append("\n");

			}
			sbGroup.insert(0, Details.functionString(functionsGroup[g], sizePreditors));
			sb.append(sbGroup);
		}
		return sb.toString();
	}
	public GeneInfered toGeneInfered() {
		int nlinhas = 1 << this.preditors.size();
		int indexTable=this.frequencyTableGroup.length-1;
		if(indexTable>0)
			indexTable=Resources.random.nextInt(this.frequencyTableGroup.length);
		int[][] frequencyTable = this.frequencyTableGroup[indexTable];
		//int[][] frequencyTable = this.frequencyTable;
		long[] booleanTable = new long[nlinhas / 64 + 1];
		long[] stateTable = new long[nlinhas / 64 + 1];
		long[] stateZ = new long[nlinhas / 64 + 1];
		for (int i = 0; i < booleanTable.length; i++) {
			stateTable[i] = -1L;
			stateZ[i] = -1L;
		}
		// gene.setGlobal_boolean_values(resources.sumFrequency(frequencyTable));
		// if(frequencyTable.length==nlinhas) {// nao tem agrupamento
//		if(this.functionsGroup[indexTable].length==1)
//			booleanTable=this.functionsGroup[indexTable][0];
//		else{
		int[] sft = Resources.sumFrequency(frequencyTable);
			int[][] groupTable = this.groupsTable[indexTable];
			for (int i = 0; i < frequencyTable.length; i++) {
				if ((frequencyTable[i][1] > frequencyTable[i][0])) {
//				if (functionsGroup[indexTable][i] == ONE || functionsGroup[indexTable][i] == IONE)
					for (int j = 0; j < groupTable[i].length; j++) {
						int index = groupTable[i][j];
						booleanTable[index / 64] = booleanTable[index / 64] | (1L << index);
					}
				} else if (frequencyTable[i][1] == frequencyTable[i][0]) {
//				else if (functionsGroup[indexTable][i] == NONE) {
					for (int j = 0; j < groupTable[i].length; j++) {
						int index = groupTable[i][j];
						stateTable[index / 64] = stateTable[index / 64] ^ (1L << index);
						if (frequencyTable[i][1] == 0)
							stateZ[index / 64] = stateZ[index / 64] ^ (1L << index);
						if (sft[1] > sft[0])
							booleanTable[index / 64] = booleanTable[index / 64] | (1L << index);
					}
				}
			}
		return new GeneInfered(this.idGene,this.preditors, booleanTable, stateTable, stateZ, this.qualityValue, sft);
	}
}
