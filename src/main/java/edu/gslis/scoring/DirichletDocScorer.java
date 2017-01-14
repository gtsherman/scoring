package edu.gslis.scoring;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.searchhits.SearchHit;
import edu.gslis.textrepresentation.FeatureVector;

/**
 * Standard Dirichlet query likelihood scorer
 * 
 * @author Garrick
 *
 */
public class DirichletDocScorer implements DocScorer {
	
	private double mu = 2500.0;
	protected double epsilon = 1.0;
	
	private CollectionStats collectionStats;
	
	public DirichletDocScorer(CollectionStats collectionStats) {
		this.collectionStats = collectionStats;
	}

	public DirichletDocScorer(double mu, CollectionStats collectionStats) {
		this(collectionStats);
		this.mu = mu;
	}
	
	public DirichletDocScorer(double mu, double epsilon, CollectionStats collectionStats) {
		this(mu, collectionStats);
		this.epsilon = epsilon;
	}
	
	public double getMu() {
		return mu;
	}
	
	public CollectionStats getCollectionStats() {
		return collectionStats;
	}
	
	@Override
	public double scoreTerm(String term, SearchHit doc) {
		FeatureVector docVector = doc.getFeatureVector();
		double wordCount = docVector.getFeatureWeight(term);
		double docLength = docVector.getLength();
		double colProb = (epsilon + collectionStats.termCount(term)) / collectionStats.getTokCount();
		double score = (wordCount + mu * colProb) / (docLength + mu);
		return score;
	}

}
