import java.util.Arrays;

public interface Primitive {
    int apply(int... args);
}

class Z implements Primitive {

    @Override
    public int apply(int... args) {
        return 0;
    }
}

class N implements Primitive {
    @Override
    public int apply(int... args) {
        return args[0] + 1;
    }
}

class Pred implements Primitive {
    @Override
    public int apply(int... args) {
        return args[0] - 1;
    }
}

class U implements Primitive {
    private int k;

    public U(int k) {
        this.k = k;
    }

    @Override
    public int apply(int... args) {
        return args[k];
    }
}

class S implements Primitive {
    private Primitive g;
    private Primitive[] f;

    public S(Primitive g, Primitive... f) {
        this.g = g;
        this.f = f;
    }

    @Override
    public int apply(int... args) {
        int[] argsG = new int[f.length];
        for (int i = 0; i < f.length; i++) {
            argsG[i] = f[i].apply(args);
        }
        return g.apply(argsG);
    }
}

class R implements Primitive {
    private Primitive f;
    private Primitive g;

    public R(Primitive f, Primitive g) {
        this.f = f;
        this.g = g;
    }

    @Override
    public int apply(int... args) {
        int[] x = Arrays.copyOfRange(args, 0, args.length - 1);
        int y = args[args.length - 1];

        if (y == 0) {
            return f.apply(x);
        }

        args[args.length - 1] = Math.abs(args[args.length - 1]) - 1;
        int[] argsG = Arrays.copyOf(args, args.length + 1);
        argsG[args.length] = this.apply(args);
        return g.apply(argsG);
    }
}

class Sum extends R {
    public Sum() {
        super(new U(0), new S(new N(), new U(2)));
    }
}

class Multy extends R {
    public Multy() {
        super(new Z(), new S(new Sum(), new U(0), new U(2)));
    }
}

class Subtract extends R {
    public Subtract() {
        super(new U(0), new S(new Pred(), new U(2)));
    }
}

class BorderSubtract extends Subtract {
    @Override
    public int apply(int... args) {
        int res = super.apply(args);
        if (res < 0) {
            return 0;
        }
        return res;
    }
}

class Le extends R {
    public Le() {
        super(new U(0), new S(new Pred(), new U(2)));
    }

    @Override
    public int apply(int... args) {
        int res = new BorderSubtract().apply(args);
        if (res == 0) {
            return 1;
        }
        return 0;
    }
}

class L extends S {
    public L() {
        super(new R(
                new Z(),
                new S(new R(new Z(), new S(new N(), new Z())),
                        new S(new N(), new Z()),
                        new S(new Subtract(), new U(1), new U(0))
                )),
                new U(0), new U(1),
                new S(new Le(), new U(0), new U(1))
        );
    }
}

class Sqrt extends S {
    public Sqrt() {
        super(
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
        );
    }
}

class Power extends R {
    public Power() {
        super(new S(new N(), new Z()), new S(new Multy(), new U(0), new U(2)));
    }
}

class Remainder extends S {
    public Remainder() {
        super(
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
        );
    }
}

class Plog extends S {
    public Plog() {
//        super(new U(0), new U(3));
        super(new R(
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
        );
    }

//    @Override
//    public int apply(int... args) {
//        if (new Remainder().apply(new U(0).apply(args[1]), new Power().apply(new U(0).apply(args[0]), new N().apply(args[2]))) == 0) {
//            args[2] = new N().apply(new N().apply(args[2]));
//            return super.apply(args);
//        }
//        return new U(2).apply(args);
//    }
}

class Main {
    public static void main(String[] args) {
        Sum sum = new Sum();
        System.out.println("Sum 2 + 2 = " + sum.apply(2, 2));

        Multy multy = new Multy();
        System.out.println("2 + 3 = " + multy.apply(2, 3));

        Subtract subtract = new Subtract();
        System.out.println("5 - 2 = " + subtract.apply(5, 2));

        BorderSubtract borderSubtract = new BorderSubtract();
        System.out.println("Bord 2 - 3 = " + borderSubtract.apply(2, 3));

        Le le = new Le();
        System.out.println("2 <= 2 = " + le.apply(2, 2));

        L L = new L();
        System.out.println("2 < 2 = " + L.apply(2, 2));

        Sqrt Sqrt = new Sqrt();
        System.out.println("Sqrt(17) = " + Sqrt.apply(17));

        Power Power = new Power();
        System.out.println("3^3 = " + Power.apply(3, 3));

        Remainder Remainder = new Remainder();
        System.out.println("17 % 3 = " + Remainder.apply(17, 3));

        Plog Plog = new Plog();
        System.out.println("Plog(3, 108) = " + Plog.apply(2, 96));
    }
}