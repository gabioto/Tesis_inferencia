package geneticNetwork;

import java.io.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toCollection;
import javafx.util.Pair;
import resources.Resources;

public class BooleanGeneExpressionData  implements GeneExpressionData{
	final private boolean[][] data;//matriz de expressao
	public final int sizeGens;//nro genes
	public final int sizeData;//instantes de tempo
	private List<Integer> validGens;//genes q tem pelo meno a minima variavilidade
	private double porcentageValue;//perventagem minimo de variablidades
	private Map<Integer, List<Integer>> clusterForRepeat;//cluster por repetição de expressao
	
	public boolean containsClusterKey(Integer key) {
		return clusterForRepeat.containsKey(key);
	}
	public Set<Integer> clusterForRepeatKeys(){
		return this.clusterForRepeat.keySet();
	}
	public List<Integer> getCluster(Integer idgene){
		return clusterForRepeat.get(getIdCluster(idgene));
	}
	public Integer getIdCluster(Integer idgene){
		if(clusterForRepeat.containsKey(idgene))
			return idgene;
		else {
			Set<Integer> keys=clusterForRepeat.keySet();
			for (Integer i : keys) {
				List<Integer> v=clusterForRepeat.get(i);
				if(v.contains(idgene))
					return i;	
			}
		}
		return null;
	}
	public BooleanGeneExpressionData customGeneData(List<Integer> customGens){
		boolean[][] sample =new boolean[this.sizeData][customGens.size()];
		for (int i = 0; i < this.sizeData; i++) {
			for (int j=0; j < sample[i].length; j++) {
				sample[i][j]=this.getBooleanData(i, customGens.get(j));
			}
		}
		return new BooleanGeneExpressionData(sample);
	}
	public Pair<BooleanGeneExpressionData, boolean[]> divideData(int posCut){
		boolean[][] sample =new boolean[this.sizeData-1][];
		int index=0;
		for (int i = posCut+1; i < this.sizeData; i++) {
			sample[index++]=this.getData(i);
		}
		for (int i = 0; i < posCut; i++) {
			sample[index++]=this.getData(i);
		}
		return new Pair<>(new BooleanGeneExpressionData(sample),this.getData(posCut));
	}
	public Pair<BooleanGeneExpressionData, BooleanGeneExpressionData> divideData(int posCut, int countTest){
		boolean[][] sample =new boolean[this.sizeData-countTest][];
		boolean[][] test =new boolean[countTest][];
		int endCut=(posCut+countTest) % this.sizeData;
		int i=0;
		int index=endCut;
		while(true) {
			sample[i++]=this.getData(index);
			if((index=(index+1)%this.sizeData)==posCut)
				break;
		}
		i=0;
		while(true) {
			test[i++]=this.getData(posCut);
			if((posCut=(posCut+1)%this.sizeData)==endCut)
				break;
		}
		return new Pair<>(new BooleanGeneExpressionData(sample, this.clusterForRepeat),new BooleanGeneExpressionData(test, this.clusterForRepeat));
	}
	public BooleanGeneExpressionData(String fileData, boolean buildClusterForRepeat) {
		Pair<Double, boolean[][]> read=readDeg(fileData);
		this.data = read.getValue();
		this.porcentageValue=read.getKey();
		this.sizeGens = data[0].length;
		this.sizeData = data.length;
		this.validGens=prePrecessData();
		this.clusterForRepeat=(buildClusterForRepeat)? clusterForRepeat():notCluster();
	}
	public static BooleanGeneExpressionData 
		importFrom(String nameFile,
				   int sizeGens,
				   boolean buildClusterForRepeat) throws IOException{
		boolean[][] deg = null;
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(nameFile));

