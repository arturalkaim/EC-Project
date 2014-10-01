

public class Individue{
	double[] gen;
	Double fitness;
	int rank;
	/**
	 * @param gen
	 * @param fitness
	 * @param rank
	 */
	public Individue(double[] gen, Double fitness) {
		super();
		this.gen = gen;
		this.fitness = fitness;
	}
	public double[] getGen() {
		return gen;
	}
	public void setGen(double[] gen) {
		this.gen = gen;
	}
	public Double getFitness() {
		return fitness;
	}
	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

/*	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	
}
