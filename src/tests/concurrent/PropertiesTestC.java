package tests.concurrent;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;
import inference.ConfigInference;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
//import tests.TestResume.Type_Test;

public class PropertiesTestC {
	public enum Type_Test{
		DEGREE_GABARITO,
		DEGREE_INFERENCE,
		DEGREE_GENE_INFERENCE,
		VALIDATE_DEGREE,
		VALIDATE_DINAMIC;

	}
	public enum Degree_Comparate{
		I,
		M,
		T;
	}
	final double[] fatorPenalizacaoNO;
	final int[] fatorPenalizacaoPO;
	// directory params
	final String pathTest;
	final String gabaritosDirectory;
	final String inferenceDirectory;
	final String degDirectory;
	final String resumeDirectory;

	// config varaibles
	final int sizeNetworks;
	final int[] sizeTimes;
	final int sizeGens;
	final int sizeSamplesPerNetwork;
	final double maxProbabilityPBN;
	final double[] meanDegree;
	final Type_Dynamic[] dynamics;
	final Topology_Network[] topologies;
	// final int nGrouping=7;
	final int testDegreeValidate;
	final Type_Test typeTest;
	final int notPenalize;// 0 whit not penaliza 1 whitout penalize
	final String filtroValidate;
	final boolean isOriginalNetwork;
	final String nameFile;
	final int maxNetworks;
	final int minNetworks;
	final int maxSamples;
	final int minSamples;
	final ID_SEARCH_ALGORITM searchAlgoritmic;
	final int nThreads;
	// final String validateDinamicVersion;
	// resume varaibles
	final boolean VALIDATE_DINAMIC;
	final int sizeDinamicTets;
	final boolean TOPOLOGY;
	final boolean TOPOLOGY_DEGREE;
	final int[] degreesTopology;
	final ID_QUALIFYING_FUNCTION[] groupingMethodologies;
	final ID_EVALUATION_METRIC[] metrics;
	final ID_EVALUATION_METRIC additionalMetric;
	int[] groupingParam = new int[] { 0, 0, 1, 1, 2, 2, 2, 0 };
	final boolean degWhitLoops;
	public final Map<Integer, ConfigInference> configInferences;
	final boolean PROPORTION_ZERO_EQUALS;
	final double porcentageValue;
	final String fileDegree;
	final Degree_Comparate degreeComp;
	final int[] customGens;
	final public String mode;
	public PropertiesTestC(String nameFileProperties) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(nameFileProperties);
		props.load(file);
		pathTest = (props.getProperty("pathTest") == null) ? 
				Paths.get(".").toAbsolutePath().normalize().toString()+"/": props.getProperty("pathTest");

		// network generation data
		sizeNetworks = Integer.parseInt(props.getProperty("sizeNetworks"));
		String[] st = props.getProperty("sizeTimes").split(",");
		sizeTimes = new int[st.length];
		for (int i = 0; i < st.length; i++) {
			sizeTimes[i] = Integer.parseInt(st[i].trim());
		}

		sizeGens = Integer.parseInt(props.getProperty("sizeGens"));
		sizeSamplesPerNetwork = Integer.parseInt(props.getProperty("sizeSamplesPerNetwork"));
		maxProbabilityPBN = Double.parseDouble(props.getProperty("maxProbabilityPBN"));
		String[] sd = props.getProperty("meanDegree").split(",");
		meanDegree = new double[sd.length];
		for (int i = 0; i < sd.length; i++) {
			meanDegree[i] = Double.parseDouble(sd[i].trim());
		}
		String[] parser = props.getProperty("Topology_Network").split(",");
		if (parser[0].equals("ALL"))
			topologies = Topology_Network.values();
		else {
			topologies = new Topology_Network[parser.length];
			for (int i = 0; i < parser.length; i++) {
				topologies[i] = Topology_Network.valueOf(parser[i]);
			}
		}
		// deg data
		String[] td = props.getProperty("Type_Dynamic").split(",");
		if (td[0].equals("ALL"))
			dynamics = Type_Dynamic.values();
		else {
			dynamics = new Type_Dynamic[td.length];
			for (int i = 0; i < td.length; i++) {
				dynamics[i] = Type_Dynamic.valueOf(td[i].trim());
			}
		}
		degWhitLoops = (props.getProperty("degWhitLoops") == null) ? false
				: Boolean.parseBoolean(props.getProperty("degWhitLoops"));
		porcentageValue = Double.parseDouble(props.getProperty("porcentageValue"));
		
		// inference data
		if (props.getProperty("groupingMethodologies") == null)
			groupingMethodologies = ID_QUALIFYING_FUNCTION.values();
		else {
			String[] gs = props.getProperty("groupingMethodologies").split(",");
			groupingMethodologies = new ID_QUALIFYING_FUNCTION[gs.length];
			for (int i = 0; i < gs.length; i++) {
				groupingMethodologies[i] = ID_QUALIFYING_FUNCTION.valueOf(gs[i].trim());
			}
		}
		
		if (props.getProperty("metrics") == null)
			metrics = ID_EVALUATION_METRIC.values();
		else {
			String[] gs = props.getProperty("metrics").split(",");
			metrics = new ID_EVALUATION_METRIC[gs.length];
			for (int i = 0; i < gs.length; i++) {
				metrics[i] = ID_EVALUATION_METRIC.valueOf(gs[i].trim());
			}
		}
		this.additionalMetric=ID_EVALUATION_METRIC.valueOf(props.getProperty("additionalMetric"));
		if(props.getProperty("customGens")!=null) {
			st = props.getProperty("customGens").split(",");
			int[] customsGens = new int[st.length];
			for (int i = 0; i < st.length; i++) {
				customsGens[i] = Integer.parseInt(st[i].trim());
			}
			this.customGens=customsGens;
		}else
			this.customGens=null;
		parser = ((props.getProperty("networks") == null) ? "0-0" : props.getProperty("networks")).split("-");
		minNetworks = Integer.parseInt(parser[0]);
		maxNetworks = Integer.parseInt(parser[1]);

