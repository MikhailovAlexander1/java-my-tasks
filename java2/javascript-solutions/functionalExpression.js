const variable = letter => (x, y, z) => {
    if (letter === "x") {
        return x;
    } else if (letter === "y") {
        return y;
    } else {
        return z;
    }
};

const cnst = value => (x, y, z) => value;

let negate = f => (x, y, z) => -f(x, y, z);

let pi = cnst(Math.PI);

let e = cnst(Math.E);

let bin = (f) => (f1, f2) => (x, y, z) => f(f1(x, y, z), f2(x, y, z));

let add = bin((a, b) => a + b)

let subtract = (f1, f2) => (x, y, z) => f1(x, y, z) - f2(x, y, z);

let multiply = (f1, f2) => (x, y, z) => f1(x, y, z) * f2(x, y, z);

let divide = (f1, f2) => (x, y, z) => f1(x, y, z) / f2(x, y, z);
//
// let expr = add(
//    subtract(
//        multiply(
//            variable("x"),
//            variable("x")
//        ),
//        multiply(
//            cnst(2),
//            variable("x")
//        ),
//    ),
//    cnst(1)
// );
//
// for (let i = 0; i < 10; i++) {
//     console.log(expr(i, 0, 0));
// }