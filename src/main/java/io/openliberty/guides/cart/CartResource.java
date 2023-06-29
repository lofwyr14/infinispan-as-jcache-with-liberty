package io.openliberty.guides.cart;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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