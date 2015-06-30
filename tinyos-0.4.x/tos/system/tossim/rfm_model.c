#include "rfm_model.h"
#include "tossim.h"
#include "external_comm.h"
#include "dbg.h"

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

// Variables for the simple model
int isTransmitting[TOSNODES];
int transmitting[TOSNODES];
int radio_active[TOSNODES];

void simple_init() {
  int i;
  for (i = 0; i < tos_state.num_nodes; i++) {
    radio_active[i] = 0;
  }
}

void simple_transmit(int moteID, char bit) {
  int i;
  
  transmitting[moteID] = bit;
  writeOutRadioBit(tos_state.tos_time, (short)moteID, bit);
  
  for (i = 0; i < tos_state.num_nodes; i++) {
    radio_active[i] += bit;
  }
}

void simple_stops_transmit(int moteID) {
  int i;
  
  if (transmitting[moteID]) {
    transmitting[moteID] = 0;
    for (i = 0; i < tos_state.num_nodes; i++) {
      radio_active[i]--;
    }
  }
}

char simple_hears(int moteID) {
  return (radio_active[moteID] > 0)? 1:0;
}

rfm_model* create_simple_model() {
  rfm_model* model = (rfm_model*)malloc(sizeof(rfm_model));
  model->init = simple_init;
  model->transmit = simple_transmit;
  model->stop_transmit = simple_stops_transmit;
  model->hears = simple_hears;
  return model;
}

char connectivity[TOSNODES][TOSNODES];

int read_entry(FILE* file, int* mote_one, int* mote_two) {
  char buf[128];
  int index = 0;
  int ch;

  // Read in first number
  while(1) {
    ch = getc(file);
    if (ch == EOF) {return 0;}
    else if (ch >= '0' && ch <= '9') {
      buf[index] = (char)ch;
      index++;
    }
    else if (ch == ':') {
      buf[index] = 0;
      break;
    }
    else if (ch == '\n' || ch == ' ' || ch == '\t') {
      if (index > 0) {return 0;}
    }
    else {
      return 0;
    }
  }

  *mote_one = atoi(buf);
  index = 0;
  // Read in second number
  while(1) {
    ch = getc(file);
    if (ch == EOF) {return 0;}
    else if (ch >= '0' && ch <= '9') {
      buf[index] = (char)ch;
      index++;
    }
    else if (ch == '\n' || ch == ' ' || ch == '\t') {
      if (index == 0) {return 0;}
      else {
	buf[index] = 0;
	break;
      }
    }
    else {
      return 0;
    }
  }

  *mote_two = atoi(buf);
  return 1;
}

void static_one_cell_init() {
  int i,j;
  
  for (i = 0; i < TOSNODES; i++) {
    for (j = 0; j < TOSNODES; j++) {
      connectivity[i][j] = 1;
    }
  }
}

void static_init() {
  int fd = open("cells.txt", O_RDONLY);
  FILE* file = fdopen(fd, "r");
  
  if (fd < 0) {
    dbg(DBG_ERROR, ("No cells.txt found for static rfm model. Defaulting to one cell.\n"));
    static_one_cell_init();
    return;
  }


  while(1) {
    int mote_one;
    int mote_two;
    if (read_entry(file, &mote_one, &mote_two)) {
      connectivity[mote_one][mote_two] = 1;
      connectivity[mote_two][mote_one] = 1;
    }
    else {
      break;
    }
  }
  dbg(DBG_BOOT, ("RFM connectivity graph constructed.\n"));
}

void static_transmit(int moteID, char bit) {
  int i;
  
  transmitting[moteID] = bit;
  writeOutRadioBit(tos_state.tos_time, (short)moteID, bit);
  
  for (i = 0; i < tos_state.num_nodes; i++) {
    if (connectivity[moteID][i]) {
      radio_active[i] += bit;
    }
  }
}

void static_stops_transmit(int moteID) {
  int i;
  
  if (transmitting[moteID]) {
    transmitting[moteID] = 0;
    for (i = 0; i < tos_state.num_nodes; i++) {
      if (connectivity[moteID][i]) {
	radio_active[i]--;
      }
    }
  }
}

char static_hears(int moteID) {
  return (radio_active[moteID] > 0)? 1:0;
}


rfm_model* create_static_model() {
  rfm_model* model = (rfm_model*)malloc(sizeof(rfm_model));
  model->init = static_init;
  model->transmit = static_transmit;
  model->stop_transmit = static_stops_transmit;
  model->hears = static_hears;
  return model;
}
