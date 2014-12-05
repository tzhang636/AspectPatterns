package data;

import java.util.Arrays;
import java.util.List;

public final class Hotel {

  private final List<Review> reviews;
  private String string = null;

  public Hotel(final Review[] reviews) {
    this.reviews = Arrays.asList(reviews);
  }

  public int numReviews() {
    return reviews.size();
  }

  public Review getReview(final int idx) {
    if (idx >= 0 && idx < reviews.size()) {
      return reviews.get(idx);
    }
    throw new IllegalArgumentException("idx out of range");
  }

  @Override
  public String toString() {
    if (string == null) {
      StringBuilder sb = new StringBuilder();
      for (Review review : reviews) {
        sb.append(review).append('\n');
      }
      string = sb.toString();
    }
    return string;
  }
}
