# ecke_home

This is a personal experiment to try out technologies (REST, OAuth2, Spring and the like). The server is located in a corner (ecke) in my living room.


## Authenticator

Used to catch the refresh token in the google authorization process. 



## Scheduler

Sends a simple GET request to another micro service (implemented in node.js, not part of this repo), that is syncing some google calendar stuff.



## TodoSender

Exposes a simple Api to drop any Line of Text via GET request. Uses the Authentication Tokens from _Authenticator_




## H2WebServer

Helper service to access Data inside the simple H2 File Database.
