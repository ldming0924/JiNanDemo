package com.kawakp.demingliu.jinandemo.bean;

import java.util.List;

/**
 * Created by deming.liu on 2016/9/14.
 */
public class MateralBean<L> {

    /**
     * endRow : 10
     * firstPage : 1
     * hasNextPage : true
     * hasPreviousPage : false
     * isFirstPage : true
     * isLastPage : false
     * lastPage : 5
     * list : [{"createBy":null,"createDate":"2016-09-18 18:40:56","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"设备离线","elementId":"device_offline","id":"35575ad1-2133-44a1-ac4f-a471300c3ee2","labelName":null,"level":null,"orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":null,"updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-19 11:10:33","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":null},{"createBy":null,"createDate":"2016-09-10 09:08:44","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"液位低报警","elementId":"gxznhrjz_t_2_e_10","id":"4a2c4df4-023a-42f6-8c8a-f57f9cd1ea94","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:58","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-10 09:08:24","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"回水压力低报警","elementId":"gxznhrjz_t_2_e_7","id":"ec6d9b87-b340-4c12-96e0-103461680a8a","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:56","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-10 09:07:56","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"超压报警","elementId":"gxznhrjz_t_2_e_9","id":"e5a0c17f-afb5-482e-a4d3-2b6d8e155115","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:54","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-10 09:07:12","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"超温报警","elementId":"gxznhrjz_t_2_e_8","id":"14a49f4a-2c26-4ff2-8c99-7b7f40732706","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:52","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-10 09:07:12","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"综合报警","elementId":"gxznhrjz_t_2_e_11","id":"333d1ed8-1f57-4f1f-95c8-6fc292ac2e32","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:51","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-09 17:08:43","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"设备离线","elementId":"device_offline","id":"c311c6c3-773c-4965-91b3-13569d9bc8d8","labelName":null,"level":null,"orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":null,"updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-14 17:17:35","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":null},{"createBy":null,"createDate":"2016-09-09 12:34:49","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"设备离线","elementId":"device_offline","id":"75fc465e-ab60-4ed2-9be5-f5a39f445cdf","labelName":null,"level":null,"orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":null,"updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-09 13:45:40","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":null},{"createBy":null,"createDate":"2016-09-09 12:33:49","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"超压报警","elementId":"gxznhrjz_t_2_e_9","id":"82c30743-8c8f-4c16-bcbc-1177da4e7781","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-09 13:45:38","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"},{"createBy":null,"createDate":"2016-09-09 12:27:45","delFlag":"0","deviceId":"deviceId0022","deviceName":"济南换热站设备","displayName":"超压报警","elementId":"gxznhrjz_t_2_e_9","id":"3360996b-a1fa-4192-bff2-a4ab9a99db67","labelName":null,"level":"1","orgId":"7cbea6a651374cf19e3a2d887e70666f","status":"1","tenantId":"jn89f14c27284c5dbf821cf5e50753b7","uint":"","updateBy":"18bc18ec6920478fa5c7003e54e6dd9e,济南管理员","updateDate":"2016-09-09 12:33:42","userId":"18bc18ec6920478fa5c7003e54e6dd9e","userName":"济南管理员","value":"true"}]
     * navigatePages : 8
     * navigatepageNums : [1,2,3,4,5]
     * nextPage : 2
     * orderBy : null
     * pageNum : 1
     * pageSize : 10
     * pages : 5
     * prePage : 0
     * size : 10
     * startRow : 1
     * total : 47
     */

    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int total;
    /**
     * createBy : null
     * createDate : 2016-09-18 18:40:56
     * delFlag : 0
     * deviceId : deviceId0022
     * deviceName : 济南换热站设备
     * displayName : 设备离线
     * elementId : device_offline
     * id : 35575ad1-2133-44a1-ac4f-a471300c3ee2
     * labelName : null
     * level : null
     * orgId : 7cbea6a651374cf19e3a2d887e70666f
     * status : 1
     * tenantId : jn89f14c27284c5dbf821cf5e50753b7
     * uint : null
     * updateBy : 18bc18ec6920478fa5c7003e54e6dd9e,济南管理员
     * updateDate : 2016-09-19 11:10:33
     * userId : 18bc18ec6920478fa5c7003e54e6dd9e
     * userName : 济南管理员
     * value : null
     */

    private List<L> list;

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<L> getList() {
        return list;
    }

    public void setList(List<L> list) {
        this.list = list;
    }

}
