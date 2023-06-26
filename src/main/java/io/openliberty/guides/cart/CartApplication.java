package io.openliberty.guides.cart;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/app/")
public class CartApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> s = new HashSet<>();
    // Ungesicherter Service aus der Demo-Anwendung

    s.add(CartResource.class);
    return s;
  }
}