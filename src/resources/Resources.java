package resources;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

//import inference.qualifyingFunction.Details;
import resources.QuineMcCluskey.*;

public class Resources {
	public static final Random random = new Random();
	public static final int MAX_REDUCIBLE = 4;
	public static long[][] functionReduzible = new long[MAX_REDUCIBLE][];
	static {
		for (int i = 0; i < functionReduzible.length; i++) {
			int nPred = i + 1;
			ArrayList<Long> fr = new ArrayList<>();
			long ntests = 1L << (1 << nPred);
			for (long j = 0; j < ntests; j++) {
				if (reducible(nPred, j))
					fr.add(j);
			}

			functionReduzible[i] = new long[fr.size()];
			for (int j = 0; j < functionReduzible[i].length; j++) {
				functionReduzible[i][j] = fr.get(j);
			}
		}
		// ArrayList<Long> fr =new ArrayList<>();
		// for (long j = Long.MIN_VALUE; j <= Long.MAX_VALUE; j++) {
		// if(resources.isReducible(6, new Long[] {j}))
		// fr.add(j);
		// }
		// functionReduzible[6]=fr.toArray(new Long[] {});
	}

	private Resources() {
	}

	public static boolean existsArray(boolean[][] array2d, int indexR, int dess) {
		for (int i = dess; i < indexR; i++) {
			if (Arrays.equals(array2d[i], array2d[indexR]))
				return true;
		}
		return false;
	}

	public static double log_b(double x, int b) {
		return (x == 0) ? 0 : (Math.log10(x) / Math.log10(b));
	}

	/**
	 *
	 * @param prob
	 * @param b
	 * @return
	 */
	public static double entropy(double prob, int b) {
		return (prob == 0) ? 0 : -prob * log_b(prob, b);
	}

	public static long binomial(int n, int k) {
		if (k > n - k)
			k = n - k;
		long b = 1;
		for (int i = 1, m = n; i <= k; i++, m--)
			b = b * m / i;
		return b;
	}

	public static boolean isReducible(int sizePreditors, long booleanTable) {
		if (sizePreditors <= 0) {
			return false;
		}
		if (sizePreditors <= 3) {
			return (Arrays.binarySearch(functionReduzible[sizePreditors - 1], booleanTable) >= 0);
		}
		return reducible(sizePreditors, booleanTable);
	}

