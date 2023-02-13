FROM openjdk:11-jre
LABEL Maintainer="Julian Razif Figaro" \
      Description="Va Service"

USER root
RUN apt-get update && apt-get -y install nano net-tools dnsutils

#use shell script or pipeline to move from target folder to build folder
ADD build/afaja-e-cash-wallet-va-service.jar /usr/share/lib/afaja-e-cash-wallet-va-service.jar
ENV JAVA_OPTS="-Xms1g -Xmx1g"
ENTRYPOINT  ["sh", "-c", "java -jar $JAVA_OPTS /usr/share/lib/afaja-e-cash-wallet-va-service.jar"]
