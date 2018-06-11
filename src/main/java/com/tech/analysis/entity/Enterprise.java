package com.tech.analysis.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
public class Enterprise {

    private String name;
    private String chuziqiye;
    private String zhuceziben;
    private String hangye;
    private String zhuceriqi;
    private String code;
    private String type;
    private String zuzhixingshi;
    private String level;
    private String zhucedi;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChuziqiye() {
        return chuziqiye;
    }

    public void setChuziqiye(String chuziqiye) {
        this.chuziqiye = chuziqiye;
    }

    public String getZhuceziben() {
        return zhuceziben;
    }

    public void setZhuceziben(String zhuceziben) {
        this.zhuceziben = zhuceziben;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye;
    }

    public String getZhuceriqi() {
        return zhuceriqi;
    }

    public void setZhuceriqi(String zhuceriqi) {
        this.zhuceriqi = zhuceriqi;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZuzhixingshi() {
        return zuzhixingshi;
    }

    public void setZuzhixingshi(String zuzhixingshi) {
        this.zuzhixingshi = zuzhixingshi;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getZhucedi() {
        return zhucedi;
    }

    public void setZhucedi(String zhucedi) {
        this.zhucedi = zhucedi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enterprise that = (Enterprise) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (chuziqiye != null ? !chuziqiye.equals(that.chuziqiye) : that.chuziqiye != null) return false;
        if (zhuceziben != null ? !zhuceziben.equals(that.zhuceziben) : that.zhuceziben != null) return false;
        if (hangye != null ? !hangye.equals(that.hangye) : that.hangye != null) return false;
        if (zhuceriqi != null ? !zhuceriqi.equals(that.zhuceriqi) : that.zhuceriqi != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (zuzhixingshi != null ? !zuzhixingshi.equals(that.zuzhixingshi) : that.zuzhixingshi != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        return zhucedi != null ? zhucedi.equals(that.zhucedi) : that.zhucedi == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (chuziqiye != null ? chuziqiye.hashCode() : 0);
        result = 31 * result + (zhuceziben != null ? zhuceziben.hashCode() : 0);
        result = 31 * result + (hangye != null ? hangye.hashCode() : 0);
        result = 31 * result + (zhuceriqi != null ? zhuceriqi.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (zuzhixingshi != null ? zuzhixingshi.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (zhucedi != null ? zhucedi.hashCode() : 0);
        return result;
    }

    public Object[] getSQL(){
        List<Object> list = new ArrayList<>();
        if(chuziqiye != null){
            list.add(chuziqiye);
        }
        if(zhuceziben != null){
            list.add(zhuceziben);
        }
        if(hangye != null){
            list.add(hangye);
        }
        if(zhuceriqi != null){
            list.add(zhuceriqi);
        }
        if(code != null){
            list.add(code);
        }
        if(type != null){
            list.add(type);
        }
        if(zuzhixingshi != null){
            list.add(zuzhixingshi);
        }
        if(level != null){
            list.add(level);
        }
        if(zhucedi != null){
            list.add(zhucedi);
        }
        Object[] object = list.toArray();
        return object;
    }

}
