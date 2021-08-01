package inference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import geneticNetwork.BooleanGeneExpressionData;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.InferenceNetwork.TYPE_STEAPS;
import javafx.util.Pair;

public class Prueba {
	public static ArrayList<Integer> readRepresentatives(String nameFile) {
		if (!nameFile.endsWith(".ged"))
			nameFile += ".ged";
		BufferedReader in;
		ArrayList<Integer> representatives = new ArrayList<>();
		try {
			in = new BufferedReader(new FileReader(nameFile));

			String line = in.readLine();
			int dimension = Integer.parseInt(line);
			int iLinha = 0;
			String line2 = in.readLine();
			String[] valores = line2.split(" ");
		for(int i=0; i<valores.length;i++) {
				representatives.add(Integer.parseInt(valores[i]));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return representatives;
	}
	public static void main(String[] arg) throws IOException {
		//System.out.println(Long.toBinaryString(16524));
		//BooleanGeneExpressionData ejemplo=new BooleanGeneExpressionData("matrizOriginal_1");
		BooleanGeneExpressionData ejemplo=new BooleanGeneExpressionData("matrizOriginal_1");
		ArrayList<Integer> repr = new ArrayList<>();
		//repr = readRepresentatives("matrizC100_1Representantes50");
		repr = readRepresentatives("matrizC100_1Representantes20");
		ejemplo.setValidGens(repr);
		System.out.println(repr);
		InferenceNetwork inference= new InferenceNetwork(ejemplo,
				"OOoriginAAlpruebassss_100_20Inf",
				ID_SEARCH_ALGORITM.IES,
				ID_EVALUATION_METRIC.IM,
				ID_EVALUATION_METRIC.IM,
				ID_QUALIFYING_FUNCTION.SA,
				new ConfigInference(ejemplo.sizeData),
				TYPE_STEAPS.NONE,
				true);
		NetworkInfered ni= inference.inferedNetworkByIds();
		ni.show();
		
		//validacion dinamica
		//BooleanGeneExpressionData ejemplo2=new BooleanGeneExpressionData("matrizOriginal_2");
		BooleanGeneExpressionData ejemplo2=new BooleanGeneExpressionData("matrizOriginal_2");
		Pair<BooleanGeneExpressionData, Boolean[][]> vd= ni.makeGeneExpresionState(ejemplo2);
		BooleanGeneExpressionData mg=vd.getKey();
		//mg.print();
		double tazadeaciertos=ejemplo2.taxaAcertos(mg, 1);
		System.out.println(tazadeaciertos);
		//BooleanGeneExpressionData r= new BooleanGeneExpressionData();
		//double taza2=ejemplo.taxaAcertos(ejemplo.getData(04, 1 00), mg.getData(40, 100));
		//Boolean[][] pro=vd.getValue();
		
	
		
	}
}
