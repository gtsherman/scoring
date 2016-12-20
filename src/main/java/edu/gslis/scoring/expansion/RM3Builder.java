package edu.gslis.scoring.expansion;

import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;
import edu.gslis.utils.retrieval.QueryResults;

/**
 * Builds an RM3 for a given query.
 * @author Garrick
 *
 */
public class RM3Builder {
	
	public FeatureVector buildRelevanceModel(QueryResults queryResults,
			RM1Builder rm1,
			double originalQueryWeight) {
		return buildRelevanceModel(queryResults, rm1, originalQueryWeight, null);
	}

	public FeatureVector buildRelevanceModel(QueryResults queryResults,
			RM1Builder rm1,
			double originalQueryWeight,
			Stopper stopper) {
		queryResults.getQuery().getFeatureVector().normalize(); // very important

		FeatureVector rmVector = rm1.buildRelevanceModel(queryResults, stopper);
		rmVector.normalize(); // very important

		return FeatureVector.interpolate(queryResults.getQuery().getFeatureVector(),
				rmVector, originalQueryWeight);
	}

}
