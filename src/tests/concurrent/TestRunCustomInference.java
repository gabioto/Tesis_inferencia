package tests.concurrent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.DiscreteGeneExpressionData;
//import geneticNetwork.DiscreteGeneExpressionData;
import geneticNetwork.GeneExpressionData;
import inference.*;
import inference.ConfigInference.*;
import inference.InferenceNetwork.TYPE_STEAPS;
import tests.concurrent.PropertiesTestC.Degree_Comparate;

public class TestRunCustomInference {
	public String mode;
	public interface Task {
		void run() throws IOException;
	}

	public class NetworkTask implements Task {
		private CustomGenesInference customGenesInference;
		private int[] customGens;
		private GeneExpressionData deg;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		
		public NetworkTask(int[] customGens,
				BooleanGeneExpressionData deg,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference) {
			
			this.customGens=(customGens==null)?
					deg.getValidArrayGens():customGens;
			this.deg = deg;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
		}

		@Override
		public void run() throws IOException {
			this.customGenesInference = 
					new CustomGenesInference(this.customGens,
							this.deg,
							this.networkFile,
							this.search_algoritm,
							this.evaluation_metric,
							this.additional_metric,
							this.qualifying_function,
							this.configInference
					,TYPE_STEAPS.ALL);
			int nGens = customGens.length;
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTaskInference(customGenesInference, i,""+this.qualifying_function));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}

	public class NetworkTaskRe implements Task {
		private CustomGenesInference customGenesInference;
		private int[] customGens;
		
		private Map<String, Integer> pesos;
		private BooleanGeneExpressionData deg;
		private DiscreteGeneExpressionData dged;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private Degree_Comparate degreeComp;
		
		public NetworkTaskRe(int[] customGens,Map<String, Integer> pesos,
				BooleanGeneExpressionData deg,
				DiscreteGeneExpressionData dged,
				String networkFile, ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function, ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference, Degree_Comparate degreeComp) {
			this.customGens=(customGens==null)?
					IntStream.range(0, deg.sizeGens).toArray():customGens;
			this.pesos=pesos;
			this.deg = deg;
			this.dged=dged;
			this.networkFile =(degreeComp==Degree_Comparate.M)? networkFile.replace(".ylu", "_e"):networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.degreeComp=degreeComp;
		}

		@Override
		public void run() throws IOException {
			this.customGenesInference = new CustomGenesInference(
					this.customGens,
					this.deg,
					this.dged,
					this.networkFile,
					this.search_algoritm, 
					this.evaluation_metric,
					this.additional_metric,
					this.qualifying_function,
					this.configInference
					,TYPE_STEAPS.NONE,
					false);
			int nGens = customGens.length;
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(
						new GeneTaskReInference(
								this.customGenesInference
								,this.customGens[i]
								,(pesos==null)?-1:
									pesos.get(
										this.qualifying_function.toString()+
										((BooleanGeneExpressionData)this.deg)
										.getIdCluster(this.customGens[i]))
								,dged
								,this.degreeComp));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}

	public class NetworkTaskEv implements Task {
		private CustomGenesInference customGenesInference;
		private GeneExpressionData deg;
		private String networkFile;
		private ConfigInference configInference;
		private ID_EVALUATION_METRIC additional_metric;
		
		
		public NetworkTaskEv(GeneExpressionData deg, String networkFile,
				ConfigInference configInference,
				ID_EVALUATION_METRIC additional_metric) {
			this.deg = deg;
			this.networkFile = networkFile;
			this.configInference = configInference;
			this.additional_metric=additional_metric;
		}

		@Override
		public void run() throws IOException {
			PreInferedNetwork preInferedNetwork = PreInferedNetwork.readFromFile(this.networkFile);
			this.customGenesInference = new CustomGenesInference(
					this.deg,
					this.networkFile,
					preInferedNetwork,
					this.configInference,
					this.additional_metric,
					TYPE_STEAPS.ALL);
			int nGens =preInferedNetwork.countAllgens();
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTaskEvInference(customGenesInference,
						i,
						preInferedNetwork.getQualifyingFunction()+""));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}

	public class GeneTaskInference implements Task {
		private CustomGenesInference customGenesInference;
		private int iGene;
		private String prefGene;

		public GeneTaskInference(CustomGenesInference customGenesInference, int iGene, String prefGene) {
			super();
			this.customGenesInference = customGenesInference;
			this.iGene = iGene;
			this.prefGene = prefGene;
		}