		parser = ((props.getProperty("samples") == null) ? "0-0" : props.getProperty("samples")).split("-");
		minSamples = Integer.parseInt(parser[0]);
		maxSamples = Integer.parseInt(parser[1]);

		// searchAlgoritmic=

		Map<ID_SEARCH_ALGORITM, Object> mapSearch = null;
		if (props.getProperty("limitSearch") != null) {
			mapSearch = new HashMap<ConfigInference.ID_SEARCH_ALGORITM, Object>();
			int limitSearch = Integer.parseInt(props.getProperty("limitSearch"));
			mapSearch.put(ID_SEARCH_ALGORITM.SFS, limitSearch);
			mapSearch.put(ID_SEARCH_ALGORITM.SBS, limitSearch);
			mapSearch.put(ID_SEARCH_ALGORITM.NDES, limitSearch);
			mapSearch.put(ID_SEARCH_ALGORITM.IES, limitSearch);
			if (props.getProperty("limitChangeSearch") != null) {
				int changeSearch = Integer.parseInt(props.getProperty("limitChangeSearch"));
				mapSearch.put(ID_SEARCH_ALGORITM.IES_SFS, new int[] { changeSearch, limitSearch });
			}
			mapSearch.put(ID_SEARCH_ALGORITM.CS, limitSearch);
		}
		Map<Integer,ConfigInference> configInferences = new HashMap<>();
		for (int i = 0; i < sizeTimes.length; i++) 
			configInferences.put(sizeTimes[i], new ConfigInference(sizeTimes[i]-1, null, null, mapSearch));
		this.configInferences= Collections.unmodifiableMap(configInferences);
		
		searchAlgoritmic = ID_SEARCH_ALGORITM.valueOf(props.getProperty("searchAlgoritmic"));

		parser = (props.getProperty("fatorPenalizacaoNO") == null) ? new String[0]
				: props.getProperty("fatorPenalizacaoNO").split(",");
		fatorPenalizacaoNO = new double[parser.length];
		for (int i = 0; i < parser.length; i++) {
			fatorPenalizacaoNO[i] = Double.parseDouble(parser[i]);
		}

		parser = (props.getProperty("fatorPenalizacaoPO") == null) ? new String[0]
				: props.getProperty("fatorPenalizacaoPO").split(",");
		fatorPenalizacaoPO = new int[parser.length];
		for (int i = 0; i < parser.length; i++) {
			fatorPenalizacaoPO[i] = Integer.parseInt(parser[i]);
		}
		nThreads = (props.getProperty("nThreads") == null) ? 0 : Integer.parseInt(props.getProperty("nThreads"));
		// validate data
		filtroValidate = props.getProperty("filtroValidate");
		testDegreeValidate = (props.getProperty("testDegreeValidate") == null) ? 0
				: Integer.parseInt(props.getProperty("testDegreeValidate"));
		isOriginalNetwork = (props.getProperty("isOriginalNetwork") == null) ? false
				: Boolean.parseBoolean(props.getProperty("isOriginalNetwork"));
		typeTest = (props.getProperty("typeTest") == null) ? null : Type_Test.valueOf(props.getProperty("typeTest"));
		notPenalize = (Boolean.parseBoolean(props.getProperty("notPenalize"))) ? 1 : 0;
		VALIDATE_DINAMIC = (props.getProperty("VALIDATE_DINAMIC") == null) ? false
				: Boolean.parseBoolean(props.getProperty("VALIDATE_DINAMIC"));
		sizeDinamicTets = (props.getProperty("sizeDinamicTets") == null) ? 0
				: Integer.parseInt(props.getProperty("sizeDinamicTets"));
		TOPOLOGY = (props.getProperty("TOPOLOGY") == null) ? false
				: Boolean.parseBoolean(props.getProperty("TOPOLOGY"));
		TOPOLOGY_DEGREE = (props.getProperty("TOPOLOGY_DEGREE") == null) ? false
				: Boolean.parseBoolean(props.getProperty("TOPOLOGY_DEGREE"));
		PROPORTION_ZERO_EQUALS = (props.getProperty("PROPORTION_ZERO_EQUALS") == null) ? false
				: Boolean.parseBoolean(props.getProperty("PROPORTION_ZERO_EQUALS"));
		if(props.getProperty("degreesTopology") != null) {
			st = props.getProperty("degreesTopology").split(",");
			degreesTopology = new int[st.length];
			for (int i = 0; i < st.length; i++) {
				degreesTopology[i] = Integer.parseInt(st[i]);
			}
		}else
			degreesTopology = new int[0];
		nameFile = props.getProperty("nameFile");
		gabaritosDirectory = pathTest + "gabaritos/";
		inferenceDirectory = pathTest + "inference/";
		degDirectory = pathTest + "deg/";
		resumeDirectory = pathTest + "resume/";
		fileDegree=pathTest + ((props.getProperty("fileDegree")==null)?"":props.getProperty("fileDegree"));
		degreeComp=(props.getProperty("degreeComp")==null)?null:Degree_Comparate.valueOf(props.getProperty("degreeComp"));
		//mode inference
		mode=(props.getProperty("mode")==null)?"IN":props.getProperty("mode");
	}

}
