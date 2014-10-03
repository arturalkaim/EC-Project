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

	double sigma = 5;
	double ds_0 = 1;
	double ds;

	int MAX_POP;
	private boolean separable;

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
			MAX_POP = 100;
		} else {
			MAX_POP = 50;
		}
		if (isSeparable) {
			separable = true;
		} else {
			separable = false;
		}
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
		double interval = 5.0;

		for (Individue i : popu) {

			Double fit = i.getFitness();
			if (fit < low)
				low = fit;
			if (fit > high)
				high = fit;

		}

		double step = (high - low) / interval;

		for (Individue j : popu) {

			Double fit = j.getFitness();

			for (int k = 0; k < interval; k++) {

				if (low + step * k <= fit && fit <= low + step * (k + 1))
					j.setRank(k + 1);

			}
		}

	}

	private ArrayList<Individue> SelectParents(ArrayList<Individue> popu) {

		rankPopulation(popu);
		ArrayList<Individue> parents = new ArrayList<Individue>();
		ArrayList<Double> prob = CalcProb(popu);


		int mu = popu.size();
		// double r = rnd_.nextDouble()/mu;

		for (int j = 0, i = rnd_.nextInt(mu); j < mu; i = (i + 1) % mu) {

			// System.out.println("START");
			double r = rnd_.nextDouble();
			if (r < prob.get(i)) {

				Individue mem = popu.get(i);
				// System.out.println(mem.getFitness());
				parents.add(j, mem);
				j++;

			}
			// System.out.println("END " + r + "   " + prob.get(i));

		}

		return parents;

	}

	private ArrayList<Double> CalcProb(ArrayList<Individue> popu) {

		ArrayList<Double> prob = new ArrayList<Double>();
		double s = 1.5;
		double c = 0;
		int mu = popu.size();

		int j = 0;
		for (Individue ind : popu) {

			double i = ind.getRank(); // or rank based

			double p = 1 - Math.exp(-i);
			c += p;

			prob.add(j, p);

			j++;
		}
		double total = 0d;
//		System.out.println("Total= " + total + " c = " + c + " PROB: " + prob);
		// normalize
		for (int k = 0; k < prob.size(); k++) {

			prob.set(k, prob.get(k) / c);
			total += prob.get(k);
		}

		return prob;
	}

	private ArrayList<Individue> createChildren(ArrayList<Individue> parents) {

		ArrayList<Individue> children = new ArrayList<Individue>();
		int succes = 0;

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
			} else
				children.add(new Individue(child1, fc1));

			double fc2 = (Double) evaluation_.evaluate(child2);
			double fm2 = (Double) evaluation_.evaluate(mutant2);

			if (fm2 >= fc2) {
				children.add(new Individue(mutant2, fm2));
				succes++;
			} else
				children.add(new Individue(child2, fc2));

		}

		if (sigma < 0)
			sigma = Math.abs(sigma);
		if (2 * succes / parents.size() >= 0.3)
			sigma += ds;
		else
			sigma -= ds;

		return children;

	}

	private double[] Mate(Individue parent1, Individue parent2) {

		double[] child = new double[10];
		if (true/*!separable*/) {
			int pivot = rnd_.nextInt(10);
			int length = rnd_.nextInt(10 - pivot);

			// System.out.println("IM HERE");
			for (int i = pivot, j = 1; i < 10 && j < length; i++, j++) {
				child[i] = parent2.getGen()[i];
			}
			for (int i = pivot + length; i < 10; i++) {
				child[i] = parent1.getGen()[i - pivot - length];
			}
			for (int i = 0; i < pivot; i++) {
				child[i] = parent1.getGen()[i];
			}

		} else
			for (int i = 0; i < parent1.getGen().length; i++) {
				int r = rnd_.nextInt(2);

				if (r == 0)
					child[i] = parent1.getGen()[i];
				if (r == 1)
					child[i] = parent2.getGen()[i];

			}

		return child;
	}

	private ArrayList<Individue> nextGeneration(ArrayList<Individue> popu,
			ArrayList<Individue> children) {

		ArrayList<Individue> newpopu = new ArrayList<Individue>(popu);
		newpopu.addAll(children);
		// combine popu and children list
		// remove members with roulette based on rank/fitness
		// never remove highest fitness (elitism)

		Collections.sort(newpopu, new Comparator<Individue>() {

			@Override
			public int compare(Individue o1, Individue o2) {
				double d = o1.getFitness() - o2.getFitness();
				if (d < 0)
					return -1;
				if (d == 0)
					return 0;
				else
					return 1;
			}

		});

		ds -= (ds_0 - 0.01) / evaluations_limit_;

		return new ArrayList<Individue>(newpopu.subList(newpopu.size()
				- MAX_POP - 1, newpopu.size()));
	}

	private double[] Mutate(double[] child) {

		double[] mutant = new double[10];

		for (int i = 0; i < child.length; i++) {

			double dx = sigma * rnd_.nextGaussian();
			mutant[i] = child[i] + dx;

			if (mutant[i] > 5)
				mutant[i] = 5;

			if (mutant[i] < -5)
				mutant[i] = -5;

		}

		return mutant;
	}

	public void run() {

		ds = ds_0;

		ArrayList<Individue> Population_List = CreatePopulation(MAX_POP);
		// System.out.println("end");

		boolean go = true;
		for (int evals = MAX_POP; evals < evaluations_limit_ && go; evals += (2 * (MAX_POP + 1))) {
			// System.out.println("Population_List.size= "+Population_List.size());
			ArrayList<Individue> parents = SelectParents(Population_List);
			ArrayList<Individue> children = createChildren(parents);
			Population_List = nextGeneration(Population_List, children);
			calcAverage(Population_List);
			
			if (evaluation_.getFinalResult()==10d) {
				return;
			}

		}
		System.out.println("Sigma= " + sigma);
		// System.out.println("FinalResult= " + evaluation_.getFinalResult());

	}

	private Double calcAverage(ArrayList<Individue> population_List) {
		Double average = 0d;
		int cont = 0;
		for (Individue i : population_List) {
			average += i.getFitness();
			cont++;
		}
		average = average/cont;
		System.out.println("Average = " + average);
		return average;
	}

	public static void main(String[] args) {
		player13 p13 = new player13();
		Double best = 0d;
		Double lowest = 11d;
		Double result = 0d;
		final int runs = 10;
		for (int i = 0; i < runs; i++) {
			p13.setEvaluation(new SphereEvaluation());
			p13.run();
			Double res = p13.evaluation_.getFinalResult();
			if (res > best)
				best = res;
			if (res < lowest)
				lowest = res;
			result += res;
			System.out.println("END");
		}
		System.out.println("Average = " + result / runs + " Best = " + best
				+ " Lowest = " + lowest);
	}
}
