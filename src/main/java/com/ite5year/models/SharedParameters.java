package com.ite5year.models;

import org.hibernate.annotations.BatchSize;

import javax.annotation.Resource;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class SharedParameters {
    @Id long id;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "system_attributes", joinColumns = @JoinColumn(name = "system_attribute_id"))
    @MapKeyColumn(name = "field_key", length = 50)
    @Column(name = "field_value", length = 100)
    @BatchSize(size = 20)
    @Resource(name="sharedParametersMap")
    private Map<String, String> customValues;
}
