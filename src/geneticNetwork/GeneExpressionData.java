package geneticNetwork;

import java.util.List;


public interface GeneExpressionData {


	List<Integer> getValidGens();

	int getSizeGens();

	byte getData(int i, int targetGene);

	int getSizeData();

	int sizeDiscreteValues();

}
