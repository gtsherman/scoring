# Scoring

An alternative document scoring library based on composition. For use with [ir-tools](https://github.com/uiucGSLIS/ir-tools).

## About

### Document Scoring

Scoring a document often involves combining several sources of information. This library emphasizes composition of these sources, treating each as a `DocScorer`. Each `DocScorer` is charged with scoring given terms in a single document based on its own internal logic.

For example, if a document should be scored by the linear interpolation of its query likelihood and cosine similarity scores, we can use three `DocScorer` implementations:

1. A `DirichletDocScorer` to compute query likelihood, provided by the library.
2. A `CosineSimilarityDocScorer` to compute cosine similarity, not currently provided by the library.
3. An `InterpolatedDocScorer` to linearly combine the previous two, provided by the library.

Each `DocScorer` is instantiated for a given document with any parameters (e.g. the smoothing parameter mu for the `DirichletDocScorer`). Its `scoreTerm` function may then be called repeatedly to compute the scores of any terms that are required.

In our example above, the `InterpolatedDocScorer` would require the other two in order to function, while each of the others would require the document in order to function. Thus, although the `InterpolatedDocScorer` is not explicitly tied to a single document, it can only score the document provided by the other two instances. The client would call `scoreTerm` on the `InterpolatedDocScorer` instance, which would itself call `scoreTerm` on each of the other instances and combine their results according to the weights provided by the client.

### Query Scoring

Query scoring is accomplished using `QueryScorer` implementations, which are expected to take one or more `DocScorer` instances and compute a score for the document across all query terms.

### Efficiency

Some applications require computing the same or similar scores repeatedly. For example, in cross validation, the same documents may be scored for the same query with the same or different parameters in all but one fold. The library therefore includes some efficency-boosting classes:

- `StoredDocScorer` sits between the client and its `DocScorer` implementation. Remember that each `DocScorer` is tasked with scoring a single document. If the `DocScorer` has already computed a requested term for its document, the `StoredDocScorer` simply looks up the previously computed term score; otherwise, it allows the `DocScorer` to score the term according to its internal logic.
- `DocScorerCreator` implementations are constructed with any reusable parameters, e.g. the `CollectionStats` object required by the `DirichletDocScorer`. They then produce a new `DocScorer` instance, inside a `StoredDocScorer` instance, using these parameters for each new document seen. If the `DocScorerCreator` has previously created a `DocScorer` for a given document with unchanged parameters, the `DocScorerCreator` will return that earlier instance rather than construct a new one. This enables the `StoredDocScorer` to work, since any scores previously computed for this document persist within the `StoredDocScorer` instance which itself persists within the `DocScorerCreator` instance.

In general, using `DocScorerCreator` instances is a good idea because they improve efficiency and code readability. However, they do use more memory (since each `DocScorer` is saved, as is each term scored by each `DocScorer`) and are therefore not appropriate in all situations.

## Installation

```
$ git clone https://github.com/gtsherman/scoring.git
$ cd scoring
$ mvn install
```

## Use

With Maven, add the following dependency (after installation):

```
<dependency>
  <groupId>edu.gslis</groupId>
  <artifactId>scoring</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
