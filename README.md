i did created new project from scratch spring boot webflux reactive programming and latest spring security and java version 17 .
project integration
 fly way for track DP history and run script
 swagger  URL project baseurl/webjars/swagger-ui/index.html.
 docker compose file to run dp inside docker container once project start up - docker must be run in platform .
Missing from implementation duo to time out
  @preauthorize - @secured - hasPremission did not work under controller layer or service layer seems bug in spring security even override defaulMethodSecurity bean not allowed  so i did simple check to handle access specific API.
  did not complete junit test duo to time run out .
  did not test notify job -> email service -  did not add swagger comment annotations over methods .
  did implement sub task or child tasks logic business.
  did not add profiles .

start project : mvn spring-boot:run