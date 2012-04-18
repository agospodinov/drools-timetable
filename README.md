IMPORTANT
=========

While this project is technically working, it is not recommended to run it in a production environment yet.

The goal of this project is to create a working example of Drools Planner in a Web based environment. 
Drools Planner is a platform that can solve planning problems using meta-heuristic algorithms such as Tabu Search and Simulated Annealing.
Due to the nature of the meta-heuristic algorithms, the solver will almost always produce different solutions even with the same input data.

For the non-algorithmic part this project uses Java EE 6 technologies such as JPA, CDI and JSF 2 as well as some modules from Seam Framework 3.

GitHub URL: http://github.com/xstyle/drools-timetable

NOTE: This project is not affiliated with JBoss Drools except that it uses Drools Expert and Drools Planner for optimizing a school timetable. It is a personal project.


Building
========

Building is fairly straightforward. You just need Java and Maven installed and have a fair amount of time if you don't have the libraries in your local repository.

    mvn clean package

When done, you can find the built application in `target/timetable.war`

Deploying
=========

Deploying should be pretty simple as well. You need a Java EE 6 compliant web server (note: I have only tested it with JBoss AS 7, but I imagine it should run with others just as well).

If you're using JBoss AS 7, create a DataSource with the following JNDI name:

    java:jboss/datasources/TimetableDS

After that just copy the built application into your `/path/to/jboss-as/standalone/deployments/` and run.

If you're not using JBoss AS 7, you're on your own for now. Sorry :(
