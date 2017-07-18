package edu.gslis.scoring;

import edu.gslis.searchhits.SearchHit;

public abstract class DocScorerWithDocumentPrior implements DocScorer {
	
	private DocScorer nonPriorScorer;
	
	/**
	 * @param nonPriorScorer A DocScorer that handles the non-prior component, e.g. P(Q|D).
	 */
	public DocScorerWithDocumentPrior(DocScorer nonPriorScorer) {
		this.nonPriorScorer = nonPriorScorer;
	}
	
	public abstract double getPrior(SearchHit document);

	@Override
	public double scoreTerm(String term, SearchHit document) {
		return getPrior(document) * nonPriorScorer.scoreTerm(term, document);
	}

}
