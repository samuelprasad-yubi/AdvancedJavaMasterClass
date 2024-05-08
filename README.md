# JavaMasterClassAdvancedCaseStudy
 JMC Case Study Advanced

Steps to run the program :

1. This is a 3 Gradle Project. Clone the project and build each one separately using ./gradlew clean build.
2. Then, create MySQL databases named "auth" and "student".
3. Start the DiscoveryHub first.
4. Next, start the student service and EasyAuth application.
5. At the application's start, a new admin user will be automatically created with the username "admin" and password "4c782eB4278aC".
6. You can use these user credentials to add new users with the role "admin" (who have access for adding new users) or "user".
7. The Swagger UI URLs are as follows: for auth, it's http://localhost:8080/swagger-ui/index.html#, for student, it's http://localhost:8080/swagger-ui/index.html#, and for DiscoveryHub, it's http://localhost:8761.
8. To access every API in the student service, you need to provide authorization by logging in to Swagger, clicking on the "Authorize" button, and entering the JWT provided in the response of the api/auth/signin API of the EasyAuth application.
9. Every API of the student service will internally call the EasyAuth application for authorization.
10. You can use the student service to upload a set of student records at /students/upload. A sample format file is provided in the API /students/sampleCSV.
11. You can modify a student's record by ID using the API /students (PUT).
12. You can search for a student by ID in /students/{id}.
13. You can search for students by filter in /students/search.
14. You can also export the filtered results as a CSV file by sending the filter in /students/export

#Have Fun
