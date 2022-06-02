<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商铺管理</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <!-- jqgrid-->
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/jqgrid/ui.jqgrid.css?0820" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
    <!-- 全局js -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
    <!-- 遮罩层 -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/layer/layer.min.js"></script>
    <!-- jqGrid -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/i18n/grid.locale-cn.js?0820"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/jqgrid/jquery.jqGrid.min.js?0820"></script>
    <!-- Sweet alert -->
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
    <!-- jQuery Validation plugin javascript-->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/additional-methods.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/validate/messages_zh.min.js"></script>
    <style>

    </style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                <div class="ibox-content">
                    <h4 class="m-t">商铺管理</h4>
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="row">
                                <form class="form-horizontal m-t">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">商铺名称：</label>
                                        <div class="col-sm-2">
                                            <input type="text" class="form-control" id="s_name" >
                                        </div>
                                        <label class="col-sm-2 control-label">商铺编号：</label>
                                        <div class="col-sm-2">
                                            <input type="text" class="form-control" id="s_code">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">商铺地址：</label>
                                        <div class="col-sm-3">
                                            <select id="s_province" name="province" class="form-control" type="text"
                                                    aria-required="true" aria-invalid="true" class="error">
                                                <option value="">请选择</option>
                                            </select>
                                        </div>
                                        <div class="col-sm-3">
                                            <select id="s_city" name="city" class="form-control" type="text"
                                                    aria-required="true" aria-invalid="true" class="error">
                                                <option value="">请选择</option>
                                            </select>
                                        </div>
                                        <div class="col-sm-3">
                                            <select id="s_county" name="county" class="form-control" type="text"
                                                    aria-required="true" aria-invalid="true" class="error">
                                                <option value="">请选择</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">商铺面积:</label>
                                        <div class="col-sm-3">
                                            <input type="text" class="form-control" id="s_start" style="width: 80%;float:left">
                                            <span style="float:left;margin-top:10px;">㎡&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;—</span>
                                        </div>
                                        <div class="col-sm-3">
                                            <input type="text" class="form-control" id="s_end"  style="width: 80%;float:left">
                                            <span style="float:left;margin-top:10px">㎡</span>
                                        </div>
                                        <div class="col-sm-3">
                                            <button type="button" class="btn btn-primary " onclick="doSerch()">
                                                <i class="fa fa-search" style="width: 13px;"></i>&nbsp;搜索
                                            </button>
                                            <button type="reset" class="btn btn-primary " >
                                                <i class="fa fa-trash" style="width: 13px;"></i>&nbsp;重置
                                            </button>
                                          <shiro:hasPermission name="addshop">
                                            <a id="add" data-toggle="modal" class="btn btn-success" ><i class="fa fa-plus"></i>新增</a>
                                          </shiro:hasPermission>
                                        </div>
                                    </div>

                                </form>

                            </div>

                        </div>
                    </div>
                    <div class="jqGrid_wrapper" style="margin-top: 10px">
                        <table id="table_list"></table>
                        <div id="pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="modal-form" class="modal fade" aria-hidden="true" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="row">
                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5 id="div_title">编辑商铺</h5>
                            <div class="ibox-tools">
                                <a id="modal-form-closer" data-toggle="modal" href="#modal-form">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <form class="form-horizontal m-t" id="editForm">
                                <input type="hidden" id="id" name="id"  />
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺名称：</label>
                                    <div class="col-sm-6">
                                        <input id="name" name="name" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="text文本50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺编号：</label>
                                    <div class="col-sm-6">
                                        <input id="code" name="code" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="text文本50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺地址：</label>
                                    <div class="col-sm-3">
                                        <select id="province" name="province" class="form-control" type="text"
                                                aria-required="true" aria-invalid="true" class="error">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-3">
                                        <select id="city" name="city" class="form-control" type="text"
                                                aria-required="true" aria-invalid="true" class="error">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-3">
                                        <select id="county" name="county" class="form-control" type="text"
                                                aria-required="true" aria-invalid="true" class="error">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label"></label>
                                    <div class="col-sm-6">
                                        <input id="detail_address" name="detail_address" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="街道地址text文本50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺面积(㎡)：</label>
                                    <div class="col-sm-6">
                                        <input id="area" name="area" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="数字50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺底价(元)：</label>
                                    <div class="col-sm-6">
                                        <input id="floor_price" name="floor_price" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="数字50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">商铺指导价(元)：</label>
                                    <div class="col-sm-6">
                                        <input id="guide_price" name="guide_price" class="form-control" type="text"
                                               aria-required="true" aria-invalid="true" class="error" placeholder="数字50字符">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-11 col-sm-offset-5">
                                        <button class="btn btn-primary" type="submit">提交</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="detail-form" class="modal fade" aria-hidden="true" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="row">
                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5 id="c_div_title">商铺详情</h5>
                            <div class="ibox-tools">
                                <a id="detail-form-closer" data-toggle="modal" href="#detail-form">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>

                        </div>
                        <div class="ibox-content">
                            <form class="form-horizontal" role="form">
                                <div class="form-group" >
                                    <label class="col-sm-4 control-label">商铺名称：</label>
                                    <div class="col-sm-4"  >
                                        <label class=" control-label" id="c_name" style="text-align:left;"> </label>
                                    </div>
                                </div>
                                <div class="form-group" >
                                    <label class="col-sm-4 control-label">商铺编号：</label>
                                    <div class="col-sm-4" >
                                        <label class=" control-label" id="c_code" style="text-align:left;"></label>
                                    </div>
                                </div>
                                <div class="form-group" >
                                    <label class="col-sm-4 control-label">商铺地址：</label>
                                    <div class="col-sm-6" >
                                        <label class=" control-label" id="c_address" style="text-align:left;"></label>
                                    </div>
                                </div>

                                <div class="form-group" >
                                    <label class="col-sm-4 control-label">商铺面积：</label>
                                    <div class="col-sm-6" >
                                        <label class=" control-label" id="c_area"></label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">商铺底价：</label>
                                    <div class="col-sm-6">
                                        <label class=" control-label" id="c_floor_price"></label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">商铺指导价：</label>
                                    <div class="col-sm-6">
                                        <label class=" control-label" id="c_guide_price"></label>
                                    </div>
                                </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">创建时间：</label>
                                <div class="col-sm-6">
                                    <label class=" control-label" id="c_create_time"></label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">更新时间：</label>
                                <div class="col-sm-6">
                                    <label class=" control-label" id="c_update_time"></label>
                                </div>
                            </div>
                                <div class="form-group">
                                    <div class="col-sm-11 col-sm-offset-5">
                                        <a class="btn btn-primary" href="javascript:void(0);" onclick="$('#detail-form').modal('hide')">确定</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function renderButton(cellvalue, options, rowObject){
        var d = "<shiro:hasPermission name='detailshop'>" +
            "<a class='btn btn-primary btn-xs' data-toggle='modal' data-target='#detail-form' onclick='javascript:detail(\""+cellvalue+"\")'>详情</a>" +
            "</shiro:hasPermission>";
        d += "<shiro:hasPermission name='editshop'>" +
            "<button class='btn btn-primary btn-xs' data-toggle='modal' data-target='#modal-form' onclick='javascript:editShop(\""+cellvalue+"\")'>编辑</button>" +
            "</shiro:hasPermission>";
        return d;
    }
</script>
<!-- 自定义js -->
<script src="<%=path%>/nh-micro-js/js/system/shop.js"></script>
<script src="<%=path%>/nh-micro-js/js/region.js"></script>
</body>

</html>

