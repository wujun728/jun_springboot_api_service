/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 */
(function ($) {
    /* 入口函数 */
    $.fn.selectTool = function () {
        //当前表格对象
        var $grid = this;
        //获取表格参数
        cyProps = $grid.attr("cyProps");
        if (!cyProps) {
            return
        }
        cyProps = cyProps ? cyProps : "";
        //将表格参数转为json
        cyProps = eval("({" + cyProps + "})");
        //获取数据的地址，只能通过表码或url，如果两个都写，默认是url
        //从表码获取数据
        var codeName = cyProps.codeName;
        //从后台获取数据
        var url = cyProps.url;
        //从枚举获取数据
        var enumName = cyProps.enumName;
        var R = "";
        //如果是通过表码取值
        if (codeName != undefined && codeName != "") {
            R = selectTool.getDataByCode(cyProps.codeName);
        }
        //如果是从后台获取数据
        if (url != undefined && url != "") {
            R = selectTool.getDataByUrl(cyProps.url);
        }
        //如果是从枚举获取数据
        if (enumName != undefined && url != "") {
            R = selectTool.getDataByEnum(cyProps.enumName);
        }
        selectTool.renderData(R, $grid, cyProps);
    };
    /*默认配置*/
    var cyProps = {};
    /*方法对象*/
    var selectTool = {
        /**获取数据 by chenyi 2017/6/21*/
        getDataByUrl: function (url) {
            var data;
            $.ajax({
                url: url,
                async: false,
                type: 'post',
                dataType: "json",
                success: function (R) {
                    if (R.code == 0) {
                        data = R;
                    } else {
                        data = {};
                        alert(R.msg);
                    }
                }
            });
            return data;
        },
        /**获取数据 by chenyi 2017/7/5*/
        getDataByCode: function (codeName) {
            /**localStorage是否已存在该数据*/
            var data = $t.getStorageItem(codeName);
            if (!data) {
                $.ajax({
                    url: $s.getDataByCode,
                    async: false,
                    data: {codeName: codeName},
                    type: 'post',
                    dataType: "json",
                    success: function (R) {
                        if (R.code == 0) {
                            data = R;
                            /**设置localStorage缓存*/
                            $t.setStorageItem(codeName, data);
                        } else {
                            data = {};
                            alert(R.msg);
                        }
                    }
                });

            }

            return data;
        },
        /**获取数据 by chenyi 2017/7/19*/
        getDataByEnum: function (enumName) {
            /**localStorage是否已存在该数据*/
            var data = $t.getStorageItem(enumName);
            if (!data) {
                $.ajax({
                    url: $s.getDataByEnum,
                    async: false,
                    type: 'post',
                    data: {enumName: enumName},
                    dataType: "json",
                    success: function (R) {
                        if (R.code == 0) {
                            data = R;
                            /**设置localStorage缓存*/
                            $t.setStorageItem(enumName, data);
                        } else {
                            data = {};
                            alert(R.msg);
                        }
                    }
                });
            }
            return data;
        },
        /**渲染下拉框数据 by chenyi 2017/6/21*/
        renderData: function (R, $grid, cyProps) {
            var _grid = $grid;
            //获取下拉控件的name
            var _name = $(_grid).attr("name");
            //获取下拉控件的默认值
            var _value = $(_grid).attr("value");
            //获取需要验证的参数
            var _verify = $(_grid).attr("lay-verify") || "";

            $(_grid).removeAttr("lay-verify");
            $(_grid).removeAttr("name");
            $(_grid).removeAttr("value");
            //是否是下拉多选
            var _multiple=cyProps.multiple||"false";
            //获取是否有提示
            var _selectTip = cyProps.tips || "请选择";
            //获取监控标识
            var filter = cyProps.filter || "";
            //搜索功能参数
            var _search = cyProps.search || "true";
            //获取下拉框禁用的值
            var _disabled = cyProps.disabled || "";
            var _disableds = _disabled.split(",");
            var data = R.data;
            var _select = '<select name="' + _name + '" ></select>';
            //是否hi多选
            if (_multiple == "true") {
                _select = _select.replace('<select', '<select  multiple value="'+_value+'"');

            }
            //是否开启搜索功能
            if (_search == "true") {
                _select = _select.replace("<select", "<select  lay-search");

            }
            //添加监控标识
            if (filter != "") {
                _select = _select.replace("<select", "<select lay-filter=" + filter);
            }
            //验证值
            if (_verify != undefined && _verify != "") {
                _select = _select.replace("<select", "<select  lay-verify='" + _verify + "'");
            }
            $(_grid).append(_select);

            if(_selectTip!="false"){
                $(_grid).find("select").append('<option value="">' + _selectTip + '</option>');
            }
            if (data != undefined) {
                for (var i = 0; i < data.length; i++) {
                    var _option = '<option value="' + data[i].CODE + '">' + data[i].VALUE + '</option>';
                    if(_multiple == "false"){
                        //设置默认值
                        if (_value == data[i].code) {
                            _option = _option.replace("<option", "<option selected ")
                        }
                    }
                    if(_multiple == "true"){
                        var _values=_value.split(",");
                        for(var z=0;z<_values.length;z++){
                            //设置默认值
                            if (_values[z] == data[i].code) {
                                _option = _option.replace("<option", "<option selected ")
                            }
                        }

                    }
                    //设置禁用
                    if (_disableds.indexOf(data[i].code) != -1) {
                        _option = _option.replace("<option", "<option disabled ")
                    }
                    $(_grid).find("select").append(_option);
                }

            }
            //渲染下拉框
            layui.use(['form'], function () {
                var form = layui.form;
                //监听提交
                form.on('select()', function (data) {
                    return false;
                });
                //下拉框监听事件
                form.on('select(area)', function (data) {

                    //当前元素的父节点
                    var _grid = $(data.othis).parent();
                    //删除当前点击的下级地区dom
                    $(_grid).nextAll(".layui-input-inline").remove();
                    //当前点击的id
                    var parentId = data.value;
                    if (parentId != "") {
                        //获取所有下级地区
                        var R = selectTool.getDataByUrl("/area/normalList/" + parentId);

                        var areaData = R.data;
                        if (areaData.length > 0) {
                            var _div = '<div id="div_' + data.value + '" class="layui-input-inline" ></div>';
                            $(_grid).after(_div);
                            var _select = '<select id="select_' + data.value + '" name="parentAreaIds[]" lay-search lay-filter="area" ></select>';
                            $("#div_" + data.value).append(_select);
                            $("#select_" + data.value).append('<option value="">请选择</option>');
                            for (var i = 0; i < areaData.length; i++) {
                                var _option = '<option value="' + areaData[i].code + '">' + areaData[i].value + '</option>';
                                $("#select_" + data.value).append(_option);
                            }
                        }

                        form.render('select');
                    }
                    return false;

                });
                form.render('select');

            });


        }
    }

})(jQuery);
$(document).ready(function () {
    //下拉树查询
    var selects = $("[cyType='selectTool']");
    for (var i = 0; i < selects.length; i++) {
        $(selects[i]).selectTool();
    }
});