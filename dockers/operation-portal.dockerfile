# Build stage
FROM maven:3.9.6 AS build
RUN mkdir -p /opt/app/implementation
RUN mkdir -p /opt/app/lib
WORKDIR /opt/app

COPY implementation /opt/app/implementation/

RUN mvn clean -f /opt/app/implementation/component/mod_fspiop/ -P local clean install
RUN mvn clean -f /opt/app/implementation/ install -DskipTests -P local
RUN mvn clean -f /opt/app/implementation/web_api_operation/ install -DskipTests -P local

FROM eclipse-temurin:21-jdk-alpine

# Create a directory for custom fonts
RUN mkdir -p /usr/share/fonts/custom

# Copy font files to the container
COPY ./dockers/fonts/calibri.ttf /usr/share/fonts/custom/
COPY ./dockers/fonts/calibri_bold.ttf /usr/share/fonts/custom/

# Set permissions for the font files (optional, ensures readability)
RUN chmod 644 /usr/share/fonts/custom/*.ttf

# Update font cache
RUN fc-cache -f -v

## Install bash
#RUN apt-get update && apt-get install -y bash

RUN mkdir -p /opt/app
RUN mkdir -p /opt/app/lib

COPY --from=build /opt/app/implementation/web_api_operation/target/operation_api.jar /opt/app/operation_api.jar
COPY --from=build /opt/app/implementation/web_api_operation/target/lib/* /opt/app/lib/

# Copy entrypoint script and make it executable
COPY ./dockers/docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

WORKDIR /opt/app/

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]

EXPOSE 8002