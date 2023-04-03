package com.web.spirder.demo.vo;

import com.web.spirder.demo.utils.ListUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hezifeng
 * @create 2023/3/31 14:08
 */
public class NearExpirationVo {
    private String value;

    private String operator;

    private Integer conditionLevel;

    private String field;

    private Integer planId;

    private Integer fromMongo;

    private String udaId;

    private boolean conflict = false;

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

    public Integer getConditionLevel() {
        return conditionLevel;
    }

    public void setConditionLevel(Integer conditionLevel) {
        this.conditionLevel = conditionLevel;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

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

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public static List<NearExpirationVo> convertPlanList(Integer planId, Integer conditionLevel, Map<String, PlanVo> planMap, List<String> varList) {
        List<NearExpirationVo> planList = new ArrayList<>();
        for (String tag : varList) {
            NearExpirationVo nearExpirationVo = new NearExpirationVo();
            nearExpirationVo.setConditionLevel(conditionLevel);
            nearExpirationVo.setPlanId(planId);
            planList.add(nearExpirationVo);
            if (tag.startsWith("!")) {
                PlanVo planVo = planMap.get(tag.substring(1));

                BeanUtils.copyProperties(planVo, nearExpirationVo);
                if (nearExpirationVo.getFromMongo() == 1) {
                    nearExpirationVo.setOperator(planVo.getOperator() + ";exclude");
                } else {
                    //productInfo
                    String oppositeOpt = getOppositeOpt(planVo.getOperator());
                    nearExpirationVo.setOperator(oppositeOpt);
                }
            } else {
                PlanVo planVo = planMap.get(tag);
                BeanUtils.copyProperties(planVo, nearExpirationVo);
                if (nearExpirationVo.getFromMongo() == 1) {
                    nearExpirationVo.setOperator(planVo.getOperator() + ";in");
                } else {
                    //productInfo
                    nearExpirationVo.setOperator(planVo.getOperator());
                }
            }
        }
        //校验SOP是否有冲突
        validPlans(planList);
        return planList;
    }

    private static String getOppositeOpt(String operator) {
        if ("is".equals(operator)) {
            return "ne";
        } else if ("ne".equals(operator)) {
            return "is";
        } else if ("in".equals(operator)) {
            return "exclude";
        } else if ("exclude".equals(operator)) {
            return "in";
        }
        return "in";
    }
    
    private static void validPlans(List<NearExpirationVo> planList) {
        Map<String, List<NearExpirationVo>> fieldMap = ListUtils.groupElementsByKey(planList, NearExpirationVo::getField);
        for (Map.Entry<String, List<NearExpirationVo>> entry : fieldMap.entrySet()) {
            List<NearExpirationVo> list = entry.getValue();
            if (list.size() == 1) {
                continue;
            } else {
                list.stream().forEach(plan -> {
                    plan.setConflict(true);
                });
            }
        }
    }
}
