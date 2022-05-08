import 'java.util.Date' as date;
import 'java.text.SimpleDateFormat' as sdf;
import 'java.util.concurrent.ThreadPoolExecutor'

var v1 = new ThreadPoolExecutor.AbortPolicy();
var now = new date();
var df = new sdf('yyyy-MM-dd');
if(v1){
    return df.format(now);
}
return 'error'
