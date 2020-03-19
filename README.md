# java-collections-and-stream

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

1. Run the command bellow to prepare your database, it'll be already configurated.

``` docker-compose -f mysql.yaml up ```

2. Now open the DBeaver to connect to the database, and follow the next steps:
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

* This course is at:
[Alura](https://www.alura.com.br/)
