# testtask

./gradlew build && java -jar build/libs/interview-task-1.0-SNAPSHOT.jar

OR

./gradlew bootRun

To run application.

Use your favorite REST client to test. https://www.getpostman.com/ suits well.



http://localhost:8080/hit
--------------------------
Request method: POST

Request body: {"userId":"user1","uri":"uri155112df32363"}

Returns: {"hits": 137, "unique": 17}}


http://localhost:8080/hits
--------------------------
Request method: POST

Request body: {"begin": "2017-04-07T21:03:54.126+0000","end": "2019-04-08T22:03:54.126+0000"}

Returns: {"hits": 137,"unique": 17,"retained": 6,"hitsRange": {"begin": "2017-04-07T21:03:54.000+0000","end": "2019-04-08T22:03:54.000+0000"}}