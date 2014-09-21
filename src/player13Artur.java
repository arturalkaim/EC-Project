import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13Artur implements ContestSubmission {
	private static final Double MIN_VALUE = -10000d;
	private static int MAX_POP = 10;
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
		TreeMap<Double, double[]> children = new TreeMap<Double, double[]>();
		TreeMap<Double, double[]> parents = new TreeMap<Double, double[]>();
		TreeMap<Double, double[]> aux = new TreeMap<Double, double[]>();

		buildRandomPopulation(population);

		int evals = 0;
		while (evals < 1000) {
			// System.out.println("GENERATION " + evals);
			// Select parents
			parents = selectParents(population, (double) evals);

			// Apply variation operators and get children
			children = variate(parents);

			population.putAll(children);
			// double child[] = ...
			// Double fitness = evaluation_.evaluate(child);
			for (double[] p : population.values()) {
				aux.put((Double) evaluation_.evaluate(p),p);
				
				evals++;
			}
			// Select survivors
			children.clear();
			population.clear();
			population.putAll(aux);
			System.out.println("POP "+population.keySet());
			System.out.println("EVALS:" + evals);
		}

		System.out.println(evaluation_.getFinalResult());
	}

	private TreeMap<Double, double[]> variate(
			TreeMap<Double, double[]> population) {
		TreeMap<Double, double[]> aux = new TreeMap<Double, double[]>();

//		buildRandomPopulation(population);
		if(population.size()%2!=0)
			population.put(-500d,new double[] { (rnd_.nextDouble() - 0.5) * 10,
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

		while (it.hasNext()) {
			Map.Entry<java.lang.Double, double[]> entry1 = (Map.Entry<java.lang.Double, double[]>) it
					.next();
			Map.Entry<java.lang.Double, double[]> entry2 = (Map.Entry<java.lang.Double, double[]>) it
					.next();

			mate(entry1.getValue(), entry2.getValue(), aux);
		}

//		buildRandomPopulation(aux);
		return aux;
	}

	private void mate(double[] ds, double[] ds2, TreeMap<Double, double[]> aux) {
		System.out.println("Mate");

		int pivot = rnd_.nextInt(10);
		double arr1[] = new double[10];
		for (int i = 0; i < pivot; i++) {
			arr1[i] = ds[i];
		}
		for (int i = pivot; i < 10; i++){
			arr1[i] = ds2[i];
		}
		System.out.println();
		double arr2[] = new double[10];
		for (int i = 0; i < pivot; i++)
			arr2[i] = ds2[i];
		for (int i = pivot; i < 10; i++)
			arr2[i] = ds[i];
		Double eval = (Double) evaluation_.evaluate(arr1);
		Double eval2 = (Double) evaluation_.evaluate(arr2);
		aux.put(eval, arr1);
		aux.put(eval2, arr2);
	}

	double sizeAux = -1;
	private TreeMap<Double, double[]> selectParents(
			TreeMap<Double, double[]> population, Double gen) {
		
		TreeMap<Double, double[]> aux = new TreeMap<Double, double[]>(
				population.tailMap(sizeAux));
		
		
		if(sizeAux<0)
			sizeAux++;
		
		System.out.println(aux.keySet());
		System.out.println("SIZE AUX: " + sizeAux);
		
		return aux;

	}

	private void buildRandomPopulation(TreeMap<Double, double[]> population) {
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
			population.put(val, aux);
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
