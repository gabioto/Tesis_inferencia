package tests.concurrent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import geneticNetwork.*;
import inference.ConfigInference;
import inference.InferenceNetwork;
import tests.concurrent.PropertiesTestC.Degree_Comparate;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;

public class TestRunInferenceCluster {
	public interface Task {
		void run() throws IOException;
	}

	public class NetworkTask implements Task {
		private InferenceNetwork inferenceNetwork;
		private BooleanGeneExpressionData deg;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private int[] idealDegrees;
		private Degree_Comparate comparate;

		public NetworkTask(BooleanGeneExpressionData deg,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference,
				int[] idealDegrees,
				Degree_Comparate comparate) {
			this.deg = deg;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.idealDegrees = idealDegrees;
			this.comparate = comparate;
		}

		@Override
		public void run() throws IOException {
			this.inferenceNetwork = new InferenceNetwork(this.deg,
					this.networkFile,
					this.search_algoritm,
					this.evaluation_metric,
					this.additional_metric,
					this.qualifying_function,
					this.configInference);
			List<Integer> validGens = this.deg.getValidGens();
			int nGens = validGens.size();
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTask(inferenceNetwork,
						i,
						idealDegrees[validGens.get(i)],
						comparate));
			}
			System.out.println("In"+comparate+" ->" + this.networkFile.substring(this.networkFile.lastIndexOf('/')));
		}
	}

	public class GeneTask implements Task {
		private InferenceNetwork inferenceNetwork;
		private int gene;
		private int idealDegree;
		private Degree_Comparate comparate;

		public GeneTask(InferenceNetwork inferenceNetwork, int gene, int idealDegree, Degree_Comparate comparate) {
			this.inferenceNetwork = inferenceNetwork;
			this.gene = gene;
			this.idealDegree = idealDegree;
			this.comparate = comparate;
		}



		@Override
		public void run() throws IOException {
			try {
				switch (comparate) {
				case I:inferenceNetwork.reInferedGene(gene, idealDegree);
					break;
				case M:inferenceNetwork.reInferedGeneMenor(gene, idealDegree);
					default:
					break;
				}
				
				// inferedGene(gene, prefGene);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public class ThreadInference implements Runnable {
		Task task;
		int id;

		public ThreadInference(int id) {
			super();
			this.id = id;
		}

		@Override
		public void run() {
			Task task = null;
			while (true) {
				synchronized (tasksNetwork) {
					try {
						if (tasksGene.isEmpty()) {
							if (tasksNetwork.isEmpty()) {
								System.out.println("end " + this.id);
								return;
							} else
								tasksNetwork.take().run();
						}
						task = tasksGene.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					task.run();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public LinkedBlockingQueue<NetworkTask> tasksNetwork;
	public LinkedBlockingQueue<GeneTask> tasksGene;

	public void run(String nameFileProperties) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BufferedReader br = Files.newBufferedReader(Paths.get(prop.fileDegree));
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			String nameNetwork = prop.topologies[0] + "__" + values[0] + "__" + prop.sizeGens + "__"
					+ prop.meanDegree[0];
	//		GeneticNetwork gabaritoNetwork = GeneticNetwork
	//					.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
			String nameSample = nameNetwork + "__" + prop.dynamics[0] + "__" + values[1] + "__" + values[2];
			BooleanGeneExpressionData deg = new BooleanGeneExpressionData(prop.degDirectory + nameSample);
			deg.setPorcentageValue(prop.porcentageValue);

			int[] clustersDegree = new int[prop.sizeGens];
			ID_QUALIFYING_FUNCTION qualifying_function = 
					ID_QUALIFYING_FUNCTION.valueOf(values[3]);
			ID_EVALUATION_METRIC metric = prop.metrics[0];
//										String prefGene = in + " " + isample + " " + ntimes + " " + qualifying_function
//												+ " " + metric;
			String namef = prop.inferenceDirectory + nameSample + "__" + prop.searchAlgoritmic + "__"
					+ qualifying_function + "__" + metric + ".ylu";
//			clustersDegree[Integer.parseInt(values[5])] = Integer.parseInt(values[values.length - 1]);
			clustersDegree[Integer.parseInt(values[6])] = Integer.parseInt(values[values.length - 1]);
			for (int i = 1; i < prop.sizeGens; i++) {
				values = br.readLine().split(",");
				clustersDegree[Integer.parseInt(values[6])] = Integer.parseInt(values[values.length - 1]);
//				clustersDegree[Integer.parseInt(values[5])] = Integer.parseInt(values[values.length - 1]);
			}
			this.tasksNetwork.add(new NetworkTask(deg, namef, prop.searchAlgoritmic, qualifying_function, metric,
					prop.additionalMetric,prop.configInferences.get(deg.sizeData), clustersDegree, prop.degreeComp));
		}
		// System.out.println(tasks.size() + " tests");
		// rodar hilos 
		for (int i = 0; i < prop.nThreads; i++) {
			(new Thread(new ThreadInference(i))).start();
		}
		// while (nThreads>endThreads) {
		// wait();
		// }
		System.out.println("fin");
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		TestRunInferenceCluster tmi = new TestRunInferenceCluster();
		if (args.length > 0)
			tmi.run(args[0]);
		else
			tmi.run("jpGN.properties");
		// tmi.run(new File("deg", "RANDOM__0__2.0__BOOLEAN__20__0.deg"), new
		// WithoutGrouping(new MutualInformation()),
		// new SequentialBackwardSelection(5));
	}
}
