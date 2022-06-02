
function serializeObject(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

function clearForm(datagrid) {
	$('#searchForm input').val('');
	datagrid.datagrid('load', {});
}

$.fn.serializeObject = function(){
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//js加法运算
function numAdd(num1, num2) {   
	   var baseNum, baseNum1, baseNum2;   
	   try {   
	      baseNum1 = num1.toString().split(".")[1].length;   
	   } catch (e) {    
	     baseNum1 = 0;  
	   }   
	   try {  
	       baseNum2 = num2.toString().split(".")[1].length;   
	   } catch (e) {  
	     baseNum2 = 0;   
	   }   
	   baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));  
	   var precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;//精度  
	   return ((num1 * baseNum + num2 * baseNum) / baseNum).toFixed(precision);;   
}; 

//js减法
function numSub(dataOne,dataTwo){ 

	var dataOneInt=dataOne.toString().split(".")[0]; 
	var dataOneFloat=""; 
	var dataTwoInt=dataTwo.toString().split(".")[0]; 
	var dataTwoFloat=""; 
	var lengthOne=0; 
	var lengthTwo=0; 
	var maxlength=0; 

	if(dataOne.toString().split(".").length==2){ 
	dataOneFloat=dataOne.toString().split(".")[1]; 
	lengthOne=dataOneFloat.toString().length; 

	} 
	if(dataTwo.toString().split(".").length==2){ 
	dataTwoFloat=dataTwo.toString().split(".")[1]; 
	lengthTwo=dataTwoFloat.toString().length; 

	} 

	maxLength=Math.max(lengthOne,lengthTwo); 
	for(var i=0;i<maxLength-lengthOne;i++){ 
	dataOneFloat+="0"; 
	} 
	for(var i=0;i<maxLength-lengthTwo;i++){ 
	dataTwoFloat+="0"; 
	} 

	/** 
	*对两个数据进行倍数放大 
	*使其都变为整数。因为整数计算 
	*比较精确。 
	*/ 
	var one=dataOneInt+""+dataOneFloat; 
	var two=dataTwoInt+""+dataTwoFloat; 
	//alert("dataOne:"+dataOne+" dataTwo:"+dataTwo +" one:"+one+" two:"+two); 

	/** 
	*数据扩大倍数后，经计算的到结果， 
	*然后在缩小相同的倍数 
	*进而得到正确的结果 
	*/ 
	var result= (Number(one)-Number(two))/Math.pow(10,maxLength); 

	return result; 

}

//JS实现将数字金额转换为大写人民币汉字的方法
function convertCurrency(money) {
    //汉字的数字
    var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖');
    //基本单位
    var cnIntRadice = new Array('', '拾', '佰', '仟');
    //对应整数部分扩展单位
    var cnIntUnits = new Array('', '万', '亿', '兆');
    //对应小数部分单位
    var cnDecUnits = new Array('角', '分', '毫', '厘');
    //整数金额时后面跟的字符
    var cnInteger = '整';
    //整型完以后的单位
    var cnIntLast = '元';
    //最大处理的数字
    var maxNum = 999999999999999.9999;
    //金额整数部分
    var integerNum;
    //金额小数部分
    var decimalNum;
    //输出的中文金额字符串
    var chineseStr = '';
    //分离金额后用的数组，预定义
    var parts;
    if (money == '') { return ''; }
    money = parseFloat(money);
    if (money >= maxNum) {
        //超出最大处理数字
        return '';
    }
    if (money == 0) {
        chineseStr = cnNums[0] + cnIntLast + cnInteger;
        return chineseStr;
    }
    //转换为字符串
    money = money.toString();
    if (money.indexOf('.') == -1) {
        integerNum = money;
        decimalNum = '';
    } else {
        parts = money.split('.');
        integerNum = parts[0];
        decimalNum = parts[1].substr(0, 4);
    }
    //获取整型部分转换
    if (parseInt(integerNum, 10) > 0) {
        var zeroCount = 0;
        var IntLen = integerNum.length;
        for (var i = 0; i < IntLen; i++) {
            var n = integerNum.substr(i, 1);
            var p = IntLen - i - 1;
            var q = p / 4;
            var m = p % 4;
            if (n == '0') {
                zeroCount++;
            } else {
                if (zeroCount > 0) {
                    chineseStr += cnNums[0];
                }
                //归零
                zeroCount = 0;
                chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
            }
            if (m == 0 && zeroCount < 4) {
                chineseStr += cnIntUnits[q];
            }
        }
        chineseStr += cnIntLast;
    }
    //小数部分
    if (decimalNum != '') {
        var decLen = decimalNum.length;
        for (var i = 0; i < decLen; i++) {
            var n = decimalNum.substr(i, 1);
            if (n != '0') {
                chineseStr += cnNums[Number(n)] + cnDecUnits[i];
            }
        }
    }
    if (chineseStr == '') {
        chineseStr += cnNums[0] + cnIntLast + cnInteger;
    } else if (decimalNum == '') {
        chineseStr += cnInteger;
    }
    return chineseStr;
}

