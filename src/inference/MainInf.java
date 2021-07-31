package inference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import geneticNetwork.GeneticNetwork.*;
import geneticNetwork.*;
import inference.ConfigInference.*;
import inference.evaluationMetric.*;
import inference.qualifyingFunction.*;
import inference.searchAlgoritm.*;
import javafx.util.Pair;
import resources.Resources;
import tests.concurrent.PropertiesTestC;

public class MainInf {
	static int defaultTimes = 46;

	public static void detailsCanalize() throws IOException {
		String path = "/home/fernadito/FIXED3A10/";
		GeneticNetwork gn = GeneticNetwork.readFromFile(path + "gabaritos/FIXED__5__50__3.0.ylu");
		// System.out.println(Long.toBinaryString(gn.getMainFunctionByGene(0)[0]));
		// System.out.println(AbstractNetwork.indexCanalizante(gn.getMainFunctionByGene(0),
		// 3));
		System.out.println("Preditores no gabarito");
		System.out.println("======================");
		System.out.println(Resources.preditorsToString(gn.getPredictors()));
		System.out.println("Canalizadores segundo o gabarito");
		System.out.println("================================");
		NavigableSet<Integer>[] gensCan = gn.canalizeGenes();
		System.out.println(Resources.preditorsToString(gensCan));
		System.out.println("Matriz de Canalização");
		System.out.println("=====================");
		System.out.println(Resources.matrizToString(AbstractNetwork.canalizeMatriz(gensCan)));
//		BooleanGeneExpressionData ged= new BooleanGeneExpressionData(path+"deg/FIXED__5__50__3.0__BOOLEAN__50__16");
//		TreeSet<Integer>[] gensCan=AbstractNetwork.canalizateByDEG(ged);
//		System.out.println(Resources.preditorsToString(gensCan));
//		System.out.println(Resources.matrizToString(AbstractNetwork.canalizeMatriz(gensCan)));
	}

//	public static void detailsCanalize2() throws IOException {
//		String path = "/home/fernadito/FIXED3A10/";
//		int[] amostras = { 30, 50 };
////		int[] amostras= {50};
//		for (int amostra : amostras) {
//			for (int i = 0; i < 30; i++) {
//				String name = amostra + "-" + i + ".txt";
//				PrintWriter writer = new PrintWriter(name);
//				BooleanGeneExpressionData ged = new BooleanGeneExpressionData(
//						path + "deg/FIXED__5__50__3.0__BOOLEAN__" + amostra + "__" + i);
//				writer.println("Canalizadores segundo amostra " + amostra + " - " + i);
//				writer.println("================================");
//				TreeSet<Integer>[] gensCan = AbstractNetwork.canalizateByDEG(ged);
//				writer.println(Resources.preditorsToString(gensCan));
//				writer.println("Matriz de Canalização " + amostra + " - " + i);
//				writer.println("=====================");
//				writer.println(Resources.matrizToString(AbstractNetwork.canalizeMatriz(gensCan)));
//				writer.close();
//			}
//		}
////		BooleanGeneExpressionData ged= new BooleanGeneExpressionData(path+"deg/FIXED__5__50__3.0__BOOLEAN__50__16");
////		TreeSet<Integer>[] gensCan=AbstractNetwork.canalizateByDEG(ged);
////		System.out.println(Resources.preditorsToString(gensCan));
////		System.out.println(Resources.matrizToString(AbstractNetwork.canalizeMatriz(gensCan)));
//	}

//	public static void detailsCanalize3() throws IOException {
//		String path = "/home/fernadito/FIXED3A10/";
//		ConfigInference ci = new ConfigInference();
//		int[] amostras = { 30, 50 };
//		// for(ID_QUALIFYING_FUNCTION met:ID_QUALIFYING_FUNCTION.values()) {
//		ID_QUALIFYING_FUNCTION met = ID_QUALIFYING_FUNCTION.CGL;
//		for (int amostra : amostras) {
//			for (int i = 0; i < 30; i++) {
//String name = met + "-" + amostra + "-" + i + ".txt";
//PrintWriter writer = new PrintWriter(name);
//BooleanGeneExpressionData ged = new BooleanGeneExpressionData(
//		path + "deg/FIXED__5__50__3.0__BOOLEAN__" + amostra + "__" + i);
//NetworkInfered net = NetworkInfered.readFromFile(path + "inference/FIXED__5__50__3.0__BOOLEAN__"
//		+ amostra + "__" + i + "__NDES__" + met + "__IM.ylu");
//PreInferedNetwork pre = InferenceNetwork.inferedPreNetwork(net,
//		ci.getQualifyingFunctionById(met, ID_EVALUATION_METRIC.IM), ged);
//System.out.println("Preditores no gabarito");
//System.out.println("======================");
//System.out.println(pre.detailsPredictors());
//// BooleanGeneExpressionData ged= new
//// BooleanGeneExpressionData(path+"deg/FIXED__5__50__3.0__BOOLEAN__"+amostra+"__"+i);
//writer.println("Preditores segundo metodo " + met + " amostra " + amostra + " - " + i);
//writer.println("================================");
//TreeSet<Integer>[] gensCan = net.canalizeGenes();
//writer.println(pre.detailsPredictors());
//writer.println("Matriz de Canalização " + met + " - " + amostra + " - " + i);
//writer.println("=====================");
//writer.println(Resources.matrizToString(AbstractNetwork.canalizeMatriz(gensCan)));
//writer.close();
//			}
//		}
//		// }
//	}

//	private static Resources resources=new Resources();
//	public static void test2(String[] args) {
//		GeneticNetwork gn_source = GeneticNetwork.buildFreeScaleNetwork(20, 2, 3.0, 0.98);
//		gn_source.show();
//		boolean[][] ged = null;
//		while (ged == null)
//			ged = gn_source.createGeneExpressionData(20, 1, Type_Dynamic.BOOLEAN, null, false);
//		resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformation();
//		SearchAlgoritm sa = new SequentialBackwardSelection(5);
//		InferenceGeneticNetwork iwg = new InferenceGeneticNetwork(new LinearGrouping(em), sa);
//		InferenceGeneticNetwork igw = new InferenceGeneticNetwork(new LinearSelectiveGrouping(em, 2), sa);
//		InferenceGeneticNetwork igo = new InferenceGeneticNetwork(new LinearSelectiveParcial(em, 2), sa);
//		// System.out.println("======================");
//		// System.out.println("======================");
//		// System.out.println("======================");
//		GeneticNetwork gn_wg = iwg.inferenceTopologyNetwork(ged);
//		System.out.println("gn_wg");
//		GeneticNetwork gn_gw = igw.inferenceTopologyNetwork(ged);
//		System.out.println("gn_gw");
//		GeneticNetwork gn_go = igo.inferenceTopologyNetwork(ged);
//		System.out.println("gn_go");
//		// gn_go.show();
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_wg)));
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_gw)));
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_go)));
//	}
//
	public static void timeCanalize1(int veces) throws IOException {
		String path = "";
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData(path + "test");
		Integer targetGene = 3441;
		long t;
		long a = 0;
		for (int i = 0; i < veces; i++) {
			t = System.currentTimeMillis();
			NDimencionExhaustiveSearch dn2 = new NDimencionExhaustiveSearch(3);
			dn2.searchPreditorsDetails(ged, targetGene,
					new CanalizateGroupingLinear(new MutualInformation(ged.sizeData - 1)));
			t = System.currentTimeMillis() - t;
			a += t;
		}
		System.out.println("CGL:\t" + a / veces + "ms");
	}

	public static void timeCanalize2(int veces) throws IOException {
		String path = "";
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData(path + "test");
		Integer targetGene = 3441;
		long t;
		long a = 0;
		for (int i = 0; i < veces; i++) {
			t = System.currentTimeMillis();
			CanalizeSearch cs = new CanalizeSearch(3);
			cs.searchPreditorsDetails(ged, targetGene, new CanalizateGrouping(new MutualInformation(ged.sizeData - 1)));
			t = System.currentTimeMillis() - t;
			a += t;
		}
		System.out.println("CS:\t" + a / veces + "ms");
	}

	public static void timeCanalize3(int veces) throws IOException {
		String path = "/home/fernadito/FIXED3A10/";
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData(path + "deg/FIXED__5__50__3.0__BOOLEAN__50__0");
		Integer targetGene = 47;
		long t;
		long a = 0;
		for (int i = 0; i < veces; i++) {
			t = System.currentTimeMillis();
			NDimencionExhaustiveSearch dn = new NDimencionExhaustiveSearch(3);
			dn.searchPreditorsDetails(ged, targetGene, new CanalizateGrouping(new MutualInformation(ged.sizeData - 1)));
			t = System.currentTimeMillis() - t;
			a += t;
		}
		System.out.println("CG:\t" + a / veces + "ms");
	}

