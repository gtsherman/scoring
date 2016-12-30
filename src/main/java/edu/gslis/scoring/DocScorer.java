package edu.gslis.scoring;

import edu.gslis.searchhits.SearchHit;

public interface DocScorer {
	
	public double scoreTerm(String term, SearchHit document);

}