	private static boolean reducible(int sizePreditors, long booleanTable) {
		List<Term> tabla = new ArrayList<Term>();
		int nroEstados = (1 << sizePreditors);
		for (int i = 0; i < nroEstados; i++) {
			long evaluador = (long) 1 << (i % 64);
			if ((booleanTable & evaluador) != 0) {
				byte[] estado = new byte[sizePreditors];
				for (int j = 0; j < sizePreditors; j++) {
					estado[j] = (byte) ((((1 << j) & (i)) == 0) ? 0 : 1);
				}
				tabla.add(new Term(estado));
			}
		}
		if (tabla.size() > 0) {
			Formula f = new Formula(tabla);
			// aplicar simplificacion
			f.reduceToPrimeImplicants();
			f.reducePrimeImplicantsToSubset();
			// limpiar variables sobrantes
			int nroDeActivas = 0;
			boolean[] activas = new boolean[sizePreditors];
			for (int i = 0; i < activas.length; i++) {
				activas[i] = false;
			}
			List<Term> termList = f.getTermList();
			for (Iterator<Term> it = termList.iterator(); it.hasNext();) {
				byte[] varVals = it.next().getVarVals();
				for (int i = 0; i < varVals.length; i++) {
					if (!activas[i] && (varVals[i] != Term.DontCare)) {
						activas[i] = true;
						nroDeActivas++;
						if (nroDeActivas == sizePreditors) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public static long maskCanalizant(int sizeGenes, int nGene) {
		long mask = 0L;
		final int sizeloop = 1 << (sizeGenes - 1 - nGene);
		final long sum = (1L << (1 << nGene)) - 1;
		final int despl = 1 << (nGene + 1);
		for (int i = 0; i < sizeloop; i++) {
			mask = mask << despl;
			mask = mask | sum;
		}
		final long top = (1L << (1 << sizeGenes)) - 1;
		// System.out.println(Long.toBinaryString(top));
		return (sizeGenes == 6) ? ~mask : (~mask) & top;
	}

	public static long[] maskArrayCanalizant(int sizeGenes, int nGene) {
		if (sizeGenes <= 6)
			return new long[] { maskCanalizant(sizeGenes, nGene) };
		else {
			long[] mask = new long[1 << (sizeGenes - 6)];
			if (nGene < 6) {
				long m = maskCanalizant(6, nGene);
				for (int i = 0; i < mask.length; i++)
					mask[i] = m;
			} else {
				long m = maskCanalizant(sizeGenes - 6, nGene - 6);
				for (int i = 0; i < mask.length; i++) {
					if ((m & (1L << i)) != 0)
						mask[i] = -1L;
				}
			}
			return mask;
		}
	}

	public static String matrizToString(int[][] matriz) {
		return Arrays.stream(matriz).map(a -> Resources.arrayToString(a, "\t")).collect(Collectors.joining("\n"));
	}

	public static String arrayToString(int[] array, String sep) {
		return Arrays.stream(array).mapToObj(String::valueOf).collect(Collectors.joining(sep));
	}

	public static String preditorsToString(NavigableSet<Integer>[] aarayPreditors) {
		AtomicInteger index = new AtomicInteger();
		return Arrays.stream(aarayPreditors).map(t -> index.getAndIncrement() + " -> " + t.toString())
				.collect(Collectors.joining("\n"));
	}
	// public static boolean isReducible(int sizePreditors, long[] booleanTable) {
	// if (sizePreditors <= 0) {
	// return false;
	// }
	// if (sizePreditors<=3) {
	// if (functionReduzible[0]==null)
	// initReduzible();
	// return (Arrays.binarySearch(functionReduzible[sizePreditors-1],
	// booleanTable[0])>=0);
	// }
	// List<Term> tabla = new ArrayList<Term>();
	// int nroEstados = (1 << sizePreditors);
	// for (int i = 0; i < nroEstados; i++) {
	// long evaluador = (long) 1 << (i % 64);
	// if ((booleanTable[i / 64] & evaluador) != 0) {
	// byte[] estado = new byte[sizePreditors];
	// for (int j = 0; j < sizePreditors; j++) {
	// estado[j] = (byte) ((((1 << j) & (i)) == 0) ? 0 : 1);
	// }
	// tabla.add(new Term(estado));
	// }
	// }
	// if (tabla.size() > 0) {
	// Formula f = new Formula(tabla);
	// // aplicar simplificacion
	// f.reduceToPrimeImplicants();
	// f.reducePrimeImplicantsToSubset();
	// // limpiar variables sobrantes
	// int nroDeActivas = 0;
	// boolean[] activas = new boolean[sizePreditors];
	// for (int i = 0; i < activas.length; i++) {
	// activas[i] = false;
	// }
	// List<Term> termList = f.getTermList();
	// for (Iterator<Term> it = termList.iterator(); it.hasNext();) {
	// byte[] varVals = it.next().getVarVals();
	// for (int i = 0; i < varVals.length; i++) {
	// if (!activas[i] && (varVals[i] != Term.DontCare)) {
	// activas[i] = true;
	// nroDeActivas++;
	// if (nroDeActivas == sizePreditors) {
	// return false;
	// }
	// }
	// }
	// }
	// }
	// return true;
	// }

	// public void printDEG(boolean[][] ged) {
	// for (int i = 0; i < ged.length; i++) {
	// boolean[] bs = ged[i];
	// for (boolean b : bs) {
	// System.out.print(((b) ? 1 : 0) + " ");
	// }
	// System.out.println();
	// }
	// }

	public static int[] sumArray(int[] array1, int[] array2) {
		int[] sum = new int[array1.length];
		for (int i = 0; i < sum.length; i++) {
			sum[i] = array1[i] + array2[i];
		}
		return sum;
	}

	public static double entropyLine(int[] line) {
		return entropyLine(line, IntStream.of(line).sum());
	}

	public static double entropyLine(int[] line, int sum) {
		double le = 0;
		if (sum > 0)
			for (int i : line) {
				le += entropy((double) i / sum, 2);
			}
		return le;
	}

	// public void saveDeg(File file, boolean[][] deg) throws FileNotFoundException
	// {
	// if (!file.getName().endsWith("deg")) {
	// file = new File(file.getAbsolutePath() + ".deg");
	// }
	// PrintWriter writer = new PrintWriter(file);
	// writer.println(deg.length +" "+deg[0].length);
	// StringBuilder sb=new StringBuilder();
	// for (int i = 0; i < deg.length; i++) {
	// for (int j = 0; j < deg[i].length; j++) {
	// sb.append((deg[i][j])? "1 ":"0 ");
	// }
	// sb.deleteCharAt(sb.length()-1).append("\n");
	// }
	// writer.print(sb.deleteCharAt(sb.length()-1));
	// writer.close();
	// }
	// public boolean[][] readDeg(File file) throws IOException{
	//
	// BufferedReader in = new BufferedReader(new FileReader(file));
	// String line = in.readLine();
	// String[] configLine = line.split(" ");
	// boolean[][] deg= new
	// boolean[Integer.parseInt(configLine[0])][Integer.parseInt(configLine[1])];
	// // read and process gene'ś data
	// int iLinha=0;
	// while ((line = in.readLine()) != null) {
	// String[] states = line.split(" ");
	// // read states
	// for (int i = 0; i < states.length; i++) {
	// deg[iLinha][i]=(Integer.parseInt(states[i])==1);
	// }
	// iLinha++;
	// }
	// in.close();
	// return deg;
	// }
	public static int[] sumFrequency(int[][] arrayFrequency) {
		int[] sum = new int[arrayFrequency[0].length];
		for (int i = 0; i < arrayFrequency.length; i++) {
			for (int j = 0; j < sum.length; j++) {
				sum[j] += arrayFrequency[i][j];
			}
		}
		return sum;
	}

	public static int max(int[] arrayInt) {
		return IntStream.of(arrayInt).max().orElse(Integer.MIN_VALUE);
	}

	public static void mergeAddMatrix(int[] baseMatrix, int[] addMatrix) {
		for (int i = 0; i < baseMatrix.length; i++) {
			baseMatrix[i] += addMatrix[i];
		}
	}

	public static void mergeAddMatrix(int[][] baseMatrix, int[][] addMatrix) {
		for (int i = 0; i < baseMatrix.length; i++) {
			for (int j = 0; j < baseMatrix[i].length; j++) {
				baseMatrix[i][j] += addMatrix[i][j];
			}
		}
	}

	public static double[][] divArrayFrequency(int[][] countValues, int[] sizeDegreeValues) {
		double[][] pArray = new double[countValues.length][countValues[0].length];
		for (int i = 0; i < pArray.length; i++) {
			for (int j = 0; j < pArray[i].length; j++) {
				pArray[i][j] = (sizeDegreeValues[i] == 0) ? 0 : ((double) countValues[i][j]) / sizeDegreeValues[i];
			}
		}
		return pArray;
	}

	public static void mergeAddMatrix(double[][] baseMatrix, double[][] addMatrix, int[] degrees) {
		for (int i = 0; i < degrees.length; i++) {
			for (int j = 0; j < baseMatrix[i].length; j++) {
				if (degrees[i] < (addMatrix.length - 1))
					baseMatrix[i][j] += addMatrix[degrees[i]][j];
			}
		}
		for (int j = 0; j < baseMatrix[baseMatrix.length - 1].length; j++) {
			baseMatrix[baseMatrix.length - 1][j] += addMatrix[addMatrix.length - 1][j];
		}
	}

	public static void divMatrix(double[][] matrix, int valueDiv) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] /= valueDiv;
			}
		}
	}

	public static double[][] divMatrix(int[][] matrix, int valueDiv) {
		double[][] proportion = new double[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			proportion[i] = new double[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				proportion[i][j] = matrix[i][j] / (double) valueDiv;
			}
		}
		return proportion;
	}

	public static String toTrueTableFormat(int number, int size) {
		StringBuilder sb = new StringBuilder(Integer.toBinaryString(number));
		while (sb.length() < size)
			sb.insert(0, 0);
		return sb.substring(sb.length() - size, sb.length());
	}

	public static String toTrueTableFormat(long number, int size) {
		StringBuilder sb = new StringBuilder(Long.toBinaryString(number));
		while (sb.length() < size)
			sb.insert(0, 0);
		return sb.substring(sb.length() - size, sb.length());
	}
	public static String toTrueTableFormat(int number, int size, int sizeDiscreteValues) {
		StringBuilder sb = new StringBuilder(String.valueOf(dec2base(number, sizeDiscreteValues)));
		while (sb.length() < size)
			sb.insert(0, 0);
		return sb.substring(sb.length() - size, sb.length());
	}
	public static String toTrueTableFormat(long[] numbers, int size) {
		StringBuilder sb = new StringBuilder();
		if (size <= 64)
			return toTrueTableFormat(numbers[0], size);
		else
			for (long number : numbers) {
				sb.insert(0, " ").insert(0, toTrueTableFormat(number, 64));
			}
		return sb.toString();
	}

	public static int[] listToArrayInt(ArrayList<Integer> list) {
		return list.stream().mapToInt(Integer::intValue).toArray();
//		int[] array = new int[list.size()];
//		for (int i = 0; i < array.length; i++) {
//			array[i] = list.get(i);
//		}
//		return array;
	}

	public static int compareDouble(double double1, double double2) {
		boolean isDiff = (Math.abs(double1 - double2) <= 0.00000000000000001);
		if (isDiff)
			return 0;
		else
			return Double.compare(double1, double2);
	}

	public static int[] mergeTable(int[][] frequencyTable) {
		return Arrays.stream(frequencyTable).mapToInt(a -> IntStream.of(a).sum()).toArray();
//		int[] merge = new int[frequencyTable.length];
//		for (int i = 0; i < merge.length; i++) {
//			//merge[i] = frequencyTable[i][0] + frequencyTable[i][1];
//			merge[i] = IntStream.of(frequencyTable[i]).sum();
//		}
//		return merge;
	}

	public static boolean containsArray(ArrayList<long[][]> repFunctions, long[][] ls) {
		i_loop: for (int i = 0; i < repFunctions.size(); i++) {
			long[][] e = repFunctions.get(i);
			for (int j = 0; j < e.length; j++) {
				if (!Arrays.equals(e[j], ls[j]))
					continue i_loop;
			}
			return true;
		}
		return false;
	}

	public static boolean containsArray(ArrayList<long[]> repFunctions, long[] ls) {
		for (int j = 0; j < repFunctions.size(); j++) {
			if (Arrays.equals(repFunctions.get(j), ls))
				return true;
		}
		return false;
	}

	public static void main(String[] arg) {
		System.out.println(toTrueTableFormat(2, 2));
		System.out.println(toTrueTableFormat(2, 2, 2));
//		for (int i = 1; i < Resources.functionReduzible.length; i++) {
//			System.out.println();
//			System.out.println(Resources.functionReduzible[i].length);
//			long ant = 0;
//			for (int j = 0; j < Resources.functionReduzible[i].length; j++) {
//				System.out.println(j + " \t"
//						+ Resources.toTrueTableFormat(Resources.functionReduzible[i][j], (1 << (i + 1))) + "\t->"
//						+ Resources.functionReduzible[i][j] + "\t->" + (Resources.functionReduzible[i][j] - ant));
//				ant = Resources.functionReduzible[i][j];
//			}
//			System.out.println();
//		}
//		Random r = new Random();
//		System.out.println(r.nextInt(10));
//		int size = 9;
//		int gene = 7;
//		System.out.println(Arrays.stream(maskArrayCanalizant(size, gene)).mapToObj(Long::toBinaryString)
//				.collect(Collectors.joining(" ")));
		// System.out.println(Long.toBinaryString(maskCanalizant(size, gene)));

		// System.out.println(random.nextInt());

	}

	public static long[] andFunction(long[] a1, long[] a2) {
		long[] and = new long[a1.length];
		for (int i = 0; i < and.length; i++) {
			and[i] = a1[i] & a2[i];
		}
		return and;
	}

	public static long[] orFunction(long[] a1, long[] a2) {
		long[] or = new long[a1.length];
		for (int i = 0; i < or.length; i++) {
			or[i] = a1[i] | a2[i];
		}
		return or;
	}

	public static long[] notFunction(long[] a1) {
		long[] not = new long[a1.length];
		for (int i = 0; i < not.length; i++) {
			not[i] = ~a1[i];
		}
		return not;
	}

	public static double[] divArray(int[] array, int divisor) {
		double[] result = new double[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i] / (double) divisor;
		}
		return result;
	}

