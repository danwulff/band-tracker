import org.junit.*;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Band_instantiatesCorrectly_true(){
    Band myBand = new Band("Coldwar Kids");
    assertEquals(true, myBand instanceof Band);
  }

  @Test
  public void getName_bandInstantiatesWithName_String(){
    Band myBand = new Band("Coldwar Kids");
    assertEquals("Coldwar Kids", myBand.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Band.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame_true() {
    Band firstBand = new Band("Coldwar Kids");
    Band secondBand = new Band("Coldwar Kids");
    assertTrue(firstBand.equals(secondBand));
  }

  @Test
  public void save_savesBandBojectIntoDatabase_true() {
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    assertTrue(Band.all().get(0).equals(myBand));
  }

  @Test
  public void save_assignsIdToBandObject() {
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    Band savedBand = Band.all().get(0);
    assertEquals(myBand.getId(), savedBand.getId());
  }

  @Test
  public void find_findsBandsInDatabase_True() {
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    Band savedBand = Band.find(myBand.getId());
    assertTrue(myBand.equals(savedBand));
  }

  @Test
  public void update_updatesBandName_true() {
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    myBand.update("Foxy Shazam");
    assertEquals("Foxy Shazam", Band.find(myBand.getId()).getName());
  }

  @Test
  public void delete_deletesBand_true() {
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    int myBandId = myBand.getId();
    myBand.delete();
    assertEquals(null, Band.find(myBandId));
  }

  @Test
  public void addVenue_addsVenueToBand() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    myBand.addVenue(myVenue);
    Venue savedVenue = myBand.getVenues().get(0);
    assertTrue(myVenue.equals(savedVenue));
  }

  @Test
  public void getVenues_returnsAllVenues_List() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    myBand.addVenue(myVenue);
    List<Venue> savedVenues = myBand.getVenues();
    assertEquals(1, savedVenues.size());
  }


  @Test
  public void delete_deletesAllBandsAndVenuesAssociations() {
    Venue myVenue = new Venue("Carnegie");
    myVenue.save();
    Band myBand = new Band("Coldwar Kids");
    myBand.save();
    myBand.addVenue(myVenue);
    myBand.delete();
    assertEquals(0, myVenue.getBands().size());
  }

}
