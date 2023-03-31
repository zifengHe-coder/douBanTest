package com.web.spirder.demo.command;

import com.web.spirder.demo.vo.PlanVo;

import java.util.List;

/**
 * @author hezifeng
 * @create 2023/3/31 14:29
 */
public class PlanDesignCommand {
    private Integer planId;
    private List<PlanVo> planList;
    private String expression;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public List<PlanVo> getPlanList() {
        return planList;
    }

    public void setPlanList(List<PlanVo> planList) {
        this.planList = planList;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
