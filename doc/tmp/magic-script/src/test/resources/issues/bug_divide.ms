var a1 = 10;
var a2 = 10l;
var a3 = 10d
var a4 = 10f;
var a5 = 10s;
var a6 = 10m;
var a7 = '10'::int;

var b1 = 2;
var b2 = 2l;
var b3 = 2d
var b4 = 2f;
var b5 = 2s;
var b6 = 2m;
var b7 = '2'::int;
var func = (x,y) => x / y;
try{
    func(1,'2')
} catch(e){
}
return [
    func(a1, b1),
    func(a2, b1),
    func(a3, b1),
    func(a4, b1),
    func(a5, b1),
    func(a6, b1),
    func(a7, b1),

    func(a1, b2),
    func(a2, b2),
    func(a3, b2),
    func(a4, b2),
    func(a5, b2),
    func(a6, b2),
    func(a7, b2),

    func(a1, b3),
    func(a2, b3),
    func(a3, b3),
    func(a4, b3),
    func(a5, b3),
    func(a6, b3),
    func(a7, b3),

    func(a1, b4),
    func(a2, b4),
    func(a3, b4),
    func(a4, b4),
    func(a5, b4),
    func(a6, b4),
    func(a7, b4),

    func(a1, b5),
    func(a2, b5),
    func(a3, b5),
    func(a4, b5),
    func(a5, b5),
    func(a6, b5),
    func(a7, b5),

    func(a1, b6),
    func(a2, b6),
    func(a3, b6),
    func(a4, b6),
    func(a5, b6),
    func(a6, b6),
    func(a7, b6),

    func(a1, b7),
    func(a2, b7),
    func(a3, b7),
    func(a4, b7),
    func(a5, b7),
    func(a6, b7),
    func(a7, b7)
].reduce((a,b) => a + b)