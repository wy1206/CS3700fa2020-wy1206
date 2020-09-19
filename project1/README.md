/* this is the README file*/
The client program is written in java by creating socket connection to the server with the hostname of "3700.network" and port 27993.
we are supposed to implement the intereaction between server and client via receiving "FIND" and "BYE" message from server and send "HELLO" and "COUNT" message to the server.
I am using PrintStream as my output stream that sent to the server which makes the program eaiser to send message constantly, and a buffer is constructed for input stream that read message from server utill it read a new line. Each time I receive message from server, I print that message out onto stdout so I can clearly see whether the server provided me correct secret flag.
