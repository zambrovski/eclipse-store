# Spring Boot Eclipse Store Sample Application

This application serves as a comprehensive display of the Eclipse Store Spring Boot extension's functionalities. It exemplifies the seamless integration of a pre-configured storage into an application as a bean, illustrates effective utilization of the locking API, and provides insights into designing an MVC application using Eclipse Store.

Moreover, it offers practical demonstrations on leveraging concurrent extensions within the Eclipse Store API, showcasing the implementation of annotations like `@Read` and `@Write` for optimized functionality.

## Implementation Notes
* Synchronization over shared data structures is handled using the annotations `@Read` and `@Write`.
* The Controller performs data conversion into JSON, therefore it must always operate with immutable data or, in the case of mutable data, it must receive a copy of such data.
* Minimum Java version is 17.
* Build tool is Maven.

## Build

The generation of the executable jar file can be performed by issuing the following command:

```shell
mvn clean package
```

This will create an executable jar file **spring-boot3-simple.jar** within the _target_ maven folder. This can be started by executing the following command

```shell
java -jar target/spring-boot3-simple.jar
```

To launch the test page, open your browser at the following URL
```shell
http://localhost:8080/  
```

## Using App rest api
This examples use Curl for Linux/macOS. You can use any other tool for sending HTTP requests.
Load sample data. Without this step, the application will not contain any data. It is not mandatory to execute this step, but without it you will have to add all your data manually.
```shell
curl --location --request POST 'http://localhost:8080/init' \
--header 'Content-Type: application/json'
```

Get all jokes
```shell
curl --location --request GET 'http://localhost:8080' \
--header 'Content-Type: application/json'
```

Get joke by Id
```shell
curl --location --request GET 'http://localhost:8080/joke?id=50' \
--header 'Content-Type: application/json' 
```

Add new Joke
```shell
curl --location --request POST 'http://localhost:8080/add' \
--header 'Content-Type: application/json' \
--data-raw '{great new joke}'
```
