package edu.gslis.scoring;

import java.util.Map;

/**
 * Linearly combines other scores, e.g. (1-lam)P(q|D) + lam*Sum_e P(q|e)P(e|D)
 * @author garrick
 *
 */
public class InterpolatedDocScorer implements DocScorer {
	
	private Map<DocScorer, Double> scorers;

	/**
	 * @param scorers A map linking a DocScorer with its mixing weight. It is the caller's job to enforce any mixing weight constraints.
	 */
	public InterpolatedDocScorer(Map<DocScorer, Double> scorers) {
		this.scorers = scorers;
	}
	
	public double scoreTerm(String term) {
		double score = 0.0;

		for (DocScorer scorer : scorers.keySet()) {
			double mixingWeight = scorers.get(scorer);
			score += mixingWeight * scorer.scoreTerm(term);
		}

		return score;
	}
	
}
