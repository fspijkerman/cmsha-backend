FROM java:8

VOLUME /tmp

ADD @project.name@-@project.version@.jar @project.name@.jar

RUN bash -c 'touch /@project.name@.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/@project.name@.jar"]

EXPOSE 8080
