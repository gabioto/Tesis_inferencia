package inference.qualifyingFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.evaluationMetric.EvaluationMetric;
import javafx.util.Pair;
import resources.Resources;
import static resources.Resources.pow;

public class CanalizateGrouping extends AbstractGrouping{
	//private Resources Resources=new Resources();
	public CanalizateGrouping(EvaluationMetric evaluationMetric) {
		super(evaluationMetric);
	}

	@Override
	protected int[][][] buildTablesGruping(int[][] frequencyTable, int sizePreditors) {
		ArrayList<int[]> canalizantes=computeCanalizant(frequencyTable, sizePreditors);

		int[][][] tableGrouping =new int[canalizantes.size()][frequencyTable.length/2 +1][];
//		int[][][] newGroups =(isWhitFrequencytable)?new int[canalizantes.size()][frequencyTable.length/2 +1][]: null;
		for (int i = 0; i < tableGrouping.length; i++) {
			int ican=canalizantes.get(i)[0];
			long nleng = 1L << ican;
			int d = 0;
			int[] canalizeGroupa= {0,0};
			int[] groupC= new int[frequencyTable.length/2];
			int ig=0;
			int igc=0;
			for (int j = 0; j < frequencyTable.length; j++) {
				if (j % nleng == 0)
					d = (d + 1) % 2;
				//calcular frecuencias de posibles genes canalizadores
				if (d==canalizantes.get(i)[1]) {
					Resources.mergeAddMatrix(canalizeGroupa, frequencyTable[j]);
					groupC[igc++]=j;
				}
				else {
					tableGrouping[i][ig]=frequencyTable[j];
//					if(isWhitFrequencytable)newGroups[i][ig]= new int[]{j};
					ig++;
				}
			}
			tableGrouping[i][ig]=canalizeGroupa;
//			if(isWhitFrequencytable)newGroups[i][ig]=groupC;
//			if(isWhitFrequencytable) aplicatedGroups=newGroups;
		}
		return tableGrouping;
	}
	@Override
	protected int[][][] buildSaveTablesGruping(int[][] frequencyTable, int sizePreditors) {
		ArrayList<int[]> canalizantes=computeCanalizant(frequencyTable, sizePreditors);
	
		int[][][] tableGrouping =new int[canalizantes.size()][frequencyTable.length/2 +1][];
		int[][][] newGroups =new int[canalizantes.size()][frequencyTable.length/2 +1][];
		for (int i = 0; i < tableGrouping.length; i++) {
			int ican=canalizantes.get(i)[0];
			long nleng = 1L << ican;
			int d = 0;
			int[] canalizeGroupa= {0,0};
			int[] groupC= new int[frequencyTable.length/2];
			int ig=0;
			int igc=0;
			for (int j = 0; j < frequencyTable.length; j++) {
				if (j % nleng == 0)
					d = (d + 1) % 2;
				//calcular frecuencias de posibles genes canalizadores
				if (d==canalizantes.get(i)[1]) {
					Resources.mergeAddMatrix(canalizeGroupa, frequencyTable[j]);
					groupC[igc++]=j;
				}
				else {
					tableGrouping[i][ig]=frequencyTable[j];
					newGroups[i][ig]= new int[]{j};
					ig++;
				}
			}
			tableGrouping[i][ig]=canalizeGroupa;
			newGroups[i][ig]=groupC;			
		}
		aplicatedGroups=newGroups;
		return tableGrouping;
	}
	//arreglo de duplas de genes canalizadores y estado de cabalizacion (con menor entropia)
	//primer dato gen q canaliza . segundo dato 0-> si canaliza en 1 y 1 si canaliza en 0 (si ya se que es medio gil)
	private ArrayList<int[]> computeCanalizant(int[][] frequencyTable, int sizePreditors) {
		long nbits = 1L << sizePreditors;
		double minEntropy=Double.MAX_VALUE;
		//int nObservaciones=-1;
		//int nHuecos=Integer.MAX_VALUE;
		ArrayList<int[]> canalizantes=new ArrayList<>();
		for (int ican = 0; ican < sizePreditors; ican++) {
			long nleng = 1L << ican;
			int d = 0;
			int[][] canalizeGroupa= {{0,0},{0,0}};
			int[] huecos= {0,0};
			for (int j = 0; j < nbits; j++) {
				if (j % nleng == 0)
					d = (d + 1) % 2;
				//calcular frecuencias de posibles genes canalizadores
				Resources.mergeAddMatrix(canalizeGroupa[d], frequencyTable[j]);
				if(frequencyTable[j][0]+frequencyTable[j][1]==0)
					huecos[d]++;
			}
			for (int i = 0; i < canalizeGroupa.length; i++) {
				//comparar las entropias
				double entrop=Resources.entropyLine(canalizeGroupa[i]);
//				int nOb=canalizeGroupa[i][0]+canalizeGroupa[i][1];
				//if (entrop<minEntropy) {
				if(Resources.compareDouble(entrop, minEntropy)<0) {
					canalizantes.clear();
//					nHuecos=huecos[i];
//					nObservaciones=nOb;
					canalizantes.add(new int[] {ican, i});
					minEntropy=entrop;
				}else if(Resources.compareDouble(entrop, minEntropy)==0) {//(entrop == minEntropy) {
//					if(huecos[i]<nHuecos) {//tiene menos huecos
//						canalizantes.clear();
//						nHuecos=huecos[i];
//						nObservaciones=nOb;
//						canalizantes.add(new int[] {ican, i});
//					}else if(huecos[i]==nHuecos) {
//						if(nObservaciones<nOb) {//tiene mas observaciones
//							canalizantes.clear();
//							nObservaciones=nOb;
//							canalizantes.add(new int[] {ican, i});
//						}else if(nOb==nObservaciones)
							canalizantes.add(new int[] {ican, i});
//					}
				}
			}
		}
		return canalizantes;
	}
	@Override
	public String getName() {
		return "Canalize group";
	}
	@Override
	public ID_QUALIFYING_FUNCTION getId() {
		return ID_QUALIFYING_FUNCTION.CG;
	}

