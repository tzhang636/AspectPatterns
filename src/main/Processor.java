package main;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp.io.SPMF;
import data.load.Json;
import data.obj.Hotel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public final class Processor {
  // static class
  private Processor() {}

  public static void extract(final Hotel hotel) throws IOException {
    final Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, parse");
    final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    final int numReviews = hotel.numReviews();
    for (int i = 0; i < numReviews; ++i) {
      hotel.getReview(i).lowercaseNouns(pipeline);
    }
  }
  
  public static void process(final Hotel hotel) throws IOException {
    final int numReviews = hotel.numReviews();
    for (int i = 0; i < numReviews; ++i) {
      SPMF.write(hotel.getReview(i).getLowercaseNouns());
    }
    List<String> freqNouns = SPMF.run(0.05);
    System.out.println(freqNouns);
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Hotel hotel = Json.consume("/json/res/596642.json");
    extract(hotel);
    process(hotel);
    System.out.println("Finished!");
    Thread.sleep(50000);
  }
}
