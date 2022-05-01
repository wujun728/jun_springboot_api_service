/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 * 弹出层封装
 */
(function ($, window) {
    //弹出层对象
    window.Alert = window.Alert || {};
    window.Msg = window.Msg || {};
    window.Tips = window.Tips || {};
    window.Loading = window.Loading || {};
    window.Page = window.Page || {};
    /**@author chenyi
     * @description 重写alert
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Alert.alert = function (msg) {
            parent.layer.alert(msg, function(index){
                parent.layer.close(index);
                if(typeof(callback) === "function"){
                    callback(index);
                }
            });

    };

    /**@author chenyi
     * @description 重写confirm
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Alert.confirm = function (msg,callback) {
        parent.layer.confirm(msg, {
            btn: ['确定','取消'] //按钮
        }, function(){
            if(typeof(callback) === "function"){
                 callback();
            }
        }, function(){
        });

    };

    /**@author chenyi
     * @description 重写prompt
     * @param msg   msg提示
     * @param type   类型 1文本框 2文本域 3文本域+文件上传
     * @Date: 2017/12/8
     */
    window.Alert.prompt = function (msg,type,callback) {
        parent.layer.prompt({title:msg, formType: type}, function(pass, index){
            if(typeof(callback) === "function"){
                callback(pass);
            }
            parent.layer.close(index);

        });

    };

    /**@author chenyi
     * @description 短消息
     * @param msg   msg提示
     * @param type   类型 默认灰色 1绿色√ 2红色x 3黄色？  4灰色锁  5红色委屈脸  6绿色开心脸  7黄色！
     * @Date: 2017/12/8
     */
    window.Msg.msg = function (msg,type) {
        parent.layer.msg(msg, {icon: type});

    };

    /**@author chenyi
     * @description 提示 (灰色)
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Msg.info = function (msg) {
        parent.layer.msg(msg);
    };

    /**@author chenyi
     * @description 操作成功 提示
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Msg.success = function (msg) {
        msg=msg?msg:"操作成功";
        parent.layer.msg(msg, {icon: 1});
    };

    /**@author chenyi
     * @description 警告提示 (感叹号)
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Msg.warn = function (msg) {
        msg=msg?msg:"操作失败";
        parent.layer.msg(msg, {icon: 7});
    };

    /**@author chenyi
     * @description 错误提示 (红叉)
     * @param msg   msg提示
     * @Date: 2017/12/8
     */
    window.Msg.error = function (msg) {
        msg=msg?msg:"操作失败";
        parent.layer.msg(msg, {icon: 2});
    };



    /**@author chenyi
     * @description tips吸附
     * @param msg   msg提示
     * @param obj   要吸附的元素对象
     * @param position   显示位置   1上 2右 3下 4左
     * @param color   tip颜色
     * @Date: 2017/12/8
     */
    window.Tips.tips = function (msg,obj,position,color) {
        position=position?position:2;
        color=color?color:"rgb(41, 145, 217)";
        layer.tips(msg,obj, {tips: [position, color]});
    };


    /**@author chenyi
     * @description tips吸附-提示
     * @param msg   msg提示
     * @param obj   要吸附的元素对象
     * @Date: 2017/12/8
     */
    window.Tips.info = function (msg,obj) {
        layer.tips(msg,obj);
    };
    /**@author chenyi
     * @description tips吸附-成功
     * @param msg   msg提示
     * @param obj   要吸附的元素对象
     * @Date: 2017/12/8
     */
    window.Tips.success = function (msg,obj) {
        layer.tips(msg,obj, {tips: [2, "#009688"]});
    };

    /**@author chenyi
     * @description tips吸附-成功
     * @param msg   msg提示
     * @param obj   要吸附的元素对象
     * @Date: 2017/12/8
     */
    window.Tips.error = function (msg,obj) {
        layer.tips(msg,obj, {tips: [2, "red"]});
    };

    /**@author chenyi
     * @description 加载层
     * @param type   type 加载的风格，支持0-2
     * @param shade   是否打开遮罩
     * @Date: 2017/12/8
     */
    window.Loading.open = function (type,shade) {
        var index;
        if(shade){
            index = parent.layer.load(type, {
                shade: [0.3, '#000'], //0.1透明度的白色背景
            });
        }else{
            index = parent.layer.load(type, {shade: false});
        }
        return index;
    };

    /**@author chenyi
     * @description 自定义消息加载层
     * @param msg   提示信息
     * @Date: 2017/12/8
     */
    window.Loading.msg = function (msg) {
        var index;
       index=parent.layer.msg(msg, {
            icon: 16
            ,shade: 0.01
        });
       return index;
    };
    /**@author chenyi
     * @description 关闭加载层
     * @param  index 加载层标识
     * @Date: 2017/12/8
     */
    window.Loading.close = function (index) {
       parent.layer.close(index);
    };
    window.Loading.closeAll = function () {
        parent.layer.closeAll();
    };

    /**@author chenyi
     * @description 打开iframe窗口
     * @param  title 标题
     * @param url 地址或页面元素
     * @Date: 2017/12/8
     */
    window.Page.open = function (title,url,id) {
        parent.layer.open({
            id:id,
            type: 2,
            title: title,
            shadeClose: false,
            shade: [0.3, '#000'],
            maxmin: true, //开启最大化最小化按钮
            area: ['80%', '80%'],
            content: url
        });
    };

    /**@author chenyi
     * @description 打开即最大化
     * @param  title 标题
     * @param url 地址
     * @Date: 2017/12/8
     */
    window.Page.max = function (title,url,id) {
       var index =parent.layer.open({
            id:id,
            type: 2,
            title: title,
            shadeClose: false,
            shade: [0.3, '#000'],
            maxmin: true, //开启最大化最小化按钮
            area: ['1000px', '700px'],
            content: url
        });
       parent.layer.full(index);
    };

    /**@author chenyi
     * @description 获取某个弹窗页面中的某个元素
     * @param  id 窗口id
     * @param element 元素节点
     * @Date: 2017/12/8
     */
    window.Page.getElement=function(id,element){
        var iframe=$(parent.document).find(id).find("iframe");
        if(iframe){
            return  $($($(parent.document).find(id).find("iframe")[0]).contents()).find(element);
        }
        else{
            return  $(parent.document).find(id).find(element);
        }
    }


})(jQuery, window);



