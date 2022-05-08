/**
 * Created by 陈熠 on 2017/6/21
 * email   :  228112142@qq.com
 */
(function ($) {
    /* 入口函数 */
    $.fn.pageGrid = function () {
        //当前表格对象
        var $grid = this;
        //获取表格参数
        pageProps = $grid.attr("cyProps");
        if (!pageProps) {
            return
        }
        pageProps = pageProps ? pageProps : "";
        //将表格参数转为json
        pageProps = eval("({" + pageProps + "})");
        //表格渲染前需要执行的方法
        var beforeRender=pageProps.beforeRender||"";
        if(beforeRender){
            var beforeRenders=beforeRender.split(",");
            $.each(beforeRenders,function (index,item) {
                var _func=eval((item));
                _func();
            })
        }
        //设置每页显示条数
        defaultParam.limit = pageProps.pageSize || 10;
        //从后台获取数据
        var R = PageGrid.getData(pageProps.url);
        //渲染数据,设置分页
        PageGrid.setPage(R, $grid, pageProps.url);
        //设置查询表单
        PageGrid.searchForm();
        //排序
        PageGrid.sort();
    };

    /*分页默认参数*/
    var defaultParam = {
        search: false,
        nd: new Date().getTime(),
        limit: 10,
        page: 1,
        sidx: '',
        order: 'desc',
        init:true,
        _: new Date().getTime()
    };
    /*默认配置*/
    var pageProps = {};
    /*方法对象*/
    var PageGrid = {
        /**获取数据 by chenyi 2017/6/21*/
        getData: function (url) {
            var data;
            $.ajax({
                url: url,
                async: false,
                data: defaultParam,
                dataType: "json",
                success: function (R) {
                    if (R.code == 0) {
                        data = R;
                    } else {
                        data = {page: null};
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
        /**设置分页 by chenyi 2017/6/21*/
        setPage: function (R, $grid, url) {

            var pageId = $grid.attr("id") + "_page";
            $("#" + pageId).remove();
            //创建分页div
            $grid.after('<div id="' + pageId + '"></div>');
            layui.use(['laypage', 'layer'], function () {
                var laypage = layui.laypage;
                laypage.render({
                    elem: pageId
                    , count: R.page ? R.page.totalCount : 0
                    , layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                    , jump: function (obj) {
                        var index=Loading.open(1,false);
                        PageGrid.toPage(R,obj, $grid, url);
                        Loading.close(index);
                    }
                });
            });

        },
        /**渲染表格数据 by chenyi 2017/6/21*/
        renderData: function (R, $grid, pageProps) {
            //获取表格参数中的name
            var _grid = $grid;
            //获取所有th
            var _th = _grid.find("thead th");

            _grid.find("tbody").remove();
            //创建tbody
            _grid.append("<tbody><tbody/>");

            for (var i = 0; i < _th.length; i++) {
                var _param=eval("(" + $(_th[i]).attr("param") + ")");
                //判断是否有隐藏的列
                var isHide = _param.hide || "false";
                if (isHide == "true") {
                    $(_th[i]).hide();
                }

                //是否开启排序功能
                var sort=_param.sort||"false";
                var _name=_param.name;
                var thHtml=$(_th[i]).text();
                if(sort=="true"){
                    var _order=defaultParam.sidx===_name?defaultParam.order:"desc";
                    $(_th[i]).html([thHtml,'<i class="fa fa-sort-'+_order+'"  style="position: absolute;right: 5px;"></i>'].join(""));
                    $(_th[i]).css("cursor","pointer");
                }
            }

            //删除多余的tbody
            if (_grid.find("tbody").length > 1) {
                for (var i = 1; i < _grid.find("tbody").length; i++) {
                    $(_grid.find("tbody")[i]).remove();
                }

            }
            //获取将要渲染的数据
            var data = R.page ? R.page.list : [];

            var _th_length = _grid.find("thead th:visible").length || 0;
            if (data.length == 0) {
                _grid.find("tbody").append('<tr><td style="text-align: center" colspan="' + _th_length + '">暂无数据</td></tr>')
            }
            var primary = "";
            var attr = new Array();

            for (var i = 0; i < data.length; i++) {
                //原始数据
                var rowdata=$.extend(true,[], data[i]);
                //为数据创建tr
                $(_grid.find("tbody")).append("<tr></tr>");
                //获取新建的tr
                var _tr = _grid.find("tbody tr")[_grid.find("tbody tr").length - 1];
                //循环所有th  获取param中的name
                for (var j = 0; j < _th.length; j++) {
                    //获取当前th的所有参数
                    var th_param = $(_th[j]).attr("param");
                    var params = eval("(" + th_param + ")");
                    //获取param属性中的name
                    var _name = params.name || "";
                    var render = params.render;
                    var enumName = params.enumName;
                    var codeName = params.codeName;
                    var sortBtn = params.sortBtn||"false";
                    //是否是操作列
                    var operate = params.operate || "false";
                    var hide = params.hide || "false";
                    //该列是否是主键
                    var isPrimary = params.isPrimary || "false";
                    //该列的属性 放入到复选框中
                    var attrName = params.attr || "";
                    if (operate == "true") {


                        //获取操作列需要渲染的按钮
                        var buttons = params.buttons || "";
                        //获取按钮数据
                        var btns = buttons.split(",");
                        var tdContent = "<span></span>"
                        for (var k = 0; k < btns.length; k++) {
                            var func = eval((btns[k]));
                            tdContent = tdContent.replace("</span>", func(rowdata,data[i]) + "</span>");
                        }
                        if (hide == "true") {
                            $(_tr).append('<td style="display: none">' + tdContent + '</td>');
                        } else {
                            $(_tr).append('<td >' + tdContent + '</td>');
                        }
                    } else {
                        for (var key in data[i]) {
                            if (key == _name) {

                                //如果是枚举
                                if (enumName != null && enumName != undefined && enumName != "") {
                                    var enumValues = PageGrid.getDataByEnum(enumName).data || "";
                                    for (var _enum in enumValues) {
                                        if (enumValues[_enum].code == data[i][key]) {
                                            data[i][key] = enumValues[_enum].value
                                        }
                                    }
                                }

                                //如果是表码

                                if (codeName != null && codeName != undefined && codeName != "") {
                                  //  var codeValues = PageGrid.getDataByCode(codeName).data || "";
                                    /*if(codeName=="state"){
                                        codeValues[_code].value="123";
                                    }*/
                                   /* for (var _code in codeValues) {
                                        if (codeValues[_code].code == data[i][key]) {
                                            data[i][key] = codeValues[_code].value
                                        }
                                    }*/
                                }
                                //如果有渲染的方法 先进去渲染方法
                                if (render != null && render != undefined && render != "") {

                                    var func = eval((render));
                                    data[i][key] = func(rowdata,data[i], i, data[i][key]);
                                }


                                var value = data[i][key] || "";
                                //如果是主键并且主键不为空，设置主键值
                                if (isPrimary == "true") {
                                    primary = value;
                                }
                                if (attrName != "") {

                                    var attrObj = {attrName: attrName, attrValue: value};
                                    attr.push(attrObj);
                                }

                                //如果是排序操作列
                                if(sortBtn == "true"){
                                    $(_tr).append('<td class="sort-btn">' +
                                        '<i class="fa fa-angle-double-up" onclick="Render.moveTop('+primary+','+defaultParam.limit+')"/>' +
                                        '<i class="fa fa-angle-double-down" onclick="Render.moveBottom('+primary+','+defaultParam.limit+')"/>' +
                                        '<i class="fa fa-angle-up" onclick="Render.moveTop('+primary+',1)"/>' +
                                        '<i class="fa fa-angle-down" onclick="Render.moveBottom('+primary+',1)"/></td>');
                                }else{
                                    //如果是隐藏列
                                    if (hide == "true") {
                                        $(_tr).append('<td style="display: none" name=' + _name + '>' + value + '</td>');
                                    } else {
                                        $(_tr).append('<td name=' + _name + '>' + value + '</td>');
                                    }


                                }

                            }
                        }

                    }

                }
                //判断是否有复选框
                var isCheckbox = pageProps.checkbox || "true";
                //如果需要复选框
                if (isCheckbox == "true") {
                    //获取监控标识
                    var filter = pageProps.filter || "choose";
                    var checkboxTd = '<td><input type="checkbox"   primary="' + primary + '" lay-skin="primary"></td>';
                    //复选框监听标识
                    if (filter != "") {
                        checkboxTd = checkboxTd.replace('<td><input type="checkbox"', '<td><input type="checkbox" lay-filter="' + filter + '"');
                    }
                    if (attr) {
                        for (var p = 0; p < attr.length; p++) {
                            checkboxTd = checkboxTd.replace('<td><input type="checkbox"', '<td><input type="checkbox" ' + attr[p].attrName + '="' + attr[p].attrValue + '"');
                        }
                    }
                    //添加全选复选框
                    $(_tr).prepend(checkboxTd);

                }
                PageGrid.renderCheckbox();

            }


            //表格渲染后需要执行的方法
            var afterRender=pageProps.afterRender||"";
            if(afterRender){
                var afterRenders=afterRender.split(",");
                $.each(afterRenders,function (index,item) {
                    var _func=eval((item));
                    _func();
                })
            }

        },
        /**跳到分页页面 by chenyi 2017/6/22*/
        toPage: function (R,obj, $grid, url) {
            defaultParam.page = obj.curr;
            /**增加每页显示条数 by chenyi 2017/08/21*/
            defaultParam.limit = obj.limit;
            /**解决列表页两次请求后台问题 by chenyi 2018/01/02*/
            //是否是首次加载列表
            if(!defaultParam.init){
                R = PageGrid.getData(url);
            }
            defaultParam.init=false;
            //重置全选按钮
            var allChoose=$grid.find("[lay-filter='allChoose']");
            allChoose.each(function (index, item) {
                item.checked = false;
            });
            layui.use('form', function () {
                var form = layui.form;
                form.render('checkbox');
            });
            //渲染表格数据
            PageGrid.renderData(R, $grid, pageProps);
        },
        /**查询表格 by chenyi 2017/6/23*/
        searchForm: function () {
            layui.use(['form'], function () {
                var form = layui.form;
                //监听提交
                form.on('submit(search)', function (data) {
                    //获取对应的表格对象
                    var table_id = $(this).attr("table-id");
                    var _table = $("#" + table_id);
                    //获取表格参数
                    var props = _table.attr("cyProps");
                    if (!props) {
                        return
                    }
                    props = props ? props : "";
                    //将表格参数转为json
                    props = eval("({" + props + "})");
                    var conditions = data.field;
                    $.extend(defaultParam, conditions);
                    var R = PageGrid.getData(props.url);
                    defaultParam.init=true;
                    PageGrid.setPage(R, _table, props.url);
                    return false;
                });

            });
        },
        renderCheckbox: function () {
            layui.use('form', function () {
                var $ = layui.jquery, form = layui.form;
                form.render('checkbox');
                //全选
                form.on('checkbox(allChoose)', function (data) {
                    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
                    child.each(function (index, item) {
                        item.checked = data.elem.checked;
                    });
                    form.render('checkbox');

                    //复选框点击需要执行的方法
                    var onCheck=pageProps.onCheck||"";
                    if(onCheck){
                        var onChecks=onCheck.split(",");
                        $.each(onChecks,function (index,item) {
                            var _func=eval((item));
                            _func(data);
                        })
                    }
                });

                var filter = pageProps.filter || "choose";
                form.on('checkbox('+filter+')', function (data) {
                    var child = $(data.elem).parents('table').find('[type="checkbox"]');
                    var allChoose=$(data.elem).parents('table').find("[lay-filter='allChoose']");
                    var flag=true;
                    child.each(function (index, item) {
                        if(index>0&&item.checked == false){
                            allChoose.each(function (index2, item2) {
                                item2.checked = false;
                            });
                            flag=false;
                        }

                    });

                    if(flag){
                        allChoose.each(function (index, item) {
                            item.checked = true;
                        });
                    }
                    form.render('checkbox');

                    //复选框点击需要执行的方法
                    var onCheck=pageProps.onCheck||"";
                    if(onCheck){
                        var onChecks=onCheck.split(",");
                        $.each(onChecks,function (index,item) {
                            var _func=eval((item));
                            _func(data);
                        })
                    }
                });

            });
        }
        /**表格排序 by chenyi 2018/01/03*/
        ,sort:function () {
            $("[cyType='pageGrid']").on("click","th",function () {
                var $grid=$(this).parents("table");
                var _param=eval("(" + $(this).attr("param") + ")");

                if($(this).find("i").hasClass("fa-sort-asc")){
                    $(this).find("i").attr("class","fa fa-sort-desc");
                    defaultParam.sidx=_param.name;
                    defaultParam.order="desc";
                    var R = PageGrid.getData(pageProps.url);
                    PageGrid.renderData(R, $grid, pageProps);
                    return;
                }
                if($(this).find("i").hasClass("fa-sort-desc")){
                    $(this).find("i").attr("class","fa fa-sort-asc");
                    defaultParam.sidx=_param.name;
                    defaultParam.order="asc";
                    var R = PageGrid.getData(pageProps.url);
                    PageGrid.renderData(R, $grid, pageProps);
                    return;
                }

            });

        }
    }

})(jQuery);
$(document).ready(function () {
    //表格渲染查询
    var tables = $("[cyType='pageGrid']");
    for (var i = 0; i < tables.length; i++) {
        $(tables[i]).pageGrid();
    }


});