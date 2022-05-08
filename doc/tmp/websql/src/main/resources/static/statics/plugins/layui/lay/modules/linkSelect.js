layui.define(['layer','form','element','laytpl'], function(exports){
    var $ = layui.$,
        form = layui.form,
        laytpl = layui.laytpl,
        element = layui.emelemt,
        hint = layui.hint(),
        THIS = 'layui-this';

    //外部接口
    var linkSelect = {
        config: {} //全局配置项
        ,cache: {} //数据缓存
        ,index: layui.laypage ? (layui.laypage.index + 10000) : 0
    };

    //操作当前实例
    var thisSelect = function(){
        var that = this,
            options = that.config,
            id = options.id;
        id && (thisSelect.config[id] = options);

        return {
            reload: function(options){
                that.reload.call(that, options);
            },
            getValue: function(){
                return that.getValue.call(that);
            },
            config: options
        }
    };

    //字符常量
    var MOD_NAME = 'linkSelect';

    //主模板
    var TPL_MAIN = [
        '<div class="layui-input-normal">',
        '<div class="layui-unselect layui-form-select " id="linkSelectDiv{{ d.index }}">',
        '<div class="layui-select-title">',
        '<input type="text" id="linkSelectInput{{ d.index }}" placeholder="{{ d.data.placeholderText }}" readonly class="layui-input layui-unselect"></input>',
        '{{# if(!d.data.disabled){ }}',
        '<i class="layui-edge"></i>',
        '{{# } }}',
        '</div>',

        '</div>',

    ].join("");
    //下拉模板
    var TPL_DL = [
        '<dl class="layui-anim layui-anim-upbit" id="linkSelectdl{{ d.sindex }}" style="width:160px;min-width:160px;left:{{ d.left }}px">',
        '{{# if(d.nodata){  }}',
        '<dd lay-value lay-sindex="{{ d.sindex }}" class="layui-select-tips">{{ d.nodata }}</dd>',
        '{{# }else{ }}',
        '<dd lay-value lay-sindex="{{ d.sindex }}" class="layui-select-tips">请选择</dd>',
        '{{# } }}',
        '{{# layui.each(d.data,function(index,item){ }}',
        '{{# if((!d.options.data) && d.options.url ){  }}',
        '<dd lay-value="{{ item[d.options.replaceId] }}" lay-sindex="{{ d.sindex }}" class>{{ item[d.options.replaceName] }} <i style="position: absolute;right: 10px;margin-top: 13px;cursor: pointer;border-width: 6px;border-color: transparent;border-left-color: #c2c2c2;border-left-style: solid;border-style: dashed;transition: all .3s;"></i></dd>',
        '{{# }else{ }}',
        '<dd lay-value="{{ item[d.options.replaceId] }}" lay-sindex="{{ d.sindex }}" class>{{ item[d.options.replaceName] }} {{# if(item[d.options.replaceChildren]&&item[d.options.replaceChildren].length>0){ }}<i style="position: absolute;right: 10px;margin-top: 13px;cursor: pointer;border-width: 6px;border-color: transparent;border-left-color: #c2c2c2;border-left-style: solid;border-style: dashed;transition: all .3s;"></i>{{# } }}</dd>',
        '{{# } }}',
        '{{# }) }}',
        '</dl>',
    ].join("");


    //构造器
    var Class = function(options){
        var that = this;
        that.index = ++linkSelect.index;
        that.sindex = 0;		//下拉选择器的标识
        that.config = $.extend(true,{}, that.config, linkSelect.config, options);
        that.render();
    };

    //核心入口
    linkSelect.render = function(options){
        var inst = new Class(options);
        return thisSelect.call(inst);
    };
    //获取选中值
    linkSelect.getValue = function(id){
        return linkSelect.cache[id]["value"];		//返回缓存中的选中值
    };
    //重载
    thisSelect.config = {};
    linkSelect.reload = function(id,options){
        var config = thisSelect.config[id];
        var selectedArr = linkSelect.cache[id]["selectedArr"];
        if(!config) return hint.error('The ID option was not found in the linkSelect instance');
        if(!options.selectedArr) options.selectedArr = selectedArr;
        return linkSelect.render($.extend(true, {}, config, options));
    };

    //默认配置
    Class.prototype.config = {
        lableName : "级联选择",	//默认lable名
        placeholderText : "请选择",	//默认placeholder
        minwidth:"160px",		//默认最小宽度
        replaceName:"name",		//默认name名
        replaceId:"id",			//默认id名	用于选择value
        replaceChildren:"children",	//默认children名
        selectedArr : [],		//默认选中数组
        disabled:false,			//禁用 默认不禁用
        where:{}
    };

    //加载容器
    Class.prototype.render = function(){
        var that = this;
        var options = that.config;
        options.elem = $(options.elem);
        var othis = options.elem;
        that.key = options.id || options.index;
        if(!options.elem[0]) return that;		//如果元素不存在
        var arr = options.selectedArr||[];
        linkSelect.cache[that.key] = {};
        linkSelect.cache[that.key]["localData"] = {};			//初始化本地缓存数据
        linkSelect.cache[that.key]["selectedArr"] = arr.slice(0);;	//将默认选中保存到缓存，以便重载的时候获取


        //请求参数的自定义格式
        options.request = $.extend({
            //pageName: 'page',
            //limitName: 'limit'
        }, options.request);

        //响应数据的自定义格式
        options.response = $.extend({
            statusName: 'code',
            statusCode: 0,
            msgName: 'msg',
            dataName: 'data',
        }, options.response);

        //主容器
        var reElem = that.elem = $(laytpl(TPL_MAIN).render({
            //VIEW_CLASS: ELEM_VIEW,
            data: options,
            index: that.index //索引
        }));
        othis.html(reElem);			//生成主元素
        that.formFilter();				//监听选择
        that.pullData(0);				//渲染初始选项
    };


    //监听选择
    Class.prototype.formFilter = function(){
        var that = this;
        var options = that.config;
        var INPUT = $("#linkSelectInput" + that.index);
        var DIV = $("#linkSelectDiv" + that.index);

        if(options.disabled) return;		//禁用

        $(INPUT).on('click', function(e){
            $(DIV).hasClass("layui-form-selected") ? (
                $(DIV).removeClass('layui-form-selected')
            ) : (
                $(DIV).addClass('layui-form-selected')
            );
        });

        $(document).on('click',function(event){
            if($(event.target)[0]!=$(INPUT)[0]){
                $(DIV).removeClass('layui-form-selected');
            }
        });
    };

    //监听选项
    Class.prototype.optionFilter = function(dl){
        var that = this;
        var options = that.config;
        var INPUT = $("#linkSelectInput" + that.index);
        var DIV = $("#linkSelectDiv" + that.index);
        var dds = $(dl).children('dd');
        //选择
        dds.on('click', function(e){
            var valueArr = linkSelect.cache[that.key]["value"];
            var othis = $(this), value = othis.attr('lay-value'),name = othis.text(),thissIndex = othis.attr('lay-sindex');
            var isC = othis.children('i').length>0;		//判断是否有子集

            if(othis.hasClass('layui-select-tips')){
                INPUT.val(getText(setValue(valueArr,thissIndex)));
            } else {
                othis.addClass(THIS);
                INPUT.val(getText(setValue(valueArr,thissIndex,value,name)));
            }
            othis.siblings().removeClass(THIS);
            if(!isC){
                //没有子集
                if(e.bubbles) $(DIV).removeClass('layui-form-selected');
                othis.parent().nextAll("dl").remove();
                that.sindex = thissIndex;
            }else{
                //有子集
                if(that.sindex!=thissIndex){
                    othis.parent().nextAll("dl").remove();
                    that.sindex = thissIndex;
                }
                that.sindex++;
                that.pullData(thissIndex,value);
            }
            typeof options.selected === 'function' && options.selected({name:name,value:value},othis);
            return false;
        });

        //保存数据
        function setValue(valueArr,thissIndex,value,name){
            valueArr = valueArr||[];
            valueArr.splice(thissIndex,valueArr.length);			//将值缓存置空
            if(value) valueArr[thissIndex] = {"name":name,"value":value};
            linkSelect.cache[that.key]["value"] = valueArr;
            return valueArr;
        }
        //获取显示文本
        function getText(arr){
            var text = "";
            $.each(arr,function(i,item){
                text += (i!=0?"> ":"")+item.name;
            });
            return text;
        }
    }

    //渲染数据
    Class.prototype.pullData = function(thissIndex,parentId){
        var that = this;
        var options = that.config;

        if(options.data){
            that.localData(thissIndex,parentId);			//data渲染
        }else if(options.url){
            that.ajaxData(thissIndex,parentId);				//url渲染
        }else{
            that.renderData("缺少数据参数");					//data和url配置都不存在
        }

    };

    //data渲染数据
    Class.prototype.localData = function(thissIndex,parentId){
        var that = this;
        var options = that.config;
        var data = options.data;

        function loopData(data,parentId){
            thissIndex--;
            for(var i=0;i<data.length;i++){
                var item = data[i];
                if(thissIndex<-1){
                    return "没有下级数据";
                }else if(thissIndex==-1){
                    if(parentId==item[options.replaceId]){
                        return item[options.replaceChildren];
                    }
                    continue;
                }else if(item[options.replaceChildren] && item[options.replaceChildren].length>0){
                    var result = loopData(item[options.replaceChildren],parentId);
                    if(result==undefined){
                        thissIndex++;
                        continue;
                    }else{
                        return result;
                    }
                }
            }
        }
        //父级id存在，则获取渲染data
        if(parentId){
            //查看缓存是否已经存在
            var localData = linkSelect.cache[that.key]["localData"][parentId];
            if(!localData){
                data = loopData(options.data,parentId);
            }else{
                data = localData;
            }
        }
        that.renderData(data,parentId);

    }


    //ajax获取数据
    Class.prototype.ajaxData = function(thissIndex,parentId){
        var that = this;
        var options = that.config;
        var response = options.response;
        var params = {};
        if(parentId) params[options.replaceId] = parentId;

        //先查看缓存有没有
        if(parentId && linkSelect.cache[that.key]["localData"][parentId]){
            var localdata = linkSelect.cache[that.key]["localData"][parentId];
            if(typeof localdata === "string") $("#linkSelectDiv" + that.index).removeClass('layui-form-selected');
            that.renderData(localdata,parentId);
        }else{
            $.ajax({
                type: options.method || 'get',
                url: options.url,
                data: $.extend(params, options.where),
                dataType: 'json',
                success: function(res){
                    if(res[response.statusName] != response.statusCode){
                        that.renderData(res[response.msgName]);
                        typeof options.error === 'function' && options.error(res);
                        return ;
                    }
                    var data = res[options.response.dataName] || [];
                    if(data.length<=0){
                        data = "没有下级数据";
                        $("#linkSelectDiv" + that.index).removeClass('layui-form-selected');
                    }
                    that.renderData(data,parentId);
                    if(parentId && data.length>0){
                        linkSelect.cache[that.key]["localData"][parentId] = data;			//将已经获取的数据保存缓存
                    }
                    typeof options.done === 'function' && options.done(res);
                }
                ,error: function(e, m){
                    that.renderData('数据接口请求异常');
                    typeof options.error === 'function' && options.error(e,m);
                }
            });
        }

    };

    //数据渲染
    Class.prototype.renderData = function(data,parentId){
        var that = this,
            options = that.config;
        var dom = $("#linkSelectDiv" + that.index);			//主dom

        var conf = {
            left:160*(that.sindex),
            options:options,
            sindex:that.sindex,
            index: that.index //索引
        }
        if(typeof data === 'string'){
            conf["nodata"] = data;
        }else{
            //渲染下拉选项
            conf["data"] = data;
        }
        var dl =  $(laytpl(TPL_DL).render(conf));
        var dldom = $(dl);
        $(dom).append(dldom);
        that.optionFilter(dldom);
        that.setDefaultValue(that.sindex,dldom);				//设置默认选择
        if(parentId) linkSelect.cache[that.key]["localData"][parentId] = data;		//保存到本地缓存
    };

    //设置默认选择
    Class.prototype.setDefaultValue = function(index,dl){
        var that = this;
        var options = that.config;
        var arr = options.selectedArr;			//获取默认选中数组
        var dds;
        if(dl){
            dds = $(dl).children('dd');
        }else{
            var DIV = $("#linkSelectDiv" + that.index);
            dds = DIV.children('dl').children('dd');
        }
        if(arr && dds){
            $.each(dds,function(i,item){
                if(arr[index]==$(item).attr('lay-value')){
                    options.selectedArr[index] = "";
                    $(item).click();
                    return;
                }
            });
        }
    }

    //表格重载
    Class.prototype.reload = function(options){
        var that = this;
        that.config = $.extend({}, that.config, options);
        linkSelect.reload(that.key, that.config);
    };
    //获取值
    Class.prototype.getValue = function(){
        var that = this;
        return linkSelect.cache[that.key]["value"];		//返回缓存中的选中值
    };


    //暴露模块
    exports(MOD_NAME, linkSelect);
});

