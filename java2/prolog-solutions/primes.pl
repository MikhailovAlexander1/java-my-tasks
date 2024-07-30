prime_support(N, X, R) :-
    X =< R,
    0 is mod(N, X), !.
prime_support(N, X, R) :-
    X =< R,
    X1 is X + 1, prime_support(N, X1, R).
prime(2).
prime(N) :-
    N1 is sqrt(N) + 1,
    \+ prime_support(N, 2, N1).
composite(N) :-
    \+ prime(N).
add([], [], []).
add([], B, [B]).
add(A, [], A).
add([H | T], B, [H | R]) :- add(T, B, R).
count([], 0).
count([_ | T], R) :- count(T, TR), R is TR + 1.
get_min_divider(N, R, Z) :-
    R =< N,
    Z is R,
    prime(R),
    0 is mod(N, R), !.
get_min_divider(N, R, Z) :-
    R =< N,
    R1 is R + 1,  get_min_divider(N, R1, Z).
prime_divisors_support(N, B, Divisors) :-
    N < 2,
    add(B, [], Divisors), !.
prime_divisors_support(N, B, Divisors) :-
    N > 1,
    get_min_divider(N, 2, Z),
    add(B, Z, B1),
    N1 is div(N, Z), prime_divisors_support(N1, B1, Divisors).
prime_divisors(N, D) :-
    prime_divisors_support(N, [], D).
compare(E1, E2) :- E1 = E2.
unique_prime([], B, R) :-
    add(B, [], R), !.
unique_prime([H], B, R) :-
    add(B, H, R), !.
unique_prime([H1, H2 | T], B,  R) :-
    compare(H1, H2),
    unique_prime([H2 | T], B, R), !.
unique_prime([H1, H2 | T], B, R) :-
    add(B, H1, B1),
    unique_prime([H2 | T], B1, R).
unique_prime_divisors(N, Divisors) :-
    prime_divisors(N, D),
    unique_prime(D, [], Divisors).
    