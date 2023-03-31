package com.web.spirder.demo.vo;

/**
 * @author hezifeng
 * @create 2023/3/31 14:30
 */
public class PlanVo {
    private Integer fromMongo;
    private String udaId;
    private String field;
    private String value;
    private String operator;
    private String tag;

    public Integer getFromMongo() {
        return fromMongo;
    }

    public void setFromMongo(Integer fromMongo) {
        this.fromMongo = fromMongo;
    }

    public String getUdaId() {
        return udaId;
    }

    public void setUdaId(String udaId) {
        this.udaId = udaId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public PlanVo() {
    }

    public PlanVo(Integer fromMongo, String udaId, String field, String value, String operator, String tag) {
        this.fromMongo = fromMongo;
        this.udaId = udaId;
        this.field = field;
        this.value = value;
        this.operator = operator;
        this.tag = tag;
    }
}
