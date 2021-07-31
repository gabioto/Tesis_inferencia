package geneticNetwork;
import java.io.*;
import java.util.*;
import java.util.stream.*;

import javafx.util.Pair;
import resources.Resources;

public class DiscreteGeneExpressionData implements GeneExpressionData {
	final private byte[][] data;
	public final int sizeGens;
	public final int sizeData;
	public final int discreteValues;
	private final List<Integer> validGens;
	public DiscreteGeneExpressionData(String fileData) {
		Pair<Integer, byte[][]> read=readDeg(fileData);
		this.data = read.getValue();
		this.discreteValues=read.getKey();
		this.sizeGens = data[0].length;
		this.sizeData = data.length;
		this.validGens=Collections.unmodifiableList(IntStream.range(0, this.sizeGens).boxed().collect(Collectors.toList()));
	}
	public static DiscreteGeneExpressionData importFrom(String nameFile, int sizeGens, int times) throws IOException{
		byte[][] deg = null;
		BufferedReader in;
		TreeSet<Byte> vlas=new TreeSet<>();
		try {
			in = new BufferedReader(new FileReader(nameFile));

			String line;// = in.readLine();
			//String[] parceLine = line.trim().split(" ");
			// read and process gene'ś data
			int iLinha = 0;
			while ((line = in.readLine()) != null) {
				String[] states = line.trim().split(" ");
				if(iLinha==0)
					deg = new byte[times][sizeGens];
				
				// read states
				for (int i = 0; i < states.length; i++) {
					byte b=Byte.parseByte((states[i]));
					deg[iLinha][i] = b;
					vlas.add(b);
				}
				iLinha++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DiscreteGeneExpressionData(deg, vlas.size());
	}
	public DiscreteGeneExpressionData(byte[][] data, int discreteValues) {
		this.data = data;
		this.sizeGens = data[0].length;
		this.sizeData = data.length;
		this.discreteValues=discreteValues;
		this.validGens=Collections.unmodifiableList(IntStream.range(0, this.sizeGens).boxed().collect(Collectors.toList()));
	}	
	@Override
	
	public List<Integer> getValidGens() {
		return this.validGens;
	}
	@Override
	public int sizeDiscreteValues() {
		return this.discreteValues;
	}
//	public Pair<int[],DiscreteGeneExpressionData> sampleCluster(int[] cluster, int nClusters) {
//		@SuppressWarnings("unchecked")
//		ArrayList<Integer>[] listCluster=new ArrayList[nClusters];
//		for (@SuppressWarnings("unused") ArrayList<Integer> list : listCluster) {
//			list=new ArrayList<>();
//		}
//		for (int i = 0; i < cluster.length; i++) {
//			listCluster[cluster[i]].add(i);
//		}
//		int[] samples=new int[nClusters];
//		byte[][] data=new byte[this.sizeData][nClusters];
//		for (int i = 0; i < nClusters; i++) {
//			int a=listCluster[i].get(Resources.random.nextInt(listCluster[i].size()));
//			for (int j = 0; j < nClusters; j++) {
//				data[j][i]=getData(j, a);
//			}
//			samples[i]=a;
//		}
//		return new Pair<>(samples, new DiscreteGeneExpressionData(data));
//	}

	public byte getData(int nData, int nGen) {
		return data[nData][nGen];
	}

	public int getSizeGens() {
		return sizeGens;
	}

	public int getSizeData() {
		return sizeData;
	}

	
	public byte[] getGeneExpressionByGene(int iGene) {
		byte[] ged = new byte[sizeData];
		for (int i = 0; i < ged.length; i++) {
			ged[i] = this.data[i][iGene];
		}
		return ged;
	}

	private Pair<Integer, byte[][]> readDeg(String nameFile) {
		if (!nameFile.endsWith(".ged"))
			nameFile += ".ged";
		byte[][] deg = null;
		BufferedReader in;
		int discreteValues=-1;
		try {
			in = new BufferedReader(new FileReader(nameFile));

			String line = in.readLine();
			String[] configLine = line.split(" ");
			deg = new byte[Integer.parseInt(configLine[0])][Integer.parseInt(configLine[1])];
			discreteValues=Integer.parseInt(configLine[2]);
			// read and process gene'ś data
			int iLinha = 0;
			while ((line = in.readLine()) != null) {
				String[] states = line.split(" ");
				// read states
				for (int i = 0; i < states.length; i++) {
					deg[iLinha][i] = Byte.parseByte(states[i]);
				}
				iLinha++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Pair<>(discreteValues, deg);
	}
	
//	public TreeMap<Integer, List<Integer>> clusterForRepeat(){
//		long[] vals=new long[this.sizeGens];
//		for (int i = 0; i < vals.length; i++) {
//			vals[i]=valLog(i);
//		}
//		TreeMap<Integer, ArrayList<Integer>> clusters= new TreeMap<>();
//		boolean[] pross=new boolean[this.sizeGens];
//		for (int i = 0; i < vals.length; i++) {
//			if(!pross[i]) {
//				ArrayList<Integer> lc= new ArrayList<>();
//				lc.add(i);
//				pross[i]=true;
//				for (int j = i+1; j < vals.length; j++) {
//					if(vals[j]==vals[i]) {
//						lc.add(j);
//						pross[j]=true;
//					}
//				}
//				clusters.put(i, lc);
//			}
//		}
//	//	this.validGens=new ArrayList<>(clusters.keySet());
//		return clusters;
//	}
//	long valLog(int gene) {
//		long l=0L;
////		boolean zeroOne=this.getData(1, gene);
////		for (int i = 2; i < data.length; i++) {
////			if(!(this.getData(i, gene)^zeroOne))
////				l=l+(1L<<i);
////		}
//		return l;
//	}
	public void saveDeg(String fileName) throws FileNotFoundException {
		File file = new File(fileName.endsWith(".gdd") ? fileName : fileName + ".gdd");
		saveDeg(file);
	}

	public void saveDeg(File file) throws FileNotFoundException {
		if (!file.getName().endsWith("gdd")) {
			file = new File(file.getAbsolutePath() + ".gdd");
		}
		PrintWriter writer = new PrintWriter(file);
		int sizeData = this.getSizeData();
		int sizeGens = this.getSizeGens();
		writer.println(sizeData + " " + sizeGens);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sizeData; i++) {
			for (int j = 0; j < sizeGens; j++) {
				sb.append((this.getData(i, j)));
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
				sb.append((this.getData(i, j)));
			}
			sb.deleteCharAt(sb.length() - 1).append("\n");
		}
		writer.print(sb.deleteCharAt(sb.length() - 1));
	}

	public byte[] getData(int linedata) {
		return Arrays.copyOf(data[linedata], getSizeGens());
	}
	public int[] getGeneEvolution(int i) {
		int[] d=new int[sizeData];
		for (int j = 0; j < d.length; j++) {
			d[j]=getData(j, i);			
		}
		return d;
	}

	public double[][] proportionData() {
		return Resources.divMatrix(this.countData(), this.sizeData - 1);
	}

	private int[][] countData() {
		int[][] count = new int[this.sizeGens][this.discreteValues];
		for (int i = 0; i < sizeData; i++) {
			for (int j = 0; j < sizeGens; j++) {
				count[j][(this.getData(i, j))]++;
			}
		}
		return count;
	}

	public static void main(String arg[]) throws IOException {
		DiscreteGeneExpressionData gdd= DiscreteGeneExpressionData.importFrom("/home/fernando/MALARIA_TEST/amostras.dat",5080,48);
		gdd.print();
		//TreeMap<Integer, ArrayList<Integer>> clusters= ged.clusterForRepeat();
		//System.out.println(clusters);
		
	//	ged.saveDeg("malaria");
	}
	public Pair<DiscreteGeneExpressionData, byte[]> divideData(int indexCut) {
		byte[][] sample =new byte[this.sizeData-1][];
		int index=0;
		for (int i = indexCut+1; i < this.sizeData; i++) {
			sample[index++]=this.getData(i);
		}
		for (int i = 0; i < indexCut; i++) {
			sample[index++]=this.getData(i);
		}
		return new Pair<>(new DiscreteGeneExpressionData(sample, this.discreteValues),this.getData(indexCut));
	}
	public Pair<DiscreteGeneExpressionData, DiscreteGeneExpressionData> divideData(int posCut, int countTest){
		byte[][] sample =new byte[this.sizeData-countTest][];
		byte[][] test =new byte[countTest][];
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
		return new Pair<>(new DiscreteGeneExpressionData(sample, this.discreteValues),
				new DiscreteGeneExpressionData(test,this.discreteValues));
	}	
}
