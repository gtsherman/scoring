package edu.gslis.scoring;

public abstract class DocScorerWithDocumentPrior implements DocScorer {
	
	private DocScorer nonPriorScorer;
	
	/**
	 * @param nonPriorScorer A DocScorer that handles the non-prior component, e.g. P(Q|D).
	 */
	public DocScorerWithDocumentPrior(DocScorer nonPriorScorer) {
		this.nonPriorScorer = nonPriorScorer;
	}
	
	public abstract double getPrior();

	public double scoreTerm(String term) {
		return getPrior() * nonPriorScorer.scoreTerm(term);
	}

}
