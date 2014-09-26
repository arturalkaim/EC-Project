import java.util.Properties;
import java.util.Random;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.TreeMap;

public class player13alex implements ContestSubmission {
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	public player13alex() {
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
	
	private Treemap<double, double[]> CreatePopulation(int max){
		
		Treemap<Double, double[]> popu = new Treemap<Double, double[]>();
				
		for (int i = 0; popu.size() < max; i++) {
			double[] aux = (new double[] {
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
			
			popu.put((Double) evaluation_.evaluate(aux), aux);
		}
		
		return popu;
	}
	
	
	private Treemap<double[], double[]> SelectParens(Treemap<Double, double[]> popu){
		
		Treemap<double[], double[]> parents = new Treemap<double[], double[]>();
		
		// assign members rank instead of fitness, (certain ammount of ranks)
		// for i<popu.size()
		// pick set of parents using roulette based on rank
		
		return parents;
		
	}
	
	private Treemap<Double, double[]> createChildren(
							Treemap<Double, double[]> popu, Treemap<double[], double[]> parents){
		
		Treemap<Double, double[]> children = new Treemap<Double, double[]>();
		double[] aux = new double[]{};
		
		// create children
		// for i <parent.size()
		// take set of parents and form children
		
		aux = mutate(aux);
		
		list.put((Double) evaluation_.evaluate(aux), aux);
		
	}
	
	private Treemap<Double, double[]> nextGeneration(
							Treemap<Double, double[]> popu, Treemap<Double, double[]> children){
		
		// combine popu and children list
		// remove members with roulette based on rank/fitness
		// never remove highest fitness (elitism)
		
	}
	
	private double getMaxFitness(Treemap<Double, double[]> popu){
		
		// get the largest fitness from population list
		Double max_fitness = 1.0;
		
		
		return max_fitness;
		
	}
	
	private void Roulette(){
		
		// roulette function
		
	}
	
	private Treemap<Double, double[]> Mutate(double[] children){
	
		// increase mutation size if top_fitness doesn't change
		
	}
	
	public void run() {
		
		int MAX_POP = 50;
		
		// keep track of best fitness in population
		double[] top_fitness = new double[]{};
		
		Treemap<Double, double[]> Population_List = CreatePopulation(MAX_POP);
		
		boolean go = true;
		for(int evals = 0; evals < evaluation_limit_ && go; evals++ ){
			
			// keep track of best fitness in population
			top_fitness.add(getMaxFitness(Population_List));
						
			Treemap<double[], double[]> parents = selectParents(Population_List);
			Treemap<Double, double[]> children = createChildren(Population_List, parents);
			Population_List = nextGeneration(Population_List, children);
			
			
		}
		
	}

	public static void main(String[] args) {
		player13alex p13 = new player13alex();

		p13.setEvaluation(new SphereEvaluation());

		p13.run();

	}
}