//	public static void test2() {
//		GeneticNetwork gn_source = GeneticNetwork.buildFreeScaleNetwork(10, 1, 3.0, 1.0);
//		gn_source.show();
//		boolean[][] ged = null;
//		while (ged == null)
//			ged = gn_source.createGeneExpressionData(10, 1, Type_Dynamic.BOOLEAN, null, false);
//		resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformation();
//		WithoutGrouping wg = new WithoutGrouping(em);
//		TreeSet<Integer> preditors = new TreeSet<Integer>();
//		preditors.add(8);
//		preditors.add(7);
//		int[][] ft = wg.buildFrequencyTable(preditors, ged, 9);
//		System.out.println();
//		for (int[] is : ft) {
//			for (int i : is) {
//System.out.print(i + " ");
//			}
//			System.out.println();
//		}
//		System.out.println(em.quantifyFromFrequencyTable(ft));
//	}
//
//	public static void testGL() {
//		GeneticNetwork gn_source = GeneticNetwork.buildFreeScaleNetwork(20, 2, 3.0, 0.98);
//		gn_source.show();
//		boolean[][] ged = null;
//		while (ged == null)
//			ged = gn_source.createGeneExpressionData(20, 1, Type_Dynamic.BOOLEAN, null, false);
//		resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformation();
//		SearchAlgoritm sa = new SequentialBackwardSelection(5);
//		InferenceGeneticNetwork igl = new InferenceGeneticNetwork(new GroupingLatticeSFS(em, 2), sa);
//
//		GeneticNetwork gn_gl = igl.inferenceTopologyNetwork(ged);
//		gn_gl.show();
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_gl)));
//	}
//
//	public static void testSBS() throws IOException {
//		GeneticNetwork gn_source = GeneticNetwork.readFromFile("red.ylu");
//		gn_source.show();
//		boolean[][] ged = gn_source.createGeneExpressionData(20, 1, Type_Dynamic.BOOLEAN,
//new boolean[] { false, false, true, true, false, false, false, false, false, true }, true);
//		resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformationNotObservedPenality(1);
//		// QualifyingFunction qf=new LinearGrouping(em);
//		// TreeSet<Integer> preditors=new TreeSet<Integer>();
//		// preditors.add(0);
//		// preditors.add(1);
//		// preditors.add(2);
//		// preditors.add(4);
//		// preditors.add(5);
//		// Details d= qf.quantifyDetails(preditors, ged, 1);
//		// System.out.println(d.qualityValue);
//		//
//		SearchAlgoritm sa = new SequentialBackwardSelection(5);
//		InferenceGeneticNetwork igl = new InferenceGeneticNetwork(new LinearGrouping(em), sa);
//		GeneticNetwork gn_gl = igl.inferenceTopologyNetwork(ged);
//		gn_gl.show();
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_gl)));
//	}
//
//	public static void test2SBS() throws IOException {
//		GeneticNetwork gn_source = GeneticNetwork.buildRandomNetwork(20, 1, 3.0, 1.0);
//		// gn_source.show();
//		boolean[][] ged = gn_source.createGeneExpressionData(20, 1, Type_Dynamic.BOOLEAN, null, true);
//		// resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformation();
//		SearchAlgoritm sa = new SequentialBackwardSelection(5);
//		InferenceGeneticNetwork igl = new InferenceGeneticNetwork(new LinearGrouping(em), sa);
//
//		GeneticNetwork gn_gl = igl.inferenceTopologyNetwork(ged);
//		gn_gl.show();
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_gl)));
//	}
//
//	public static void testIncrementalSFS() throws IOException {
//		GeneticNetwork gn_source = GeneticNetwork.buildRandomNetwork(30, 1, 6.0, 1.0);
//		gn_source.show();
//		boolean[][] ged = gn_source.createGeneExpressionData(20, 1, Type_Dynamic.BOOLEAN, null, true);
//		// resources.printDEG(ged);
//		EvaluationMetric em = new MutualInformation();
//		SearchAlgoritm sa = new IncrementalExhaustiveSearchWithSFS(4, 10);
//		InferenceGeneticNetwork igl = new InferenceGeneticNetwork(new LinearGrouping(em), sa);
//
//		GeneticNetwork gn_gl = igl.inferenceTopologyNetwork(ged);
//		gn_gl.show();
//		System.out.println(Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, gn_gl)));
//	}
//
//	public static void testNotObservationPenalization() throws IOException {
//		double[] penalizations = { 0.0001, 0.001, 0.01, 0.1, 1, 2 };
//		int[][] tabela1 = { { 1, 0 }, { 1, 2 }, { 2, 0 }, { 1, 0 } };
//
//		for (double p : penalizations) {
//			MutualInformationNotObservedPenality no = new MutualInformationNotObservedPenality(p);
//			System.out.println(p + "\t" + no.quantifyFromFrequencyTable(tabela1) + "\t");
//		}
//		MutualInformation mi = new MutualInformation();
//		System.out.println("sp\t" + mi.quantifyFromFrequencyTable(tabela1) + "\t");
//	}
//
//	public static void testErro() throws IOException {
//		InferenceGeneticNetwork ig = new InferenceGeneticNetwork(
//new WithoutGrouping(new MutualInformationPoorlyObserved(2)), new SequentialBackwardSelection(5));
//		boolean[][] deg = resources.readDeg(new File("deg", "RANDOM__1__4.0__PROBABILISTIC_BOOLEAN__20__0.deg"));
//		GeneticNetwork gn = ig.inferenceTopologyNetwork(deg);
//		gn.show();
//	}
//
	public static void testInf(int desde, int hasta) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData(
				"/home/fernandito/MALARIA_TEST/deg/RANDOM__0__50__3.0__PROBABILISTIC_BOOLEAN__30__0.ged");
		ged.setPorcentageValue(0.1);
		InferenceNetwork inference = new InferenceNetwork(ged, "infNet", ID_SEARCH_ALGORITM.IES_SFS,
				ID_EVALUATION_METRIC.IM, null, ID_QUALIFYING_FUNCTION.SA);
		for (int i = desde; i < hasta; i++) {
			inference.inferedGene(i);
			// System.out.println(i);
		}
	}

	public static void testInf(int[] arrayTargets) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria.ged");
		ged.setPorcentageValue(-0.1);
		InferenceNetwork inference = new InferenceNetwork(ged, "infMal", ID_SEARCH_ALGORITM.NDES,
				ID_EVALUATION_METRIC.IM, null, ID_QUALIFYING_FUNCTION.SA);
		for (int i : arrayTargets) {
			inference.inferedGene(i);
		}
		// System.out.println(i);

	}

	public static void inferenceCustom(String gens, String sementes, String sep, String nameNet) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria.ged");
		ged.setValidGens(gens + "," + sementes, sep);
		// int[]
		// customGens=ged.getValidGens().stream().mapToInt(Integer::intValue).toArray();
		int[] customGens = Stream.of(sementes.split(sep)).mapToInt(Integer::parseInt).toArray();
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			String f = id_Function.toString();
			CustomGenesInference inference = new CustomGenesInference(customGens, ged, f + "_" + nameNet,
					ID_SEARCH_ALGORITM.IES_SFS, ID_EVALUATION_METRIC.IM, null, id_Function, true);
			PreInferedNetwork p = inference.inferedPreNetwork(f);
			p.show();
			p.writeFormatGraph(f + "_" + nameNet);
		}
	}

	public static void formatInferenceCustom(String nameNet) throws IOException {
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			String f = id_Function.toString();
			PreInferedNetwork p = PreInferedNetwork.readFromFile(f + "_" + nameNet);
			p.show();
			p.writeFormatGraph(f + "_" + nameNet);
		}
	}

	public static void formatInferenceCustomDirectory(String pathDirectory) throws IOException {
		File[] files = new File(pathDirectory).listFiles((dir, name) -> name.endsWith(".ylu"));
		for (File file : files) {
			PreInferedNetwork p = PreInferedNetwork.readFromFile(file);
			// p.show();
			p.writeFormatGraph(file.getAbsolutePath() + ".txt");
		}
	}

	public static void testInf(int[] arrayTargets, int n) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria.ged");
		ged.setPorcentageValue(-0.1);
		ConfigInference configInference = new ConfigInference(ged.sizeData - 1);
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {

			for (int i : arrayTargets) {
				PrintWriter writer = new PrintWriter("d_" + id_Function.toString() + i + ".txt");
				System.out.println("d_" + id_Function.toString() + i + ".txt");
				Details[] details = configInference.getSearchAlgoritmbyId(ID_SEARCH_ALGORITM.NDES)
						.searchPreditorsDetails(ged, i, n,
								configInference.getQualifyingFunctionById(id_Function, ID_EVALUATION_METRIC.IM));
				for (Details d : details) {
					String p = d.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(" "));
					System.out.println(p + " " + d.qualityValue);
					writer.println(i + " " + p + " " + d.qualityValue);
				}
				System.out.println();
				writer.close();
			}
			// System.out.println(i);
		}
	}

	public static void testInf2(int[] arrayTargets) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria.ged");
		ged.setPorcentageValue(-0.1);
		ConfigInference configInference = new ConfigInference(ged.sizeData - 1);
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			for (int i : arrayTargets) {
				PrintWriter writer = new PrintWriter(id_Function.toString() + i + ".txt");
				System.out.println(id_Function.toString() + i + ".txt");
				List<Details> details = configInference.getSearchAlgoritmbyId(ID_SEARCH_ALGORITM.IES_SFS)
						.searchPreditorsDetails(ged, i,
								configInference.getQualifyingFunctionById(id_Function, ID_EVALUATION_METRIC.IM));
				for (Details d : details) {

					String p = d.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(" "));
					writer.println(i + " " + p + " " + d.qualityValue);
				}
				System.out.println("Red Hx: " + details.size());
				System.out.println();
				writer.close();
			}
			// System.out.println(i);
		}
	}

	public static void testInf2() throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria.ged");
		ged.setPorcentageValue(-0.1);
		ConfigInference configInference = new ConfigInference(ged.sizeData - 1);
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			String line;
			BufferedReader in = new BufferedReader(new FileReader("sementes" + id_Function));
			while ((line = in.readLine()) != null) {
				File f = new File(id_Function.toString() + line + ".txt");
				if (!f.exists()) {
					PrintWriter writer = new PrintWriter(f);
					System.out.println(id_Function.toString() + line + ".txt");
					List<Details> details = configInference.getSearchAlgoritmbyId(ID_SEARCH_ALGORITM.IES_SFS)
							.searchPreditorsDetails(ged, Integer.parseInt(line),
									configInference.getQualifyingFunctionById(id_Function, ID_EVALUATION_METRIC.IM));
					for (Details d : details) {
						String p = d.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(" "));
						writer.println(line + " " + p + " " + d.qualityValue);
					}
					System.out.println("Red Hx: " + details.size());
//System.out.println(
					writer.close();
				}
			}
			in.close();
			// System.out.println(i);
		}
	}

//	public static void testInf2() throws FileNotFoundException {
//		BooleanGeneExpressionData ged =new BooleanGeneExpressionData("test");
//		InferenceNetwork inference = new InferenceNetwork(ged, "infNet", ID_SEARCH_ALGORITM.SFS,
//ID_EVALUATION_METRIC.IM, ID_QUALIFYING_FUNCTION.SA);
//		for (int i = desde; i < hasta; i++) {
//			inference.inferedGene(i);
//		}
//	}
//
//	public static void testbf() {
//		int[][] g = { { 0 }, { 1, 2, 4 }, { 3, 5, 6 }, { 7 } };
//		int[][] f = { { 0, 2 }, { 3, 7 }, { 8, 3 }, { 3, 1 } };
//		InferenceGeneticNetwork i = new InferenceGeneticNetwork(null, null);
//		System.out.println(Long.toBinaryString(i.buildBooleanFunction(f, g, 8)[0]));
//	}
//
//	public static void testreinf() throws IOException {
//		GeneticNetwork on = GeneticNetwork.readFromFile("/home/fernando/runrun/gabaritos/RANDOM__0__50__2.0.ylu");
//		on.show();
//		GeneticNetwork in = GeneticNetwork
//.readFromFile("/home/fernando/runrun/inference/RANDOM__0__2_0__BOOLEAN__20__0__LM_IM.ylu");
//		in.show();
//		boolean[][] deg = resources.readDeg(new File("/home/fernando/runrun/deg/RANDOM__0__2.0__BOOLEAN__20__1.deg"));
//		InferenceGeneticNetwork inf = new InferenceGeneticNetwork(
//new GroupingLatticeSFS(new MutualInformation(), 2), null);
//		GeneticNetwork rin = inf.reInferenceNetwork(deg, in);
//		rin.show();
//	}
//
//	public static void testinf2() throws IOException {
//		GeneticNetwork on = GeneticNetwork.readFromFile("/home/fernandito/SFCF/gabaritos/SCALE_FREE__12__50__2.0.ylu");
//		on.show();
//		// GeneticNetwork in = GeneticNetwork
//		// .readFromFile("/home/fernando/runrun/inference/RANDOM__0__2_0__BOOLEAN__20__0__LM_IM.ylu");
//		// in.show();
//		boolean[][] deg = resources
//.readDeg(new File("/home/fernandito/SFCF/deg/SCALE_FREE__12__50__2.0__BOOLEAN__20__0.deg"));
//		InferenceGeneticNetwork inf = new InferenceGeneticNetwork(
//new CanalizateGrouping(new MutualInformation()), new IncrementalExhaustiveSearchWithSFS(4, 10));
//		GeneticNetwork rin = inf.inferenceTopologyNetwork(deg);
//		rin.show();
//	}
//
//	public static void testreinf2() throws IOException {
//		GeneticNetwork on = GeneticNetwork.readFromFile("/home/fernandito/nets/gabaritos/RANDOM__0__50__2.0.ylu");
//		on.show();
//		System.out.println("============================");
//		GeneticNetwork in = GeneticNetwork.readFromFile(
//"/home/fernandito/nets/inferenceRE/RANDOM__0__50__2_0__BOOLEAN__20__0__IES_SFS__LS_IM.ylu");
//		in.show();
//		System.out.println("==============================");
//		boolean[][] deg = resources
//.readDeg(new File("/home/fernandito/nets/deg/RANDOM__0__50__2.0__BOOLEAN__20__0.deg"));
//		InferenceGeneticNetwork inf = new InferenceGeneticNetwork(new LinearSelective(new MutualInformation(), 1),
//new IncrementalExhaustiveSearchWithSFS(5, 10));
//		NetworkInfered rin = inf.reInferenceNetwork2(deg, in254);
//		rin.show();
//	}
//
//	public static void testComp() throws IOException {
//		GeneticNetwork gn_source = null;
//		BooleanGeneExpressionData ged = null;
//		int minValue = Integer.MAX_VALUE;
//		int minIdx = Integer.MAX_VALUE;
//		TreeSet<Integer>[] cans = null;
//		while (ged == null) {
//			gn_source = GeneticNetwork.buildNetwork(50, 1, 3.0, 1.0, Topology_Network.FIXED);
//			ged = gn_source.createGeneExpressionData(30, 1, Type_Dynamic.BOOLEAN, null, false);
//			cans = AbstractNetwork.canalizingByDEG(ged);
//			for (int k = 0; k < cans.length; k++) {
//				int il = cans[k].size();
//				if (il == 0)
//					continue;
//				if (il < minValue) {
//					minIdx = k;
//					minValue = il;
//				}
//			}
//		}
//		gn_source.writeInFile(new File("gn_source"));
//		ged.saveDeg(new File("test"));
//		System.out.println(minIdx + " " + minValue + " " + cans[minIdx]);
////		ged=new BooleanGeneExpressionData("test");
////		InferenceNetwork igSa = new InferenceNetwork(ged, "saTest", ID_SEARCH_ALGORITM.CS, ID_EVALUATION_METRIC.IM,
////ID_QUALIFYING_FUNCTION.CG);
////		igSa.inferedGene(21);
////		NetworkInfered2 sa = igSa.inferedNetwork();
////		sa.show();
////		sa.writeInFile(new File("sa.ylu"));
////		GeneticNetwork gnGl = igGl.inferenceTopologyNetwork(ged);
////		gnGl.writeInFile(new File("gnGl.ylu"));
////		NetworkInfered gl = igGl.reInferenceNetwork2(ged, gnGl);
////		gl.writeInFile(new File("CG.ylu"));
//	}

	public static class dataGN {
		public GeneticNetwork gn;
		BooleanGeneExpressionData ged;
		public boolean[] initState;
		public List<Integer> constanGens;
		public List<Integer> top;

		public dataGN(GeneticNetwork gn, BooleanGeneExpressionData ged, boolean[] initState, List<Integer> constanGens,
				List<Integer> top) {
			this.gn = gn;
			this.ged = ged;
			this.initState = initState;
			this.constanGens = constanGens;
			this.top = top;
		}

	}

