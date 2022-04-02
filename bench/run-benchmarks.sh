#!/bin/bash -e

declare -A C
C[10]=26
C[20]=488
C[30]=8608
C[40]=152589
C[50]=2709318
C[60]=48114851
C[70]=854438881

print_result()
{
    local count seconds
    ISF= read count seconds <<<$2
    if [[ $count != ${C[$1]} ]]; then
        printf "   ERROR"
    else
        printf " %7s" $seconds
    fi
}

echo "Tn    C++     Java   Python"
echo "==  ======= ======= ======="

for n in {1..7}; do
    tn=${n}0
    echo -n "$tn ";
    print_result $tn "$( /usr/bin/time -f "%U" ./BenchBackwardCollatz $tn 2>&1 | tr \\n ' ' )"
    print_result $tn "$( /usr/bin/time -f "%U" java BenchBackwardCollatz $tn 2>&1 | tr \\n ' ' )"
    print_result $tn "$( /usr/bin/time -f "%U" python3 BenchBackwardCollatz.py $tn 2>&1 | tr \\n ' ' )"
    echo
done
