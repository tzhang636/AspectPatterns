package nlp.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;

public final class SPMF {

  /* bidirectional map to store nouns and their ids */
  private static int id = 0;
  private static final BidiMap<String, Integer> nounId;
  static {
    nounId = new DualHashBidiMap<>();
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
  
  private static final String OUTPUT_PATH = "src/nlp/res/Output.txt";

  private SPMF() {}

  /**
   * Writes nouns to SPMF input file. Saves new nouns and their ids to lookup table. TODO: separate
   * noun phrases into separate words.
   * 
   * @param nouns
   * @throws IOException
   */
  public static void write(final List<List<String>> nouns) throws IOException {
    boolean isEmpty = false;
    if (!input.exists()) {
      input.createNewFile();
      isEmpty = true;
    }
    final FileWriter fw = new FileWriter(input, true);
    final BufferedWriter bw = new BufferedWriter(fw);
    if (!isEmpty) {
      bw.newLine(); // prepare for append on newline
    }

    StringBuilder line = new StringBuilder(256);
    for (int i = 0; i < nouns.size(); ++i) {
      line.setLength(0);
      List<String> sent = nouns.get(i);
      for (String noun : sent) {
        if (!nounId.containsKey(noun)) {
          nounId.put(noun, id++);
        }
        line.append(nounId.get(noun)).append(" ");
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

  public static List<String> run(final double minSup) throws IOException {
    if (!input.exists()) {
      throw new IOException("input has not been created");
    }
    // run apriori
    final AlgoApriori apriori = new AlgoApriori();
    apriori.runAlgorithm(minSup, INPUT_PATH, OUTPUT_PATH);
    apriori.printStats();

    // read output to compile frequent itemsets
    final List<String> result = new ArrayList<>();
    final BufferedReader br = new BufferedReader(new FileReader(new File(OUTPUT_PATH)));
    String line = br.readLine();
    StringBuilder noun = new StringBuilder(256);
    while (line != null) {
      noun.setLength(0);
      String[] tokens = line.split("\\s+");
      if (tokens.length > 0) { // not an empty line
        for (String token : tokens) {
          if (token.equals("#SUP:")) { // reached delimiter
            break;
          }
          noun.append(nounId.getKey(Integer.parseInt(token))).append(" ");
        }
        if (noun.length() > 0) {
          noun.deleteCharAt(noun.length() - 1); // delete last space
        }
        result.add(noun.toString()); // add noun to result set
        line = br.readLine();
      }
    }
    br.close();
    return result;
  }
}