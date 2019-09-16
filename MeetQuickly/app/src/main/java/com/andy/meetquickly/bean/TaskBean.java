package com.andy.meetquickly.bean;

import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/6/15 17:54
 * 描述：
 */
public class TaskBean {


    private List<DailyTasksBean> dailyTasks;
    private List<DailyTasksBean> personalTasks;

    public List<DailyTasksBean> getDailyTasks() {
        return dailyTasks;
    }

    public void setDailyTasks(List<DailyTasksBean> dailyTasks) {
        this.dailyTasks = dailyTasks;
    }

    public List<DailyTasksBean> getPersonalTasks() {
        return personalTasks;
    }

    public void setPersonalTasks(List<DailyTasksBean> personalTasks) {
        this.personalTasks = personalTasks;
    }

    public static class DailyTasksBean {
        /**
         * category : 1
         * createTime : null
         * depict : 10魅力值
         * exp : 10
         * id : 1
         * name : 登陆
         * sort : 0
         * status : 1
         * type : login
         * url : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/dailyTasks/login.png
         */

        private String category;
        private Object createTime;
        private String depict;
        private String exp;
        private String id;
        private String name;
        private String sort;
        private String status;
        private String type;
        private String url;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
