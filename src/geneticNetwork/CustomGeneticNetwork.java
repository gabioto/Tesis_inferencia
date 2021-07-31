package geneticNetwork;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javafx.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CustomGeneticNetwork extends AbstractNetwork {
	private TreeMap<Integer, TreeSet<Integer>> gens;
	public CustomGeneticNetwork(TreeMap<Integer, TreeSet<Integer>> gens) {
		this.gens = gens;
	}
	public CustomGeneticNetwork() {
		this(new TreeMap<>());
	}
	
	public TreeMap<Integer, TreeSet<Integer>> getGens() {
		return gens;
	}
	public void setGens(TreeMap<Integer, TreeSet<Integer>> gens) {
		this.gens = gens;
	}
	@Override
	public int[] getArrayPreditorsByGene(int idGene) {
		return this.gens.get(idGene).stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public long[] getMainFunctionByGene(int idGene) {
		throw new NotImplementedException();
	}

	@Override
	public Pair<int[][], boolean[]> dinamicGeneDetails(int nGene, BooleanGeneExpressionData ged) {
		throw new NotImplementedException();
	}

	@Override
	public int getSizeGenes() {
		return this.gens.size();
	}

	@Override
	public TreeSet<Integer> getPredictorsByGene(int nGene) {
		return this.gens.get(nGene);
	}
	public String toFullString() {
		StringBuilder sb=new StringBuilder("CustomGeneticNEtwork|").append(this.gens.size()).append("\n");
		this.gens.forEach((k,v)->{
			sb.append(k).append(":")
			.append(v.stream().map(String::valueOf).collect(Collectors.joining(",")))
			.append("\n");		
		});
		return sb.toString();
	}
}