			String line;// = in.readLine();
			//String[] parceLine = line.trim().split(" ");
			// read and process gene'ś data
			int iLinha = 0;
			while ((line = in.readLine()) != null) {
				String[] states = line.trim().split(" ");
				if(iLinha==0)
					deg = new boolean[states.length][sizeGens];
				
				// read states
				for (int i = 0; i < states.length; i++) {
					deg[i][iLinha] = (Integer.parseInt(states[i]) == 1);
				}
				iLinha++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BooleanGeneExpressionData(deg,0.0, buildClusterForRepeat);
	}
	public BooleanGeneExpressionData(boolean[][] data, double porcentageValue, boolean buildClusterForRepeat) {
		this.data = data;
		this.sizeGens = data[0].length;
		this.sizeData = data.length;
		this.porcentageValue = porcentageValue;
		this.validGens=prePrecessData();
		this.clusterForRepeat=(buildClusterForRepeat)? clusterForRepeat():notCluster();
	}
	public BooleanGeneExpressionData(boolean[][] data,Map<Integer, List<Integer>> clusterForRepeat) {
		this(data,-1,false);
		if(clusterForRepeat!=null) this.clusterForRepeat=clusterForRepeat;
	}
	public BooleanGeneExpressionData(boolean[][] data) {
		this(data,-1,false);
	}
	
