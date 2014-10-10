import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13alex implements ContestSubmission {
	private static final double DISTANCE_TO_MUTATE = 0.25;
	private Random rnd_;
	private ContestEvaluation evaluation_;
	private int evaluations_limit_;

	private int numberOfNichesFound = 0;
	double  f2limit = 6;

	boolean isMultimodal;
	boolean hasStructure;
	boolean isSeparable;

	double sigma;
	double ds_0;
	double ds;
	double temp_0;
	double temp;
	double nrank;
	double top_fit;

	int MAX_POP;

	boolean f1;
	boolean f2;
	boolean f3;

	public player13alex() {
		rnd_ = new Random();
	}

	public void setSeed(long seed) {
		// Set seed of algorithms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation) {
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
		evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
		hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
		isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		f1 = !isMultimodal;
		f2 = isMultimodal && !hasStructure;
		f3 = isMultimodal && hasStructure;
		
		// Change settings(?)
		if (f1) {
			MAX_POP = 50;
			sigma = 0.5;
			ds_0 = 1.5;
			//temp_0 = 34.8;
			nrank = MAX_POP / 2;
		}

		if (f2) {
			MAX_POP = 200;
			sigma = 2; // higher mutation at the beginning
			ds_0 = 1.5;
			//temp_0 = 34.8; // 75% of admittance for mutant with df = 10
			nrank = MAX_POP / 8; // less ranks more equal prob distribution
		}

		if (f3) {
			MAX_POP = 100;
			sigma = 0.5;
			ds_0 = 1.5;
			//temp_0 = 34.8;
			nrank = MAX_POP / 2;
		}

		ds = ds_0;
		temp = temp_0;

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

			if (high == low)
				j.setRank(1);
			else {
				Double fit = j.getFitness();

				for (int k = 0; k < nrank; k++) {

					if (low + step * k <= fit && fit < low + step * (k + 1))
						j.setRank(k + 1);

				}
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
		if (true) {
			int j = 0;
			double s = 1.5;
			double mu = nrank;

			for (Individue ind : popu) {

				double i = ind.getRank();
				double p = ((2 - s) / mu) + (2 * i * (s - 1) / (mu * (mu - 1))); // linear
																					// prob
																					// function

				c += p;

				prob.add(j, p);

				j++;
			}
		} else {
			int j = 0;
			for (Individue ind : popu) {

				double i = ind.getRank();

				double p = 1 - Math.exp(-i); // exponential prob function
				c += p;

				prob.add(j, p);

				j++;
			}
		}

		// double total = 0d;

		// normalize
		for (int k = 0; k < prob.size(); k++) {

			prob.set(k, prob.get(k) / c);
			// total += prob.get(k);
		}

		// System.out.println("Total= "+ total+" PROB: "+prob);

		return prob;
	}

	private ArrayList<Double> CalcProb2(ArrayList<Individue> popu) {

		ArrayList<Double> prob = new ArrayList<Double>();
		double c = 0;

		// multimodal functions
		if (true) {
			int j = 0;
			double s = 1.5;
			double mu = nrank;

			for (Individue ind : popu) {

				double i = ind.getRank();
				double p = ((2 - s) / mu)
						+ (2 * (mu - i) * (s - 1) / (mu * (mu - 1))); // lin
																		// prob
																		// function

				c += p;

				prob.add(j, p);

				j++;
			}
		} else {
			int j = 0;
			for (Individue ind : popu) {

				double i = ind.getRank();

				double p = 1 - Math.exp(-(nrank - i));
				c += p;

				prob.add(j, p);

				j++;
			}
		}

		// double total = 0d;

		// normalize
		for (int k = 0; k < prob.size(); k++) {

			prob.set(k, prob.get(k) / c);
			// total += prob.get(k);
		}

		// System.out.println("Total= "+ total+" PROB: "+prob);

		return prob;
	}

	private boolean checkNiche(ArrayList<Individue> parents, double[] child1) {
		boolean result = false;
		double distance = 0;
		for (int i = 0; i < parents.size(); i++) {
			for (int j = 0; j < 10; j++) {
				distance += Math.pow((child1[j] - parents.get(i).getGen()[j]),
						2);
			}
			distance = Math.sqrt(distance);

			if (distance < 1) {
				System.out.println("distance is  " + distance);
			}

			if (distance < DISTANCE_TO_MUTATE) {
				result = true;
				System.out.println("found it");
				numberOfNichesFound++;

				break;
			}
		}

		return result;
	}

	private ArrayList<Individue> createChildren(ArrayList<Individue> parents) {

		ArrayList<Individue> children = new ArrayList<Individue>();
		int succes = 0;
		// int nosucces = 0;

		for (int i = 0; i < parents.size() / 2; i++) {
			Individue parent1 = parents.get(i);
			Individue parent2 = parents.get(i + 1);

			// create two children
			double[] child1 = Mate(parent1, parent2);
			double[] child2 = Mate(parent1, parent2);

			// create two mutants
			double[] mutant1 = Mutate(child1);
			double[] mutant2 = Mutate(child2);

			if (f2 && evaluation_.getFinalResult() < f2limit) {

				while (checkNiche(parents, mutant1))
					mutant1 = Mutate(mutant1);

				while (checkNiche(parents, mutant2))
					mutant2 = Mutate(mutant2);
				
				double fm1 = (Double) evaluation_.evaluate(mutant1);
				double fm2 = (Double) evaluation_.evaluate(mutant2);
				
				children.add(new Individue(mutant1, fm1));
				children.add(new Individue(mutant2, fm2));
				
				

			} else {

				double fc1 = (Double) evaluation_.evaluate(child1);
				double fm1 = (Double) evaluation_.evaluate(mutant1);

				double fc2 = (Double) evaluation_.evaluate(child2);
				double fm2 = (Double) evaluation_.evaluate(mutant2);

				if (fm1 > fc1) {
					succes++;
					children.add(new Individue(mutant1, fm1));
				} else {
					children.add(new Individue(child1, fc1));
				}

				if (fm2 > fc2) {
					succes++;
					children.add(new Individue(mutant2, fm2));
				} else {
					children.add(new Individue(child2, fc2));
				}
				
				if ((double) succes / parents.size() >= 0.2)
					sigma = sigma * ds;
				else
					sigma = sigma / ds;			
				
				
			}

		}

		

		// System.out.println(sigma);
		return children;

	}

	private double[] Mate(Individue parent1, Individue parent2) {

		double[] child = new double[10];

		for (int i = 0; i < parent1.getGen().length; i++) {

			// take variable random from each parent

			double r = rnd_.nextDouble();

			if (r < 0.5)
				child[i] = parent1.getGen()[i];

			if (r >= 0.5)
				child[i] = parent2.getGen()[i];

		}

		return child;
	}

	private ArrayList<Individue> nextGeneration(ArrayList<Individue> popu,
			ArrayList<Individue> children) {

		ArrayList<Individue> newpopu = new ArrayList<Individue>(popu);
		newpopu.addAll(children);

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

		// double mid_fit = newpopu.get((int)
		// newpopu.size()*3/4-1).getFitness();
		// System.out.println(mid_fit);

		if (f2) {
			rankPopulation(newpopu);
			ArrayList<Double> prob = CalcProb2(newpopu); // calcprob2 gives
															// inverse
															// probability

			int mu = newpopu.size();
			for (int i = rnd_.nextInt(mu); mu > MAX_POP; i = rnd_.nextInt(mu)) {

				Individue mem = newpopu.get(i);
				double r = rnd_.nextDouble();
				// System.out.println(fit);
				// System.out.println(mid_fit);

				if (r < prob.get(i) && i != newpopu.size() - 1) {

					newpopu.remove(mem);

				}

				mu = newpopu.size();
			}

		} else {

			newpopu = new ArrayList<Individue>(newpopu.subList(newpopu.size()
					- MAX_POP, newpopu.size()));

		}

		return newpopu;

	}

	private double[] Mutate(double[] child) {

		double[] mutant = new double[10];

		for (int i = 0; i < child.length; i++) {

			// 20% chance of mutation for each variable
			if (rnd_.nextDouble() < 0.2) {
				double dx = sigma * rnd_.nextGaussian();
				mutant[i] = child[i] + dx;

				if (mutant[i] > 5)
					mutant[i] = 5;

				if (mutant[i] < -5)
					mutant[i] = -5;
			} else {
				mutant[i] = child[i];
			}

		}

		return mutant;
	}

	public void run() {

		ArrayList<Individue> Population_List = CreatePopulation(MAX_POP);
		// System.out.println("end");

		boolean go = true;
		for (int evals = MAX_POP; evals < evaluations_limit_ && go; evals += 2 * (MAX_POP)) {

			// System.out.println("Population_List.size= "+Population_List.size());
			ArrayList<Individue> parents = SelectParents(Population_List);
			ArrayList<Individue> children = createChildren(parents);
			Population_List = nextGeneration(Population_List, children);

			ds = ds_0 - (ds_0 - 1.001)* ((double) evals / evaluations_limit_);

			temp = temp_0 - (temp_0 - 2.18)
					* ((double) evals / evaluations_limit_);

		}

		System.out.println("FinalResult= " + evaluation_.getFinalResult());

	}

	public static void main(String[] args) {
		player13alex p13 = new player13alex();

		p13.setEvaluation(new SphereEvaluation());

		p13.run();

	}
}
