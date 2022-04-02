#include <stdint.h>
#include <iostream>
#include <sstream>


using u128 = __uint128_t;
using u64  = __uint64_t;
using u32  = __uint32_t;

template< typename T >
u64 generate( T x, T maxsub )
{
    u64 count = 1;
    for( T sub = 1; sub < maxsub; sub <<= 1 )
    {
        T tmp = x - sub;
        if( tmp % T(3) == 0 )
        {
            T next = tmp / T(3);
            if( next > ( u128(1) << 62 ) )
            {
                count += generate<u128>( next, sub );
            }
            else
            {
                if( next > ( u64(1) << 31 ) )
                {
                    count += generate<u64>( u64(next), u64(sub) );
                }
                else
                {
                    count += generate<u32>( u32(next), u32(sub) );
                }
            }
            sub <<= 1;
        }
    }
    return count;
}

int main( int argc, const char ** argv )
{
    std::stringstream ss;
    ss << argv[1];
    u64 Tn;
    ss >> Tn;
    u64 count = 0;
    if( Tn > 62 )
    {
        count = generate<u128>( u128(1) << Tn, u128(1) << Tn );
    }
    else
    {
        count = generate<u64>( u64(1) << Tn, u64(1) << Tn );
    }
    std::cout << count << std::endl;
    return 0;
}
