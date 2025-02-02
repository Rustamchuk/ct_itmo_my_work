map_build([], e).
map_build([(Key, Value) | T], TreeMap) :-
    map_build(T, CurTree), insert(Key, Value, CurTree, TreeMap).

insert(Key, Value, e, node(A, B, e, e)) :- A is Key, B = Value.

insert(Key, Value, node(Key, Value, L1, R1), node(A, B, L, R)) :- A is Key, B is Value, L is L1, R is R1.

insert(Key, Value, node(A, B, L1, R1), node(A, B, L, R)) :-
    Key < A,
    R = R1,
    insert(Key, Value, L1, L).

insert(Key, Value, node(A, B, L1, R1), node(A, B, L, R)) :-
    Key > A,
    L = L1,
    insert(Key, Value, R1, R).

map_get(node(A, B, _, _), Key, Value) :- A is Key, B = Value.
map_get(node(A, _, L, _), Key, Value) :-
    Key < A,
    map_get(L, Key, Value).
map_get(node(A, _, _, R), Key, Value) :-
    Key > A,
    map_get(R, Key, Value).

map_keys(e, []).
map_keys(node(Key, _, L, R), Keys) :-
    map_keys(L, Left),
    map_keys(R, Right),
    append(Left, [Key | Right], Keys).