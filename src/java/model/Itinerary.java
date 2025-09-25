package model;

public class Itinerary {

    private int itinerary_id;
    private int trip_id;
    private int day_number;
    private String description;

    public Itinerary() {
    }

    public Itinerary(int itinerary_id, int trip_id, int day_number, String description) {
        this.itinerary_id = itinerary_id;
        this.trip_id = trip_id;
        this.day_number = day_number;
        this.description = description;
    }

    public int getItinerary_id() {
        return itinerary_id;
    }

    public void setItinerary_id(int itinerary_id) {
        this.itinerary_id = itinerary_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getDay_number() {
        return day_number;
    }

    public void setDay_number(int day_number) {
        this.day_number = day_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Itinerary{" + "itinerary_id=" + itinerary_id + ", trip_id=" + trip_id + ", day_number=" + day_number + ", description=" + description + '}';
    }
}


