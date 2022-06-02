<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>文件上传</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/style.css?v=3.2.0" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="<%=path%>/nh-micro-js/js/bootstrap/css/plugins/dropzone/dropzone.css" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeIn">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
		<!-- 上传文件的例子 -->
   		<form id="fileForm" novalidate method="post" action="/micromvc/file/testFile" enctype="multipart/form-data">
			<table id="fileTable" style="margin-top: 10px; margin-left: 20px; margin-right:10px;">
				<tr height="30px">
					<td align="right">上传文件：</td>
					<td>
						 <input type="file" id="uploadObjectFile" name="file" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td><button type="button" class="btn btn-primary pull-right" onclick="detailUploadPic()">提交</button></td>
				</tr>
			</table>
		</form>
		
                    <%--<div class="ibox-content">--%>
                        <%--<form id="my-awesome-dropzone" class="dropzone" action="form_file_upload.html#">--%>
                            <%--<div class="dropzone-previews"></div>--%>
                            <%--<button type="submit" class="btn btn-primary pull-right">提交</button>--%>
                        <%--</form>--%>
                        <%--<div>--%>
                            <%--<div class="m"><small>DropzoneJS是一个开源库，提供拖放文件上传与图片预览：<a href="https://github.com/enyo/dropzone" target="_blank">https://github.com/enyo/dropzone</a></small>，百度前端团队提供的<a href="http://fex.baidu.com/webuploader/" target="_blank">Web Uploader</a>也是一个非常不错的选择，值得一试！</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>

                  <!--   <div class="ibox-content">
                        <div class="form-group">
                            <label class="title">真人照(最多只能传一张)</label>
                            <form enctype="multipart/form-data">
                                <div id="dropz" class="dropzone"></div>//这段代码是展示dropzone.js的精髓
                            </form>
                        </div>
                    </div>
                    <div>
                        <input type="file" name="myFile" id="myFile" multiple onchange="detailUploadPic()"/>
                    </div> -->
                </div>
            </div>
        </div>
    </div>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/jquery-2.1.1.min.js"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/content.js?v=1.0.0"></script>
    <!-- DROPZONE -->
    <script src="<%=path%>/nh-micro-js/js/bootstrap/js/plugins/dropzone/dropzone.js"></script>
    <script>
//        $(document).ready(function () {
//            Dropzone.options.myAwesomeDropzone = {
//
//                autoProcessQueue: false,
//                uploadMultiple: true,
//                parallelUploads: 100,
//                maxFiles: 100,
//
//                // Dropzone settings
//                init: function () {
//                    var myDropzone = this;
//
//                    this.element.querySelector("button[type=submit]").addEventListener("click", function (e) {
//                        e.preventDefault();
//                        e.stopPropagation();
//                        myDropzone.processQueue();
//                    });
//                    this.on("sendingmultiple", function () {});
//                    this.on("successmultiple", function (files, response) {});
//                    this.on("errormultiple", function (files, response) {});
//                }
//
//            }
//
//        });
//        Dropzone.autoDiscover = false;
//        var myDropzone = new Dropzone("#dropz", {
//            url: "/micromvc/file/testFile",//文件提交地址
//            method:"post",  //也可用put
//            paramName:"file", //默认为file
//            maxFiles:1,//一次性上传的文件数量上限
//            maxFilesize: 2, //文件大小，单位：MB
//            acceptedFiles: ".jpg,.gif,.png,.jpeg", //上传的类型
//            addRemoveLinks:true,
//            parallelUploads: 1,//一次上传的文件数量
//            //previewsContainer:"#preview",//上传图片的预览窗口
//            dictDefaultMessage:'拖动文件至此或者点击上传',
//            dictMaxFilesExceeded: "您最多只能上传1个文件！",
//            dictResponseError: '文件上传失败!',
//            dictInvalidFileType: "文件类型只能是*.jpg,*.gif,*.png,*.jpeg。",
//            dictFallbackMessage:"浏览器不受支持",
//            dictFileTooBig:"文件过大上传文件最大支持.",
//            dictRemoveLinks: "删除",
//            dictCancelUpload: "取消",
//            init: function() {
//                //res为服务器响应回来的数据
//                this.on("success", function(result) {
//
//                });
//                this.on("removedfile", function(result) {
//                    $.ajax({
//                        url: "改成你的删除图片的路径",
//                        type: "post",
//                        //file.path可以获取到点击删除按钮的那张图片
//                        data: { 'path': result.path }
//                    });
//                });
//            }
//        });

    function detailUploadPic() {
        var files = $('#uploadObjectFile')[0].files; //获取file控件中的内容
        if (!files) {
            return;
        }

        /*var formData = new FormData();
        for (i=0;i< files.length; i++) {
            formData.append('file[]', files[i]);
        }*/
        var formData = new FormData();
        formData.append('file', files[0]);

        //var formData = new FormData($('#fileForm')[0]);
        console.log(formData)
        $.ajax({
            type: 'post',
            url: "/micromvc/file/testFile",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
        }).success(function (data) {
            console.log(data);
        }).error(function () {
            alert("上传失败");
        });

    }
    </script>
</body>

</html>