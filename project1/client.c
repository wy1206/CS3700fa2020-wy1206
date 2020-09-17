#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <arpa/inet.h>

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
	int PORT = 27993;
	char *NID = "001248102\n";
	//file descriptor for socket
	int sockfd = socket(AF_INET, SOCK_STREAM, 0);
	
	struct sockaddr_in address;
	//buffer for hello message
	char buffer[1024];
	char *hellomsg = "HELLO ";
	strcpy(buffer, hellomsg);
	strcat(buffer, NID);//concatenate hello with my neu id
	
	address.sin_family = AF_INET;
	address.sin_addr.s_addr = inet_addr(HOSTNAME);
	address.sin_port = htons(PORT);

	if (inet_pton(AF_INET, HOSTNAME, &address.sin_addr) < 0){
		printf("Invalid address!\n");
	}
	

	if (connect(sockfd, (struct sockaddr *)&address, sizeof(address)) != 0)
	{
		printf("Fail to connect!\n");
	}
	else{
		send(sockfd, buffer, strlen(buffer), 0);
		printf("Hello message sent!\n");
	}
	
	return 0;
}
