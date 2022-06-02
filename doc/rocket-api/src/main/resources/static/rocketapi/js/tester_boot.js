/*const gwtStartupScript = document.currentScript.getAttribute('data-gwt-startup-src');

const readFavoriteLanguageCallback = function (result) {
    const userFavoriteLanguage = (result && result.favoriteLanguage) || 'en';

    window.localStorage.setItem({ favoriteLanguage: userFavoriteLanguage }, () => {
        // create the GWT meta tag that allows the GWT bootstrap to choose the right permutation
        const gwtLocaleMetaTag = document.createElement('meta');
        gwtLocaleMetaTag.setAttribute('name', 'gwt:property');
        gwtLocaleMetaTag.content = `locale=${userFavoriteLanguage}`;
        document.head.appendChild(gwtLocaleMetaTag);

        // then inject the GWT bootstrap code
        const gwtBootstrapScriptTag = document.createElement('script');
        gwtBootstrapScriptTag.setAttribute('src', gwtStartupScript);

        document.body.appendChild(gwtBootstrapScriptTag);
    });
};

window.localStorage.getItem('favoriteLanguage', readFavoriteLanguageCallback);*/
let loadApiListUrl = baseUrl + "/api-list";
let saveApiUrl = baseUrl + "/api-info";
let getApiUrl = baseUrl + "/api-info/";
let lastApiUrl = baseUrl + "/api-info/last";
let deleteApiUrl = baseUrl + "/api-info";
let runApiUrl = baseUrl +"/api-info/run";
let getApiGroupNameUrl = baseUrl + "/group-name-list";
let getApiNameUrl = baseUrl + "/api-name-list";
let renameGroupUrl = baseUrl + "/api-info/group";
let saveExampleUrl = baseUrl + "/api-example";
let lastExampleUrl = baseUrl + "/api-example/last";
let deleteExampleUrl = baseUrl + "/api-example";
let apiDocPushUrl = baseUrl + "/api-doc-push";

//代码提示
let completionItemsUrl = baseUrl + "/completion-items";
let completionClazzUrl = baseUrl + "/completion-clazz";

//远程同步
let remoteSyncUrl = baseUrl + "/remote-sync";

//导出,导入
let exportUrl = baseUrl + "/export";
let importUrl = baseUrl + "/import";

//登录操作
let loginUrl = baseUrl + "/login";
let logoutUrl = baseUrl + "/logout";

//配置操作
let getApiConfigUrl = baseUrl + "/api-config";
let saveApiConfigUrl = baseUrl + "/api-config";

//数据源操作
let getDBConfigListUrl = baseUrl + "/db-config/list"
let saveDBConfigUrl = baseUrl + "/db-config";
let removeDBConfigUrl = baseUrl + "/db-config";
let getDBDriverListUrl = baseUrl + "/db-driver/list"
let testConnectUrl = baseUrl + "/db-test";

//目录操作
let directoryListUrl = baseUrl + "/directory/list";
let saveDirectoryUrl = baseUrl + "/directory";
let deleteDirectoryUrl = baseUrl + "/directory";

let checkVersionUrl = baseUrl + "/check-version";

let editor = "admin";

//当前apiInfo
let currApiInfo = null;
//当前example
let currExample = {
    apiInfoId:null,
    url:null,
    method:null,
    requestHeader:null,
    requestBody:null,
    responseHeader:null,
    responseBody:null,
    status:null,
    elapsedTime:0,
    options:null
}

let defaultExample = {

}

let editorTextarea;
let exampleTextarea;
let originalModel;
let modifiedModel;
let settingTextarea;
let apiSettingTextarea;

let hasResponse;
let gdata = {
    directoryList:null,
    apiList:null,
    exampleHistoryList:null,
    apiHistoryList:null,
    historyCurrPageNo:1,
    completionItems:null
}

//本地缓存信息
let rocketUser = {
    "user":{
        username:"admin"
    },
    "panel":{
        "left":"show",
        "leftWidth": "325px",
        "bottom":"show",
        "bottomHeight":"150px"
    },
    "setting":{
        "header":{},
        "options":{}
    }
}

function loadCurrApi() {
    if (currApi){
        loadDetailById(currApi,"#editor-section")
        //构建api history
    }else{
        newRequest();
    }
}


function initUser() {
    if (rocketUser.user.token){
        $("#top-section .login-btn").hide();
        $("#top-section .login-info").show();
        $("#top-section .login-info .name").text(rocketUser.user.username);
        $.ajaxSetup({
            headers:{"rocket-user-token":rocketUser.user.token}
        });
    }
}

function initPanel() {
    if (rocketUser.panel.left == "show"){
        $(".h-splitter").click();
    }else{
        $(".hide-pane-l").click();
    }

    if (rocketUser.panel.bottom == "show"){
        $(".v-splitter").click();
    }else{
        $(".bottom-down").click();
    }
}

//版本检测
function versionCheck() {
    $.getJSON(checkVersionUrl,function (data) {
        data = unpackResult(data);
        if (data.data && data.data.value){
            $("#top-section .center-version").show();
            $("#top-section .center-version span").text(data.data.value);
        }
    });
}

$(function(){
    if (!localStorage.getItem("rocketUser")){
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    }else{
        rocketUser = JSON.parse(localStorage.getItem("rocketUser"));
    }
    $("#loader").hide();
    //加载API列表
    loadApiList(false);
    loadEvent();
    initUser();
    initPanel();
    versionCheck();
    initCompletionItems();

    editorTextarea = monaco.editor.create(document.getElementById('monaco-editor'), {
        language: languageName,
        wordWrap: 'on',  //自行换行
        verticalHasArrows: true,
        horizontalHasArrows: true,
        scrollBeyondLastLine: false,
        contextmenu:false,
        automaticLayout: true,
        fontSize:13,
        minimap: {
            enabled: false // 关闭小地图
        }

    });


    //run
    editorTextarea.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter, function () {
        runApi(false);
    });

    //debug
    editorTextarea.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Alt| monaco.KeyCode.Enter, function () {
        runApi(true);
    });

    //保存
    editorTextarea.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S ,function () {
        saveEditor('#editor-section')
    });

    //快捷键提示
    editorTextarea.addCommand(monaco.KeyMod.Alt | monaco.KeyCode.US_SLASH ,function () {
        editorTextarea.trigger('', 'editor.action.triggerSuggest', {});
    });

    //多行注释
    editorTextarea.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.US_SLASH ,function () {
        let selectRange = editorTextarea.getSelection();
        let prefixRange = new monaco.Range(selectRange.startLineNumber, selectRange.startColumn, selectRange.startLineNumber, selectRange.startColumn+2);
        let prefix = editorTextarea.getModel().getValueInRange(prefixRange);
        let postfixRange = new monaco.Range(selectRange.endLineNumber, selectRange.endColumn-2, selectRange.endLineNumber, selectRange.endColumn);
        let postfix = editorTextarea.getModel().getValueInRange(postfixRange);

        let prefixOp = {"range":prefixRange,"text":""};
        let postfixOp = {"range":postfixRange,"text":""};
        //取消注释
        if (prefix == '/*' && postfix == "*/"){
            editorTextarea.executeEdits('insert-code',[prefixOp])
            if (selectRange.startLineNumber == selectRange.endLineNumber){
                postfixOp.range.startColumn = postfixOp.range.startColumn - 2;
                postfixOp.range.endColumn = postfixOp.range.endColumn - 2;
            }
            editorTextarea.executeEdits('insert-code',[postfixOp])
            return;
        }

        prefixRange = new monaco.Range(selectRange.startLineNumber, selectRange.startColumn, selectRange.startLineNumber, selectRange.startColumn);
        postfixRange = new monaco.Range(selectRange.endLineNumber, selectRange.endColumn , selectRange.endLineNumber, selectRange.endColumn);
        prefixOp = {"range":prefixRange,"text":"/*"};
        postfixOp = {"range":postfixRange,"text":"*/"};
        editorTextarea.executeEdits('insert-code',[prefixOp])
        if (selectRange.startLineNumber == selectRange.endLineNumber){
            postfixOp.range.startColumn = postfixOp.range.startColumn + 2;
            postfixOp.range.endColumn = postfixOp.range.endColumn + 2;
        }
        editorTextarea.executeEdits('insert-code',[postfixOp])
    });

    //单行注释
    editorTextarea.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.US_SLASH ,function () {
        let selectRange = editorTextarea.getSelection();

        let isAdd = false;
        for(let i=selectRange.startLineNumber;i<=selectRange.endLineNumber;i++){
            let lineNumber = i;
            let range = new monaco.Range(lineNumber, 0, lineNumber, 3);
            let prefix = editorTextarea.getModel().getValueInRange(range);
            if (prefix != "//"){
                isAdd = true;
                break;
            }
        }

        for(let i=selectRange.startLineNumber;i<=selectRange.endLineNumber;i++){
            let lineNumber = i;
            let range = null;
            let text = null;
            if (isAdd){
                range = new monaco.Range(lineNumber, 0, lineNumber, 0);
                text = "//";
            }else{
                range = new monaco.Range(lineNumber, 0, lineNumber, 3);
                text = "";
            }
            editorTextarea.executeEdits('insert-code',[{"range":range,"text":text}])
        }
    });



    exampleTextarea = monaco.editor.create(document.getElementById('example-editor'), {
        language: 'json',
        values:"",
        verticalHasArrows: true,
        horizontalHasArrows: true,
        links:true,
        contextmenu:false,
        fontSize:12,
        lineHeight:18,
        Index: 'Advanced',
        scrollBeyondLastLine: false,
        minimap: {
            enabled: false // 关闭小地图
        }
    });

});


function loadHistoryScrollEvent() {
    $("#history-api-section .records").unbind("scroll").bind("scroll", function (e) {
        var sum = this.scrollHeight;
        if (sum <= $(this).scrollTop() + $(this).height()) {
            gdata.historyCurrPageNo = gdata.historyCurrPageNo +1;
            loadApiHistory(currApiInfo.id,gdata.historyCurrPageNo);
        }
    });
}

function loadKeyCodeEvent() {
    $(document).keyup(function (event) {
        if (event.keyCode == 87 && event.altKey){
            if (currPage == "example"){
                showEditorPanel();
            }else {
                showExamplePanel();
            }
        }
        return false;
    });
}

function loadRemoteSyncChecboxEvent(target) {

    //隐藏
    $(target).on("click",".level1 .icon-caret-down",function () {
        $(this).parents(".level1").find(">ul").hide();
        $(this).removeClass("icon-caret-down").addClass("icon-caret-right");
        return false;
    })

    $(target).on("click",".level1 .icon-caret-right",function () {
        $(this).parents(".level1").find(">ul").show();
        $(this).removeClass("icon-caret-right").addClass("icon-caret-down");
        return false;
    })

    //根节点
    $(target).on("click",".level0>.tree-entry input:checkbox",function (e) {
        let isChecked = $(this).prop('checked');
        if (isChecked){
            $(this).parents(".level0").find(".level1 .tree-entry input:checkbox").prop("checked","checked");
        }else{
            $(this).parents(".level0").find(".level1 .tree-entry input:checkbox").prop("checked","");
        }

        $(target + " .items-count").text($(target + " .level2>.tree-entry input:checkbox:checked").length+" item selected");
    });

    //二级节点
    $(target).on("click",".level1>.tree-entry input:checkbox",function (e) {
        let isChecked = $(this).prop('checked');
        if (isChecked){
            $(this).parents(".level1").find(".level2 .tree-entry input:checkbox").prop("checked","checked");
        }else{
            $(this).parents(".level1").find(".level2 .tree-entry input:checkbox").prop("checked","");
        }
        let checkedNum = $(target + " .level1>.tree-entry input:checkbox:checked").length;
        if (checkedNum == 0){
            $(target + " .level0>.tree-entry input:checkbox").prop("checked","");
        }else {
            $(target + " .level0>.tree-entry input:checkbox").prop("checked","checked");
        }
        $(target + " .items-count").text($(target + " .level2>.tree-entry input:checkbox:checked").length+" item selected");
    });

    $(target).on("click",".btn-link",function (e) {
        $(this).parent().find("input:checkbox").click();
    })

    //三级节点
    $(target).on("click",".level2>.tree-entry input:checkbox",function (e) {
        let checked2Num = $(this).parents(".level1").find(".level2>.tree-entry input:checkbox:checked").length;
        if (checked2Num == 0){
            $(this).parents(".level1").find(">.tree-entry input:checkbox").prop("checked","");
        }else{
            $(this).parents(".level1").find(">.tree-entry input:checkbox").prop("checked","checked");
        }

        let checkedNum = $(target + " .level1>.tree-entry input:checkbox:checked").length;
        if (checkedNum == 0){
            $(target + " .level0>.tree-entry input:checkbox").prop("checked","");
        }else{
            $(target + " .level0>.tree-entry input:checkbox").prop("checked","checked");
        }
        $(target + " .items-count").text($(target + " .level2>.tree-entry input:checkbox:checked").length+" item selected");
    });
}

