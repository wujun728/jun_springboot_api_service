package com.zy.common.filter;

import com.zy.common.exception.ServiceException;
import com.zy.common.utils.ParamUtils;
import com.zy.common.utils.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 防止XSS攻击的过滤器
 *
 * @author ruoyi
 */
public class XssFilter implements Filter
{
    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(tempExcludes))
        {
            String[] url = tempExcludes.split(",");
            for (int i = 0; url != null && i < url.length; i++)
            {
                excludes.add(url[i]);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp))
        {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response)
    {
        String url = request.getServletPath();
        String method = request.getMethod();

        // 检查url中是否含有非法参数
        boolean flag = checkUrl(request);
        if(flag){
            throw new ServiceException("参数不合法,已拒绝本次请求");
        }

        // GET DELETE 不过滤
        if (method == null || method.matches("GET") || method.matches("DELETE"))
        {
            return true;
        }
        return StringUtils.matches(url, excludes);
    }

    @Override
    public void destroy()
    {

    }

    public boolean checkUrl(HttpServletRequest request){
        Map<String, Object> inputParamMap = ParamUtils.getRequestParamMap(request);

        String badStr =
                "(?:\\$)|(?:\\%)|(?:\\.\\.)|(?:\\.)|(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                        + "(\\b(\\>|\\<)\\b)";

        Pattern compile = Pattern.compile(badStr, Pattern.CASE_INSENSITIVE);

        boolean flag = false;

        for(String s:inputParamMap.keySet()){
            String param = inputParamMap.get(s)+"";
            Matcher matcher = compile.matcher(param);

            if(matcher.find()){
                flag = true;
                break;
            }
        }

        return flag;
    }

}
