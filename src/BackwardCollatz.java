import java.math.BigInteger;
import java.util.ArrayList;

/*

Compile:
$> javac BackwardCollatz.java

Display first 3 children starting from Tn=60:
$> java -DTn=60 -Dprint.depth=3 BackwardCollatz

Search back path to 47 starting from Tn=70:
$> java -DTn=70  BackwardCollatz 47

*/

public class BackwardCollatz
{
    static final boolean isTty  = System.console() != null && "/".equals( System.getProperty( "file.separator" ) );
    static final int printDepth = System.getProperty( "print.depth" ) == null ? -1 : Integer.parseInt( System.getProperty( "print.depth" ) );
    static final int Tn         = System.getProperty( "Tn" ) == null ? 70 : Integer.parseInt( System.getProperty( "Tn" ) );
    static final ArrayList<BigInteger> pathToTarget = new ArrayList<>();
    static final BigInteger pathBig[]  = new BigInteger[ Tn + 1 ];
    static final long       pathLong[] = new long[ Tn + 1 ];
    static int  curBig = 0;
    static int  curLong = 0;
    static long target = 0;

    public static void main( String args[] )
    {
        // we accept targets < Long.MAX_VALUE
        target = Long.parseLong( args.length > 0 ? args[0] : "0" );

        // only two neighbours are necessary: one even, one odd
        for( int p = Tn-1; p <= Tn; ++p )
        {
            if( printDepth < 0 ) System.out.print( "Computing all for Tn=" + p + " " );
            long t1 = System.currentTimeMillis();
            BigInteger big = BigInteger.ONE.shiftLeft( p );
            long count = p > 61 ? generate( big, big, 0 ) : 
                                  generate( 1L << p, 1L << p, 0 );
            long t2 = System.currentTimeMillis();
            if( printDepth < 0 ) System.out.format( "%6.3f sec, values: %20d, log ratio %5.4f\n", 
                                                    (t2-t1)/1000., count, Math.log( Math.pow( 2., p ) / 3. ) / Math.log( count ) ).flush();
        }
        if( target > 0 && pathToTarget.isEmpty() )
        {
            System.out.println( "No path to " + target );
            System.out.println( "Increase -DTn and restart" );
        }
        else
        {
            System.out.println( "\nPath to " + target );

            int step = pathToTarget.size();
            for( int i = 0; i < pathToTarget.size(); ++i )
            {
                --step;
                BigInteger v = pathToTarget.get(i);
                if( i < pathToTarget.size() - 1 && v.shiftRight(2).equals( pathToTarget.get(i+1)) )
                {
                    continue;
                }
                BigInteger regular = v.shiftRight( v.getLowestSetBit() );
                System.out.format( "%3d %" + ( 2 + Tn ) + "s %30s  %10s\n", step, v.toString(2), v.toString(), regular.toString() );
            }
            System.out.println();
        }
}

    static long generate( long v, long maxsub, int indent )
    {
        if( indent < printDepth )
        {
            for( int i = 0; i < indent; i++ ) System.out.print( "  " );
            if( isTty )
            {
                System.out.println( "\033[3" + FG[indent%FG.length] + "m" + v + "\033[m  " + Long.toBinaryString(v) );
            }
            else
            {
                System.out.println( v + "  " + Long.toBinaryString(v) );
            }
        }

        pathLong[ curLong++ ] = v;
        if( v == target )
        {
            int cur = 0;
            while( cur < curBig )
            {
                pathToTarget.add( pathBig[cur] );
                ++cur;
            }
            while( cur < curLong )
            {
                pathToTarget.add( BigInteger.valueOf( pathLong[cur] ) );
                ++cur;
            }
        }
        long count = 1;
        long sub = 1;
        while( sub < maxsub )
        {
            long x = v - sub;
            long next = x / 3L;
            if( x % 3L == 0 )
            {
                count += generate( next, sub, indent + 1 );
                sub <<= 1;
            }
            sub <<= 1;
        }
        --curLong;
        return count;
    }

    static long generate( BigInteger v, BigInteger maxsub, int indent )
    {
        if( indent < printDepth )
        {
            for( int i = 0; i < indent; i++ ) System.out.print( "  " );
            if( isTty )
            {
                System.out.println( "\033[3" + FG[indent%FG.length] + "m" + v.toString() + "\033[m  " + v.toString(2) );
            }
            else
            {
                System.out.println( v.toString() + "  " + v.toString(2) );
            }
        }
        pathBig[ curBig++ ] = v;

        long count = 1;
        BigInteger sub = BigInteger.ONE;
        while( sub.compareTo( maxsub ) < 0 )
        {
            BigInteger next[] = v.subtract( sub ).divideAndRemainder( THREE );
            if( next[1].equals( BigInteger.ZERO ) )
            {
                if( next[0].compareTo( MAXBIG ) > 0 )
                {
                    count += generate( next[0], sub, indent + 1 );
                }
                else
                {
                    curLong = curBig;
                    count += generate( next[0].longValueExact(), sub.longValueExact(), indent + 1 );
                }
                sub = sub.shiftLeft(1);
            }
            sub = sub.shiftLeft(1);
        }
        --curBig;
        return count;
    }

    final static int FG[] = { 1, 2, 3, 4, 5, 6 };
    static final BigInteger THREE   = new BigInteger( "3" );
    static final BigInteger MAXBIG  = BigInteger.valueOf( 1L << 61 );

}
