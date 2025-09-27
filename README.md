# Spring Boot

## Migrations Database
```sh
./mvnw flyway:migrate \
  -Dflyway.url=jdbc:postgresql://172.17.0.1:5403/postgres \
  -Dflyway.user=postgres \
  -Dflyway.password=postgres \
  -Dflyway.locations=classpath:db/migration
```