	public static long[] notFunction(long[] a1, int sizegens) {
		if (sizegens >= 6)
			return notFunction(a1);
		return new long[] { notFunction(a1)[0] & ((1 << (1 << sizegens)) - 1) };
	}

	public static String toString(List<?> list, String sep) {
		return list.stream().map(o -> o.toString()).collect(Collectors.joining(sep));
	}
	public static String toString(Deque<?> deque, String sep) {
		// TODO Auto-generated method stub
		return deque.stream().map(o -> o.toString()).collect(Collectors.joining(sep));
	}
	public static String toString(Object[] arr, String sep, int startInclusive, int endExclusive) {
		return Arrays.stream(arr, startInclusive, endExclusive).map(o -> o.toString()).collect(Collectors.joining(sep));
	}

	public static String toString(Object[] arr, String sep) {
		return toString(arr, sep, 0, arr.length);
	}

	public static <E> E choice(Collection<? extends E> coll, Random rand) {
		if (coll.size() == 0) {
			return null; // or throw IAE, if you prefer
		}

		int index = rand.nextInt(coll.size());
		if (coll instanceof List) { // optimization
			return ((List<? extends E>) coll).get(index);
		} else {
			Iterator<? extends E> iter = coll.iterator();
			for (int i = 0; i < index; i++) {
				iter.next();
			}
			return iter.next();
		}
	}

