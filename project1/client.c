#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>

int count_symbol(char symbol, char *str) {
	int acc, i;
	acc = 0;
	for(i = 0; i < strlen(str); i++){
		acc += (str[i] == symbol);
	}
	return acc;
}

int main(int argc, char *argv[]){
	char *HOSTNAME = "3700.network";
	char *PORT = "27993";
	char *NID = "001248102\n";
	//file descriptor for socket
	
	int status;
	struct addrinfo hints;
	memset(&hints, 0, sizeof hints);
	struct addrinfo *servinfo;
	
	// setting up address info
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE;
	
	if ((status = getaddrinfo(HOSTNAME, PORT, &hints, &servinfo)) != 0) {
		printf("Fail to get address info!\n");
	}

	//get the socket file descpritor.
	int sockfd, new_sockfd;
	sockfd = socket(servinfo->ai_family, servinfo->ai_socktype, servinfo-> ai_protocol);

	// bind it to the port in getaddrinfo
	bind(sockfd, servinfo->ai_addr, servinfo->ai_addrlen);

	
	if (connect(sockfd, servinfo->ai_addr, servinfo->ai_addrlen) != 0)
	{
		printf("Fail to connect!\n");
		exit(1);
	}
	//listen
	listen(sockfd, 10);

	//accept
	struct sockaddr_storage new_addr;
	socklen_t addr_size;
	new_sockfd = accept(sockfd, (struct sockaddr *)&new_addr, &addr_size);

	//buffer for hello message
	char buffer[1024];
	char *hellomsg = "cs3700fall2020 HELLO ";
	strcpy(buffer, hellomsg);
	strcat(buffer, NID);//concatenate hello with my neu id

	send(sockfd, buffer, strlen(buffer), 0);
	printf("Hello message sent!\nmsg: %s", buffer);

	char buffer2[8192];
	int sflag;
	
	if(recv(sockfd, buffer2, strlen(buffer2) - 1, 0) == 0 ){
		printf("Connection lost!\n");
	} else {
		printf("Message recieved!\nmsg: %s\n", buffer2);
	}
	
	return 0;
}
