package edu.gslis.scoring;

import java.util.Map;

import edu.gslis.searchhits.SearchHit;

/**
 * Linearly combines other scores
 * @author Garrick
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
	
	@Override
	public double scoreTerm(String term, SearchHit document) {
		double score = 0.0;

		for (DocScorer scorer : scorers.keySet()) {
			double mixingWeight = scorers.get(scorer);
			score += mixingWeight * scorer.scoreTerm(term, document);
		}

		return score;
	}
	
}
