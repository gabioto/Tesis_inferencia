package tests.concurrent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.DiscreteGeneExpressionData;
import inference.ConfigInference;
import inference.CustomInferenceForMultipleData;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;

public class TestInferenceForMultipleData {
	public interface Task {
		void run() throws IOException;
	}

	public class NetworkTask implements Task {
		private CustomInferenceForMultipleData customInferenceForMultipleData;
		private int[] customGens;
		private BooleanGeneExpressionData bgde;
		private DiscreteGeneExpressionData dgde;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private String mode;
		
		public NetworkTask(int[] customGens,
				BooleanGeneExpressionData bged,
				DiscreteGeneExpressionData dgde,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference,
				String mode) {
			this.customGens=customGens;
			this.bgde = bged;
			this.dgde= dgde;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.mode=mode;
		}

		

		@Override
		public void run() throws IOException {
			this.customInferenceForMultipleData = new CustomInferenceForMultipleData(
					 this.customGens
					,this.bgde
					,this.dgde
					,this.networkFile
					,this.search_algoritm
					,this.evaluation_metric
					,this.additional_metric
					,this.qualifying_function
					,this.configInference
					,true);
			int nGens = customGens.length;
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTaskInference(this.customInferenceForMultipleData, i,mode));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}

	public class NetworkTaskCut implements Task {
		private CustomInferenceForMultipleData customInferenceForMultipleData;
		private int[] customGens;
		private BooleanGeneExpressionData bgde;
		private DiscreteGeneExpressionData dgde;
		private int indexCut;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private String mode;
		
		public NetworkTaskCut(int[] customGens,
				BooleanGeneExpressionData bged,
				DiscreteGeneExpressionData dgde,
				int indexCut,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference,
				String mode) {
			this.customGens=customGens;
			this.bgde = bged;
			this.dgde= dgde;
			this.indexCut=indexCut;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.mode=mode;
		}
		@Override
		public void run() throws IOException {
			this.customInferenceForMultipleData = new CustomInferenceForMultipleData(
					 this.customGens
					,this.bgde.divideData(indexCut).getKey()
					,this.dgde.divideData(indexCut).getKey()
					,this.networkFile
					,this.search_algoritm
					,this.evaluation_metric
					,this.additional_metric
					,this.qualifying_function
					,this.configInference
					,true);
			int nGens = customGens.length;
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTaskInference(this.customInferenceForMultipleData, i,mode));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}
	public class NetworkTaskCut2 implements Task {
		private CustomInferenceForMultipleData customInferenceForMultipleData;
		private int[] customGens;
		private BooleanGeneExpressionData bgde;
		private DiscreteGeneExpressionData dgde;
		private int indexCut;
		private int sizeCut;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ID_EVALUATION_METRIC additional_metric;
		private ConfigInference configInference;
		private String mode;
		
		public NetworkTaskCut2(int[] customGens,
				BooleanGeneExpressionData bged,
				DiscreteGeneExpressionData dgde,
				int indexCut,
				int sizeCut,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric,
				ID_EVALUATION_METRIC additional_metric,
				ConfigInference configInference,
				String mode) {
			this.customGens=customGens;
			this.bgde = bged;
			this.dgde= dgde;
			this.indexCut=indexCut;
			this.sizeCut=sizeCut;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			this.additional_metric=additional_metric;
			this.configInference = configInference;
			this.mode=mode;
		}
		@Override
		public void run() throws IOException {
			this.customInferenceForMultipleData = new CustomInferenceForMultipleData(
					 this.customGens
					,this.bgde.divideData(indexCut, sizeCut).getKey()
					,this.dgde.divideData(indexCut, sizeCut).getKey()
					,this.networkFile
					,this.search_algoritm
					,this.evaluation_metric
					,this.additional_metric
					,this.qualifying_function
					,this.configInference
					,true);
			int nGens = customGens.length;
			for (int i = 0; i < nGens; i++) {
				tasksGene.add(new GeneTaskInference(this.customInferenceForMultipleData, i,mode));
			}
			System.out.println("In ->" + this.networkFile);
		}
	}
	public class GeneTaskInference implements Task {
		private CustomInferenceForMultipleData customInferenceForMultipleData;
		private int iGene;
		private String mode;

		public GeneTaskInference(CustomInferenceForMultipleData customInferenceForMultipleData, int iGene, String mode) {
			super();
			this.customInferenceForMultipleData = customInferenceForMultipleData;
			this.iGene = iGene;
			this.mode = mode;
		}

