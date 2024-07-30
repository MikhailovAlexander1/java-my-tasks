let divide = (a, b) => a / b;
let subtract = (a, b) => a - b;
let add = (a, b) => a + b;
let multiply = (a, b) => a * b;

function Variable (letter) {
    this.letter = letter;
}

Variable.prototype.evaluate = function(x, y, z) {
    if (this.letter === "x") {
        return x;
    } else if (this.letter === "y") {
        return y;
    } else {
        return z;
    }
}

Variable.prototype.toString = function () {
    return this.letter;
}

Variable.prototype.prefix = function () {
    return this.letter;
}

function Const (value) {
    this.value = +value;
}

Const.prototype.evaluate = function () {
    return this.value;
}

Const.prototype.toString = function () {
    return this.value.toString();
}

Const.prototype.prefix = function () {
    return this.value.toString();
}

function Negate (f) {
    this.f = f;
}

Negate.prototype.evaluate = function (x, y, z) {
    return -(this.f.evaluate(x, y, z));
}

Negate.prototype.toString = function () {
    return getString("negate", this.f);
}

Negate.prototype.prefix = function () {
    return getPrefix("negate", this.f);
}

function Add (f1, f2) {
    this.f1 = f1;
    this.f2 = f2;
}

Add.prototype.evaluate = function (x, y, z) {
    return new Bin(add, this.f1, this.f2).evaluate(x, y, z);
}

Add.prototype.toString = function () {
    return getString("+", this.f1, this.f2);
}

Add.prototype.prefix = function () {
    return getPrefix("+", this.f1, this.f2);
}

function Subtract (f1, f2) {
    this.f1 = f1;
    this.f2 = f2;
}

Subtract.prototype.evaluate = function (x, y, z) {
    return new Bin(subtract, this.f1, this.f2).evaluate(x, y, z);
}

Subtract.prototype.toString = function () {
    return getString("-", this.f1, this.f2);
}

Subtract.prototype.prefix = function () {
    return getPrefix("-", this.f1, this.f2);
}

function Multiply (f1, f2) {
    this.f1 = f1;
    this.f2 = f2;
}

Multiply.prototype.evaluate = function (x, y, z) {
    return new Bin(multiply, this.f1, this.f2).evaluate(x, y, z);
}

Multiply.prototype.toString = function () {
    return getString("*", this.f1, this.f2);
}

Multiply.prototype.prefix = function () {
    return getPrefix("*", this.f1, this.f2);
}

function Divide (f1, f2) {
    this.f1 = f1;
    this.f2 = f2;
}

Divide.prototype.evaluate = function (x, y, z) {
    return new Bin(divide, this.f1, this.f2).evaluate(x, y, z);
}

Divide.prototype.toString = function () {
    return getString("/", this.f1, this.f2);
}

Divide.prototype.prefix = function () {
    return getPrefix("/", this.f1, this.f2);
}

function Sinh (sinh) {
    this.f = sinh;
}

Sinh.prototype.evaluate = function (x, y, z) {
    return Math.sinh(this.f.evaluate(x, y, z));
}

Sinh.prototype.toString = function () {
    return getString("sinh", this.f);
}

Sinh.prototype.prefix = function () {
    return getPrefix("sinh", this.f);
}

function Cosh (cosh) {
    this.f = cosh;
}

Cosh.prototype.evaluate = function (x, y, z) {
    return Math.cosh(this.f.evaluate(x, y, z));
}

Cosh.prototype.toString = function () {
    return getString("cosh", this.f);
}

Cosh.prototype.prefix = function () {
    return getPrefix("cosh", this.f);
}

function Bin (f, a, b) {
    this.f = f;
    this.a = a;
    this.b = b;
}

Bin.prototype.evaluate = function (x, y, z) {
    return this.f(this.a.evaluate(x, y, z), this.b.evaluate(x, y, z));
}

let getString = (op, f1, f2) => f2 !== undefined ? f1 + " " + f2 + " " + op : f1 + " " + op;

let getPrefix = (op, f1, f2) => f2 !== undefined ? "(" + op + " " + f1.prefix() + " " + f2.prefix() + ")"
    : "(" + op + " " + f1.prefix() + ")";

isDigit = function (el) {
    if (el === ' ') {
        return false;
    }
    return !isNaN(+el);
}

let variables = ["x", "y", "z"];
let operations = ["+", "-", "*", "/", "negate", "sinh", "cosh"];
let brackets = ["(", ")"];

