## Локальный запуск проложения в Docker-compose
##### В корне проекта находятся два файла Dockerfile и docker-compose.yml. 
##### Вместе с проектом будет поднята БД и PGAdmin для работы с БД
##### Для запукса нужно перейти в корень с проектом и ввести команду *docker-compose up -d*
___
### Для подключения к БД через PGAdmin:
##### В браузере нужно заходить по адресу *<host_name>:5050*
##### Логин и пароль для авторизации: Логин - admin@admin.com , пароль - root
___
### Для подключения к БД
##### Строка подклюения к БД *<host_name>:5432/task_db*
##### Пользователь и пароль - student
___
### Доступ к Swagger
#### Swagger доступен по ссылке *http://<host_name>:8004/swagger-ui/index.html*




