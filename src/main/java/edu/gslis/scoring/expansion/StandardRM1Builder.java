package edu.gslis.scoring.expansion;

import java.util.Iterator;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.queries.GQuery;
import edu.gslis.scoring.DirichletDocScorer;
import edu.gslis.scoring.DocScorer;
import edu.gslis.scoring.queryscoring.QueryLikelihoodQueryScorer;
import edu.gslis.scoring.queryscoring.QueryScorer;
import edu.gslis.searchhits.SearchHit;
import edu.gslis.searchhits.SearchHits;
import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;

/**
 * Builds an RM1 for a given query.
 * @author Garrick
 *
 */
public class StandardRM1Builder implements RM1Builder {
	
	public static final int DEFAULT_FEEDBACK_DOCS = 20;
	public static final int DEFAULT_FEEDBACK_TERMS = 20;
	
	private int feedbackDocs;
	private int feedbackTerms;
	
	private DirichletDocScorer docScorer; 
	private DirichletDocScorer zeroMuDocScorer; 
	
	public StandardRM1Builder(CollectionStats collectionStats) {
		this(DEFAULT_FEEDBACK_DOCS, DEFAULT_FEEDBACK_TERMS, collectionStats);
	}

	public StandardRM1Builder(int feedbackDocs, int feedbackTerms, CollectionStats collectionStats) {
		setFeedbackDocs(feedbackDocs);
		setFeedbackTerms(feedbackTerms);
		createDocScorers(collectionStats);
	}

	private void createDocScorers(CollectionStats collectionStats) {
		docScorer = new DirichletDocScorer(collectionStats);
		zeroMuDocScorer = new DirichletDocScorer(0, collectionStats);
	}
	
	public void setFeedbackDocs(int feedbackDocs) {
		this.feedbackDocs = feedbackDocs;
	}
	
	public void setFeedbackTerms(int feedbackTerms) {
		this.feedbackTerms = feedbackTerms;
	}
	
	@Override
	public FeatureVector buildRelevanceModel(GQuery query, SearchHits initialResults) {
		return buildRelevanceModel(query, initialResults, null);
	}

	@Override
	public FeatureVector buildRelevanceModel(GQuery query, SearchHits initialResults, Stopper stopper) {
		FeatureVector termScores = new FeatureVector(stopper);
		
		int i = 0;
		Iterator<SearchHit> hitIt = initialResults.iterator();
		while (hitIt.hasNext() && i < feedbackDocs) {
			SearchHit hit = hitIt.next();
			i++;
			
			// Prep the scorers
			QueryScorer queryScorer = new QueryLikelihoodQueryScorer(docScorer);
			DocScorer rmScorer = new RelevanceModelScorer(zeroMuDocScorer,
					Math.exp(queryScorer.scoreQuery(query, hit)));
				
			// Score each term
			for (String term : hit.getFeatureVector().getFeatures()) {
				if (stopper != null && stopper.isStopWord(term)) {
					continue;
				}
				termScores.addTerm(term, rmScorer.scoreTerm(term, hit) / initialResults.size());
			}
		}
		termScores.clip(feedbackTerms);
		return termScores;
	}

}
