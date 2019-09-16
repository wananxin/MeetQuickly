package com.andy.meetquickly.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/21 15:12
 * 描述：
 */
public class UserShopDetailBean implements Serializable {


    /**
     * beerList : [{"addTime":null,"categoryId":"1","cost":"20","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/beer/17.jpg","id":"1","name":"雪花","price":"15","remark":"雪花啤酒","status":"1","storeId":"1105","type":"1","unit":"瓶"},{"addTime":null,"categoryId":"1","cost":"25","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/beer/13.jpg","id":"2","name":"青岛","price":"18","remark":"青岛啤酒","status":"1","storeId":"1105","type":"1","unit":"瓶"}]
     * businessTime : 12:00-05:00
     * cocktailList : [{"addTime":null,"categoryId":"4","cost":"128","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/cocktail/6.jpg","id":"7","name":"醉生梦死","price":"88","remark":"醉生梦死","status":"1","storeId":"1105","type":"4","unit":"杯"},{"addTime":null,"categoryId":"4","cost":"128","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/cocktail/7.jpg","id":"8","name":"天使之吻呢","price":"88","remark":"天使之吻","status":"1","storeId":"1105","type":"4","unit":"杯"}]
     * discount : 7
     * drinksList : [{"addTime":null,"categoryId":"6","cost":"68","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/drinks/2.jpg","id":"11","name":"红牛","price":"48","remark":"红牛","status":"1","storeId":"1105","type":"6","unit":"瓶"},{"addTime":null,"categoryId":"6","cost":"68","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/drinks/10.jpg","id":"12","name":"旺仔牛奶","price":"48","remark":"旺仔牛奶","status":"1","storeId":"1105","type":"6","unit":"瓶"}]
     * feature : 没有什么能够阻挡
     * id : 1027
     * isOnline : 1
     * mealList : [{"addTime":null,"categoryId":"7","cost":"400","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/1.jpg","id":"13","name":"套餐A","price":"388","remark":"套餐内容XXX","status":"1","storeId":"1105","type":"7","unit":"份"},{"addTime":null,"categoryId":"7","cost":"698","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/meal/2.jpg","id":"14","name":"套餐B","price":"588","remark":"套餐B","status":"1","storeId":"1105","type":"7","unit":"份"}]
     * redWineList : [{"addTime":null,"categoryId":"3","cost":"3880","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/redWine/10.jpg","id":"5","name":"梅特罗","price":"3290","remark":"梅特罗","status":"1","storeId":"1105","type":"3","unit":"瓶"},{"addTime":null,"categoryId":"3","cost":"880","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/redWine/8.jpg","id":"6","name":"蒙比利兄弟","price":"680","remark":"蒙比利兄弟","status":"1","storeId":"1105","type":"3","unit":"瓶"}]
     * snackList : [{"addTime":null,"categoryId":"5","cost":"100","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/snack/5.jpg","id":"10","name":"鸭脖子","price":"78","remark":"鸭脖子","status":"1","storeId":"1105","type":"5","unit":"盘"},{"addTime":null,"categoryId":"5","cost":"288","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/snack/1.jpg","id":"9","name":"大果盘","price":"188","remark":"大果盘","status":"1","storeId":"1105","type":"5","unit":"盘"}]
     * storeDiscountsType : [{"addTime":"2018-11-22 15:11:01","endTime":"2018-11-30","id":"382","isDelete":"1","name":"免包厢费，进店开机送一打酒","startTime":"2018-11-22","storeId":"1105"}]
     * storeId : 1105
     * storeTableType : [{"addTime":"2018-10-18 17:10:24","boxFee":"488","id":"169","maxPeople":"4","minCharge":"800","minPeople":"1","name":"小包","remark":null,"serviceCharge":"0","status":"1","storeId":"1105"},{"addTime":"2018-10-18 17:10:36","boxFee":"628","id":"170","maxPeople":"8","minCharge":"1000","minPeople":"5","name":"中包","remark":null,"serviceCharge":"0","status":"1","storeId":"1105"},{"addTime":"2018-10-18 17:10:08","boxFee":"828","id":"171","maxPeople":"15","minCharge":"1600","minPeople":"9","name":"大包","remark":null,"serviceCharge":"0","status":"1","storeId":"1105"},{"addTime":"2018-11-22 15:11:31","boxFee":"1388","id":"403","maxPeople":"20","minCharge":"3000","minPeople":"10","name":"Vip","remark":null,"serviceCharge":"0","status":"1","storeId":"1105"},{"addTime":"2018-11-22 15:11:30","boxFee":"1688","id":"404","maxPeople":"40","minCharge":"4000","minPeople":"20","name":"总统","remark":null,"serviceCharge":"0","status":"1","storeId":"1105"}]
     * uId : 20000241
     * wineList : [{"addTime":null,"categoryId":"2","cost":"3980","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/wine/6.jpg","id":"3","name":"轩尼诗XO","price":"3000","remark":"轩尼诗","status":"1","storeId":"1105","type":"2","unit":"瓶"},{"addTime":null,"categoryId":"2","cost":"3980","drinksImg":"http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/wine/10.jpg","id":"4","name":"人头马XO","price":"2880","remark":"人头马","status":"1","storeId":"1105","type":"2","unit":"瓶"}]
     */