function loadEvent() {
    loadSelectBoxEvent();
    loadInputTypeEvent();
    loadEditAbleEvent();
    loadExampleMethodEvent();
    loadHistoryScrollEvent();
    loadLeftSideEvent();
    loadBottomSideEvent();
    loadRemoteSyncChecboxEvent("#remote-sync")
    loadRemoteSyncChecboxEvent("#export-dialog")
    loadKeyCodeEvent()
}

function openConfirmModal(msg,fun) {
    $("#confirmModal").show();
    $("#confirmModal .gwt-HTML").text(msg);
    $("#confirmModal .r-btn-danger").unbind('click').bind("click",fun);
    $("#modal-backdrop").show();
}

/**
 * 关闭确认框
 */
function closeConfirmModal() {
    $("#confirmModal").hide();
    $("#modal-backdrop").hide();
}

function copyApi(e,id) {
    let cApi = null;
    $.each(gdata.apiList,function (index,item) {
        if (id == item.id){
            cApi = item;
            return false;
        }
    })
    delete cApi.id;
    cApi.name = (cApi.name?cApi.name:cApi.path) +"-Copy"
    cApi.path = cApi.path+"-TEMP-"+uuid();
    cApi.script = editorTextarea.getValue();
    saveExecuter(cApi);
}

//API移动
function moveApi(e,id) {
    showDialogGroup(id);
}
function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}
/**
 * 添加一个请求
 * @param e
 */
function addARequest(e) {
    newRequest();
    saveExecuter({
        "method": "GET",
        "datasource":$("#editor-section").find(".api-info-datasource").attr("default-value"),
        "path": "TEMP-"+uuid(),
        "directoryId": $(e).parents(".service").attr("data-directoryId"),
        "editor": editor,
        "name": "Request",
        "script": "",
        "options":"{}"
    });
}



function removeApi(e,id) {

    openConfirmModal("The request will be permanently deleted. Are you sure?",function () {
        showSendNotify("Removeing api")
        $.ajax({
            type: "delete",
            url: deleteApiUrl,
            contentType : "application/json",
            data: JSON.stringify({"id":id}),
            success: function (data) {
                closeConfirmModal();
                data = unpackResult(data);
                if (data.code !=200){
                    openMsgModal(data.msg);
                    return;
                }
                $(e).parents(".request").remove();
                history.pushState(null,null,baseUrl);
            },complete:function () {
                hideSendNotify();
            }
        });
    });

    event.stopPropagation();    //  阻止事件冒泡
}

function buildExampleBodyJson() {
    let exampleBodyStr = buildExampleBodyStr();
    try {
        return JSON.parse(exampleBodyStr);
    }catch (e) {
        return {};
    }
}

function buildExampleBodyStr() {
    let type = $("#example-section .example-method").val();
    return (type == "POST" || type == "PUT")?exampleTextarea.getValue():"";
}

function runApi(debug) {
    let params = {
        "debug":debug,
        "script":editorTextarea.getValue(),
        "datasource":$("#editor-section .api-info-datasource").val(),
        "header": buildHeaderJson(getHeaderParams()),
        "body" : buildExampleBodyJson(),
        "pattern":$("#editor-section .api-info-path").val(),
        "options":buildApiOptionsJsonStr(),
        "url":$("#example-section .example-url").val()
    }

    showSendNotify("Running script");
    $("#bottom-side .console-bottom").html("");
    let startTime=new Date().getTime()
    $.ajax({
        type: "POST",
        url: runApiUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            let content = "";
            if (data.data && data.data.logs){
                $.each(data.data.logs,function (index,item) {
                    content += item +"\r\n";
                })
            }

            if (data.code != 200){
                content += "<div style='color: #de6a53;background-color: none;letter-spacing: 1px;'>"+data.msg+"</div>";
            }else{
                content += "<a style='color:green;'>"+data.msg+"</a>";
            }

            content += "<p>--------------</p>";

            content += buildJsonStr((data.data && (data.data.data == 0 || data.data.data))?data.data.data:"There is no return value");

            $("#bottom-side .console-bottom").html(content);

        },complete:function () {
            hideSendNotify();
            hasConsole = true;
            let ms = (new Date().getTime()-startTime)+"ms";
            $("#bottom-side .el-time").attr("title",ms).text("Elapsed time: "+ms);
        }
    });

    if (debug){
        let tokenStr = "DEBUG次数";
        _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
    }else {
        let tokenStr = "RUN次数";
        _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
    }
}

function buildJsonStr(obj) {
    if (typeof obj == "object"){
        return  JSON.stringify(obj,null, "\t");
    }
    return obj;
}

function insertExample(e) {
    let position = editorTextarea.getPosition();
    let express = $(e).attr("express");
    editorTextarea.executeEdits('', [
        {
            range: {
                startLineNumber: position.lineNumber,
                startColumn: position.column,
                endLineNumber: position.lineNumber,
                endColumn: position.column
            },
            text: express
        }
    ]);
}

//comment 编辑事件
function loadEditAbleEvent() {
    $(".editable-input>input").on("click",function () {
        $(this).removeAttr("readonly");
        $(this).parent().removeClass("read-only");
        $(this).parent().find(".add-on>i").removeClass("icon-pencil");
        $(this).parent().find(".add-on>i").addClass("icon-ok");
    });

    $(".editable-input>input").on("blur",function () {
        $(this).attr("readonly","readonly");
        $(this).parent().addClass("read-only");
        $(this).parent().find(".add-on>i").removeClass("icon-ok");
        $(this).parent().find(".add-on>i").addClass("icon-pencil");
    });
}

//输入框占用字节显示事件
function loadInputTypeEvent() {
    $(".gwt-TextBox").on("blur",function () {
        let value = $(this).val();
        $(this).parents(".uri-input").children(".uri-length").text("length: "+value.length+" byte(s) ");
    });
}
//method下拉框事件
function loadSelectBoxEvent() {
    $(".dropdown-menu>li").on("click",function (e) {
        let value = $(this).text().trim();
        $(this).parents(".btn-group").find(".input-append>input").val(value);
    });
}

function loadDetailByHistoryId(id, form) {
    showEditorPanel();

    let apiHistory = null;
    for(let i=0;i<gdata.apiHistoryList.length;i++){
        if (gdata.apiHistoryList[i].id == id){
            apiHistory = jQuery.extend(true, {}, gdata.apiHistoryList[i]);
            break;
        }
    }
    apiHistory.id = apiHistory.apiInfoId;

    loadDetail(apiHistory,form,function () {
        //构建example history
        loadExampleHistory(currApiInfo,true);

        //构建 api history
        loadApiHistory(currApiInfo.id,1);
    });

}

function loadDetailById(id,form,callback) {
    $.getJSON(getApiUrl+id,function (data) {
        data = unpackResult(data).data;
        if (!data){
            newRequest();
            return;
        }
        loadDetail(data,form,callback);
    });

}

function loadDetail(apiInfo,form,callback) {

    cancelDiff();

    //init curr data
    hasConsole = null;
    hasResponse = null;
    currApiInfo = apiInfo;
    currApi = currApiInfo.id;
    //------构建editor
    $(".request").removeClass("selected");
    $(".service").removeClass("parent-selected");
    $(".request"+currApiInfo.id).addClass("selected");
    $(".request"+currApiInfo.id).parents(".service").addClass("parent-selected");
    $(".request"+currApiInfo.id).parents(".service").removeClass("collapsed");
    collapsedDirectory(apiInfo.directoryId)
    $('#editor-section .draft-ribbon-text').text("Edit");

    let url = baseUrl+"?id="+currApiInfo.id+"&page="+(currPage?currPage:'example');
    history.pushState(null,null,url);
    removeAllQueryParameterForm("#bottom-side");

    $(form).find(".api-info-id").val(currApiInfo.id);
    $(form).find(".api-info-method").val(currApiInfo.method);
    $(form).find(".api-info-datasource").val(currApiInfo.datasource),
    $(form).find(".api-info-path").val(currApiInfo.path.startsWith("TEMP-")?"":currApiInfo.path).blur();
    $(form).find(".api-info-fullpath").val(currApiInfo.fullPath).blur();
    $(form).find(".api-info-directory").val(currApiInfo.directoryId);
    $(form).find(".api-info-editor").val(currApiInfo.editor);
    $(form).find(".api-info-comment").val(currApiInfo.name);

    $(form).find(".api-info-method").removeAttr("readonly").parent().removeClass("disabled");
    $(form).find(".api-info-path").removeAttr("readonly");
    $(form).find(".api-info-datasource").removeAttr("readonly");
    $(form).find(".api-info-datasource").parent().removeClass("disabled");

    if(currApiInfo.type == 'Code'){
        $(form).find(".api-info-method").attr("readonly","readonly");
        $(form).find(".api-info-method").parent().addClass("disabled");
        $(form).find(".api-info-path").attr("readonly","readonly");
        $(form).find(".api-info-datasource").attr("readonly","readonly");
        $(form).find(".api-info-datasource").parent().addClass("disabled");
    }

    document.title = currApiInfo.name?currApiInfo.name:currApiInfo.path;
    buildApiOptionsDom(currApiInfo.options);
    editorTextarea.setValue(currApiInfo.script?currApiInfo.script:"");

    //构建example history
    loadExampleHistory(currApiInfo,true);

    //构建 api history
    loadApiHistory(currApiInfo.id,1);

    //回调
    if (callback){
        callback();
    }
}

function apiOptionAdd(key,value) {
    key = key?key:"";
    value = value?value:"";
    let $form = $("#bottom-side");
    $form.find(".query-parameters-form-block").append("<div class=\"query-parameter-row active\" e2e-tag=\"query-parameter\"><span class=\"gwt-CheckBox query-parameter-cell\" title=\"Enable/Disable\" e2e-tag=\"query-parameter-state\"><label for=\"gwt-uid-2020\"></label></span><span class=\"expression-input input-append query-parameter-cell-name query-parameter-cell\"><input type=\"text\" class=\"gwt-TextBox key\" onchange='buildUrlInput()' value='"+key+"' placeholder=\"name\" e2e-tag=\"query-parameter-name\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span><span class=\"gwt-InlineLabel query-parameter-cell\">=</span><span class=\"expression-input input-append query-parameter-cell-value query-parameter-cell\"><input type=\"text\" onchange='buildUrlInput()' class=\"gwt-TextBox value\" value='"+value+"' placeholder=\"value\" e2e-tag=\"query-parameter-value\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span><button class=\"r-btn r-btn-link query-parameter-cell\" onclick='queryParameterRemove(this,\"#editor-section\")' title=\"Remove\" e2e-tag=\"query-parameter-remove\"><i class=\"fa fa-times-thin\"></i><span></span><span class=\"r-btn-indicator\" aria-hidden=\"true\" style=\"display: none;\"></span></button><div class=\"btn-group ctrls dropdown-secondary query-parameter-encoding query-parameter-cell\" e2e-tag=\"query-parameter-additional-actions\"><ul class=\"pull-right dropdown-menu\"><li class=\"dropdown-item\" e2e-tag=\"query-parameter-encode\"><a><i class=\"fa fa-check\"></i> <span>Encode before sending</span></a></li></ul></div></div>");
    $form.find(".query-parameters-form-block .query-parameter-row .key").focus();
}

function saveAsEditor() {
    showDialogGroup(null);
}

function closeModal() {
    $("#modal-backdrop").hide();
    $("#msgModal").hide();
}

function openMsgModal(msg) {

    if ("Permission denied" == msg){
        showLoginDialog();
        return;
    }

    $("#modal-backdrop").show();
    $("#msgModal").show();
    $("#msgModal .modal-body").text(msg);
}

function confirmDialog(form) {
    let directoryId = $("#save-dialog .path-buttons .active").attr("data-directoryId");
    let params={
        "id": $("#save-dialog").attr("data-id"),
        "method": $(form).find(".api-info-method").val(),
        "datasource":$(form).find(".api-info-datasource").val(),
        "path": $(form).find(".api-info-path").val(),
        "directoryId": directoryId,
        "editor": $(form).find(".api-info-editor").val(),
        "name": $("#save-dialog .input-xlarge").val(),
        "script": editorTextarea.getValue(),
        "options":buildApiOptionsJsonStr()
    }

    saveExecuter(params);
}

