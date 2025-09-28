# Spring Boot

## Migrations Database
```sh
# primeira vez ------
./mvnw flyway:migrate \
  -Dflyway.url=jdbc:postgresql://172.17.0.1:5403/postgres \
  -Dflyway.user=postgres \
  -Dflyway.password=postgres \
  -Dflyway.locations=classpath:db/migration


# next times -------
./mvnw resources:resources -DskipTests
#./mvnw clean resources:resources -DskipTests
 
./mvnw flyway:migrate \
  -Dflyway.url=jdbc:postgresql://172.17.0.1:5403/postgres \
  -Dflyway.user=postgres \
  -Dflyway.password=postgres \
  -Dflyway.locations=classpath:db/migration


# mas directo, lee file system ---
./mvnw flyway:migrate \
  -Dflyway.locations=filesystem:src/main/resources/db/migration
```


## Py scaffold
```sh
python3 ./scaffold_spring.py \
  --base-package com.ecommerce.api \
  --feature auth \
  --entity Role \
  --mapstruct

```