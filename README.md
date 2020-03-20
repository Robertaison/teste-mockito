# teste-mockito

## This repository intend to explore tests implementation using mockito framework.

> To run the tests, the environment must be prepared, I prepared some things that can help, ready the steps bellow.

### Stack used to test
 * JUnit
 * Hamcrest
 * Mockito
 
### Preparing the environment
 
> You may need some information that isn't here, i'll let some links that can be useful.
 
#### Things you must have 
 * Docker
 * Docker-compose
 * DBeaver
 
1. Import the project

2. Import the external libraries, it will be at 'external-libs' folder

3. Run the commands bellow to prepare your database, it'll be already configurated. (Uou must be in the file folder to run docker-compose command) 

``` 
cd infra-context
docker-compose -f mysql.yaml up 
```

4. Now open the DBeaver to connect to the database, and follow the next steps:
  * At the top, in the Database tab, click on 'New Database Connection'
  * Choose MySQL
  * To connect, use the following configuration
```
  Server Host: localhost
  Port: 3306
  Database: mocks
  User name: root
  Password: root
```  
  * In the application, we have a directory called infra-context, copy script in db_migration.sql to DBeaver and execut it, each command individually, following the order.
  
5. Now you can run your tests, enjoy!!

##### Support Material
 * [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
 * [Docker-compose](https://dbeaver.io/download/)
 * [DBeaver](https://dbeaver.io/download/)

##### This course is at:
[Alura](https://www.alura.com.br/)
