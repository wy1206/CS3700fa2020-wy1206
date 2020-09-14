#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#define PORT 27993

//file descriptor for socket
int sockfd = socekt(AF_INET, SOCK_STREAM, 0);

struct sockaddr_in serv_addr;

char *hellomsg = "HELLO 001248102\n";

int count(char symbol, char *str) {
	int acc, i;
	acc = 0;
	for(i = 0; i < strlen(str); i++){
		if (str[i] == symbol) {
			acc++;
		}
	}
	return acc
}

int main(int argc, char *argv[]){
	int sock = 0;
	
	serv_addr.sin_family = AF_INET;
	serv_addr.sin.port = ntons(PORT);

	if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr) < 0))
	{
		printf("\nFail to connect! \n");
	}
	else{
		send(sock, hellomsg, strlen(hellomsg), 0);
	}
	//...
}