let binOp = ['+', '-', '*', '/'];
let unaryOp = ['negate', 'sinh', 'cosh'];

let map = new Map([
    ['+', Add],
    ['-', Subtract],
    ['*', Multiply],
    ['/', Divide],
    ["negate", Negate],
    ["sinh", Sinh],
    ["cosh", Cosh]
]);

let getAction = function (op, f1, f2) {
    let action = map.get(op);
    return new action(f1, f2);
}

let parseMain = function (expr, isPrefix) {
    let stack = [];
    for (let i = 0; i < expr.length; i++) {
        if (isDigit(expr[i])) {
            stack.push(new Const(expr[i]));
        } else if (variables.includes(expr[i])) {
            stack.push(new Variable(expr[i]));
        } else {
            let f1 = stack.pop();
            if (!unaryOp.includes(expr[i])) {
                let f2 = stack.pop();
                if (isPrefix) {
                    stack.push(getAction(expr[i], f1, f2));
                } else {
                    stack.push(getAction(expr[i], f2, f1));
                }
            } else {
                stack.push(getAction(expr[i], f1));
            }
        }
    }
    return stack[0];
}

let parse = function (string) {
    let expr = getGoodString(string).split(' ');
    return parseMain(expr, false);
}

let parsePrefix = function (string) {
    let expr = getPrefixArray(string).reverse();
    return parseMain(expr, true);
}

let getGoodString = function (string) {
    let newString = "";
    let whitespace = 0;
    for (let i = 0; i < string.length; i++) {
        if (string.charAt(i) !== ' ') {
            newString += string.charAt(i);
            whitespace = 1;
        } else if (whitespace === 1) {
            newString += ' ';
            whitespace = 0;
        }
    }
    return newString;
}

function ExpressionFormatError(message) {
    this.name = 'ExpressionFormatError';
    this.message = message || "Wrong format of expression. Can not to parse it!\n";
}
ExpressionFormatError.prototype = Object.create(Error.prototype);
ExpressionFormatError.prototype.constructor = ExpressionFormatError;

function ExpressionArgumentError(message) {
    this.name = 'ExpressionArgumentError';
    this.message = message;
}
ExpressionArgumentError.prototype = Object.create(Error.prototype);
ExpressionArgumentError.prototype.constructor = ExpressionArgumentError;

let skipWhiteSpaces = function (string, i) {
    while (i < string.length && string[i] === ' ') {
        i++;
    }
    return i;
}

let hasNextDigit = function (string, i) {
    let buf = "";
    if (string[i] === '-') {
        buf += '-';
        i++;
    }
    while (i < string.length && isDigit(string[i])) {
        buf += string[i];
        i++;
    }
    if (buf === '-') {
        return "";
    } else {
        return buf;
    }
}

let getExprElement = function (string, i) {
    let bufForElement = hasNextDigit(string, i);
    if (bufForElement.length !== 0) {
        return bufForElement;
    }
    while (i < string.length && string[i] !== ' ') {
        bufForElement += string[i];
        i++;
        if (operations.includes(bufForElement) || variables.includes(bufForElement)
            || brackets.includes(bufForElement)) {
            return bufForElement;
        }
    }
    let exprTitle = "Expression : \"";
    let buf = '';
    for (let j = 0; j < i + exprTitle.length - bufForElement.length; j++) {
        buf += ' ';
    }
    throw new ExpressionArgumentError("Wrong expression elements format: \"" + bufForElement + "\"\n" +
        exprTitle + string + "\"\n" + buf + "^\n");
}

let skipNotWhiteSpace = function (string, i) {
    let elementToSkip = hasNextDigit(string, i);
    if (elementToSkip.length !== 0) {
        return i + elementToSkip.length;
    }
    while (i < string.length && string[i] !== ' ') {
        elementToSkip += string[i];
        i++;
        if (operations.includes(elementToSkip) || variables.includes(elementToSkip)
            || brackets.includes(elementToSkip)) {
            return i;
        }
    }
}

let oneElementExpression = function (string, i) {
    if (string[i] === '(') {
        i++;
        i = skipWhiteSpaces(string, i);
    }
    i = skipNotWhiteSpace(string, i);
    i = skipWhiteSpaces(string, i);
    if (string[i] === ')') {
        i++;
        i = skipWhiteSpaces(string, i);
    }
    return i === string.length;
}

