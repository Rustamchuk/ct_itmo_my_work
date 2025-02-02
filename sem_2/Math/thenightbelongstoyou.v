(** Welcome all, welcome home (unless you don't feel at home).

TASK 2: 0    + 0.1  + 0.2 + 0.2 + 0.4 + 0.2 + 0.3             = 1.4
TASK 3: 0.6  + 0.2  + 0.1 + 0.3 + 0.6                         = 1.8
TASK 1: 0.05 + 0.05 + 0.1 + 0.1 + 0.1 + 0.2 + 0.2 + 0.4 + 0.4 = 1.6
TOTAL                                                         = 4.8
YOUR SCORE                                                    = MIN(3, TOTAL)

Press Alt+Z to enable word wrap.

IMPORTANT! All the theorems that are part of the exercises will have the [Admitted] command attached to them. This command is placed when we give up on proving something. If you want to prove the theorem, you'll need to remove this command.
If at ANY point you'll feel stuck or unwilling to continue solving the exercises from the current task, don't forget to place [Admitted] back in the proof and try moving on to the other tasks/exercises, there's plenty of points to acquire.

However, another important note about task 1: almost all the exercises depend on something that was defined previously in the previous exercises. Since Coq's interactive mode scans the file top to bottom, it'll be almost always impossible to do exercise N without having done exercises 1 through N - 1.
For THAT reason task 1 has been moved to the bottom of the file. If you wanna start with task 1, aim your cursor at the beginning of [Section Sets] and press Alt+Right.

A gentle reminder about the (completely made up) golden rule of Coq: don't ever hesitate to [unfold] your definitions! *)

From Coq Require Import Arith FunctionalExtensionality Lia.

(***********
 * TASK 2! *
 ***********)

Section Congruence.

(** This task is all about relations. Specifically, the equivalence relations. More specifically, the relation called 'congruence modulo n'. Let's define this relation in terms of Peano naturals. *)

Definition congruent n a b := exists q r, q * n + a = r * n + b.

Notation "a '===' b 'MOD' n" := (congruent n a b) (no associativity, at level 89).

(** How to read this: two natural numbers [a] and [b] are congruent modulo [n] iff there exist such multiples of [n], i.e. [q * n] and [r * n], that [a] is equal to [b] up to adding these multiples to each of these numbers.

Example: 5 and 17 are congruent modulo 4, because 3 * 4 + 5 = 0 * 4 + 17; also,
                                                  4 * 4 + 5 = 1 * 4 + 17; and so on.
The number of ways to prove the congruence of 5 and 17 modulo 4 is aleph_0. *)

(** EXERCISE (0 points lmao get zero'd). Prove that this relation is reflexive. *)

Theorem congr_refl (n a : nat) : a === a MOD n.
Proof.
  unfold congruent.
  exists 2.
  exists 2.
  reflexivity.
Qed.

(** EXERCISE (0.1 points). Prove that this relation is symmetric. *)

Theorem congr_sym (n a b : nat) : a === b MOD n -> b === a MOD n.
Proof.
  unfold congruent.
  intros.
  destruct H as [A B].
  destruct B as [C D].
  exists C, A.
  symmetry.
  exact D.
Qed.

(** The KEY tactic for the next batch of proofs will be [lia]! It's a tactic that solves linear inequalities and equations (and stands for 'linear algebra'), bearing in mind such logical connectives as disjunction, conjunction, negation, and implication. Instead of using a bunch of theorems like [add_assoc], [add_comm], [mul_comm], [add_0_l], and so on, we can use [lia] to request Coq to attempt and solve these inequalities and equations (if they are linear). *)

Goal forall (x : nat), x <= 5 /\ x >= 4 -> x = 4 \/ x = 5.
Proof. lia. Qed.

(** EXERCISE (0.2 points). Prove that this relation is transitive. *)

Theorem congr_trans (n a b c : nat) : a === b MOD n -> b === c MOD n -> a === c MOD n.
Proof.
  unfold congruent.
  intros.
  destruct H as [A B].
  destruct H0 as [C D].
  destruct B as [E F].
  destruct D as [G H].
  exists (A + C), (E + G).
  lia.
Qed.

(** EXERCISE (0.2 points). Prove that [n] is congruent to any multiple of [n], modulo n. *)

Theorem congr_cycle (n : nat) : forall k, n === k * n MOD n.
Proof.
  intros.
  unfold congruent.
  exists k, 1.
  lia.
Qed.

(** A 'fun' corollary: n is congruent to 0 modulo n. *)

Corollary congr_zero (n : nat) : n === 0 MOD n. Proof. exact (congr_cycle n 0). Qed.

(** EXERCISE (0.4 points). Prove that adding a number to each side of the congruence, either to the left or to the right of a number, doesn't invalidate the congruence.

Try proving [add_cancel_l] VIA [add_cancel_r] or vice versa! *)

Theorem add_cancel_r (n a b c : nat) : a === b MOD n <-> a + c === b + c MOD n.
Proof.
  unfold congruent.
  split.
  intros.
  destruct H as [A B].
  destruct B as [C D].
  exists A, C.
  lia.
  intros.
  destruct H as [A B].
  destruct B as [C D].
  exists A, C.
  lia.
Qed.

Theorem add_cancel_l (n a b c : nat) : a === b MOD n <-> c + a === c + b MOD n.
Proof.
  unfold congruent.
  split.
  intros.
  destruct H as [A B].
  destruct B as [C D].
  exists A, C.
  lia.
  intros.
  destruct H as [A B].
  destruct B as [C D].
  exists A, C.
  lia.
Qed.

(** EXERCISE (0.2 points). Prove that if [a] is congruent to [c], and [b] is congruent to [d], then [a + b] is congruent to [c + d]. *)

Theorem add_both_sides (n a b c d : nat)
  : a === c MOD n -> b === d MOD n -> a + b === c + d MOD n.
Proof.
  unfold congruent.
  intros.
  destruct H as [A B].
  destruct H0 as [C D].
  destruct B as [E F].
  destruct D as [G H].
  exists (A + C), (E + G).
  lia.
Qed.

(** EXERCISE (0.3 points). Same as above, but it's MULTIPLICATION!

Please please please do not attempt to prove this by hand, the proof you'll get will get _colossal_. If you come up with such _correct_ [q] and [r], the [lia] tactic will successfully solve the rest of the theorem. *)

Theorem mul_both_sides (n a b c d : nat)
  : a === c MOD n -> b === d MOD n -> a * b === c * d MOD n.
Proof.
  unfold congruent.
  intros.
  destruct H as [A B].
  destruct H0 as [C D].
  destruct B as [E F].
  destruct D as [G H].
  exists (a * C + A * (b + C * n)), 
         (c * G + E * (d + G * n)).
  lia.
Qed.

End Congruence.

(*************
 * TASK 3!/2 *
 *************)

Section AbstractAlgebra.

(** Let's define a typeclass of monoids. *)

Class monoid (M : Type) :=
  { op : M -> M -> M
  ; op_assoc : forall x y z, op x (op y z) = op (op x y) z
  ; ne : M
  ; ne_l : forall x, op ne x = x
  ; ne_r : forall x, op x ne = x
  }.

Infix "#" := op (at level 20, left associativity).

(** This is a _typeclass_. You can think of typeclasses as interfaces from your favorite imperative progamming language.

In imperative languages, interfaces define some functionality, then you define a bunch of classes that implement said functionality. In declarative languages such as Coq (and, more famously, Haskell), typeclasses also define some functionality, and you need to _instantiate_ them with the appropriate _datatypes_ given at hand.

Instantiating a typeclass for a datatype means you define the functionality described by that typeclass for that _specific_ datatype. In other words, typeclasses DESCRIBE (DECLARE) functionality, and instances of typeclasses for datatypes DEFINE functionality.

Now, what information does the typeclass of monoids contain?

(1) The [monoid] typeclass accepts a [Type] [M].
(2) A binary operation [op] accepts two arguments of type [M] and returns an element
    of type [M]. This encodes the closure property.
(3) [op_assoc] is a _proof_ that the binary operation [op] is associative.
(4) A neutral element [ne] is of type [M] (or, in set-theoretic terms, is an element
    of [M]).
(5) [ne_l] and [ne_r] are _proofs_ of the left and right unit laws.

Let's see how to instantiate a typeclass of monoids with our beloved Peano naturals! *)

Instance nat_monoid : monoid nat :=
  {| op := Nat.add
   ; op_assoc := Nat.add_assoc
   ; ne := 0
   ; ne_l := Nat.add_0_l
   ; ne_r := Nat.add_0_r
  |}.

(** We can either provide a name for our instance, or create an anonymous instance. Here, the name of the instance is [nat_monoid], and it has type [monoid nat].

The functions and constants don't need to have type signatures explicitly written again in the instances, they just need to have their appropriate definitions bound to them. Since [M] is [nat], [op] has type [nat -> nat -> nat]. One of the simplest operations that has this type is addition! Thankfully, it is associative ([Nat.add_assoc]), and [0] is its neutral element. Marvelous. Here, all the appropriate proofs have been already defined _for_ us.

Let's now define a VERY INTERESTING type synonym of _positive_ integers (i.e. 1, 2, 3, etc.). Here's why this type synonym is 'interesting': *)

Definition pos := nat -> nat.

(** We're encoding the set of all positive integers in a _function_ from non-negative integers [nat] to non-negative integers [nat]!

How on earth does this function encode positive integers?!?! Recall that _every_ positive integer can be _uniquely_ factorized into a product of prime numbers. The primes are, as we all know, a countable set, which is equivalent to the set of [nat]s (we can enumerate them). Moreover, the exponents in the unique factorization can vary from 0 to whatever. In other words, the exponents have type [nat].

So, what is this function [nat -> nat]? It's the function that accepts an index of the prime number and returns an exponent!

Let's define the number [twelve] of type [pos]. 12 = 2^2 * 3^1 *)

Definition twelve : pos := fun pIndex =>
  match pIndex with
  | 0 (* if the prime number in question is [2] *)      => 2
  | 1 (* if the prime number in question is [3] *)      => 1
  | _ (* every other prime number's exponent is zero *) => 0
  end.

(** EXERCISE (0.6 points). Prove that the type [pos] is a monoid (i.e. define a [monoid] instance)! Come up with the simplest associative operation that has a neutral element satisfying the left and right unit laws.

You can define the operation and prove the associativity and the unit laws _outside_ of the scope of the instance, and then fill in the binds of the instance with these definitions.

You can use [apply functional_extensionality] in your proofs to transform a goal of the form [f = g] into a goal of the form [forall x, f x = g x]. This is called _functional extensionality_, which says that two functions are equal iff, for every input from the domain, the results of applying the functions to the given input are equal. Since sets are functions, functional extensionality will come in handy here.

If you feel like you can't progress through the proof, don't forget to [unfold] your definitions! [unfold x] is a tactic that unfolds the definition [x] in the type of the goal. [unfold x in hyp] unfolds [x] in a hypothesis [hyp]; if [hyp] is '*', the unfolding occurs _everywhere_. *)

(********************************************)

(** We know that groups are a subclass of monoids where each element is _invertible_. We can encode the 'being a subclass of' (or, equivalently, 'being a superclass of') relationship with Coq's syntactic beauty! *)

Class group (M : Type) `{m : monoid M} :=
  { inv : M -> M
  ; inv_l : forall x, x # inv x = ne
  ; inv_r : forall x, inv x # x = ne
  }.

(** We define the typeclass [group] which accepts a type [M] and an object [m] of type [monoid M], indicating that in order to define a [group] instance for the type [M], we need to have a [monoid] instance for that same type. In other words, [group] is a _subclass_ of [monoid].

Here, we could have omitted the 'm :' part of the second argument, and could have just written [`{monoid M}], since we don't use [m] anywhere in the definition of the subclass. Thanks to the second argument, we are able to reuse [op] (and its infix notation [#]) and [ne].

The unary function [inv] inverts an element, and the two following theorems are the crucial left- and right-inverse theorems.

Bear in mind that we could've written `(...) instead of `{...}. Recall that, roughly speaking, the arguments wrapped with parentheses '(...)' are explicit, and the arguments wrapped in
'{...}' are implicit. By writing [`{m : monoid M}] we essentially say `I don't want to explicitly provide an argument of the type [monoid M], please resolve the instance yourself.` *)

(** EXERCISE (0.2 points). Define a typeclass of COMMUTATIVE MONOIDS! Don't hesitate to glance at the definition of the [group] typeclass to recall how to create subclasses. Afterward, prove that [nat]s form a commutative monoid under addition (i.e. create an instance).

NECESSARY HINT: If you get stuck because the operation [op] is not unified with your associative operation, you will need to perform eta-expansion.

Eta-expansion of the function [f] of arity 'n' means writing the arguments explicitly and applying them to the function.

[f] -->> [fun x1 x2 .. xn => f x1 x2 .. xn] *)

Class comm_monoid (M : Type) `{monoid M} :=
{
  op_comm : forall x y, op x y = op y x
}.

Instance nat_comm_monoid : comm_monoid nat.
Proof.
  constructor.
  intros x y.
  apply Nat.add_comm.
Qed.

(** EXERCISE (0.1 points). Prove the uniqueness of the neutral element. *)

Definition ne_laws {M : Type} op (ne : M)
  := forall x, (op ne x = x /\ op x ne = x).

Theorem ne_unique {M : Type} {op} (ne ne' : M)
  : ne_laws op ne -> ne_laws op ne' -> ne = ne'.
Proof.
  intros.
  specialize (H ne').
  specialize (H0 ne).
  destruct H as [A B].
  destruct H0 as [C D].
  rewrite A in D.
  symmetry.
  exact D.
Qed.

(** EXERCISE (0.3 points). Prove the uniqueness of the inverse of a given element. *)

Definition is_inv {M : Type} {op} {ne : M} (invx x : M)
  := op x invx = ne /\ op invx x = ne.

Definition assoc {M : Type} (op : M -> M -> M)
  := forall a b c, op a (op b c) = op (op a b) c.

Theorem inv_unique {M : Type} {op} {ne} {a b c : M}
  : ne_laws op ne -> assoc op ->
    @is_inv M op ne b a -> @is_inv M op ne c a -> b = c.
Proof.
  intros.
  pose proof (H c) as [C F].
  unfold is_inv in H1.
  destruct H1 as [G I].
  rewrite <- G in F.
  rewrite -> H0 in F.
  unfold is_inv in H2.
  destruct H2 as [J K].
  rewrite -> K in F.
  unfold ne_laws in H.
  specialize (H b).
  destruct H as [A B].
  rewrite -> A in F.
  exact F.
Qed.

(** FORMER FINAL EXERCISE (0.6 points). Prove that the group commutator is equal to the identity element iff the commutativity property holds.

Useful functions: [eq_sym, f_equal] *)

Definition commutator {G : Type} `{group G} (x y : G)
  := x # y # inv x # inv y.

Notation "'[' x  ',,' y ']'" := (commutator x y).

Theorem commutator_ne {G : Type} `{group G}
  : forall x y, [x ,, y] = ne <-> x # y = y # x.
Admitted.

End AbstractAlgebra.

(***********
 * TASK 1! *
 ***********)

Section Sets.

(** Did you know we can interpret primitive sets as functions? *)

Definition set : Type -> Type := fun A => A -> Prop.

Notation "'{' x ':|:' P '}'" := (fun x => P).

(** A set accepts a type (for example, [set nat] can be interpreted as 'a set of nats') and returns a function from that type to the universe called [Prop], the universe of propositions.

The notation above is reminiscent of the notation for set construction.

Let's now define a function called [mem]. *)

Definition mem {A : Type} (a : A) (s : set A) := s a.

(** It's a function that accepts an element of the type [A], a set of elements of type [A], and answeres the question 'does the element belong to the set?' Since [set] is represented by the function [A -> Prop], we simply apply [s] to [a]. The result has type [Prop], which is a proposition. *)

Notation "x '\in' X" := (mem x X) (at level 70).

(** With this definition of sets and membership we are able to create the empty set and the universal set. To do that, let's recall the definitions of [True : Prop] and [False : Prop]. All eyes on the output window. *)

Print True.
Print False.

(** The [True] datatype is the datatype with one nullary constructor, and the [False] datatype is the datatype with no constructors at all (i.e. it's impossible to construct a value of the type [False], it's _uninhabited_). *)

Definition U {A} : set A := {_ :|: True}.
Definition empty {A} : set A := {_ :|: False}.
Notation "∅" := empty.

(** EXERCISE (0.05 points). Prove that all elements belong to the universal set. *)

Theorem univ_set {A : Type} : forall (x : A), x \in U.
Proof.
  intros.
  unfold U. 
  unfold mem.
  apply I.
Qed.

(** EXERCISE (0.05 points). Prove that none of the elements belong to the empty set. *)

Theorem empty_set {A : Type} : forall (x : A), ~ x \in ∅.
Proof.
  intros.
  unfold not. 
  unfold mem. 
  unfold empty.
  intros.
  apply H.
Qed.

(** Important note. Here specifically, the empty set and the universal set are BY _NO_ MEANS unique, since we can define as many empty types and unit-types as we wish. *)

(** EXERCISE (0.1 points). Conjunction and disjunction are propositions. Define the intersection [inter] and the union [union] of sets accordingly. Here are the definitions of conjunction and disjunction. Do not erase any of the notations, they might come in handy.

Place your definitions above the notations. n order to acquire 0.1 points for this task, you need to define both [inter] and [union]. *)

Definition inter {A : Type} (s t : A -> Prop) : A -> Prop :=
  fun x => s x /\ t x.

Definition union {A : Type} (s t : A -> Prop) : A -> Prop :=
  fun x => s x \/ t x.

Print and.
Print or.

Notation "s ∩ t" := (inter s t) (at level 71, left associativity).
Notation "s ∪ t" := (union s t) (at level 72, left associativity).

(** EXERCISE (0.1 points). Define the subset relation. A set [s] is a subset of a set [t] iff, for every element in the set [s], that element is also a member of the set [t].

Place your definition above the notation. *)

Definition subset {A : Type} (q r : A -> Prop) : Prop :=
  forall x, q x -> r x.

Notation "s ⊂ t" := (subset s t) (at level 73).

(** EXERCISE (0.1 points). Define the 'equality' of sets! Two sets are equal iff one is the subset of the other and vice versa.

Place your definition above the notation. *)

Definition set_eq {A : Type} (q r : A -> Prop) : Prop :=
  forall x, q x <-> r x.

Notation "s ≡ t" := (set_eq s t) (at level 74).

(** EXERCISE (0.2 points). Prove that the universal set is the neutral element of the intersection.

Here you need to use the set equality defined above. Don't forget about an important syntactic operation: [;]. Suppose you write [t1; t2] in your proof, and [t1] and [t2] are tactics. This means 'apply [t1] to the current goal and then apply [t2] to EVERY goal _generated by_ [t1].' This syntactic operation usually helps write concise, though sometimes unreadable, proofs :)

Useful tactic: [contradiction]. If there is an 'obvious' contradiction in the hypotheses, Coq will automatically close the goal and prove the theorem. *)

Theorem univ_inter_neut {A} {s : set A} : U ∩ s ≡ s.
Proof.
  unfold set_eq, inter.
  intros.
  split.
  intros.
  destruct H as [B C].
  exact C.
  split.
  easy.
  exact H.
Qed.

(** EXERCISE (0.2 points). Prove that the empty set is the neutral element of the union. *)

Theorem empty_union_neut {A} {s : set A} : ∅ ∪ s ≡ s.
Proof.
  unfold set_eq, inter.
  intros.
  split.
  intros.
  destruct H as [B | C].
  easy.
  exact C.
  right.
  exact H.
Qed.

(** The above defined intersection and union are binary operations... What about their n-ary counterparts? Suppose instead of two sets we have a collection of sets indexed by some type I. Then we can define the I-intersection and I-union as follows. *)

Definition Inter {I} {A} (f : I -> set A) : set A
  := {x :|: forall i, x \in f i}.
Definition Union {I} {A} (f : I -> set A) : set A
  := {x :|: exists i, x \in f i}.

Notation "'⋂' i ',' f" := (Inter (fun i => f)) (at level 80).
Notation "'⋃' i ',' f" := (Union (fun i => f)) (at level 81).

(** Pay attention to the quantifiers. For an element [x] to belong to the I-intersection of an indexed family of sets [f] it needs to belong to [every] set of the family, hence [forall]. Same kind of logic for the I-union. *)

(** EXERCISE (0.4 points). Prove that an I-intersection of an intersection is the same as an intersection of I-intersections. *)

Theorem Inter_inter {I} {U} {A B : I -> set U} : (⋂ i , A i ∩ B i) ≡ (⋂ i, A i) ∩ (⋂ i, B i).
Proof.
  unfold set_eq, Inter.
  intros.
  split.
  intros.
  split.

  intros. 
  specialize (H i). 
  destruct H as [C D]. 
  exact C.

  intros. 
  specialize (H i). 
  destruct H as [E F]. 
  exact F.

  intros. 
  destruct H as [G J].
  split.
  specialize (G i). 
  exact G.
  specialize (J i). 
  exact J.
Qed.

(** EXERCISE (0.4 points). Prove that an I-union of a union is the same as a union of I-unions. *)

Theorem Union_union {I} {U} {A B : I -> set U} : (⋃ i , A i ∪ B i) ≡ (⋃ i, A i) ∪ (⋃ i, B i).
Admitted.

(** To end off the section, here is the definition of the AXIOM OF SET EXTENSIONALITY. Beware of the yellow highlight. *)

Axiom set_ext : forall {A} (s t : set A), s ≡ t -> s = t.

End Sets.
