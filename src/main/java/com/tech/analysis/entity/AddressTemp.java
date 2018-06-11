package com.tech.analysis.entity;

/**
 * Created by Administrator on 2018/3/24 0024.
 */
public class AddressTemp {

    private String uid;
    private String organization;
    private String suborganization;
    private String full_address;
    private String city;
    private String country;
    private String zip;
    private String addr_no;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSuborganization() {
        return suborganization;
    }

    public void setSuborganization(String suborganization) {
        this.suborganization = suborganization;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddr_no() {
        return addr_no;
    }

    public void setAddr_no(String addr_no) {
        this.addr_no = addr_no;
    }

    @Override
    public String toString() {
        return "AddressTemp{" +
                "uid='" + uid + '\'' +
                ", organization='" + organization + '\'' +
                ", suborganization='" + suborganization + '\'' +
                ", full_address='" + full_address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                ", addr_no=" + addr_no +
                '}';
    }

    public String getInsertSql(){

        String sql = "Insert into AddressTemp (";
        if(uid!=null)sql += "uid,";
        if(organization!=null)sql +="organization,";
        if(suborganization!=null)sql +="suborganization,";
        if(full_address!=null)sql +="full_address,";
        if(city!=null)sql +="city,";
        if(country!=null)sql +="country,";
        if(zip!=null)sql +="zip,";
        if(addr_no!=null)sql +="addr_no,";
        sql = sql.substring(0,sql.length()-1);
        sql += ") values(";
        if(uid!=null)sql = sql + "'" + uid +"',";
        if(organization!=null)sql = sql + "'" + organization +"',";
        if(suborganization!=null)sql = sql + "'" + suborganization +"',";
        if(full_address!=null)sql = sql + "'" + full_address +"',";
        if(city!=null)sql = sql + "'" + city +"',";
        if(country!=null)sql = sql + "'" + country +"',";
        if(zip!=null)sql = sql + "'" + zip +"',";
        if(addr_no!=null)sql = sql + "'" + addr_no +"',";
        sql = sql.substring(0,sql.length()-1);
        sql += ")";
        return sql;
    }
}
