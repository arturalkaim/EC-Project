import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13Rafael implements ContestSubmission {
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	public player13Rafael() {
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

	public ArrayList<Individue> selectParents(ArrayList<Individue> population, double topParents){
		//Sorted List
		ArrayList<Individue> selectedParents = new ArrayList<Individue>();
		int parentsSelected = (int) (population.size()*topParents);
		

		return selectedParents;
	}
	
	public void run() {
		// Run your algorithm here
		int popNumber = 100;
		int pop=0;
		ArrayList<Individue> population  = new ArrayList<Individue>();
		double[] newchild;
		while(pop < popNumber){
		 newchild = new double[] { rnd_.nextDouble()-0.5*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10 }; 
			if( null != (Double) evaluation_.evaluate(newchild))
				population.add(new Individue(newchild, ((Double) evaluation_.evaluate(newchild))));
			else{
				//System.out.println(pop);
			}
			pop++;
		}
		//System.out.println(population);
		
		int evals = 0;
		while (evals < evaluations_limit_) {
			// Select parents
			ArrayList<Individue> result =  selectParents(population, 0.5);
			System.out.println(result);
			// Apply variation operators and get children
			// double child[] = ...
			// Double fitness = evaluation_.evaluate(child);
			evals++;
			// Select survivors
		}

		System.out.println(evaluation_.getFinalResult());
	}

	
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
	}*/
	
	/*private void mate2(double[] ds, double[] ds2, ArrayList<double[]> aux) {
		// a4 b2 b3 a1
	int pivot = rnd_.nextInt(10);
	int length = rnd_.nextInt(10-pivot);
	
	
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
}*/
	private static float MUT_PER = 1f;
	private void mutation(double[] arr) {

		if (rnd_.nextFloat() < MUT_PER) {

			int from = rnd_.nextInt(10);
			double aux = arr[from];
			int to = rnd_.nextInt(10);
			arr[from] = arr[to];
			arr[to] = arr[from];
			arr[rnd_.nextInt(10)] = (rnd_.nextFloat()-0.5f)*10;

		}
		System.out.println("IM HERE");
	}
	
	public static void main(String[] args) {
		player13Rafael p13 = new player13Rafael();

		p13.setEvaluation(new SphereEvaluation());

		p13.run();

	}
	
}
