## Benchmarking

Backward Collatz problem is solved in C++, Java and Python3 to compare computational times.
Because of the combinatorial nature the complexity grows too quickly to afford after 2^74.

## Compilation

```bash
$> javac BackwardCollatz.java
$> c++ -o BenchBackwardCollatz BenchBackwardCollatz.cpp -O3
```

## Run benchmark

```bash
$> taskset -c 5 ./run-benchmarks.sh 
```

## Results

> obtained on Intel(R) Core(TM) i9-9900K CPU at 4.8GHz

|  Tn|   C++|     Java|  Python|
|----|-----:|--------:|-------:|
|  10|  0.00|     0.02|    0.00|
|  20|  0.00|     0.02|    0.00|
|  30|  0.00|     0.02|    0.01|
|  40|  0.00|     0.03|    0.04|
|  50|  0.01|     0.04|    0.86|
|  60|  0.24|     0.29|   16.10|
|  70|  4.00|     5.03|  290.02|
|  75| 16.99|    24.26|     ...|
|  80| 83.54|   151.98|     ...|

