package inference;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import geneticNetwork.*;
import inference.ConfigInference.*;
import inference.qualifyingFunction.*;
import resources.Resources;

public class MainConcurrent {
//	Resources resources=new Resources();
	public interface Task {
		void run();
	}
	public class NetworkTask implements Task {
		private GeneticNetwork originalNetwork;
		private String degFile;
		private String networkFile;
		private ID_SEARCH_ALGORITM search_algoritm;
		private ID_QUALIFYING_FUNCTION qualifying_function;
		private ID_EVALUATION_METRIC evaluation_metric;
		private ConfigInference configInference;

		public NetworkTask(GeneticNetwork originalnetwork,
				String degFile,
				String networkFile,
				ID_SEARCH_ALGORITM search_algoritm,
				ID_QUALIFYING_FUNCTION qualifying_function,
				ID_EVALUATION_METRIC evaluation_metric) {
			this.originalNetwork=originalnetwork;
			this.degFile = degFile;
			this.networkFile = networkFile;
			this.search_algoritm = search_algoritm;
			this.qualifying_function = qualifying_function;
			this.evaluation_metric = evaluation_metric;
			HashMap<ID_SEARCH_ALGORITM, Object> searchParams=new HashMap<>();
			searchParams.put(search_algoritm, 3);
			this.configInference=new ConfigInference(-1,null, null, searchParams); 
		}

		@Override
		public void run() {
			BooleanGeneExpressionData ged= new BooleanGeneExpressionData(this.degFile);
			int nGens = ged.getValidGens().size();
			for (int i = 0; i < nGens; i++) {
				int idGene=ged.getValidGens().get(i);
				InferenceGene inferenceGene=new InferenceGene(configInference.getQualifyingFunctionById(qualifying_function, evaluation_metric), 
						configInference.getSearchAlgoritmbyId(search_algoritm),null);
				tasksGene.add(new GeneTask(inferenceGene, idGene,ged,networkFile,originalNetwork.getPredictors()[idGene], originalNetwork.getBoolean_functions()[idGene]));
			}
			System.out.println(this.search_algoritm + " " + this.qualifying_function + " " + this.evaluation_metric);
		}
	}

	public class GeneTask implements Task {
		private InferenceGene inferenceGene;
		private int iGene;
		private BooleanGeneExpressionData booleanGeneExpressionData;
		private String sufFile;
		private TreeSet<Integer> preditors;
		private long[][] originalfunction;

		public GeneTask(InferenceGene inferenceGene, int iGene, BooleanGeneExpressionData booleanGeneExpressionData, String sufFile,
				TreeSet<Integer> preditors, long[][] originalfunction) {
			this.inferenceGene = inferenceGene;
			this.iGene = iGene;
			this.booleanGeneExpressionData = booleanGeneExpressionData;
			this.sufFile = sufFile+".txt";
			this.preditors = preditors;
			this.originalfunction = originalfunction;
		}
		@Override
		public void run() {
			try {
				PrintWriter p=new PrintWriter(iGene+"_"+sufFile);
				p.println(iGene+":\t"+preditors);
				if(originalfunction!=null)
					p.println(Resources.toTrueTableFormat(originalfunction[0], 1<<preditors.size()));
				else
					p.println("RANDOM");
//				WithoutGrouping wg=new WithoutGrouping(null);
				Details d=new Details();
				d.preditors=preditors;
				d.frequencyTable= AbstractNetwork.buildFrequencyTable(preditors, booleanGeneExpressionData, iGene);
				p.print(d.frequencyTableString());
				p.close();
				inferenceGene.saveDetailsPreditors(booleanGeneExpressionData, iGene, sufFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println(iGene);
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
					}
				}
				task.run();
			}

		}
	}

	public LinkedBlockingQueue<NetworkTask> tasksNetwork;
	public LinkedBlockingQueue<GeneTask> tasksGene;

	public MainConcurrent() {
		super();
		this.tasksNetwork = new LinkedBlockingQueue<>();
		this.tasksGene = new LinkedBlockingQueue<>();
	}

	public void run() {
		ID_QUALIFYING_FUNCTION[] qualifyings = new ID_QUALIFYING_FUNCTION[] { 
				ID_QUALIFYING_FUNCTION.SA,
				ID_QUALIFYING_FUNCTION.CG};
		int sizes[]=new int[] {
				20,
				50};
		GeneticNetwork originalNetwork=null;
		try {
			originalNetwork = GeneticNetwork.readFromFile("/home/fernando/FIXED3A/gabaritos/FIXED__0__50__3.0.ylu");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (ID_QUALIFYING_FUNCTION qualifying : qualifyings) {
			for (int size : sizes) {
				this.tasksNetwork.add(new NetworkTask(originalNetwork,"/home/fernando/FIXED3A/deg/FIXED__0__50__3.0__BOOLEAN__"+size+"__1",
						qualifying + "_"+size, ID_SEARCH_ALGORITM.NDES, qualifying, ID_EVALUATION_METRIC.IM));		
			}
		}
		
		// }
		int nThread = 1;
		for (int i = 0; i < nThread; i++) {
			(new Thread(new ThreadInference(i))).start();
		}
	}

	public static void main(String[] arg) {
		MainConcurrent m = new MainConcurrent();
		m.run();
	}
}
