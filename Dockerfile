FROM arm64v8/eclipse-temurin:17
COPY /backend/build/libs/*.jar /backend/app.jar
ENTRYPOINT ["java","-jar","/backend/app.jar","--spring.profiles.active=prod"]