package main;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp.io.SPMF;
import data.load.Json;
import data.obj.Hotel;
import data.obj.Review;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public final class Processor {

  /* Stanford NLP tools */
  private static final Properties props;
  private static final StanfordCoreNLP pipeline;

  // initialize the Stanford NLP pipeline
  static {
    props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, parse");
    pipeline = new StanfordCoreNLP(props);
  }

  // static class
  private Processor() {}

  public static void process(final Hotel hotel) throws IOException {
    final int numReviews = hotel.numReviews();
    for (int i = 0; i < numReviews; ++i) {
      final Review review = hotel.getReview(i);
      review.annotate(pipeline);
      List<List<String>> nouns = review.lowercaseNouns();
      SPMF.write(nouns);
      SPMF.run();
    }
  }

  public static void main(String[] args) throws IOException {
    Hotel hotel = Json.consume("/json/res/596642.json");
    process(hotel);
  }
}
