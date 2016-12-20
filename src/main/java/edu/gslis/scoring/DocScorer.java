package edu.gslis.scoring;

import edu.gslis.searchhits.SearchHit;

public interface DocScorer {
	
	public static final int TERM_KEY_INDEX = 0;
	public static final int DOC_KEY_INDEX = 1;
	
	public double scoreTerm(String term, SearchHit document);

}
