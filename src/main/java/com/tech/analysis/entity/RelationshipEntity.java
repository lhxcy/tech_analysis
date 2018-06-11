package com.tech.analysis.entity;
import org.neo4j.graphdb.RelationshipType;

import java.io.Serializable;

/**
 * Created by XCY on 2018/3/26.
 */
public class RelationshipEntity implements Serializable {
    private static final long serialVersionUID = 1269373329410167403l;
    private Long source;
    private Long target;
    private Long times;
    private RelationshipTypes type;
    private String stringType;

    public RelationshipEntity(Long source, Long target, String stringType) {
        this.source = source;
        this.target = target;
        this.stringType = stringType;
    }
    public RelationshipEntity(Long source, Long target, Long times, String stringType) {
        this.source = source;
        this.target = target;
        this.times = times;
        this.stringType = stringType;
    }
    public RelationshipEntity(Long source, Long target, Long times, RelationshipTypes type) {
        this.source = source;
        this.target = target;
        this.times = times;
        this.type = type;
    }

    public String getStringType(){
        return stringType;
    }
    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public RelationshipTypes getType() {
        return type;
    }

    public void setType(RelationshipTypes type) {
        this.type = type;
    }
}
