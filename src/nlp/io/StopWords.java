package nlp.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.CharArraySet;

public final class StopWords {

  private StopWords() {}

  /* absolute path, from src folder */
  private static final String STOP_WORDS_PATH = "/nlp/res/StopWords.txt";

  private static CharArraySet stopWords = null;

  public static CharArraySet getStopWords() throws IOException {
    if (stopWords == null) {
      List<String> stopWords = new ArrayList<>();
      final InputStream is = NLP.class.getResourceAsStream(STOP_WORDS_PATH);
      final Reader reader = new InputStreamReader(is, "UTF-8");
      final BufferedReader br = new BufferedReader(reader);
      String line = br.readLine();
      while (line != null) {
        stopWords.add(line);
        line = br.readLine();
      }
      br.close();
      StopWords.stopWords = new CharArraySet(stopWords, true);
    }
    return stopWords;
  }
}