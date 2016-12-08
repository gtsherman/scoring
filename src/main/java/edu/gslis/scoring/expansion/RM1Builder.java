package edu.gslis.scoring.expansion;

import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.Stopper;

public interface RM1Builder {
	
	public FeatureVector buildRelevanceModel();
	
	public FeatureVector buildRelevanceModel(Stopper stopper);

}

