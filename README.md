Topjava-16 Graduation Project - Restaurant Voting Service
===============================

Stack: Java 11/ Maven/ Spring/ Security/ JPA(Hibernate)/ HSQLDB/ REST(Jackson)/ Swagger/ JUnit5


Main task was: 
-------------------------------------
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and **README.md with API documentation and curl commands to get data for voting and vote.**

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

-----------------------------


Steps to make this thing work:
------------------------------

- Make sure that you have Java 11 and Maven installed on your machine. Also you will need Tomcat 9;
- Download source codes;
- In root directory of the project execute command "mvn clean package". This step will run tests as well;
- Add result package to your Tomcat webapp directory;
- Launch Tomcat;
- With your browser go to the project page - usualy it is a localhost:8080/tj_graduation/
- Authenticate as Admin (admin@example.com - admin) or user (firstuser@example.com - password)
- You can explore API in {contextpath}/swagger-ui.html (require Admin authentication. WARNING - by default Admin doesn't have a role "USER", so he can't make votes)

#### Tips

- By default application use in-memory HSQL database. If you want to fetch data from database, you should deploy standalone HSQLDB server and configure hsqldb.properties in project. [Link](https://www.programmingforfuture.com/2010/06/using-hypersql-hsqldb.html)

#### TODO-s

- Full documentation in Swagger
- Checkout response keys.
- Implement idempotency in POST and PUT requests;
- Try out some Spring Security features;
- Add embedded Tomcat support;
- Add logs, that usable in ELK-stack analysis;
