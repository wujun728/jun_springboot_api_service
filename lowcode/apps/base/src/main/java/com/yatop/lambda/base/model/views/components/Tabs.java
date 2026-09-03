package com.yatop.lambda.base.model.views.components;

import java.util.List;
import java.util.Objects;

public class Tabs {
    private String type;
    private String tabsMode;
    private List<Tab> tabs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTabsMode() {
        return tabsMode;
    }

    public void setTabsMode(String tabsMode) {
        this.tabsMode = tabsMode;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

    public static class Tab{
        private String title;
        private Object body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getBody() {
            return body;
        }

        public void setBody(Object body) {
            this.body = body;
        }
    }


}
