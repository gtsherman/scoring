package edu.gslis.scoring;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.searchhits.SearchHit;
import edu.gslis.textrepresentation.FeatureVector;

public class JelinekMercerDocScorer implements DocScorer {
	
	private CollectionStats collectionStats;
	private double collectionWeight;
	
	private double epsilon = 1.0;
	
	public JelinekMercerDocScorer(CollectionStats collectionStats, double collectionWeigh) {
		this.collectionStats = collectionStats;
		this.collectionWeight = collectionWeigh;
	}

	public JelinekMercerDocScorer(double epsilon, CollectionStats collectionStats, double smoothingParam) {
		this(collectionStats, smoothingParam);
		this.epsilon = epsilon;
	}

	@Override
	public double scoreTerm(String term, SearchHit document) {
		FeatureVector docVector = document.getFeatureVector();
		double wordCount = docVector.getFeatureWeight(term);
		double docLength = docVector.getLength() == 0 ? epsilon : docVector.getLength();
		double docProb = wordCount / docLength;
		
		double colProb = (epsilon + collectionStats.termCount(term)) / collectionStats.getTokCount();

		double score = (1 - collectionWeight) * docProb + collectionWeight * colProb;
		return score;
	}

}
