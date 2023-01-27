(* Номер 1 *)

(* Симметричность *)
(* если ∀a,b∈X: aRb⇒bRa. *)
Definition sim {X : Type} (R : X -> X -> Prop) : Prop :=
forall (a:X) (b:X), R a b -> R b a.

(* Антисимметричность *)
(* если для любых неравных элементов a и b множества X из 
выполнения отношения aRb следует невыполнение отношения bRa. *)
Definition antisim {X : Type} (R : X -> X -> Prop) : Prop :=
forall (a:X) (b:X), not(a = b) /\ R a b -> not(R b a).

(* Асимместричность *)
(* если для любых элементов a и b множества X одновременное 
выполнение отношений aRb и bRa невозможно. *)
Definition asim {X : Type} (R : X -> X -> Prop) : Prop :=
forall (a:X) (b:X), R a b -> not(R b a).

(* Транзитивность *)
(* если для ∀ a,b,c∈X: (aRb) ∧ (bRc)⇒ (aRc). *)
Definition trans {X : Type} (R : X -> X -> Prop) : Prop :=
forall (a:X) (b:X) (c:X), R a b /\ R b c -> R a c.

(* Номер 2 *)

(* (a) A → B → A *)
Theorem a (A : Prop) (B : Prop) : A -> B -> A.
Proof.
    intros a.
    intros b.
    exact a.
Qed.

(* (A → B) → (A → B → C) → (A → C) *)
Theorem b (A : Prop) (B : Prop) (C : Prop) : (A -> B) -> (A -> B -> C) -> (A -> C).
Proof.
    intros ab.
    intros abc.
    intros a.
    apply abc.
    exact a.
    apply ab.
    exact a.
Qed.

(* A ∧ B → A *)
Theorem c (A : Prop) (B : Prop) : A /\ B -> A.
Proof.
    intros ab.
    destruct ab as [a b].
    exact a.
Qed.

(* A ∧ B → B *)
Theorem d (A : Prop) (B : Prop) : A /\ B -> B.
Proof.
    intros ab.
    destruct ab as [a b].
    assumption.
Qed.

(* A → B → A ∧ B *)
Theorem e (A : Prop) (B : Prop) : A -> B -> A /\ B.
Proof.
    intros a.
    intros b.
    split.
    exact a.
    exact b.
Qed.

(* A → A ∨ B *)
Theorem f (A : Prop) (B : Prop) : A -> A \/ B.
Proof.
    intros a.
    left.
    exact a.
Qed.

(* B → A ∨ B *)
Theorem g (A : Prop) (B : Prop) : B -> A \/ B.
Proof.
    intros b.
    right.
    exact b.
Qed.

(* (A → C) → (B → C) → (A ∨ B → C) *)
Theorem h (A : Prop) (B : Prop) (C : Prop) : (A -> C) -> (B -> C) -> (A \/ B -> C).
Proof.
    intros ac.
    intros bc.
    intros aorb.
    destruct aorb as [a | b].
    apply ac.
    exact a.
    apply bc.
    exact b.
Qed.

(* (A → B) → (A → ¬B) → ¬A *)
Theorem i (A : Prop) (B : Prop) : (A -> B) -> (A -> not(B)) -> not(A).
Proof.
    intros ab.
    intros anotb.
    unfold not in *.
    intros a.
    apply anotb.
    exact a.
    apply ab.
    exact a.
Qed.

(* сложение натуральных чисел *)
Theorem assosiation (A : nat) (B : nat) (C : nat) : (A + B) + C = A + (B + C).
Proof.
    intros.
    induction A.
    induction B.
    induction C.
    reflexivity.
    simpl.
    reflexivity.
    simpl.
    reflexivity.
    simpl.
    rewrite IHA.
    reflexivity.
Qed.

(* Номер 3 *)

(* Инъекция *)
Definition injection {A : Type} {B : Type} (f : A -> B) :=
forall (x1:A) (x2:A), not(x1 = x2) -> not(f(x1) = f(x2)).

(* Доказательство инъективности функции *)
Theorem injection_proof (A : Type) : injection(fun x : A => x).
Proof.
    unfold injection.
    intros.
    exact H.
Qed.

(* Сюръекция *)
Definition surjection {A : Type} {B : Type} (f : A -> B) :=
forall (y:B), exists (x:A), y = f(x).

(* Доказательство сюръективности функции *)
Theorem surjection_proof (A : Type) : surjection(fun x : A => x).
Proof.
    unfold surjection.
    intros.
    exists y.
    reflexivity.
Qed.

(* Биекция *)
Definition biection {A : Type} {B : Type} (f : A -> B) := 
forall (y:B) (x1:A) (x2:A), exists (x:A), (y = f(x) /\ not(x1 = x2)) -> not(f(x1) = f(x2)).

(* Definition injectionNew {A : Type} {B : Type} (f : A -> B) :=
forall (x:A), exists (y:B), y = f(x). *)

(* Definition biection {A : Type} {B : Type} (f : A -> B) := injectionNew(f) /\ surjection(f). *)

(* Доказательство, что композиции двух биективных функций биективна *)
Definition comp {A B C : Type} (g : B -> C) (f : A -> B) : A -> C :=
fun x : A => g (f x).
Notation "g :.: f" := (comp g f) (at level 41, right associativity).

(* Theorem biection_proof (A B C : Type) (g : B -> C) (f : A -> B) : biection(g :.: f).
Proof.
    unfold biection.
    unfold surjection.
    unfold injectionNew.
    split.
    all : intros.
    all : unfold comp.
    - apply x f g.
Qed. *)