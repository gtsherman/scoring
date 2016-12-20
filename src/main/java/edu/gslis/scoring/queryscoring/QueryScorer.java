package edu.gslis.scoring.queryscoring;

import edu.gslis.queries.GQuery;
import edu.gslis.searchhits.SearchHit;

public interface QueryScorer {

	public double scoreQuery(GQuery query, SearchHit document);
	
}
