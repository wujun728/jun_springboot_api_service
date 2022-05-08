import org.ssssssss.script.GrammarTests$AutoClose as A
import java.lang.StringBuilder
import java.util.concurrent.atomic.LongAdder
var sb = new StringBuilder()


var a = () => {
    try (;;
        var a = new A(sb, '1');
        var a = new A(sb, '2');
    ;;
    ) {
        sb.append('try')
        var n = 1 / 0
    } catch(e){
        sb.append('catch')
    } finally{
        sb.append('finally')
    }
}

var b = () => {
    try {
        return 2 / 0;
    } catch(e){
        return 9
    } finally{
        return 8
    }
}

var c = () => {
    try {
        if(false) {
            return 1
        }
        if(true){
            return 2
        }
    } finally{
        return 3
    }
}

var d = () => {
    try {
        return 1
    } finally{
        return 3
    }
}

var e = () => {
     var t = 0
     try {
         t = 1
         return t
     } finally{
         t = 2
     }
 }

 var f = () => {
    LongAdder addr = new LongAdder();
    try {
       addr.increment();
       return addr.intValue();
    } finally{
        addr.increment();
    }
 }

a()
sb.append(String.valueOf(b()))
sb.append(String.valueOf(c()))
sb.append(String.valueOf(d()))
sb.append(String.valueOf(e()))
sb.append(String.valueOf(f()))


return sb.toString()