//	public static class Pesable<T> implements Comparable<Pesable<T>>{
//		T objeto;
//		Integer peso;
//		public Pesable(T objeto, Integer peso) {
//			this.objeto = objeto;
//			this.peso = peso;
//		}
//		@Override
//		public int compareTo(Pesable<T> o) {
//			// TODO Auto-generated method stub
//			return peso.compareTo(o.peso);
//		}
//		@Override
//		public String toString() {
//			// TODO Auto-generated method stub
//			return String.valueOf(peso);
//		}
//		
//	}
	public static dataGN testGN(GeneticNetwork gn_source, boolean[] baseInit, int timesExpresions) {
		BooleanGeneExpressionData ged = null;
		int t = 0;
		boolean[] stateMin = null;
		int minLen = 50;
		List<Integer> constanGens = null;
		int[] counts = new int[gn_source.getSizeGenes()];
		BooleanGeneExpressionData gedFinal = null;
		while (ged == null) {
			boolean[] initState = (baseInit == null)
					? BooleanGeneExpressionData.createRandonState(gn_source.getSizeGenes())
					: baseInit;
			ged = gn_source.createGeneExpressionData(timesExpresions, 1, Type_Dynamic.PROBABILISTIC_BOOLEAN, initState,
					false);
			if (ged != null) {
				t++;
				List<Integer> c = ged.constantsGens(0.1);
//			System.out.println(t + "\t" + c.size() + "\t" + c);
				if (t == 100) {
////System.out.println("fin di");
					break;
				}
				if (minLen > c.size()) {
					minLen = c.size();
					stateMin = initState;
					constanGens = c;
					gedFinal = ged;
				}
				if (!c.isEmpty()) {
					ged = null;
					addArray(counts, c);
				}
				if (minLen == 0) {
					break;
				}
			}
		}
//	ged = gn_source.createGeneExpressionData(50, 1, Type_Dynamic.BOOLEAN, stateMin, false);
//	System.out.println(constanGens.size() + "\t" + constanGens);
		List<Integer> top = topArray(counts);
		return new dataGN(gn_source, gedFinal, stateMin, constanGens, top);
//	System.out.println(top.size() + "\t" + top);
//	for (Integer i : constanGens) {
//		System.out.println(Arrays.toString(ged.getGeneEvolution(i)));
//	}
//	System.out.println(Arrays.toString(stateMin));
//	gn_source.writeInFile(new File("gn_source"));
//	ged.saveDeg(new File("test"));
	}

	public static dataGN testGN() {
		HashMap<Type_Function, Double> configFunction = new HashMap<>();
		configFunction.put(Type_Function.R, 1.0);
		// configFunction.put(Type_Function.,0.2);
		GeneticNetwork gn_source = GeneticNetwork.buildNetwork(580, 2, 3.0, 0.98, Topology_Network.RANDOM,
				configFunction);
		return testGN(gn_source, null, 46);
	}

	public static void analiseNetwork() throws FileNotFoundException {
		int test = 0;
		int nConst = 50;
		dataGN data = null;
		int count0s = 0;
		while (test++ < 100) {
//		while (count0s < 200) {
			dataGN dataTest = testGN();
			if (dataTest.constanGens.size() < nConst) {
				nConst = dataTest.constanGens.size();
				data = dataTest;
			}
			if (dataTest.constanGens.size() == 0) {
				data = dataTest;
				count0s++;
			}
		}
		System.out.println(data.constanGens.size() + "\t" + data.constanGens);
		System.out.println(data.top.size() + "\t" + data.top);
		System.out.println(count0s);
//		for (Integer i : constanGens) {
//			System.out.println(Arrays.toString(ged.getGeneEvolution(i)));
//		}
		System.out.println(Arrays.toString(data.initState));
		data.gn.writeInFile(new File("gn_source"));
		data.ged.saveDeg(new File("test"));
	}

	public static GeneticNetwork buildNetwork() {
		dataGN data = null;
		while (true) {
			dataGN dataTest = testGN();
			if (dataTest.constanGens.size() == 0) {
				data = dataTest;
				break;
			}
		}
		return data.gn;
	}

	public static BooleanGeneExpressionData[] buidGED(GeneticNetwork gn, int length, int timesExpresions,
			int tentativas) {
		BooleanGeneExpressionData[] geds = new BooleanGeneExpressionData[length];
		int t = 0;
		int count = 0;
		while (count < length && t < tentativas) {
			dataGN data = testGN(gn, null, timesExpresions);
			if (data.constanGens.size() == 0) {
				geds[count] = data.ged;
				count++;
			}
			t++;
		}
		if (t >= tentativas)
			return null;
		return geds;
	}

	public static void addArray(int[] counts, List<Integer> list) {
		for (Integer integer : list) {
			counts[integer]++;
		}
	}

	public static List<Integer> topArray(int[] conts) {
		List<Integer> top = new ArrayList<>();
		int max = -1;
		for (int i = 0; i < conts.length; i++) {
			if (conts[i] > max) {
				max = conts[i];
				top.clear();
			}
			if (conts[i] == max)
				top.add(i);
		}
		return top;
	}

	public static void testCo2() throws IOException {
		GeneticNetwork gn_source = GeneticNetwork.readFromFile("gn_source");
//		gn_source.buildCanalizingFunctionsForGene(27);
//		gn_source.buildCanalizingFunctionsForGene(20);
//		gn_source.buildCanalizingFunctionsForGene(31);
//		gn_source.buildCanalizingFunctionsForGene(36);
//		gn_source.buildCanalizingFunctionsForGene(37);
		boolean[] initState = { false, false, false, true, false, true, true, false, true, true, true, true, true,
				false, true, true, true, true, false, false, false, true, true, true, true, true, true, false, true,
				true, false, true, false, false, false, true, false, true, true, false, true, true, true, true, true,
				false, true, true, true, true };
//		boolean[] initState=null;
		BooleanGeneExpressionData ged = gn_source.createGeneExpressionData(30, 1, Type_Dynamic.PROBABILISTIC_BOOLEAN,
				initState, false);
//		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("test");
		ArrayList<Integer> constanGens = ged.constantsGens(0.1);
		System.out.println(constanGens.size() + "\t" + constanGens);
		for (Integer i : constanGens) {
			System.out.println(Arrays.toString(ged.getGeneEvolution(i)));
		}
//		System.out.println(gn_source.getPredictorsByGene(27));

		// gn_source.writeInFile(new File("gn_source"));
	}

	public static void testRebuildDEG() throws IOException {
		GeneticNetwork gn_source = GeneticNetwork.readFromFile("gn_source");
//		boolean[] initState = { false, false, false, true, false, true, true, false, true, true, true, true, true,
//false, true, true, true, true, false, false, false, true, true, true, true, true, true, false, true,
//true, false, true, false, false, false, true, false, true, true, false, true, true, true, true, true,
//false, true, true, true, true };
		dataGN data = null;
		int test = 0;
		int nConst = 50;
		int count0s = 0;
		while (test++ < 100) {
//			dataGN dataTest = testGN(gn_source,initState);
			dataGN dataTest = testGN(gn_source, null, 30);
			if (dataTest.constanGens.size() < nConst) {
				nConst = dataTest.constanGens.size();
				data = dataTest;
			}
			if (dataTest.constanGens.size() == 0)
				count0s++;
		}
		System.out.println(data.constanGens.size() + "\t" + data.constanGens);
		System.out.println(data.top.size() + "\t" + data.top);
		System.out.println(count0s);
//		for (Integer i : constanGens) {
//			System.out.println(Arrays.toString(ged.getGeneEvolution(i)));
//		}
//		System.out.println(Arrays.toString(data.initState));
//		data.gn.writeInFile(new File("gn_source"));
//		data.ged.saveDeg(new File("test"));
	}

	public static void testCo2Prob() throws IOException {
		String path = "/home/fernandito/IRANDOM/";
		String sgab = path + "gabaritos/RANDOM__0__50__3.0";
		String sdeg = path + "deg/RANDOM__0__50__3.0__BOOLEAN__30__0";
		String sinf = path + "inference/RANDOM__0__50__3.0__BOOLEAN__30__0__IES__GLSFS__IM.ylu";
		(new File("inf_.ylu")).delete();
		GeneticNetwork gn_source = GeneticNetwork.readFromFile(sgab);
		gn_source.show();
//		BooleanGeneExpressionData ged = gn_source.createGeneExpressionData(30, 1, Type_Dynamic.BOOLEAN, null, false);
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData(sdeg);
		ged.setPorcentageValue(-0.1);
//		InferenceNetwork igSa = new InferenceNetwork(ged, "inf_", ID_SEARCH_ALGORITM.SFS, ID_EVALUATION_METRIC.IM,
//ID_QUALIFYING_FUNCTION.GLSFS);
//		
//		NetworkInfered2 ni = igSa.inferedNetwork();
		NetworkInfered ni = NetworkInfered.readFromFile(sinf);
		System.out.println();
		ni.show();
		System.out.println(
				Arrays.toString(GeneticNetwork.validateFPFNTPTN(gn_source, ni, 0, "", true, ged.getValidGens())));
		BooleanGeneExpressionData gtest = BooleanGeneExpressionData.generateRandomStates(gn_source.getSizeGenes(),
				1000);
		gtest.saveDeg("gtestDin");
//		BooleanGeneExpressionData gtest = new BooleanGeneExpressionData("gtestDin");
		BooleanGeneExpressionData gGabarito = gn_source.makeGeneExpresionData(Type_Dynamic.PROBABILISTIC_BOOLEAN,
				gtest);
		int[] degrees = gn_source.getDegrees(ged.getValidGens());
		Pair<BooleanGeneExpressionData, double[][]> degInference = ni.makeProportionGeneExpresionState(gtest, degrees,
				new int[] { 1, 2, 3 });
		System.out.println(Arrays.toString(degrees));
		System.out.println(ged.getValidGens());
//		
//		gGabarito.print();
//		System.out.println();
//		degInference.getKey().print();
//		System.out.println();
		System.out.println(gGabarito.distHaming(degInference.getKey(), degrees, ged.getValidGens()));
//		
	}

	public static void test() throws IOException {
		GeneticNetwork g = GeneticNetwork.buildNetwork(50, 1, 3.0, 1.0, Topology_Network.FIXED);
		g.writeInFile(new File("test.ylu"));
		for (int i = 0; i < g.getPredictors().length; i++) {
			System.out.println(g.stringDinamicGene(i));
		}
	}

