package lab2.tests;

public class Test {
    public static final String[] expressionTest = {
        /*1*/"", // E -> ε (успешно)
        /*2*/"_", // E -> ε (неверный ввод)
        /*3*/"var a: Array<Int>;", // E -> V : A ; (успешно)
        /*4*/": Array<Int>;", // E -> V : A ; (отсутствие V)
        /*5*/"var a Array<Int>;", // E -> V : A ; (отсутствие :)
        /*6*/"var b: ;", // E -> V : A ; (отсутствие A)
        /*7*/"var a: Array<Int>", // E -> V : A ; (отсутствие ;)
        /*8*/"var a: Array<Int>; Int"}; // E -> V : A ; (продолжение после конца строки)
    public static final int[] expressionTestCheck = {0, 1, 0, 1, 1, 1, 1, 1};

    public static final String[] variableNameTest = {
        /*1*/"var arrayForFloats: Array<Float>;", // V -> var name (успешно)
        /*2*/"var _: Array<Int>;", // V -> var name (неверный name)
        /*3*/"var 1: Array<Double>", // V -> var name (неверный name)
        /*4*/"var 11f1: Array<Float>", // V -> var name (неверный name)
        /*5*/"var STATIC: Array<Document>;", // V -> var name (успешно)
        /*6*/"a: Array<Int>;", // V -> var name (отсутствие var)
        /*7*/"var: Array<Int>;"}; // V -> var name (отсутствие name)
    public static final int[] variableNameTestCheck = {0, 1, 1, 1, 0, 1, 1};

    public static final String[] arrayTypeTest = {
        /*1*/"var a: Array<MyUniqueType>;", // A -> T AT (успешно)
        /*2*/"var a: <Int>;", // A -> T AT (отсутствие T)
        /*3*/"var a: 111aaa<Int>;", // A -> T AT (неверный T)
        /*4*/"var a: Array<1nt>;", // A -> T AT (неверный T)
        /*5*/"var a: Array;"}; // A -> T AT (отсутствие AT)
    public static final int[] arrayTypeTestCheck = {0, 1, 1, 1, 1};

    public static final String[] ATTest = {
        /*1*/"var a: Array<Int>;", // AT -> < TT > (успешно)
        /*2*/"var a: ArrayInt>;", // AT -> < TT > (отсутствие '<')
        /*3*/"var a: Array<Int;", // AT -> < TT > (отсутствие '>')
        /*4*/"var a: Array<>;"}; // AT -> < TT > (отсутствие TT)
    public static final int[] ATTestCheck = {0, 1, 1, 1};

    public static final String[] TTTest = {
        /*1*/"var a: Array<Int>;", // TT -> T TTT (успешно)
        /*2*/"var a: Array<Array<Int>>;", // TT -> T TTT -> T AT -> ... -> T < T < T ε > > (успешно)
        /*3*/"var a: Array<<Int>>;", // TT -> T TTT (отсутствие T)
        /*4*/"var a: Array<Array<>>;", // TT -> T TTT (отсутствие TTT)
        /*5*/"var a: Array<Document<Note>>>;", // TT -> T TTT (лишняя '>' в TTT -> AT -> < TT >)
        /*6*/"var a: Array<Document<<>>;"}; // TT -> T TTT -> ... -> T < T ε > (неверный T - "<")
    public static final int[] TTTestCheck = {0, 0, 1, 1, 1, 1};

    public static final String[] TTTTTest = {
        /*1*/"var a224 : Array<Array<HashMap<Int, String>>>;",
        /*2*/"var hello : Array<HashMap<Int, String>>;",
        /*3*/"var a224 : Array<HashMap<Int, Array<Document>>>;",
        /*4*/"var a224 : Array<Array<MyClass<A, B, C, D>>>;",
        /*5*/"var a224 : Array<HashMap<Array<MyClass<A, B, C, D>>, Array<HashMap<Int, String>>>>;",
        /*6*/"var a224 : Array<HashMap<, Array<Document>>>;",
        /*7*/"var a224 : Array<HashMap<Int, >>>;"};
    public static final int[] TTTTTestCheck = {0, 0, 0, 0, 0, 1, 1};
}
