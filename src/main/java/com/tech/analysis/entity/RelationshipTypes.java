package com.tech.analysis.entity;

import org.neo4j.graphdb.RelationshipType;
/**
 * Created by XCY on 2018/5/23.
 */
public enum RelationshipTypes implements RelationshipType {
    publish,
    works_in,
    involve,
    cooperate,
    work_together,
    included_in,
    similar
}
