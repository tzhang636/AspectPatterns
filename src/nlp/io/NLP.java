package nlp.io;

public final class NLP {

  private NLP() {}

  public static boolean isNoun(String pos) {
    return pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS");
  }

  public static boolean isNounPhrase(String tag) {
    return tag.equals("NP");
  }
}
