package tests.concurrent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import geneticNetwork.GeneticNetwork;
import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;
import geneticNetwork.GeneticNetwork.Type_Function;
import inference.ConfigInference;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.InferenceNetwork;
import inference.InferenceNetwork.TYPE_STEAPS;

public class TestRunInferenceC {
	public interface Task {
		void run() throws IOException;
	}

	public class NetworkTask implements Task {
		private InferenceNetwork inferenceNetwork;
		private GeneExpressionData deg;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private int[] sizePreditors;
		private Type_Function[] type_Functions;

		public NetworkTask(GeneExpressionData deg, String networkFile, ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function, ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric, ConfigInference configInference,
				int[] sizePreditors,
				Type_Function[] type_Functions) {
			this.deg = deg;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.sizePreditors=sizePreditors;
			this.type_Functions=type_Functions;
		}

		@Override
		public void run() throws IOException {
			this.inferenceNetwork = new InferenceNetwork(this.deg,
					this.networkFile,
					this.search_algoritm,
					this.evaluation_metric,
					this.additional_metric,
					this.qualifying_function,
					this.configInference
					,TYPE_STEAPS.NONE);
			List<Integer> validGens=this.deg.getValidGens();
			int nGens = validGens.size();
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTask(inferenceNetwork, i, 
				//		prefGene +" " + 
				String.valueOf(sizePreditors[validGens.get(i)])+
						" "+type_Functions[validGens.get(i)]));
			}
			System.out.println("In ->" + this.networkFile.substring(this.networkFile.lastIndexOf('/')));
		}
	}

	public class GeneTask implements Task {
		private InferenceNetwork inferenceNetwork;
		private int gene;
		private String prefGene;

		public GeneTask(InferenceNetwork inferenceNetwork, int gene, String prefGene) {
			super();
			this.inferenceNetwork = inferenceNetwork;
			this.gene = gene;
			this.prefGene = prefGene;
		}

		@Override
		public void run() throws IOException {
			try {
				inferenceNetwork.inferedGene(gene, prefGene);
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

	// public class ThreadInference implements Runnable {
	// InferenceTask inferenceTask;
	// int id;
	// private Resources resources=new Resources();
	// InferenceGeneticNetwork ig;
	// public ThreadInference(int id) {
	// this.id = id;
	// }
	//
	// @Override
	// public void run() {
	// while (!tasks.isEmpty()) {
	// synchronized (this) {
	// if (tasks.isEmpty())
	// return;
	// try {
	// inferenceTask = tasks.take();
	// ig = new InferenceGeneticNetwork(inferenceTask.qf, inferenceTask.sa);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// boolean[][] deg = null;
	// try {
	// deg = resources.readDeg(new File(prop.degDirectory, inferenceTask.degFile));
	// GeneticNetwork gn = ig.inferenceTopologyNetwork(deg);
	// String sn = inferenceTask.degFile.replaceAll(".deg", "");
	// System.out.println(inferenceTask.tasknumber+"\t"+ this.id + " " + sn + " " +
	// inferenceTask.sa.getShortName()+ " " + inferenceTask.qf.toShortString());
	// String fileName = sn + "__" + inferenceTask.sa.getShortName()+ "__" +
	// inferenceTask.qf.toShortString();
	// gn.writeInFile(new File(prop.inferenceDirectory, fileName.replace('.',
	// '_')));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// System.out.println("end thread " + id);
	// }
	// }

	public void run(String nameFileProperties) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		// int whitnot=((prop.whitNotPenalize)?0:1);
		// int nmetrics = ((whitnot==1)?1:1)+ prop.fatorPenalizacaoNO.length +
		// prop.fatorPenalizacaoPO.length;
		for (Topology_Network topology : prop.topologies) {
			// cuantas redes
			for (int in = prop.minNetworks; in <= prop.maxNetworks; in++) {
				// graus medios
				for (double degree : prop.meanDegree) {
					// cuantas dinamicas booleano o probabilistico
					for (Type_Dynamic dynamic : prop.dynamics) {
						for (int ntimes : prop.sizeTimes) {
							for (int isample = prop.minSamples; isample <= prop.maxSamples; isample++) {
								String nameNetwork = topology + "__" + in + "__" + prop.sizeGens + "__" + degree;
								GeneticNetwork gabaritoNetwork = GeneticNetwork
										.readFromFile(prop.gabaritosDirectory + nameNetwork + ".ylu");
								String nameSample =  nameNetwork+ "__"
										+ dynamic + "__" + ntimes + "__" + isample;
								GeneExpressionData deg=new BooleanGeneExpressionData(prop.degDirectory + nameSample,true);
								//deg.clusterForRepeat();
								//deg.setPorcentageValue(prop.porcentageValue);
								int[] sizePreditors= gabaritoNetwork.sizePredictors(deg.getValidGens());
								for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
									for (ID_EVALUATION_METRIC metric : prop.metrics) {
//										String prefGene = in + " " + isample + " " + ntimes + " " + qualifying_function
//												+ " " + metric;
//										String prefGene="";
										String namef = prop.inferenceDirectory + nameSample + "__"
												+ prop.searchAlgoritmic + "__" + qualifying_function + "__" + metric
												+ ".ylu";
										this.tasksNetwork.add(new NetworkTask(deg,
												namef,
												prop.searchAlgoritmic,
												qualifying_function,
												metric,
												prop.additionalMetric,
												prop.configInferences.get(ntimes),
												sizePreditors,
												gabaritoNetwork.getType_Functions()));
									}
									// QualifyingFunction qf =prop.getQualityFunctionById(idGrouping, imetric);
									// SearchAlgoritm sa =prop.searchAlgoritmic;
									// tasks.add(new InferenceTask(nameSample, qf, sa, tasks.size()));
								}
							}
						}
					}
				}
			}
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
		TestRunInferenceC tmi = new TestRunInferenceC();
		if (args.length > 0)
			tmi.run(args[0]);
		else
			tmi.run("jpGN.properties");
		// tmi.run(new File("deg", "RANDOM__0__2.0__BOOLEAN__20__0.deg"), new
		// WithoutGrouping(new MutualInformation()),
		// new SequentialBackwardSelection(5));
	}
}
