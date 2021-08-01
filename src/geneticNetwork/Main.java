package geneticNetwork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;
import geneticNetwork.GeneticNetwork.Type_Function;
import inference.evaluationMetric.InvGaussianError;
import inference.evaluationMetric.MeanErrorLine;
import inference.evaluationMetric.MutualInformation;


public class Main {
	public static void test() throws IOException{
		GeneticNetwork g=GeneticNetwork.buildFixedNetwork(50, 1, 3.0, 1.0);
		System.out.println(Arrays.toString(g.sizeCanalizeGenes()));
	}
	public static void  test2(){
		System.out.println("May");
		GeneticNetwork gn=GeneticNetwork.buildRandomNetwork(10, 2, 3.0, 0.5);
		gn.show();
		boolean [] init_state=BooleanGeneExpressionData.createRandonState(10);
		BooleanGeneExpressionData ged= gn.createGeneExpressionData(10,1, GeneticNetwork.Type_Dynamic.BOOLEAN, init_state, true);
		System.out.println("RANDOM");
		ged.print();
		System.out.println("\nProbabilistic\n");
		ged= gn.createGeneExpressionData(10,1, GeneticNetwork.Type_Dynamic.PROBABILISTIC_BOOLEAN, init_state, false);
		ged.print();	
	}
	public static void  test3(){
		System.out.println("May");
		double degree=0;
		int nsim=100;
		for (int i = 0; i < nsim; i++) {
			GeneticNetwork gn=GeneticNetwork.buildRandomNetwork(20, 2, 6.0, 0.5);
			double d=gn.getMeanDegree();
			System.out.println(d);
			degree+=d;
		}
		System.out.println();
		System.out.println(degree/nsim);
			
	}
	public static void testHamingDistance() throws IOException {
		double acc = 0,abb=0;
		for(int j = 0; j < 30; j++) {
//		GeneticNetwork gSG= GeneticNetwork.readFromFile("/home/fernandito/Downloads/runrun_final/inference/RANDOM__0__6_0__BOOLEAN__20__"+j+"__SA_IM.ylu");
//		GeneticNetwork gAL= GeneticNetwork.readFromFile("/home/fernandito/Downloads/runrun_final/inference/RANDOM__0__6_0__BOOLEAN__20__"+j+"__LM_IM.ylu");
		double a = 0,b = 0,ac = 0,ab = 0;
		for (int i = 0; i < 30; i++) {
			if(i==j)continue;
//			boolean [][] deg=resources.readDeg(new File("/home/fernandito/Downloads/runrun_final/deg/RANDOM__0__6.0__BOOLEAN__20__"+i+".deg"));
//	    	System.out.print((a=gSG.hammingDistanceGED_V2(deg))+"\t");
//			System.out.println(b=gAL.hammingDistanceGED_V2(deg));
			ac+=a;
			ab+=b;
		}
		ac/=30;ab/=30;
		System.out.println(ac +"\t"+ab);
		acc+=ac;
		abb+=ab;
		}
		acc/=30;abb/=30;
		System.out.println(acc +"\t"+abb);
	}
	public static void ejemplo() throws FileNotFoundException {
		HashMap<Type_Function, Double> configFunctions=
				new HashMap<>();
		configFunctions.put(Type_Function.C,0.4);
		configFunctions.put(Type_Function.T,0.4);
		configFunctions.put(Type_Function.R,0.2);	
		GeneticNetwork gn=GeneticNetwork.buildNetwork(
				10, //nro de genes
				2, //cuantas funcoes por gene sempre é dois
				3.0,//grau medio por gene
				0.98,//probabilidade da funçaão principal
				Topology_Network.SCALE_FREE,//topologya
				configFunctions);
		
		BooleanGeneExpressionData bgn= gn.createGeneExpressionData(8, 1, Type_Dynamic.PROBABILISTIC_BOOLEAN, null, true);
		bgn.saveDeg("ejemplo");
		bgn.print();
	}
	public static void  main(String[] args) throws IOException {
		//GeneticNetwork gn=GeneticNetwork.buildRandomNetwork(4, 1, 3.0, 1.0);
//		GeneticNetwork gn =GeneticNetwork.readFromFile("sample"); 
//		//gn.writeInFile("sample");
////		boolean[] init = new boolean[4];
//		boolean F=false,T=true;
//		boolean[] init = new boolean[] {T,F,F,F};
//		BooleanGeneExpressionData deg=gn.createGeneExpressionData(16, 1,Type_Dynamic.BOOLEAN, init, true);
//		deg.print();
		ejemplo();
//		MutualInformation mu=new MutualInformation(20);
//		MeanErrorLine mel=new MeanErrorLine();
//		InvGaussianError ige=new InvGaussianError(20);
//		int[][] table= {{0,12},{1,1},{6,0},{0,0}};
//		System.out.println(mu.quantifyFromFrequencyTable(table));
//		
//		int[][] table2= {{1,12},{1,0},{6,0},{0,0}};
//		System.out.println(mu.quantifyFromFrequencyTable(table2));
//		
//		System.out.println(mel.quantifyFromFrequencyTable(table));
//		System.out.println(mel.quantifyFromFrequencyTable(table2));
//		
//		System.out.println(ige.quantifyFromFrequencyTable(table));
//		System.out.println(ige.quantifyFromFrequencyTable(table2));
	}
}