function buildApiOptionsJsonStr() {
    let list = $("#bottom-side .query-parameters-form-block>.active");
    let map = {};
    $.each(list,function (index,item) {
        let key = $(item).find(".key").val();
        if (!key)return;
        let value = $(item).find(".value").val();
        map[key] = value;
    })
    return JSON.stringify(map);
}

function buildApiOptionsDom(optionsJsonStr) {
    $("#bottom-side .query-parameters-form-block").html("")
    let map = {};
    if (optionsJsonStr){
        map = JSON.parse(optionsJsonStr);
    }
    map = $.extend({}, rocketUser.setting.options, map);
    $.each(map,function (key,value) {
        apiOptionAdd(key,value);
    })
}

function removeAllQueryParameterForm(e) {
    $(e).find(".query-parameters-form-block").html("");
    $(e).find(".subtitle-counter").text("["+$(e).find(".query-parameters-form-block>.active").length+"]");
}

function confirmGroup() {
    cancelGroup();
    let value = $(".new-path .new-item-name").val();
    $("#save-dialog .local-drive").append("<li><a class='curr-add' onclick='listRequest(this)'><i class=\"api-tester-icon api-tester-project\"></i><span>"+value+"</span></a></li>")
    $("#save-dialog .curr-add").click();
}

function addGroup() {
    $(".new-path>a").hide();
    $(".new-path>div").show();
    $(".new-path .flex-container>input").val("GroupName");
}

function cancelGroup() {
    $(".new-path>a").show();
    $(".new-path>div").hide();
}

function cancelDialogGroup() {
    $("#modal-backdrop").hide();
    $("#save-dialog").hide();
}

function showDialogGroup(id) {
    $("#save-dialog").attr("data-id",id);
    $("#save-dialog").show();
    $("#save-dialog .input-xlarge").val($("#editor-section .api-info-comment").val());
    $("#save-dialog .path-buttons .r-btn").addClass("active");


    $("#save-dialog .local-drive").html("");

    let directoryId = null;
    if (id){
        $.each(gdata.apiList,function (index,item){
            if (item.id == id){
                directoryId = item.directoryId;
                return false;
            }
        })
    }

    buildDirectoryPath(directoryId);
}

function buildDirectoryPath(directoryId){
    $("#save-dialog .button-path-selector").remove();

    let currDirectoryId = directoryId;
    while (true){
        let directory = null;
        $.each(gdata.directoryList,function (index,item){
            if (item.id == directoryId){
                directory = item;
                return false;
            }
        })

        if (directory != null){
            let tPath = $("<a class=\"r-btn button-path-selector\" data-directoryId='"+directoryId+"' onclick='buildDirectoryPath(\""+directoryId+"\")' ><i class=\"api-tester-icon api-tester-project\"></i><span>"+directory.name+"</span></a>");
            if (currDirectoryId == directoryId){
                $("#save-dialog .r-btn").removeClass("active");
                tPath.addClass("active");
            }

            $("#save-dialog .path-buttons .root-path").after(tPath);
            directoryId = directory.parentId;
        }

        if (directory == null || !directory.parentId){
            break;
        }
    }

    listChildrens(currDirectoryId);
}

function listChildrens(directoryId) {


    $("#save-dialog .local-drive").html("");

    let isEmpty = true;
    $.each(gdata.directoryList,function (index,item) {

        if (!directoryId && !item.parentId){

        }else if (directoryId != item.parentId){
            return;
        }
        isEmpty = false;
        $("#save-dialog .local-drive").append("<li><a onclick='buildDirectoryPath(\""+item.id+"\")'><i class=\"api-tester-icon api-tester-project\"></i><span>"+item.name+"</span></a></li>")
    })
    $.each(gdata.apiList,function (index,item) {
        if (directoryId != item.directoryId){
            return;
        }
        isEmpty = false;
        $("#save-dialog .local-drive").append("<li><a><i class=\"api-tester-icon api-tester-request\"></i><span>"+(item.name?item.name:item.path)+"</span></a></li>")
    })

    if (isEmpty){
        $("#save-dialog .local-drive").append("<li><p class=\"navbar-text\">Empty</p></li>")
    }

}

function saveEditor(form) {

    let id = $(form).find(".api-info-id").val();

    if (!id){
        showDialogGroup(null);
        return;
    }

    let params={
        "id":$(form).find(".api-info-id").val(),
        "method": $(form).find(".api-info-method").val(),
        "datasource":$(form).find(".api-info-datasource").val(),
        "path": $(form).find(".api-info-path").val(),
        "directoryId": $(form).find(".api-info-directory").val(),
        "editor": $(form).find(".api-info-editor").val(),
        "name": $(form).find(".api-info-comment").val(),
        "script": editorTextarea.getValue(),
        "options":buildApiOptionsJsonStr()
    };

    saveExecuter(params);
}

function showSendNotify(msg) {
    $("#notification").show().find("#notification-message").text(msg);
}
function hideSendNotify() {
    $("#notification").hide();
}

function saveExecuter(params) {

    showSendNotify("Saveing api");
    $.ajax({
        type: "post",
        url: saveApiUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            cancelDialogGroup();
            currApi = data.data;

            //参数保存
            saveExample(currApi);

            loadApiList(false,function () {
                collapsedDirectory(params.directoryId)
            });

            if (params.id){
                let tokenStr = "API保存次数";
                _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
            }else{
                let tokenStr = "API新增次数";
                _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
            }

        },complete:function (req,data) {
            hideSendNotify();
        }
    });
    if (params.id){
        MtaH5.clickStat("api_save")
    }else{
        MtaH5.clickStat("api_new")
    }
}

function searchApi(e) {
    let keyword = $(e).val().trim();
    let searchResult = searchKeyword(keyword);
    buildApiTree(searchResult.dirList,searchResult.apiList,keyword?"":"collapsed");
}

function searchKeyword(keyword) {

    if (!keyword){
        return {
            "apiList":gdata.apiList,
            "dirList":gdata.directoryList
        }
    }

    //api搜索
    let searchApiResult = [];
    $.each(gdata.apiList,function (index,item) {
        if (keyword.split("=").length == 2){
            if (!item.options){
                return;
            }
            let kv = keyword.split("=");
            let options = JSON.parse(item.options);
            if (options[kv[0]] == kv[1]){
                searchApiResult.push(item);
            }
        }else if (item.name.indexOf(keyword) >=0){
            let newItem = $.extend({},item);
            newItem.name = newItem.name.replace(keyword,'<span class="s_match">'+keyword+'</span>')
            searchApiResult.push(newItem);
        }else if(item.path.indexOf(keyword)>=0){
            let newItem = $.extend({},item);
            newItem.path = newItem.path.replace(keyword,'<span class="s_match">'+keyword+'</span>')
            searchApiResult.push(newItem);
        }else if(item.fullPath.indexOf(keyword)>=0){
            let newItem = $.extend({},item);
            newItem.pathFullMatch = item.fullPath == keyword;
            newItem.path = newItem.path.replace(newItem.path,'<span class="s_match">'+newItem.path+'</span>')
            searchApiResult.push(newItem);
        }else if(item.script.indexOf(keyword)>=0){
            let newItem = $.extend({},item);
            newItem.script = newItem.script.replace(keyword,'<span class="s_match">'+keyword+'</span>')
            searchApiResult.push(newItem);
        }
    });

    //directory搜索
    let dirMap = new Map();
    let apiMap = new Map();

    //搜索API上级目录
    $.each(searchApiResult,function (index,item){
        searchParentDir(dirMap,item.directoryId,item.pathFullMatch);

        //搜索结果优先
        apiMap.set(item.id,item);
    });

    //搜索匹配的目录
    let searchDirectoryResult = [];
    $.each(gdata.directoryList,function (index,item) {
        if (item.name.indexOf(keyword) >=0 ){
            let newItem = $.extend({},item);
            newItem.name = newItem.name.replace(keyword,'<span class="s_match">'+keyword+'</span>')
            searchDirectoryResult.push(newItem);
        }else if(item.path && item.path.indexOf(keyword)>=0){
            let newItem = $.extend({},item);
            newItem.path = newItem.path.replace(keyword,'<span class="s_match">'+keyword+'</span>')
            searchDirectoryResult.push(newItem);
        }

    })

    //搜索匹配目录的上级目录，下级目录，当前目录和下级目录的接口
    $.each(searchDirectoryResult,function (index,item) {

        //搜索上级目录
        searchParentDir(dirMap,item.id)

        //搜索下级目录和接口
        searchChildDirOrApi(dirMap,apiMap,item.id)

        //搜索结果优先
        dirMap.set(item.id,item);
    })

    let apiList = [];
    let dirList = [];

    for(let item of dirMap) {
        dirList.push(item[1]);
    }

    for(let item of apiMap) {
        apiList.push(item[1]);
    }

    return {
        "apiList":apiList,
        "dirList":dirList
    }
}

function buildApiDom(item) {
    return $('<li class="request level2 request'+item.id+'" ><div class="name" onclick="loadDetailById(\''+item.id+'\',\'#editor-section\')" ><i\n' +
        '                                                        class="fa fa-caret-right invisible" aria-hidden="true" style="display: none;"></i>\n' +
        '                                                    <div class="play-icon" title="Launch request" ><i class="fa fa-play"></i></div>\n' +
        '                                                    <span class="gwt-InlineHTML node-text '+(item.type=='Code'?'fa fa-file-o':'')+'" >'+(item.name?item.name:item.path)+'<span style=\'margin-left:10px;color:#8a8989;\'>'+('['+item.path+']')+'</span></span>\n' +
        '                                                    <div class="status" aria-hidden="true" style="display: none;"></div>\n' +
        '                                                    <div class="btn-group ctrls dropdown-primary"  data-id="'+item.id+'" >' +
        '                                                       <a class="btn-mini dropdown-toggle" data-toggle="dropdown" ><i class="sli-icon-options-vertical"></i></a>\n' +
        '                                                        <ul class="pull-right dropdown-menu">' +
        '                                                           <li class="dropdown-item"  title="复制" onclick="copyApi(this,\''+item.id+'\')"><a><i class="fa fa-copy"></i><span class="gwt-InlineHTML">Copy</span></a></li>' +
        '                                                           <li class="dropdown-item"  title="移动" onclick="moveApi(this,\''+item.id+'\')"><a><i class="fa fa-random"></i><span class="gwt-InlineHTML">Move</span></a></li>' +
        '                                                           <li class="dropdown-item"  title="文档同步" onclick="apiPush(\''+item.id+'\')"><a><i class="fa fa-cloud-upload"></i><span class="gwt-InlineHTML">Doc</span></a></li>' +
        '                                                           <li class="dropdown-item"  title="移除" onclick="removeApi(this,\''+item.id+'\')"><a><i class="fa fa-trash-o"></i><span class="gwt-InlineHTML">Trash</span></a></li>' +
        '</ul>\n' +
        '                                                    </div>\n' +
        '                                                </div>' +
        '</li>');
}

function buildApiDirectoryDom(item,collapsed) {
    return $('<li class="'+collapsed+' service level1" data-directoryId="'+item.id+'" >' +
        '                                        <div class="name"  ><i class="fa '+(collapsed?'fa-caret-right':'fa-caret-down')+'"  onclick="collapsedTree(this)" ></i>\n' +
        '                                            <div aria-hidden="true" style="display: none;"><i class="fa fa-play" ></i></div>\n' +
        '                                            <span class="gwt-InlineHTML node-text" >'+item.name+'<span style=\'margin-left:10px;color:#8a8989;\'>'+(item.path?('['+item.path+']'):'')+'</span></span>\n' +
        '                                            <div class="status" aria-hidden="true" style="display: none;"></div>\n' +
        '                                            <div class="btn-group ctrls dropdown-primary">' +
        '                                               <a class="btn-mini dropdown-toggle" data-toggle="dropdown">' +
        '                                                   <i class="sli-icon-options-vertical"></i>' +
        '                                               </a>\n' +
        '                                                <ul class="pull-right dropdown-menu">' +
        '                                                   <li class="dropdown-item" onclick="addARequest(this)"><a><i class="fa fa-plus"></i><span class="gwt-InlineHTML">Add a request</span></a></li>' +
        '                                                   <li class="dropdown-item" onclick="showCreateDirectory(\''+item.id+'\',\'Create a directory\')"><a><i class="fa fa-plus"></i><span class="gwt-InlineHTML">Add a directory</span></a></li>' +
        '                                                   <li class="dropdown-item" onclick="renameDirectory(\''+item.id+'\',\'Rename a directory\')"><a><i class="fa fa-edit"></i><span class="gwt-InlineHTML">Rename</span></a></li>' +
        '                                                   <li class="dropdown-item" onclick="removeDirectory(\''+item.id+'\')"><a><i class="fa fa-trash-o"></i><span class="gwt-InlineHTML">Trash</span></a></li>' +
        '                                               </ul>\n' +
        '                                            </div>\n' +
        '                                        </div><ul id="directory-id-'+item.id+'" style="margin-left: 24px;"></ul></ul></li>');
}

