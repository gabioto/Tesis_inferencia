package tests.concurrent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneticNetwork;
import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;

public class TestMainInference {
//	private Resources Resources=new Resources();
	PropertiesTestC prop;

	public BooleanGeneExpressionData[] buidDEG(GeneticNetwork gn, int sizeSamplesDeg, Type_Dynamic dynamic) {
		BooleanGeneExpressionData[] degs = new BooleanGeneExpressionData[prop.sizeSamplesPerNetwork];
		for (int iSample = 0; iSample < prop.sizeSamplesPerNetwork; iSample++) {
			BooleanGeneExpressionData deg = null;
			int trys = 0;
			ArrayList<ArrayList<Integer>> conts=new ArrayList<>();
			while (deg == null) {
				boolean[] initState = null;
				while (!verifInitState(degs, iSample, initState = BooleanGeneExpressionData.createRandonState(prop.sizeGens)))
					;
				deg = gn.createGeneExpressionData(sizeSamplesDeg, 1, dynamic, initState, prop.degWhitLoops);
				if(deg==null) {
					trys++; continue;
				}
				ArrayList<Integer> c=deg.constantsGens(prop.porcentageValue);
				if(!c.isEmpty())
				{
					deg=null;
					conts.add(c);
				}
				if (trys++ > 10000) {
					return null;
				}
			}
			degs[iSample] = deg;
		}
		return degs;
	}

	private boolean verifInitState(BooleanGeneExpressionData[] degs, int pos, boolean[] initState) {
		for (int iDeg = 0; iDeg < pos; iDeg++) {
			if (Arrays.equals(degs[iDeg].getCopyData(0), initState))
				return false;
		}
		return true;
	}

	public void buildDEGFromGeneticNetwork() throws Exception {
		// para las topologias livre y random
		for (Topology_Network topology : prop.topologies) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					String nameNetwork = topology + "__" + in + "__" + prop.sizeGens + "__" + prop.meanDegree[iDegree];
					GeneticNetwork gn = GeneticNetwork.readFromFile(prop.gabaritosDirectory + nameNetwork);
					// cuantas dinamicas booleano o probabilistico
					BooleanGeneExpressionData[][][] degsTimes = new BooleanGeneExpressionData[prop.dynamics.length][prop.sizeTimes.length][];
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							BooleanGeneExpressionData[] degs = buidDEG(gn, prop.sizeTimes[isize], prop.dynamics[id]);
							if (degs != null)
								degsTimes[id][isize] = degs;
							else {
								throw new Exception("Network can't build DEG " + nameNetwork);
							}
						}
					}
					gn.writeInFile(new File(prop.gabaritosDirectory, nameNetwork));
				}
			}
		}
	}

	public void run(String nameFileProperties) throws IOException {
		prop = new PropertiesTestC(nameFileProperties);
		Files.createDirectory(Paths.get(prop.gabaritosDirectory));
		Files.createDirectory(Paths.get(prop.inferenceDirectory));
		Files.createDirectory(Paths.get(prop.degDirectory));
		// para las topologias livre y random
		lab_topologies: for (Topology_Network topology : prop.topologies) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				lab_degree: for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					GeneticNetwork gn = GeneticNetwork.buildNetwork(prop.sizeGens, 2, prop.meanDegree[iDegree],
							prop.maxProbabilityPBN, topology);
					if (gn == null)
						continue lab_topologies;
					// cuantas dinamicas booleano o probabilistico
					BooleanGeneExpressionData[][][] degsTimes = new BooleanGeneExpressionData[prop.dynamics.length][prop.sizeTimes.length][];
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							BooleanGeneExpressionData[] degs = buidDEG(gn, prop.sizeTimes[isize], prop.dynamics[id]);
							if (degs != null)
								degsTimes[id][isize] = degs;
							else {
								iDegree--;
								continue lab_degree;
							}
						}
					}
					String nameNetwork = topology + "__" + in + "__" + prop.sizeGens + "__" + prop.meanDegree[iDegree];
					gn.writeInFile(new File(prop.gabaritosDirectory, nameNetwork));
					System.out.println(nameNetwork);
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							for (int isample = 0; isample < prop.sizeSamplesPerNetwork; isample++) {
								String nameSample = nameNetwork + "__" + prop.dynamics[id] + "__"
										+ prop.sizeTimes[isize] + "__" + isample;
								
								degsTimes[id][isize][isample].saveDeg(new File(prop.degDirectory, nameSample));
								System.out.println(nameSample);
							}
						}
					}
				}
			}
		}
	}
	public void run2(String nameFileProperties) throws IOException {
		prop = new PropertiesTestC(nameFileProperties);
		Files.createDirectory(Paths.get(prop.gabaritosDirectory));
		Files.createDirectory(Paths.get(prop.inferenceDirectory));
		Files.createDirectory(Paths.get(prop.degDirectory));
		// para las topologias livre y random
		lab_topologies: for (Topology_Network topology : prop.topologies) {
			// cuantas redes
			for (int in = 0; in < prop.sizeNetworks; in++) {
				// graus medios
				lab_degree: for (int iDegree = 0; iDegree < prop.meanDegree.length; iDegree++) {
					String nameNetwork = topology + "__" + in + "__" + prop.sizeGens + "__" + prop.meanDegree[iDegree];
					GeneticNetwork gn =GeneticNetwork.readFromFile(new File(prop.gabaritosDirectory, nameNetwork));
					if (gn == null)
						continue lab_topologies;
					// cuantas dinamicas booleano o probabilistico
					BooleanGeneExpressionData[][][] degsTimes = new BooleanGeneExpressionData[prop.dynamics.length][prop.sizeTimes.length][];
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							BooleanGeneExpressionData[] degs = buidDEG(gn, prop.sizeTimes[isize], prop.dynamics[id]);
							if (degs != null)
								degsTimes[id][isize] = degs;
							else {
								iDegree--;
								continue lab_degree;
							}
						}
					}
					
					System.out.println(nameNetwork);
					for (int id = 0; id < prop.dynamics.length; id++) {
						for (int isize = 0; isize < prop.sizeTimes.length; isize++) {
							for (int isample = 0; isample < prop.sizeSamplesPerNetwork; isample++) {
								String nameSample = nameNetwork + "__" + prop.dynamics[id] + "__"
										+ prop.sizeTimes[isize] + "__" + isample;
								
								degsTimes[id][isize][isample].saveDeg(new File(prop.degDirectory, nameSample));
								System.out.println(nameSample);
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		TestMainInference tmi = new TestMainInference();
		tmi.run("jpGN.properties");
	}
}
