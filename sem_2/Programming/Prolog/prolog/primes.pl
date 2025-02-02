prime(2).
prime(3).
prime(N) :- N > 3, \+ 0 is mod(N, 2), prime_for(N, 3).
prime_for(N, A) :- A * A > N; \+ 0 is mod(N, A), prime_for(N, A + 2).
composite(N) :- N > 1, \+ prime(N).

prime_divisors(1, []).
prime_divisors(N, Divisors) :- N > 1, prime_divisors(N, 2, Divisors).

prime_divisors(N, D, []) :- D > N.
prime_divisors(N, D, A) :- 
    N >= D, 
    \+ 0 is mod(N, D), 
    prime_divisors(N, D + 1, A).
prime_divisors(N, D, [A | B]) :- 
    N >= D, 
    0 is mod(N, D),
    A is D,
    prime_divisors(N // D, D, B).



square_divisors(N, Divisors) :- prime_divisors(N, PD), double(PD, Divisors).

double([], []).
double([D | B], [A1, A2 | C]) :- A1 is D, A2 is D, double(B, C).