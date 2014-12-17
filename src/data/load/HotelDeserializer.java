package data.load;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import data.obj.Hotel;
import data.obj.Review;

final class HotelDeserializer implements JsonDeserializer<Hotel> {

  @Override
  public Hotel deserialize(final JsonElement json, final Type type,
      final JsonDeserializationContext context) throws JsonParseException {

    // get a json object representation of the whole file
    final JsonObject jsonObject = json.getAsJsonObject();

    // deserialize all the reviews
    Review[] reviews = context.deserialize(jsonObject.get("Reviews"), Review[].class);

    // fill out and return a new Hotel object
    return new Hotel(reviews);
  }
}
