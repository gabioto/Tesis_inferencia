package tests.concurrent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneticNetwork;
import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;
import inference.NetworkInfered;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import javafx.util.Pair;
import resources.Resources;

public class TestResumenIC {
//	private Resources Resources=new Resources();
	PropertiesTestC prop;

	public enum Type_Test {
		DEGREE_GABARITO, TOPOLOGY, TOPOLOGY_DEGREE, TOPOLOGY_DEGREE_RANGE, DEGREE_GENE_INFERENCE, VALIDATE_DINAMIC
	}

	public TestResumenIC(String fileProp) {
		try {
			prop = new PropertiesTestC(fileProp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void degreesGabarito() throws IOException {
		PrintWriter fileDegree = new PrintWriter(
				prop.resumeDirectory + ((prop.nameFile != null) ? prop.nameFile : "degreeGabarito.csv"));
		// para las topologias livre y random
		StringBuilder sb = new StringBuilder("TOPOLOGY Sample IdealDegree RealDegree");
		for (int it = 0; it < prop.topologies.length; it++) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					// cuantas dinamicas booleano o probabilistico
					String nameNetwork = prop.topologies[it].toString() + "__" + in + "__" + prop.meanDegree[iDegree];
					GeneticNetwork gn = GeneticNetwork.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
					sb.append("\n" + prop.topologies[it].toString() + " " + in + " " + prop.meanDegree[iDegree] + " "
							+ gn.getMeanDegree());
				}
			}
		}
		fileDegree.print(sb.toString());
		fileDegree.close();
	}

	private String validateTopologyValues(GeneticNetwork gabaritoNetwork, 
			NetworkInfered networkInfered,
			List<Integer> validGens) {
		StringBuilder sb = new StringBuilder(" ");
		sb.append(networkInfered.getMeanDegree());
		int[] FPFNTNTP = GeneticNetwork.validateFPFNTPTN(gabaritoNetwork, networkInfered, validGens);
		for (int i : FPFNTNTP) {
			sb.append(" " + i);
		}
		return sb.toString();
	}

	private String validateTopologyValuesDegree(GeneticNetwork gabaritoNetwork,
			NetworkInfered networkInfered,
			List<Integer> validGens) {
		StringBuilder sb = new StringBuilder();
		int[] degrees = prop.degreesTopology;
		for (int degree : degrees) {
			int[] FPFNTNTP = GeneticNetwork.validateFPFNTPTN(gabaritoNetwork, networkInfered, degree,
					prop.filtroValidate, prop.isOriginalNetwork,
					validGens);
			if(FPFNTNTP==null)
				sb.append("    ");
			else
			for (int i : FPFNTNTP) {
				sb.append(" ").append(i);
			}
		}
		return sb.toString();
	}

	private String validateTopologyValuesProp(GeneticNetwork gabaritoNetwork, NetworkInfered networkInfered,
			int[] genesInc) {
		StringBuilder sb = new StringBuilder();
		int[] FPFNTNTP = NetworkInfered.validateFPFNTPTN(gabaritoNetwork, networkInfered, genesInc);
		for (int i : FPFNTNTP) {
			sb.append(" ").append(i);
		}
		return sb.toString();
	}

	private String lineValidateDinamicNetwork(GeneticNetwork gabaritoNetwork,
			NetworkInfered networkInfered,
			int[] labelsGroupGens,
			List<Integer> validGens) {
		int sizeTest = prop.sizeDinamicTets;
		int sizeGenes = gabaritoNetwork.getPredictors().length;
		BooleanGeneExpressionData gedTest = BooleanGeneExpressionData.generateRandomStates(sizeGenes, sizeTest);
		BooleanGeneExpressionData gedGabarito = gabaritoNetwork.makeGeneExpresionData(Type_Dynamic.BOOLEAN, gedTest);
		int[] degrees = prop.degreesTopology;
		Pair<BooleanGeneExpressionData, double[][]> degInference = networkInfered.makeProportionGeneExpresionState(gedTest,
				labelsGroupGens, degrees);
		BooleanGeneExpressionData booleanGeneExpressionData = degInference.getKey();
		double[][] probabilisticValues = degInference.getValue();
		StringBuilder sb = new StringBuilder();
//		Resources.divMatrix(probabilisticValues,sizeTest);
		Map<Integer, Double> mapDistances = gedGabarito.distHaming(booleanGeneExpressionData,
				labelsGroupGens,
				validGens);
		for (int i = 0; i < degrees.length; i++) {
			Double distances = mapDistances.get(degrees[i]);
			if (distances == null || Double.isNaN(distances))
				sb.append("   ");
			else
				sb.append(" ").append(distances).append(" ").append(probabilisticValues[i][0]).append(" ")
						.append(probabilisticValues[i][1]);
		}
		int i = probabilisticValues.length - 1;
		sb.append(" ").append(mapDistances.get(-1)).append(" ").append(probabilisticValues[i][0]).append(" ")
				.append(probabilisticValues[i][1]);
		return sb.toString();
	}