//	public static void testCo3() throws IOException {
//		GeneticNetwork g = GeneticNetwork.readFromFile("gn_source");
//		BooleanGeneExpressionData gedTest = BooleanGeneExpressionData.generateRandomStates(50, 1000);
//		int[] sizeA = { 30, 50 };
////		int[] sizeA= {30};
////		ID_QUALIFYING_FUNCTION[] qs= {ID_QUALIFYING_FUNCTION.CG};
//		ID_QUALIFYING_FUNCTION[] qs = { ID_QUALIFYING_FUNCTION.SA, ID_QUALIFYING_FUNCTION.CG };
//		for (int is = 0; is < sizeA.length; is++) {
//			int s = sizeA[is];
////			BooleanGeneExpressionData geneExpressionData=g.createGeneExpressionData(s, 1, Type_Dynamic.BOOLEAN,null,false);
////			geneExpressionData.saveDeg("deg_"+is);
//			BooleanGeneExpressionData geneExpressionData = new BooleanGeneExpressionData("deg_" + is);
//			for (int j = 0; j < qs.length; j++) {
//ID_QUALIFYING_FUNCTION q = qs[j];
//String suf = q + "_" + s + ".txt";
//InferenceNetwork igSa = new InferenceNetwork(geneExpressionData, "saTestI", ID_SEARCH_ALGORITM.NDES,
//		ID_EVALUATION_METRIC.IM, q);
//PreInferedNetwork pre = igSa.inferedPreNetwork();
//ArrayList<Details>[] details = pre.getGensInfered();
//for (int i = 0; i < details.length; i++) {
//	PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(i + "_" + suf), false));
//	StringBuilder sb = new StringBuilder();
//	TreeSet<Integer> preditors = g.getPredictors()[i];
//	long[] originalfunction = null;
//	if (!preditors.isEmpty())
//		originalfunction = g.getBoolean_functions()[i][0];
//	sb.append(i).append(":\t").append(preditors).append("\n");
//	if (originalfunction != null)
//		sb.append(Resources.toTrueTableFormat(originalfunction[0], 1 << preditors.size()));
//	else
//		sb.append("RANDOM");
////	WithoutGrouping wg = new WithoutGrouping(null);
//	Details d = new Details();
//	d.preditors = preditors;
//	d.frequencyTable = AbstractNetwork.buildFrequencyTable(preditors, geneExpressionData, i);
//	sb.append("\n").append(d.frequencyTableString());
//	Pair<int[][], boolean[]> ddg = g.dinamicGeneDetails(i, gedTest);
//	d.frequencyTable = ddg.getKey();
//	sb.append("dinamica 1000 genes\n").append(d.frequencyTableString());
//	sb.append(pre.detailsPredictors(i, gedTest, ddg.getValue())).append("\n");
//	System.out.println(sb);
//	printWriter.print(sb); // System.out.println(pre.frequencyTableGeneString(i, gedTest));
//	printWriter.close();
//}
//			}
//		}
//
//		// sa.show();
//	}

