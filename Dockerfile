FROM open-liberty:23.0.0.4-full-java11-openj9

ARG VERSION=1.0
ARG REVISION=SNAPSHOT

#ENV HZ_NETWORK_JOIN_KUBERNETES_SERVICENAME="cart-service-hazelcast"
#ENV HZ_K8S_ENABLED=true

LABEL \
  org.opencontainers.image.authors="Your Name" \
  org.opencontainers.image.vendor="Open Liberty" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.source="https://github.com/OpenLiberty/guide-sessions" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  vendor="Open Liberty" \
  name="cart-app" \
  version="$VERSION-$REVISION" \
  summary="The cart application from the Sessions guide" \
  description="This image contains the cart application running with the Open Liberty runtime."

COPY --chown=1001:0 src/main/liberty/config /config/
COPY --chown=1001:0 target/guide-sessions.war /config/apps
COPY --chown=1001:0 target/docker-liberty/lib/infinispan /opt/ol/wlp/usr/shared/resources/infinispan

RUN configure.sh
