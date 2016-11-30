package edu.gslis.scoring;

import java.util.HashMap;
import java.util.Map;

/**
 * Returns previously computed term scores if available, otherwise compute.
 * This class is intended to decrease computation time when term scores are the
 * same from one computation to the next. Since it stores term scores, any 
 * previously computed scores will be looked up instead of recomputed.
 * 
 * @author Garrick
 *
 */
public class StoredDocScorer implements DocScorer {
	
	private Map<String, Double> alreadyComputedTerms = new HashMap<String, Double>();
	
	private DocScorer docScorer;
	
	public StoredDocScorer(DocScorer docScorer) {
		this.docScorer = docScorer;
	}

	public double scoreTerm(String term) {
		if (!alreadyComputedTerms.containsKey(term)) {
			alreadyComputedTerms.put(term, docScorer.scoreTerm(term));
		}
		return alreadyComputedTerms.get(term);
	}

}