	public static int sum(int[] line) {
		return IntStream.of(line).sum();

	}

	public static int pow(int a, int b) {
		if (b == 0)
			return 1;
		if (b == 1)
			return a;
		if (a == 2)
			return 1 << b;
		if (b % 2 == 0)
			return pow(a * a, b / 2); // even a=(a^2)^b/2
		else
			return a * pow(a * a, b / 2); // odd a=a*(a^2)^b/2

	}

	public static int dec2base(int number, int base) {
		int baseNumber = 0, p = 1;
		while (number != 0) {
			baseNumber = baseNumber + p * (number % base);
			number = number / base;
			p = p * 10;
		}
		return baseNumber;
	}

	public static int[] statesBase(int sizeDiscretes, int sizePredictors) {
		int[] states = new int[pow(sizeDiscretes, sizePredictors)];
		for (int i = 0; i < states.length; i++) {
			states[i] = dec2base(i, sizeDiscretes);
		}
		return states;
	}

	public static int[] randomOrderSample(int[] array, int sizeSample) {
		TreeSet<Integer> ts=new TreeSet<>();
		for (int i = 0; i < sizeSample; i++) {
			if(!ts.add(array[random.nextInt(array.length)]))
				i--;
		}
		return ts.stream().mapToInt(Integer::intValue).toArray();
	}

	public static long toLong(boolean[] state) {
		long n=0;
		for (int i = 0; i < state.length; i++) {
			if(state[i])
				n= n | (1L<<i);//set 1 na posicao i nivel bit
			//para trocar o bit i
			//n=n ^ (1L<<i);
			//para set 0 se faz n = n & ~(1L<<i);
		}
		return n;
	}

	public static int getPosition( boolean[] visits, boolean b) {
		for (int i = 0; i < visits.length; i++) {
			if(visits[i]==b)return i;
		}
		return -1;
	}

	public static boolean[] toState(long longState, int nGenes) {
		boolean[] state=new boolean[nGenes];
		for (int i = 0; i < state.length; i++) {
			if((longState & (1L<<i))!=0)
				state[i]=true;
		}
		return state;
	}

	public static int getPosition(List<boolean[]> listState, boolean[] state) {
		for (int i = 0; i < listState.size(); i++) {
			if(Arrays.equals(listState.get(i),state))
				return i;
		}
		return -1;
	}

}
