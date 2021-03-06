package edu.gslis.scoring.expansion;

import edu.gslis.scoring.DocScorer;
import edu.gslis.searchhits.SearchHit;

/**
 * Computes the relevance model score for a given term.
 * @author Garrick
 *
 */
public class RelevanceModelScorer implements DocScorer{
	
	private DocScorer termScorer;
	private double queryWeight;
	
	/**
	 * @param termScorer Some DocScorer capable of producing P(w|D).
	 * @param queryWeight The query weight, P(Q|D), probably given by a QueryScorer. Careful not to provide a logarithm!
	 */
	public RelevanceModelScorer(DocScorer termScorer, double queryWeight) {
		this.termScorer = termScorer;
		this.queryWeight = queryWeight;
	}

	@Override
	public double scoreTerm(String term, SearchHit document) {
		return termScorer.scoreTerm(term, document) * queryWeight;
	}

}
