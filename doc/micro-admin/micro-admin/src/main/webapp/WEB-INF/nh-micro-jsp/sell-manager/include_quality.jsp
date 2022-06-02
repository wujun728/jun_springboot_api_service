
<%@page pageEncoding="UTF-8"%>
<div class="panel panel-primary">
    <div class="panel-heading">
        进件质检
    </div>
    <div class="panel-body">
        <div class="form-group">
            <label  class="col-sm-1 control-label"></label>
            <div class="col-sm-2">
                <input type="radio" name="quality_status" value="1" checked>通过
                <input type="radio" name="quality_status" value="-1" >退回
            </div>
        </div>
        <div class="form-group" id="quality_result" >
            <label  class="col-sm-2 control-label">退回原因</label>
            <div class="col-sm-8">
                <textarea name="quality_remark" class="form-control" style="text-align:left" placeholder="text文本500字符" required></textarea>
            </div>
        </div>
    </div>
</div>

