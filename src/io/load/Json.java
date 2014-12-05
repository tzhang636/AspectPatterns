package io.load;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.Hotel;
import data.Ratings;
import data.Review;

public final class Json {

  private static final GsonBuilder gsonBuilder;
  private static final Gson gson;

  // initialize and configure gson
  static {
    gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Hotel.class, new HotelDeserializer());
    gsonBuilder.registerTypeAdapter(Review.class, new ReviewDeserializer());
    gsonBuilder.registerTypeAdapter(Ratings.class, new RatingDeserializer());
    gson = gsonBuilder.create();
  }

  // static class - prevents instances of this class to be constructed
  private Json() {}

  /**
   * Consumes the json file at filePath and populates a Hotel instance.
   * 
   * @param filePath "/res/596642.json"
   * @return
   * @throws IOException
   */
  public static Hotel consume(final String filePath) throws IOException {
    final InputStream is = Json.class.getResourceAsStream(filePath);
    final Reader reader = new InputStreamReader(is, "UTF-8");
    final Hotel hotel = gson.fromJson(reader, Hotel.class);
    return hotel;
  }
}