function buildApiDirectory(directoryList,dirId,collapsed){
    $.each(directoryList,function (index,item) {

        if (!dirId && !item.parentId){
            $(".authenticated").append(buildApiDirectoryDom(item,collapsed));
            buildApiDirectory(directoryList,item.id,collapsed);
            return;
        }

        if (item.parentId != dirId){
            return;
        }
        $("#directory-id-"+dirId).append(buildApiDirectoryDom(item,collapsed));
        buildApiDirectory(directoryList,item.id,collapsed);
    })
}

function buildApiTree(directoryList,apiList,collapsed) {
    $(".authenticated").html("");
    $("#repository .api-counter").text("["+apiList.length+"]");

    buildApiDirectory(directoryList,null,collapsed);

    $.each(apiList,function(index,item){
        $("#directory-id-"+item.directoryId).append(buildApiDom(item));
    });

}

function loadApiList(isDb,callback) {

    //拉取目录信息
    $.getJSON(loadApiListUrl+"?isDb="+(isDb?true:false),function (data) {
        data = unpackResult(data);
        gdata.apiList = data.data;

        $.getJSON(directoryListUrl,function (data) {
            data = unpackResult(data);
            gdata.directoryList = data.data;

            buildApiTree(gdata.directoryList,gdata.apiList,"collapsed");
            loadCurrApi();
            if (callback){
                callback();
            }
        })

    })




}

function unpackResult(data){
    if (data.unpack){
        return data;
    }

    $.each(data,function (key,value) {
        if ($.isPlainObject(value)){
            data = unpackResult(value);
            return false;
        }
    })
    return data;
}

function collapsedTree(fa) {
    if($(fa).hasClass("fa-caret-right")){
        $(fa).removeClass("fa-caret-right");
        $(fa).addClass("fa-caret-down");
        $(fa).parent().parent().removeClass("collapsed");
    }else {
        $(fa).removeClass("fa-caret-down");
        $(fa).addClass("fa-caret-right");
        $(fa).parent().parent().addClass("collapsed");
    }
}

function cancelDialog(e) {
    $(e).hide();
}

function newRequest() {
    newEditor();
    newExample();
    showEditorPanel();
    cancelDiff();
    loadApiHistory(null,1);
}

function newEditor() {
    //clean editor
    let form = "#editor-section";
    history.pushState(null,null,baseUrl);
    $(form).find(".api-info-id").val("");
    $(form).find(".api-info-method").val("GET");

    $(form).find(".api-info-path").val("");
    $(form).find(".api-info-datasource").val($(form).find(".api-info-datasource").attr("default-value"));
    $(form).find(".api-info-directory").val("");
    $(form).find(".api-info-editor").val("admin");
    $(form).find(".api-info-comment").val("Request");
    editorTextarea.setValue("");

    removeAllQueryParameterForm(form);

    //css
    $(form).find('.draft-ribbon-text').text("NEW");
    $(form).find(".api-info-method").removeAttr("readonly").parent().removeClass("disabled");
    $(form).find(".api-info-path").removeAttr("readonly");
    $(form).find(".api-info-datasource").removeAttr("readonly");
    $(form).find(".api-info-datasource").parent().removeClass("disabled");

    $("#editor-section .draft-ribbon").show();
    $("#example-section .draft-ribbon").show();

    hasConsole = null;
    currApiInfo = {};
    buildApiOptionsDom()
}

function newExample() {
    //clean example
    let form = "#example-section";
    $(form).find(".example-method").val("GET");
    $(form).find(".example-url").val(buildDefaultUrl("")).blur();
    $(form).find(".headers-form-block").html("");
    exampleTextarea.setValue("");
    $("#response").hide();
    currExample = {};
    hasResponse = null;
}




//--------------------------------example start -----------------------------------
function buildDefaultUrl(path) {
    let defaultUrl = baseUrl.substring(0,baseUrl.lastIndexOf("/"));
    return defaultUrl+(path.indexOf("TEMP-") == 0?"":path);
}

function loadExampleById(exampleId) {
    let example = null;
    for(let i=0;i<gdata.exampleHistoryList.length;i++){
        if (gdata.exampleHistoryList[i].id == exampleId){
            example = gdata.exampleHistoryList[i];
            break;
        }
    }
    loadExample(currApiInfo,example);
}

function loadExample(apiInfo,example) {


    let $form = $("#example-section");
    $form.find(".save-example-btn .changes-indicator").remove();

    //------构建example
    currExample = $.extend({
        apiInfoId:apiInfo.id,
        url:buildDefaultUrl(apiInfo.fullPath),
        method:apiInfo.method,
        requestHeader:"{}",
        requestBody:"",
        responseHeader:"{}",
        responseBody:"",
        status:200,
        elapsedTime:0,
        options:"{}"
    },example);

    currExample.method = apiInfo.method;
    $form.find(".example-method").val(currExample.method);
    let url = buildDefaultUrl(apiInfo.fullPath);
    if(currExample.url.indexOf("?") !=-1){
        url += currExample.url.substring(currExample.url.indexOf("?"));
    }

    $form.find(".example-url").val(url).blur();

    if (example){
        hasResponse = true;
    }
    //请求header
    setHeaderParams(JSON.parse(currExample.requestHeader));

    switchExampleMethod(currExample.method);

    //请求体
    exampleTextarea.setValue(currExample.requestBody?currExample.requestBody:'{}');
    formatExample();
    //响应状态码
    buildResponseStatus(currExample.status);
    //耗时
    $("#response .el-time").html('<span title="'+currExample.elapsedTime+'ms">Elapsed time: '+currExample.elapsedTime+'ms</span>');
    //响应header
    setResponseHeader(JSON.parse(currExample.responseHeader));
    //响应体
    $("#response #responseBody").text(formatJson(currExample.responseBody));

    if (currPage == 'example'){
        showExamplePanel();
    }else{
        showEditorPanel();
    }

}

function fullPath(target){
    let value = $(target).val();
    if (value.indexOf("/") != 0){
        $(target).val("/"+value);
    }
}

function toNotSave() {
    let $form = $("#example-section");
    $form.find(".save-example-btn .changes-indicator").remove();
    $form.find(".save-example-btn").append('<div class="changes-indicator"></div>');
}

function formatJson(body){
    try{
        body = body.replace(/:s*([0-9]{15,})s*(,?)/g, ': "$1" $2');
        return JSON.stringify(JSON.parse(body), null, "    ")
    }catch (e) {
        return body;
    }
}

function saveExample(id) {
    let apiInfoId = null;

    if (id){
        apiInfoId = id;
    }else{
        apiInfoId = $("#editor-section .api-info-id").val();
    }

    if (apiInfoId == ""){
        openMsgModal("API is not stored ");
        return;
    }
    let params = {
        apiInfoId:apiInfoId,
        url:$("#example-section .example-url").val(),
        method:$("#example-section .example-method").val(),
        requestHeader:JSON.stringify(buildHeaderJson(getHeaderParams())),
        requestBody:buildExampleBodyStr(),
        responseHeader:currExample.responseHeader,
        responseBody:currExample.responseBody,
        status:currExample.status,
        elapsedTime:currExample.elapsedTime,
        options:currExample.options
    }
    showSendNotify("Saveing example")
    $.ajax({
        type: "POST",
        url: saveExampleUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            $("#example-section .save-example-btn .changes-indicator").remove();

            //
            params.id = data.data;
            params.createTime = "now";
            if (gdata.exampleHistoryList){
                gdata.exampleHistoryList.splice(0,0,params);
            }
            let template = buildHistoryItemStr(params);
            $("#history-section .history tbody").prepend(template);
        },complete:function () {
            hideSendNotify();
        }
    });

    let tokenStr = "POSTMAN保存次数";
    _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
}
function requextUrlExample(ableRedirect) {
    let $form = $("#example-section");
    let url = $form.find(".example-url").val();
    requestExample(url,ableRedirect);
}

function buildHeaderJson(headerArrs) {
    let headers = {};
    $.each(headerArrs,function (index,item) {
        let split = item.indexOf(":");
        if (split == -1)return;
        let key = item.substring(0,split);
        let value = item.substring(split + 1);
        headers[key] = value;
    })
    return headers;
}

function requestExample(url,ableRedirect){

    toNotSave();

    let bodyParam = buildExampleBodyStr();
    let headers = buildHeaderJson(getHeaderParams());
    let type = $("#example-section .example-method").val();
    let startTime = new Date().getTime();
    $("#response").show();
    $("#response #responseBody").text("");
    setResponseHeader({});
    showSendNotify("Sending request");
    $.ajax({
        url:url,
        type:type,
        data:bodyParam,
        headers: headers,
        beforeSend: function(request){

        },
        success: function(data){

        },error:function (req,msg,ex) {

        },complete:function (req,data) {
            hideSendNotify();
            let responseHeader = buildHeaderJson(req.getAllResponseHeaders().split("\r\n"));
            let status = req.status;
            let currTime = new Date().getTime();
            let diff = currTime-startTime;
            currExample = {
                apiInfoId:$("#editor-section .api-info-id").val(),
                url:url,
                method:type,
                requestHeader:JSON.stringify(headers),
                requestBody:bodyParam,
                responseHeader:JSON.stringify(responseHeader),
                responseBody:req.responseText,
                status:status,
                elapsedTime:diff,
                options:"{}"
            }

            let redirectUrl = req.getResponseHeader("redirectUrl");
            if (redirectUrl && ableRedirect){
                requestExample(redirectUrl,false);
                return;
            }

            buildResponseStatus(status);

            setResponseHeader(responseHeader)
            $("#response #responseBody").text(formatJson(req.responseText));
            $("#response .el-time").html("<span title=\""+diff+"ms\">Elapsed time: "+diff+"ms</span>")
        }});
}

function buildResponseStatus(status) {
    $("#response .response-status-line").removeClass("response-status-line-failure").removeClass("response-status-line-ok");

    if (status == 200){
        $("#response .response-status-line").addClass("response-status-line-ok");
    }else{
        $("#response .response-status-line").addClass("response-status-line-failure");
    }

    if (status == 0){
        $("#response .response-status-line .status").html("<a >No response </a>");
    }else{
        $("#response .response-status-line .status").html("<a >"+status+" </a>");
    }
}

function setResponseHeader(headers){
    $("#response .headers-form-table>tbody").html("");
    $.each(headers,function (key,value) {
        if (key.length == 0)return;
        $("#response .headers-form-table>tbody").append('<tr>\n' +
            '<td><a><span title="'+key+'">'+key+':</span></a></td>\n' +
            '<td><span class="gwt-InlineHTML" title="'+decodeURIComponent(value)+'">'+decodeURIComponent(value)+'</span></td></tr>')
    })
}

function triggerQueryParameterForm(e) {
    let isCollapsed = $(e).parents(".query-parameters-form").hasClass("collapsed");
    if (isCollapsed){
        $(e).parents(".query-parameters-form").removeClass("collapsed");
        $(e).parents(".query-parameters-form-title").removeClass("collapsed-title");
        $(e).parent().children(".fa").removeClass("fa-caret-right").addClass("fa-caret-down");
        $(e).parent().children(".r-btn-link").show();
    }else{
        $(e).parents(".query-parameters-form").addClass("collapsed");
        $(e).parents(".query-parameters-form-title").addClass("collapsed-title");
        $(e).parent().children(".fa").removeClass("fa-caret-down").addClass("fa-caret-right");
        $(e).parent().children(".r-btn-link").hide();
    }
}

function parseUrlInput(e) {
    let $form = $("#example-section");
    $form.find(".query-parameters-form-block").html("");
    $("#example-section .subtitle-counter").text("")

    let path = $(e).val();
    if (path.indexOf("http") != 0){
        path = "http://"+path;
        $(e).val(path);
    }

    if (path.length == 7){
        $(".uri-validation-error").show();
    }else{
        $(".uri-validation-error").hide();
    }

    if (path.indexOf("?") == -1){
        return;
    }
    let paramList = path.substring(path.indexOf("?")+1).split("&");
    if (!paramList || paramList.length == 0){
        return;
    }

    $.each(paramList,function (index,item) {
        let arr = item.split("=");
        queryParameterAdd(arr[0],arr[1]);
    })
}

