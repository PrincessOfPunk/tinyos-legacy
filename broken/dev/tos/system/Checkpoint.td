/*									tab:4
 *  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.  By
 *  downloading, copying, installing or using the software you agree to
 *  this license.  If you do not agree to this license, do not download,
 *  install, copy or use the software.
 *
 *  Intel Open Source License 
 *
 *  Copyright (c) 2002 Intel Corporation 
 *  All rights reserved. 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *	Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *	Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *      Neither the name of the Intel Corporation nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE INTEL OR ITS
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 */
/*
 * checkpoint.c - provide a reliable checkpoint to eeprom facility
 *
 * Authors: David Gay
 * History: created 12/19/01
 */

module Checkpoint 
{
  provides {
    interface CheckpointInit;
    interface CheckpointRead;
    interface CheckpointWrite;
  }
  uses {
    interface EEPROMRead;
    interface EEPROMWrite;
    interface StdControl as EEPROMControl;
  }
}
implementation
{

  enum {
    COOKIE = 0x6f776c73,
    MAX_DATA_SETS = TOS_EEPROM_LINE_SIZE
  };

  uint16_t eepromBase;
  uint16_t dataLength;
  uint8_t ndataSets;
  uint8_t noHeader;

  uint8_t currentIndex;
  uint8_t freeDataSet;
  uint8_t index[MAX_DATA_SETS];

  uint8_t eepromLine[TOS_EEPROM_LINE_SIZE];
  uint8_t *readDest;

  uint8_t *userData;
  uint16_t userBytes;
  uint16_t userLine;
  result_t writeResult;

  enum { s_init, 
	 s_load_config_1, s_load_config_2, s_load_config_3,
         s_ready,
	 s_reading,
	 s_writing, s_writing_index, s_writing_selector, s_writing_header }
  state;

  enum {
    HEADER_OFFSET = 0,
    SELECTOR_OFFSET = 1,
    INDEX_OFFSET = 2,
    DATA_OFFSET = 4
  };

  struct header {
    uint32_t cookie;
    uint8_t ndataSets;
    uint16_t dataLength;
  };

  void setState(int n) {
    state = n;
    /*dbgn(n);*/
  }

  uint16_t linesPerSet(uint16_t dlen) {
    return (dlen + TOS_EEPROM_LINE_SIZE - 1) / TOS_EEPROM_LINE_SIZE;
  }

  void clearData() {
    uint8_t i;

    setState(s_ready);
    currentIndex = 0;
    freeDataSet = ndataSets;
    noHeader = TRUE;

    for (i = 0; i < ndataSets; i++)
      index[i] = i;
    for (; i < MAX_DATA_SETS; i++)
      index[i] = 42;

    signal CheckpointInit.initialised(TRUE);
  }

  void headerRead(uint8_t n, uint8_t *to) {
    readDest = to;
    if (!call EEPROMRead.read(eepromBase + n, to))
      clearData();
  }

  void loadConfig() {
    setState(s_load_config_1);
    headerRead(HEADER_OFFSET, eepromLine);
  }

  void loadConfig1() {
    struct header *config = (struct header *)eepromLine;

    if (config->cookie != COOKIE ||
	config->ndataSets != ndataSets ||
	config->dataLength != dataLength)
      {
	clearData();
      }
    else
      {
	setState(s_load_config_2);
	headerRead(SELECTOR_OFFSET, eepromLine);
      }
  }

  void loadConfig2() {
    setState(s_load_config_3);
    currentIndex = eepromLine[0] != 0;
    headerRead(INDEX_OFFSET + currentIndex, index);
  }

  void loadConfig3() {
    /*
      #if MAX_DATA_SETS >= 31
      #error Code below limited to 31 data sets
      #endif
    */
    uint32_t freeSets = (1 << (ndataSets + 1)) - 1;
    uint8_t i, bitcount;
    bool valid = TRUE;
    uint8_t *line = index;
    uint8_t nsets = ndataSets;

    for (i = 0; i < nsets; i++)
      if (line[i] > nsets)
	valid = FALSE;
      else
	freeSets &= ~(1 << line[i]);

    /* More sanity checking, unused entries should be 42 */
    for (; i < MAX_DATA_SETS; i++)
      if (line[i] != 42)
	valid = FALSE;

    /* Should be only one free bit in freeSets */
    bitcount = 0;
    for (i = 0; i <= nsets; i++)
      if (freeSets & (1 << i))
	{
	  bitcount++;
	  freeDataSet = i;
	}

    if (bitcount != 1)
      valid = FALSE;

    if (!valid)
      {
	clearData();
      }
    else
      {
	setState(s_ready);
	noHeader = FALSE;
	signal CheckpointInit.initialised(FALSE);
      }
  }

  command result_t CheckpointInit.init(uint16_t ebase,
				       uint16_t dlen,
				       uint8_t nsets) {
    unsigned int nlinesPerSet;

    setState(s_init);

    eepromBase = ebase;
    ndataSets = nsets;
    dataLength = dlen;

    if (ndataSets >= MAX_DATA_SETS)
      return FAIL;

    /* Truly egregious values will overflow */
    nlinesPerSet = linesPerSet(dlen);
    if (eepromBase + 4 + nlinesPerSet * (ndataSets + 1) >= TOS_EEPROM_MAX_LINES)
      return FAIL;

    if (!call EEPROMControl.init())
      return FAIL;

    loadConfig();
    return SUCCESS;
  }

  void readEnd(result_t success) {
    setState(s_ready);
    signal CheckpointRead.readDone(success, userData + userBytes - dataLength);
  }

  void startNextRead() {
    readDest = eepromLine;
    if (!call EEPROMRead.read(userLine, readDest))
      readEnd(FAIL);
  }

  void processRead() {
    if (userBytes < TOS_EEPROM_LINE_SIZE)
      {
	memcpy(userData, readDest, userBytes);
	readEnd(SUCCESS);
      }
    else
      {
	memcpy(userData, readDest, TOS_EEPROM_LINE_SIZE);
	userData += TOS_EEPROM_LINE_SIZE;
	userBytes -= TOS_EEPROM_LINE_SIZE;
	userLine++;
	startNextRead();
      }
  }

  command result_t CheckpointRead.read(uint8_t data_set, uint8_t *data) {
    if (state != s_ready || data_set >= ndataSets)
      return FAIL;

    userData = data;
    userBytes = dataLength;
    userLine = eepromBase + DATA_OFFSET +
      index[data_set] * linesPerSet(dataLength);
    setState(s_reading);
    startNextRead();

    return SUCCESS;
  }

  event result_t EEPROMRead.readDone(uint8_t *buffer, result_t success) {
    if (buffer == readDest)
      {
	if (success)
	  switch (state)
	    {
	    case s_load_config_1:
	      loadConfig1();
	      break;
	    case s_load_config_2:
	      loadConfig2();
	      break;
	    case s_load_config_3:
	      loadConfig3();
	      break;
	    case s_reading:
	      processRead();
	    default:
	      /* BUG */
	      break;
	    }
	else
	  switch (state)
	    {
	    case s_load_config_1: case s_load_config_2: case s_load_config_3:
	      clearData();
	      break;
	    case s_reading:
	      readEnd(FAIL);
	      break;
	    default:
	      /* BUG */
	      break;
	    }
      }
    return SUCCESS;
  }

  result_t writeFinished() {
    noHeader = FALSE;
    setState(s_ready);
    return 
      signal CheckpointWrite.writeDone(writeResult,
				       userData + userBytes - dataLength);
  }

  event result_t EEPROMWrite.endWriteDone(result_t success) {
    writeResult = rcombine(writeResult, success);
    return writeFinished();
  }

  void endWrite() {
    if (!call EEPROMWrite.endWrite())
      {
	writeResult = FAIL;
	writeFinished();
      }
  } 

  void headerWrite(uint8_t n, uint8_t *data) {
    if (!call EEPROMWrite.write(eepromBase + n, data))
      {
	writeResult = FAIL;
	endWrite();
      }
  }

  void commitWrite() {
    /* Write new index */
    setState(s_writing_index);
    headerWrite(INDEX_OFFSET + !currentIndex, index);
  }

  void writeSelector() {
    currentIndex = !currentIndex;
    eepromLine[0] = currentIndex;
    setState(s_writing_selector);
    headerWrite(SELECTOR_OFFSET, eepromLine);
  }

  void writeHeader() {
    if (noHeader)
      {
	struct header *config = (struct header *)eepromLine;

	config->cookie = COOKIE;
	config->ndataSets = ndataSets;
	config->dataLength = dataLength;
	setState(s_writing_header);
	headerWrite(HEADER_OFFSET, eepromLine);
      }
    else
      endWrite();
  }

  void startNextWrite() {
    /* Writing data from beyond the user's array should be fine ? */
    /* minor worry: what happens at end of SRAM ? */
    if (!call EEPROMWrite.write(userLine, userData))
      {
	writeResult = FAIL;
	endWrite();
      }
  }

  void writeDone() {
    if (userBytes < TOS_EEPROM_LINE_SIZE)
      {
	commitWrite();
      }
    else
      {
	userLine++;
	userData += TOS_EEPROM_LINE_SIZE;
	userBytes -= TOS_EEPROM_LINE_SIZE;
	startNextWrite();
      }
  }

  command result_t CheckpointWrite.write(uint8_t dataSet, uint8_t *data) {
    uint8_t old_free_set;

    if (state != s_ready || dataSet >= ndataSets ||
	!call EEPROMWrite.startWrite())
      return FAIL;

    setState(s_writing);

    /* Update index */
    old_free_set = freeDataSet;
    freeDataSet = index[dataSet];
    index[dataSet] = old_free_set;

    userData = data;
    userBytes = dataLength;
    userLine = eepromBase + DATA_OFFSET +
      old_free_set * linesPerSet(dataLength);
    writeResult = SUCCESS;
    startNextWrite();

    return SUCCESS;
  }

  event result_t EEPROMWrite.writeDone(uint8_t *buffer) {
    switch (state)
      {
      case s_writing:
	writeDone();
	break;
      case s_writing_index:
	writeSelector();
	break;
      case s_writing_selector:
	writeHeader();
	break;
      case s_writing_header:
	endWrite();
	break;
      default:
	/* BUG */
	break;
      }
    return SUCCESS;
  }
}
