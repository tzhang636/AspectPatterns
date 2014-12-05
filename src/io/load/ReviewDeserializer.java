package io.load;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import data.Ratings;
import data.Review;

final class ReviewDeserializer implements JsonDeserializer<Review> {

  @Override
  public Review deserialize(final JsonElement json, final Type type,
      final JsonDeserializationContext context) throws JsonParseException {

    // get a json object representation of a review
    final JsonObject jsonObject = json.getAsJsonObject();

    // deserialize the review's ratings field
    Ratings ratings = context.deserialize(jsonObject.get("Ratings"), Ratings.class);

    // deserialize the review's other fields
    String location = null, title = null, author = null;
    String reviewId = null, content = null, date = null;

    if (jsonObject.has("AuthorLocation")) {
      location = jsonObject.get("AuthorLocation").getAsString();
    }
    if (jsonObject.has("Title")) {
      title = jsonObject.get("Title").getAsString();
    }
    if (jsonObject.has("Author")) {
      author = jsonObject.get("Author").getAsString();
    }
    if (jsonObject.has("ReviewID")) {
      reviewId = jsonObject.get("ReviewID").getAsString();
    }
    if (jsonObject.has("Content")) {
      content = jsonObject.get("Content").getAsString();
    }
    if (jsonObject.has("Date")) {
      date = jsonObject.get("Date").getAsString();
    }

    // fill out and return a Review object
    return new Review(ratings, location, title, author, reviewId, content, date);
  }
}
