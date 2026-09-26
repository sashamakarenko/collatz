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
            return "\u001B[92;1m" + v + "\u001B[90m ^" + p + " \u001B[3" + (p == 0 ? '1' : '4') + "m" + toLong() + "\u001B[0m";
        }
    }

    static void children(Number nm, int nmax, int depth, int shift) {
        System.out.printf("%3d %3d | %3d | ", depth, shift, nmax);
        for (int d = 0; d < depth; ++d) {
            System.out.print(".  ");
        }
        System.out.println(nm);

        for (int n = 1; n <= nmax; ++n) {
            Number next = nm.minusOne(nmax - n);
            if (next.div3() == 0) {
                children(next, nmax - n, depth + 1, n);
            }
        }
    }

    static long dig(Number nm, int nmax) {
        long count = 1;
        for (int n = 1; n <= nmax; ++n) {
            Number next = nm.minusOne(nmax - n);
            if (next.div3() == 0) {
                count += dig(next, nmax - n);
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

        if (p <= 24) {
            children(x, x.p - 2, 0, 0);
        }

        long t1 = System.nanoTime();
        long count = dig(x, x.p - 2);
        long t2 = System.nanoTime();

        System.out.println(count + " " + ((t2 - t1) / 1_000_000L));
    }
}