	private String lineProportionGuessValues(GeneticNetwork gabaritoNetwork, NetworkInfered networkInfered) {
		int sizeTest = prop.sizeDinamicTets;
		int sizeGenes = gabaritoNetwork.getPredictors().length;
		BooleanGeneExpressionData gedTest = BooleanGeneExpressionData.generateRandomStates(sizeGenes, sizeTest);
		BooleanGeneExpressionData gedGabarito = gabaritoNetwork.makeGeneExpresionData(Type_Dynamic.BOOLEAN, gedTest);
		int[][] countsAcertos = { { 0, 0 }, { 0, 0 } };
		Pair<BooleanGeneExpressionData, Boolean[][]> gedInferedStates = networkInfered.makeGeneExpresionState(gedTest);
		BooleanGeneExpressionData gedInfered = gedInferedStates.getKey();
		Boolean[][] states = gedInferedStates.getValue();
		for (int i = 0; i < sizeTest; i++) {
			for (int j = 0; j < sizeGenes; j++) {
				if (states[i][j] != null) {
					countsAcertos[0][1]++;
					if (gedGabarito.getData(i, j) == gedInfered.getData(i, j))
						countsAcertos[0][0]++;
					if (states[i][j] == true) {
						countsAcertos[1][1]++;
						if (gedGabarito.getData(i, j) == gedInfered.getData(i, j))
							countsAcertos[1][0]++;
					}
				}
			}
		}
		return String.valueOf((countsAcertos[0][1] > 0) ? (double) countsAcertos[0][0] / countsAcertos[0][1] : "") + " "
				+ String.valueOf((countsAcertos[1][1] > 0) ? (double) countsAcertos[1][0] / countsAcertos[1][1] : "");
	}

//private String lineValidateDinamicNetwork(GeneticNetwork gabaritoNetwork, NetworkInfered2 networkInfered) {
//		int sizeTest=prop.sizeDinamicTets;
//		int sizeGenes = gabaritoNetwork.getPredictors().length;
//		boolean[][] samples = samplesTestDinamyc(sizeGenes, sizeTest);
//		boolean[][] degGabarito = new boolean[sizeTest][];
//		boolean[][] degInfered = new boolean[sizeTest][sizeGenes];
//		int[] degrees=prop.degreesTopology;
//		int[] sizePredictors=gabaritoNetwork.sizePredictors();
//		double[][] probabilisticValues= new double[degrees.length+1][2];
//		for (int i = 0; i < sizeTest; i++) {
//			degGabarito[i] = gabaritoNetwork.makeGeneExpresionState(Type_Dynamic.BOOLEAN, samples[i]);
//			Resources.mergeAddMatrix(probabilisticValues,networkInfered.makeGeneExpresionState(samples[i],degInfered[i], sizePredictors),degrees);
//		}
//		//double[] distances=new double[degrees.length];
//		StringBuilder sb=new StringBuilder();
//		Resources.divMatrix(probabilisticValues,sizeTest);
//		for (int i = 0; i < degrees.length; i++) {
//			double distances=Resources.distHaming(degGabarito, degInfered, sizePredictors, degrees[i]);
//			if(Double.isNaN(distances))
//				sb.append("   ");
//			else
//				sb.append(" ").append(distances).append(" ").append(probabilisticValues[i][0]).append(" ").append(probabilisticValues[i][1]);
//		}
//		int i=probabilisticValues.length-1;
//		sb.append(" ").append(Resources.distHaming(degGabarito, degInfered)).append(" ").append(probabilisticValues[i][0]).append(" ").append(probabilisticValues[i][1]);
//		return sb.toString();
//	}
//	
	public void countCanalize() throws IOException {
		// final int nmetrics = 1 + prop.fatorPenalizacaoNO.length +
		// prop.fatorPenalizacaoPO.length;
		StringBuilder sb = new StringBuilder("TOPOLOGY Sample IdealDegree Dynamic Times Grouping Penalization ");
		sb.append(IntStream.range(0, prop.sizeGens).mapToObj(String::valueOf).collect(Collectors.joining("G ")))
				.append("G");
		for (int it = 0; it < prop.topologies.length; it++) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					String nameNetwork = prop.topologies[it].toString() + "__" + in + "__" + prop.sizeGens + "__"
							+ prop.meanDegree[iDegree];
					GeneticNetwork gabaritoNetwork = GeneticNetwork
							.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
					sb.append("\n" + prop.topologies[it].toString() + " " + in + " " + prop.meanDegree[iDegree] + " - "
							+ "- GAB - ")
							.append(Arrays.stream(gabaritoNetwork.sizeCanalizeGenes()).mapToObj(Integer::toString)
									.collect(Collectors.joining(" ")));
					// cuantas dinamicas booleano o probabilistico
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
								for (ID_EVALUATION_METRIC metric : prop.metrics) {
									int[] countCanalize = new int[prop.sizeGens];
									for (int isample = 0; isample < prop.sizeSamplesPerNetwork; isample++) {
										String nameDeg = nameNetwork + "__" + prop.dynamics[id] + "__"
												+ prop.sizeTimes[isize] + "__" + isample;
										String nameInferedNetwork = nameDeg + "__" + prop.searchAlgoritmic + "__"
												+ qualifying_function + "__" + metric;
										NetworkInfered networkInfered = NetworkInfered
												.readFromFile(prop.inferenceDirectory + nameInferedNetwork);
										Resources.mergeAddMatrix(countCanalize, networkInfered.sizeCanalizeGenes());
									}
									sb.append("\n" + prop.topologies[it].toString() + " " + in + " "
											+ prop.meanDegree[iDegree] + " " + prop.dynamics[id] + " "
											+ prop.sizeTimes[isize] + " " + qualifying_function + " " + metric + " ")
											.append(Arrays
													.stream(Resources.divArray(countCanalize,
															prop.sizeSamplesPerNetwork))
													.mapToObj(Double::toString).collect(Collectors.joining(" ")));
								}
							}
						}
					}
				}
			}
		}
		System.out.println(sb);
	}

	public void validateDinamicNetwork() throws IOException {
		// final int nmetrics = 1 + prop.fatorPenalizacaoNO.length +
		// prop.fatorPenalizacaoPO.length;
		PrintWriter fileDegree = new PrintWriter(
				prop.resumeDirectory + ((prop.nameFile != null) ? prop.nameFile : prop.searchAlgoritmic + "_vd.csv"));
		// para las topologias livre y random
		StringBuilder sb = new StringBuilder(
				"TOPOLOGY Sample IdealDegree Dynamic Times SampleDeg Grouping Penalization");
		if (prop.TOPOLOGY)
			sb.append(" RealDegree FP FN TP TN");
		if (prop.TOPOLOGY_DEGREE) {
			int[] degrees = prop.degreesTopology;
			for (int degree : degrees) {
				sb.append(" FP").append(degree).append(" FN").append(degree).append(" TP").append(degree).append(" TN")
						.append(degree);
			}
			sb.append(" FP").append("p").append(" FN").append("p").append(" TP").append("p").append(" TN").append("p");
		}

		if (prop.VALIDATE_DINAMIC) {
			int[] degrees = prop.degreesTopology;
			for (int degree : degrees) {
				sb.append(" Dim").append(degree).append(" DiPvZ").append(degree).append(" DiPvE").append(degree);
			}
			sb.append(" Dim DiPvZ DiPvE PropE PropZ");
		}
		if(prop.PROPORTION_ZERO_EQUALS)
			sb.append(" PropEFun PropZFun");
		for (int it = 0; it < prop.topologies.length; it++) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					String nameNetwork = prop.topologies[it].toString() + "__" + in + "__" + prop.sizeGens + "__"
							+ prop.meanDegree[iDegree];
					GeneticNetwork gabaritoNetwork = GeneticNetwork
							.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
					// cuantas dinamicas booleano o probabilistico
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							for (int isample = 0; isample < prop.sizeSamplesPerNetwork; isample++) {
								for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
									for (ID_EVALUATION_METRIC metric : prop.metrics) {
										String nameDeg = nameNetwork + "__" + prop.dynamics[id] + "__"
												+ prop.sizeTimes[isize] + "__" + isample;
										BooleanGeneExpressionData ged=new BooleanGeneExpressionData(prop.degDirectory + nameDeg);
										ged.setPorcentageValue(prop.porcentageValue);
									//	int[] genesInc = gabaritoNetwork.predictorsVariacao(0.4,ged);
										String nameInferedNetwork = nameDeg + "__" + prop.searchAlgoritmic + "__"
												+ qualifying_function + "__" + metric;
										NetworkInfered networkInfered = NetworkInfered
												.readFromFile(prop.inferenceDirectory + nameInferedNetwork);
										sb.append("\n" + prop.topologies[it].toString() + " " + in + " "
												+ prop.meanDegree[iDegree] + " " + prop.dynamics[id] + " "
												+ prop.sizeTimes[isize] + " " + isample + " " + qualifying_function
												+ " " + metric);
										List<Integer> vg= networkInfered.getIntegerGens();
										if (prop.TOPOLOGY)
											sb.append(validateTopologyValues(gabaritoNetwork, networkInfered,vg));
										if (prop.TOPOLOGY_DEGREE) {
											sb.append(validateTopologyValuesDegree(gabaritoNetwork, networkInfered, vg));
										if(prop.PROPORTION_ZERO_EQUALS)	
											sb.append(validateTopologyValuesProp(gabaritoNetwork, networkInfered,
													new int[prop.sizeGens]));
										}
										if (prop.VALIDATE_DINAMIC)
											sb.append(lineValidateDinamicNetwork(gabaritoNetwork, networkInfered,
													gabaritoNetwork.getDegrees(ged.getValidGens()),vg))
											   .append(" ")
												.append(lineProportionGuessValues(gabaritoNetwork, networkInfered));
										if(prop.PROPORTION_ZERO_EQUALS)
											sb.append(" ").append(networkInfered.proportionEquals())
											.append(" ").append(networkInfered.proportionZeros());
									}
								}
							}
						}
					}
				}
			}
		}
		fileDegree.print(sb.toString());
		fileDegree.close();
		System.out.println("end.vd");
	}

	public void degreesGeneInference() throws IOException {
		StringBuilder sb = new StringBuilder("network deg Pen iGene GB EM degreeIM FC");
//		for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies)
//			sb.append(" ").append(qualifying_function).append(" ").append(qualifying_function).append("_fc");
		final String header = sb.toString();
		// para las topologias livre y random
		for (Topology_Network topology : prop.topologies) {
			// cuantas dinamicas booleano o probabilistico
			for (Type_Dynamic dynamic : prop.dynamics) {
				// graus medios
				for (double degree : prop.meanDegree) {
					for (int times : prop.sizeTimes) {
						String nameFile = prop.searchAlgoritmic + "_dgi__" + topology + "__" + dynamic + "__"
								+ Double.toString(degree).replace(".", "_") + "__" + times + ".csv";
						PrintWriter fileDegree = new PrintWriter(prop.resumeDirectory + nameFile);
						sb = new StringBuilder(header);
						// cuantas redes
						for (int in = 0; in < prop.sizeNetworks; in++) {
							final String nameNetwork = topology.toString() + "__" + in + "__" + prop.sizeGens + "__"
									+ degree;
							//NetworkInfered2[] networks = new NetworkInfered2[prop.groupingMethodologies.length];
							GeneticNetwork gabarito = GeneticNetwork
									.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
							for (ID_EVALUATION_METRIC metric : prop.metrics) {
								for (int isample = 0; isample < prop.sizeSamplesPerNetwork; isample++) {
									String linha = "\n" + in + " " + isample + " " + metric;
//									int iGrouping = 0;
									for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
										String nameInferedNetwork = nameNetwork + "__" + dynamic + "__" + times + "__"
												+ isample + "__" + prop.searchAlgoritmic + "__" + qualifying_function
												+ "__" + metric;
										NetworkInfered networkInfered = NetworkInfered
												.readFromFile(prop.inferenceDirectory + nameInferedNetwork + ".ylu");
//										networks[iGrouping] = NetworkInfered2
//												.readFromFile(prop.inferenceDirectory + nameInferedNetwork + ".ylu");
//										iGrouping++;

//									int nGenes = networks[0].getGens().length;
										int nGenes = networkInfered.getGens().length;
										for (int iGene = 0; iGene < nGenes; iGene++) {
											sb.append(linha + " " + iGene).append(" ")
													.append(gabarito.getPredictors()[iGene].size()).append(" ")
													.append(qualifying_function);
//										for (int iNetwork = 0; iNetwork < networks.length; iNetwork++) {
											sb.append(" ")
//													.append(networks[iNetwork].getGens()[iGene].getPredictors().size())
//													.append(" ").append(networks[iNetwork].getGens()[iGene]
													.append(networkInfered.getGens()[iGene].getPredictors().size())
													.append(" ").append(networkInfered.getGens()[iGene]
															.getCritery_function_values());
										}
									}

								}
							}
						}
						fileDegree.print(sb);
						fileDegree.close();
						System.out.println("fc");
					}
				}
			}
		}
	}

	public void run() throws IOException {
//		switch (prop.typeTest) {
//		case DEGREE_GABARITO:
//			degreesGabarito();
//			break;
//		case DEGREE_GENE_INFERENCE:
//			degreesGeneInference();
//			break;
//		case DEGREE_INFERENCE:
//			validateTopology();
//			break;
//		case VALIDATE_DEGREE:
//			validateTopologyDegree(prop.testDegreeValidate);
//			break;
//		case VALIDATE_DINAMIC:
//			validateDinamicNetwork(1000);
//			break;
//		default:
//			break;
//
//		}
		degreesGeneInference();
		validateDinamicNetwork();
	}

	public static void main(String[] args) throws IOException {
		String fileProp = "";
		if (args.length > 0)
			fileProp = args[0];
		else
			fileProp = "jpGN.properties";
		TestResumenIC tr = new TestResumenIC(fileProp);
		tr.run();
		// tr.countCanalize();
	}
}
