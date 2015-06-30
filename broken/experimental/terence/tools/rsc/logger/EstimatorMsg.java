/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'EstimatorMsg'
 * message type.
 */



public class EstimatorMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 21;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 12;

    /** Create a new EstimatorMsg of size 21. */
    public EstimatorMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new EstimatorMsg of the given data_length. */
    public EstimatorMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg with the given data_length
     * and base offset.
     */
    public EstimatorMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg using the given byte array
     * as backing store.
     */
    public EstimatorMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public EstimatorMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public EstimatorMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg embedded in the given message
     * at the given base offset.
     */
    public EstimatorMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EstimatorMsg embedded in the given message
     * at the given base offset and length.
     */
    public EstimatorMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <EstimatorMsg> \n";
      try {
        s += "  [addr=0x"+Long.toHexString(get_addr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [amtype=0x"+Long.toHexString(get_amtype())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [group=0x"+Long.toHexString(get_group())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [length=0x"+Long.toHexString(get_length())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [oldGoodness=0x"+Long.toHexString(get_oldGoodness())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [lastSeqnum=0x"+Long.toHexString(get_lastSeqnum())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [oldNew=0x"+Long.toHexString(get_oldNew())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [received=0x"+Long.toHexString(get_received())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [missed=0x"+Long.toHexString(get_missed())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [goodness=0x"+Long.toHexString(get_goodness())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [expTotal=0x"+Long.toHexString(get_expTotal())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [total=0x"+Long.toHexString(get_total())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [new=0x"+Long.toHexString(get_new())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [whichSource=0x"+Long.toHexString(get_whichSource())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [source=0x"+Long.toHexString(get_source())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [seqnum=0x"+Long.toHexString(get_seqnum())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [crc=0x"+Long.toHexString(get_crc())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: addr
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'addr' is signed (false).
     */
    public static boolean isSigned_addr() {
        return false;
    }

    /**
     * Return whether the field 'addr' is an array (false).
     */
    public static boolean isArray_addr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'addr'
     */
    public static int offset_addr() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'addr'
     */
    public static int offsetBits_addr() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'addr'
     */
    public int get_addr() {
        return (int)getUIntElement(offsetBits_addr(), 16);
    }

    /**
     * Set the value of the field 'addr'
     */
    public void set_addr(int value) {
        setUIntElement(offsetBits_addr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'addr'
     */
    public static int size_addr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'addr'
     */
    public static int sizeBits_addr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: amtype
    //   Field type: short, unsigned
    //   Offset (bits): 16
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'amtype' is signed (false).
     */
    public static boolean isSigned_amtype() {
        return false;
    }

    /**
     * Return whether the field 'amtype' is an array (false).
     */
    public static boolean isArray_amtype() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'amtype'
     */
    public static int offset_amtype() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'amtype'
     */
    public static int offsetBits_amtype() {
        return 16;
    }

    /**
     * Return the value (as a short) of the field 'amtype'
     */
    public short get_amtype() {
        return (short)getUIntElement(offsetBits_amtype(), 8);
    }

    /**
     * Set the value of the field 'amtype'
     */
    public void set_amtype(short value) {
        setUIntElement(offsetBits_amtype(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'amtype'
     */
    public static int size_amtype() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'amtype'
     */
    public static int sizeBits_amtype() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: group
    //   Field type: short, unsigned
    //   Offset (bits): 24
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'group' is signed (false).
     */
    public static boolean isSigned_group() {
        return false;
    }

    /**
     * Return whether the field 'group' is an array (false).
     */
    public static boolean isArray_group() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'group'
     */
    public static int offset_group() {
        return (24 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'group'
     */
    public static int offsetBits_group() {
        return 24;
    }

    /**
     * Return the value (as a short) of the field 'group'
     */
    public short get_group() {
        return (short)getUIntElement(offsetBits_group(), 8);
    }

    /**
     * Set the value of the field 'group'
     */
    public void set_group(short value) {
        setUIntElement(offsetBits_group(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'group'
     */
    public static int size_group() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'group'
     */
    public static int sizeBits_group() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: length
    //   Field type: short, unsigned
    //   Offset (bits): 32
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'length' is signed (false).
     */
    public static boolean isSigned_length() {
        return false;
    }

    /**
     * Return whether the field 'length' is an array (false).
     */
    public static boolean isArray_length() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'length'
     */
    public static int offset_length() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'length'
     */
    public static int offsetBits_length() {
        return 32;
    }

    /**
     * Return the value (as a short) of the field 'length'
     */
    public short get_length() {
        return (short)getUIntElement(offsetBits_length(), 8);
    }

    /**
     * Set the value of the field 'length'
     */
    public void set_length(short value) {
        setUIntElement(offsetBits_length(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'length'
     */
    public static int size_length() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'length'
     */
    public static int sizeBits_length() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: oldGoodness
    //   Field type: short, unsigned
    //   Offset (bits): 40
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'oldGoodness' is signed (false).
     */
    public static boolean isSigned_oldGoodness() {
        return false;
    }

    /**
     * Return whether the field 'oldGoodness' is an array (false).
     */
    public static boolean isArray_oldGoodness() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'oldGoodness'
     */
    public static int offset_oldGoodness() {
        return (40 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'oldGoodness'
     */
    public static int offsetBits_oldGoodness() {
        return 40;
    }

    /**
     * Return the value (as a short) of the field 'oldGoodness'
     */
    public short get_oldGoodness() {
        return (short)getUIntElement(offsetBits_oldGoodness(), 8);
    }

    /**
     * Set the value of the field 'oldGoodness'
     */
    public void set_oldGoodness(short value) {
        setUIntElement(offsetBits_oldGoodness(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'oldGoodness'
     */
    public static int size_oldGoodness() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'oldGoodness'
     */
    public static int sizeBits_oldGoodness() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: lastSeqnum
    //   Field type: short, unsigned
    //   Offset (bits): 48
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'lastSeqnum' is signed (false).
     */
    public static boolean isSigned_lastSeqnum() {
        return false;
    }

    /**
     * Return whether the field 'lastSeqnum' is an array (false).
     */
    public static boolean isArray_lastSeqnum() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'lastSeqnum'
     */
    public static int offset_lastSeqnum() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'lastSeqnum'
     */
    public static int offsetBits_lastSeqnum() {
        return 48;
    }

    /**
     * Return the value (as a short) of the field 'lastSeqnum'
     */
    public short get_lastSeqnum() {
        return (short)getUIntElement(offsetBits_lastSeqnum(), 8);
    }

    /**
     * Set the value of the field 'lastSeqnum'
     */
    public void set_lastSeqnum(short value) {
        setUIntElement(offsetBits_lastSeqnum(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'lastSeqnum'
     */
    public static int size_lastSeqnum() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'lastSeqnum'
     */
    public static int sizeBits_lastSeqnum() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: oldNew
    //   Field type: short, unsigned
    //   Offset (bits): 56
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'oldNew' is signed (false).
     */
    public static boolean isSigned_oldNew() {
        return false;
    }

    /**
     * Return whether the field 'oldNew' is an array (false).
     */
    public static boolean isArray_oldNew() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'oldNew'
     */
    public static int offset_oldNew() {
        return (56 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'oldNew'
     */
    public static int offsetBits_oldNew() {
        return 56;
    }

    /**
     * Return the value (as a short) of the field 'oldNew'
     */
    public short get_oldNew() {
        return (short)getUIntElement(offsetBits_oldNew(), 8);
    }

    /**
     * Set the value of the field 'oldNew'
     */
    public void set_oldNew(short value) {
        setUIntElement(offsetBits_oldNew(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'oldNew'
     */
    public static int size_oldNew() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'oldNew'
     */
    public static int sizeBits_oldNew() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: received
    //   Field type: int, unsigned
    //   Offset (bits): 64
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'received' is signed (false).
     */
    public static boolean isSigned_received() {
        return false;
    }

    /**
     * Return whether the field 'received' is an array (false).
     */
    public static boolean isArray_received() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'received'
     */
    public static int offset_received() {
        return (64 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'received'
     */
    public static int offsetBits_received() {
        return 64;
    }

    /**
     * Return the value (as a int) of the field 'received'
     */
    public int get_received() {
        return (int)getUIntElement(offsetBits_received(), 16);
    }

    /**
     * Set the value of the field 'received'
     */
    public void set_received(int value) {
        setUIntElement(offsetBits_received(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'received'
     */
    public static int size_received() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'received'
     */
    public static int sizeBits_received() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: missed
    //   Field type: int, unsigned
    //   Offset (bits): 80
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'missed' is signed (false).
     */
    public static boolean isSigned_missed() {
        return false;
    }

    /**
     * Return whether the field 'missed' is an array (false).
     */
    public static boolean isArray_missed() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'missed'
     */
    public static int offset_missed() {
        return (80 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'missed'
     */
    public static int offsetBits_missed() {
        return 80;
    }

    /**
     * Return the value (as a int) of the field 'missed'
     */
    public int get_missed() {
        return (int)getUIntElement(offsetBits_missed(), 16);
    }

    /**
     * Set the value of the field 'missed'
     */
    public void set_missed(int value) {
        setUIntElement(offsetBits_missed(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'missed'
     */
    public static int size_missed() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'missed'
     */
    public static int sizeBits_missed() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: goodness
    //   Field type: short, unsigned
    //   Offset (bits): 96
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'goodness' is signed (false).
     */
    public static boolean isSigned_goodness() {
        return false;
    }

    /**
     * Return whether the field 'goodness' is an array (false).
     */
    public static boolean isArray_goodness() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'goodness'
     */
    public static int offset_goodness() {
        return (96 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'goodness'
     */
    public static int offsetBits_goodness() {
        return 96;
    }

    /**
     * Return the value (as a short) of the field 'goodness'
     */
    public short get_goodness() {
        return (short)getUIntElement(offsetBits_goodness(), 8);
    }

    /**
     * Set the value of the field 'goodness'
     */
    public void set_goodness(short value) {
        setUIntElement(offsetBits_goodness(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'goodness'
     */
    public static int size_goodness() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'goodness'
     */
    public static int sizeBits_goodness() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: expTotal
    //   Field type: short, unsigned
    //   Offset (bits): 104
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'expTotal' is signed (false).
     */
    public static boolean isSigned_expTotal() {
        return false;
    }

    /**
     * Return whether the field 'expTotal' is an array (false).
     */
    public static boolean isArray_expTotal() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'expTotal'
     */
    public static int offset_expTotal() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'expTotal'
     */
    public static int offsetBits_expTotal() {
        return 104;
    }

    /**
     * Return the value (as a short) of the field 'expTotal'
     */
    public short get_expTotal() {
        return (short)getUIntElement(offsetBits_expTotal(), 8);
    }

    /**
     * Set the value of the field 'expTotal'
     */
    public void set_expTotal(short value) {
        setUIntElement(offsetBits_expTotal(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'expTotal'
     */
    public static int size_expTotal() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'expTotal'
     */
    public static int sizeBits_expTotal() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: total
    //   Field type: short, unsigned
    //   Offset (bits): 112
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'total' is signed (false).
     */
    public static boolean isSigned_total() {
        return false;
    }

    /**
     * Return whether the field 'total' is an array (false).
     */
    public static boolean isArray_total() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'total'
     */
    public static int offset_total() {
        return (112 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'total'
     */
    public static int offsetBits_total() {
        return 112;
    }

    /**
     * Return the value (as a short) of the field 'total'
     */
    public short get_total() {
        return (short)getUIntElement(offsetBits_total(), 8);
    }

    /**
     * Set the value of the field 'total'
     */
    public void set_total(short value) {
        setUIntElement(offsetBits_total(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'total'
     */
    public static int size_total() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'total'
     */
    public static int sizeBits_total() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: new
    //   Field type: short, unsigned
    //   Offset (bits): 120
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'new' is signed (false).
     */
    public static boolean isSigned_new() {
        return false;
    }

    /**
     * Return whether the field 'new' is an array (false).
     */
    public static boolean isArray_new() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'new'
     */
    public static int offset_new() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'new'
     */
    public static int offsetBits_new() {
        return 120;
    }

    /**
     * Return the value (as a short) of the field 'new'
     */
    public short get_new() {
        return (short)getUIntElement(offsetBits_new(), 8);
    }

    /**
     * Set the value of the field 'new'
     */
    public void set_new(short value) {
        setUIntElement(offsetBits_new(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'new'
     */
    public static int size_new() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'new'
     */
    public static int sizeBits_new() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: whichSource
    //   Field type: short, unsigned
    //   Offset (bits): 128
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'whichSource' is signed (false).
     */
    public static boolean isSigned_whichSource() {
        return false;
    }

    /**
     * Return whether the field 'whichSource' is an array (false).
     */
    public static boolean isArray_whichSource() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'whichSource'
     */
    public static int offset_whichSource() {
        return (128 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'whichSource'
     */
    public static int offsetBits_whichSource() {
        return 128;
    }

    /**
     * Return the value (as a short) of the field 'whichSource'
     */
    public short get_whichSource() {
        return (short)getUIntElement(offsetBits_whichSource(), 8);
    }

    /**
     * Set the value of the field 'whichSource'
     */
    public void set_whichSource(short value) {
        setUIntElement(offsetBits_whichSource(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'whichSource'
     */
    public static int size_whichSource() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'whichSource'
     */
    public static int sizeBits_whichSource() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: source
    //   Field type: short, unsigned
    //   Offset (bits): 136
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'source' is signed (false).
     */
    public static boolean isSigned_source() {
        return false;
    }

    /**
     * Return whether the field 'source' is an array (false).
     */
    public static boolean isArray_source() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'source'
     */
    public static int offset_source() {
        return (136 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'source'
     */
    public static int offsetBits_source() {
        return 136;
    }

    /**
     * Return the value (as a short) of the field 'source'
     */
    public short get_source() {
        return (short)getUIntElement(offsetBits_source(), 8);
    }

    /**
     * Set the value of the field 'source'
     */
    public void set_source(short value) {
        setUIntElement(offsetBits_source(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'source'
     */
    public static int size_source() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'source'
     */
    public static int sizeBits_source() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: seqnum
    //   Field type: byte, unsigned
    //   Offset (bits): 144
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'seqnum' is signed (false).
     */
    public static boolean isSigned_seqnum() {
        return false;
    }

    /**
     * Return whether the field 'seqnum' is an array (false).
     */
    public static boolean isArray_seqnum() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'seqnum'
     */
    public static int offset_seqnum() {
        return (144 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'seqnum'
     */
    public static int offsetBits_seqnum() {
        return 144;
    }

    /**
     * Return the value (as a byte) of the field 'seqnum'
     */
    public byte get_seqnum() {
        return (byte)getSIntElement(offsetBits_seqnum(), 8);
    }

    /**
     * Set the value of the field 'seqnum'
     */
    public void set_seqnum(byte value) {
        setSIntElement(offsetBits_seqnum(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'seqnum'
     */
    public static int size_seqnum() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'seqnum'
     */
    public static int sizeBits_seqnum() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: crc
    //   Field type: int, unsigned
    //   Offset (bits): 152
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'crc' is signed (false).
     */
    public static boolean isSigned_crc() {
        return false;
    }

    /**
     * Return whether the field 'crc' is an array (false).
     */
    public static boolean isArray_crc() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'crc'
     */
    public static int offset_crc() {
        return (152 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'crc'
     */
    public static int offsetBits_crc() {
        return 152;
    }

    /**
     * Return the value (as a int) of the field 'crc'
     */
    public int get_crc() {
        return (int)getUIntElement(offsetBits_crc(), 16);
    }

    /**
     * Set the value of the field 'crc'
     */
    public void set_crc(int value) {
        setUIntElement(offsetBits_crc(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'crc'
     */
    public static int size_crc() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'crc'
     */
    public static int sizeBits_crc() {
        return 16;
    }

}
