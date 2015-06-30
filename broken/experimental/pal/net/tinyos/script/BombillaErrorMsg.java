/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'BombillaErrorMsg'
 * message type.
 */

package net.tinyos.script;

public class BombillaErrorMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 4;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 29;

    /** Create a new BombillaErrorMsg of size 4. */
    public BombillaErrorMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new BombillaErrorMsg of the given data_length. */
    public BombillaErrorMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg with the given data_length
     * and base offset.
     */
    public BombillaErrorMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg using the given byte array
     * as backing store.
     */
    public BombillaErrorMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public BombillaErrorMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public BombillaErrorMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg embedded in the given message
     * at the given base offset.
     */
    public BombillaErrorMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new BombillaErrorMsg embedded in the given message
     * at the given base offset and length.
     */
    public BombillaErrorMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <BombillaErrorMsg> \n";
      try {
        s += "  [context=0x"+Long.toHexString(get_context())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [reason=0x"+Long.toHexString(get_reason())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [capsule=0x"+Long.toHexString(get_capsule())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [instruction=0x"+Long.toHexString(get_instruction())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: context
    //   Field type: short, unsigned
    //   Offset (bits): 0
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'context' is signed (false).
     */
    public static boolean isSigned_context() {
        return false;
    }

    /**
     * Return whether the field 'context' is an array (false).
     */
    public static boolean isArray_context() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'context'
     */
    public static int offset_context() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'context'
     */
    public static int offsetBits_context() {
        return 0;
    }

    /**
     * Return the value (as a short) of the field 'context'
     */
    public short get_context() {
        return (short)getUIntElement(offsetBits_context(), 8);
    }

    /**
     * Set the value of the field 'context'
     */
    public void set_context(short value) {
        setUIntElement(offsetBits_context(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'context'
     */
    public static int size_context() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'context'
     */
    public static int sizeBits_context() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: reason
    //   Field type: short, unsigned
    //   Offset (bits): 8
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'reason' is signed (false).
     */
    public static boolean isSigned_reason() {
        return false;
    }

    /**
     * Return whether the field 'reason' is an array (false).
     */
    public static boolean isArray_reason() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'reason'
     */
    public static int offset_reason() {
        return (8 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'reason'
     */
    public static int offsetBits_reason() {
        return 8;
    }

    /**
     * Return the value (as a short) of the field 'reason'
     */
    public short get_reason() {
        return (short)getUIntElement(offsetBits_reason(), 8);
    }

    /**
     * Set the value of the field 'reason'
     */
    public void set_reason(short value) {
        setUIntElement(offsetBits_reason(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'reason'
     */
    public static int size_reason() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'reason'
     */
    public static int sizeBits_reason() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: capsule
    //   Field type: short, unsigned
    //   Offset (bits): 16
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'capsule' is signed (false).
     */
    public static boolean isSigned_capsule() {
        return false;
    }

    /**
     * Return whether the field 'capsule' is an array (false).
     */
    public static boolean isArray_capsule() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'capsule'
     */
    public static int offset_capsule() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'capsule'
     */
    public static int offsetBits_capsule() {
        return 16;
    }

    /**
     * Return the value (as a short) of the field 'capsule'
     */
    public short get_capsule() {
        return (short)getUIntElement(offsetBits_capsule(), 8);
    }

    /**
     * Set the value of the field 'capsule'
     */
    public void set_capsule(short value) {
        setUIntElement(offsetBits_capsule(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'capsule'
     */
    public static int size_capsule() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'capsule'
     */
    public static int sizeBits_capsule() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: instruction
    //   Field type: short, unsigned
    //   Offset (bits): 24
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'instruction' is signed (false).
     */
    public static boolean isSigned_instruction() {
        return false;
    }

    /**
     * Return whether the field 'instruction' is an array (false).
     */
    public static boolean isArray_instruction() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'instruction'
     */
    public static int offset_instruction() {
        return (24 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'instruction'
     */
    public static int offsetBits_instruction() {
        return 24;
    }

    /**
     * Return the value (as a short) of the field 'instruction'
     */
    public short get_instruction() {
        return (short)getUIntElement(offsetBits_instruction(), 8);
    }

    /**
     * Set the value of the field 'instruction'
     */
    public void set_instruction(short value) {
        setUIntElement(offsetBits_instruction(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'instruction'
     */
    public static int size_instruction() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'instruction'
     */
    public static int sizeBits_instruction() {
        return 8;
    }

}
