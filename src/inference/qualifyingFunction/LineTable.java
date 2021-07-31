package inference.qualifyingFunction;

import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.IntStream;

import resources.Resources;

class LineTable implements Comparable<LineTable> {
//	private Resources Resources = new Resources();
	TreeSet<Integer> originalIndexs;// indices en la tabla de frecuencia sin agrupar
	int[] line; // arreglo de acumulacion de lineas
	int sum; // nuemro de elementos en el arreglo

	public LineTable(int[] line, int originalIndex) {
		this.line = line;
		this.sum =IntStream.of(line).sum();
		this.originalIndexs = new TreeSet<>();
		this.originalIndexs.add(originalIndex);
	}

	public LineTable(LineTable lt1, LineTable lt2) {
		if(Collections.disjoint(lt1.originalIndexs, lt2.originalIndexs)) {
			this.line = Resources.sumArray(lt1.line, lt2.line);
			this.sum = lt1.sum + lt2.sum;
			this.originalIndexs = new TreeSet<>();
			this.originalIndexs.addAll(lt1.originalIndexs);
			this.originalIndexs.addAll(lt2.originalIndexs);
		}
		else
			throw new IllegalArgumentException("lineas con index en comun: " + lt1.originalIndexs + " "+ lt2.originalIndexs);
	}

	@Override
	public int compareTo(LineTable o) {
		return Integer.compare(this.sum, o.sum);
	}
}