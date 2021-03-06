/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'CapsuleChunkMsg'
 * message type.
 */

package vm_specific;

public class CapsuleChunkMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 27;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 32;

    /** Create a new CapsuleChunkMsg of size 27. */
    public CapsuleChunkMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new CapsuleChunkMsg of the given data_length. */
    public CapsuleChunkMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg with the given data_length
     * and base offset.
     */
    public CapsuleChunkMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg using the given byte array
     * as backing store.
     */
    public CapsuleChunkMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public CapsuleChunkMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public CapsuleChunkMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg embedded in the given message
     * at the given base offset.
     */
    public CapsuleChunkMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new CapsuleChunkMsg embedded in the given message
     * at the given base offset and length.
     */
    public CapsuleChunkMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <CapsuleChunkMsg> \n";
      try {
        s += "  [version=0x"+Long.toHexString(get_version())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [capsuleNum=0x"+Long.toHexString(get_capsuleNum())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [piece=0x"+Long.toHexString(get_piece())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [chunk=";
        for (int i = 0; i < 21; i++) {
          s += "0x"+Long.toHexString(getElement_chunk(i) & 0xff)+" ";
        }
        s += "]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: version
    //   Field type: long, unsigned
    //   Offset (bits): 0
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'version' is signed (false).
     */
    public static boolean isSigned_version() {
        return false;
    }

    /**
     * Return whether the field 'version' is an array (false).
     */
    public static boolean isArray_version() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'version'
     */
    public static int offset_version() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'version'
     */
    public static int offsetBits_version() {
        return 0;
    }

    /**
     * Return the value (as a long) of the field 'version'
     */
    public long get_version() {
        return (long)getUIntElement(offsetBits_version(), 32);
    }

    /**
     * Set the value of the field 'version'
     */
    public void set_version(long value) {
        setUIntElement(offsetBits_version(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'version'
     */
    public static int size_version() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'version'
     */
    public static int sizeBits_version() {
        return 32;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: capsuleNum
    //   Field type: short, unsigned
    //   Offset (bits): 32
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'capsuleNum' is signed (false).
     */
    public static boolean isSigned_capsuleNum() {
        return false;
    }

    /**
     * Return whether the field 'capsuleNum' is an array (false).
     */
    public static boolean isArray_capsuleNum() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'capsuleNum'
     */
    public static int offset_capsuleNum() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'capsuleNum'
     */
    public static int offsetBits_capsuleNum() {
        return 32;
    }

    /**
     * Return the value (as a short) of the field 'capsuleNum'
     */
    public short get_capsuleNum() {
        return (short)getUIntElement(offsetBits_capsuleNum(), 8);
    }

    /**
     * Set the value of the field 'capsuleNum'
     */
    public void set_capsuleNum(short value) {
        setUIntElement(offsetBits_capsuleNum(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'capsuleNum'
     */
    public static int size_capsuleNum() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'capsuleNum'
     */
    public static int sizeBits_capsuleNum() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: piece
    //   Field type: short, unsigned
    //   Offset (bits): 40
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'piece' is signed (false).
     */
    public static boolean isSigned_piece() {
        return false;
    }

    /**
     * Return whether the field 'piece' is an array (false).
     */
    public static boolean isArray_piece() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'piece'
     */
    public static int offset_piece() {
        return (40 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'piece'
     */
    public static int offsetBits_piece() {
        return 40;
    }

    /**
     * Return the value (as a short) of the field 'piece'
     */
    public short get_piece() {
        return (short)getUIntElement(offsetBits_piece(), 8);
    }

    /**
     * Set the value of the field 'piece'
     */
    public void set_piece(short value) {
        setUIntElement(offsetBits_piece(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'piece'
     */
    public static int size_piece() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'piece'
     */
    public static int sizeBits_piece() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: chunk
    //   Field type: short[], unsigned
    //   Offset (bits): 48
    //   Size of each element (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'chunk' is signed (false).
     */
    public static boolean isSigned_chunk() {
        return false;
    }

    /**
     * Return whether the field 'chunk' is an array (true).
     */
    public static boolean isArray_chunk() {
        return true;
    }

    /**
     * Return the offset (in bytes) of the field 'chunk'
     */
    public static int offset_chunk(int index1) {
        int offset = 48;
        if (index1 < 0 || index1 >= 21) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return (offset / 8);
    }

    /**
     * Return the offset (in bits) of the field 'chunk'
     */
    public static int offsetBits_chunk(int index1) {
        int offset = 48;
        if (index1 < 0 || index1 >= 21) throw new ArrayIndexOutOfBoundsException();
        offset += 0 + index1 * 8;
        return offset;
    }

    /**
     * Return the entire array 'chunk' as a short[]
     */
    public short[] get_chunk() {
        short[] tmp = new short[21];
        for (int index0 = 0; index0 < numElements_chunk(0); index0++) {
            tmp[index0] = getElement_chunk(index0);
        }
        return tmp;
    }

    /**
     * Set the contents of the array 'chunk' from the given short[]
     */
    public void set_chunk(short[] value) {
        for (int index0 = 0; index0 < value.length; index0++) {
            setElement_chunk(index0, value[index0]);
        }
    }

    /**
     * Return an element (as a short) of the array 'chunk'
     */
    public short getElement_chunk(int index1) {
        return (short)getUIntElement(offsetBits_chunk(index1), 8);
    }

    /**
     * Set an element of the array 'chunk'
     */
    public void setElement_chunk(int index1, short value) {
        setUIntElement(offsetBits_chunk(index1), 8, value);
    }

    /**
     * Return the total size, in bytes, of the array 'chunk'
     */
    public static int totalSize_chunk() {
        return (168 / 8);
    }

    /**
     * Return the total size, in bits, of the array 'chunk'
     */
    public static int totalSizeBits_chunk() {
        return 168;
    }

    /**
     * Return the size, in bytes, of each element of the array 'chunk'
     */
    public static int elementSize_chunk() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of each element of the array 'chunk'
     */
    public static int elementSizeBits_chunk() {
        return 8;
    }

    /**
     * Return the number of dimensions in the array 'chunk'
     */
    public static int numDimensions_chunk() {
        return 1;
    }

    /**
     * Return the number of elements in the array 'chunk'
     */
    public static int numElements_chunk() {
        return 21;
    }

    /**
     * Return the number of elements in the array 'chunk'
     * for the given dimension.
     */
    public static int numElements_chunk(int dimension) {
      int array_dims[] = { 21,  };
        if (dimension < 0 || dimension >= 1) throw new ArrayIndexOutOfBoundsException();
        if (array_dims[dimension] == 0) throw new IllegalArgumentException("Array dimension "+dimension+" has unknown size");
        return array_dims[dimension];
    }

    /**
     * Fill in the array 'chunk' with a String
     */
    public void setString_chunk(String s) { 
         int len = s.length();
         int i;
         for (i = 0; i < len; i++) {
             setElement_chunk(i, (short)s.charAt(i));
         }
         setElement_chunk(i, (short)0); //null terminate
    }

    /**
     * Read the array 'chunk' as a String
     */
    public String getString_chunk() { 
         char carr[] = new char[Math.min(net.tinyos.message.Message.MAX_CONVERTED_STRING_LENGTH,21)];
         int i;
         for (i = 0; i < carr.length; i++) {
             if ((char)getElement_chunk(i) == (char)0) break;
             carr[i] = (char)getElement_chunk(i);
         }
         return new String(carr,0,i);
    }

}
