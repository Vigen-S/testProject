FROM gradle:7.5-jdk17-focal AS base
MAINTAINER Vigen Stepanyan
COPY ./ /base/src
WORKDIR /base/src
RUN gradle build

FROM openjdk:17-jdk-slim
COPY --from=base /base/src/build/libs/*.jar /tmp
#CMD ["tail", "-f", "/dev/null"]
WORKDIR /tmp
CMD ["java","-jar","demo-0.0.1-SNAPSHOT.jar"]