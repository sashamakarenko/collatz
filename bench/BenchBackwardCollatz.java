import java.math.BigInteger;

public class BenchBackwardCollatz
{
    public static void main( String [] args )
    {
        final int Tn = Integer.parseInt( args[0] );
        BigInteger v = BigInteger.ONE.shiftLeft( Tn );
        long count = generate( v, v );
        System.out.println( count );
    }

    static long generate( long v, long maxsub )
    {
        long count = 1;
        
        for( long sub = 1; sub < maxsub; sub <<= 1 )
        {
            long x = v - sub;
            if( x % 3L == 0 )
            {
                count += generate( x / 3, sub );
                sub <<= 1;
            }
        }        

        return count;
    }

    static long generate( BigInteger v, BigInteger maxsub )
    {
        long count = 1;
        BigInteger sub = BigInteger.ONE;
        while( sub.compareTo( maxsub ) < 0 )
        {
            BigInteger next[] = v.subtract( sub ).divideAndRemainder( THREE );
            if( next[1].equals( BigInteger.ZERO ) )
            {
                if( next[0].compareTo( MAXBIG ) > 0 )
                {
                    count += generate( next[0], sub );
                }
                else
                {
                    count += generate( next[0].longValueExact(), sub.longValueExact() );
                }
                sub = sub.shiftLeft(1);
            }
            sub = sub.shiftLeft(1);
        }
        return count;
    }
    static final BigInteger THREE   = new BigInteger( "3" );
    static final BigInteger MAXBIG  = BigInteger.valueOf( 1L << 62 );

}
