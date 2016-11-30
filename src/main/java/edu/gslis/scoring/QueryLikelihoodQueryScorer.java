package edu.gslis.scoring;

import java.util.Iterator;

import edu.gslis.queries.GQuery;

/**
 * Computes score across all query terms, e.g. Prod_q (1-lam)P(q|D) + lam*Sum_e P(q|e)P(e|D)
 * @author garrick
 *
 */
public class QueryLikelihoodQueryScorer implements QueryScorer {

	private DocScorer termScorer;
	
	public QueryLikelihoodQueryScorer(DocScorer termScorer) {
		this.termScorer = termScorer;
	}
	
	public double scoreQuery(GQuery query) {
		double loglikelihood = 0.0;

		Iterator<String> termIt = query.getFeatureVector().iterator();
		while (termIt.hasNext()) {
			String term = termIt.next();
			double termProb = termScorer.scoreTerm(term);
			if (termProb == 0) {
				continue;
			}
			double qWeight = query.getFeatureVector().getFeatureWeight(term) / query.getFeatureVector().getLength();
			loglikelihood += qWeight * Math.log(termProb);
		}
		
		return loglikelihood;
	}

}
