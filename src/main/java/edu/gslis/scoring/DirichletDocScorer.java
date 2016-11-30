package edu.gslis.scoring;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.searchhits.SearchHit;
import edu.gslis.textrepresentation.FeatureVector;

/**
 * Standard Dirichlet query likelihood scorer, i.e. P(q|D)
 * 
 * @author garrick
 *
 */
public class DirichletDocScorer implements DocScorer {
	
	protected double mu = 2500.0;
	protected double epsilon = 1.0;
	
	private SearchHit doc;
	private CollectionStats collectionStats;
	
	public DirichletDocScorer(SearchHit doc, CollectionStats collectionStats) {
		setDoc(doc);
		setCollectionStats(collectionStats);
	}

	public DirichletDocScorer(double mu, SearchHit doc, CollectionStats collectionStats) {
		this(doc, collectionStats);
		setMu(mu);
	}
	
	public DirichletDocScorer(double mu, double epsilon, SearchHit doc, CollectionStats collectionStats) {
		this(mu, doc, collectionStats);
		this.epsilon = epsilon;
	}
	
	public void setDoc(SearchHit doc) {
		this.doc = doc;
	}
	
	public void setMu(double mu) {
		this.mu = mu;
	}
	
	public void setCollectionStats(CollectionStats cs) {
		this.collectionStats = cs;
	}
	
	public double scoreTerm(String term) {
		FeatureVector docVector = doc.getFeatureVector();
		double wordCount = docVector.getFeatureWeight(term);
		double docLength = docVector.getLength();
		double colProb = (epsilon + collectionStats.termCount(term)) / collectionStats.getTokCount();
		double score = (wordCount + mu * colProb) / (docLength + mu);
		return score;
	}

}
