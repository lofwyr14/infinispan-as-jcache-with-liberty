package io.openliberty.guides.cart;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Enumeration;

@Path("/")
public class CartResource {

    private Integer counter;

    @Inject
    private SessionInfo sessionInfo;

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(HttpSession session, Integer counter) {
        this.counter = counter;
        session.setAttribute("Counter", counter);
    }

    @POST
    @Path("cart/{item}&{price}")
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponse(responseCode = "200", description = "Item successfully added to cart.")
    @Operation(summary = "Add a new item to cart.")
    public String addToCart(@Context HttpServletRequest request,
                            @Parameter(description = "Item you need for intergalatic travel.",
                                    required = true)
                            @PathParam("item") String item,
                            @Parameter(description = "Price for this item.",
                                    required = true)
                            @PathParam("price") double price) {
        HttpSession session = request.getSession();
        session.setAttribute(item, price);
        Integer counter = (Integer) session.getAttribute("Counter");
        if (counter == null) {
            counter = 1;
        } else {
            counter++;
        }
        session.setAttribute("Counter", counter);
        return item + " added to your cart and costs $" + price;
    }

    @GET
    @Path("cart")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200",
            description = "Items successfully retrieved from your cart.")
    @Operation(summary = "Return an JsonObject instance which contains "
            + "the items in your cart and the subtotal.")
    public JsonObject getCart(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        Enumeration<String> names = session.getAttributeNames();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("pod-name", getHostname());
        builder.add("session-id", session.getId());
        String counter = "";
        if (session.getAttribute("Counter") != null) {
            counter = session.getAttribute("Counter").toString();
        }
        builder.add("Counter", counter);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        double subtotal = 0.0;
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String price = session.getAttribute(name).toString();
            arrayBuilder.add(name + " | $" + price);
            try {
                subtotal += Double.parseDouble(price);
            } catch (NumberFormatException e) {
                System.err.println("Ignoring: " + e.getMessage());
            }
        }
        builder.add("cart", arrayBuilder);
        builder.add("subtotal", subtotal);
        builder.add("sessionInfoCounter", sessionInfo.getCounter());
        return builder.build();
    }

    @GET
    @Path("counter")
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponse(responseCode = "200")
    @Operation(summary = "Returns the counter.")
    public String getCounter(@Context HttpServletRequest request) {
        return String.valueOf(sessionInfo.getCounter()) +
            ';' +
            sessionInfo.getMyPodName() +
            ';' +
            sessionInfo.getMyPodIp() +
            ';' +
            request.getSession().getId() +
            ';';
    }

    private String getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null) {
            hostname = "localhost";
        }
        return hostname;
    }
}