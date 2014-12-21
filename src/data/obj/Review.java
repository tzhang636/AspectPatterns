package data.obj;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nlp.io.NLP;
import nlp.io.StopWords;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

public final class Review {

  /* NOTE: some of these fields could be null */
  private final Ratings ratings;
  private final String location;
  private final String title;
  private final String author;
  private final String reviewId;
  private final String content;
  private final String date;

  /* string representation of this review */
  private String string = null;

  /* nouns and noun phrases associated with this review */
  private List<List<String>> nouns = null;

  /* nouns with stop words removed, then stemmed */
  private List<List<String>> stopStemNouns = null;

  /* lowercased nouns */
  private List<List<String>> lNouns = null;

  public Review(final Ratings ratings, final String location, final String title,
      final String author, final String reviewId, final String content, final String date) {
    this.ratings = ratings;
    this.location = location;
    this.title = title;
    this.author = author;
    this.reviewId = reviewId;
    this.content = content;
    this.date = date;
  }

  public final List<List<String>> getLowercaseNouns() {
    return lNouns;
  }

  /**
   * Extracts raw nouns and noun phrases from the review content.
   * 
   * @return
   */
  public final List<List<String>> extractNouns(final StanfordCoreNLP pipeline) {
    if (nouns == null) {
      // create annotation
      final Annotation annotation = new Annotation(content);
      pipeline.annotate(annotation);

      nouns = new ArrayList<>();

      List<CoreMap> sents = annotation.get(SentencesAnnotation.class);
      for (CoreMap sent : sents) {
        List<String> sentNouns = new ArrayList<>();

        // get nouns in this sentence
        List<CoreLabel> tokens = sent.get(TokensAnnotation.class);
        for (CoreLabel token : tokens) {
          String pos = token.getString(PartOfSpeechAnnotation.class);
          if (NLP.isNoun(pos)) {
            sentNouns.add(token.value());
          }
        }

        // get noun phrases in this sentence
        Tree sentParse = sent.get(TreeAnnotation.class);
        TregexPattern pattern = TregexPattern.compile("@NP");
        TregexMatcher matcher = pattern.matcher(sentParse);
        while (matcher.find()) {
          Tree match = matcher.getMatch();
          List<Tree> leaves = match.getLeaves();
          List<String> transformed = Lists.transform(leaves, Functions.toStringFunction());
          String nounPhrase = Joiner.on(' ').join(transformed);
          sentNouns.add(nounPhrase);
        }

        // add if there is at least one noun in sentence
        if (!sentNouns.isEmpty()) {
          nouns.add(sentNouns);
        }
      }
    }
    // nouns should not be modifiable from the outside
    return Collections.unmodifiableList(nouns);
  }

  /**
   * Strips out all stop words and all non-words from the nouns.
   * 
   * @param analyzer
   * @return
   * @throws IOException
   */
  public final List<List<String>> stopStemNouns(final StanfordCoreNLP pipeline) throws IOException {
    if (nouns == null) {
      extractNouns(pipeline);
    }
    if (stopStemNouns == null) {
      stopStemNouns = new ArrayList<>();

      for (List<String> sentNouns : nouns) {
        List<String> sentStopStemNouns = new ArrayList<>();

        for (String noun : sentNouns) {
          TokenStream tokenStream = new StandardTokenizer(new StringReader(noun));
          tokenStream = new StopFilter(tokenStream, StopWords.getStopWords());
          tokenStream = new PorterStemFilter(tokenStream);
          CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
          // initialize stream
          tokenStream.reset();
          // reassembles noun with stop words removed
          StringBuilder sb = new StringBuilder();
          while (tokenStream.incrementToken()) {
            sb.append(term.toString()).append(" ");
          }
          // add if noun is not empty
          if (sb.length() > 1) { // 1 because of last space
            sb.deleteCharAt(sb.length() - 1); // remove last space
            sentStopStemNouns.add(sb.toString());
          }
          // teardown stream
          tokenStream.end();
          tokenStream.close();
        }
        // add if not empty
        if (!sentStopStemNouns.isEmpty()) {
          stopStemNouns.add(sentStopStemNouns);
        }
      }
    }
    return Collections.unmodifiableList(stopStemNouns);
  }

  /**
   * Lowercase all nouns and noun phrases.
   * 
   * @return
   * @throws IOException
   */
  public final List<List<String>> lowercaseNouns(final StanfordCoreNLP pipeline) throws IOException {
    if (stopStemNouns == null) {
      stopStemNouns(pipeline);
    }
    if (lNouns == null) {
      lNouns = new ArrayList<List<String>>();
      for (List<String> sentNouns : stopStemNouns) {
        List<String> lSentNouns = new ArrayList<String>();
        for (String noun : sentNouns) {
          lSentNouns.add(noun.toLowerCase());
        }
        lNouns.add(lSentNouns);
      }
    }
    return Collections.unmodifiableList(lNouns);
  }

  @Override
  public String toString() {
    if (string == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Ratings: ").append(ratings).append('\n');
      sb.append("Author Location: ").append(location).append('\n');
      sb.append("Title: ").append(title).append('\n');
      sb.append("Author: ").append(author).append('\n');
      sb.append("ReviewID: ").append(reviewId).append('\n');
      sb.append("Content: ").append(content).append('\n');
      sb.append("Date: ").append(date).append('\n');
      string = sb.toString();
    }
    return string;
  }
}