//
//	public static void testTable() throws IOException {
//		GeneticNetwork gn_source = null;
//		boolean[][][] ged = new boolean[60][][];
//		int sizeGenes = 50;
//		int sizeAmostras = 30;
//		int[] amostras = { 20, 50 };
//		while (gn_source == null) {
//			boolean[][] initstates = new boolean[sizeAmostras * amostras.length][sizeGenes];
//			gn_source = GeneticNetwork.buildFreeScaleNetwork(sizeGenes, 1, 2.0, 1.0);
//			lamostras: for (int i_ged = 0; i_ged < sizeAmostras; i_ged++) {
//for (int i_amostra = 0; i_amostra < amostras.length; i_amostra++) {
//	int index = i_amostra * sizeAmostras + i_ged;
//	initstates[index] = resources.createRandonState(sizeGenes);
//	boolean[][] ged_i = gn_source.createGeneExpressionData(amostras[i_amostra], 1, Type_Dynamic.BOOLEAN,
//			initstates[index], false);
//	int v = 0;
//	while (resources.existsArray(initstates, index, 0) || ged_i == null) {
//		initstates[index] = resources.createRandonState(sizeGenes);
//		ged_i = gn_source.createGeneExpressionData(amostras[i_amostra], 1, Type_Dynamic.BOOLEAN,
//initstates[index], false);
//		if (v++ > 100) {
//			gn_source = null;
//			break lamostras;
//		}
//	}
//	ged[index] = ged_i;
//}
//			}
//		}
//		gn_source.writeInFile(new File("gn_source.ylu"));
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfSa = new WithoutGrouping(evaluationMetric);
//		// sizeGenes = 2;
//		for (int i = 0; i < sizeGenes; i++) {
//			for (int i_amostra = 0; i_amostra < amostras.length; i_amostra++) {
//PrintWriter pw = new PrintWriter(i + "_" + amostras[i_amostra] + ".txt");
//StringBuilder sb = new StringBuilder("Gene ").append(i).append(gn_source.getPredictors()[i])
//		.append("\n");
//int sizeTab = 1 << gn_source.getPredictors()[i].size();
//sb.append(resources.toTrueTableFormat(gn_source.getBoolean_functions()[i][0], sizeTab)).append("\n");
//for (int i_ged = 0; i_ged < sizeAmostras; i_ged++) {
//	int index = i_amostra * sizeAmostras + i_ged;
//	Details dsa = qfSa.quantifyDetails(gn_source.getPredictors()[i], ged[index], i);
//	sb.append(dsa.frequencyTableString()).append("\n");
//	// System.out.println();
//}
//pw.print(sb);
//pw.close();
//			}
//		}
//	}
//
//	public static void auxComp(QualifyingFunction qf) throws IOException {
//		SearchAlgoritm searchAlgoritm = new IncrementalExhaustiveSearchWithSFS(5, 10);
//		InferenceGeneticNetwork igLsg = new InferenceGeneticNetwork(qf, searchAlgoritm);
//		boolean[][] ged = resources.readDeg(new File("test.deg"));
//		GeneticNetwork gn = igLsg.inferenceTopologyNetwork(ged);
//		// gn.writeInFile(new File("gnGl.ylu"));
//		// GeneticNetwork gn=GeneticNetwork.readFromFile("gnGl.ylu");
//		NetworkInfered gl = igLsg.reInferenceNetwork2(ged, gn);
//		gl.writeInFile(new File(qf.getShortName() + ".ylu"));
//	}
//
//	public static void testComp2(QualifyingFunction qfGl) throws IOException {
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfSa = new WithoutGrouping(evaluationMetric);
//		// QualifyingFunction qfGl=new GroupingLatticeSFS(evaluationMetric,2, true);
//		boolean[][] ged = resources.readDeg(new File("test.deg"));
//		GeneticNetwork gn_source = GeneticNetwork.readFromFile("gn_source.ylu");
//		int sizeGenes = gn_source.getPredictors().length;
//		NetworkInfered sa = NetworkInfered.readFromFile("sa.ylu");
//		NetworkInfered gl = NetworkInfered.readFromFile(qfGl.getShortName() + ".ylu");// "gl.ylu");
//		int sizeSamples = 1000;
//		boolean[][] samples = new boolean[sizeSamples][];
//		for (int i = 0; i < sizeSamples; i++) {
//			samples[i] = resources.createRandonState(sizeGenes);
//			if (resources.existsArray(samples, i, 0))
//i--;
//		}
//		boolean[][] dinSource = new boolean[sizeSamples][];
//		boolean[][] dinSa = new boolean[sizeSamples][sizeGenes];
//		boolean[][] dinGl = new boolean[sizeSamples][sizeGenes];
//		for (int i = 0; i < sizeSamples; i++) {
//			dinSource[i] = gn_source.makeGeneExpresionState(Type_Dynamic.BOOLEAN, samples[i]);
//			sa.makeGeneExpresionState(samples[i], dinSa[i]);
//			gl.makeGeneExpresionState(samples[i], dinGl[i]);
//		}
//		System.out.println("Sem Agrupar:" + resources.distHaming(dinSource, dinSa) + "\t" + qfGl.getName() + ":"
//+ resources.distHaming(dinSource, dinGl));
//		for (int i = 0; i < sizeGenes; i++) {
//			double dhSa = resources.distHaming(dinSource, dinSa, i);
//			double dhGl = resources.distHaming(dinSource, dinGl, i);
//			// if (dhSa > dhGl) {
//			System.out.println("\n\n============================\nGene " + i + gn_source.getPredictors()[i] + "\tSa: "
//	+ dhSa + " > Gl: " + dhGl);
//			Details dsa = qfSa.quantifyDetails(sa.getPredictors()[i], ged, i);
//			int sizeTab = 1 << dsa.preditors.size();
//			qfSa.showDetail(dsa);
//			System.out.println("BL: " + resources.toTrueTableFormat(sa.getBoolean_functions()[i][0], sizeTab));
//			Details dgl = qfGl.quantifyDetails(gl.getPredictors()[i], ged, i);
//			qfGl.showDetail(dgl);
//			sizeTab = 1 << dgl.preditors.size();
//			System.out.println("BL: " + resources.toTrueTableFormat(gl.getBoolean_functions()[i][0], sizeTab));
//			// }
//		}
//	}
//
//	public static void testComp3() throws IOException {
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfSa = new WithoutGrouping(evaluationMetric);
//		QualifyingFunction qfGl = new GroupingLatticeSFS(evaluationMetric, 2);
//		boolean[][] ged = resources.readDeg(new File("test.deg"));
//		TreeSet<Integer> pred = new TreeSet<>();
//		pred.add(6);
//		pred.add(28);
//		Details details = qfGl.quantifyDetails(pred, ged, 46);
//		qfGl.showDetail(details);
//
//	}
//	public static void testQf() throws IOException {
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfLS = new LinearSelective(evaluationMetric, 1);
//		boolean[][] ged = resources.readDeg(new File("/home/fernandito/RMCF/deg/RANDOM__1__50__2.0__BOOLEAN__50__17.deg"));
//		TreeSet<Integer> pred = new TreeSet<>();
//		pred.add(6);
//		pred.add(28);
//		InferenceGene ig=new InferenceGene(qfLS, new IncrementalExhaustiveSearchWithSFS(4, 9));
//		GeneInfered g= ig.inferenceGene(ged, 0);
//System.out.println(g);
//	}
//
//	private static void testComp4() throws IOException {
//		String fileNet="/home/fernandito/RMCF/gabaritos/RANDOM__0__50__2.0.ylu";
//		String fileDEG="/home/fernandito/RMCF/deg/RANDOM__0__50__2.0__BOOLEAN__50__6.deg";
//		int gene=17;
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfSa = new WithoutGrouping(evaluationMetric);
//		QualifyingFunction qfCG = new CanalizateGrouping(evaluationMetric);
//		boolean[][] geneExpressionData = resources.readDeg(new File(fileDEG));
//		SearchAlgoritm searchAlgoritm2 = new NDimencionExhaustiveSearch(2);
//		SearchAlgoritm searchAlgoritm3 = new NDimencionExhaustiveSearch(3);
//		ArrayList<Details> listDetails = searchAlgoritm2.searchPreditorsDetails(geneExpressionData, gene, qfSa);
//		GeneticNetwork gn_source = GeneticNetwork.readFromFile(fileNet);
//		System.out.println("\n\n============================\nGene " + gene + gn_source.getPredictors()[gene]);
//		for (Iterator iterator = listDetails.iterator(); iterator.hasNext();) {
//			Details detailsL = (Details) iterator.next();
//
//			Details details = qfSa.quantifyDetails(detailsL.preditors, geneExpressionData, gene);
//			qfSa.showDetail(details);
//		}
//		System.out.println("------------------------------");
//		listDetails = searchAlgoritm2.searchPreditorsDetails(geneExpressionData, gene, qfCG);
//		double fc=0;
//		for (Iterator iterator = listDetails.iterator(); iterator.hasNext();) {
//			Details detailsL = (Details) iterator.next();
//
//			Details details = qfCG.quantifyDetails(detailsL.preditors, geneExpressionData, gene);
//			qfCG.showDetail(details);
//			fc=details.qualityValue;
//		}
//		listDetails = searchAlgoritm3.searchPreditorsDetails(geneExpressionData, gene, qfCG);
//		for (Iterator iterator = listDetails.iterator(); iterator.hasNext();) {
//			Details detailsL = (Details) iterator.next();
//
//			Details details = qfCG.quantifyDetails(detailsL.preditors, geneExpressionData, gene);
//			qfCG.showDetail(details);
//			System.out.println(details.qualityValue>fc);
//		}
//		System.out.println("------------------------------");
//	}
//	private static void testComp5() throws IOException {
//		String fileNet="/home/fernandito/RMCF/gabaritos/RANDOM__0__50__2.0.ylu";
//		String fileDEG="/home/fernandito/RMCF/deg/RANDOM__0__50__2.0__BOOLEAN__50__6.deg";
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfSa = new WithoutGrouping(evaluationMetric);
//		QualifyingFunction qfCG = new CanalizateGrouping(evaluationMetric);
//		boolean[][] geneExpressionData = resources.readDeg(new File(fileDEG));
//		SearchAlgoritm searchAlgoritm= new IncrementalExhaustiveSearchWithSFS(4, 9);
//		GeneticNetwork gn_source = GeneticNetwork.readFromFile(fileNet);
//		InferenceGeneticNetwork ig = new InferenceGeneticNetwork(qfSa, searchAlgoritm);
//		GeneticNetwork gn = ig.inferenceTopologyNetwork(geneExpressionData);
//		gn.show();
//		InferenceGeneticNetwork ig2 = new InferenceGeneticNetwork(qfCG, searchAlgoritm);
//		GeneticNetwork gn2 = ig2.inferenceTopologyNetwork(geneExpressionData);
//		gn2.show();
//	}
//
//	public static void name() throws IOException {
//		EvaluationMetric evaluationMetric = new MutualInformation();
//		QualifyingFunction qfGl = new GroupingLatticeSFS(evaluationMetric, 2);
//		boolean[][] ged = resources.readDeg(new File("test.deg"));
//		// SearchAlgoritm searchAlgoritm= new IncrementalExhaustiveSearchWithSFS(5, 10);
//		// ArrayList<Details> d= searchAlgoritm.searchPreditorsDetails(ged, 3, qfGl);
//		TreeSet<Integer> p = new TreeSet<>();
//		p.add(9);
//		p.add(19);
//		Details d = qfGl.quantifyDetails(p, ged, 3);
//		int[][] a = { { 0, 1 }, { 8, 0 }, { 2, 0 }, { 0, 8 } };
//		int[][] b = { { 8, 0 }, { 2, 0 }, { 0, 9 } };
//		MutualInformation m = new MutualInformation();
//		System.out.println(m.quantifyFromFrequencyTable(a));
//		System.out.println(m.quantifyFromFrequencyTable(b));
//	}
	public static int max(int[] arr) {
		int max = -1;
		for (int i = 0; i < arr.length; i++) {
			if (max < arr[i])
				max = arr[i];
		}
		return max;
	}

	public static int indexOf(int[] arr, int value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == value) {
				return i;
			}
		}
		return -1;
	}

	public static void makeGEN(String pathFolder, int desde, int countNetworks, int countDEGs) throws IOException {
		String userHome = System.getProperty("user.home");
		// String path = userHome+"/NC_RANDOM_S0/";
		String path = userHome + "/" + pathFolder + "/";
		int testsNetworks = countNetworks * 10;
		int testsDEGs = countDEGs * 100;
		HashMap<Type_Function, Double> configFunction = new HashMap<>();
		configFunction.put(Type_Function.R, 1.0);
//		configFunction.put(Type_Function.C, 0.8);
//		configFunction.put(Type_Function.T, 0.2);
		int[] pesosNet = new int[countNetworks];

		@SuppressWarnings("unchecked")
		Pair<GeneticNetwork, BooleanGeneExpressionData[]>[] networkSet = new Pair[countNetworks];
		// TreeSet<Pesable<Pair<GeneticNetwork,
		// TreeSet<Pesable<BooleanGeneExpressionData>>>>>
		// = new TreeSet<>();
		int maxNet = -1;
		for (int i = 0; i < testsNetworks; i++) {
			GeneticNetwork gn = GeneticNetwork.buildNetwork(5080, 2, 3.0, 0.98, Topology_Network.RANDOM,
					configFunction);
			BooleanGeneExpressionData[] geds = new BooleanGeneExpressionData[countDEGs];
			int[] pesosDeg = new int[countDEGs];
			int pesoNet = 0;
			int maxDeg = -1;
			for (int j = 0; j < testsDEGs; j++) {
				BooleanGeneExpressionData ged = gn.createGeneExpressionData(47, 1, Type_Dynamic.PROBABILISTIC_BOOLEAN,
						null, false);
				int pesoDeg = ged.constantsGens(0.1).size();
				if (j < countDEGs) {
					geds[j] = ged;
					pesosDeg[j] = pesoDeg;
					pesoNet += pesoDeg;
					if (pesoDeg > maxDeg)
						maxDeg = pesoDeg;
				} else {
					if (pesoDeg < maxDeg) {
						int index = indexOf(pesosDeg, maxDeg);
						geds[index] = ged;
						pesosDeg[index] = pesoDeg;
						pesoNet += pesoDeg;
						pesoNet -= maxDeg;
						maxDeg = max(pesosDeg);
					}
				}
			}
			if (i < countNetworks) {
				networkSet[i] = new Pair<>(gn, geds);
				pesosNet[i] = pesoNet;
				if (pesoNet > maxNet)
					maxNet = pesoNet;
			} else {
				if (pesoNet < maxNet) {
					int index = indexOf(pesosNet, maxNet);
					networkSet[index] = new Pair<>(gn, geds);
					pesosNet[index] = pesoNet;
					maxNet = max(pesosNet);
				}
			}
		}
		for (int i = 0; i < networkSet.length; i++) {
			Pair<GeneticNetwork, BooleanGeneExpressionData[]> pair = networkSet[i];
			// guardar network
			String nn = "RANDOM__" + desde + "__50__3.0";
			pair.getKey().writeInFile(path + "gabaritos/" + nn);
			BooleanGeneExpressionData[] geds = pair.getValue();
			for (int j = 0; j < geds.length; j++) {
				BooleanGeneExpressionData ged = geds[j];
				ged.saveDeg(path + "deg/" + nn + "__PROBABILISTIC_BOOLEAN__30__" + j);

			}
			System.out.println(desde + "\t" + pesosNet[i]);
			desde++;
		}
	}

	public static void makeGEN(String pathFolder, int desde, int hasta) throws IOException {
		// testInf(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
//	testRebuildDEG();
		String userHome = System.getProperty("user.home");
		// String path = userHome+"/NC_RANDOM_S0/";
		String path = userHome + "/" + pathFolder + "/";
//	String sdeg = path + "deg/RANDOM__0__50__3.0__BOOLEAN__30__0";
//	String nameNet=path + "gabaritos/RANDOM__";
//	String nameDEG="";
		for (int n = desde; n <= hasta; n++) {
			GeneticNetwork gn = buildNetwork();
			BooleanGeneExpressionData[] ged30 = buidGED(gn, 4, 46, 1000);
			// BooleanGeneExpressionData[] ged50 = buidGED(gn, 4, 50, 10000);
			if (ged30 != null) {
// if (ged30 != null && ged50 != null) {
				String nn = "RANDOM__" + n + "__50__3.0";
				gn.writeInFile(path + "gabaritos/" + nn);
				for (int i = 0; i < ged30.length; i++) {
					ged30[i].saveDeg(path + "deg/" + nn + "__PROBABILISTIC_BOOLEAN__30__" + i);
				}
//for (int i = 0; i < ged50.length; i++) {
//	ged50[i].saveDeg(path + "deg/" + nn + "__PROBABILISTIC_BOOLEAN__50__" + i);
//}
				System.out.println(n);
			} else
				n--;
		}
	}

	public static void refor() throws IOException {
		StringBuilder sb = new StringBuilder();
		String userHome = System.getProperty("user.home");
		BufferedReader in = new BufferedReader(new FileReader(userHome + "/p10_stepsIES.csv"));
		String line = null;
		PropertiesTestC pro = new PropertiesTestC("jpGN.properties");
		String path = userHome + "/RANDOMF/";
		int il = 0;
		while ((line = in.readLine()) != null) {
			il++;
			if (line.endsWith("1.0"))
				sb.append(line).append("\n");
			else {
				if (line.equals(""))
					break;
				String[] configLine = line.split(" ");
				if (configLine.length >= 11)
					sb.append(line).append("\n");
				else {
					in.readLine();
					String n = configLine[0];
					String sz = configLine[2];
					String i = configLine[1];
					String nn = "RANDOM__" + n + "__50__3.0";
					BooleanGeneExpressionData ged = new BooleanGeneExpressionData(
							path + "deg/" + nn + "__PROBABILISTIC_BOOLEAN__" + sz + "__" + i);
					ged.setPorcentageValue(0);
					SearchAlgoritm sfs = new IncrementalExhaustiveSearch(4);
					String func = configLine[3];
					QualifyingFunction function = pro.configInferences.get(ged.sizeData)
							.getQualifyingFunctionById(ID_QUALIFYING_FUNCTION.valueOf(func), ID_EVALUATION_METRIC.IM);
					int geneTarget = Integer.parseInt(configLine[6]);
					Deque<Double>[] eqf = sfs.evolutionQualityFunction(ged, geneTarget, function, false);
					String parteconf = Resources.toString(configLine, " ", 0, 7);
					sb.append(parteconf + " HY " + Resources.toString(eqf[0], " ")).append("\n");
					sb.append(parteconf + " HYt " + Resources.toString(eqf[1], " ")).append("\n");
					System.out.println(il + "\t" + line);
				}
			}
		}
		in.close();
		PrintWriter writer = new PrintWriter(userHome + "/2_p10_stepsIES.csv");
		writer.print(sb.toString());
		writer.close();
	}

	public static void formatText(String pathFile, String nameFile, int ntop) throws IOException {
		TreeMap<Double, TreeSet<Integer>> gene = new TreeMap<>();
		BufferedReader in = new BufferedReader(new FileReader(pathFile + nameFile));
		String line;// = in.readLine();
		String[] configLine = null;// = line.split(" ");
		TreeSet<Integer> duplis = new TreeSet<>();
		String topS = ntop + "_";
		if (ntop <= 0) {
			ntop = Integer.MAX_VALUE;
			topS = "";
		}
		int nline = 0;
		while (((line = in.readLine()) != null) && (nline < ntop)) {
			nline++;
			configLine = line.split(" ");
			Double key = Double.parseDouble(configLine[configLine.length - 1]);
			if (!gene.containsKey(key))
				gene.put(key, new TreeSet<>());
			for (int i = 1; i < configLine.length - 1; i++) {
				Integer e = Integer.valueOf(configLine[i]);
				if (duplis.add(e))
					gene.get(key).add(e);
			}
		}
		in.close();
		PrintWriter writer = new PrintWriter(pathFile + "g_" + topS + nameFile);
		final String nome = configLine[0];
		gene.forEach((k, v) -> {
			if (!v.isEmpty())
				writer.println(
						nome + " " + v.stream().map(o -> o.toString()).collect(Collectors.joining(" ")) + " " + k);
		});
		writer.close();
		// System.out.println(gene);
		System.out.println(nome + "->" + duplis.size());
	}

	public static void sfsDuplas(String pathFile, int targetGene, QualifyingFunction function,
			BooleanGeneExpressionData ged) throws IOException {
		ArrayList<Details> genes = new ArrayList<>();
		String name = function.getId().toString() + targetGene + ".txt";
		BufferedReader in = new BufferedReader(new FileReader(pathFile + name));
		String line;// = in.readLine();
		String[] configLine = null;// = line.split(" ");
		SequentialForwardSelection sfs = new SequentialForwardSelection(10);
		while ((line = in.readLine()) != null) {
			configLine = line.split(" ");
			Double im = Double.parseDouble(configLine[configLine.length - 1]);
			TreeSet<Integer> preditors = new TreeSet<>();
			for (int i = 1; i < configLine.length - 1; i++) {
				Integer e = Integer.valueOf(configLine[i]);
				preditors.add(e);
			}
			Details details = new Details();
			details.preditors = preditors;
			details.qualityValue = im;
			List<Details> listBest = new ArrayList<>();
			listBest.add(details);
			List<Details> sfslist = sfs.reducePreditorsDetails(function,
					sfs.searchPreditorsDetails(ged, targetGene, function, null, listBest, false, true).getKey());
			Details gen = sfslist.get(Resources.random.nextInt(sfslist.size()));
			genes.add(gen);
			// System.out.println(gen.getPredictors()+""+gen.getCritery_function_values());
		}
		in.close();
		PrintWriter writer = new PrintWriter(pathFile + "sfs_" + name);
//	
//	
		final String ng = configLine[0];
		for (Details gen : genes) {
			writer.println(ng + " " + gen.preditors.stream().map(o -> o.toString()).collect(Collectors.joining(" "))
					+ " " + gen.qualityValue);
		}
//	gene.forEach((k,v)->{
//		if(!v.isEmpty())
//			writer.println(nome+" "+
//	v.stream().map(o->o.toString()).collect(Collectors.joining( " " ))+" "+
//	k);		
//	});
		writer.close();
		// System.out.println(gene);
		System.out.println(name + "->");
	}

	public static void formatText(String pathFile, int[] arrayTargets, int ntop) throws IOException {
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			for (int i : arrayTargets) {
				String nameFile = "" + id_Function + i + ".txt";
				formatText(pathFile, nameFile, ntop);
			}
		}
	}

	static void formatTextLevel(String pathFile, int[] arrayTargets, int level) throws IOException {
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			for (int i : arrayTargets) {
				String nameFile = "" + id_Function + i + ".txt";
				BufferedReader in = new BufferedReader(new FileReader(pathFile + nameFile));
				String line;// = in.readLine();
				String[] configLine = null;// = line.split(" ");
				while ((line = in.readLine()) != null) {
					configLine = line.split(" ");
					for (int e = 1; e < configLine.length - 1; e++) {
						int gene = Integer.valueOf(configLine[e]);
						nameFile = "" + id_Function + gene + ".txt";
						formatText(pathFile, nameFile, 0);
					}
				}
				in.close();
			}
		}
	}

	public static void listPreditores(TreeSet<Integer> genes, String fullname) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fullname));
		String line;// = in.readLine();
		String[] configLine = null;// = line.split(" ");
		while (((line = in.readLine()) != null)) {
			configLine = line.split(" ");
			for (int i = 1; i < configLine.length - 1; i++) {
				Integer e = Integer.valueOf(configLine[i]);
				genes.add(e);
			}
		}
		in.close();
	}

	public static void listPreditores(String pathFile, int[] arrayTargets) throws IOException {
		ID_QUALIFYING_FUNCTION[] idFunctions = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.GLSFS, ID_QUALIFYING_FUNCTION.CG };
		for (ID_QUALIFYING_FUNCTION id_Function : idFunctions) {
			TreeSet<Integer> genes = new TreeSet<>();
			for (int i : arrayTargets) {
				String fullname = pathFile + id_Function + "" + i + ".txt";
				listPreditores(genes, fullname);
			}
			PrintWriter writer = new PrintWriter(pathFile + "sementes" + id_Function);
			writer.print(genes.stream().map(o -> o.toString()).collect(Collectors.joining("\n")));
			writer.close();
			// System.out.println(gene);
			System.out.println(pathFile + "sementes" + id_Function);
		}
	}

	public static void sfsDuplas(String pathFile, int[] arrayTargets, BooleanGeneExpressionData ged)
			throws IOException {
		EvaluationMetric evaluationMetric = new MutualInformation(ged.sizeData - 1);
		QualifyingFunction[] idFunctions = new QualifyingFunction[] { new WithoutGrouping(evaluationMetric),
				new LinearGrouping(evaluationMetric), new GroupingLatticeSFS(evaluationMetric, 2),
				new CanalizateGrouping(evaluationMetric) };
		for (QualifyingFunction function : idFunctions) {
			for (int i : arrayTargets) {
				sfsDuplas(pathFile, i, function, ged);
			}
		}
	}

	public static void sfsDuplas(String pathFile, int me, int[] arrayTargets, BooleanGeneExpressionData ged)
			throws IOException {
		EvaluationMetric evaluationMetric = new MutualInformation(ged.sizeData - 1);
		QualifyingFunction[] idFunctions = new QualifyingFunction[] { new WithoutGrouping(evaluationMetric),
				new LinearGrouping(evaluationMetric), new GroupingLatticeSFS(evaluationMetric, 2),
				new CanalizateGrouping(evaluationMetric) };
//	for (QualifyingFunction function : idFunctions) {
		QualifyingFunction function = idFunctions[me];
		for (int i : arrayTargets) {
			sfsDuplas(pathFile, i, function, ged);
		}
//	}
	}

	public static void refor2() throws IOException {
		StringBuilder sb = new StringBuilder();
		String userHome = System.getProperty("user.home");
		BufferedReader in = new BufferedReader(new FileReader(userHome + "/2_p10_stepsIES.csv"));
		String line = in.readLine();
		sb.append(line).append("\n");
		int il = 0;
		while ((line = in.readLine()) != null) {
			if (line.equals(""))
				break;
			il++;
			if (il % 2 != 0) {
				sb.append(line).append("\n");
			} else {
				String[] configLine = line.split(" ");
				String hyVal = configLine[7];
				if (hyVal.equals("HY")) {
					configLine[7] = "HYt";
					sb.append(Resources.toString(configLine, " ")).append("\n");
					System.out.println(il + "\t" + line);
				} else
					sb.append(line).append("\n");
			}
		}
		in.close();
		PrintWriter writer = new PrintWriter(userHome + "/2b_p10_stepsIES.csv");
		writer.print(sb.toString());
		writer.close();
	}

	public static void viewEv() {
		String userHome = System.getProperty("user.home");
		String path = userHome + "/RANDOMF/";
		for (int n = 230; n < 231; n++) {
//		GeneticNetwork gn=null;
//		BooleanGeneExpressionData[] ged30=buidGED(gn, 4, 30,1000);
//		BooleanGeneExpressionData[] ged50=buidGED(gn, 4, 50,10000);
//		if(ged30!=null && ged50!=null) {
			String nn = "RANDOM__" + n + "__50__3.0";

//			gn=GeneticNetwork.readFromFile(path+"gabaritos/"+nn);
			for (int i = 2; i < 3; i++) {
				BooleanGeneExpressionData ged30 = new BooleanGeneExpressionData(
						path + "deg/" + nn + "__PROBABILISTIC_BOOLEAN__50__" + i);
				ged30.setPorcentageValue(0);
				SearchAlgoritm sfs = new IncrementalExhaustiveSearch(4);
				QualifyingFunction function = new CanalizateGrouping(new MutualInformation(ged30.sizeData - 1));
//			InferenceNetwork inference = new InferenceNetwork(ged30, "infNet", ID_SEARCH_ALGORITM.IES,
//	ID_EVALUATION_METRIC.IM, ID_QUALIFYING_FUNCTION.CG); 
// NetworkInfered2 ni= inference.inferedNetwork();
				for (int geneTarget = 40; geneTarget < 41; geneTarget++) {
					List<Details> d = sfs.searchPreditorsDetails(ged30, geneTarget, function);
					System.out.println(d.size());
					System.out.println(d.get(0).frequencyTableString());
					NDimencionExhaustiveSearch nSearch = new NDimencionExhaustiveSearch(3);
					List<Details> e = nSearch.searchPreditorsDetails(ged30, geneTarget, function);
					System.out.println(e.size());
					System.out.println(e.get(0).frequencyTableString());
					nSearch.setDimencion(4);
					e = nSearch.searchPreditorsDetails(ged30, geneTarget, function);
					System.out.println(e.size());
					System.out.println(e.get(0).frequencyTableString());
					Deque<Double>[] eqf = sfs.evolutionQualityFunction(ged30, geneTarget, function, false);
					System.out.println(geneTarget + " - " + eqf[0].size() + "\t" + Arrays.toString(eqf));
				}
// ni.show();
			}

		}
	}

