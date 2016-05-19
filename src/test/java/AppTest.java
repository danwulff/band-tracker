import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("band tracker");
  }

  @Test
  public void bandsPageDisplaysBandName() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    goTo("http://localhost:4567/bands");
    assertThat(pageSource()).contains("Citizen Cope");
  }

  @Test
  public void bandIsCreatedTest() {
    goTo("http://localhost:4567/bands");
    fill("#name").with("Citizen Cope");
    submit(".btn");
    assertThat(pageSource()).contains("Citizen Cope");
  }

  @Test
  public void individualBandPageDisplaysBandName() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    String url = String.format("http://localhost:4567/bands/%d", testBand.getId());
    goTo(url);
    assertThat(pageSource()).contains("Citizen Cope");
  }

  @Test
  public void individualBandPageDisplaysConnectedVenues() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    Venue testVenue = new Venue("Carnegie");
    testVenue.save();
    testBand.addVenue(testVenue);
    String url = String.format("http://localhost:4567/bands/%d", testBand.getId());
    goTo(url);
    assertThat(pageSource()).contains("Carnegie");
  }

  @Test
  public void individualBandPageAddsVenues() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    Venue testVenue = new Venue("Carnegie");
    testVenue.save();
    String url = String.format("http://localhost:4567/bands/%d", testBand.getId());
    goTo(url);
    fillSelect("#venue_id").withText("Carnegie");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Carnegie");
  }

  @Test
  public void bandEditPageDisplays() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    String url = String.format("http://localhost:4567/bands/%d/edit", testBand.getId());
    goTo(url);
    assertThat(pageSource()).contains("Edit Citizen Cope");
  }


  @Test
  public void bandUpdateChangesName() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    String url = String.format("http://localhost:4567/bands/%d/edit", testBand.getId());
    goTo(url);
    fill("#name").with("Foxy Shazam");
    click("button", withText("Update name"));
    assertThat(pageSource()).contains("Foxy Shazam");
  }

  @Test
  public void bandDeleteRemovesBand() {
    Band testBand = new Band("Citizen Cope");
    testBand.save();
    String url = String.format("http://localhost:4567/bands/%d/edit", testBand.getId());
    goTo(url);
    click("button", withText("Delete"));
    assertEquals(0, Band.all().size());
  }

  //Venue pages tests------------------------------------------
  @Test
  public void venuesPageDisplaysVenueName() {
    Venue testVenue = new Venue("Carnegie");
    testVenue.save();
    goTo("http://localhost:4567/venues");
    assertThat(pageSource()).contains("Carnegie");
  }

  @Test
  public void venueIsCreatedTest() {
    goTo("http://localhost:4567/venues");
    fill("#name").with("Carnegie");
    submit(".btn");
    assertThat(pageSource()).contains("Carnegie");
  }

}
