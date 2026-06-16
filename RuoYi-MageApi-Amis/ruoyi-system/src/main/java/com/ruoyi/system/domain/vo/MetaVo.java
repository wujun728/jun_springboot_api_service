package com.ruoyi.system.domain.vo;

import com.ruoyi.common.utils.StringUtils;

/**
 * 路由显示信息
 * 
 * @author ruoyi
 */
public class MetaVo
{

    /**
     * 路由编号
     */
    private Long menuId;
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon,Long menuId)
    {
        this.title = title;
        this.icon = icon;
        this.menuId = menuId;
    }

    public MetaVo(String title, String icon, boolean noCache,Long menuId)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.menuId = menuId;
    }

    public MetaVo(String title, String icon, String link,Long menuId)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVo(String title, String icon, boolean noCache, String link ,Long menuId)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link))
        {
            this.link = link;
        }
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
