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
	char *NID = "001248102";
	//file descriptor for socket
	int sockfd = socket(AF_INET, SOCK_STREAM, 0);
	
	struct sockaddr_in address;
	
	char buffer[256];
	char *hellomsg = "HELLO ";
	strcpy(buffer, hellomsg);
	strcat(buffer, NID);//concatenate hello with my neu id
	
	int sock = 0;
	
	address.sin_family = AF_INET;
	address.sin_port = htons(PORT);

	if (inet_pton(AF_INET, HOSTNAME, &address.sin_addr) < 0){
		printf("Invalid address!\n");
	}
	

	if (connect(sock, (struct sockaddr *)&address, sizeof(address) < 0))
	{
		printf("Fail to connect!\n");
	}
	else{
		send(sock, buffer, strlen(buffer), 0);
	}
	
	return 0;
}
