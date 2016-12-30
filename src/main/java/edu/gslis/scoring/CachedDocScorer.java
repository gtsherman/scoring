package edu.gslis.scoring;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.gslis.searchhits.SearchHit;

public abstract class CachedDocScorer implements DocScorer {

	private LoadingCache<DocumentTermKey, Double> termScores = CacheBuilder.newBuilder()
			.softValues()
			.build(
					new CacheLoader<DocumentTermKey, Double>() {
						public Double load(DocumentTermKey key) throws Exception {
							return scoreTerm(key);
						}
					});	
	
	public LoadingCache<DocumentTermKey, Double> getCache() {
		return termScores;
	}

	@Override
	public double scoreTerm(String term, SearchHit document) {
		try {
			// Lookup in cache
			return getCache().get(new DocumentTermKey(term, document));
		} catch (ExecutionException e) {
			System.err.println("Error scoring term '" + term +
					"' in document '" + document.getDocno() + "'");
			System.err.println(e.getStackTrace());
		}
		
		// Default to zero, if we have an issue
		return 0.0;
	}
	
	protected abstract double scoreTerm(DocumentTermKey key);

	protected class DocumentTermKey {
		
		private String term;
		private SearchHit document;

		public DocumentTermKey(String term, SearchHit document) {
			this.term = term;
			this.document = document;
		}
		
		public String getTerm() {
			return term;
		}
		
		public SearchHit getDocument() {
			return document;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof DocumentTermKey) {
				DocumentTermKey other = (DocumentTermKey) obj;

				return term.equalsIgnoreCase(other.getTerm()) && 
						document.equals(other.getDocument());
			}
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return (term + document.getDocno()).hashCode();
		}
		
	}

}