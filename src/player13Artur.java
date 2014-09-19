import java.util.Properties;
import java.util.Random;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

public class player13Artur implements ContestSubmission {
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

		int evals = 0;
		while (evals < evaluations_limit_) {
			System.out.println(evaluation_.evaluate(new double[] { (rnd_.nextDouble()-0.5)*10 ,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10,
					(rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10, (rnd_.nextDouble()-0.5)*10 }));
			// Select parents
			// Apply variation operators and get children
			// double child[] = ...
			// Double fitness = evaluation_.evaluate(child);
			evals++;
			// Select survivors
		}

		System.out.println(evaluation_.getFinalResult());
	}

	public static void main(String[] args) {
		player13Artur p13 = new player13Artur();

		p13.setEvaluation(new SphereEvaluation());

		p13.run();

	}
}
