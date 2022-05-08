# magic-script

## 介绍
`magic-script`是一款基于JVM的脚本语言，目前主要是为`magic-api`项目设计。

## 应用案例
- [magic-api，接口快速开发框架，通过Web页面配置，自动映射为HTTP接口](https://gitee.com/ssssssss-team/magic-api)
- [spider-flow，新一代爬虫平台，以图形化方式定义爬虫流程，不写代码即可完成爬虫](https://gitee.com/ssssssss-team/spider-flow)
## 脚本语法

### 关键字
<table>
    <thead>
        <tr>
            <th>关键字</th>
            <th>含义</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>var</td>
            <td>定义变量</td>
        </tr>
        <tr>
            <td>if</td>
            <td>用在条件语句中，表明当条件不成立时的分支</td>
        </tr>
        <tr>
            <td>for</td>
            <td>循环语句</td>
        </tr>
        <tr>
            <td>in</td>
            <td>与 for 配合使用</td>
        </tr>
        <tr>
            <td>continue</td>
            <td>执行下一次循环</td>
        </tr>
        <tr>
            <td>break</td>
            <td>跳出循环</td>
        </tr>
        <tr>
            <td>return</td>
            <td>终止当前过程的执行并正常退出到上一个执行过程中</td>
        </tr>
        <tr>
            <td>exit</td>
            <td>终止当前脚本，并退出返回，如<code>exit 200,'执行成功',[1,2,3];</code>(v0.5.0中新增)</td>
        </tr>
        <tr>
            <td>try</td>
            <td>用于捕获可能发生异常的代码块</td>
        </tr>
        <tr>
            <td>catch</td>
            <td>与 try 关键字配合使用，当发生异常时执行</td>
        </tr>
        <tr>
            <td>finally</td>
            <td>与 try 关键字配合使用，finally 块无论发生异常都会执行</td>
        </tr>
        <tr>
            <td>import</td>
            <td>导入 Java 类或导入已定义好的模块</td>
        </tr>
        <tr>
            <td>as</td>
            <td>与 import 关键字配合使用，用作将导入的 Java类或模块 命名为一个本地变量名</td>
        </tr>
        <tr>
            <td>new</td>
            <td>创建对象</td>
        </tr>
        <tr>
            <td>true</td>
            <td>基础类型之一，表示 Boolean 的：真值</td>
        </tr>
        <tr>
            <td>false</td>
            <td>基础类型之一，表示 Boolean 的：假值</td>
        </tr>
        <tr>
            <td>null</td>
            <td>基础类型之一，表示 NULL 值</td>
        </tr>
        <tr>
            <td>async</td>
            <td>异步调用</td>
        </tr>
    </tbody>
</table>

### 运算符
<table>
    <thead>
        <tr>
            <th colspan="2">数学运算</th>
            <th colspan="2">比较运算</th>
            <th colspan="2">逻辑运算</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>+</td>
            <td>加法</td>
            <td>&lt;</td>
            <td>小于</td>
            <td>&&</td>
            <td>并且</td>
        </tr>
        <tr>
            <td>-</td>
            <td>减法</td>
            <td>&lt;=</td>
            <td>小于等于</td>
            <td>||</td>
            <td>或者</td>
        </tr>
        <tr>
            <td>*</td>
            <td>乘法</td>
            <td>&gt;</td>
            <td>大于</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>/</td>
            <td>除法</td>
            <td>&gt;=</td>
            <td>大于等于</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>%</td>
            <td>取模</td>
            <td>==</td>
            <td>等于</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>++</td>
            <td>自增</td>
            <td>!=</td>
            <td>不等于</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>--</td>
            <td>自减</td>
            <td>===</td>
            <td>等于</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>!==</td>
            <td>不等于</td>
            <td></td>
            <td></td>
        </tr>
    </tbody>
</table>

### 类型
<table>
    <thead>
        <tr>
            <th>类型</th>
            <th>写法</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>byte</td>
            <td>`123b`、`123B`</td>
        </tr>
        <tr>
            <td>short</td>
            <td>`123s`、`123S`</td>
        </tr>
        <tr>
            <td>int</td>
            <td>`123`</td>
        </tr>
        <tr>
            <td>long</td>
            <td>`123l`、`123L`</td>
        </tr>
        <tr>
            <td>float</td>
            <td>`123f`、`123F`</td>
        </tr>
        <tr>
            <td>double</td>
            <td>`123d`、`123D`</td>
        </tr>
        <tr>
            <td>BigDecimal</td>
            <td>`123m`、`123M`</td>
        </tr>
        <tr>
            <td>boolean</td>
            <td>`true`、`false`</td>
        </tr>
        <tr>
            <td>string</td>
            <td>`'hello'`</td>
        </tr>
        <tr>
            <td>string</td>
            <td>`"hello"`</td>
        </tr>
        <tr>
            <td>string</td>
            <td>`"""多行文本块,主要用于编写SQL"""`</td>
        </tr>
        <tr>
            <td>Pattern</td>
            <td><code>/\d+/g`,`/pattern/gimuy</code> 用于定义正则，v0.5.0中新增</td>
        </tr>
        <tr>
            <td>lambda</td>
            <td>`()=>expr`、`(param1,param2....)=>{...}`</td>
        </tr>
        <tr>
            <td>list</td>
            <td>`[1,2,3,4,5]`</td>
        </tr>
        <tr>
            <td>map</td>
            <td>{key : value,key1 : value}</td>
        </tr>
        <tr>
            <td colspan="2">`{[key] : "value"}` `[key]`表示动态从变量中获取key值</td>
        </tr>
    </tbody>
</table>

### 一元运算符

您可以通过一元运算`-`符将数字取反，例如`-234`。要取反布尔表达式，可以使用`!`运算符，例如`!true`。
自增/自减 `i++` 、 `++i`、`i--`、`--i`

### 算术运算符

支持常见的算术运算符，例如`1 + 2 * 3 / 4 % 2`、同样也支持`+=`、`-=`、`*=`、`/=`、`%=`

### 比较运算符

`23 < 34`，`23 <= 34`，`23 > 34`，`23 >= 34`，`true != false`，`23 == 34`

比较运算符结果为`boolean`类型

### 逻辑运算符

除了一元运算`!`符，您还可以使用`&&`和`||`。就像Java中一样，运算符也是一种短路运算符。如果`&&`左边计算为`false`，则不会计算右边。如果`||`左侧为true，则不会计算右边
在1.3.0+版本中增强了`&&` `||` 不在强制两边必须是布尔类型。作用与`JS`一样

### 三元运算符

三元运算符是`if`语句的简写形式，其工作方式类似于Java中，例如`true ? "yes" : "no"`
在1.2.7+版本中，增强了`if` 和三元运算符，不在强制值必须是布尔类型，可以写`if(xxx)`的形式当`xxx`为以下情况时为`fasle`、其它情况为`true`
- `null`
- 空集合
- 空Map
- 空数组
- 数值==0
- 非空字符串
- `false`

### 类型转换

可使用`::type(defaultValue)` 的方式进行类型转换，如
```javascript
var a = "123"::int; // 123
var b = "abc"::int(111); // 111
var c = "2020-01-01"::date('yyyy-MM-dd'); // 转换为date
```

### 可选链操作符
可选链操作符(`?.`)允许读取位于连接对象链深处的属性的值，而不必明确验证链中的每个引用是否有效。`?.`操作符的功能类似于`.`链式操作符，不同之处在于，在引用为空 的情况下不会引起错误，该表达式短路返回值是 `null`。

当尝试访问可能不存在的对象属性时，可选链操作符将会使表达式更短、更简明。在探索一个对象的内容时，如果不能确定哪些属性必定存在，可选链操作符也是很有帮助的。
```javascript
obj?.prop
obj?.method(args)
```

示例：
```javascript
var a = null;
var b = a?.name;    // b = null;
var c = a?.getName();   // c = null;
```

### 扩展运算符

扩展运算符，又叫展开语法(Spread syntax)， 是用于将list或map在语法层面展开；

语法:
> lambda 调用
```javascript
var sum = (a,b,c) => a + b + c;
System.out.println(sum(...[1,2,3]))
/*
结果：6
*/
```
> list 展开
```javascript
var arr = [3,4,5];
System.out.println([1,2,...arr,6,7])
/*
结果：[1, 2, 3, 4, 5, 6, 7]
*/
```
> list 展开到 map 中
```javascript
var arr = [3,4,5];
System.out.println({key1:1,...arr})
/*
结果：{key1=1, 0=3, 1=4, 2=5}

虽然这些key看起来像数值，但其实是String类型的key，如果把它们转为JSON看起来是这样的：

{"key1":1, "0":3, "1":4, "2":5}

*/
```

> map 展开
```javascript
var map = {key2:2}
System.out.println({key1:1,...map,key3:3})
/*
结果：{key1=1, key2=2, key3=3}
*/
```

### for循环

当前for循环只支持两种，循环集合或Map

#### 循环集合
```javascript
import 'java.lang.System' as System;
var list = [1,2,3];
for(index,item in list){    //如果不需要index，也可以写成for(item in list)
    System.out.println(index + ":" + item);
}
/*
结果：
0:1
1:2
2:3
*/
```

#### 循环指定次数
```javascript
var sum = 0;
for(value in range(0,100)){    //包括0包括100
    sum = sum + value; //不支持+= -= *= /= ++ -- 这种运算
}
return sum;
/*
结果：5050
*/
```

#### 循环map
```javascript
import 'java.lang.System' as System;
var map = {
    key1 : 123,
    key2 : 456
};
for(key,value in map){    //如果不需要key，也可以写成for(value in map)
    System.out.println(key + ":" + value);
}
/*
结果：
key1:123
key2:456
*/
```

### Import导入

#### 导入Java类
```javascript
import 'java.lang.System' as system;//导入静态类并赋值给system作为变量
import 'javax.sql.DataSource' as ds;//从spring中获取DataSource并将值赋值给ds作为变量
import 'org.apache.commons.lang3.StringUtils' as string;//导入静态类并赋值给ds作为变量

System.out.println('调用System打印');//调用静态方法
System.out.println(ds);
System.out.println(string.isBlank('')); //调用静态方法
```

### new创建对象

#### 创建对象
```javascript
import 'java.util.Date' as Date;//创建之前先导包，不支持.*的操作
return new Date();
```

#### 导入已定义的模块
```javascript
import log; //导入log模块，并定义一个与模块名相同的变量名
//import log as logger; //导入log模块，并赋值给变量 logger
log.info('Hello {}','Magic API!')
```

### 异步调用

#### 异步调用方法
```javascript
var val = async db.select('.....'); // 异步调用，返回Future类型
return val.get();   //调用Future的get方法
```

#### 异步调用lambda
```javascript
var list = [];
for(index in range(1,10)){
    list.add(async (index)=>db.selectInt('select #{index}'));
}
return list.map(item=>item.get());  // 循环获取结果
```