//	public static void inferenceTestTies() throws NumberFormatException, IOException {
//		PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile("a__IES_SFS__SA__IM.ylu");
//		DiscreteGeneExpressionData gdd = DiscreteGeneExpressionData
//				.importFrom("/home/fernandito/Downloads/amostras.dat", 5080, 47);
//		CustomGenesInference cgi = new CustomGenesInference("trSA", preInferedNetwork);
//		cgi.inferedPreNetworkTies(gdd).show();
//	}
//
//	public static void inferenceTestTiesAll(String path) throws IOException {
//		DiscreteGeneExpressionData gdd = DiscreteGeneExpressionData
//				.importFrom("/home/fernandito/Downloads/amostras.dat", 5080, 47);
//		try (Stream<Path> filePathStream = Files.walk(Paths.get(path))) {
//			filePathStream.forEach(filePath -> {
//				if (filePath.toString().endsWith("_c.ylu")) {
//					try {
//						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
//						String cf = filePath.toString().replace("_c.ylu", "");
//						CustomGenesInference cgi = new CustomGenesInference(cf, preInferedNetwork);
////						PreInferedNetwork preInferedNetworkTies = 
//								cgi.inferedPreNetworkTies(gdd);
////						preInferedNetworkTies.writeInFile(cf + "_ties");
////						preInferedNetworkTies.writeFormatGraph(cf + "_ties");
//						System.out.println(cf);
//					} catch (NumberFormatException | IOException e) {
//
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//	}
//	public static void inferenceTestTiesAllQf(String path) throws IOException {
//		DiscreteGeneExpressionData gdd = DiscreteGeneExpressionData
//				.importFrom("/home/fernandito/Downloads/amostras.dat", 5080, 47);
//		try (Stream<Path> filePathStream = Files.walk(Paths.get(path))) {
//			filePathStream.forEach(filePath -> {
//				if (filePath.toString().endsWith("_c.ylu")) {
//					try {
//						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
//						String cf = filePath.toString().replace("_c.ylu", "_tqf");
//						CustomGenesInference cgi = new CustomGenesInference(cf, preInferedNetwork);
////						PreInferedNetwork preInferedNetworkTies = 
//								cgi.inferedPreNetworkTiesQf(gdd);
////						preInferedNetworkTies.writeInFile(cf + "_ties");
////						preInferedNetworkTies.writeFormatGraph(cf + "_ties");
//						System.out.println(cf);
//					} catch (NumberFormatException | IOException e) {
//
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//	}

	public static void directoryToGrafhFormat(String directoryPath) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith(".ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.writeFormatGraph(filePath.toString().replace(".ylu", ""));
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void addClusterPreditorsAll(String directoryPath) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria", true);
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("M.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.addClusterPreditors(ged);
						preInferedNetwork.writeInFile(filePath.toString().replace(".ylu", "_t"));
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void completeClusterPreditorsAll(String directoryPath) throws IOException {
		BooleanGeneExpressionData ged = new BooleanGeneExpressionData("malaria");
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_t.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.completeClusterPreditors(ged);
						preInferedNetwork.writeInFile(filePath.toString().replace("_t.ylu", "_c"));
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void grafthFoomats(String directoryPath, ArrayList<Integer> customGens, int from, int to, String pref)
			throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_u.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.subPreInferedNetwork(customGens, from, to).writeFormatGraph(
								filePath.toString().replace("_u.ylu", "_" + pref + "_" + from + "_" + to));
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void grafthRadioFoomats(String directoryPath, ArrayList<Integer> customGens, int from, int to,
			String pref) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_u.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.writeRadioFormatGraph(
								filePath.toString().replace("_u.ylu", "_" + pref + "_" + from + "_" + to), customGens,
								from, to);
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void uniquePreNets(String directoryPath) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_e.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						preInferedNetwork.toUniquePreditor().writeInFile(filePath.toString().replace("_e.ylu", "_u"));
						System.out.println(filePath.toString());
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void coefCluster(String directoryPath, int[] sementes, String type) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_u.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						int[][] adj = preInferedNetwork.adjacencyMatrix();
						System.out.print(type + " " + filePath.getFileName().toString().replaceAll("_u.ylu", "") + " "
								+ preInferedNetwork.gensInfered.keySet().parallelStream()
										.mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum()
										/ preInferedNetwork.getSizeGenes());
						System.out.print(" " + IntStream.of(sementes)
								.mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum()
								/ sementes.length + "\n");
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void countPreditionEquals(String directoryPath, String pref, String suf) throws IOException {
		ID_QUALIFYING_FUNCTION[] metodos = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.CG, ID_QUALIFYING_FUNCTION.GLSFS };
		BooleanGeneExpressionData malData = new BooleanGeneExpressionData(directoryPath + "malaria");
		ConfigInference cf = new ConfigInference(malData.sizeData);
		for (ID_QUALIFYING_FUNCTION qf : metodos) {
			boolean[][] bI = new boolean[46][];
			boolean[][] bO = new boolean[46][];
			for (int i = 0; i < malData.sizeData; i++) {
				PreInferedNetwork g = PreInferedNetwork.readFromFile(directoryPath + i + "__" + pref + "__" + qf + suf);
				NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(i).getKey());
				List<Integer> genes = net.getIntegerGens();
				bI[i] = net.makeGeneExpresionState(malData.getData(i - 1), genes);
				bO[i] = malData.getData(i, genes);
			}
			System.out.println(qf + ":\t"
					+ (new BooleanGeneExpressionData(bO)).taxaAcertos((new BooleanGeneExpressionData(bI)), 0));
		}
	}

//	public static void countPreditionEquals2(String directoryPath, String pref, String suf) throws IOException {
//		ID_QUALIFYING_FUNCTION[] metodos = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
//				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.CG, ID_QUALIFYING_FUNCTION.GLSFS };
//		BooleanGeneExpressionData malData = new BooleanGeneExpressionData(directoryPath + "malaria");
//		ConfigInference cf = new ConfigInference(malData.sizeData);
//		for (ID_QUALIFYING_FUNCTION qf : metodos) {
//			boolean[][] bI = new boolean[46][];
//			boolean[][] bO = new boolean[46][];
//			for (int i = 0; i < 5; i++) {
//				int c = 3 + 8 * i;
//				PreInferedNetwork g = PreInferedNetwork
//						.readFromFile(directoryPath + c + "_8__" + pref + "__" + qf + suf);
//				NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(c, 8).getKey());
//				List<Integer> genes = net.getIntegerGens();
//				for (int j = 0; j < 8; j++) {
//					bI[c + j] = net.makeGeneExpresionState(malData.getData(c + j - 1), genes);
//					bO[c + j] = malData.getData(c + j, genes);
//				}
//			}
//			int init = 43;
//			PreInferedNetwork g = PreInferedNetwork
//					.readFromFile(directoryPath + init + "_8__" + pref + "__" + qf + suf);
//			NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(init, 6).getKey());
//			List<Integer> genes = net.getIntegerGens();
//			for (int j = 0; j < 6; j++) {
//				int i = (init + j) % 46;
//				bI[i] = net.makeGeneExpresionState(malData.getData(i - 1), genes);
//				bO[i] = malData.getData(i, genes);
//			}
//			System.out.println(qf + ":\t"
//					+ (new BooleanGeneExpressionData(bO)).taxaAcertos((new BooleanGeneExpressionData(bI)), 0));
//		}
//	}
//
//	public static void countPreditionEquals3(String directoryPath, String pref, String suf) throws IOException {
//		ID_QUALIFYING_FUNCTION[] metodos = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
//				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.CG, ID_QUALIFYING_FUNCTION.GLSFS };
//		BooleanGeneExpressionData malData = new BooleanGeneExpressionData(directoryPath + "malaria");
//		ConfigInference cf = new ConfigInference(malData.sizeData);
//		for (ID_QUALIFYING_FUNCTION qf : metodos) {
//			boolean[][] bI = new boolean[46][];
////			boolean[][] bO=new boolean[46][];
//			for (int i = 0; i < 5; i++) {
//				int c = 3 + 8 * i;
//				PreInferedNetwork g = PreInferedNetwork
//						.readFromFile(directoryPath + c + "_8__" + pref + "__" + qf + suf);
//				NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(c, 8).getKey());
//				// List<Integer> genes=net.getIntegerGens();
//				bI[c] = net.makeGeneExpresionState(malData.getData(c - 1)).getKey();
//				for (int j = 1; j < 8; j++) {
//					bI[c + j] = net.makeGeneExpresionState(bI[c + j - 1]).getKey();
//				}
//			}
//			int init = 43;
//			PreInferedNetwork g = PreInferedNetwork
//					.readFromFile(directoryPath + init + "_8__" + pref + "__" + qf + suf);
//			NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(init, 6).getKey());
////			List<Integer> genes=net.getIntegerGens();
//			bI[init] = net.makeGeneExpresionState(malData.getData(init - 1)).getKey();
//			for (int j = 1; j < 6; j++) {
//				int i = (init + j) % 46;
//				bI[i] = net.makeGeneExpresionState(bI[(i==0)?45:i - 1]).getKey();
//			}
//			System.out.println(qf + ":\t" + malData.taxaAcertos((new BooleanGeneExpressionData(bI)), 0));
//		}
//	}
	public static String countPreditionEquals2(String directoryPath, String pref,String suf) throws IOException {
        List<Integer> semA = Arrays.asList(new Integer[]{4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550});
        List<Integer> semB = Arrays.asList(new Integer[]{5007,5008,5009,5010,5011,5012,5013,5014,5015,5016,5017,5018,5019,5020,5021,5022,5023,5024,5025,5026,5027,5028,5029,5030,5031,5032,5033});
        ID_QUALIFYING_FUNCTION[] metodos=new ID_QUALIFYING_FUNCTION [] {ID_QUALIFYING_FUNCTION.SA,
                ID_QUALIFYING_FUNCTION.LG
                ,ID_QUALIFYING_FUNCTION.CG
                ,ID_QUALIFYING_FUNCTION.GLSFS};
        BooleanGeneExpressionData malData= new BooleanGeneExpressionData(directoryPath+"malaria");
        ConfigInference cf= new ConfigInference(malData.sizeData);
        StringBuilder sb=new StringBuilder();
        for (ID_QUALIFYING_FUNCTION qf : metodos) {
            boolean[][] bI=new boolean[46][];
            boolean[][] bO=new boolean[46][];
            for(int i=0;i<5;i++) {
                int c=3+8*i;
                PreInferedNetwork g=PreInferedNetwork.readFromFile(directoryPath+c+"_8__"+pref+"__"+qf+suf)
                        .toReduceCountPreditor();
                NetworkInfered net= g.toNetworkInfered(cf, malData.divideData(c,8).getKey());
                List<Integer> genes=net.getIntegerGens();
                for(int j=0;j<8;j++) {
                    bI[c+j]=net.makeGeneExpresionState(malData.getData(c+j-1),genes);
                    bO[c+j]=malData.getData(c+j,genes);
//                  System.out.println(qf.toString() + (c+j));
                }
            }
            int init=43;
            PreInferedNetwork g=PreInferedNetwork.readFromFile(directoryPath+init+"_8__"+pref+"__"+qf+suf);
            NetworkInfered net= g.toNetworkInfered(cf, malData.divideData(init,6).getKey());
            List<Integer> genes=net.getIntegerGens();
            for(int j=0;j<6;j++) {
                int i=(init+j)%46;
                bI[i]=net.makeGeneExpresionState(malData.getData(i-1),genes);
                bO[i]=malData.getData(i,genes);
//              System.out.println(qf.toString() + (i));
            }
            BooleanGeneExpressionData boe=new BooleanGeneExpressionData(bO);
            BooleanGeneExpressionData bie=new BooleanGeneExpressionData(bI);
            sb.append(qf)
                .append(",all,")
                .append(boe.taxaAcertos(bie, 0))
                .append('\n')
                .append(qf)
                .append(",g,")
                .append(boe.customGeneData(semA)
                        .taxaAcertos(bie.customGeneData(semA), 0))
                .append('\n')
                .append(qf)
                .append(",a,")
                .append(boe.customGeneData(semB)
                        .taxaAcertos(bie.customGeneData(semB), 0))
                .append('\n');
//          System.out.println();
//          bie.customGeneData(semA).print();
//          System.out.println();
        }
        return sb.toString();
    }
    public static String countPreditionEquals3(String directoryPath, String pref,String suf) throws IOException {
        List<Integer> semA = Arrays.asList(new Integer[]{4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550});
        List<Integer> semB = Arrays.asList(new Integer[]{5007,5008,5009,5010,5011,5012,5013,5014,5015,5016,5017,5018,5019,5020,5021,5022,5023,5024,5025,5026,5027,5028,5029,5030,5031,5032,5033});
        ID_QUALIFYING_FUNCTION[] metodos=new ID_QUALIFYING_FUNCTION [] {ID_QUALIFYING_FUNCTION.SA,
                ID_QUALIFYING_FUNCTION.LG
                ,ID_QUALIFYING_FUNCTION.CG
                ,ID_QUALIFYING_FUNCTION.GLSFS};
        BooleanGeneExpressionData malData= new BooleanGeneExpressionData(directoryPath+"malaria");
        ConfigInference cf= new ConfigInference(malData.sizeData);
        StringBuilder sb=new StringBuilder();
        for (ID_QUALIFYING_FUNCTION qf : metodos) {
            boolean[][] bI=new boolean[46][];
//          boolean[][] bO=new boolean[46][];
            for(int i=0;i<5;i++) {
                int c=3+8*i;
                PreInferedNetwork g=PreInferedNetwork.readFromFile(directoryPath+c+"_8__"+pref+"__"+qf+suf)
                        .toReduceCountPreditor();
                NetworkInfered net= g.toNetworkInfered(cf, malData.divideData(c,8).getKey());
                         
                //List<Integer> genes=net.getIntegerGens();
                bI[c]=net.makeGeneExpresionState(malData.getData(c-1)).getKey();
                for(int j=1;j<8;j++) {
                    bI[c+j]=net.makeGeneExpresionState(bI[c+j-1]).getKey();
                }
            }
            int init=43;
            PreInferedNetwork g=PreInferedNetwork.readFromFile(directoryPath+init+"_8__"+pref+"__"+qf+suf);
            NetworkInfered net= g.toNetworkInfered(cf, malData.divideData(init,6).getKey());
//          List<Integer> genes=net.getIntegerGens();
            bI[init]=net.makeGeneExpresionState(malData.getData(init-1)).getKey();
            for(int j=1;j<6;j++) {
                int i=(init+j)%46;
                bI[i]=net.makeGeneExpresionState(bI[(i==0)?45:i - 1]).getKey();
            }
            BooleanGeneExpressionData bie=new BooleanGeneExpressionData(bI);
            sb.append(qf)
                .append(",all,")
                .append(malData.taxaAcertos(bie, 0))
                .append('\n')
                .append(qf)
                .append(",g,")
                .append(malData.customGeneData(semA)
                        .taxaAcertos(bie.customGeneData(semA), 0))
                .append('\n')
                .append(qf)
                .append(",a,")
                .append(malData.customGeneData(semB)
                        .taxaAcertos(bie.customGeneData(semB), 0))
                .append('\n');
        }
        return sb.toString();
    }
	public static String countEvolution(String directoryPath, String pref, String suf) throws IOException {
		ID_QUALIFYING_FUNCTION[] metodos = new ID_QUALIFYING_FUNCTION[] { ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.LG, ID_QUALIFYING_FUNCTION.CG, ID_QUALIFYING_FUNCTION.GLSFS };
		BooleanGeneExpressionData malData = new BooleanGeneExpressionData(directoryPath + "malaria");
		ConfigInference cf = new ConfigInference(malData.sizeData);
		StringBuilder sb = new StringBuilder();
		for (ID_QUALIFYING_FUNCTION qf : metodos) {
			boolean[] bI = new boolean[46];
//			boolean[][] bO=new boolean[46][];
			for (int i = 0; i < 5; i++) {
				int c = 3 + 8 * i;
				PreInferedNetwork g = PreInferedNetwork
						.readFromFile(directoryPath + c + "_8__" + pref + "__" + qf + suf).toReduceCountPreditor();
				NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(c, 8).getKey());

				// List<Integer> genes=net.getIntegerGens();
				bI = net.makeGeneExpresionState(malData.getData(c - 1)).getKey();
				sb.append(qf).append(",1,").append(BooleanGeneExpressionData.taxaAcertos(malData.getData(c), bI))
				.append('\n');
				for (int j = 1; j < 8; j++) {
					bI = net.makeGeneExpresionState(bI).getKey();
					sb.append(qf).append(",").append(j + 1).append(",")
							.append(BooleanGeneExpressionData.taxaAcertos(malData.getData(c + j), bI))
							.append('\n');
				}
			}
			int init = 43;
			PreInferedNetwork g = PreInferedNetwork
					.readFromFile(directoryPath + init + "_8__" + pref + "__" + qf + suf);
			NetworkInfered net = g.toNetworkInfered(cf, malData.divideData(init, 6).getKey());
//			List<Integer> genes=net.getIntegerGens();
			bI = net.makeGeneExpresionState(malData.getData(init - 1)).getKey();
			sb.append(qf).append(",1,").append(BooleanGeneExpressionData.taxaAcertos(malData.getData(init), bI))
			.append('\n');
			for (int j = 1; j < 6; j++) {
				int i = (init + j) % 46;
				bI = net.makeGeneExpresionState(bI).getKey();
				sb.append(qf).append(",").append(j + 1).append(",")
						.append(BooleanGeneExpressionData.taxaAcertos(malData.getData(i), bI))
						.append('\n');
			}

		}
		return sb.toString();
	}

	public static void distFreq(String directoryPath, int[] sementesA, int[] sementesB) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_u.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						int[][] adj = preInferedNetwork.distanceMatrix();
						System.out.println(filePath.getFileName().toString());
						Map<Integer, Integer> dist = preInferedNetwork.distanceFrequency(adj);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println("M&D:\t" + Arrays.toString(preInferedNetwork.distMedDP(adj)));
						System.out.println();
						System.out.println("sementes A:");
						dist = preInferedNetwork.distanceFrequency(adj, sementesA);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println("M&D:\t" + Arrays.toString(preInferedNetwork.distMedDP(adj, sementesA)));
						System.out.println();
						System.out.println("sementes B:");
						dist = preInferedNetwork.distanceFrequency(adj, sementesB);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println("M&D:\t" + Arrays.toString(preInferedNetwork.distMedDP(adj, sementesB)));
						System.out.println();
						System.out.println();
