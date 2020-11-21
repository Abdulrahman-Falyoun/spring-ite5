package com.ite5year.models;

import javax.persistence.*;

// INSERT INTO shared_parameters(field_key, field_value) VALUES ('seatsNumber', 4)
// INSERT INTO shared_parameters(field_key, field_value) VALUES ('profitPercentage', 1000)
@Entity
public class SharedParameters {
    @Id long id;
    String fieldKey, fieldValue;

    public SharedParameters() {
    }

    public SharedParameters(String fieldKey, String fieldValue) {
        this.fieldKey = fieldKey;
        this.fieldValue = fieldValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    //    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "system_attributes", joinColumns = @JoinColumn(name = "system_attribute_id"))
//    @MapKeyColumn(name = "field_key", length = 50)
//    @Column(name = "field_value", length = 100)
//    @BatchSize(size = 20)
//    @Resource(name="sharedParametersMap")
//    final private Map<String, Object> sharedParamsMap = new HashMap<String, Object>() {{
//        put("numberOfSeats", 4);
//    }};
}
