	FROM openjdk:17
	
	WORKDIR /app
	
	COPY target/enrollmentapp-0.0.1-SNAPSHOT.jar enrollmentapp.jar
	
	EXPOSE 8084
	
	ENTRYPOINT ["java", "-jar", "enrollmentapp.jar"]