    private String businessTime;
    private String discount;
    private String feature;
    private String id;
    private String isOnline;
    private String storeId;
    private String uId;
    private List<SpendTypeListBean> beerList;
    private List<SpendTypeListBean> cocktailList;
    private List<SpendTypeListBean> drinksList;
    private List<SpendTypeListBean> mealList;
    private List<SpendTypeListBean> redWineList;
    private List<SpendTypeListBean> snackList;
    private List<StoreDiscountsTypeBean> storeDiscountsType;
    private List<StoreTableTypeBean> storeTableType;
    private List<SpendTypeListBean> wineList;

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public List<SpendTypeListBean> getBeerList() {
        return beerList;
    }

    public void setBeerList(List<SpendTypeListBean> beerList) {
        this.beerList = beerList;
    }

    public List<SpendTypeListBean> getCocktailList() {
        return cocktailList;
    }

    public void setCocktailList(List<SpendTypeListBean> cocktailList) {
        this.cocktailList = cocktailList;
    }

    public List<SpendTypeListBean> getDrinksList() {
        return drinksList;
    }

    public void setDrinksList(List<SpendTypeListBean> drinksList) {
        this.drinksList = drinksList;
    }

    public List<SpendTypeListBean> getMealList() {
        return mealList;
    }

    public void setMealList(List<SpendTypeListBean> mealList) {
        this.mealList = mealList;
    }

    public List<SpendTypeListBean> getRedWineList() {
        return redWineList;
    }

    public void setRedWineList(List<SpendTypeListBean> redWineList) {
        this.redWineList = redWineList;
    }

    public List<SpendTypeListBean> getSnackList() {
        return snackList;
    }

    public void setSnackList(List<SpendTypeListBean> snackList) {
        this.snackList = snackList;
    }

    public List<StoreDiscountsTypeBean> getStoreDiscountsType() {
        return storeDiscountsType;
    }

    public void setStoreDiscountsType(List<StoreDiscountsTypeBean> storeDiscountsType) {
        this.storeDiscountsType = storeDiscountsType;
    }

    public List<StoreTableTypeBean> getStoreTableType() {
        return storeTableType;
    }

    public void setStoreTableType(List<StoreTableTypeBean> storeTableType) {
        this.storeTableType = storeTableType;
    }

    public List<SpendTypeListBean> getWineList() {
        return wineList;
    }

    public void setWineList(List<SpendTypeListBean> wineList) {
        this.wineList = wineList;
    }

    public static class SpendTypeListBean implements Serializable {
        /**
         * addTime : null
         * categoryId : 1
         * cost : 20
         * drinksImg : http://replace-i.oss-cn-hangzhou.aliyuncs.com/image/drinkImage/beer/17.jpg
         * id : 1
         * name : 雪花
         * price : 15
         * remark : 雪花啤酒
         * status : 1
         * storeId : 1105
         * type : 1
         * unit : 瓶
         */

        private Object addTime;
        private String categoryId;
        private String cost;
        private String drinksImg;
        private String id;
        private String name;
        private String price;
        private String remark;
        private String status;
        private String storeId;
        private String type;
        private String unit;

        public Object getAddTime() {
            return addTime;
        }

        public void setAddTime(Object addTime) {
            this.addTime = addTime;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getDrinksImg() {
            return drinksImg;
        }

        public void setDrinksImg(String drinksImg) {
            this.drinksImg = drinksImg;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class StoreDiscountsTypeBean implements Serializable{
        /**
         * addTime : 2018-11-22 15:11:01
         * endTime : 2018-11-30
         * id : 382
         * isDelete : 1
         * name : 免包厢费，进店开机送一打酒
         * startTime : 2018-11-22
         * storeId : 1105
         */

        private String addTime;
        private String endTime;
        private String id;
        private String isDelete;
        private String name;
        private String startTime;
        private String storeId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }

    public static class StoreTableTypeBean implements Serializable{
        /**
         * addTime : 2018-10-18 17:10:24
         * boxFee : 488
         * id : 169
         * maxPeople : 4
         * minCharge : 800
         * minPeople : 1
         * name : 小包
         * remark : null
         * serviceCharge : 0
         * status : 1
         * storeId : 1105
         */

        private String addTime;
        private String boxFee;
        private String id;
        private String maxPeople;
        private String minCharge;
        private String minPeople;
        private String name;
        private String remark;
        private String serviceCharge;
        private String status;
        private String storeId;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getBoxFee() {
            return boxFee;
        }

        public void setBoxFee(String boxFee) {
            this.boxFee = boxFee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMaxPeople() {
            return maxPeople;
        }

        public void setMaxPeople(String maxPeople) {
            this.maxPeople = maxPeople;
        }

        public String getMinCharge() {
            return minCharge;
        }

        public void setMinCharge(String minCharge) {
            this.minCharge = minCharge;
        }

        public String getMinPeople() {
            return minPeople;
        }

        public void setMinPeople(String minPeople) {
            this.minPeople = minPeople;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }

}
