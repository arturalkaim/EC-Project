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

	public TreeMap<Double, double[]> selectParents(TreeMap<Double, double[]> population, double topParents){
		//Sorted List
		TreeMap<Double, double[]> selectedParents = new TreeMap<Double, double[]>();
		int parentsSelected = (int) (population.size()*topParents);
		
		//get the treeMap ordered by the greater to the lowest.
		SortedMap<Double, double[]> aux = population.descendingMap();
		//get the X greater fittness parents.
		for(int i = 0; i < parentsSelected; i++){
			Double keyAux = aux.firstKey();
			selectedParents.put(keyAux, aux.get(keyAux));
			aux = aux.tailMap(keyAux);
		}
		return selectedParents;
	}
	
	public void run() {
		// Run your algorithm here
		int popNumber = 100;
		int pop=0;
		TreeMap<Double, double[]> population  = new TreeMap<Double, double[]>();
		double[] newchild;
		while(pop < popNumber){
		 newchild = new double[] { rnd_.nextDouble()-0.5*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10 }; 
			
			population.put((Double) evaluation_.evaluate(newchild), newchild);
		}
		System.out.println(population);
		
		int evals = 0;
		while (evals < evaluations_limit_) {
			// Select parents
			TreeMap<Double, double[]> result =  selectParents(population, 0.5);
			System.out.println(result);
			// Apply variation operators and get children
			// double child[] = ...
			// Double fitness = evaluation_.evaluate(child);
			evals++;
			// Select survivors
		}

		System.out.println(evaluation_.getFinalResult());
	}

	public static void main(String[] args) {
		player13Rafael p13 = new player13Rafael();

		p13.setEvaluation(new SphereEvaluation());

		p13.run();

	}
}
