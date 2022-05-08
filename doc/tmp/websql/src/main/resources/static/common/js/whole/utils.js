/**
 * Created by Administrator on 2017/11/9.
 */
/**
 * 前台js常用函数工具类
 * @author:chenyi
 * @version 1.0
 * @Date: 2017/12/8
 */
(function ($, window) {
    //前台工具类对象
    window.$t = window.$t || {};

    /**
     * 渲染组件
     * @Date: 2018/01/27
     * @param type radio|checkbox|select
     */
    window.$t.render=function (type) {
        type=type?type:"";
        layui.use('form', function () {
            var form = layui.form;
            form.render(type);
        });
    };

    /**
     * 关闭窗口
     * @Date: 2017/12/8
     */
    window.$t.closeWindow=function () {
        var  frameindex= parent.layer.getFrameIndex(window.name);
        parent.layer.close(frameindex);
    };
    /**
     * 关闭窗口并刷新
     * @Date: 2017/12/8
     */
    window.$t.Refresh=function () {
        var  frameindex= parent.layer.getFrameIndex(window.name);
        parent.layer.close(frameindex);
        var parent_iframe=$(parent.document).find("iframe.cy-show ")[0]||$(parent.document).find("iframe")[0];
        $(parent_iframe).contents().find(".search-btn").click();
    };
    /**
     * 获取前端缓存
     * @param key   字典或枚举  code|enum
     * @Date: 2017/12/8
     */
    window.$t.getStorageItem = function (key) {
        return JSON.parse(localStorage.getItem(key));
    };

    /**
     * 设置前端缓存
     * @param key   字典或枚举  code|enum
     * @param data   存储的值（数组）
     * @Date: 2017/12/8
     */
    window.$t.setStorageItem = function (key, data) {
        localStorage.setItem(key, JSON.stringify(data));
    };
    /**
     ** 加法函数，用来得到精确的加法结果
     ** 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
     ** 调用：accAdd(arg1,arg2)
     ** 返回值：arg1加上arg2的精确结果
     **/
    window.$t.accAdd = function (arg1, arg2) {
        var r1, r2, m, c;
        try {
            r1 = arg1.toString().split(".")[1].length;
        }
        catch (e) {
            r1 = 0;
        }
        try {
            r2 = arg2.toString().split(".")[1].length;
        }
        catch (e) {
            r2 = 0;
        }
        c = Math.abs(r1 - r2);
        m = Math.pow(10, Math.max(r1, r2));
        if (c > 0) {
            var cm = Math.pow(10, c);
            if (r1 > r2) {
                arg1 = Number(arg1.toString().replace(".", ""));
                arg2 = Number(arg2.toString().replace(".", "")) * cm;
            } else {
                arg1 = Number(arg1.toString().replace(".", "")) * cm;
                arg2 = Number(arg2.toString().replace(".", ""));
            }
        } else {
            arg1 = Number(arg1.toString().replace(".", ""));
            arg2 = Number(arg2.toString().replace(".", ""));
        }
        return (arg1 + arg2) / m;
    }

    /**
     ** 减法函数，用来得到精确的减法结果
     ** 说明：javascript的减法结果会有误差，在两个浮点数相减的时候会比较明显。这个函数返回较为精确的减法结果。
     ** 调用：accSub(arg1,arg2)
     ** 返回值：arg1减去arg2的精确结果
     **/
    window.$t.accSub = function (arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length;
        }
        catch (e) {
            r1 = 0;
        }
        try {
            r2 = arg2.toString().split(".")[1].length;
        }
        catch (e) {
            r2 = 0;
        }
        m = Math.pow(10, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
        n = (r1 >= r2) ? r1 : r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
    },

        /**
         ** 乘法函数，用来得到精确的乘法结果
         ** 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
         ** 调用：accMul(arg1,arg2)
         ** 返回值：arg1乘以 arg2的精确结果
         **/
        window.$t.accMul = function (arg1, arg2) {
            var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            }
            catch (e) {
            }
            try {
                m += s2.split(".")[1].length;
            }
            catch (e) {
            }
            return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
        },

        /**
         ** 除法函数，用来得到精确的除法结果
         ** 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
         ** 调用：accDiv(arg1,arg2)
         ** 返回值：arg1除以arg2的精确结果
         **/
        window.$t.accDiv = function (arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
            }
            with (Math) {
                r1 = Number(arg1.toString().replace(".", ""));
                r2 = Number(arg2.toString().replace(".", ""));
                return (r1 / r2) * pow(10, t2 - t1);
            }
        },

        /**
         * 获取项目根目录
         */
        window.$t.getContextPath = function () {
            // 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
            var curWwwPath = window.document.location.href;
            // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            // 获取主机地址，如： http://localhost:8083
            var localhostPaht = curWwwPath.substring(0, pos);
            // 获取带"/"的项目名，如：/uimcardprj
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            return (localhostPaht + projectName) + "/";
        };


    window.$t.removeImg=function(){

    };

    //实现工具类
    $.extend(window.$t.utils, {
        /**
         * 验证对象的数据类型
         * @param obj 待验证的对象
         * @returns 验证结果，参考dataType属性
         */
        'type': function (obj) {
            var type = $.type(obj);
            return type.toLowerCase();
        },
        /**
         * 产生一个整型的随机数
         * @param maxNum  产生的最大整数
         * @returns 产生的随机数
         */
        'randomInt': function (maxNum) {
            var num = maxNum && maxNum ? maxNum : 1000000;
            return Math.ceil(Math.random() * num);
        },
        /**
         * 产生一个小数的随机数(0~1)
         * @returns 产生的随机数
         */
        'randomFloat': function () {
            return Math.random();
        },
        /**
         * 获取系统项目根路径
         */
        'getContextPath': function () {
            if ($.fn.contextPath) {
                return $.fn.contextPath;
            }
            // 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
            var curWwwPath = window.document.location.href;
            // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            // 获取主机地址，如： http://localhost:8083
            var localhostPaht = curWwwPath.substring(0, pos);
            // 获取带"/"的项目名，如：/uimcardprj
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            return (localhostPaht + projectName) + "/";
        },

        /**
         * 动态执行js
         * @param str js代码片段
         */
        'execScript': function (str) {
            return eval("(" + str + ")");
        }
    });


})(jQuery, window);