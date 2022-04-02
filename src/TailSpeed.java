
public class TailSpeed
{

  public static void main(String[] args){
    for( int width = 2; width < 25; ++width ){
      speed(width);
    }
  }
  
  private static void speed( int width ){
    long vmax = 1L << width;
    long dist[] = new long[width+2];
    long sum = 0;
    for( long v = 1; v < vmax; v += 2 ){
      long v3 = v * 3L;
      int s = 0;
      for( long one = 1; (one&v3) != 0; one <<= 1 ) {s++;}
      dist[s]++;
      sum += s;
    }
    double av = 2.0*(double)sum/vmax;
    System.out.println( "" + width + " " + av );
    for( int i = 1; i < dist.length; ++i ){
      //System.out.println( i + " " + dist[i]);
    }
  }
}
