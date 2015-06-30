/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'ConfigMsg'
 * message type.
 */

public class ConfigMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 4;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 35;

    /** Create a new ConfigMsg of size 4. */
    public ConfigMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new ConfigMsg of the given data_length. */
    public ConfigMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg with the given data_length
     * and base offset.
     */
    public ConfigMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg using the given byte array
     * as backing store.
     */
    public ConfigMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public ConfigMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public ConfigMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg embedded in the given message
     * at the given base offset.
     */
    public ConfigMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new ConfigMsg embedded in the given message
     * at the given base offset and length.
     */
    public ConfigMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <ConfigMsg> \n";
      try {
        s += "  [type=0x"+Long.toHexString(get_type())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [samplingInterval=0x"+Long.toHexString(get_samplingInterval())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [DetectionThreshold=0x"+Long.toHexString(get_DetectionThreshold())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [ScanID=0x"+Long.toHexString(get_ScanID())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: type
    //   Field type: short, unsigned
    //   Offset (bits): 0
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'type' is signed (false).
     */
    public static boolean isSigned_type() {
        return false;
    }

    /**
     * Return whether the field 'type' is an array (false).
     */
    public static boolean isArray_type() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'type'
     */
    public static int offset_type() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'type'
     */
    public static int offsetBits_type() {
        return 0;
    }

    /**
     * Return the value (as a short) of the field 'type'
     */
    public short get_type() {
        return (short)getUIntElement(offsetBits_type(), 8);
    }

    /**
     * Set the value of the field 'type'
     */
    public void set_type(short value) {
        setUIntElement(offsetBits_type(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'type'
     */
    public static int size_type() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'type'
     */
    public static int sizeBits_type() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: samplingInterval
    //   Field type: short, unsigned
    //   Offset (bits): 8
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'samplingInterval' is signed (false).
     */
    public static boolean isSigned_samplingInterval() {
        return false;
    }

    /**
     * Return whether the field 'samplingInterval' is an array (false).
     */
    public static boolean isArray_samplingInterval() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'samplingInterval'
     */
    public static int offset_samplingInterval() {
        return (8 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'samplingInterval'
     */
    public static int offsetBits_samplingInterval() {
        return 8;
    }

    /**
     * Return the value (as a short) of the field 'samplingInterval'
     */
    public short get_samplingInterval() {
        return (short)getUIntElement(offsetBits_samplingInterval(), 8);
    }

    /**
     * Set the value of the field 'samplingInterval'
     */
    public void set_samplingInterval(short value) {
        setUIntElement(offsetBits_samplingInterval(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'samplingInterval'
     */
    public static int size_samplingInterval() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'samplingInterval'
     */
    public static int sizeBits_samplingInterval() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: DetectionThreshold
    //   Field type: short, unsigned
    //   Offset (bits): 16
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'DetectionThreshold' is signed (false).
     */
    public static boolean isSigned_DetectionThreshold() {
        return false;
    }

    /**
     * Return whether the field 'DetectionThreshold' is an array (false).
     */
    public static boolean isArray_DetectionThreshold() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'DetectionThreshold'
     */
    public static int offset_DetectionThreshold() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'DetectionThreshold'
     */
    public static int offsetBits_DetectionThreshold() {
        return 16;
    }

    /**
     * Return the value (as a short) of the field 'DetectionThreshold'
     */
    public short get_DetectionThreshold() {
        return (short)getUIntElement(offsetBits_DetectionThreshold(), 8);
    }

    /**
     * Set the value of the field 'DetectionThreshold'
     */
    public void set_DetectionThreshold(short value) {
        setUIntElement(offsetBits_DetectionThreshold(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'DetectionThreshold'
     */
    public static int size_DetectionThreshold() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'DetectionThreshold'
     */
    public static int sizeBits_DetectionThreshold() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: ScanID
    //   Field type: short, unsigned
    //   Offset (bits): 24
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'ScanID' is signed (false).
     */
    public static boolean isSigned_ScanID() {
        return false;
    }

    /**
     * Return whether the field 'ScanID' is an array (false).
     */
    public static boolean isArray_ScanID() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'ScanID'
     */
    public static int offset_ScanID() {
        return (24 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'ScanID'
     */
    public static int offsetBits_ScanID() {
        return 24;
    }

    /**
     * Return the value (as a short) of the field 'ScanID'
     */
    public short get_ScanID() {
        return (short)getUIntElement(offsetBits_ScanID(), 8);
    }

    /**
     * Set the value of the field 'ScanID'
     */
    public void set_ScanID(short value) {
        setUIntElement(offsetBits_ScanID(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'ScanID'
     */
    public static int size_ScanID() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'ScanID'
     */
    public static int sizeBits_ScanID() {
        return 8;
    }

}
