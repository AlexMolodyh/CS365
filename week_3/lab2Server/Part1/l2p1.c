#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

int main()
{
	pid_t pid, pid1;

	char *who[] = {"who", NULL};	

	pid = fork();

	if(pid < 0)
	{
		printf("Error forking!!\n");
		exit(0);
	}
	else if(pid == 0)
	{
		printf("Launching child process....\n");
		printf("launching who...\n");
		printf("The user is: \n");
		execve("/usr/bin/who", who, NULL);
	}
	else
	{
		wait();
		printf("The Child is done executing: ");
		printf("Exiting parent...\n");
	}
	

	return 0;
}