function queryParameterRemove(e,form){
    $(e).parents(".query-parameter-row").remove();
    buildUrlInput();
    $(form).find(".subtitle-counter").text("["+$(form).find(".query-parameters-form-block>.active").length+"]");
}

function buildUrlInput() {
    let url = $("#example-section .example-url").val();
    url = url.substring(0,url.indexOf("?") == -1?url.length:url.indexOf("?"));
    $.each($("#example-section .query-parameters-form-block>.active"),function (index,item) {
        let key = $(item).find(".key").val();
        if (key.length == 0){
            return;
        }
        let value = $(item).find(".value").val();
        if (index > 0){
            url += "&";
        }else{
            url += "?";
        }
        url += (key + "=" + value);
    })
    $("#example-section .example-url").val(url);
}

function queryParameterAdd(key,value) {
    key = key?key:"";
    value = value?value:"";
    let $form = $("#example-section");
    $form.find(".query-parameters-form-block").append("<div class=\"query-parameter-row active\" e2e-tag=\"query-parameter\"><span class=\"gwt-CheckBox query-parameter-cell\" title=\"Enable/Disable\" e2e-tag=\"query-parameter-state\"><input type=\"checkbox\" value=\"on\" onclick='urlTriggerEnable(this)'  tabindex=\"0\" checked=\"\"><label for=\"gwt-uid-2020\"></label></span><span class=\"expression-input input-append query-parameter-cell-name query-parameter-cell\"><input type=\"text\" class=\"gwt-TextBox key\" onchange='buildUrlInput()' value='"+key+"' placeholder=\"name\" e2e-tag=\"query-parameter-name\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span><span class=\"gwt-InlineLabel query-parameter-cell\">=</span><span class=\"expression-input input-append query-parameter-cell-value query-parameter-cell\"><input type=\"text\" onchange='buildUrlInput()' class=\"gwt-TextBox value\" value='"+value+"' placeholder=\"value\" e2e-tag=\"query-parameter-value\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span><button class=\"r-btn r-btn-link query-parameter-cell\" onclick='queryParameterRemove(this,\"#example-section\")' title=\"Remove\" e2e-tag=\"query-parameter-remove\"><i class=\"fa fa-times-thin\"></i><span></span><span class=\"r-btn-indicator\" aria-hidden=\"true\" style=\"display: none;\"></span></button><div class=\"btn-group ctrls dropdown-secondary query-parameter-encoding query-parameter-cell\" e2e-tag=\"query-parameter-additional-actions\"><a class=\"btn-mini dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"sli-icon-options-vertical\"></i></a> <ul class=\"pull-right dropdown-menu\"><li class=\"dropdown-item\" e2e-tag=\"query-parameter-encode\"><a><i class=\"fa fa-check\"></i> <span>Encode before sending</span></a></li></ul></div></div>");
    $form.find(".query-parameters-form-block .query-parameter-row .key").focus();
    $("#example-section .subtitle-counter").text("["+$(".query-parameters-form-block>.active").length+"]");
}

function urlTriggerEnable(e) {
    if ($(e).is(':checked')){
        $(e).parents(".query-parameter-row").addClass("active");
        $(e).parents(".query-parameter-row").find(".expression-input input").removeClass("disabled").removeAttr("disabled");
    }else {
        $(e).parents(".query-parameter-row").find(".expression-input input").addClass("disabled").attr("disabled","disabled");
        $(e).parents(".query-parameter-row").removeClass("active");
    }
    buildUrlInput();
    $(e).parents(".query-parameters-form").find(".subtitle-counter").text("["+$(e).parents(".query-parameters-form-block").find(">.active").length+"]");
}

function headerTriggerEnable(e) {
    if ($(e).is(':checked')){
        $(e).parents(".header-row").addClass("active");
        $(e).parents(".header-row").find(".expression-input input").removeClass("disabled").removeAttr("disabled");
    }else {
        $(e).parents(".header-row").find(".expression-input input").addClass("disabled").attr("disabled","disabled");
        $(e).parents(".header-row").removeClass("active");
    }
}

function headRemoveAll() {
    $("#example-section .headers-form-block").html("");
    $("#example-section .mode-raw textarea").val("");
}

function getHeaderParams() {
    let isForm = $("#example-section .headers-form-title .dropdown-toggle").text().trim() == 'Form';
    let headersParams = [];
    if (isForm){
        let headers = $("#example-section .headers-form-block>.active");
        $.each(headers,function (index,item) {
            let key = $(item).find(".key").val();
            let value = $(item).find(".value").val();
            headersParams.push(key+":"+value);
        });
    }else{
        headersParams = $("#example-section .mode-raw textarea").val().split(/[(\r\n)\r\n]+/);
    }
    return headersParams;
}

function setHeaderParams(headersParams) {
    headersParams = $.extend({},rocketUser.setting.header,headersParams);
    let isForm = $("#example-section .headers-form-title .dropdown-toggle").text().trim() == 'Form';
    if (isForm){
        $("#example-section .headers-form-block").html("");
        $.each(headersParams,function (key,value) {
            headerAdd(key,decodeURIComponent(value));
        });
    }else{
        let content = "";
        $.each(headersParams,function (key,value) {
            content +=(key+":"+decodeURIComponent(value)+"\r\n");
        });
        $("#example-section .mode-raw textarea").val(content);
    }
}

function getRawHeaders() {
    let headers = $("#example-section .mode-raw textarea").val().split(/[(\r\n)\r\n]+/);
    let headerNews = [];
    $.each(headers,function (index,item) {
        item = item.trim();
        if (item.length > 0 && item.indexOf(":")>0){
            headerNews.push(item);
        }
    })
    return headerNews;
}

function headerRemove(e){
    $(e).parents(".header-row").remove();
}

function headerAdd(key,value) {
    $("#example-section .headers-form-block").append(buildHeadItem(key,value));
}


function buildHeadItem(key,value) {
    key = key?key:"";
    value = decodeURIComponent(value?value:"");
    return "<div class=\"header-row active\" e2e-tag=\"header\"><span class=\"gwt-CheckBox header-cell\" title=\"Enable/Disable\" e2e-tag=\"header-state\"><input type=\"checkbox\" value=\"on\" onclick='headerTriggerEnable(this)' tabindex=\"0\" checked=\"\"><label for=\"gwt-uid-1412\"></label></span><span class=\"gwt-InlineHTML header-cell-name header-name-ro header-cell\" aria-hidden=\"true\" style=\"display: none;\"><a href=\"http://tools.ietf.org/html/rfc7231#section-3.1.1.5\" target=\"_blank\" class=\"header-link\"><span title=\"Content-Type\">Content-Type</span></a></span><span class=\"expression-input input-append header-cell-name header-cell\"><input type=\"text\" class=\"gwt-TextBox key\" value='"+key+"' placeholder=\"name\" e2e-tag=\"header-name\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span><span class=\"gwt-InlineLabel header-cell\">:</span><span class=\"expression-input input-append header-cell-value header-cell\"><input type=\"text\" class=\"gwt-TextBox value\" value='"+value+"' placeholder=\"value\" e2e-tag=\"header-value\"><span class=\"add-on\" data-original-title=\"\" title=\"\"><i class=\"icon-magic\"></i></span></span>\n" +
        "                                    <button class=\"btn-remove-header r-btn r-btn-link header-cell\" onclick='headerRemove(this)' title=\"Remove\" e2e-tag=\"header-remove\"><i class=\"fa fa-times-thin\"></i><span></span><span class=\"r-btn-indicator\" aria-hidden=\"true\" style=\"display: none;\"></span></button>\n" +
        "                                    <div class=\"header-cell-fixed-action header-cell\">\n" +
        "                                        <button class=\"r-btn r-btn-link hide\" title=\"Edit\" e2e-tag=\"header-edit\"><i class=\"sli-icon-key\"></i><span></span><span class=\"r-btn-indicator\" aria-hidden=\"true\" style=\"display: none;\"></span></button>\n" +
        "                                        <button class=\"header-warning r-btn r-btn-link hide\" title=\"Show warnings\" e2e-tag=\"header-warning\"><i class=\"fa fa-exclamation-triangle\"></i><span></span><span class=\"r-btn-indicator\" aria-hidden=\"true\" style=\"display: none;\"></span>\n" +
        "                                        </button>\n" +
        "                                    </div>\n" +
        "                                </div>";
}

function showHeaderForm(e) {
    let isForm = $("#example-section .headers-form-title .dropdown-toggle").text().trim() == 'Form';
    if (isForm){
        return;
    }

    $("#example-section .mode-form").show();
    $("#example-section .mode-raw").hide();
    $(e).parents(".dropdown").children(".dropdown-toggle").html('<i></i> Form <span class="caret"></span>');
    $("#example-section .headers-form-block>.active").remove();

    let headers = $("#example-section .mode-raw textarea").val().split(/[(\r\n)\r\n]+/);
    let activeHeader = "";
    $.each(headers,function (index,item) {
        let split = item.indexOf(":");
        if (split == -1)return;
        let key = item.substring(0,split);
        let value = item.substring(split + 1);
        activeHeader += buildHeadItem(key,value)
    });
    $("#example-section .headers-form-block").prepend(activeHeader);

}
function showHeaderRaw(e) {
    let isForm = $("#example-section .headers-form-title .dropdown-toggle").text().trim() == 'Form';
    if (!isForm){
        return;
    }

    $("#example-section .mode-form").hide();
    $("#example-section .mode-raw").show();
    $(e).parents(".dropdown").children(".dropdown-toggle").html('<i></i> Raw <span class="caret"></span>');

    let content = "";
    let headers = $("#example-section .headers-form-block>.active");
    $.each(headers,function (index,item) {
        content += $(item).find(".key").val();
        content += ":";
        content += encodeURI($(item).find(".value").val());
        content += "\r\n";
    });
    $("#example-section .mode-raw textarea").val(content);
}

function formatExample() {
    exampleTextarea.setValue(formatJson(exampleTextarea.getValue()));
}

function setModeExample(e,mode) {
    exampleTextarea.setOption("mode",mode);
    $(e).siblings("button").removeClass("selected");
    $(e).addClass("selected");
}
function cleanExample() {
    exampleTextarea.setValue("");
}

function switchExampleMethod(option) {
    let isJsonBody = false;
    if (option == 'POST' || option == 'PUT'){
        isJsonBody = true;
    }
    let isForm = $("#example-section .headers-form-title .dropdown-toggle").text().trim() == 'Form';
    let key = "Content-Type";
    let value = "application/json";

    if (isJsonBody){
        $("#example-section .note").hide();
        $("#example-section .b-container").show();
        if (isForm){
            let exists = false;
            $.each($("#example-section .headers-form-block>.header-row"),function (index,item) {
                if ($(item).find(".key").val() == key){
                    exists = true;
                    $(item).find(".value").val(value);
                }
            });
            if (!exists){
                headerAdd(key,value);
            }

        }else{
            let headers = getRawHeaders();
            let exists = false;
            for (let i=0;i<headers.length;i++){
                let item = headers[i];
                if (item.indexOf(key+":") == 0){
                    headers[i] = key+":"+value;
                    exists = true;
                }
            }
            if (!exists){
                headers.push(key+":"+value);
            }
            $("#example-section .mode-raw textarea").val(headers.join("\r\n"));
        }
    }else{
        $("#example-section .note").show();
        $("#example-section .note .note-method").text(option);
        $("#example-section .b-container").hide();
        if (isForm){
            $.each($("#example-section .headers-form-block input[value='"+key+"']"),function (index,item) {
                if ($(item).val() == key){
                    $(item).parents(".header-row").remove();
                }
            })

        }else {
            let headers = getRawHeaders();
            let headerNews = [];
            $.each(headers,function (index,item) {
                if (item.indexOf(key+":") == 0){
                    return;
                }
                headerNews.push(item);
            })
            $("#example-section .mode-raw textarea").val(headerNews.join("\r\n"));
        }
    }
}

function loadExampleMethodEvent() {
    $("#example-section .uri-method .dropdown-menu>li").on("click",function () {
        switchExampleMethod($(this).text().trim());
    })
}

function showExamplePanel() {
    $("#example-panel").show();
    $("#editor-panel").hide();

    let urlParam = buildUrlParam();
    let url = window.location.href;
    if(urlParam.page){
        url = url.replace("page=editor","page=example");
        history.pushState(null,null,url);
    }
    currPage = "example";
    monaco.editor.setTheme("vs");
}

function buildUrlParam() {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    let urlParam = {};
    for (let i=0;i<vars.length;i++) {
        let pair = vars[i].split("=");
        urlParam[pair[0]] = pair[1];
    }
    return urlParam;
}

