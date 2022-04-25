# we will use openjdk 8 with alpine as it is a very small linux distro
FROM openjdk:11
# copy the packaged jar file into our docker image
COPY target/kube1.jar /kube1.jar
# set the startup command to execute the jar
ENTRYPOINT ["java", "-jar", "/kube1.jar"]