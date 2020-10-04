*this is a README file for project 2.* 
<p>
In this project, I managed to complete the implementation in 5 steps.
1) The first step is commandline parsing which correctly manipulated the data in args. 
This involved the implementation of my ReadURL method which based on whether the given
parameter start with "ftp" or not. 2) The second step is connection building which 
establish the connection to the FTP server with given username and password. 3) The 
next step is implementing the method to open a data channel for the client, and this
include the correct usage of PASV. Meanwhile, implementing ls for the client is done 
within this step. 4) Implementing MKD and RMD for the client would took the least time
in this project. 5) the last part of the project is implementing STOR, RETR and DELE
which required the client open a data channel and correctly upload, download and 
delete files in to/from the server. This is the most time-consuming part of the
project since I tried different way of reading and writing file in order to intereact
with the server.
</p>

