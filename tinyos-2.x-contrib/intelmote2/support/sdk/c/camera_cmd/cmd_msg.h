/**
 * This file is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This file defines the layout of the 'cmd_msg' message type.
 */

#ifndef CMD_MSG_H
#define CMD_MSG_H
#include <message.h>

enum {
  /** The default size of this message type in bytes. */
  CMD_MSG_SIZE = 7,

  /** The Active Message type associated with this message. */
  CMD_MSG_AM_TYPE = 4,

  /* Field node_id: type uint16_t, offset (bits) 0, size (bits) 16 */
  /** Offset (in bytes) of the field 'node_id' */
  CMD_MSG_NODE_ID_OFFSET = 0,
  /** Offset (in bits) of the field 'node_id' */
  CMD_MSG_NODE_ID_OFFSETBITS = 0,
  /** Size (in bytes) of the field 'node_id' */
  CMD_MSG_NODE_ID_SIZE = 2,
  /** Size (in bits) of the field 'node_id' */
  CMD_MSG_NODE_ID_SIZEBITS = 16,

  /* Field cmd: type uint8_t, offset (bits) 16, size (bits) 8 */
  /** Offset (in bytes) of the field 'cmd' */
  CMD_MSG_CMD_OFFSET = 2,
  /** Offset (in bits) of the field 'cmd' */
  CMD_MSG_CMD_OFFSETBITS = 16,
  /** Size (in bytes) of the field 'cmd' */
  CMD_MSG_CMD_SIZE = 1,
  /** Size (in bits) of the field 'cmd' */
  CMD_MSG_CMD_SIZEBITS = 8,

  /* Field val1: type uint16_t, offset (bits) 24, size (bits) 16 */
  /** Offset (in bytes) of the field 'val1' */
  CMD_MSG_VAL1_OFFSET = 3,
  /** Offset (in bits) of the field 'val1' */
  CMD_MSG_VAL1_OFFSETBITS = 24,
  /** Size (in bytes) of the field 'val1' */
  CMD_MSG_VAL1_SIZE = 2,
  /** Size (in bits) of the field 'val1' */
  CMD_MSG_VAL1_SIZEBITS = 16,

  /* Field val2: type uint16_t, offset (bits) 40, size (bits) 16 */
  /** Offset (in bytes) of the field 'val2' */
  CMD_MSG_VAL2_OFFSET = 5,
  /** Offset (in bits) of the field 'val2' */
  CMD_MSG_VAL2_OFFSETBITS = 40,
  /** Size (in bytes) of the field 'val2' */
  CMD_MSG_VAL2_SIZE = 2,
  /** Size (in bits) of the field 'val2' */
  CMD_MSG_VAL2_SIZEBITS = 16,
};

/**
 * Return the value of the field 'node_id'
 */
uint16_t cmd_msg_node_id_get(tmsg_t *msg);

/**
 * Set the value of the field 'node_id'
 */
void cmd_msg_node_id_set(tmsg_t *msg, uint16_t value);

/**
 * Return the value of the field 'cmd'
 */
uint8_t cmd_msg_cmd_get(tmsg_t *msg);

/**
 * Set the value of the field 'cmd'
 */
void cmd_msg_cmd_set(tmsg_t *msg, uint8_t value);

/**
 * Return the value of the field 'val1'
 */
uint16_t cmd_msg_val1_get(tmsg_t *msg);

/**
 * Set the value of the field 'val1'
 */
void cmd_msg_val1_set(tmsg_t *msg, uint16_t value);

/**
 * Return the value of the field 'val2'
 */
uint16_t cmd_msg_val2_get(tmsg_t *msg);

/**
 * Set the value of the field 'val2'
 */
void cmd_msg_val2_set(tmsg_t *msg, uint16_t value);

#endif
