get_pair([(K, V) | _], K, V, M, M) :- !.
get_pair([_ | T], K, V, M, C) :-
    C1 is C + 1,
    get_pair(T, K, V, M, C1).

build(_, nil, E, E) :- !.
build(ListMap, node(Key, Value, Lft, Rgt), S, E) :-
    Sum is S + E,
    M is div(Sum, 2),
    get_pair(ListMap, Key, Value, M, 0),
    build(ListMap, Lft, S, M),
    M1 is M + 1,
    build(ListMap, Rgt, M1, E).

map_build([], nil) :- !.
map_build(ListMap, TreeMap) :-
    length(ListMap, C),
    build(ListMap, TreeMap, 0, C), !.

map_get(node(K, V, _, _), K, V) :- !.
map_get(node(Key, _, _, Rgt), K, V) :-
    K > Key, map_get(Rgt, K, V), !.
map_get(node(_, _, Lft, _), K, V) :-
    map_get(Lft, K, V).

map_minKey(node(Key, _, nil, _), Key) :- !.
map_minKey(node(_, _, Lft, _), Key) :- map_minKey(Lft, Key).

map_maxKey(node(Key, _, _, nil), Key) :- !.
map_maxKey(node(_, _, _, Rgt), Key) :- map_maxKey(Rgt, Key).