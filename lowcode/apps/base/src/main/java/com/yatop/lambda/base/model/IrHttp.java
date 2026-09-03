package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Table(name = "base_http")
public class IrHttp extends Model<IrHttp> {

    @Service
    public void home(HttpServletRequest request, HttpServletResponse response) {
        String view = getContext().get("base.IrUiView").call("loadWeb","base.web_home");
        view = view.replaceAll("\\{\\{TITLE\\}\\}", "JDOO");
        view = view.replace("{{COPYRIGHT}}", "Copyright &copy; 2022-2023 <a href=\"http://jdoo.org\">JDOO</a>.");
        view = view.replace("{{VERSION}}", "<b>Version</b> 1.0");
        view = view.replace("{{USERNAME}}", "admin");
        HttpUtils.WriteHtml(response, view);
    }

    @Service
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String view = getContext().get("base.IrUiView").call("loadWeb","base.web_login");
        view = view.replaceAll("\\{\\{TITLE\\}\\}", "JDOO");
        view = view.replace("{{COPYRIGHT}}", "Copyright &copy; 2022-2023 <a href=\"http://jdoo.org\">JDOO</a>.");
        view = view.replace("{{VERSION}}", "<b>Version</b> 1.0");
        HttpUtils.WriteHtml(response, view);
    }

    @Service
    public void view(HttpServletRequest request, HttpServletResponse response) {
        String view = getContext().get("base.IrUiView").call("loadWeb","base.web_view");
        HttpUtils.WriteHtml(response, view);
    }
}