let getPrefixArray = function (string) {
    let exprTitle = "Expression : \"";
    let bufForError = '';
    let i = skipWhiteSpaces(string, 0);
    if (i === string.length) {
        throw new ExpressionFormatError("no elements in expr!\n");
    }
    let expr = [];
    let exprElement;
    if (oneElementExpression(string, i)) {
        exprElement = getExprElement(string, i);
        if (!variables.includes(exprElement) && !isDigit(exprElement)) {
            throw new ExpressionFormatError("Expression has only one argument and it is wrong!\n" +
                "Expected : digit or variable.\n" +
                "Actual : " + exprElement + ".\n");
        }
        expr.push(exprElement);
        return expr;
    }
    let stack = [];
    while (i < string.length) {
        exprElement = getExprElement(string, i);
        if (!isDigit(exprElement) && !variables.includes(exprElement)
            && !binOp.includes(exprElement) && !unaryOp.includes(exprElement)
            && !brackets.includes(exprElement)) {
            for (let j = 0; j < i + exprTitle.length; j++) {
                bufForError += ' ';
            }
            throw new ExpressionArgumentError("Unsupported element in expression : \"" + exprElement + "\"!\n" +
                "Element position is " + (i + 1) + ".\n" +
                exprTitle + string + "\"\n" + bufForError + "^\n");
        } else {
            let buf = stack.pop();
            if (binOp.includes(exprElement) || unaryOp.includes(exprElement)) {
                if (buf !== '(') {
                    throw new ExpressionFormatError("Expected only one math operation after \'(\'" +
                        ", actual is not this way!\n" +
                        "Sign position is " + (i + 1) + ".\n");
                }
                stack.push(buf);
                if (binOp.includes(exprElement)) {
                    stack.push(2);
                } else {
                    stack.push(1);
                }
                expr.push(exprElement);
            } else if (variables.includes(exprElement) || isDigit(exprElement)) {
                if (!isDigit(buf)) {
                    for (let j = 0; j < i + exprTitle.length; j++) {
                        bufForError += ' ';
                    }
                    throw new ExpressionArgumentError("Expression element : \"" + exprElement +
                        "\" is staying right after the bracket : \"" + buf + "\"!\n" +
                        exprTitle + string + "\"\n" + bufForError + "^\n");
                } else if (buf === 0) {
                    let supportBuf;
                    while (isDigit(supportBuf = stack.pop())) {
                        buf = supportBuf;
                    }
                    for (let j = 0; j < i + exprTitle.length; j++) {
                        bufForError += ' ';
                    }
                    throw new ExpressionFormatError("Too many variables for one sign!\n__________\n" +
                        "Expected : " + buf + ".\n" +
                        "Actual : " + (buf + 1) + ".\n__________\n" +
                        exprTitle + string + "\"\n" + bufForError + "^\n");
                }
                stack.push(buf);
                stack.push(buf - 1);
                expr.push(exprElement);
            } else if (exprElement === ')') {
                if (buf !== 0) {
                    for (let j = 0; j < i + exprTitle.length; j++) {
                        bufForError += ' ';
                    }
                    throw new ExpressionFormatError("Closed bracket is staying in the wrong place\n" +
                        exprTitle + string + "\"\n" + bufForError + "^\n");
                }
                while (buf !== '(') {
                    buf = stack.pop();
                }
                if (stack.length === 0) {
                    break;
                }
                buf = stack.pop();
                stack.push(buf);
                stack.push(buf - 1);
            } else if (exprElement === '(') {
                if (typeof buf === 'undefined') {
                    stack.push('(');
                } else {
                    if (buf === '(') {
                        for (let j = 0; j < i + exprTitle.length; j++) {
                            bufForError += ' ';
                        }
                        throw new ExpressionFormatError("The second opened bracket in a row!\n" +
                            exprTitle + string + "\"\n" + bufForError + "^\n");
                    }
                    stack.push(buf);
                    stack.push('(');
                }
            }
        }
        i = skipNotWhiteSpace(string, i);
        i = skipWhiteSpaces(string, i);
    }
    if (string[i] === ')') {
        i++;
    }
    i = skipWhiteSpaces(string, i);
    if (i < string.length || stack.length > 0) {
        for (let j = 0; j < i + exprTitle.length; j++) {
            bufForError += ' ';
        }
        throw new ExpressionFormatError("Unfinished expression!\n" +
            exprTitle + string + "\"\n" + bufForError + "^\n");
    }
    return expr;
}