package edu.gslis.scoring.expansion;

import java.util.Iterator;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.docscoring.support.IndexBackedCollectionStats;
import edu.gslis.indexes.IndexWrapper;
import edu.gslis.queries.GQuery;
import edu.gslis.scoring.DocScorer;
import edu.gslis.scoring.creators.DirichletDocScorerCreator;
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
	
	private GQuery query;
	private SearchHits initialHits;
	
	private DirichletDocScorerCreator docScorerCreator; 
	private DirichletDocScorerCreator zeroMuDocScorerCreator; 
	
	public StandardRM1Builder(GQuery query, SearchHits initialHits, int feedbackDocs, int feedbackTerms, CollectionStats collectionStats) {
		setFeedbackDocs(feedbackDocs);
		setFeedbackTerms(feedbackTerms);
		setQuery(query, initialHits);

		createDocScorerCreators(collectionStats);
	}
	
	public StandardRM1Builder(GQuery query, SearchHits initialHits, int feedbackDocs, int feedbackTerms, DirichletDocScorerCreator docScorerCreator) {
		setFeedbackDocs(feedbackDocs);
		setFeedbackTerms(feedbackTerms);
		setQuery(query, initialHits);
		
		this.docScorerCreator = docScorerCreator;
		this.zeroMuDocScorerCreator = new DirichletDocScorerCreator(0, docScorerCreator.getCollectionStats(), false);
	}
	
	public StandardRM1Builder(GQuery query, SearchHits initialHits, CollectionStats collectionStats) {
		this(query, initialHits, DEFAULT_FEEDBACK_DOCS, DEFAULT_FEEDBACK_TERMS, collectionStats);
	}
	
	public StandardRM1Builder(GQuery query, IndexWrapper index, int feedbackDocs, int feedbackTerms) {
		setFeedbackDocs(feedbackDocs);
		setFeedbackTerms(feedbackTerms);
		setQuery(query, index);

		IndexBackedCollectionStats collectionStats = new IndexBackedCollectionStats();
		collectionStats.setStatSource(index);

		createDocScorerCreators(collectionStats);
	}

	public StandardRM1Builder(GQuery query, IndexWrapper index) {
		this(query, index, DEFAULT_FEEDBACK_DOCS, DEFAULT_FEEDBACK_TERMS);
	}
	
	private void createDocScorerCreators(CollectionStats collectionStats) {
		docScorerCreator = new DirichletDocScorerCreator(collectionStats);
		zeroMuDocScorerCreator = new DirichletDocScorerCreator(0, collectionStats, false);
	}
	
	public void setFeedbackDocs(int feedbackDocs) {
		this.feedbackDocs = feedbackDocs;
	}
	
	public void setFeedbackTerms(int feedbackTerms) {
		this.feedbackTerms = feedbackTerms;
	}
	
	public void setQuery(GQuery query, SearchHits initialHits) {
		this.query = query;
		this.initialHits = initialHits;
	}
	
	public void setQuery(GQuery query, IndexWrapper index) {
		this.query = query;
		this.initialHits = index.runQuery(query, feedbackDocs);
	}
	
	public FeatureVector buildRelevanceModel() {
		return buildRelevanceModel(null);
	}

	public FeatureVector buildRelevanceModel(Stopper stopper) {
		FeatureVector termScores = new FeatureVector(stopper);
		
		int i = 0;
		Iterator<SearchHit> hitIt = initialHits.iterator();
		while (hitIt.hasNext() && i < feedbackDocs) {
			SearchHit hit = hitIt.next();
			i++;
			
			// Prep the scorers
			QueryScorer queryScorer = new QueryLikelihoodQueryScorer(docScorerCreator.getDocScorer(hit));
			DocScorer rmScorer = new RelevanceModelScorer(zeroMuDocScorerCreator.getDocScorer(hit),
					Math.exp(queryScorer.scoreQuery(query)));
				
			// Score each term
			for (String term : hit.getFeatureVector().getFeatures()) {
				if (stopper != null && stopper.isStopWord(term)) {
					continue;
				}
				termScores.addTerm(term, rmScorer.scoreTerm(term)/initialHits.size());
			}
		}
		termScores.clip(feedbackTerms);
		return termScores;
	}

}
