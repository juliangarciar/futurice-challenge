# Futurice challenge

This project is a code challenge for an open position from Futurice. It's open to everyone who wants to play with it.

# About the project

The project is a REST API, with two end-points:

  ·GET /calculus/encode?query=[]
  
  This end-point expects a HTTP Method of type GET, with a parameter named 'query' which is expected to
  be an UTF-8 encoded URI of a mathematical expression. It will serve a Base64 encoded string of this expression
  without structured body.
  
  ·GET /calculus?query=[]
  
  This end-point expects a HTTP Method of type GET, with a parameter named 'query' which is expected to
  be an UTF-8 Base64 encoded string of a mathematical expression. You can utilize the previous end-point 
  in order to generate one. 
  
  This method makes use of regular expressions to group these in simpler ones, then validates them.
  In case the expression is correct, it will operate it having in count the next technical constraints.
  
    · The method supports floating numbers, like 20.3, just make sure to use dots.
    · () The method can operate through nested parentheses.
    · +- The method can operate sum and substraction operations.
    · */ The method can also operate product and division operations.
    · The method also accounts for minus signed values, like 1--4 (this will be traduced as 1 + 4).
    · The method also supports random spaces between symbols and numbers, like 10 +4 * 3*( 1 + 2).

# Building and running the project

  The project has been built using Java 11 and Maven 3.6.2.
  
  To run it, build it using maven and then execute the jar:
  
    mvn clean install
    java -jar target/codechallenge-1.0.0-SNAPSHOT.jar
    
  Or use the spring-boot goal:

    mvn spring-boot:run
  
  If you need to listen on another port, you can change the .properties as you wish, or alternatively use:
  
    java -jar target/codechallenge-1.0.0-SNAPSHOT.jar --server.port=8080
  

