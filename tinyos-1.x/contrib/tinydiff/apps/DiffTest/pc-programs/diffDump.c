
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <inttypes.h>
#include <stdlib.h>
#include <string.h>

#include "myAM.h"
#include "attribute.h"
#include "OnePhasePull.h"
#include "OPPLib/DataStructures.h"
#include "msg_types.h"

#define MOTE_DEV "/dev/mote/0/tos"

void printAttributes(Attribute *array, uint8_t num)
{
  uint8_t i = 0;

  printf("-------attributes-------\n");
  for (i = 0; i < num; i++)
  {
    printf("#1: key = %d op = %d value = %d\n", array[i].key,
	   array[i].op, array[i].value);
  }
  printf("------------------------\n");
}

void printInterest(TOS_MsgPtr tosMsg)
{
  InterestMessage *intMsg;

  intMsg = (InterestMessage *)tosMsg->data;
  printf("INTEREST: sink: %d seq = %d prev = %d ttl = %d exp = %d\n", 
	  intMsg->sink, intMsg->seqNum, intMsg->prevHop, 
	  intMsg->ttl, intMsg->expiration);
  printAttributes(intMsg->attributes, intMsg->numAttrs);
  
  printf("\n");
}

void printData(TOS_MsgPtr tosMsg)
{
  DataMessage *dataMsg;

  dataMsg = (DataMessage *)tosMsg->data;
  printf("DATA: src: %d seq = %d prev = %d hop2src = %d\n",
	  dataMsg->source, dataMsg->seqNum, dataMsg->prevHop, 
	  dataMsg->hopsToSrc);
  printAttributes(dataMsg->attributes, dataMsg->numAttrs);
  
  printf("\n");
}

int main(int argc, char * argv[]) 
{
  int fd;
  uint8_t j;
  char dev[255]=MOTE_DEV;

  TOS_Msg msg;	

  // Optional: use a different tos device than 0.
  switch (argc) {
    case 2:
      j=strtol(argv[1], (char **)NULL, 10);
      sprintf(dev, "/dev/mote/%d/tos", j);
    break;
  }

  fd = open(dev, O_RDWR);
  if (fd<0) {
    printf("failed to open %s\n", dev);
    exit(1);
  }

  while (1) {

    int status = read(fd, &msg, sizeof(msg));

    if (status == sizeof(msg)) {
      if (msg.type == ESS_OPP_INTEREST)
      {
	printInterest(&msg);
      }
      else if (msg.type == ESS_OPP_DATA)
      {
	printData(&msg);
      }
    }
    else {
      perror("read failed");
    }

    fflush(NULL);
  }

  return 0;
}
