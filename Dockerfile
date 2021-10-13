FROM navikt/java:15-appdynamics
COPY target/*.jar app.jar
ENV APPD_ENABLED=true
ENV APP_NAME=fpinfo-historikk
ENV JAVA_OPTS --enable-preview
