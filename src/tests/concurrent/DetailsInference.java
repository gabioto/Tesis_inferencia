package tests.concurrent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import geneticNetwork.BooleanGeneExpressionData;
import geneticNetwork.GeneticNetwork;
import geneticNetwork.GeneticNetwork.Topology_Network;
import geneticNetwork.GeneticNetwork.Type_Dynamic;
import inference.ConfigInference;
import inference.ConfigInference.ID_EVALUATION_METRIC;
import inference.ConfigInference.ID_QUALIFYING_FUNCTION;
import inference.ConfigInference.ID_SEARCH_ALGORITM;
import inference.InferenceNetwork;
import inference.NetworkInfered;
import inference.evaluationMetric.InvGaussianError;
//import inference.evaluationMetric.EvaluationMetric;
import inference.evaluationMetric.MutualInformation;
import inference.qualifyingFunction.CanalizateGrouping;
import inference.qualifyingFunction.CanalizateGroupingLinear;
import inference.qualifyingFunction.Details;
import inference.qualifyingFunction.DetailsGroup;
//import inference.qualifyingFunction.GroupingLatticeSFS;
//import inference.qualifyingFunction.LinearGrouping;
import inference.qualifyingFunction.QualifyingFunction;
import inference.qualifyingFunction.WithoutGrouping;
import javafx.util.Pair;

