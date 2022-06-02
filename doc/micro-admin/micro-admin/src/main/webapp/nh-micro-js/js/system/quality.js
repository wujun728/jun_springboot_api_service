

$("#quality_result").hide();

$("input[name='quality_status']").change(function(){
    if(this.value == 1){
        $("#quality_result").hide();
    }else if(this.value == -1){
        $("#quality_result").show();
    }
})

function qualitySubmit(){
    var status = $("input[name='quality_status']:checked").val();
    var remark = $("[name='quality_remark']").val();
    var id = $("#business_id").val();
    if(status == "-1"){
        if(null == remark || "" === $.trim(remark)){
            alert("请填写退回原因");
            return;
        }
    }

    $.ajax({
        url:'/micromvc/busi/quality',
        type:'post',
        dataType:'json',
        data:{
           status:status,
           remark:remark,
           id:id
        },
        success:function(data){
            if(data.resultCode=='000'){
                alert("操作成功！")
                window.location.href="/micromvc/proxy/to?page=/nh-micro-jsp/sell-manager/listBusiness.jsp";
            }else{
                swal(data.message)
            }
        }
    })
}

