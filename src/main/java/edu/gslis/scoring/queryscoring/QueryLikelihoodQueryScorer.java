package edu.gslis.scoring.queryscoring;

import java.util.Iterator;

import edu.gslis.queries.GQuery;
import edu.gslis.scoring.DocScorer;
import edu.gslis.searchhits.SearchHit;

/**
 * Computes score across all query terms.
 * @author Garrick
 *
 */
public class QueryLikelihoodQueryScorer implements QueryScorer {

	private DocScorer termScorer;
	
	public QueryLikelihoodQueryScorer(DocScorer termScorer) {
		this.termScorer = termScorer;
	}
	
	@Override
	public double scoreQuery(GQuery query, SearchHit document) {
		double loglikelihood = 0.0;

		Iterator<String> termIt = query.getFeatureVector().iterator();
		while (termIt.hasNext()) {
			String term = termIt.next();
			double termProb = termScorer.scoreTerm(term, document);
			if (termProb == 0) {
				continue;
			}
			double qWeight = query.getFeatureVector().getFeatureWeight(term) / query.getFeatureVector().getLength();
			loglikelihood += qWeight * Math.log(termProb);
		}
		
		return loglikelihood;
	}

}
