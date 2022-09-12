FROM openjdk:18
COPY target/classes/ /tmp
WORKDIR /tmp
CMD java com.jetbrains.Main