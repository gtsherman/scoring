package edu.gslis.scoring;

import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.keyvalue.MultiKey;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

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
	
	protected double mu = 2500.0;
	protected double epsilon = 1.0;
	
	private LoadingCache<MultiKey, Double> termScores = CacheBuilder.newBuilder()
			.softValues()
			.build(
					new CacheLoader<MultiKey, Double>() {
						public Double load(MultiKey key) throws Exception {
							String term = (String) key.getKey(TERM_KEY_INDEX);
							SearchHit doc = (SearchHit) key.getKey(DOC_KEY_INDEX);
							
							FeatureVector docVector = doc.getFeatureVector();
							double wordCount = docVector.getFeatureWeight(term);
							double docLength = docVector.getLength();
							double colProb = (epsilon + collectionStats.termCount(term)) / collectionStats.getTokCount();
							double score = (wordCount + mu * colProb) / (docLength + mu);
							return score;
						}
					});
	
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
		// Setup keys
		Object[] keys = new Object[2];
		keys[TERM_KEY_INDEX] = term;
		keys[DOC_KEY_INDEX] = doc;
		
		// Convert to MultiKey
		MultiKey key = new MultiKey(keys);
		
		// Lookup in cache
		try {
			return termScores.get(key);
		} catch (ExecutionException e) {
			System.err.println("Error scoring term '" + term +
					"' in document '" + doc.getDocno() + "'");
			System.err.println(e.getStackTrace());
		}
		
		// Default to zero, if we have an issue
		return 0.0;
	}

}
