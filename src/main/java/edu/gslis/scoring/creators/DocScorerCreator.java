package edu.gslis.scoring.creators;

import java.util.HashMap;
import java.util.Map;

import edu.gslis.scoring.DocScorer;
import edu.gslis.scoring.StoredDocScorer;
import edu.gslis.searchhits.SearchHit;

/**
 * Provides a DocScorer using previously specified parameters.
 * Classes extending this one should require reusable parameters in their 
 * creation. These parameters are then used to build new DocScorers as needed. 
 * If a DocScorer has been created already, return the prior instance.
 * 
 * @author Garrick
 *
 */
public abstract class DocScorerCreator {
	
	protected Map<String, StoredDocScorer> storedScorers = new HashMap<String, StoredDocScorer>();
	
	public DocScorer getDocScorer(SearchHit doc) {
		createIfNecessary(doc);
		return storedScorers.get(docKey(doc));
	}
	
	/**
	 * Creates a DocScorer for the given document with current parameters, if one does not exist.
	 * @param doc The document that needs to be scored.
	 */
	protected abstract void createIfNecessary(SearchHit doc);

	/**
	 * Returns a unique key based on the document and any parameters.
	 * @param doc The document that needs to be scored.
	 * @return A String uniquely representing the document and any parameters.
	 */
	protected abstract String docKey(SearchHit doc);

}
