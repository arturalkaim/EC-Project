import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13Artur implements ContestSubmission {
	private static final Double MIN_VALUE = -10000d;
	private static double PERC_BEST = 0.6;
	private static float MUT_PER = 1f;
	private static int MAX_POP = 150;
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	public player13Artur() {
		rnd_ = new Random();
	}

	public void setSeed(long seed) {
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation) {
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
		evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		boolean isMultimodal = Boolean.parseBoolean(props
				.getProperty("Multimodal"));
		boolean hasStructure = Boolean.parseBoolean(props
				.getProperty("GlobalStructure"));
		boolean isSeparable = Boolean.parseBoolean(props
				.getProperty("Separable"));

		// Change settings(?)
		if (isMultimodal) {
			// Do sth
		} else {
			// Do sth else
		}
	}

	public void run() {
		// Run your algorithm here
		TreeMap<Double, double[]> population = new TreeMap<Double, double[]>();
		ArrayList<double[]> children = new ArrayList<double[]>();
		TreeMap<Double, double[]> parents = new TreeMap<Double, double[]>();
		TreeMap<Double, double[]> aux = new TreeMap<Double, double[]>();

		buildRandomPopulation(population, true);

		System.out.println("POP " + population.descendingKeySet().headSet(0d));

		int evals = MAX_POP;
		boolean go = true;
		while (evals < evaluations_limit_ && go) {
			// System.out.println("GENERATION " + evals);
			// Select parents
			parents = selectParents(population, (double) evals);

			// Apply variation operators and get children
			children = new ArrayList<double[]>();
			children = variate(parents);

			// double child[] = ...
			// Double fitness = evaluation_.evaluate(child);
			for (double[] p : children) {
				Double val = (Double) evaluation_.evaluate(p);
				if (val == null) {
					go = false;
					break;
				}
				/*if (val > 7) {
					for (double d : p)
						System.out.print(d + " ");
					System.out.println();
				}*/
				aux.put(val, p);
				evals++;
			}
			// Select survivors
			children.clear();
			population.putAll(aux);
			aux.clear();
			/*
			 * if (evals % 10000 == 0) System.out.println("POP " +
			 * population.descendingKeySet().headSet(0d));
			 */
			// System.out.println("EVALS:" + evals);
		}

		System.out.println(evaluation_.getFinalResult());
	}

	private ArrayList<double[]> variate(TreeMap<Double, double[]> population) {
		ArrayList<double[]> aux = new ArrayList<double[]>();

		// buildRandomPopulation(population);
		if (population.size() % 2 != 0)
			population.put(-500d, new double[] {
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10 });

		Iterator<Entry<Double, double[]>> it = population.entrySet().iterator();
		Map.Entry<java.lang.Double, double[]> entry1 = null;
		if (it.hasNext())
			entry1 = (Map.Entry<java.lang.Double, double[]>) it.next();
		while (it.hasNext()) {
			Map.Entry<java.lang.Double, double[]> entry2 = (Map.Entry<java.lang.Double, double[]>) it
					.next();
			mate(entry1.getValue(), entry2.getValue(), aux);
			if (it.hasNext())
				entry1 = (Map.Entry<java.lang.Double, double[]>) it.next();
			mate(entry2.getValue(), entry1.getValue(), aux);

		}

		buildRandomKids(aux);
		return aux;
	}

	private void buildRandomKids(ArrayList<double[]> aux) {
		for (; aux.size() < MAX_POP;) {
			double[] aux1 = new double[] { (rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10 };
			aux.add(aux1);
		}
	}

	/*private void mate(double[] ds, double[] ds2, ArrayList<double[]> aux) {

		int pivot = rnd_.nextInt(10);
		double arr1[] = new double[10];
		for (int i = 0; i < pivot; i++) {
			arr1[i] = ds[i];
		}
		for (int i = pivot; i < 10; i++) {
			arr1[i] = ds2[i];
		}
		double arr2[] = new double[10];
		for (int i = 0; i < pivot; i++)
			arr2[i] = ds2[i];
		for (int i = pivot; i < 10; i++)
			arr2[i] = ds[i];

		mutation(arr1);
		mutation(arr2);

		aux.add(arr1);
		aux.add(arr2);
	}*/
	/* private void mate1(double[] ds, double[] ds2, ArrayList<double[]> aux) {
	// a1 b2 b3 a4
	int pivot = rnd_.nextInt(10);
	int length = rnd_.nextInt(10-pivot);
	
	
	double arr1[] = new double[10];
	for (int i = 0; i < pivot; i++) {
		arr1[i] = ds[i];
	}
	for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
		arr1[i] = ds2[i];
	}
	for (int i = pivot + length; i < 10; i++) {
		arr1[i] = ds[i];
	}

	double arr2[] = new double[10];
	for (int i = 0; i < pivot; i++)
		arr2[i] = ds2[i];
	for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
		arr2[i] = ds[i];
	}
	for (int i = pivot + length; i < 10; i++)
		arr2[i] = ds2[i];

	//mutation(arr1);
	//mutation(arr2);

	aux.add(arr1);
	aux.add(arr2);
}*/

	private void mate(double[] ds, double[] ds2, ArrayList<double[]> aux) {
		// a4 b2 b3 a1
	int pivot = rnd_.nextInt(10);
	int length = rnd_.nextInt(10-pivot);
	
	//System.out.println("IM HERE");
	double arr1[] = new double[10];
	for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
		arr1[i] = ds2[i];
	}
	for (int i = pivot + length; i < 10; i++) {
		arr1[i] = ds[i-pivot-length];
	}
	for (int i = 0; i < pivot; i++) {
		arr1[i] = ds[i];
	}

	double arr2[] = new double[10];
	for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
		arr2[i] = ds[i];
	}	
	for (int i = pivot + length; i < 10; i++)
		arr2[i] = ds2[i-pivot-length];
	for (int i = 0; i < pivot; i++)
		arr2[i] = ds2[i];
	mutation(arr1);
	mutation(arr2);

	aux.add(arr1);
	aux.add(arr2);
}
	private void mutation(double[] arr) {

		if (rnd_.nextFloat() < MUT_PER) {

			int from = rnd_.nextInt(10);
			double aux = arr[from];
			int to = rnd_.nextInt(10);
			arr[from] = arr[to];
			arr[to] = arr[from];
			arr[rnd_.nextInt(10)] = (rnd_.nextFloat()-0.5f)*10;

		}

	}
	private void mutation2(double[] arr) {
		double x = 0.5;
		double[] aux = new double[] { rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x,
				rnd_.nextGaussian()*x};
		for(int i = 0; i <10; i++){
			arr[i] += aux[i];
		}

	}
	
	
	

	double sizeAux = -1;

	private TreeMap<Double, double[]> selectParents(
			TreeMap<Double, double[]> population, Double gen) {

		TreeMap<Double, double[]> aux = new TreeMap<Double, double[]>(
				/* private void mate(double[] ds, double[] ds2, ArrayList<double[]> aux) {

				int pivot = rnd_.nextInt(10);
				int length = rnd_.nextInt(10-pivot);
				
				
				double arr1[] = new double[10];
				for (int i = 0; i < pivot; i++) {
					arr1[i] = ds[i];
				}
				for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
					arr1[i] = ds2[i];
				}
				for (int i = pivot + length; i < 10; i++) {
					arr1[i] = ds[i];
				}

				double arr2[] = new double[10];
				for (int i = 0; i < pivot; i++)
					arr2[i] = ds2[i];
				for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
					arr2[i] = ds[i];
				}
				for (int i = pivot + length; i < 10; i++)
					arr2[i] = ds2[i];

				//mutation(arr1);
				//mutation(arr2);

				aux.add(arr1);
				aux.add(arr2);
			}*/		population.tailMap(0d));

		Iterator<Entry<Double, double[]>> it = population.entrySet().iterator();
		sizeAux = aux.size();
		if (sizeAux > 0) {
			for (int i = aux.size(); i < MAX_POP * PERC_BEST && it.hasNext(); i++) {
				Entry<Double, double[]> next = it.next();
				aux.put(next.getKey(), next.getValue());
			}
			//System.out.println(aux.descendingKeySet());
			buildRandomPopulation(aux, false);
			PERC_BEST += 0.05d;
			MUT_PER = (float) (2.8f / sizeAux);
		}

		// System.out.println(aux.keySet());
		// System.out.println("SIZE AUX: " + aux.size());

		return aux;

	}

	private void buildRandomPopulation(TreeMap<Double, double[]> population,
			boolean eval) {
		Double val = MIN_VALUE;
		for (; population.size() < MAX_POP; val++) {
			double[] aux = new double[] { (rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10 };
			if (eval) {
				population.put((Double) evaluation_.evaluate(aux), aux);
			} else {
				population.put(val, aux);
			}
		}

	}

	public static void main(String[] args) {
		player13Artur p13 = new player13Artur();

		for (int i = 0; i < 1; i++) {
			p13.setEvaluation(new SphereEvaluation());
			p13.run();

		}

	}
}
