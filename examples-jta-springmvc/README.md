Pre Requisite

JAVA 8

mvn package
mvn package -Ppayment_app
mvn package -Pinventory_app


cd target

java -jar examples-jta-springmvc-payment.jar

java -jar examples-jta-springmvc-inventory.jar

java -jar examples-jta-springmvc-client.jar