function showEditorPanel() {
    $("#example-panel").hide();
    $("#editor-panel").show();

    let urlParam = buildUrlParam();
    let url = window.location.href;
    if(urlParam.page){
        url = url.replace("page=example","page=editor");
        history.pushState(null,null,url);
    }
    currPage = "editor";
    monaco.editor.setTheme(languageTheme);
}
//--------------------------------example end -----------------------------------


//--------------------------------login start -----------------------------------
function hideLoginDialog() {
    $("#loginDialog").hide();
    $("#modal-backdrop").hide();
}

function showLoginDialog() {
    $("#loginDialog .login-error-message").text("");
    $("#loginDialog").show();
    $("#modal-backdrop").show();
}


function logout() {

    showSendNotify("logout ...");
    $.ajax({
        type: "post",
        url: logoutUrl,
        contentType : "application/json",
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            rocketUser.user.username = "";
            rocketUser.user.token = "";
            $.ajaxSetup({
                headers:{"rocket-user-token":""}
            });
            localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
            $("#top-section .login-btn").show();
            $("#top-section .login-info").hide();

            let tokenStr = "登出次数";
            _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])

        },complete:function (req,data) {
            hideSendNotify();
        }
    });
}

function login() {
    let username = $("#loginDialog .username").val();
    let password = $("#loginDialog .password").val();

    showSendNotify("login ...");
    $.ajax({
        type: "post",
        url: loginUrl,
        contentType : "application/json",
        data: JSON.stringify({
            "username":username,
            "password":password
        }),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                $("#loginDialog .login-error-message").text(data.msg);
                return;
            }
            rocketUser.user.username = username;
            rocketUser.user.token = data.data;
            localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
            $.ajaxSetup({
                headers:{"rocket-user-token":data.data}
            });
            $("#top-section .login-btn").hide();
            $("#top-section .login-info").show();
            $("#top-section .login-info .name").text(rocketUser.user.username);
            hideLoginDialog();

            let tokenStr = "登录次数";
            _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])

        },complete:function (req,data) {
            hideSendNotify();
        }
    });
}
//--------------------------------login end -----------------------------------

//--------------------------------example history start -----------------------------------
function showHistory() {
    $("#left-side .history").addClass("active");
    //$("#history-section").show();
    $("#history-api-section").show();

    $("#left-side .repository").removeClass("active");
    $("#repository").hide();
}

function showRepository() {
    $("#left-side .history").removeClass("active");
    //$("#history-section").hide();
    $("#history-api-section").hide();

    $("#left-side .repository").addClass("active");
    $("#repository").show();
}

function loadExampleHistory(apiInfo, isLoadExample) {
    $.getJSON(lastExampleUrl+"?pageNo=1&pageSize=1&apiInfoId="+apiInfo.id,function (data) {
        data = unpackResult(data);
        gdata.exampleHistoryList = data.data;
        if (isLoadExample){
            loadExample(apiInfo,data.data.length>0?data.data[0]:null);
        }
        //buildExampleHistory();
    });

}

function buildExampleHistory(filter) {
    let list = gdata.exampleHistoryList;

    let $form = $("#history-section");
    $form.find(".history tbody").html("");
    $.each(list,function (index,item) {
        if (filter && (
            item.url.indexOf(filter) == -1
            && item.editor.indexOf(filter) == -1
            && item.method.indexOf(filter) == -1
            && item.status != filter
        )){
            return;
        }
        let template = buildHistoryItemStr(item);
        $form.find(".history tbody").append(template);
    })
}

function buildHistoryItemStr(item){
    return '<tr><td>' +
        '<div class="item">' +
        '   <label class="checkbox selector" >' +
        '   <input type="checkbox" value="on" onclick="selectExampleItem()" data-id="'+item.id+'" tabindex="0"><span></span>' +
        '   </label> ' +
        '   <div class="method method-'+(item.method.toLowerCase())+'" title="GET">'+item.method+'</div> ' +
        '   <div class="item-body"> ' +
        '       <div> ' +
        '           <a href="javascript:;" onclick="loadExampleById(\''+item.id+'\')" class="btn path btn-link" title="'+item.url+'"><i></i> '+item.url+' </a> ' +
        '       </div> ' +
        '       <div class="count">'+item.editor+'</div> ' +
        '       <div class="el-time" title="'+item.time+'ms">'+item.time+'ms</div> ' +
        '       <div class="el-time" title="'+item.createTime+'">'+item.createTime+'</div> ' +
        '       <div class="status response-ok" title="'+item.status+'">'+item.status+'</div> ' +
        '</div></div></td></tr>';
}

function selectExampleItem() {

    let num = $("#history-section .history tbody .selector>input:checked").length;

    if (num == 0){
        $("#history-section .ellipsis").addClass("disabled");
    }else{
        $("#history-section .ellipsis").removeClass("disabled");
    }
}

function exampleDeselect() {
    $("#history-section .history tbody .selector>input:checked").removeAttr("checked")
    selectExampleItem();
}

function exampleRemoveSelect() {
    exampleRemove($("#history-section .history tbody .selector>input:checked"))
}

function exampleRemoveAll() {
    exampleRemove($("#history-section .history tbody .selector>input"));
}

function exampleRemove(exList) {

    let apiExampleList = [];
    $.each(exList,function (index,item) {
        apiExampleList.push({"id":$(item).attr("data-id")});
    })

    openConfirmModal("The request will be permanently deleted. Are you sure?",function () {
        if (apiExampleList.length == 0){
            closeConfirmModal();
            return;
        };
        showSendNotify("Removeing example");
        $.ajax({
            type: "delete",
            url: deleteExampleUrl,
            contentType : "application/json",
            data: JSON.stringify({"apiExampleList":apiExampleList}),
            success: function (data) {
                closeConfirmModal();
                if (data.code !=200){
                    openMsgModal(data.msg);
                    return;
                }
                loadExampleHistory(currApiInfo,false);
                selectExampleItem();
            },complete:function () {
                hideSendNotify();
            }
        });
    });
}

function searchExample(e) {
    buildExampleHistory($(e).val())
}

//--------------------------------example history end -----------------------------------

//--------------------------------api history start -----------------------------------

function loadApiHistory(apiInfoId,pageNo) {
    if (!apiInfoId){
        apiInfoId = "";
    }
    gdata.historyCurrPageNo = pageNo;
    $.getJSON(lastApiUrl+"?pageSize=30&pageNo="+pageNo+"&apiInfoId="+apiInfoId,function (data) {
        data = unpackResult(data);
        if (pageNo == 1){
            gdata.apiHistoryList = [];
            $("#history-api-section .history tbody").html("");
        }
        $.each(data.data,function (index,item) {
            gdata.apiHistoryList.push(item);
        })
        buildApiHistory(data.data,null);
    });

}

function buildApiHistory(list,filter) {
    let $form = $("#history-api-section");
    $.each(list,function (index,item) {
        if (filter && (
            item.path.indexOf(filter) == -1
            && item.editor.indexOf(filter) == -1
            && item.method.indexOf(filter) == -1
            && item.datasource.indexOf(filter) == -1
        )){
            return;
        }
        let template = buildApiHistoryItemStr(item);
        $form.find(".history tbody").append(template);
    })
}

function buildApiHistoryItemStr(item){
    return '<tr><td>' +
        '<div class="item">' +
        '   <label class="checkbox selector" >' +
        '   <input type="checkbox" value="on" style="display: none" data-id="'+item.id+'" tabindex="0"><span></span>' +
        '   </label> ' +
        '   <div class="method method-'+(item.method.toLowerCase())+'" title="GET">'+item.method+'</div> ' +
        '   <div class="item-body"> ' +
        '       <div> ' +
        '           <a href="javascript:;" onclick="loadDetailByHistoryId(\''+item.id+'\',\'#editor-section\')" class="btn path btn-link" title="'+item.path+'"><i></i> '+item.path+' </a> ' +
        '       </div> ' +
        '       <div class="el-time" title="'+item.createTime+'">'+item.createTime+'</div> ' +
        '       <div class="count">'+item.editor+'</div> ' +
        '       <div class="el-time"><a onclick="showDiff(\''+item.id+'\')">Show Diff</a></div> ' +
        '       <div class="status response-ok" title="'+item.datasource+'">'+item.datasource+'</div> ' +
        '</div></div></td></tr>';
}

function searchApiHistory(e) {
    $("#history-api-section .history tbody").html("");
    buildApiHistory(gdata.apiHistoryList,$(e).val())
}

function showDiff(id) {
    let apiHistory = null;
    for(let i=0;i<gdata.apiHistoryList.length;i++){
        if (gdata.apiHistoryList[i].id == id){
            apiHistory = jQuery.extend(true, {}, gdata.apiHistoryList[i]);
            break;
        }
    }

    $("#diff-left-id").html(apiHistory.id);
    $("#diff-left-time").html(apiHistory.createTime);

    loadDetailById(apiHistory.apiInfoId,"#editor-section",function () {
        $("#editor-section .diff-body").show();
        $("#editor-section .code-body").hide();
        $("#diff-editor").html("");
        originalModel = monaco.editor.createModel(apiHistory.script, languageName);
        modifiedModel = monaco.editor.createModel(editorTextarea.getValue(), languageName);
        let diffEditor = monaco.editor.createDiffEditor(document.getElementById("diff-editor"), {
            // You can optionally disable the resizing
            scrollBeyondLastLine:false,
            automaticLayout: true,
            enableSplitViewResizing: false
        });
        diffEditor.setModel({
            original: originalModel,
            modified: modifiedModel
        });
    })

}

function acceptLeft() {
    modifiedModel.setValue(originalModel.getValue());
}
function confirmDiff() {
    cancelDiff();
    editorTextarea.setValue(modifiedModel.getValue());

    let tokenStr = "版本比对确认次数";
    _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
}
function cancelDiff() {
    $("#editor-section .diff-body").hide();
    $("#editor-section .code-body").show();
}
//--------------------------------api history end -----------------------------------


//--------------------------------api left-side start -----------------------------------
function loadLeftSideEvent(){
    $(".h-splitter").click(function () {
        $(this).hide();
        $("#left-side").show();
        let leftPx = rocketUser.panel.leftWidth;
        $("#left-side").css("width",leftPx);
        $(".content-view").css("left",leftPx);
        $(".h-divider").css("left",leftPx);
        rocketUser.panel.left = "show";
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    });

    $(".hide-pane-l").click(function () {
        $(".h-splitter").show();
        $("#left-side").hide();
        $(".content-view").css("left",10);
        $(".h-divider").css("left",0);
        rocketUser.panel.left = "hide";
        rocketUser.panel.leftWidth = $("#left-side").css("width");
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    });

    let dividerIsDown = false;
    $(".h-divider").on("mousedown",function () {
        dividerIsDown = true;
        document.onselectstart=new Function("event.returnValue=false;");
    })

    $(document).on("mouseup",function () {
        dividerIsDown = false;
        document.onselectstart=new Function("event.returnValue=true;");
    })

    $(document).on("mousemove",function (e) {
        if (!dividerIsDown)return;
        let x = e.pageX;
        if (x < 325){
            $(".h-splitter").show();
            $("#left-side").hide();
            $(".content-view").css("left",10);
            $(".h-divider").css("left",0);
            rocketUser.panel.leftWidth = "325px";
            rocketUser.panel.left = "hide";
        }else{
            $(".h-splitter").hide();
            $("#left-side").show();
            $(".content-view").css("left",x);
            $(".h-divider").css("left",x);
            $("#left-side").css("width",x);
            rocketUser.panel.left = "show";
            rocketUser.panel.leftWidth = x +"px";
        }

        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    });

}

