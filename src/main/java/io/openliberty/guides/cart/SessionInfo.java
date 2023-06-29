package io.openliberty.guides.cart;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

@SessionScoped
@Named
public class SessionInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private Integer counter = 0;

  private final Integer objectId = Math.abs(RANDOM.nextInt()) % 1000;

  public SessionInfo() {
    System.out.println("Constructor of SessionInfo");
  }

//  public void preRenderComponent(PreRenderComponentEvent event) {
//    System.out.println("PreRenderComponentEvent");
//    setCounter(getCounter() + 1);
//    final Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//    System.out.printf("class name='%s', session='%s'", session.getClass().getName(), session);
//  }

  public void increaseCounter() {
    System.out.println("increaseCounter");
    setCounter(counter + 1);
  }

  public Integer getObjectId() {
    return objectId;
  }

  public Integer getCounter() {
    increaseCounter();
    return counter;
  }

  public void setCounter(Integer counter) {
    this.counter = counter;
  }

  public String getMyPodName() {
    return System.getenv().get("MY_POD_NAME");
  }

  public String getMyPodNamespace() {
    return System.getenv().get("MY_POD_NAMESPACE");
  }

  public String getMyPodIp() {
    return System.getenv().get("MY_POD_IP");
  }

  public String getSessionId() {
    return FacesContext.getCurrentInstance().getExternalContext().getSessionId(false);
  }

  public Integer getIdentityHashCode() {
    return System.identityHashCode(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionInfo that = (SessionInfo) o;
    return Objects.equals(counter, that.counter)
        && Objects.equals(objectId,
        that.objectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(counter, objectId);
  }

  @Override
  public String toString() {
    return String.format("SessionInfo{counter=%d, objectId=%d}", counter, objectId);
  }
}