	@Override
	protected int[][][] buildTablesGrupingDiscret(int[][] frequencyTable, int sizePreditors) {
		int sizeDiscretes=frequencyTable[0].length;
		ArrayList<Pair<Integer, Pair<Integer, int[]>>> canalizantes=computeCanalizantDiscrete(frequencyTable, sizePreditors);

		int[][][] tableGrouping =new int[canalizantes.size()][pow(sizeDiscretes,sizePreditors)-(pow(sizeDiscretes,sizePreditors-1))+1][];
//		int[][][] newGroups =(isWhitFrequencytable)?new int[canalizantes.size()][frequencyTable.length/2 +1][]: null;
		for (int i = 0; i < tableGrouping.length; i++) {
			int ican=canalizantes.get(i).getKey();
			long nleng = pow(sizeDiscretes , ican);;
			int d = 0;
			int[] canalizeGroupa= canalizantes.get(i).getValue().getValue();
//			int[] groupC= new int[frequencyTable.length/2];
			int ig=0;
//			int igc=0;
			for (int j = 0; j < frequencyTable.length; j++) {
				//calcular frecuencias de posibles genes canalizadores
//				if (d==canalizantes.get(i).getValue().getKey()) {
//					//Resources.mergeAddMatrix(canalizeGroupa, frequencyTable[j]);
////					groupC[igc++]=j;
//				}
//				else
				if (d!=canalizantes.get(i).getValue().getKey()) {
					tableGrouping[i][ig]=frequencyTable[j];
//					if(isWhitFrequencytable)newGroups[i][ig]= new int[]{j};
					ig++;
				}
				//verificar cambio de bit
				if ((j+1) % nleng == 0)
					d = (d + 1) % sizeDiscretes;
			}
			tableGrouping[i][ig]=canalizeGroupa;
//			if(isWhitFrequencytable)newGroups[i][ig]=groupC;
//			if(isWhitFrequencytable) aplicatedGroups=newGroups;
		}
		return tableGrouping;
	}
//lista de pares de gene canalizador, estado canalizador con menor entropia (este si es el estado q canaliza, no es tab gil como la version binaria)
	private ArrayList<Pair<Integer,Pair<Integer,int[]>>> computeCanalizantDiscrete(int[][] frequencyTable, int sizePreditors) {
		int sizeDriscretes=frequencyTable[0].length;
		double minEntropy=Double.MAX_VALUE;
		//int nObservaciones=-1;
		//int nHuecos=Integer.MAX_VALUE;
		ArrayList<Pair<Integer,Pair<Integer,int[]>>> canalizantes=new ArrayList<>();
		for (int ican = 0; ican < sizePreditors; ican++) {
			long nleng = pow(sizeDriscretes , ican);
			int d = 0;
			int[][] canalizeGroupa= new int[sizeDriscretes][sizeDriscretes];
//			int[] huecos= new int[sizeDriscretes];
			for (int j = 0; j < frequencyTable.length; j++) {
				//calcular frecuencias de posibles genes canalizadores
				Resources.mergeAddMatrix(canalizeGroupa[d], frequencyTable[j]);
//				if(IntStream.of( frequencyTable[j]).sum() ==0)
//					huecos[d]++;
				//verificar cambio de bit
				if ((j+1) % nleng == 0)
					d = (d + 1) % sizeDriscretes;
			}
			for (int i = 0; i < canalizeGroupa.length; i++) {
				//comparar las entropias
				double entrop=Resources.entropyLine(canalizeGroupa[i]);
				if(Resources.compareDouble(entrop, minEntropy)<0) {
					canalizantes.clear();
					canalizantes.add(new Pair<>(ican, new Pair<>(i, canalizeGroupa[i])));
					minEntropy=entrop;
				}else if(Resources.compareDouble(entrop, minEntropy)==0) {
					canalizantes.add(new Pair<>(ican, new Pair<>(i, canalizeGroupa[i])));
				}
			}
		}
		return canalizantes;
	}
	public static void main(String arg[]) {
		int[][] ft=new int[][] {
		   //0 1
			{0,1}, //000
			{0,1}, //001
			{0,3}, //010
			{0,1}, //011
			{3,0}, //100
			{3,0}, //101
			{0,2}, //110
			{0,3}  //111
			};
			CanalizateGrouping cg =new CanalizateGrouping(null);
			System.out.println(cg.computeCanalizant(ft, 3).stream()
					.map(Arrays::toString).collect(Collectors.joining("\n")));
			System.out.println(Stream.of(cg.buildTablesGruping(ft, 3))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			System.out.println(cg.computeCanalizantDiscrete(ft, 3).stream()
					.map(e->e.getKey()+"->"
							+e.getValue().getKey()+":"+
							Arrays.toString(e.getValue().getValue())).collect(Collectors.joining("\n")));
			System.out.println(Stream.of(cg.buildTablesGrupingDiscret(ft, 3))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			
			int[][] ft3=new int[][] {
				{0,0,1}, //00
				{0,0,1}, //01
				{0,2,0}, //02
				{0,0,1}, //10
				{0,0,1}, //11
				{0,0,2}, //12
				{0,0,1}, //20
				{0,1,0}, //21
				{0,0,1}  //22
//				,
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,0,1},
//				{0,5,1}
			};
			System.out.println(cg.computeCanalizantDiscrete(ft3, 2).stream()
					.map(e->e.getKey()+"->"
							+e.getValue().getKey()+":"+
							Arrays.toString(e.getValue().getValue())).collect(Collectors.joining("\n")));
			System.out.println(Stream.of(cg.buildTablesGrupingDiscret(ft3, 2))
					.map(Arrays::deepToString).collect(Collectors.joining("\n")));
			
	}
}