		@Override
		public void run() throws IOException{
			try {
				customInferenceForMultipleData.inferedGenebyIndex(iGene, mode);
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

	
	public void run(String nameFileProperties,
			String booleanName,
			String discreteName,
			String inferenceName,
			String cut) throws IOException, InterruptedException {
		PropertiesTestC prop = new PropertiesTestC(nameFileProperties);
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
		BooleanGeneExpressionData bged = new BooleanGeneExpressionData(booleanName,true);
//		System.out.println(bged.clusterForRepeatKeys());
		DiscreteGeneExpressionData dged= DiscreteGeneExpressionData.importFrom(discreteName, 5080, 47);
		//bged.clusterForRepeat();
		int[] customGens=(prop.customGens==null)?bged.getValidArrayGens():prop.customGens;
//		System.out.println(Arrays.toString(customGens));
		//deg.clusterForRepeat();
		//deg.setPorcentageValue(prop.porcentageValue);
		if(cut==null) {
			for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
				ID_EVALUATION_METRIC metric =ID_EVALUATION_METRIC.IM;
				ID_EVALUATION_METRIC additonal_metric =ID_EVALUATION_METRIC.IM;//ID_EVALUATION_METRIC.IGE;
				String namef = inferenceName + "__"+ qualifying_function + ".ylu";
						this.tasksNetwork
							.add(new NetworkTask(customGens,
									bged,
									dged,
									namef,
									prop.searchAlgoritmic,
									qualifying_function,
									metric,
									additonal_metric,
									prop.configInferences.get(bged.sizeData),
									prop.mode));
				
			}
		}
		else if(cut.equals("CUT")) {
			for (int i = 0; i < bged.getSizeGens(); i++) {
				for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
					ID_EVALUATION_METRIC metric =ID_EVALUATION_METRIC.IM;
					ID_EVALUATION_METRIC additonal_metric =ID_EVALUATION_METRIC.IM;//ID_EVALUATION_METRIC.IGE;
					String namef = i+"__"+inferenceName + "__"+ qualifying_function + ".ylu";
							this.tasksNetwork
								.add(new NetworkTaskCut(customGens,
										bged,
										dged,
										i,
										namef,
										prop.searchAlgoritmic,
										qualifying_function,
										metric,
										additonal_metric,
										prop.configInferences.get(bged.sizeData),
										prop.mode));
					
				}
			}
		}
		else if(cut.equals("CUT2")) {
			for (int i = 0; i < 5; i++) {
				for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
					ID_EVALUATION_METRIC metric =ID_EVALUATION_METRIC.IM;
					ID_EVALUATION_METRIC additonal_metric =ID_EVALUATION_METRIC.IM;//ID_EVALUATION_METRIC.IGE;
					int pointCut=5+(8*i); int sizeCut=8;
					String namef = pointCut+"_"+sizeCut+"__"+inferenceName + "__"+ qualifying_function + ".ylu";
							this.tasksNetwork
								.add(new NetworkTaskCut2(customGens,
										bged,
										dged,
										pointCut,
										sizeCut,
										namef,
										prop.searchAlgoritmic,
										qualifying_function,
										metric,
										additonal_metric,
										prop.configInferences.get(bged.sizeData),
										prop.mode));
					
				}
			}
			for (ID_QUALIFYING_FUNCTION qualifying_function : prop.groupingMethodologies) {
				ID_EVALUATION_METRIC metric =ID_EVALUATION_METRIC.IM;
				ID_EVALUATION_METRIC additonal_metric =ID_EVALUATION_METRIC.IM;//ID_EVALUATION_METRIC.IGE;
				int pointCut=5+(8*5); int sizeCut=6;
				String namef = pointCut+"_"+sizeCut+"__"+inferenceName + "__"+ qualifying_function + ".ylu";
						this.tasksNetwork
							.add(new NetworkTaskCut2(customGens,
									bged,
									dged,
									pointCut,
									sizeCut,
									namef,
									prop.searchAlgoritmic,
									qualifying_function,
									metric,
									additonal_metric,
									prop.configInferences.get(bged.sizeData),
									prop.mode));
				
			}
		}
		// rodar hilos
		for (int i = 0; i < prop.nThreads; i++) {
			(new Thread(new ThreadInference(i))).start();
		}
		
		System.out.println("fin");
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		TestInferenceForMultipleData tmi = new TestInferenceForMultipleData();
		if (args.length > 0)
			tmi.run(args[0],args[1],args[2],args[3],
					(args.length<5)?null:args[4]);
		else
			tmi.run("jpGN.properties",
					"/home/fernandito/MALARIA_TEST/malaria.ged",
					"/home/fernandito/MALARIA_TEST/amostras.dat",
					"re_mel",
					"CUT2");
	}
}