	public BooleanGeneExpressionData(String degFile) {
		this(degFile,false);
	}
	public Pair<int[],BooleanGeneExpressionData> sampleCluster(int[] cluster, int nClusters) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] listCluster=new ArrayList[nClusters];
		for (@SuppressWarnings("unused") ArrayList<Integer> list : listCluster) {
			list=new ArrayList<>();
		}
		for (int i = 0; i < cluster.length; i++) {
			listCluster[cluster[i]].add(i);
		}
		int[] samples=new int[nClusters];
		boolean[][] data=new boolean[this.sizeData][nClusters];
		for (int i = 0; i < nClusters; i++) {
			int a=listCluster[i].get(Resources.random.nextInt(listCluster[i].size()));
			for (int j = 0; j < nClusters; j++) {
				data[j][i]=this.data[j][a];//getData(j, a);
			}
			samples[i]=a;
		}
		return new Pair<>(samples, new BooleanGeneExpressionData(data));
	}
	@Override
	public int sizeDiscreteValues() {
		return 2;
	}
	public byte getData(int nData, int nGen) {
		return (byte) ((this.data[nData][nGen])?1:0);
	}
	public boolean getBooleanData(int nData, int nGen) {
		return this.data[nData][nGen];
	}
	public int getSizeGens() {
		return sizeGens;
	}

	public int getSizeData() {
		return sizeData;
	}

	public List<Integer> getValidGens() {
		return this.validGens;
	}
	
	public void setValidGens(ArrayList<Integer> validGens) {
		this.validGens = validGens;
	}
	public void setValidGens(String validGens) {
		this.setValidGens(validGens, ",");
	}
	public void setValidGens(String validGens, String sep) {
		TreeSet<Integer> set = Stream.of(validGens.split(sep))
				.map(e->Integer.valueOf(e.trim())).collect(toCollection(TreeSet::new));
		this.validGens= new ArrayList<>(set);
	}
	public double getPorcentageValue() {
		return porcentageValue;
	}

	public void setPorcentageValue(double porcentageValue) {
		this.porcentageValue = porcentageValue;
		this.validGens=prePrecessData();
	}

	public boolean[] getGeneExpressionByGene(int iGene) {
		boolean[] ged = new boolean[sizeData];
		for (int i = 0; i < ged.length; i++) {
			ged[i] = this.data[i][iGene];
		}
		return ged;
	}

	private Pair<Double, boolean[][]> readDeg(String nameFile) {
		if (!nameFile.endsWith(".ged"))
			nameFile += ".ged";
		boolean[][] deg = null;
		BufferedReader in;
		double persentage=0;
		try {
			in = new BufferedReader(new FileReader(nameFile));

			String line = in.readLine();
			String[] configLine = line.split(" ");
			deg = new boolean[Integer.parseInt(configLine[0])][Integer.parseInt(configLine[1])];
			if(configLine.length>2)
				persentage=Double.parseDouble(configLine[2]);
			// read and process gene'ś data
			int iLinha = 0;
			while ((line = in.readLine()) != null) {
				String[] states = line.split(" ");
				// read states
				for (int i = 0; i < states.length; i++) {
					deg[iLinha][i] = (Integer.parseInt(states[i]) == 1);
				}
				iLinha++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Pair<>(persentage, deg);
	}
	public ArrayList<Integer> constantsGens(double proportion) {
		ArrayList<Integer> constGens = new ArrayList<>();
		for (int j = 0; j < sizeGens; j++) {
			int z = 0, u = 0;
			for (int i = 0; i < sizeData; i++) {
				if (data[i][j])
					u++;
				else
					z++;
			}
			double pu= (sizeData-u)/((double)sizeData);
			double pz= (sizeData-z)/((double)sizeData);
			if((pu<proportion || pz<proportion))
				constGens.add(j);
		}
		return constGens;
	}
	private List<Integer> prePrecessData() {
		if(porcentageValue<=-1)
			return IntStream.range(0, getSizeGens()).boxed().collect(Collectors.toList());
		ArrayList<Integer> validGens = new ArrayList<>();
		for (int j = 0; j < sizeGens; j++) {
			int z = 0, u = 0;
			for (int i = 0; i < sizeData; i++) {
				if (data[i][j])
					u++;
				else
					z++;
			}
			double pu= (sizeData-u)/((double)sizeData);
			double pz= (sizeData-z)/((double)sizeData);
			if(!(pu<=porcentageValue || pz<=porcentageValue))
				validGens.add(j);
		}
		return validGens;
	}
	private Map<Integer, List<Integer>> clusterForRepeat(){
		long[] vals=new long[this.sizeGens];
		for (int i = 0; i < vals.length; i++) {
			vals[i]=valLog(i);
		}
		HashMap<Integer, List<Integer>> clusters= new HashMap<>();
		boolean[] pross=new boolean[this.sizeGens];
		for (int i = 0; i < vals.length; i++) {
			if(!pross[i]) {
				ArrayList<Integer> lc= new ArrayList<>();
				lc.add(i);
				pross[i]=true;
				for (int j = i+1; j < vals.length; j++) {
					if(vals[j]==vals[i]) {
						lc.add(j);
						pross[j]=true;
					}
				}
				clusters.put(i, Collections.unmodifiableList(lc));
			}
		}
		this.validGens= Collections.unmodifiableList(new ArrayList<>(clusters.keySet()));
		return Collections.unmodifiableMap(clusters);
	}
	private Map<Integer, List<Integer>> notCluster(){	
		Map<Integer, List<Integer>> clusters= new ConcurrentHashMap<>();
		for (int i = 0; i < this.sizeGens; i++) {
			List<Integer> lc= new ArrayList<>();
			lc.add(i);
			synchronized (this.data) {
				clusters.put(i, lc);	
			}
		}
		this.validGens=Collections.unmodifiableList(new ArrayList<>(clusters.keySet()));
		return Collections.unmodifiableMap(clusters);
	}
	long valLog(int gene) {
		long l=0L;
		boolean zeroOne=this.data[0][gene];
		for (int i = 0; i < this.data.length; i++) {
			if(!(this.data[i][gene]^zeroOne))
				l+=(1L<<i);
		}
		return l;
	}
	public void saveDeg(String fileName) throws FileNotFoundException {
		File file = new File(fileName.endsWith(".ged") ? fileName : fileName + ".ged");
		saveDeg(file);
	}

	public void saveDeg(File file) throws FileNotFoundException {
		if (!file.getName().endsWith("ged")) {
			file = new File(file.getAbsolutePath() + ".ged");
		}
		PrintWriter writer = new PrintWriter(file);
		int sizeData = this.getSizeData();
		int sizeGens = this.getSizeGens();
		writer.println(sizeData + " " + sizeGens+" "+this.porcentageValue);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sizeData; i++) {
			for (int j = 0; j < sizeGens; j++) {
				sb.append((this.data[i][j]) ? "1 " : "0 ");
			}
			sb.deleteCharAt(sb.length() - 1).append("\n");
		}
		writer.print(sb.deleteCharAt(sb.length() - 1));
		writer.close();
	}

	public void print() {
		PrintStream writer = System.out;
		int sizeData = this.getSizeData();
		int sizeGens = this.getSizeGens();
		writer.println(sizeData + " " + sizeGens);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sizeData; i++) {
			for (int j = 0; j < sizeGens; j++) {
				sb.append((this.data[i][j]) ? "1 " : "0 ");
			}
			sb.deleteCharAt(sb.length() - 1).append("\n");
		}
		writer.print(sb.deleteCharAt(sb.length() - 1));
	}

	public boolean[] getCopyData(int linedata) {
		return Arrays.copyOf(data[linedata], getSizeGens());
	}
	public boolean[] getData(int linedata) {
		if(linedata<0)
			return data[this.sizeData+linedata];
		return data[linedata];
	}
	public boolean[] getData(int linedata, List<Integer> genes) {
		boolean[] b=new boolean[genes.size()];
		boolean[] source=getData(linedata);
		for (int i = 0; i < b.length; i++) {
			b[i]=source[genes.get(i)];
		}
		return b;
	}
	public int[] getGeneEvolution(int i) {
		int[] d=new int[sizeData];
		for (int j = 0; j < d.length; j++) {
			d[j]=this.data[i][j]?1:0;			
		}
		return d;
	}

	public double[][] proportionData() {
		return Resources.divMatrix(this.countData(), this.sizeData - 1);
	}

	private int[][] countData() {
		int[][] count = new int[this.sizeGens][2];
		for (int i = 0; i < sizeData; i++) {
			for (int j = 0; j < sizeGens; j++) {
				count[j][(this.data[i][j]) ? 1 : 0]++;
			}
		}
		return count;
	}
