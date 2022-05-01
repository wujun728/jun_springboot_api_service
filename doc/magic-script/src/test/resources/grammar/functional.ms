
new Thread(()=>{
    System.out.println("from lambda");
}).start();

var list = [{
    a: 1
}];

list.stream().map(it => it.a).forEach(it => System.out.println(it))
