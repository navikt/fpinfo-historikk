FROM ghcr.io/navikt/fp-baseimages/java:17-appdynamics
LABEL org.opencontainers.image.source=https://github.com/navikt/fpinfo-historikk
COPY target/*.jar app.jar
ENV APPD_ENABLED true
ENV APP_NAME fpinfo-historikk
ENV APPDYNAMICS_CONTROLLER_HOST_NAME appdynamics.adeo.no
ENV APPDYNAMICS_CONTROLLER_PORT 443
ENV APPDYNAMICS_CONTROLLER_SSL_ENABLED true
ENV JAVA_OPTS -XX:MaxRAMPercentage=75 --enable-preview
