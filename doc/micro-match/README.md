# micro-match
撮合系统

表结构在create.sql中

撮合平台：
1，	实现买单向卖单或卖单向买的双向撮合。
2，	支持并发撮合。
3，	撮合规则可以自定义添加。


撮合规则管理：
内置5个撮合规则，
金额范围规则（match_rule_amount）

撮合条数规则（match_rule_matchnum）

产品类别对应规则（match_rule_product）

利率范围规则（match_rule_rate）

时间范围规则（match_rule_time）

可以自行开发其他规则如借款期限匹配规则、利率范围规则等等。
 


通过撮合规则列表页面设置规则实例id和参数。在撮合时可以指定使用哪几个规则实例。
金额范围规则（match_rule_amount）参数是最小金额min和最大金额max
例如{"min":100,"max":1000}
撮合条数规则（match_rule_matchnum）参数是最小条数min和最大条数max
例如{"min":1,"max":3}
产品类别对应规则（match_rule_product）参数是list形式pipeiList，每行参数为买单产品类别buyProduct、买单平台类别buyPlatform、卖单产品类别saleProduct、卖单平台类别salePlatform
例如
{"pipeiList":[{"buyProduct":"jingying","buyPlatform":"p2p","saleProduct":"jingying","salePlatform":"p2p"}]}

利率范围规则（match_rule_rate）参数是最小浮动利率min可以填写负数和最大浮动利率max
例如{"min":-1.0,"max":1.0}

时间范围规则（match_rule_time）参数是最小浮动天数min可以填写负数和最大浮动天数max
例如{"min":-1,"max":1}

挂单管理
可以通过买单列表和卖单列表页面或接口，实现挂单。
 

撮合
在卖单列表中选中某条记录点击撮合实现向买单撮合，在买单列表中选中某条记录点击撮合实现向卖单撮合。点击撮合按钮时弹出输入框，输入规则实例id，多个用逗号分隔，不输默认使用所有的已配置规则实例进行撮合。

撮合结果管理
通过撮合结果列表页面或接口查询撮合结果。
 


用户管理
使用admin登录后（默认密码admin）可看到用户列表页面，可以添加或删除用户。
 

**平台接口说明**

**挂买单接口：**

功能：创建买单记录

地址:/api/createBuyOrder

输入参数:

user_name 出借人用户名

lender_rate  出借收益率

account_amount 待撮合金额

product_class 产品类型

platform_class 平台类型

lender_start_date 出借开始日期（格式必须为yyyy-MM-dd hh:mm:ss）

input_amount 挂单金额

match_priority 撮合优先级

lender_no 出借合同号

user_id 出借人编号

输出参数：

Json格式输出

status 结果状态(0正常)

code 结果编号(success\error)

orderId 挂单编号


**挂卖单接口：**

功能：创建卖单记录

地址:/api/createSaleOrder

输入参数:

user_name 借款人用户名

borrow_rate  借款收益率

account_amount 待撮合金额

product_class 产品类型

platform_class 平台类型

borrow_start_date 借款开始日期（格式必须为yyyy-MM-dd hh:mm:ss）

input_amount 挂单金额

match_priority 撮合优先级

borrow_no 借款合同号

user_id 出借人编号

输出参数：

Json格式输出

status 结果状态(0正常)

code 结果编号(success\error)

orderId 挂单编号


**撮合接口：**

功能：根据某买单向卖单列表撮合，或根据卖单向买单列表撮合。

地址:/api/execMatch

输入参数:

rules 撮合规则编号，多个用逗号分隔，all表示全部

recordId  某挂单id

dirFlag 撮合方向(买单向卖单撮合buy2sale  卖单向买单撮合sale2buy)


输出参数：

Json格式输出

status 结果状态(0正常)

code 结果编号(success\error)

matchId 撮合编号


**撮合结果查询接口：**

功能：根据撮合id或挂单id查询撮合结果。

地址:/api/queryMatchResult

输入参数:

match_id 撮合id(非必填)

buy_id  买单id(非必填)

sale_id 卖单id(非必填)

输出参数：

Json格式输出

status 结果状态(0正常)

code 结果编号(success\error)

data 撮合结果列表