
<%@page pageEncoding="UTF-8"%>
<div class="panel panel-primary">
    <div class="panel-heading">
        进件复核
    </div>
    <div class="panel-body">
        <c:forEach items="${qualityList}" var="c">
          <c:if test="${c.status==-1}">
              <div class="form-group">
                  <label  class="col-sm-2 control-label" style="color: red">退回</label>
                  <label class="col-sm-3 control-label">退回时间：${c.create_time}</label>
              </div>
              <div class="form-group" >
                  <label  class="col-sm-2 control-label">退回原因</label>
                  <label class="col-sm-3 control-label" style="text-align:left"> ${c.remark}</label>
              </div>
          </c:if>
          <c:if test="${c.status==1}">
              <div class="form-group">
                  <label  class="col-sm-2 control-label" style="color: red">通过</label>
                  <label class="col-sm-3 control-label">通过时间：${c.create_time}</label>
              </div>
          </c:if>
        </c:forEach>
    </div>
</div>

