Complete Google App Engine + Spring 4 Web App Example
=====================================================

This repository contains best practices for an JEE (Spring based) application architecture. It's a great boilerplate to start JEE web apps and deploy them to GAE. 

### Introduction

This sample web app integrate the following technologies, methodologies and tools:

* Google App Engine (Java) for its back end
* Objectify for persistence layer
* Java Classes organized in the following packages 
	+ Domain
	+ Repository (with the DAO pattern)
	+ Service
	+ DTO (to adapt the domain objects for the Spring MVC models)
	+ Controller (using Spring MVC)
* RESTful Spring MVC web controllers
* i18n
* JSTL
* Apache Tiles
* Spring Security (with customized user accounts)
* Test Driven development (Test units and integration tests)
* Maven

You can learn a lot just reading the source code. 

Getting Started
---------------

### Running the app

To run the app at localhost using maven type the following command in your terminal, at the root folder of the project:
``` 
mvn appengine:devserver
```
And launch your web browser to `http://localhost:8080/` and sign in with a default user:

``` 
User: admin
Password: admin
```

``` 
User: user
Password: user
```

Each user has a different role (for authentication and authorization purposes).


### Deploying the app

To deploy the app on GAE, change fill the following tag in pom.xml with your application name:
``` 
<app.id>example</app.id>
```

And run
```
mvn appengine:update
```

That's all.

### Default file system structure
``` 
+---src
   +---main
   ¦   +---java
   ¦   ¦   +---com
   ¦   ¦       +---namespace
   ¦   ¦           +---domain
   ¦   ¦           +---repository
   ¦   ¦           +---service
   ¦   ¦           ¦   +---dto
   ¦   ¦           ¦   +---validator
   ¦   ¦           +---util
   ¦   ¦           +---web
   ¦   +---resources
   ¦   ¦   +---META-INF
   ¦   ¦       +---spring
   ¦   +---webapp
   ¦       +---images
   ¦       +---js
   ¦       +---META-INF
   ¦       +---styles
   ¦       +---WEB-INF
   ¦           +---i18n
   ¦           +---layouts
   ¦           +---spring
   ¦           ¦   +---exampleServlet
   ¦           +---views
   ¦               +---account
   ¦               ¦   +---settings
   ¦               +---commons
   ¦               +---users
   ¦                   +---create
   ¦                   +---disabled
   ¦                   +---enabled
   ¦                   +---update
   +---test
       +---java
       ¦   +---com
       ¦       +---namespace
       ¦           +---domain
       ¦           +---repository
       ¦           ¦   +---mock
       ¦           +---service
       ¦           ¦   +---dto
       ¦           ¦   +---mock
       ¦           ¦   +---validator
       ¦           +---util
       ¦           +---web
       +---resources
```


### TODO list

* Better integration of Apache Tiles with Spring Framework
* Improve the test units, uncomment test cases and fix them
* Fill all the internationalization fields 
* Write the pagination for the user lists
* Better error handling for the DAO layer (using GAE specific Exceptions)
