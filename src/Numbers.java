import java.math.BigInteger;

public class Numbers {
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger THREE = BigInteger.valueOf(3);

    static final class Number {
        private BigInteger v;
        private int p;

        Number(int p2) {
            this(ONE, p2);
        }

        Number(BigInteger lv, int p2) {
            this.v = lv;
            this.p = p2;
        }

        BigInteger toLong() {
            return v.shiftLeft(p);
        }

        Number minusOne(int p2) {
            if (p2 > p) {
                return new Number(v.subtract(ONE.shiftLeft(p2 - p)), p);
            }
            return new Number(v.shiftLeft(p - p2).subtract(ONE), p2);
        }

        int div3() {
            BigInteger rem = v.remainder(THREE);
            if (!rem.equals(ZERO)) {
                return rem.intValue();
            }
            v = v.divide(THREE);
            return 0;
        }

        @Override
        public String toString() {
            return "\u001B[9" + ( v.remainder(THREE).equals(ZERO) ? '3' : '2') + ";1m" + v + "\u001B[90m ^" + (v.bitLength()+p-1) + "-" + p + " \u001B[3" + (p == 0 ? '1' : '4') + "m" + toLong() + "\u001B[0m";
        }
    }

    // returns number of children multiple of 3 (yellow numbers)
    static long children(Number nm, int nmax, int depth, int shift) {
        long count = 0;

        for (int d = 0; d < depth; ++d) {
            System.out.print(".  ");
        }
        if( nm.v.remainder(THREE).equals(ZERO) ){
            count = 1;
        }
        System.out.println(nm);

        for (int n = 1; n <= nmax; ++n) {
            Number next = nm.minusOne(nmax - n);
            if (next.div3() == 0) {
                count += children(next, nmax - n, depth + 1, n);
            }
        }
        return count;
    }

    static long dig(Number nm, int nmax) {
        long count = 1;
        for (int n = 1; n <= nmax; ++n) {
            Number next = nm.minusOne(nmax - n);
            if (next.div3() == 0) {
                count += dig(next, nmax - n);
                if( next.p == 0 ){
                    count += 1L << 32;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Numbers <p>");
            System.exit(1);
        }

        int p = Integer.parseInt(args[0]);
        Number x = new Number(p);

        long mul3 = 0;
        if (p <= 24) {
            mul3 = children(x, x.p - 2, 0, 0);
        }

        long count = dig(x, x.p - 2);
        long newCount = count >> 32;
        count &= 0xffffffffL;
        System.out.println( "total: " + count + " - new(red): " + newCount + " - mul3(yellow): " + mul3 );
    }
}