		@Override
		public void run() throws IOException{
			try {
				customGenesInference.inferedGenebyIndex(iGene, prefGene);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class GeneTaskReInference implements Task {
		private CustomGenesInference customGenesInference;
		private int idGene;
		private int fixedDegree;
		private Degree_Comparate degreeComp;

		public GeneTaskReInference(CustomGenesInference customGenesInference,
				int idGene,
				int fixedDegree,
				DiscreteGeneExpressionData gdd,
				Degree_Comparate degreeComp) {
			super();
			this.customGenesInference = customGenesInference;
			this.idGene = idGene;
			this.fixedDegree = fixedDegree;
			this.degreeComp=degreeComp;
		}
//		public GeneTaskReInference(CustomGenesInference customGenesInference,
//				int idGene,
//				int fixedDegree,
//				Degree_Comparate degreeComp) {
//			super();
//			this.customGenesInference = customGenesInference;
//			this.idGene = idGene;
//			this.fixedDegree = fixedDegree;
//			this.degreeComp=degreeComp;
//		}
		
		@Override
		public void run() throws IOException{
			try {
				switch (this.degreeComp) {
				case I:
					customGenesInference.reInferedGeneById(idGene, fixedDegree);
					break;
				case M:
					customGenesInference.reInferedGeneMenorById(idGene, fixedDegree);
					break;
				case T:
					customGenesInference.reTiesFromDiscreteById(idGene);
					break;
				default:
					break;
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public class GeneTaskEvInference implements Task {
		private CustomGenesInference customGenesInference;
		private int iGene;
		private String prefGene;
		public GeneTaskEvInference(CustomGenesInference customGenesInference, int iGene, String prefGene) {
			super();
			this.customGenesInference = customGenesInference;
			this.iGene = iGene;
			this.prefGene = prefGene;
		}

		@Override
		public void run() throws IOException{
			try {
				customGenesInference.inferedEvGenebyIndex(iGene, prefGene);
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

	public LinkedBlockingQueue<Task> tasksNetwork;
	public LinkedBlockingQueue<Task> tasksGene;

	public void runCut(String nameFileProperties,
			String degName,
			String inferenceName) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		mode=prop.mode;
		DiscreteGeneExpressionData dged
			=DiscreteGeneExpressionData.importFrom("amostras.dat", 5080, 46);
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BooleanGeneExpressionData deg = new BooleanGeneExpressionData(degName,true);
		//deg.clusterForRepeat();
//		int[] customGens=(prop.customGens==null)?deg.getValidArrayGens():prop.customGens;
		int[] customGens=prop.customGens;
		//deg.clusterForRepeat();
		//deg.setPorcentageValue(prop.porcentageValue);
		for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
			for (ID_EVALUATION_METRIC metric : prop.metrics) {
//				String namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
//						+ qualifying_function + "__" + metric + ".ylu";
				String namef = inferenceName+"__"+ qualifying_function + ".ylu";
				if(mode.equals("IN")) {
//					customGens=Resources.randomOrderSample(customGens, 4);
//					namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
//							+ qualifying_function + "__" + metric + ".ylu";
					for (int posCut = 0; posCut < deg.getSizeData(); posCut++) {
						this.tasksNetwork
							.add(new NetworkTask(
									customGens,
									deg.divideData(posCut).getKey(),
									posCut+"__"+namef,
									prop.searchAlgoritmic,
									qualifying_function,
									metric,
									prop.additionalMetric,
									prop.configInferences.get(deg.sizeData-1)));
						}
					
				}
				else if(mode.equals("RE")) {
					for (int posCut = 0; posCut < deg.getSizeData(); posCut++) {
						this.tasksNetwork
						.add(new NetworkTaskRe(customGens,
								pesosCut(prop.fileDegree, qualifying_function,posCut),
								deg.divideData(posCut).getKey(),
								dged.divideData(posCut).getKey(),
								prop.pathTest + posCut+"__"+ namef,
								prop.searchAlgoritmic,
								qualifying_function,
								metric, 
								prop.additionalMetric,
								prop.configInferences.get(deg.sizeData-1),
								prop.degreeComp));
					}
				}
				else if(mode.equals("EV")) {
					namef=prop.pathTest +inferenceName+qualifying_function+"_3Dis";
					this.tasksNetwork
					.add(new NetworkTaskEv(deg, namef, prop.configInferences.get(deg.sizeData),
							prop.additionalMetric));
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
	public void runCut2(String nameFileProperties,
			String degName,
			String inferenceName) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		mode=prop.mode;
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BooleanGeneExpressionData deg = new BooleanGeneExpressionData(degName, true);
		//deg.clusterForRepeat();
		int[] customGens=(prop.customGens==null)?deg.getValidArrayGens():prop.customGens;
//		int[] customGens=prop.customGens;
		//deg.clusterForRepeat();
		//deg.setPorcentageValue(prop.porcentageValue);
		for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
			for (ID_EVALUATION_METRIC metric : prop.metrics) {
//				String namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
//						+ qualifying_function + "__" + metric + ".ylu";
				String namef = inferenceName+"__"+ qualifying_function + ".ylu";
				if(mode.equals("IN")) {
//					customGens=Resources.randomOrderSample(customGens, 4);
//					namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
//							+ qualifying_function + "__" + metric + ".ylu";
					for (int cut = 0; cut < 5; cut++) {
						int posCut=3+8*cut;
						this.tasksNetwork
							.add(new NetworkTask(
									customGens,
									deg.divideData(posCut,8).getKey(),
									posCut+"_8"+"__"+namef,
									prop.searchAlgoritmic,
									qualifying_function,
									metric,
									prop.additionalMetric,
									prop.configInferences.get(deg.sizeData-8)));
						}
					int posCut=3+40;
					this.tasksNetwork
						.add(new NetworkTask(
								customGens,
								deg.divideData(posCut,6).getKey(),
								posCut+"_8"+"__"+namef,
								prop.searchAlgoritmic,
								qualifying_function,
								metric,
								prop.additionalMetric,
								prop.configInferences.get(deg.sizeData-8)));
					
				}
				else if(mode.equals("RE")) {
					DiscreteGeneExpressionData dged=
							DiscreteGeneExpressionData.importFrom("amostras.dat", 5080, 46);
					for (int cut = 0; cut < 5; cut++) {
						int posCut=3+8*cut;
					this.tasksNetwork
						.add(new NetworkTaskRe(customGens,
							pesosCut(prop.fileDegree,
									qualifying_function,
									posCut),
							deg.divideData(posCut, 8).getKey(),
							dged.divideData(posCut, 8).getKey(),
							prop.pathTest + posCut+"_8"+"__"+namef,
							prop.searchAlgoritmic,
							qualifying_function,
							metric, 
							prop.additionalMetric,
							prop.configInferences.get(deg.sizeData-8),
							prop.degreeComp));
					}
					int posCut=3+40;
					this.tasksNetwork
					.add(new NetworkTaskRe(customGens,
						pesosCut(prop.fileDegree, qualifying_function,
								posCut),
						deg.divideData(posCut, 6).getKey(),
						dged.divideData(posCut, 6).getKey(),
						prop.pathTest + posCut+"_8"+"__"+namef,
						prop.searchAlgoritmic,
						qualifying_function,
						metric, 
						prop.additionalMetric,
						prop.configInferences.get(deg.sizeData-8),
						prop.degreeComp));
				}
//				else if(mode.equals("EV")) {
//					namef=prop.pathTest +inferenceName+qualifying_function+"_3Dis";
//					this.tasksNetwork
//					.add(new NetworkTaskEv(deg, namef, prop.configInferences.get(deg.sizeData),
//							prop.additionalMetric));
//				}
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
	public void runCut2(String nameFileProperties,
			String degName,
			String inferenceName, String baseFile) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		mode=prop.mode;
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BooleanGeneExpressionData deg = new BooleanGeneExpressionData(degName, true);
		//deg.clusterForRepeat();
		int[] customGens=(prop.customGens == null && prop.degreeComp==Degree_Comparate.T)?
				deg.getValidArrayGens():prop.customGens;
		//3_8__all__SA
		String[] bs=baseFile.split("__");
		ID_QUALIFYING_FUNCTION qualifying_function =ID_QUALIFYING_FUNCTION.valueOf(bs[2].split("\\.")[0]); 
			for (ID_EVALUATION_METRIC metric : prop.metrics) {
				int posCut=Integer.parseInt(bs[0].split("_")[0]);
				if(mode.equals("IN")) {
						if(posCut<43)
						this.tasksNetwork
							.add(new NetworkTask(
									customGens,
									deg.divideData(posCut,8).getKey(),
									baseFile,
									prop.searchAlgoritmic,
									qualifying_function,
									metric,
									prop.additionalMetric,
									prop.configInferences.get(deg.sizeData-8)));
						else
						this.tasksNetwork
							.add(new NetworkTask(
								customGens,
								deg.divideData(posCut,6).getKey(),
								baseFile,
								prop.searchAlgoritmic,
								qualifying_function,
								metric,
								prop.additionalMetric,
								prop.configInferences.get(deg.sizeData-8)));
				}
				else if(mode.equals("RE")) {
					DiscreteGeneExpressionData dged=
							DiscreteGeneExpressionData.importFrom("amostras.dat", 5080, 46);
					if(posCut<43)
					this.tasksNetwork
						.add(new NetworkTaskRe(customGens,
							pesosCut(prop.fileDegree,
									qualifying_function,posCut),
							deg.divideData(posCut, 8).getKey(),
							dged.divideData(posCut, 8).getKey(),
							prop.pathTest + baseFile,
							prop.searchAlgoritmic,
							qualifying_function,
							metric, 
							prop.additionalMetric,
							prop.configInferences.get(deg.sizeData-8),
							prop.degreeComp));
					else
					this.tasksNetwork
					.add(new NetworkTaskRe(customGens,
						pesosCut(prop.fileDegree, qualifying_function,posCut),
						deg.divideData(posCut, 6).getKey(),
						dged.divideData(posCut, 6).getKey(),
						prop.pathTest + baseFile,
						prop.searchAlgoritmic,
						qualifying_function,
						metric, 
						prop.additionalMetric,
						prop.configInferences.get(deg.sizeData-8),
						prop.degreeComp));
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
	public void run(String nameFileProperties,
			String degName,
			DiscreteGeneExpressionData dged,
			String inferenceName) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		mode=prop.mode;
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BooleanGeneExpressionData deg = new BooleanGeneExpressionData(degName,true);
		//deg.clusterForRepeat();
		int[] customGens=(prop.customGens==null)?deg.getValidArrayGens():prop.customGens;
		//deg.clusterForRepeat();
		//deg.setPorcentageValue(prop.porcentageValue);
		for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
			for (ID_EVALUATION_METRIC metric : prop.metrics) {
//				String namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
//						+ qualifying_function + "__" + metric + ".ylu";
				String namef = "allRE__"+ qualifying_function + ".ylu";
				if(mode.equals("IN")) {
//					customGens=Resources.randomOrderSample(customGens, 4);
					namef = inferenceName + "__" + prop.searchAlgoritmic + "__"
							+ qualifying_function + "__" + metric + ".ylu";
					this.tasksNetwork
						.add(new NetworkTask(customGens,
								deg,
								namef,
								prop.searchAlgoritmic,
								qualifying_function,
								metric,
								prop.additionalMetric,
								prop.configInferences.get(deg.sizeData)));
				}
				else if(mode.equals("RE")) {
					this.tasksNetwork
					.add(new NetworkTaskRe(customGens,
							pesos(prop.fileDegree, qualifying_function),
							deg,
							dged,
							prop.pathTest + namef,
							prop.searchAlgoritmic,
							qualifying_function,
							metric, 
							prop.additionalMetric,
							prop.configInferences.get(deg.sizeData),
							prop.degreeComp));
				}
				else if(mode.equals("EV")) {
					namef=prop.pathTest +inferenceName+qualifying_function+"_3Dis";
					this.tasksNetwork
					.add(new NetworkTaskEv(deg, namef, prop.configInferences.get(deg.sizeData),
							prop.additionalMetric));
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

	private HashMap<String, Integer> pesos(String fileDegree,
			ID_QUALIFYING_FUNCTION qualifying_function) throws IOException {
		HashMap<String, Integer> map=new HashMap<>();
		BufferedReader in = new BufferedReader(new FileReader(fileDegree));
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			String[] configLine = line.split(","); 
			map.put(configLine[0]+configLine[1],Integer.parseInt(configLine[configLine.length-1]));
		}
		in.close();
		return map;
	}
	private HashMap<String, Integer> pesosCut(String fileDegree,
			ID_QUALIFYING_FUNCTION qualifying_function, int cut) throws IOException {
		if (fileDegree==null || fileDegree.isEmpty() || !fileDegree.endsWith(".csv"))
			return null;
		HashMap<String, Integer> map=new HashMap<>();
		BufferedReader in = new BufferedReader(new FileReader(fileDegree));
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			String[] configLine = line.split(",");
			if(cut == Integer.parseInt(configLine[configLine.length-2]))
				map.put(configLine[0]+configLine[1]
						,Integer.parseInt(configLine[configLine.length-1]));
		}
		in.close();
		return map;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		TestRunCustomInference tmi = new TestRunCustomInference();
		if (args.length > 0)
			if(args[0].equals("CUT")) {
				tmi.runCut(args[1], args[2], args[3]);
			}else if(args[0].equals("CUT2")) {
				if(args.length>4)
					tmi.runCut2(args[1], args[2], args[3],args[4]);
				else
					tmi.runCut2(args[1], args[2], args[3]);
			}else
			tmi.run(args[0],args[1],
					DiscreteGeneExpressionData.importFrom(args[2], 5080, 46),args[3]);
		else
			tmi.runCut2("jpGN.properties","/home/fernandito/MALARIA_TEST/cut8allRE/malaria.ged", "all", "3_8__all__SA.ylu");
//			tmi.run("jpGN.properties",System.getProperty("user.home")+"/MALARIA_TEST/malaria.ged",
//					DiscreteGeneExpressionData.importFrom(System.getProperty("user.home")+"/MALARIA_TEST/amostras.dat", 5080, 47), "b");
	}
}
