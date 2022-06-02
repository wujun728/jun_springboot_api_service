spring.application.name=${projectName}

spring.cloud.config.enabled=true
spring.cloud.config.label=master
spring.cloud.config.fail-fast=true
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-service

eureka.client.serviceUrl.defaultZone=@eureka-url@
eureka.instance.prefer-ip-address=true

spring.profiles.active=@deploy.env@
spring.cloud.config.profile=@deploy.env@

spring.cloud.stream.kafka.binder.brokers=@kafka-url@
spring.kafka.bootstrap-servers=@kafka-url@

#logging.config=classpath:logback-spring.xml
server.port=8080