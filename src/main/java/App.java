import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {

  public static void main (String[] args){
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get ("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get ("/bands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("bands", Band.all());
      model.put("template", "templates/bands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post ("/bands", (request, response) -> {
      String name = request.queryParams("name");
      if (name.equals("")) {
        response.redirect("/bands");
        return null;
      }
      Band newBand = new Band(name);
      newBand.save();
      response.redirect("/bands");
      return null;
    });

    get ("/bands/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band thisBand = Band.find(id);
      model.put("band", thisBand);
      model.put("venues", Venue.all());
      model.put("template", "templates/band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post ("/add_venue", (request, response) -> {
      int bandId = Integer.parseInt(request.queryParams("band_id"));
      int venueId = Integer.parseInt(request.queryParams("venue_id"));
      Band thisBand = Band.find(bandId);
      thisBand.addVenue(Venue.find(venueId));
      String url = String.format("http://localhost:4567/bands/%d", bandId);
      response.redirect(url);
      return null;
    });

    get ("/bands/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Band thisBand = Band.find(id);
      model.put("band", thisBand);
      model.put("template", "templates/band-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post ("/bands/:id", (request, response) -> {
      int id = Integer.parseInt(request.params("id"));
      Band thisBand = Band.find(id);
      String name = request.queryParams("name");
      if (name.equals("")) {
        String url = String.format("http://localhost:4567/bands/%d/edit", id);
        response.redirect(url);
        return null;
      }
      thisBand.update(name);
      String url = String.format("http://localhost:4567/bands/%d", id);
      response.redirect(url);
      return null;
    });

    post ("/bands/:id/delete", (request, response) -> {
      int id = Integer.parseInt(request.params("id"));
      Band thisBand = Band.find(id);
      thisBand.delete();
      response.redirect("http://localhost:4567/bands");
      return null;
    });

    //Venue pages------------------------------------------
    get ("/venues", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("venues", Venue.all());
      model.put("template", "templates/venues.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post ("/venues", (request, response) -> {
      String name = request.queryParams("name");
      if (name.equals("")) {
        response.redirect("/venues");
        return null;
      }
      Venue newVenue = new Venue(name);
      newVenue.save();
      response.redirect("/venues");
      return null;
    });

    get ("/venues/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Venue thisVenue = Venue.find(id);
      model.put("venue", thisVenue);
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
