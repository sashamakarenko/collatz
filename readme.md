# 3n+1 problem proof and computer programs

## Proof

Read [makarenko-alexandre-3n+1.pdf](makarenko-alexandre-3n%2B1.pdf)


## Code

In `src` all java files are standalone programs.
To compile for example ForwardCollatz.java:

```bash
$> java ForwardCollatz.java
```

### Forward sequence

Example: solve Collatz in new formulation for 47:
![fwd](img/forward-collatz-47.png)

### Backward sequence

Example: solve reverse Collatz for 47 starting from 2^68:
![bwd](img/backward-collatz-47.png)


## Benchmarks

See in `bench` directory.
