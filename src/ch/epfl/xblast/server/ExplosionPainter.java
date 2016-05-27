package ch.epfl.xblast.server;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date April 21, 2016
 *
 */

public final class ExplosionPainter {
    public static final byte BYTE_FOR_EMPTY = 16;

    /**
     * Private constructor, not to be instanced
     */
    private ExplosionPainter() {
    }

    /**
     * If the bomb's fuse length is power of 2, it returns a white bomb image.
     * Otherwise it returns a normal (black) bomb image.
     * 
     * @param bomb
     * @return the byte used to identify the bomb
     */
    public static byte byteForBomb(Bomb bomb) {
        return (byte) (Integer.bitCount(bomb.fuseLength()) == 1 ? 21 : 20);
    }

    /**
     * Depending on the blast around the actual, it returns the correct blast
     * image.
     * 
     * @param north
     * @param east
     * @param south
     * @param west
     * @return the byte used to identify the blast
     */
    public static byte byteForBlast(boolean north, boolean east, boolean south,
            boolean west) {
        byte output = 0;
        if (north)
            output += 8;
        if (east)
            output += 4;
        if (south)
            output += 2;
        if (west)
            output++;
        return output;
    }
}
