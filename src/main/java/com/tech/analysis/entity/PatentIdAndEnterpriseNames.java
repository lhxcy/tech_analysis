package com.tech.analysis.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
public class PatentIdAndEnterpriseNames {
    private String patentId;
    private String[] enterpriseNames;

    public String getPatentId() {
        return patentId;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public String[] getEnterpriseNames() {
        return enterpriseNames;
    }

    public void setEnterpriseNames(String[] enterpriseNames) {
        this.enterpriseNames = enterpriseNames;
    }
}
