## Messenger
- REST-приложение на Spring Boot
- В качестве БД используется PostgreSQL
- БД запускается в Docker
- Flyway для инициализации БД
- ORM (Hibernate)
- Пагинация
- Настройка доступа при помощи Spring Security
- Stateless-сессии с подтверждением авторизации через JWT-токен

#### run Docker with PostgreSQL:

step 1:
```sh
docker pull postgres
```
step 2:
```sh
docker run --rm --name pgdocker -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=project_db -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
```