function loadBottomSideEvent() {
    $(".v-splitter").click(function () {
        $(".v-splitter").hide();
        $("#bottom-side").show();
        let bottom = rocketUser.panel.bottomHeight;
        $("#bottom-side").css("height",bottom);
        $("#editor-panel .ui-lay-c").css("bottom",bottom);
        $(".v-divider").show().css("bottom",Number(bottom.replace("px",""))+7+"px");
        rocketUser.panel.bottom = "show";
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    });

    $(".bottom-down").click(function () {
        $(".v-splitter").show();
        $("#bottom-side").hide();
        $(".v-divider").hide();
        $("#editor-panel .ui-lay-c").css("bottom",0);
        rocketUser.panel.bottom = "hide";
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
    });

    let dividerVIsDown = false;
    $(".v-divider").on("mousedown",function () {
        dividerVIsDown = true;
        document.onselectstart=new Function("event.returnValue=false;");
    })

    $(document).on("mouseup",function () {
        dividerVIsDown = false;
        document.onselectstart=new Function("event.returnValue=true;");
    })

    $(document).on("mousemove",function (e) {
        if (!dividerVIsDown)return;
        let y = e.pageY;
        let bottom = $(document).height()-y;
        if (bottom <= 150){
            bottom = 0;
            $(".v-splitter").show();
            $("#bottom-side").hide();
            $(".v-divider").hide();
            rocketUser.panel.bottom = "hide";
            rocketUser.panel.bottomHeight = "150px";
        }else{
            $(".v-splitter").hide();
            $("#bottom-side").show();
            $(".v-divider").show();
            rocketUser.panel.bottom = "show";
            rocketUser.panel.bottomHeight = bottom+"px";
        }

        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
        $("#editor-panel .ui-lay-c").css("bottom",bottom);
        $(".v-divider").css("bottom",bottom+7);
        $("#bottom-side").css("height",bottom);
    });
}

function showBottomTab(target,e) {
    $("#bottom-side .bottom-pane-selector>li").removeClass("active");
    $(e).addClass("active");
    $("#bottom-side .bottom-tab>div").hide();
    $(target).show();
}

//--------------------------------api left-side end -----------------------------------

//-------------------------------- global setting start -------------------------------
function showGlobalConfig() {
    $("#global-setting").show();
    $("#global-setting .modal-body").html("");
    settingTextarea = monaco.editor.create($("#global-setting .modal-body")[0], {
        language: 'json',
        value:formatJson(JSON.stringify(rocketUser.setting)),
        wordWrap: 'on',  //自行换行
        verticalHasArrows: true,
        horizontalHasArrows: true,
        scrollBeyondLastLine: false,
        contextmenu:false,
        automaticLayout: true,
        fontSize:13,
        minimap: {
            enabled: false // 关闭小地图
        }

    });
}
function hideGlobalConfig() {
    $("#global-setting").hide();
}

function saveGlobalConfig() {
    try {
        rocketUser.setting = JSON.parse(settingTextarea.getValue());
        localStorage.setItem("rocketUser",JSON.stringify(rocketUser));
        hideGlobalConfig();
    }catch (e) {
        openMsgModal(e);
    }

}
//-------------------------------- global setting end -------------------------------

//-------------------------------- datasource setting start -----------------------
function showDataSourceConfig(){
    $("#datasource-setting").show();
    showSendNotify("load Setting")
    $.ajax({
        type: "GET",
        url: getDBDriverListUrl,
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            gdata.driverList = data.data;
            fillDriverSelect(data.data);
            loadDbConfigList()
        },complete:function () {
            hideSendNotify();
        }
    });
}


function hideDataSourceConfig(){
    $("#datasource-setting").hide();
}



function loadDbConfigList(selectDBId) {
    $.ajax({
        type: "GET",
        url: getDBConfigListUrl,
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            gdata.dbList = data.data;
            fillDBList(data.data,selectDBId);
        },complete:function () {
            hideSendNotify();
        }
    });
}

function fillDBList(dbList,selectDBId) {
    $(".db-list").html('');


    $.each(dbList,function(index,item){
        let driverObj = getDriver(item.driver);
        $(".db-list").append('<div data="'+item.id+'" onclick="selectDB(this)"><img src="'+driverObj.icon+'" >'+item.name+'</div>')
    })

    if (dbList.length == 0){
        $(".db-list").html('<div><span style="color:gray;cursor: auto;">Empty</span></div>');
    }else{
        let selectedTarget = null;
        if (!selectDBId){
            selectedTarget = $(".db-list>div:first");
        }else{
            selectedTarget = $(".db-list>div[data='"+selectDBId+"']");
        }
        selectDB(selectedTarget)
    }

}

function selectDB(target) {
    $(".db-list>div").removeClass("db-selected")
    $(target).addClass("db-selected");
    let dbId = $(target).attr("data");
    //show db details
    $(".db-right>div").show();

    let dbObj = getDB(dbId);
    let driver = getDriver(dbObj.driver);
    $(".db-right input[name='id']").val(dbObj.id);
    $(".db-right input[name='name']").val(dbObj.name);
    $(".db-right input[name='comment']").val(dbObj.comment);
    $(".db-right input[name='user']").val(dbObj.user);
    $(".db-right input[name='password']").val(dbObj.password);
    $(".db-right input[name='url']").val(dbObj.url);
    $(".db-right input[name='driver']").val(dbObj.driver);
    $(".db-right .db-driver-info").attr("title",dbObj.driver);

    $(".db-right .db-driver-icon>img").attr("src",driver.icon);
    $(".db-right .db-driver-info").text(driver.name);

    testConnectStatus();

    //填充options
    $(".db-right .db-properties").html("")
    if (dbObj.properties){
        $.each(dbObj.properties,function (key,value){
            generateDBLine(key,value)
        })
    }
    generateDBLine('','')
}

function generateDBLine(key,value) {
    let domStr = '<div class="db-properties-line">\n' +
        '                    <div class="controls" >\n' +
        '                        <input type="text" class="gwt-TextBox input-xlarge" onblur="checkLine(this)" name="key" value="'+key+'" >\n' +
        '                    </div>\n' +
        '                    <div class="controls">\n' +
        '                        <input type="text" class="gwt-TextBox input-xlarge" onblur="checkLine(this)" name="value" value="'+value+'" >\n' +
        '                    </div>\n' +
        '                </div>';
    $(".db-right .db-properties").append(domStr);
}

function checkLine(target) {
    let lineDom = $(target).parents(".db-properties-line");
    let key = lineDom.find("input[name='key']").val().trim();
    let value = lineDom.find("input[name='value']").val().trim();
    if (key != '' || value != ''){
        lineDom.addClass("has-value");
    }else{
        lineDom.removeClass("has-value");

        //移除非最后一个元素
        if (lineDom.next().length > 0){
            lineDom.remove();
        }

    }
    if ($(".db-properties .db-properties-line:last").hasClass("has-value")){
        generateDBLine('','')
    }
}

function testConnectStatus(status){
    let loadClass = "fa-spinner fa-spin";
    let okClass = "fa-check";
    let errClass = "fa-times";
    $(".db-connection-status").removeClass(loadClass);
    $(".db-connection-status").removeClass(okClass);
    $(".db-connection-status").removeClass(errClass);
    if (!status){
        return;
    }
    switch (status){
        case "none":break;
        case "load":$(".db-connection-status").addClass(loadClass);break;
        case "ok":$(".db-connection-status").addClass(okClass);break;
        case "err":$(".db-connection-status").addClass(errClass);break;
    }

}

function testConnect() {

    testConnectStatus("load");

    let params = buildDBConfigParams();
    $.ajax({
        type: "POST",
        url: testConnectUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                testConnectStatus("err");
                openMsgModal(data.msg);
                return;
            }
            testConnectStatus("ok");
        },complete:function () {
            hideSendNotify();
        }
    });
}

function buildDBConfigParams(){
    let dbRight = $(".db-right");
    let params = {
        "id": dbRight.find("input[name='id']").val(),
        "name": dbRight.find("input[name='name']").val(),
        "comment":dbRight.find("input[name='comment']").val(),
        "user":dbRight.find("input[name='user']").val(),
        "password":dbRight.find("input[name='password']").val(),
        "url":dbRight.find("input[name='url']").val(),
        "driver":dbRight.find("input[name='driver']").val()
    }
    let properties = {};
    params["properties"] = properties;

    let lines = dbRight.find(".db-properties-line");
    $.each(lines,function (index,item){
        let key = $(item).find("input[name='key']").val().trim();
        let value = $(item).find("input[name='value']").val().trim();
        if (key == ''){
            return;
        }
        properties[key] = value;
    })

    return params;
}

function getDB(dbId) {
    let dbObj = null;
    $.each(gdata.dbList,function (index,item) {
        if (item.id == dbId){
            dbObj = item;
            return false;
        }
    })
    return dbObj;
}

function getDriver(driver){
    let driverObj = {};
    $.each(gdata.driverList,function (index,item) {
        if (item.driver == driver){
            driverObj = item;
            return false;
        }
    })
    return driverObj;
}

function fillDriverSelect(data) {
    $(".db-driver").html("")
    $.each(data,function(index,item){
        $(".db-driver").append('<li class="dropdown-item" onclick="addNewDB(this)" data="'+item.driver+'"><a><img src="'+item.icon+'" ><span class="gwt-InlineHTML">'+item.name+'</span></a></li>')
    })
    $(".db-driver").append('<li class="dropdown-item" style="border-top: 1px solid #e0dada;"><a target="_blank" href="https://alenfive.gitbook.io/rocket-api/zi-ding-yi-kuo-zhan/zi-ding-yi-dong-tai-shu-ju-yuan-qu-dong-kuo-zhan">自定义驱动</a></li>')


}

function addNewDB(target) {
    let driver = $(target).attr("data");
    let driverObj = getDriver(driver);


    let params = {
        "driver":driver,
        "name":generateDBName("@localhost"),
        "url": driverObj.format,
        "enabled":false
    };

    saveDB(params)

}

function saveDB(params,closeDialog) {
    $.ajax({
        type: "POST",
        url: saveDBConfigUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }

            if (params.id){
                let tokenStr = "数据源保存次数";
                _czc.push(["_trackEvent",tokenStr,params.driver,params.name,"1",tokenStr])
            }else{
                let tokenStr = "数据源新增次数";
                _czc.push(["_trackEvent",tokenStr,params.driver,params.name,"1",tokenStr])
            }

            if (closeDialog){
                hideDataSourceConfig();
            }else{
                loadDbConfigList(data.data);
            }
        },complete:function () {
            hideSendNotify();
        }
    });
}

function removeDB() {
    let id = $(".db-selected").attr("data");
    if (!id){
        return;
    }
    let params = {
        "id":id
    }
    $.ajax({
        type: "DELETE",
        url: removeDBConfigUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            loadDbConfigList();
        },complete:function () {
            hideSendNotify();
        }
    });
}

function generateDBName(name){

    let reg = new RegExp("( \[[0-9]+\])$","g")
    name = name.replace(reg,"");

    let startStr = name +" [";
    let endStr = "]";
    let nameNum = null;
    $.each(gdata.dbList,function (index,item){

        if(!item.name.startsWith(startStr) || !item.name.endsWith(endStr)){
            return;
        }
        let num = item.name.substring(startStr.length,item.name.length-1);

        try {
            num = parseInt(num);
        }catch (e) {
            return;
        }
        if (nameNum < num){
            nameNum = num;
        }
    })

    name = nameNum?(name+" ["+(nameNum+1)+"]"):(name+" [1]")
    return name;
}

function copyDB(){
    let id = $(".db-selected").attr("data");
    if (!id){
        return;
    }
    let targetDB = null;
    $.each(gdata.dbList,function (index,item){
        if (item.id == id){
            targetDB = item;
            return false;
        }
    })
    let newDB = jQuery.extend({}, targetDB);
    newDB.id = null;
    newDB.name = generateDBName(targetDB.name);
    saveDB(newDB)
}

function saveDataSourceConfig(closeDialog) {
    let params = buildDBConfigParams();
    params.enabled = true;
    saveDB(params,closeDialog);

}

//-------------------------------- datasource setting end -------------------------

//-------------------------------- yml setting start -------------------------------
function showYmlConfig() {

    showSendNotify("load Setting")
    $.ajax({
        type: "GET",
        url: getApiConfigUrl,
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            showYmlConfigView(data.data);
        },complete:function () {
            hideSendNotify();
        }
    });
}

function showYmlConfigView(data) {
    $("#yml-setting").show();
    $("#yml-setting .modal-body").html("");
    apiSettingTextarea = monaco.editor.create($("#yml-setting .modal-body")[0], {
        language: 'yaml',
        value:data&&data.configContext?data.configContext:"#1.等价于springboot application.yml,yaml格式 优先级最高\n#2.可以在脚本中使用env.get(\"config.hello\")获取配置项\n#3.当前配置文件中的描述内容只在首次未配置时显示，注意删除他们\n\nconfig.hello: word",
        /*verticalHasArrows: true,
        horizontalHasArrows: true,*/
        scrollBeyondLastLine: false,
        /*contextmenu:false,*/
        automaticLayout: true,
        fontSize:13,
        minimap: {
            enabled: false // 关闭小地图
        }
    });
}

function hideYmlConfig() {
    $("#yml-setting").hide();
}