public class DetailsInference {
	public void sampleNetwork() throws FileNotFoundException {
		Topology_Network topology= Topology_Network.RANDOM;
		int genes=10;
		double degree=3;
		Type_Dynamic dynamic=Type_Dynamic.BOOLEAN;
		int amostras=40;
		GeneticNetwork gn=null;
		BooleanGeneExpressionData ged=null;
		int tg=0;
		while(gn==null && tg++<10000) {
			gn=GeneticNetwork.buildNetwork(genes, 1, degree, 1.0, topology);
			int tge=0;
			while(ged==null && tge++<100) {
				ged=gn.createGeneExpressionData(amostras, 1, dynamic, null, false);
			}
			if(ged==null)
				gn=null;
		}
		if(ged!=null) {
			gn.writeInFile("gnSource");
			ged.saveDeg("sampleGED");
			System.out.println("chovi");
		}else
			System.out.println("nadas");
	}
	public void inferenceCompara() throws IOException {
		String path="/home/fernandito/FIXED/";
		Topology_Network topology= Topology_Network.FIXED;
		int genes=50;
		int in=0;
		int ntimes=0;
		double degree=3;
		Type_Dynamic dynamic=Type_Dynamic.BOOLEAN;
		int amostras=50;
		ID_SEARCH_ALGORITM sa=ID_SEARCH_ALGORITM.SFS;
		HashMap<ID_SEARCH_ALGORITM, Object> hs=new HashMap<>();
		hs.put(sa, 6);
		//ID_EVALUATION_METRIC em=ID_EVALUATION_METRIC.IM;
		HashMap<ID_EVALUATION_METRIC, Object> he=null;
		//ID_QUALIFYING_FUNCTION qf=ID_QUALIFYING_FUNCTION.CGL;
		HashMap<ID_QUALIFYING_FUNCTION, Integer> hq=null;
		MutualInformation em=new MutualInformation(amostras-1);
		InvGaussianError ige=new InvGaussianError(amostras-1);
		//QualifyingFunction[] qfs=new QualifyingFunction[] {new  WithoutGrouping(em)};
		QualifyingFunction qf=//new CanalizateGroupingLinear(em);
//				new LinearGrouping(em);
				new CanalizateGrouping(em);
		
		//hq.put(qf, value)
		ConfigInference configInference=new ConfigInference(amostras-1,he,hq,hs);
				
		String nameNetwork =topology + "__" + in + "__" + genes + "__" + degree;
//		GeneticNetwork gn=GeneticNetwork.readFromFile(path+"gabaritos/"+nameNetwork);
//		geneticNetwork.showBoolean();
		String nameSample = nameNetwork  + "__"
				+ dynamic + "__" + amostras + "__" + ntimes;
//		String namef = nameSample + "__"
//				+ sa + "__" + qf.getId()+ "__" + em.getId()
//				+ ".ylu";
		//NetworkInfered2 ni=NetworkInfered2.readFromFile(path+"inference/"+namef);
		BooleanGeneExpressionData ged= new BooleanGeneExpressionData(path+"deg/"+nameSample);
		InferenceNetwork inferenceNetwork=new InferenceNetwork(ged, "inf",sa,em.getId(),ige.getId(), qf.getId(),configInference);
		Pair<NetworkInfered, StringBuffer> inTuple=inferenceNetwork.inferedNetwork("pref");
//		NetworkInfered2 ni=inTuple.getKey();
//		int sizeGenes=gn.getSizeGenes();
		//for (int i = 0; i < sizeGenes; i++) {
			//LinkedList<Double>[] lists=(LinkedList<Double>[])inTuple.getValue()[i];
			//System.out.println("Gabarito:\n"+gn.detailsGeneExpression(i, ged));
			System.out.println(inTuple.getValue());
//			Details details= qf.quantifyDetails(ni.getGeneInferedById(i).getPredictors(), ged, i);
//			qf.buildGroupingDetails(details);
//			System.out.println(details.toPreditorsString());
		//}
	}
	public void detailsInference() throws IOException {
		String path="/home/fernandito/FIXED3A10/";
		Topology_Network topology= Topology_Network.FIXED;
		int genes=50;
		int in=0;
		int ntimes=0;
		double degree=3;
		Type_Dynamic dynamic=Type_Dynamic.BOOLEAN;
		int amostras=30;
		ID_SEARCH_ALGORITM sa=ID_SEARCH_ALGORITM.IES;
		MutualInformation em=new MutualInformation(amostras-1);
		QualifyingFunction[] qfs=new QualifyingFunction[] {new  WithoutGrouping(em),new CanalizateGroupingLinear(em)};
		QualifyingFunction qf=qfs[1];
		String nameNetwork =topology + "__" + in + "__" + genes + "__" + degree;
//		GeneticNetwork geneticNetwork=GeneticNetwork.readFromFile(path+"gabaritos/"+nameNetwork);
//		geneticNetwork.showBoolean();
		String nameSample = nameNetwork  + "__"
				+ dynamic + "__" + amostras + "__" + ntimes;
		String namef = nameSample + "__"
						+ sa + "__" + qf.getId()+ "__" + em.getId()
						+ ".ylu";
		NetworkInfered ni=NetworkInfered.readFromFile(path+"inference/"+namef);
		BooleanGeneExpressionData booleanGeneExpressionData=new BooleanGeneExpressionData(path+"deg/"+nameSample); 
		for (int i = 0; i < ni.getGens().length; i++) {
			Details details= qf.quantifyDetails(ni.getGeneInferedById(i).getPredictors(), booleanGeneExpressionData, i);
			qf.buildGroupingDetails(details);
			System.out.println("Gene: "+i);
			//System.out.println("Canalizantes:<-"+AbstractNetwork.canalizingByDEGByGene(booleanGeneExpressionData, i));
			System.out.println(details.toPreditorsString());
		}
	}
	public void detailsCriteryFunction() throws IOException {
		String path="/home/fernandito/FIXED3A10/";
		Topology_Network topology= Topology_Network.FIXED;
		int genes=50;
		int in=0;
		int ntimes=0;
		double degree=3;
		Type_Dynamic dynamic=Type_Dynamic.BOOLEAN;
		int amostras=50;
		ID_SEARCH_ALGORITM sa=ID_SEARCH_ALGORITM.IES;
		MutualInformation em=new MutualInformation(amostras-1);
		QualifyingFunction[] qfs=new QualifyingFunction[] {new  WithoutGrouping(em),new CanalizateGroupingLinear(em)};
		String nameNetwork =topology + "__" + in + "__" + genes + "__" + degree;
//		GeneticNetwork geneticNetwork=GeneticNetwork.readFromFile(path+"gabaritos/"+nameNetwork);
//		geneticNetwork.showBoolean();
		String nameSample = nameNetwork  + "__"
				+ dynamic + "__" + amostras + "__" + ntimes;
		String namef = nameSample + "__"
						+ sa + "__" + qfs[0].getId()+ "__" + em.getId()
						+ ".ylu";
		NetworkInfered ni0=NetworkInfered.readFromFile(path+"inference/"+namef);
		String namef1 = nameSample + "__"
				+ sa + "__" + qfs[1].getId()+ "__" + em.getId()
				+ ".ylu";
		NetworkInfered ni1=NetworkInfered.readFromFile(path+"inference/"+namef1);
		BooleanGeneExpressionData booleanGeneExpressionData=new BooleanGeneExpressionData(path+"deg/"+nameSample); 
		for (int i = 0; i < ni0.getGens().length; i++) {
			Details details0= qfs[0].quantifyDetails(ni0.getGeneInferedById(i).getPredictors(), booleanGeneExpressionData, i);
			qfs[0].buildGroupingDetails(details0);
			Details details1= qfs[1].quantifyDetails(ni1.getGeneInferedById(i).getPredictors(), booleanGeneExpressionData, i);
			qfs[1].buildGroupingDetails(details1);
			double em0=em.conditionalEntropy(details0.frequencyTable);
			double em1=em.conditionalEntropy(
					(details1 instanceof DetailsGroup)?
							((DetailsGroup)details1).frequencyTableGroup[0]
									:details1.frequencyTable);
			System.out.println(((details0.preditors.equals(details1.preditors))?1:0)+","+
					details0.qualityValue+","+em0+","+
					details1.qualityValue+","+em1+","+
					(details0.qualityValue-details1.qualityValue)+","+
					(em0-em1));
		}
	}
	public static void main(String arg[]) throws IOException {
		DetailsInference d=new DetailsInference();
		d.inferenceCompara();
		//d.detailsCriteryFunction();
		//d.detailsInference();
	}
}