//gera random states que nao seguem a logica de uma rede
	public static BooleanGeneExpressionData generateRandomStates(int nGens, int nStates) {
		boolean[][] samples = new boolean[nStates][];
		for (int i = 0; i < nStates; i++) {
			samples[i] = createRandonState(nGens);
			if (Resources.existsArray(samples, i, 0))
				i--;
		}
		return new BooleanGeneExpressionData(samples);
	}

	public static boolean[] createRandonState(int nGens) {
		long[] state = new long[nGens / 64 + 1];
		for (int i = 0; i < state.length; i++) {
			state[i] = Resources.random.nextLong();
		}
		boolean[] rs = new boolean[nGens];
		for (int i = 0; i < rs.length; i++) {
			long pos = 1 << i % 64;
			rs[i] = (pos & state[i / 64]) != 0;
		}
		return rs;
	}
//	public static double distHaming(boolean[][] geneExpresionSource, boolean[][] geneExpresionTest, int iGene) {
//		int equalsGens = 0;
//		int sizeGens = 0;
//		for (int i = 0; i < geneExpresionTest.length; i++) {
//			if (geneExpresionSource[i][iGene] == geneExpresionTest[i][iGene])
//				equalsGens++;
//			sizeGens++;
//
//		}
//		return ((double) equalsGens) / sizeGens;
//	}
	public static double taxaAcertos(boolean[][] geneExpresionSource, boolean[][] geneExpresionTest) {
		int equalsGens = 0;
		for (int i = 0; i < geneExpresionTest.length; i++) {
			for (int j = 0; j < geneExpresionTest[i].length; j++) {
				if (geneExpresionSource[i][j] == geneExpresionTest[i][j])
					equalsGens++;
			}
		}
		return ((double) equalsGens) / (geneExpresionTest.length * geneExpresionTest[0].length);
	}
	public static double taxaAcertos(boolean[] stateSource, boolean[]stateTest) {
		int equalsGens = 0;
		for (int i = 0; i < stateTest.length; i++) {
				if (stateSource[i] == stateTest[i])
					equalsGens++;
			
		}
		return ((double) equalsGens) /stateTest.length;
	}
