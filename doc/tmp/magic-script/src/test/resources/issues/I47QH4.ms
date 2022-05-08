let a = '1,2,3'
let b  = a.split(',')
let val = 0;
for (it in b) {
    val += it::int;
}
return val;