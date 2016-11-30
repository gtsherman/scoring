package edu.gslis.scoring.expansion;

import edu.gslis.queries.GQuery;
import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;

/**
 * Builds an RM3 for a given query.
 * @author Garrick
 *
 */
public class RM3Builder {
	
	private RM1Builder rm1;
	private GQuery query;
	
	public RM3Builder(GQuery query, RM1Builder rm1) {
		setRM1Builder(rm1);
		setQuery(query);
	}
	
	public void setRM1Builder(RM1Builder rm1) {
		this.rm1 = rm1;
	}
	
	public void setQuery(GQuery query) {
		this.query = query;
		this.query.getFeatureVector().normalize(); // very important
	}
	
	public FeatureVector buildRelevanceModel(double originalQueryWeight) {
		return buildRelevanceModel(originalQueryWeight, null);
	}

	public FeatureVector buildRelevanceModel(double originalQueryWeight, Stopper stopper) {
		FeatureVector rmVector = rm1.buildRelevanceModel(stopper);
		rmVector.normalize(); // very important
		return FeatureVector.interpolate(query.getFeatureVector(), rmVector, originalQueryWeight);
	}

}
