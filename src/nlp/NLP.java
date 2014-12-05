package nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.CharArraySet;

public final class NLP {

  private NLP() {}

  private static final String STOP_WORDS_PATH = "/nlp/res/StopWords.txt";

  public static CharArraySet getStopWords() throws IOException {
    List<String> stopWords = new ArrayList<>();
    
    final InputStream is = NLP.class.getResourceAsStream(STOP_WORDS_PATH);
    final Reader reader = new InputStreamReader(is, "UTF-8");
    final BufferedReader br = new BufferedReader(reader);
    try {
      String line = br.readLine();
      while (line != null) {
        stopWords.add(line);
        line = br.readLine();
      }
    } finally {
      br.close();
    }

    return new CharArraySet(stopWords, true);
  }

  public static boolean isNoun(String pos) {
    return pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS");
  }

  public static boolean isNounPhrase(String tag) {
    return tag.equals("NP");
  }

}
