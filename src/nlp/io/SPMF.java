package nlp.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;

public final class SPMF {

  /* bidirectional map to store nouns and their ids */
  private static int id = 0;
  private static final BidiMap<String, Integer> nounsToId;
  static {
    nounsToId = new DualHashBidiMap<>();
  }

  /* input file to SPMF, relative path */
  private static final String INPUT_PATH = "src/nlp/res/Input.txt";
  private static final File input;
  static {
    input = new File(INPUT_PATH);
    if (input.exists()) {
      input.delete(); // delete previous input file on startup
    }
  }

  private SPMF() {}

  /**
   * Writes nouns to SPMF input file. Saves new nouns and their ids to lookup table.
   * 
   * @param nouns
   * @throws IOException
   */
  public static void write(List<List<String>> nouns) throws IOException {
    boolean isEmpty = false;
    if (!input.exists()) {
      input.createNewFile();
      isEmpty = true;
    }
    FileWriter fw = new FileWriter(input, true);
    BufferedWriter bw = new BufferedWriter(fw);
    if (!isEmpty) {
      bw.newLine(); // prepare for append on newline
    }
    for (int i = 0; i < nouns.size(); ++i) {
      List<String> sent = nouns.get(i);
      StringBuilder line = new StringBuilder();
      for (String noun : sent) {
        if (!nounsToId.containsKey(noun)) {
          nounsToId.put(noun, id++);
        }
        line.append(nounsToId.get(noun)).append(" ");
      }
      if (line.length() > 0) {
        line.deleteCharAt(line.length() - 1); // delete last space
      }
      bw.write(line.toString());
      if (i < nouns.size() - 1) {
        bw.newLine(); // append a newline if not the last sentence
      }
    }
    bw.close();
  }

  public static void run() throws IOException {
    AlgoApriori apriori = new AlgoApriori();
    apriori.runAlgorithm(0.1, "src/nlp/res/Input.txt", "src/nlp/res/Output.txt");
    apriori.printStats();
  }

}