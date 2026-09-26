#include <iostream>
#include <cinttypes>
#include <chrono>
#include <cstdlib>

using Long  = __uint128_t;
using Short = uint64_t;
constexpr unsigned ShortBits = 8 * sizeof( Short );

union Value
{
    Long  big;
    Short little;
    Short halves[2];
};

struct Number
{
    Number( unsigned p2 ): v{1}, p{p2}{}
    Number( Long lv, unsigned p2 ): v{lv}, p{p2}{}
    Long toLong() const { return v.big << p; }
    Value    v;
    unsigned p;

    Number minusOne( unsigned p2 ) const
    {
        return  p2 > p ? Number( v.big - ( Long(1) << ( p2 - p ) ), p ) : 
                         Number( ( v.big << ( ( p - p2 ) ) ) - 1, p2 );
    }
    
    unsigned div3()
    {
        if( v.halves[1] == 0 )
        {
            unsigned rem = v.little % 3;
            if( rem != 0 )
            {
                return rem;
            }
            v.little /= 3;
            return rem;
        }
        else
        {
            unsigned rem = v.big % 3;
            if( rem != 0 )
            {
                return rem;
            }
            v.big /= 3;
            return rem;
        }
    }
};


std::ostream & operator << ( std::ostream & os, Long v )
{
    if( v < ( long(1) << 63 ) )
    {
        os << (uint64_t)v;
    }
    else
    {
        int buf[64];
        unsigned n = 0;
        while( v )
        {
            buf[n++] = v % 10;
            v /= 10;
        }
        if( n == 0 )
        {
            os << '0';
        }
        else
        {
            while( n )
            {
                os << char( '0' + buf[--n]);
            }
        }
    }
    return os;
}

std::ostream & operator << ( std::ostream & os, const Number & nm )
{
    return os << "\033[92;1m" << nm.v.big << "\033[90m ^" << nm.p << " \033[3" << ( nm.p == 0 ? '1' : '4' ) << "m" << nm.toLong() << "\033[0m";
}


void children( const Number & nm, unsigned nmax, unsigned depth, unsigned shift )
{
    printf( "%3d %3d | %3d | ", depth, shift, nmax );
    for( unsigned d = 0; d < depth; ++d )
    {
        std::cout << ".  ";
    }
    std::cout << nm << std::endl;
    for( unsigned n = 1; n <= nmax; ++n )
    {
        Number next = nm.minusOne(nmax - n);
        if( next.div3() == 0 )
        {
            children( next, nmax - n, depth + 1, n );
        }
    }
}

size_t dig( const Number & nm, unsigned nmax )
{
    size_t count = 1;
    for( unsigned n = 1; n <= nmax; ++n )
    {
        Number next = nm.minusOne(nmax - n);
        if( next.div3() == 0 )
        {
            count += dig( next, nmax - n );
        }
    }
    return count;
}

int main( int argc, char ** argv )
{
    unsigned p = std::atoi( argv[1] );
    Number x( p );
    
    if( p <= 24 )
    {
        children( x, x.p - 2, 0, 0 );
    }

    auto t1 = std::chrono::system_clock::now();
    size_t count = dig( x, x.p - 2 );
    auto t2 = std::chrono::system_clock::now();
    std::cout << count << " " << ( t2 - t1 ).count() / 1000'000UL << std::endl;
}
