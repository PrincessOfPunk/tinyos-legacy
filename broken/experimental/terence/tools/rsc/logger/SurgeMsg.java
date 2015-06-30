/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'SurgeMsg'
 * message type.
 */



public class SurgeMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 31;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 17;

    /** Create a new SurgeMsg of size 31. */
    public SurgeMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new SurgeMsg of the given data_length. */
    public SurgeMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg with the given data_length
     * and base offset.
     */
    public SurgeMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg using the given byte array
     * as backing store.
     */
    public SurgeMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public SurgeMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public SurgeMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg embedded in the given message
     * at the given base offset.
     */
    public SurgeMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new SurgeMsg embedded in the given message
     * at the given base offset and length.
     */
    public SurgeMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <SurgeMsg> \n";
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
        s += "  [type=0x"+Long.toHexString(get_type())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [sourceaddr=0x"+Long.toHexString(get_sourceaddr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [originaddr=0x"+Long.toHexString(get_originaddr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [parentaddr=0x"+Long.toHexString(get_parentaddr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [seqno=0x"+Long.toHexString(get_seqno())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [hopcount=0x"+Long.toHexString(get_hopcount())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [reading=0x"+Long.toHexString(get_reading())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [parent_link_quality=0x"+Long.toHexString(get_parent_link_quality())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [nbrs=";
        for (int i = 0; i < 4; i++) {
          s += "0x"+Long.toHexString(getElement_nbrs(i) & 0xff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [q=";
        for (int i = 0; i < 4; i++) {
          s += "0x"+Long.toHexString(getElement_q(i) & 0xff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [debug_code=0x"+Long.toHexString(get_debug_code())+"]\n";
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
    // Accessor methods for field: type
    //   Field type: short, unsigned
    //   Offset (bits): 40
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
        return (40 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'type'
     */
    public static int offsetBits_type() {
        return 40;
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
    // Accessor methods for field: sourceaddr
    //   Field type: int, unsigned
    //   Offset (bits): 48
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'sourceaddr' is signed (false).
     */
    public static boolean isSigned_sourceaddr() {
        return false;
    }

    /**
     * Return whether the field 'sourceaddr' is an array (false).
     */
    public static boolean isArray_sourceaddr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'sourceaddr'
     */
    public static int offset_sourceaddr() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'sourceaddr'
     */
    public static int offsetBits_sourceaddr() {
        return 48;
    }

    /**
     * Return the value (as a int) of the field 'sourceaddr'
     */
    public int get_sourceaddr() {
        return (int)getUIntElement(offsetBits_sourceaddr(), 16);
    }

    /**
     * Set the value of the field 'sourceaddr'
     */
    public void set_sourceaddr(int value) {
        setUIntElement(offsetBits_sourceaddr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'sourceaddr'
     */
    public static int size_sourceaddr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'sourceaddr'
     */
    public static int sizeBits_sourceaddr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: originaddr
    //   Field type: int, unsigned
    //   Offset (bits): 64
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'originaddr' is signed (false).
     */
    public static boolean isSigned_originaddr() {
        return false;
    }

    /**
     * Return whether the field 'originaddr' is an array (false).
     */
    public static boolean isArray_originaddr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'originaddr'
     */
    public static int offset_originaddr() {
        return (64 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'originaddr'
     */
    public static int offsetBits_originaddr() {
        return 64;
    }

    /**
     * Return the value (as a int) of the field 'originaddr'
     */
    public int get_originaddr() {
        return (int)getUIntElement(offsetBits_originaddr(), 16);
    }

    /**
     * Set the value of the field 'originaddr'
     */
    public void set_originaddr(int value) {
        setUIntElement(offsetBits_originaddr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'originaddr'
     */
    public static int size_originaddr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'originaddr'
     */
    public static int sizeBits_originaddr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: parentaddr
    //   Field type: int, unsigned
    //   Offset (bits): 80
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'parentaddr' is signed (false).
     */
    public static boolean isSigned_parentaddr() {
        return false;
    }

    /**
     * Return whether the field 'parentaddr' is an array (false).
     */
    public static boolean isArray_parentaddr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'parentaddr'
     */
    public static int offset_parentaddr() {
        return (80 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'parentaddr'
     */
    public static int offsetBits_parentaddr() {
        return 80;
    }

    /**
     * Return the value (as a int) of the field 'parentaddr'
     */
    public int get_parentaddr() {
        return (int)getUIntElement(offsetBits_parentaddr(), 16);
    }

    /**
     * Set the value of the field 'parentaddr'
     */
    public void set_parentaddr(int value) {
        setUIntElement(offsetBits_parentaddr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'parentaddr'
     */
    public static int size_parentaddr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'parentaddr'
     */
    public static int sizeBits_parentaddr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: seqno
    //   Field type: short, unsigned
    //   Offset (bits): 96
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'seqno' is signed (false).
     */
    public static boolean isSigned_seqno() {
        return false;
    }

    /**
     * Return whether the field 'seqno' is an array (false).
     */
    public static boolean isArray_seqno() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'seqno'
     */
    public static int offset_seqno() {
        return (96 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'seqno'
     */
    public static int offsetBits_seqno() {
        return 96;
    }

    /**
     * Return the value (as a short) of the field 'seqno'
     */
    public short get_seqno() {
        return (short)getUIntElement(offsetBits_seqno(), 8);
    }

    /**
     * Set the value of the field 'seqno'
     */
    public void set_seqno(short value) {
        setUIntElement(offsetBits_seqno(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'seqno'
     */
    public static int size_seqno() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'seqno'
     */
    public static int sizeBits_seqno() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: hopcount
    //   Field type: short, unsigned
    //   Offset (bits): 104
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'hopcount' is signed (false).
     */
    public static boolean isSigned_hopcount() {
        return false;
    }

    /**
     * Return whether the field 'hopcount' is an array (false).
     */
    public static boolean isArray_hopcount() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'hopcount'
     */
    public static int offset_hopcount() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'hopcount'
     */
    public static int offsetBits_hopcount() {
        return 104;
    }

    /**
     * Return the value (as a short) of the field 'hopcount'
     */
    public short get_hopcount() {
        return (short)getUIntElement(offsetBits_hopcount(), 8);
    }

    /**
     * Set the value of the field 'hopcount'
     */
    public void set_hopcount(short value) {
        setUIntElement(offsetBits_hopcount(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'hopcount'
     */
    public static int size_hopcount() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'hopcount'
     */
    public static int sizeBits_hopcount() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: reading
    //   Field type: int, unsigned
    //   Offset (bits): 112
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'reading' is signed (false).
     */
    public static boolean isSigned_reading() {
        return false;
    }

    /**
     * Return whether the field 'reading' is an array (false).
     */
    public static boolean isArray_reading() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'reading'
     */
    public static int offset_reading() {
        return (112 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'reading'
     */
    public static int offsetBits_reading() {
        return 112;
    }

    /**
     * Return the value (as a int) of the field 'reading'
     */
    public int get_reading() {
        return (int)getUIntElement(offsetBits_reading(), 16);
    }

    /**
     * Set the value of the field 'reading'
     */
    public void set_reading(int value) {
        setUIntElement(offsetBits_reading(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'reading'
     */
    public static int size_reading() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'reading'
     */
    public static int sizeBits_reading() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: parent_link_quality
    //   Field type: short, unsigned
    //   Offset (bits): 128
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'parent_link_quality' is signed (false).
     */
    public static boolean isSigned_parent_link_quality() {
        return false;
    }

    /**
     * Return whether the field 'parent_link_quality' is an array (false).
     */
    public static boolean isArray_parent_link_quality() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'parent_link_quality'
     */
    public static int offset_parent_link_quality() {
        return (128 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'parent_link_quality'
     */
    public static int offsetBits_parent_link_quality() {
        return 128;
    }

    /**
     * Return the value (as a short) of the field 'parent_link_quality'
     */
    public short get_parent_link_quality() {
        return (short)getUIntElement(offsetBits_parent_link_quality(), 8);
    }

    /**
     * Set the value of the field 'parent_link_quality'
     */
    public void set_parent_link_quality(short value) {
        setUIntElement(offsetBits_parent_link_quality(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'parent_link_quality'
     */
    public static int size_parent_link_quality() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'parent_link_quality'
     */
    public static int sizeBits_parent_link_quality() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: nbrs
    //   Field type: short[], unsigned
    //   Offset (bits): 136
    //   Size of each element (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'nbrs' is signed (false).
     */
    public static boolean isSigned_nbrs() {
        return false;
    }

    /**
     * Return whether the field 'nbrs' is an array (true).
     */
    public static boolean isArray_nbrs() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'nbrs'
     */
    public static int offset_nbrs(int index1) {
        int offset = 136;
        if (index1 < 0 || index1 >= 4) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'nbrs'
     */
    public static int offsetBits_nbrs(int index1) {
        int offset = 136;
        if (index1 < 0 || index1 >= 4) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return offset;
    }

    /**
     * Return the entire array 'nbrs' as a short[]
     */
    public short[] get_nbrs() {
        short[] tmp = new short[4];
        for (int index0 = 0; index0 < numElements_nbrs(0); index0++) {
            tmp[index0] = getElement_nbrs(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'nbrs' from the given short[]
     */
    public void set_nbrs(short[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_nbrs(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a short) of the array 'nbrs'
     */
    public short getElement_nbrs(int index1) {
        return (short)getUIntElement(offsetBits_nbrs(index1), 8);
    }

    /**
     * Set an element of the array 'nbrs'
     */
    public void setElement_nbrs(int index1, short value) {
        setUIntElement(offsetBits_nbrs(index1), 8, value);
    }

    /**
     * Return the total size, in bytes, of the array 'nbrs'
     */
    public static int totalSize_nbrs() {
        return (32 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'nbrs'
     */
    public static int totalSizeBits_nbrs() {
        return 32;
    }

    /**
     * Return the size, in bytes, of each element of the array 'nbrs'
     */
    public static int elementSize_nbrs() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'nbrs'
     */
    public static int elementSizeBits_nbrs() {
        return 8;
    }

    /**
     * Return the number of dimensions in the array 'nbrs'
     */
    public static int numDimensions_nbrs() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'nbrs'
     */
    public static int numElements_nbrs() {
        return 4;
    }

    /**
     * Return the number of elements in the array 'nbrs'
     * for the given dimension.
     */
    public static int numElements_nbrs(int dimension) {
      int array_dims[] = { 4,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

    /**
     * Fill in the array 'nbrs' with a String
     */
    public void setString_nbrs(String s) { 
         int len = s.length();
         int i;
         for (i = 0; i < len; i++) {
             setElement_nbrs(i, (short)s.charAt(i));
         }
         setElement_nbrs(i, (short)0); //null terminate
    }

    /**
     * Read the array 'nbrs' as a String
     */
    public String getString_nbrs() { 
         char carr[] = new char[Math.min(net.tinyos.message.Message.MAX_CONVERTED_STRING_LENGTH,4)];
         int i;
         for (i = 0; i < carr.length; i++) {
             if ((char)getElement_nbrs(i) == (char)0) break;
             carr[i] = (char)getElement_nbrs(i);
         }
         return new String(carr,0,i);
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: q
    //   Field type: short[], unsigned
    //   Offset (bits): 168
    //   Size of each element (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'q' is signed (false).
     */
    public static boolean isSigned_q() {
        return false;
    }

    /**
     * Return whether the field 'q' is an array (true).
     */
    public static boolean isArray_q() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'q'
     */
    public static int offset_q(int index1) {
        int offset = 168;
        if (index1 < 0 || index1 >= 4) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'q'
     */
    public static int offsetBits_q(int index1) {
        int offset = 168;
        if (index1 < 0 || index1 >= 4) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return offset;
    }

    /**
     * Return the entire array 'q' as a short[]
     */
    public short[] get_q() {
        short[] tmp = new short[4];
        for (int index0 = 0; index0 < numElements_q(0); index0++) {
            tmp[index0] = getElement_q(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'q' from the given short[]
     */
    public void set_q(short[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_q(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a short) of the array 'q'
     */
    public short getElement_q(int index1) {
        return (short)getUIntElement(offsetBits_q(index1), 8);
    }

    /**
     * Set an element of the array 'q'
     */
    public void setElement_q(int index1, short value) {
        setUIntElement(offsetBits_q(index1), 8, value);
    }

    /**
     * Return the total size, in bytes, of the array 'q'
     */
    public static int totalSize_q() {
        return (32 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'q'
     */
    public static int totalSizeBits_q() {
        return 32;
    }

    /**
     * Return the size, in bytes, of each element of the array 'q'
     */
    public static int elementSize_q() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'q'
     */
    public static int elementSizeBits_q() {
        return 8;
    }

    /**
     * Return the number of dimensions in the array 'q'
     */
    public static int numDimensions_q() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'q'
     */
    public static int numElements_q() {
        return 4;
    }

    /**
     * Return the number of elements in the array 'q'
     * for the given dimension.
     */
    public static int numElements_q(int dimension) {
      int array_dims[] = { 4,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

    /**
     * Fill in the array 'q' with a String
     */
    public void setString_q(String s) { 
         int len = s.length();
         int i;
         for (i = 0; i < len; i++) {
             setElement_q(i, (short)s.charAt(i));
         }
         setElement_q(i, (short)0); //null terminate
    }

    /**
     * Read the array 'q' as a String
     */
    public String getString_q() { 
         char carr[] = new char[Math.min(net.tinyos.message.Message.MAX_CONVERTED_STRING_LENGTH,4)];
         int i;
         for (i = 0; i < carr.length; i++) {
             if ((char)getElement_q(i) == (char)0) break;
             carr[i] = (char)getElement_q(i);
         }
         return new String(carr,0,i);
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: debug_code
    //   Field type: long, unsigned
    //   Offset (bits): 200
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'debug_code' is signed (false).
     */
    public static boolean isSigned_debug_code() {
        return false;
    }

    /**
     * Return whether the field 'debug_code' is an array (false).
     */
    public static boolean isArray_debug_code() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'debug_code'
     */
    public static int offset_debug_code() {
        return (200 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'debug_code'
     */
    public static int offsetBits_debug_code() {
        return 200;
    }

    /**
     * Return the value (as a long) of the field 'debug_code'
     */
    public long get_debug_code() {
        return (long)getUIntElement(offsetBits_debug_code(), 32);
    }

    /**
     * Set the value of the field 'debug_code'
     */
    public void set_debug_code(long value) {
        setUIntElement(offsetBits_debug_code(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'debug_code'
     */
    public static int size_debug_code() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'debug_code'
     */
    public static int sizeBits_debug_code() {
        return 32;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: crc
    //   Field type: int, unsigned
    //   Offset (bits): 232
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
        return (232 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'crc'
     */
    public static int offsetBits_crc() {
        return 232;
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
