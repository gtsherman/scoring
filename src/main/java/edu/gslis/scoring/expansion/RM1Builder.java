package edu.gslis.scoring.expansion;

import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;
import edu.gslis.utils.retrieval.QueryResults;

public interface RM1Builder {
	
	public FeatureVector buildRelevanceModel(QueryResults queryResults);
	
	public FeatureVector buildRelevanceModel(QueryResults query, Stopper stopper);

}

