import org.junit.*;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class VenueTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Venue_instantiatesCorrectly_true(){
    Venue myVenue = new Venue("Carnegie");
    assertEquals(true, myVenue instanceof Venue);
  }

  @Test
  public void getName_bandInstantiatesWithName_String(){
    Venue myVenue = new Venue("Carnegie");
    assertEquals("Carnegie", myVenue.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Venue.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame_true() {
    Venue firstVenue = new Venue("Carnegie");
    Venue secondVenue = new Venue("Carnegie");
    assertTrue(firstVenue.equals(secondVenue));
  }

  @Test
  public void save_savesVenueBojectIntoDatabase_true() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    assertTrue(Venue.all().get(0).equals(myVenue));
  }

  @Test
  public void save_assignsIdToVenueObject() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    Venue savedVenue = Venue.all().get(0);
    assertEquals(myVenue.getId(), savedVenue.getId());
  }

  @Test
  public void find_findsVenuesInDatabase_True() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    Venue savedVenue = Venue.find(myVenue.getId());
    assertTrue(myVenue.equals(savedVenue));
  }

  @Test
  public void update_updatesVenueName_true() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    myVenue.update("Red Rocks");
    assertEquals("Red Rocks", Venue.find(myVenue.getId()).getName());
  }

  @Test
  public void delete_deletesVenue_true() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    int myVenueId = myVenue.getId();
    myVenue.delete();
    assertEquals(null, Venue.find(myVenueId));
  }

}
