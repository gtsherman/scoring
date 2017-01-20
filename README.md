# Scoring

An alternative document scoring library based on composition. For use with [ir-tools](https://github.com/uiucGSLIS/ir-tools).

## About

### Document Scoring

Scoring a document often involves combining several sources of information. This library emphasizes composition of these sources, treating each as a `DocScorer`. Each `DocScorer` is charged with scoring given terms in a single document based on its own internal logic.

For example, if a document should be scored by the linear interpolation of its query likelihood and cosine similarity scores, we can use three `DocScorer` implementations:

1. A `DirichletDocScorer` to compute query likelihood, provided by the library.
2. A `CosineSimilarityDocScorer` to compute cosine similarity, not currently provided by the library.
3. An `InterpolatedDocScorer` to linearly combine the previous two, provided by the library.

Each `DocScorer` is instantiated with any parameters (e.g. the smoothing parameter mu for the `DirichletDocScorer`). Its `scoreTerm` function may then be called repeatedly with the term and document passed as parameters to compute the scores of any terms that are required.

In our example above, the client would call `scoreTerm` on the `InterpolatedDocScorer` instance, which would itself call `scoreTerm` on each of the other instances and combine their results according to the weights provided by the client.

### Query Scoring

Query scoring is accomplished using `QueryScorer` implementations, which are expected to take one or more `DocScorer` instances and compute a score for the document across all query terms.

### Efficiency

Some applications require computing the same or similar scores repeatedly. For example, in cross validation, the same documents may be scored for the same query with the same or different parameters in all but one fold. The library therefore includes the `CachedDocScorer` class, which sits in between the actual `DocScorer` implementation and the client, holding a cache of any previously scored document/term pairs.

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