//	System.out.print(filePath.getFileName().toString()+" "+ 
//			preInferedNetwork.gensInfered.keySet().parallelStream().mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum()
//					/ preInferedNetwork.getSizeGenes());
//	System.out.print(
//			" "+IntStream.of(sementes).mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum() / sementes.length +"\n");
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void distMedDP(String directoryPath, int[] sementesA, int[] sementesB) throws IOException {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directoryPath))) {
			filePathStream.forEach(filePath -> {
				if (filePath.toString().endsWith("_u.ylu")) {
					try {
						PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(filePath.toFile());
						int[][] adj = preInferedNetwork.distanceMatrix();
						System.out.println(filePath.getFileName().toString());
						Map<Integer, Integer> dist = preInferedNetwork.distanceFrequency(adj);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println();
						System.out.println("sementes A:");
						dist = preInferedNetwork.distanceFrequency(adj, sementesA);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println();
						System.out.println("sementes B:");
						dist = preInferedNetwork.distanceFrequency(adj, sementesB);
						dist.forEach((k, v) -> System.out.println(k + "->" + v));
						System.out.println();
						System.out.println();
//	System.out.print(filePath.getFileName().toString()+" "+ 
//			preInferedNetwork.gensInfered.keySet().parallelStream().mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum()
//					/ preInferedNetwork.getSizeGenes());
//	System.out.print(
//			" "+IntStream.of(sementes).mapToDouble(o -> PreInferedNetwork.clusteringCoefficient(o, adj)).sum() / sementes.length +"\n");
					} catch (NumberFormatException | IOException e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

//	public static void comprobar() throws NumberFormatException, IOException {
//		PreInferedNetwork p=PreInferedNetwork.readFromFile("/home/fernandito/MALARIA_TEST/all/all__IES_SFS__CG__IM_t");
//		DiscreteGeneExpressionData gdd = DiscreteGeneExpressionData
//				.importFrom("/home/fernandito/Downloads/amostras.dat", 5080, 47);
//	//	CustomGenesInference cgi = new CustomGenesInference(new int[] {3505},
//	//			ged, "/home/fernandito/MALARIA_TEST/all/all__IES_SFS__CG__IM_t", p.getSearchAlgoritm(), p.getEvaluationMetric(), p.getQualifyingFunction()) ;
//		CustomGenesInference cgi = new CustomGenesInference("/home/fernandito/MALARIA_TEST/all/all__IES_SFS__CG__IM_t",p) ;
//		cgi.reTiesFromDiscreteById(3505, gdd);
//		
//	}:-:-
	public static void main(String[] args) throws IOException {

//      makeGEN("ml", 5,5,4);
//      IntStream.range(0, 5080).forEach(System.out::println);
        String directoryPath = System.getProperty("user.home") + "/MALARIA_TEST/knnEndTest/tranfer/50NC";
        directoryPath = System.getProperty("user.home")+"/MALARIA_TEST/cut8allRE/knn/";
//        directoryPath = System.getProperty("user.home")+"/MALARIA_TEST/cut8allRE/";
//      uniquePreNets(directoryPath);
        String suf="";
        suf="_e";
        StringBuilder aSB=new StringBuilder();
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            aSB.append(countEvolution(directoryPath, "all", suf));
        }
        System.out.println(aSB);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("knncountEv.csv"))) {
            bw.append(aSB);//Internally it does aSB.toString();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		ArrayList<Integer> customGens = new ArrayList<Integer>(
//				Arrays.asList(new Integer[] { 
//    				4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550,
//    				5007,5008,5009,5010,5011,5012,5013,5014,5015,5016,5017,5018,5019,5020,5021,5022,5023,5024,5025,5026,5027,5028,5029,5030,5031,5032,5033
//				}));
//////		int[] semA = {4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550};
//////		int[] semB = {5007,5008,5009,5010,5011,5012,5013,5014,5015,5016,5017,5018,5019,5020,5021,5022,5023,5024,5025,5026,5027,5028,5029,5030,5031,5032,5033};
////////		coefCluster(directoryPath, customGens.stream().mapToInt(Integer::intValue).toArray(),"b");
//////		distFreq(directoryPath, semA, semB);
//		int[] pre= {1,2,3};
//		int[] pos= {0,1,2,3};
//		for (int i : pre) {
//			for (int j : pos) {
//				grafthRadioFoomats(directoryPath, customGens, i, j, "rab");
//				grafthFoomats(directoryPath, customGens, i, j, "ab");
//			}
//		}
//		 grafthFoomats(directoryPath, customGens, 1, 0, "b");
//		uniquePreNets(directoryPath);
		// PreInferedNetwork
		// p=PreInferedNetwork.readFromFile("/home/fernandito/MALARIA_TEST/all/all__IES_SFS__CG__IM_e");
//		p.clearTiesforRepetPreditors();
//		p.show();
//		comprobar();
//		 inferenceTestTiesAllQf("/home/fernandito/MALARIA_TEST/all");
//		completeClusterPreditorsAll("/home/fernandito/MALARIA_TEST/all");
//		addClusterPreditorsAll("/home/fernandito/MALARIA_TEST/all");
//		PreInferedNetwork p=PreInferedNetwork.readFromFile("/home/fernandito/MALARIA_TEST/all/t/all__IES_SFS__SA__IM_t_c");

//		grafthFoomats("/home/fernando/MALARIA_TEST/all", customGens, 3, 2, "b");
////		p.subPreInferedNetwork(customGens, 1, 1).show();
//		BooleanGeneExpressionData ged=new BooleanGeneExpressionData("malaria");
//		p.addClusterPreditors(ged.clusterForRepeat());
//		p.writeInFile("/home/fernandito/all__IES_SFS__SA__IM_t");
//		inferenceTestTiesAll("/home/fernandito/MALARIA_TEST/ties2");
//		//directoryToGrafhFormat("/home/fernandito/MALARIA_TEST/ties1");
//		if(args.length>0)
//		makeGEN(args[0], Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
//		else
//			makeGEN("ml", 15,5,4);
//		BooleanGeneExpressionData ged=new BooleanGeneExpressionData("malaria");
//		sfsDuplas("/home/fernandito/MALARIA_TEST/malaria/d_", 4546, new CanalizateGrouping(new MutualInformation()), ged);
//		sfsDuplas("/home/fernandito/MALARIA_TEST/malaria/d_", 
//sfsDuplas(args[0],Integer.parseInt(args[1]),
//		formatText("/home/fernando/MALARIA_TEST/malsfs/d_",
		// testInf2(new int[] {
//		testInf2();
//		listPreditores("", new int[] { 4546, 3881, 1879, 3206, 3907, 3625, 1949, 2286, 2391, 4550,
////		
//5007, 5008, 5009, 5010, 5011, 5012, 5013, 5014, 5015, 5016, 5017, 5018, 5019, 5020, 5021, 5022, 5023,
//5024, 5025, 5026, 5027, 5028, 5029, 5030, 5031, 5032, 5033 });
//		}, ged);
//		testInf(
//		formatText("", 
//		new int[] {
////4546,
//3881,
//1879,
//3206,
//3907,
//3625,
//1949,
//2286,
//2391,
//4550},0);
//		testInf(new int[] {
//		5007,
//		5008,
//		5009,
//		5010,
//		5011,
//		5012,
//		5013,
//		5014,
//		5015,
//		5016,
//		5017,
//		5018,
//		5019,
//		5020,
//		5021,
//		5022,
//		5023,
//		5024,
//		5025,
//		5026,
//		5027,
//		5028,
//		5029,
//		5030,
//		5031,
//		5032,
//		5033},0);
//		testInf2(new int[] {
//3881
//});
//		refor2();
		// for (int i = 0; i < ged50.length; i++) {
//	ged50[i].saveDeg(path+"deg/"+nn+"__BOOLEAN__50__"+i);
//}
//System.out.println(n);
//			}
//			else
//n--;
//		String a = "4546,3881,1879,3206,3907,3625,1949,2286,2391,4550";
//		String b = "5007,5008,5009,5010,5011,5012,5013,5014,5015,5016,"
//				+ "5017,5018,5019,5020,5021,5022,5023,5024,5025,5026," + "5027,5028,5029,5030,5031,5032,5033";
////		inferenceCustom("32,51,59,67,197,207,392,582," + "591,687,701,1160,1291,1386,1470,"
//+ "1644,1659,1721,1727,1793,1976,2112," + "2147,2382,2451,2490,2655,2868,3059,"
//+ "3067,3074,3100,3106,3141,3534," + "3965,3977,4093,4095,4167,4276,4351,"
//+ "4422,4555,4587,4590,4597,4616,4728,4917", a + "," + b, ",", "ab_Mal");
//		formatInferenceCustomDirectory("/home/fernandito/MALARIA_TEST/inferenceEmpKnn/");
//		inferenceTestTiesAll("/home/fernandito/MALARIA_TEST/inferenceEmp/");

	}
	// testCo2();
//		analiseNetwork();
	// testInf(0,20);
//		Random r=new Random();
//		System.out.println(r.nextInt(10));
//		System.out.println(Resources.random.nextInt());
//		testComp();
//		System.out.println("HM");
//		BooleanGeneExpressionData ged =new BooleanGeneExpressionData("test");
//		InferenceNetwork inf = new InferenceNetwork(ged,"inf",ID_SEARCH_ALGORITM.NDES,ID_EVALUATION_METRIC.IM,ID_QUALIFYING_FUNCTION.SA);
//		inf.inferedNetwork();
//		TreeSet<Integer>[] can= AbstractNetwork.canalizingByDEG(ged);
//		System.out.println(Arrays.toString(ged.getGeneExpressionByGene(0)));
//		System.out.println(ged.getGeneExpressionByGene(154));

	// testInf(434,435);
//		if (args[0].equals("CS"))
//			timeCanalize2(1);
//		if (args[0].equals("CGL"))
//			timeCanalize1(1);
//		timeCanalize2();
//		timeCanalize2();
//		timeCanalize2();
//		timeCanalize2();
	// testComp();
//		detailsCanalize3();

}