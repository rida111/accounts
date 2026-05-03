# Use maintained OpenJDK base image
FROM eclipse-temurin:17-jdk-jammy

#deprecated: 
# MAINTAINER ridasfa1 

#there is a jar in my target folder to be coppied to my docker image
copy target/accounts-0.0.1-SNAPSHOT.jar accounts-1.0.jar


#AS THE CONTAINER GOT CREATED FROM IMAGE => EXECUTE THE FOLLOWING COMMAND
#execute the application
ENTRYPOINT ["java", "-jar", "accounts-1.0.jar"]

#the preceding command is equivalent to:
#equivalent to java -jar target/accounts-0.0.1-SNAPSHOT.jar 
