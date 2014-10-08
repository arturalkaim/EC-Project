import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13 implements ContestSubmission {
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	boolean isMultimodal;
	boolean hasStructure;
	boolean isSeparable;

	double sigma;
	double ds_0;
	double ds;
	double nrank;
	double top_fit;

	int MAX_POP;

	public player13() {
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
		isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
		hasStructure = Boolean.parseBoolean(props
				.getProperty("GlobalStructure"));
		isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Change settings(?)
		if (isMultimodal) {
			MAX_POP = 300;
			sigma = 1;
			ds_0 = 0.5;
			nrank = 25.0;
		}else{
			MAX_POP = 200;
			sigma = 0.5;
			ds_0 = 0.25;
			nrank = 25.0;
		}
		
		if (hasStructure){
		
		}
		
		if (isSeparable){
			
		}
		
		ds = ds_0;
		
		
	}

	private ArrayList<Individue> CreatePopulation(int max) {

		ArrayList<Individue> popu = new ArrayList<Individue>();

		for (; popu.size() < max;) {
			double[] aux = (new double[] { (rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10,
					(rnd_.nextDouble() - 0.5) * 10 });

			Double mem = (Double) evaluation_.evaluate(aux);
			popu.add(new Individue(aux, mem));
			// System.out.println(mem);
		}

		return popu;
	}

	private void rankPopulation(ArrayList<Individue> popu) {
		Double low = Double.MAX_VALUE, high = Double.MIN_VALUE;
		
		for (Individue i : popu) {

			Double fit = i.getFitness();
			if (fit < low)
				low = fit;
			if (fit > high)
				high = fit;

		}

		double step = (high - low) / nrank;

		for (Individue j : popu) {

			Double fit = j.getFitness();

			for (int k = 0; k < nrank; k++) {

				if (low + step * k <= fit && fit < low + step * (k + 1))
					j.setRank(k + 1);

			}
		}

	}

	private ArrayList<Individue> SelectParents(ArrayList<Individue> popu) {

		rankPopulation(popu);
		ArrayList<Individue> parents = new ArrayList<Individue>();
		ArrayList<Double> prob = CalcProb(popu);

		int mu = popu.size();
		
		for (int j = 0, i = rnd_.nextInt(mu); j < mu; i = rnd_.nextInt(mu)) {

			double r = rnd_.nextDouble();
			if (r < prob.get(i)) {

				Individue mem = popu.get(i);
				// System.out.println(mem.getFitness());
				parents.add(j, mem);
				j++;

			}

		}

		return parents;

	}

	private ArrayList<Double> CalcProb(ArrayList<Individue> popu) {

		ArrayList<Double> prob = new ArrayList<Double>();
		double c = 0;
		
		// multimodal functions
		if(isMultimodal){
			int j = 0;
			double s = 1.5;
			double mu = nrank;
			
			for(Individue ind : popu){
			
				double i = ind.getRank();
				double p = ((2-s)/mu)+(2*i*(s-1)/(mu*(mu-1))); // lin prob function
				
				c += p;
				
				prob.add(j, p);
				
				j++;
			}
		}
		
		// unimodal functions
		if(!isMultimodal){
			int j = 0;
			for (Individue ind : popu) {
	
				double i = ind.getRank(); 
	
				double p = 1 - Math.exp(-i);
				c += p;
	
				prob.add(j, p);
	
				j++;
			}
		}
		
		double total = 0d;
		
		// normalize
		for (int k = 0; k < prob.size(); k++) {

			prob.set(k, prob.get(k) / c);
			total += prob.get(k);
		}

		// System.out.println("Total= "+ total+" PROB: "+prob);

		return prob;
	}

	private ArrayList<Individue> createChildren(ArrayList<Individue> parents) {

		ArrayList<Individue> children = new ArrayList<Individue>();
		int succes = 0;
		int nosucces = 0;

		for (int i = 0; i < parents.size() / 2; i++) {
			Individue parent1 = parents.get(i);
			Individue parent2 = parents.get(i + 1);

			// create two children
			double[] child1 = Mate(parent1, parent2);
			double[] child2 = Mate(parent1, parent2);

			// create two mutants
			double[] mutant1 = Mutate(child1);
			double[] mutant2 = Mutate(child2);

			double fc1 = (Double) evaluation_.evaluate(child1);
			double fm1 = (Double) evaluation_.evaluate(mutant1);

			if (fm1 >= fc1) {
				children.add(new Individue(mutant1, fm1));
				succes++;
			} else{
				children.add(new Individue(child1, fc1));
				nosucces++;
			}

			double fc2 = (Double) evaluation_.evaluate(child2);
			double fm2 = (Double) evaluation_.evaluate(mutant2);

			if (fm2 >= fc2) {
				children.add(new Individue(mutant2, fm2));
				succes++;
			} else{
				children.add(new Individue(child2, fc2));
				nosucces++;
			}

		}

		if (succes / parents.size() >= 0.2)
			sigma += ds;
		else
			sigma -= ds;

		//System.out.println(succes/nosucces);
		return children;

	}

	private double[] Mate(Individue parent1, Individue parent2) {

		double[] child = new double[10];

		for (int i = 0; i < parent1.getGen().length; i++) {
			
			// take variable random from each parent
			if(!isSeparable){
				double r = rnd_.nextDouble();
	
				if (r < 0.5)
					child[i] = parent1.getGen()[i];
				if (r >= 0.5)
					child[i] = parent2.getGen()[i];
			}
			
			// take average of variables
			if(isSeparable){
				
				child[i] = (parent1.getGen()[i] + parent2.getGen()[i])/2;
				
			}

		}

		return child;
	}

	private ArrayList<Individue> nextGeneration(ArrayList<Individue> popu,
			ArrayList<Individue> children) {

		// keep track of top fitness
		top_fit = evaluation_.getFinalResult();
		
		ArrayList<Individue> newpopu = new ArrayList<Individue>(popu);
		newpopu.addAll(children);
		
		Collections.sort(newpopu, new Comparator<Individue>() {

			@Override
			public int compare(Individue o1, Individue o2) {
				double d = o1.getFitness()-o2.getFitness();
				if(d<0)
					return -1;
				if(d==0)
					return 0;
				else
					return 1;
			}

		});
		
		/*rankPopulation(newpopu);
		ArrayList<Double> prob = CalcProb(newpopu);
		
		int mu = newpopu.size();
		for (int i = rnd_.nextInt(mu); newpopu.size() > MAX_POP; i = rnd_.nextInt(mu)){
		
			double fit = newpopu.get(i).getFitness();
			double r = rnd_.nextDouble();
			
			if (r < prob.get(i) && fit != top_fit) {
				
				
			}
			
			
			mu = newpopu.size();
		}*/
		
		
		

		return new ArrayList<Individue>(newpopu.subList(newpopu.size()-MAX_POP-1, newpopu.size()));
	}

	private double[] Mutate(double[] child) {

		double[] mutant = new double[10];

		for (int i = 0; i < child.length; i++) {
			
			// 20% chance of mutation for each variable
			if(rnd_.nextDouble() < 0.2){
				double dx = sigma * rnd_.nextGaussian();
				mutant[i] = child[i] + dx;
	
				if (mutant[i] > 5)
					mutant[i] = 5;
	
				if (mutant[i] < -5)
					mutant[i] = -5;
			}else{
				mutant[i] = child[i];
			}

		}

		return mutant;
	}

	public void run() {

		ArrayList<Individue> Population_List = CreatePopulation(MAX_POP);
		// System.out.println("end");

		boolean go = true;
		for (int evals = MAX_POP; evals < evaluations_limit_  && go; evals += 2*(MAX_POP)) {

			// System.out.println("Population_List.size= "+Population_List.size());
			ArrayList<Individue> parents = SelectParents(Population_List);
			ArrayList<Individue> children = createChildren(parents);
			Population_List = nextGeneration(Population_List, children);
			
			ds = ds_0 - (ds_0 - 0.01)*(evals/evaluations_limit_);

		}

		System.out.println("FinalResult= " + evaluation_.getFinalResult());

	}

	public static void main(String[] args) {
		player13 p13 = new player13();

		p13.setEvaluation(new SphereEvaluation());
		
		p13.run();

	}
}
