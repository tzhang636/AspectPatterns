package data.obj;

public final class Ratings {

  private final int service;
  private final int cleanliness;
  private final double overall;
  private final int value;
  private final int rooms;
  private final int location;

  private String string = null;

  public Ratings(int service, int cleanliness, double overall, int value, int rooms, int location) {
    this.service = service;
    this.cleanliness = cleanliness;
    this.overall = overall;
    this.value = value;
    this.rooms = rooms;
    this.location = location;
  }

  @Override
  public String toString() {
    if (string == null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Service: ").append(service).append(", ");
      sb.append("Cleanliness: ").append(cleanliness).append(", ");
      sb.append("Overall: ").append(overall).append(", ");
      sb.append("Value: ").append(value).append(", ");
      sb.append("Rooms: ").append(rooms).append(", ");
      sb.append("Location: ").append(location);
      string = sb.toString();
    }
    return string;
  }
}
