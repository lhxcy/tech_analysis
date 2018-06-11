package com.tech.analysis.entity;

import java.io.Serializable;

/**
 * Created by XCY on 2018/5/23.
 */
public class AuthorEntity implements Serializable{
    private static final long serialVersionUID = 1269373329410167403l;
    private String name;
    private String institution;
    private Long id;

    public AuthorEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }
    public AuthorEntity(String name, String institution) {
        this.name = name;
        this.institution = institution;
    }
    public AuthorEntity(String name, String institution, Long id) {
        this.name = name;
        this.institution = institution;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
