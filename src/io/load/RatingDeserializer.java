package io.load;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import data.Ratings;

final class RatingDeserializer implements JsonDeserializer<Ratings> {

  @Override
  public Ratings deserialize(final JsonElement json, final Type type,
      final JsonDeserializationContext context) throws JsonParseException {

    // get a json object representation of a rating
    final JsonObject jsonObject = json.getAsJsonObject();

    // deserialize the rating's fields
    final int service = jsonObject.get("Service").getAsInt();
    final int cleanliness = jsonObject.get("Cleanliness").getAsInt();
    final double overall = jsonObject.get("Overall").getAsDouble();
    final int value = jsonObject.get("Value").getAsInt();
    final int rooms = jsonObject.get("Rooms").getAsInt();
    final int location = jsonObject.get("Location").getAsInt();

    // fill out and return a Rating object
    return new Ratings(service, cleanliness, overall, value, rooms, location);
  }
}