function saveYmlGlobalConfig() {
    showSendNotify("Saving Setting")
    $.ajax({
        type: "post",
        url: saveApiConfigUrl,
        contentType : "application/json",
        data: apiSettingTextarea.getValue(),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            hideYmlConfig();

            let tokenStr = "YML保存次数";
            _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])

        },complete:function () {
            hideSendNotify();
        }
    });
}
//-------------------------------- yml setting end -------------------------------

//-------------------------------- api push end -------------------------------
function apiPush(apiInfoId) {
    showSendNotify("Pushing Doc")
    $.ajax({
        type: "GET",
        url: apiDocPushUrl,
        data:{ "apiInfoId": apiInfoId },
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            $("#repository>.buttons>div").text(data.data);
        },complete:function () {
            hideSendNotify();
        }
    });
}
//-------------------------------- api push end -------------------------------

//-------------------------------- api push start -------------------------------
function initCompletionItems() {
    $.get(completionItemsUrl,function (data) {
        data = unpackResult(data);
        gdata.completionItems = data.data;
    })
}
function buildMethodsForClazz(clazz) {
    $.ajax({
        type: "post",
        url: completionClazzUrl,
        contentType : "application/json",
        data: JSON.stringify({"clazz":clazz}),
        success: function (data) {
            data = unpackResult(data);
            gdata.completionItems.clazzs[clazz] = data.data;
        }
    });
}
//-------------------------------- api push end -------------------------------



//-------------------------------- export start ------------------------------
function showExport() {
    $("#export-dialog").show();
    buildSelectApiTree("#export-dialog",gdata.directoryList,gdata.apiList,"collapsed")
    $("#export-dialog .filename").focus();
}
function hideExport(){
    $("#export-dialog").hide();
}
function exportApiInfo(){

    let fileName = $("#export-dialog .filename").val();

    if (!fileName){
        $("#export-dialog .error-message").text("filename is empty")
        return;
    }

    let apiInfoIds = $("#export-dialog .level2 input:checkbox:checked");

    if (apiInfoIds.length == 0){
        $("#export-dialog .error-message").text("export is empty");
        return;
    }

    let idsParams = [];
    $.each(apiInfoIds,function () {
        idsParams.push($(this).val());
    })

    $("#export-dialog .subform").attr("action",exportUrl);
    $("#export-dialog input[name='apiInfoIds']").val(idsParams.join(","));
    $("#export-dialog input[name='token']").val(rocketUser.user.token);
    $("#export-dialog .subform").submit();
    hideExport();

    let tokenStr = "API导出次数";
    _czc.push(["_trackEvent",tokenStr,fileName,tokenStr,apiInfoIds.length,tokenStr])
}
//-------------------------------- export end ------------------------------

//-------------------------------- import start ------------------------------
function showImport(){
    $("#import-dialog").show();
}
function hideImport() {
    $("#import-dialog").hide();
}
function importApi() {

    $("#import-dialog .error-message").text("In process of import ... ")
    $("#import-form").ajaxSubmit({
        url: importUrl,
        type:"POST",
        dataType:"json",
        success:function (data) {
            data = unpackResult(data);

            let tokenStr = "API导入次数";
            _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])

            if (data.code !=200){
                $("#import-dialog .error-message").text("import error:"+data.msg)
                return;
            }

            $("#import-dialog .error-message").text("import successful size:"+data.data)

            loadApiList(false,function(){

            });
        }
    });

}
//-------------------------------- import end ------------------------------

//-------------------------------- Remote Sync start ------------------------------
function showRemoteSync() {
    $("#remote-sync").show();
    buildSelectApiTree("#remote-sync",gdata.directoryList,gdata.apiList,"collapsed")
}

function hideRemoteSync() {
    $("#remote-sync").hide();
}

function remoteSync(increment) {
    let apiInfoIds = [];
    if (increment){
        $.each($("#remote-sync .level2 input:checkbox:checked"),function () {
            apiInfoIds.push($(this).val())
        })
    }

    let params = {
        "increment":increment,
        "remoteUrl":$("#remote-sync .remote-url").val(),
        "apiInfoIds":apiInfoIds,
        "secretKey":$("#remote-sync .secret-key").val()
    }
    showSendNotify("Remote release")
    $.ajax({
        type: "post",
        url: remoteSyncUrl,
        contentType : "application/json",
        data: JSON.stringify(params),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }

            let tokenStr = "远程发布次数";
            _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])

            $("#remote-sync .error-message").text("Remote release successful size:"+data.data)
        },complete:function () {
            hideSendNotify();
        }
    });
}

function searchSelectApi(target,e) {
    let keyword = $(e).val().trim();

    let searchResult = searchKeyword(keyword);
    $(target + " .items-count").text("0 item selected");

    buildSelectApiTree(target,searchResult.dirList,searchResult.apiList,keyword?"":"collapsed");
}

function searchChildDirOrApi(directoryMap,apiMap,directoryId){
    $.each(gdata.directoryList,function (index, item) {
          if (item.parentId == directoryId){
              if (!directoryMap.get(item.id)){
                  directoryMap.set(item.id,item);
              }
              searchChildDirOrApi(directoryMap,apiMap,item.id)
          }
    })

    $.each(gdata.apiList,function (index,item){
        if (item.directoryId == directoryId && !apiMap.get(item.id)){
            apiMap.set(item.id,item);
        }
    })
}

/**
 * suffixPath 后缀路径，用于向上传递路径，并对目录中的path是否匹配，标记选中颜色
 * @param directoryMap
 * @param directoryId
 * @param suffixPath
 */
function searchParentDir(directoryMap,directoryId,pathFullMatch){
    $.each(gdata.directoryList,function (index,item) {
        if (item.id == directoryId){

            if (directoryMap.get(item.id)){
                return;
            }
            let newItem = $.extend({},item);
            if (pathFullMatch){
                newItem.path = newItem.path.replace(newItem.path,'<span class="s_match">'+newItem.path+'</span>')
            }

            directoryMap.set(newItem.id,newItem);

            if (newItem.parentId){
                searchParentDir(directoryMap,newItem.parentId,pathFullMatch)
            }

        }
    })
}

function buildApiSelectDirectoryDom(directory,collapsed) {
    return $('    <li class="level1 api" style="display: block;">\n' +
        '        <div class="tree-entry">\n' +
        '            <label class="checkbox">\n' +
        '            <input type="checkbox" value="on" tabindex="0">\n' +
        '            </label>\n' +
        '            <a href="javascript:;"  class="btn btn-link name">\n' +
        '                <i class="'+(collapsed?'icon-caret-right':'icon-caret-down')+'"></i>\n' +
        '                <i class="node-icon api-tester-icon api-tester-project"></i>\n' +
        '                <span class="gwt-InlineHTML node-text" >'+directory.name+'</span>\n' +
        '            </a>\n' +
        '        </div>\n' +
        '<ul style="'+(collapsed?'display: none;':'display: block;')+'" class="directory-select-id-'+directory.id+'"></ul>' +
        '    </li>');
}

function buildSelectApiDirectory(target,directoryList,dirId,collapsed){
    $.each(directoryList,function (index,item) {

        if (!dirId && !item.parentId){
            $(target + " .api-list-body").append(buildApiSelectDirectoryDom(item,collapsed));
            buildSelectApiDirectory(target,directoryList,item.id,collapsed);
            return;
        }

        if (item.parentId != dirId){
            return;
        }
        $(target + " .directory-select-id-"+dirId).append(buildApiSelectDirectoryDom(item,collapsed));
        buildSelectApiDirectory(target,directoryList,item.id,collapsed);
    })
}

function buildSelectApiTree(target,dirList,apiList,collapsed) {

    $(target +" .api-list-body").html("");
    //生成tree
    buildSelectApiDirectory(target,dirList,null,collapsed);

    $.each(apiList,function (index,item) {
        let _children = $(target + " .directory-select-id-"+item.directoryId);
        _children.append('  <li class="level2 request">\n' +
            '                <div class="tree-entry">\n' +
            '                    <label class="checkbox" >\n' +
            '                        <input type="checkbox" value="'+item.id+'" tabindex="0">\n' +
            '                    </label>\n' +
            '                    <a href="javascript:;" class="btn btn-link name"><i></i>\n' +
            '                        <i class="node-icon api-tester-icon api-tester-request"></i>\n' +
            '                        <span class="gwt-InlineHTML node-text" >'+(item.name?item.name:item.path)+'<span style="margin-left:10px;color:#8a8989;">['+item.path+']</span></span>\n' +
            '                    </a>\n' +
            '                </div>\n' +
            '            </li>');
    })
}
//-------------------------------- Remote Sync end ------------------------------


//-------------------------------- Directory Edit start ------------------------------

function showCreateDirectory(parentId,title) {
    let dialog = $("#directory-editor");
    dialog.show();
    dialog.find(".modal-header h3").html(title+" <small></small>");
    dialog.find("input[name='parentId']").val(parentId);
    dialog.find("input[name='id']").val("");
    dialog.find("input[name='path']").val("");
    dialog.find("input[name='name']").val("").focus();
}

function removeDirectory(id) {
    openConfirmModal("The directory and requests will be permanently deleted. Are you sure?",function () {
        let parentId = $("#directory-id-"+id).parents(".service").parents(".service").attr("data-directoryid");

        showSendNotify("Remove directory")
        $.ajax({
            type: "delete",
            url: deleteDirectoryUrl,
            contentType : "application/json",
            data: JSON.stringify({"id":id}),
            success: function (data) {
                data = unpackResult(data);
                if (data.code !=200){
                    openMsgModal(data.msg);
                    return;
                }
                closeConfirmModal();
                loadApiList(false,function(){
                    collapsedDirectory(parentId);
                });
            },complete:function () {
                hideSendNotify();
            }
        });
    });

}

function saveDirectory() {
    let dialog = $("#directory-editor");
    let id = dialog.find("input[name='id']").val();
    let name = dialog.find("input[name='name']").val();
    let path = dialog.find("input[name='path']").val();
    let parentId = dialog.find("input[name='parentId']").val();
    if (!name){
        dialog.find(".error-message").html("name is empty")
        return;
    }

    showSendNotify("Save directory")
    $.ajax({
        type: "post",
        url: saveDirectoryUrl,
        contentType : "application/json",
        data: JSON.stringify({"id":id,"name":name,"path":path,"parentId":parentId}),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            loadApiList(false,function(){
                collapsedDirectory(data.data);
            });
            cancelDialog('#directory-editor');

            let tokenStr = "Directory创建次数";
            _czc.push(["_trackEvent",tokenStr,name,tokenStr,"1",tokenStr])

        },complete:function () {
            hideSendNotify();
        }
    });
}

//重命名组
function renameDirectory(directoryId,title) {
    let directory = null;
    $.each(gdata.directoryList,function (index,item) {
        if (item.id == directoryId){
            directory = item;
            return false;
        }
    })
    let dialog = $("#directory-editor");
    dialog.show();
    dialog.find(".modal-header h3").html(title+" <small></small>");
    dialog.find("input[name='parentId']").val(directory.parentId);
    dialog.find("input[name='path']").val(directory.path);
    dialog.find("input[name='id']").val(directory.id);
    dialog.find("input[name='name']").val(directory.name).focus();

}
//确认重命名组
function confirmRenameDialog(e) {
    let newGroupName = $("#rename-dialog").find(".newname").val();
    let oldGroupName = $("#rename-dialog").find(".oldname").val();
    showSendNotify("Renameing group")
    $.ajax({
        type: "put",
        url: renameGroupUrl,
        contentType : "application/json",
        data: JSON.stringify({"newGroupName":newGroupName,"oldGroupName":oldGroupName}),
        success: function (data) {
            data = unpackResult(data);
            if (data.code !=200){
                openMsgModal(data.msg);
                return;
            }
            $(".authenticated .renameing").children(".name").attr("title",newGroupName);
            $(".authenticated .renameing").children(".name").children(".gwt-InlineHTML").text(newGroupName);
            cancelDialog('#rename-dialog');
        },complete:function () {
            hideSendNotify();
        }
    });
}

function collapsedDirectory(parentId) {
    if (!parentId){
        return;
    }

    let fa = $("#directory-id-"+parentId).parents(".service").children(".name").children(".fa")
    $(fa).removeClass("fa-caret-right");
    $(fa).addClass("fa-caret-down");

    $("#directory-id-"+parentId).parents(".service").removeClass("collapsed");
    parentId = $("#directory-id-"+parentId).parents(".service").parents(".service").attr("data-directoryid");
    collapsedDirectory(parentId);
}
//-------------------------------- Directory Edit end ------------------------------

function targetGo(tokenStr,url) {
    _czc.push(["_trackEvent",tokenStr,tokenStr,tokenStr,"1",tokenStr])
    window.open(url);
}