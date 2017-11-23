package com.sug.core.platform.dingtalk.request;

import java.util.List;

public class DingtalkCreateUserForm {
    private String name;
    private String mobile;
    private List<Integer> department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Integer> getDepartment() {
        return department;
    }

    public void setDepartment(List<Integer> department) {
        this.department = department;
    }
}
