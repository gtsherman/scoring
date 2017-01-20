package edu.gslis.scoring.expansion;

import edu.gslis.queries.GQuery;
import edu.gslis.searchhits.SearchHits;
import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;

public interface RM1Builder {
	
	public FeatureVector buildRelevanceModel(GQuery query, SearchHits initialResults);
	
	public FeatureVector buildRelevanceModel(GQuery query, SearchHits initialResults, Stopper stopper);

}

