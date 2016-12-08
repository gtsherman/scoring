package edu.gslis.scoring.creators;

import edu.gslis.docscoring.support.CollectionStats;
import edu.gslis.scoring.DirichletDocScorer;
import edu.gslis.scoring.StoredDocScorer;
import edu.gslis.searchhits.SearchHit;

public class DirichletDocScorerCreator extends DocScorerCreator {
	
	public static final double DEFAULT_MU = -1;
	
	private CollectionStats collectionStats;
	private double mu = DEFAULT_MU;
	private boolean useStoredScorers = true;
	
	public DirichletDocScorerCreator(CollectionStats collectionStats) {
		this.collectionStats = collectionStats;
	}
	
	public DirichletDocScorerCreator(CollectionStats collectionStats, boolean useStoredScorers) {
		this(collectionStats);
		useStoredScorers(useStoredScorers);
	}
	
	public DirichletDocScorerCreator(double mu, CollectionStats collectionStats) {
		this(collectionStats);
		setMu(mu);
	}
	
	public DirichletDocScorerCreator(double mu, CollectionStats collectionStats, boolean useStoredScorers) {
		this(mu, collectionStats);
		useStoredScorers(useStoredScorers);
	}
	
	public CollectionStats getCollectionStats() {
		return collectionStats;
	}
	
	public double getMu() {
		return mu;
	}
	
	public void setMu(double mu) {
		this.mu = mu;
	}
	
	public void useStoredScorers(boolean useStoredScorers) {
		this.useStoredScorers = useStoredScorers;
	}
	
	@Override
	protected void createIfNecessary(SearchHit doc) {
		String docKey = docKey(doc);
		if (!storedScorers.containsKey(docKey)) {
			DirichletDocScorer docScorer = new DirichletDocScorer(doc, collectionStats);
			if (mu != DEFAULT_MU) {
				docScorer.setMu(mu);
			}
			if (useStoredScorers) {
				storedScorers.put(docKey, new StoredDocScorer(docScorer));
			} else {
				storedScorers.put(docKey, docScorer);
			}
		}
	}
	
	@Override
	protected String docKey(SearchHit doc) {
		return doc.getDocno() + mu;
	}

}