//
//	public static double distHaming(boolean[][] geneExpresionSource, boolean[][] geneExpresionTest,
//			int[] degreeExpresion, int degreeTest) {
//		int equalsGens = 0;
//		int sizeGens = 0;
//		for (int i = 0; i < geneExpresionTest.length; i++) {
//			for (int j = 0; j < geneExpresionTest[i].length; j++) {
//				if (degreeExpresion[j] != degreeTest)
//					continue;
//				if (geneExpresionSource[i][j] == geneExpresionTest[i][j])
//					equalsGens++;
//				sizeGens++;
//			}
//		}
//		return ((double) equalsGens) / sizeGens;
//	}
	public double taxaAcertos(BooleanGeneExpressionData otherGeneExpression, int thisInit) {
		long size=0;
		long sizeEquals=0;
		for (int i = 0; i < this.sizeGens; i++) {
			for (int j = thisInit; j < this.sizeData; j++) {
				sizeEquals+=(this.getData(j, i)==otherGeneExpression.getData(j-thisInit, i))?1:0;
				size++;
			}
		}
		return (double)sizeEquals/size;
	}
			
	public Map<Integer, Double> distHaming(BooleanGeneExpressionData otherGeneExpression,
			int[] categorias,
			List<Integer> validGens) {
//		if(categorias.length!=validGens.size())
//			throw new RuntimeException("validgens =! categorias");
		Map<Integer, Integer> countEquals = new TreeMap<>();
		Map<Integer, Integer> countCategorias = new TreeMap<>();
		for (Iterator<Integer> iterator = validGens.iterator(); iterator.hasNext();) {
			Integer i = iterator.next();
			if (!countEquals.containsKey(categorias[i])) {
				countEquals.put(categorias[i], 0);
				countCategorias.put(categorias[i], 0);
			}
			countCategorias.replace(categorias[i], countCategorias.get(categorias[i]) + 1);
		}
		int sum = 0;
		for (int i = 0; i < this.sizeData; i++) {
			for (Iterator<Integer> iterator = validGens.iterator(); iterator.hasNext();) {
				Integer j = iterator.next();
				if (this.data[i][j] == otherGeneExpression.getBooleanData(i, j)) {
					sum++;
					countEquals.replace(categorias[j], countEquals.get(categorias[j]) + 1);
				}
			}
		}
		TreeMap<Integer, Double> distancias = new TreeMap<>();
		distancias.put(-1, (double) sum / (this.sizeData * validGens.size()));
		countEquals.forEach((k, v) -> {
			distancias.put(k, (double) v / (countCategorias.get(k) * this.sizeData));
		});
		return distancias;
	}

	public Map<Integer, Double> distHaming(boolean[][] bs, 
			int[] categorias,
			ArrayList<Integer> validGens) {
		return distHaming(new BooleanGeneExpressionData(bs), categorias,validGens);
	}
	public int[] getValidArrayGens() {
		return this.validGens.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public static void main(String arg[]) throws IOException {
		boolean[][] d=new boolean[][] {
			{true,false}
			,{true,true}
			,{false,true}
			,{true,true}
			,{true,true}
			,{true,true}
			,{false,true}
			,{false,true}
			,{false,true}
			,{false,true}
		};
		BooleanGeneExpressionData ged=new BooleanGeneExpressionData(d) ;
//				BooleanGeneExpressionData.importFrom("/home/fernandito/Downloads/dados_binarizados.txt", 5080, false);
		Pair<BooleanGeneExpressionData,BooleanGeneExpressionData> div= ged.divideData(2, 4);
		div.getKey().print();
		System.out.println();
		System.out.println();
		div.getValue().print();
//		Map<Integer, List<Integer>> clusters= ged.clusterForRepeat();
//		//System.out.println(clusters);
//		clusters.entrySet().forEach(entry->{
//		    System.out.println(entry.getKey() + "\t " + entry.getValue());  
//		 });
//		System.out.println(clusters.values().stream().mapToInt(a->a.size()).sum());
//		System.out.println(clusters.size());
	//	ged.saveDeg("malaria");
	}
	
}