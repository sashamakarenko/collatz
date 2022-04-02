import java.math.BigInteger;
import java.util.ArrayList;

public class ForwardCollatz
{
   
    public static void main( String args[] )
    {
        if( args.length == 0 )
        {
            System.err.println( 
                "\n\nusage: java ForwardCollatz value" +  
                "\n where value may be a conventional decimal integer or binary if it starts with 0." +  
                "\n" +  
                "\nexamples: " +  
                "\n> java ForwardCollatz 47" +  
                "\n> java ForwardCollatz 0111101010101110101\n\n"
            );
            System.exit(1);
        }

        BigInteger X = new BigInteger( args[0], args[0].startsWith( "0" ) ? 2 : 10 );
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<BigInteger> collatzValues = new ArrayList<>();

        int singleOneCount = 0;        // number of occurences beyond the sequence end
        int endOfSequence = 0;         // step where sequence ends
        int Tn = 0;                    // power of two where sequence ends
        int lastTrailingZeroCount = 0;
        int stepCount = 0;
        
        showStep( stepCount, X );
        lines.add( formatStep( X ) );
        collatzValues.add( X );

        while( singleOneCount < 10 && // to display tail speed vs head speed 
               stepCount      < 150 ) // stop at some reasonable point
        {
            stepCount++;
            
            // compute next value
            X = X.multiply( THREE );
            
            // tail
            lastTrailingZeroCount = 0;
            BigInteger Ti = BigInteger.ONE;
            while( Ti.and( X ).equals(  BigInteger.ZERO ) )
            {
                Ti = Ti.shiftLeft(1);
                lastTrailingZeroCount++;
            }
            X = X.add( Ti );
    
            showStep( stepCount, X );
            lines.add( formatStep( X ) );

            // find new tail
            while( Ti.and( X ).equals(  BigInteger.ZERO ) )
            {
                Ti = Ti.shiftLeft(1);
                lastTrailingZeroCount++;
            }
            collatzValues.add( X.shiftRight( lastTrailingZeroCount ) );

            if( X.equals( Ti ) ) // we reach sequence end
            {
                if( singleOneCount == 0 ) // first time here
                {
                    Tn = lastTrailingZeroCount;
                    endOfSequence = stepCount;
                }
                singleOneCount++;
            }
        }

        // display tail speed
        if( singleOneCount > 0 )
        {
            if( isTty )
            {
                System.out.println( "\n   \033[44m \033[0m - tail speed\n" );
            }
            else
            {
                System.out.println( "\n\n" );
            }

            int step = stepCount;
            for( String line : lines )
            {
                int row = stepCount - step;
                int pos = lastTrailingZeroCount - step * 2;
                int offset = DISPWIDTH - pos - 1;
                System.out.format( "%4d ", row );
                if( isTty && offset >= 0 && offset < DISPWIDTH )
                {
                    System.out.print( line.substring( 0, offset ) + "\033[44m" + line.substring( offset, offset + 1 ) + "\033[0m" + line.substring( offset + 1 ) );
                }
                else
                {
                    System.out.print( line );
                }
                System.out.print( "|" );
                if( row == endOfSequence ) 
                {
                    System.out.format( " 2^%-3d", Tn );
                }
                else
                {
                    System.out.print( "      " );
                }
                System.out.println( collatzValues.get( row ) );
                step--;
            }
        }
    }

    static void showStep( int step, BigInteger value )
    {
        System.out.format( "%4d %" + DISPWIDTH + "s| %s\n", step, value.toString(2).replaceAll( "0", " " ), value.toString() ).flush();
    }

    static String formatStep( BigInteger value )
    {
        return String.format( "%" + DISPWIDTH + "s", value.toString(2).replaceAll( "0", " " ) );
    }

    static final boolean isTty  = System.console() != null && "/".equals( System.getProperty( "file.separator" ) );

    static final BigInteger THREE     = new BigInteger( "3" );
    static final int        DISPWIDTH = 100;
}
