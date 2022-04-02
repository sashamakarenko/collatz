import sys

def generate( v: int, maxsub: int ) -> int:
    count: int = 1
    sub: int = 1
    while sub < maxsub:
        x: int = v - sub;
        if x % 3 == 0:
            count += generate( x // 3, sub )
            sub = sub << 1
        sub = sub << 1
    return count

Tn = int(sys.argv[1])
count = generate( 1<<Tn, 1<<Tn )
print( count )
