#include <iostream>
#include <cmath>

class Primitive {
public:
    virtual int apply(int... args) = 0;
};

class Z : public Primitive {
public:
    int apply(int... args) override {
        return 0;
    }
};

class N : public Primitive {
public:
    int apply(int... args) override {
        return args[0] + 1;
    }
};

class Pred : public Primitive {
public:
    int apply(int... args) override {
        return args[0] - 1;
    }
};

class U : public Primitive {
private:
    int k;

public:
    U(int k) : k(k) {}

    int apply(int... args) override {
        return args[k];
    }
};

class S : public Primitive {
private:
    Primitive* g;
    Primitive** f;
    int fLength;

public:
    S(Primitive* g, Primitive** f, int fLength) : g(g), f(f), fLength(fLength) {}

    int apply(int... args) override {
        int* argsG = new int[fLength];
        for (int i = 0; i < fLength; i++) {
            argsG[i] = f[i]->apply(args);
        }
        int result = g->apply(argsG);
        delete[] argsG;
        return result;
    }
};

class R : public Primitive {
private:
    Primitive* f;
    Primitive* g;

public:
    R(Primitive* f, Primitive* g) : f(f), g(g) {}

    int apply(int... args) override {
        int* x = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            x[i] = args[i];
        }
        int y = args[args.length - 1];

        if (y == 0) {
            int result = f->apply(x);
            delete[] x;
            return result;
        }

        args[args.length - 1] = std::abs(args[args.length - 1]) - 1;
        int* argsG = new int[args.length + 1];
        for (int i = 0; i < args.length; i++) {
            argsG[i] = args[i];
        }
        argsG[args.length] = this->apply(args);
        int result = g->apply(argsG);
        delete[] x;
        delete[] argsG;
        return result;
    }
};

class Sum : public R {
public:
    Sum() : R(new U(0), new S(new N(), new U(2))) {}
};

class Multy : public R {
public:
    Multy() : R(new Z(), new S(new Sum(), new U(0), new U(2))) {}
};

class Subtract : public R {
public:
    Subtract() : R(new U(0), new S(new Pred(), new U(2))) {}
};

class BorderSubtract : public Subtract {
public:
    int apply(int... args) override {
        int res = Subtract::apply(args);
        if (res < 0) {
            return 0;
        }
        return res;
    }
};

class Le : public R {
public:
    Le() : R(new U(0), new S(new Pred(), new U(2))) {}

    int apply(int... args) override {
        int res = BorderSubtract().apply(args);
        if (res == 0) {
            return 1;
        }
        return 0;
    }
};

class L : public S {
public:
    L() : S(new R(
            new Z(),
            new S(new R(new Z(), new S(new N(), new Z())),
                    new S(new N(), new Z()),
                    new S(new Subtract(), new U(1), new U(0))
            )),
            new U(0), new U(1),
            new S(new Le(), new U(0), new U(1))
    )) {}
};

class Sqrt : public S {
public:
    Sqrt() : S(
                     new R(
                             new U(0),
                             new S(
                                     new R(
                                             new U(2),
                                             new U(1)
                                                     ),
                                     new U(0),
                                     new U(1),
                                     new U(2),
                                     new S(new Sum(),
                                           new S(new L(),
                                                 new U(0),
                                                 new S(new Multy(),
                                                       new U(2),
                                                       new U(2)
                                                               )
                                                         ),
                                           new S(new Le(),
                                                 new S(new Multy(),
                                                       new S(new N(), new U(2)),
                                                       new S(new N(), new U(2))
                                                               ),
                                                 new U(0)
                                                         )
                                                   )
                                             )
                                     ),
                     new U(0),
                     new S(new N(), new U(0))
             ) {}
};

class Power : public R {
public:
    Power() : R(new S(new N(), new Z()), new S(new Multy(), new U(0), new U(2))) {}
};

class Remainder : public S {
public:
    Remainder() : S(
                          new R(
                                  new U(0),
                                  new S(new R(new U(2), new U(0)),
                                        new S(new Subtract(),
                                              new U(3),
                                              new U(1)
                                                      ),
                                        new U(1),
                                        new U(3),
                                        new S(new Sum(),
                                              new S(new Le(),
                                                    new S(new Sum(),
                                                          new U(3),
                                                          new U(1)
                                                                  ),
                                                    new U(1)
                                                            ),
                                              new S(new Le(),
                                                    new U(1),
                                                    new U(3)
                                                            )
                                                      )
                                                )
                                          ),
                          new U(0),
                          new U(1),
                          new U(0)
                  ) {}
};

class Plog : public S {
public:
    Plog() : S(new R(
                       new Z(),
                       new S(new R(new U(3), new U(2)),
                             new U(0),
                             new U(1),
                             new U(2),
                             new U(3),
                             new S(new L(),
                                   new S(new Remainder(),
                                         new U(1),
                                         new S(new Power(),
                                               new U(0),
                                               new S(new N(), new U(3))
                                                       )
                                                 ),
                                   new S(new N(), new Z())
                                           )
                                     )
                               ),
               new U(0),
               new U(1),
               new U(1)
             ) {}
};

int main() {
    Sum sum;
    std::cout << "Sum 2 + 2 = " << sum.apply(2, 2) << std::endl;

    Multy multy;
    std::cout << "2 + 3 = " << multy.apply(2, 3) << std::endl;

    Subtract subtract;
    std::cout << "5 - 2 = " << subtract.apply(5, 2) << std::endl;

    BorderSubtract borderSubtract;
    std::cout << "Bord 2 - 3 = " << borderSubtract.apply(2, 3) << std::endl;

    Le le;
    std::cout << "2 <= 2 = " << le.apply(2, 2) << std::endl;

    L L;
    std::cout << "2 < 2 = " << L.apply(2, 2) << std::endl;

    Sqrt Sqrt;
    std::cout << "Sqrt(17) = " << Sqrt.apply(17) << std::endl;

    Power Power;
    std::cout << "3^3 = " << Power.apply(3, 3) << std::endl;

    Remainder Remainder;
    std::cout << "17 % 3 = " << Remainder.apply(17, 3) << std::endl;

    Plog Plog;
    std::cout << "Plog(3, 108) = " << Plog.apply(3, 108) << std::endl;

    return 0;
}