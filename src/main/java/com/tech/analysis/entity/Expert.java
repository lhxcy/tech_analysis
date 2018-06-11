package com.tech.analysis.entity;

/**
 * Created by Administrator on 2018/5/18 0018.
 */
public class Expert {

    private String name;
    private String enterpriseName;
    private String enterpriseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expert expert = (Expert) o;

        if (name != null ? !name.toLowerCase().equals(expert.name.toLowerCase()) : expert.name != null) return false;
        if (enterpriseName != null ? !enterpriseName.toLowerCase().equals(expert.enterpriseName.toLowerCase()) : expert.enterpriseName != null)
            return false;
        return enterpriseId != null ? enterpriseId.equals(expert.enterpriseId) : expert.enterpriseId == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (enterpriseName != null ? enterpriseName.hashCode() : 0);
        result = 31 * result + (enterpriseId != null ? enterpriseId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "name='" + name + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", enterpriseId='" + enterpriseId + '\'' +
                '}';
    }
}
