import com.bpodgursky.jbool_expressions.*;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import com.idaoben.web.common.util.TimeUtils;
import com.web.spirder.demo.PacongApplication;
import com.web.spirder.demo.command.PlanDesignCommand;
import com.web.spirder.demo.service.MovieSpiderService;
import com.web.spirder.demo.service.impl.MovieSpiderServiceImpl;
import com.web.spirder.demo.utils.JsonHelper;
import com.web.spirder.demo.utils.ListUtils;
import com.web.spirder.demo.utils.LogicalExpressionEvaluator;
import com.web.spirder.demo.utils.TimingUtils;
import com.web.spirder.demo.vo.NearExpirationVo;
import com.web.spirder.demo.vo.PlanVo;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hezifeng
 * @create 2023/3/27 15:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PacongApplication.class)
public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    private static Map<String, String> cookieMap = new HashMap<>();

    @PostConstruct
    public void init() {
        cookieMap.put("__cf_bm","H7OPlQakSfCS7HbxgxVFsyGdnQW9ZKf.Fb49AJROOPM-1679903611-0-ASHycM56GHajKQqTGX00dlUJWrhbB8fcb9TbAtiVu+dC1WK/Ni4B7ZVfgjmq6iKbzZ6Jj2mnLaKvBiO9LnS2BsmNWiMf/dbAZiUMN+Sri2//7nE8gMXLyFmWTSRR+mPd5g==");
        cookieMap.put("SSESSca767d5b133bea7112f2b49e9c7d0053","kPZvI8Jy-7PbAyY3dE_571ciduWqt5eCoBNRT2yjFGU");
    }


    @Resource
    public MovieSpiderService movieSpiderService;

//    @org.junit.Test
//    public void test() {
//        try {
//            movieSpiderService.getMovieLinks(cookieMap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @org.junit.Test
    public void searchTest() {
        String keyword = "假面骑士";
        try {
            movieSpiderService.searchAll(keyword, cookieMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void expressionTest() {
        String formula = "A&!(B|(C&D)|!E|(F&(G|H)))";
        Expression<String> expression = ExprParser.parse(formula);
        Expression<String> simplify = RuleSet.simplify(expression);
        Expression<String> cnf = RuleSet.toCNF(expression);
        logger.info("simplify : {}", simplify);
        logger.info("cnf : {}", cnf);
        String exp = cnf.toString().substring(1, cnf.toString().length() - 1);
        String prefix = exp;
        String suffix = "";
        if(exp.indexOf("(") != -1) {
            prefix = exp.substring(0, exp.indexOf("(") - 1);
            suffix = exp.substring(exp.indexOf("("));
        }
        if (StringUtils.isNotBlank(suffix)) {
            Expression<String> secondExpression = RuleSet.toDNF(ExprParser.parse(suffix));
            String formula0 = String.format("(%s)", prefix.substring(0, prefix.lastIndexOf("&") - 1));
            logger.info("conditionLevel0 : {}", formula0);
            List<String> varList = getExpressVar(formula0.replaceAll("\\(", "").replaceAll("\\)", ""));
            String[] expList = secondExpression.toString().substring(1, secondExpression.toString().length() - 1).split("\\|");
            int i = 1;
            for (String s : expList) {
                String formulaI = s.substring(s.indexOf("(") > 0 ? 1 : 0);
                varList = getExpressVar(formulaI.replaceAll("\\(", "").replaceAll("\\)", ""));
                logger.info("conditionLevel{} : {}", i++, formulaI);
            }
        } else {
            logger.info("conditionLevel0 : {}", String.format("(%s)", prefix));
        }
    }

    @org.junit.Test
    public void phoneValidTest() {
        String phone = "0579-87102202";
        if (isValidPhoneNumber(phone)) {
            logger.info("校验通过");
        } else {
            logger.info("校验失败");
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // 匹配区号-固话格式，例如：010-12345678
        String regex1 = "^0\\d{2,3}-\\d{7,8}$";

        // 匹配固话格式，例如：12345678
        String regex2 = "^\\d{7,8}$";

        // 匹配手机号格式，例如：13812345678
        String regex3 = "^1[3456789]\\d{9}$";

        // 验证字符串是否符合以上三种格式之一
        return phoneNumber.matches(regex1) || phoneNumber.matches(regex2) || phoneNumber.matches(regex3);
    }

    @org.junit.Test
    public void planTest() {
        TimingUtils.TickTock tickTock = new TimingUtils.TickTock();
        tickTock.tick();
        String formula = "A&!(B|(C&D)|!E|(F&(G|H)))";
        if (LogicalExpressionEvaluator.evaluate(formula)) {
            logger.info("校验通过!");
        } else {
            logger.info("校验失败!");
            return;
        }
        tickTock.tock();
        logger.info("校验耗时{}ms", tickTock.toMillis());
        tickTock.tick();
        Integer planId = 1;
        List<NearExpirationVo> plans = new ArrayList<>();
        PlanDesignCommand command = new PlanDesignCommand();
        command.setPlanId(1);
        command.setExpression(formula);
        List<PlanVo> planList = new ArrayList<>();
        planList.add(new PlanVo(0, null, "groupName", "Personal Care,Skincare,Cosmetics,General Merchandise", "in", "A"));
        planList.add(new PlanVo(0, null, "className", "Baby Milk Powder", "is", "B"));
        planList.add(new PlanVo(0, null, "deptId", "4001", "is", "C"));
        planList.add(new PlanVo(1, "4", "udaValueDesc", "Own label / 自有品牌,Private Label", "in", "D"));
        planList.add(new PlanVo(1, "1436", "udaValue", "0", "ne", "E"));
        planList.add(new PlanVo(1, "49", "udaValue", "2", "is", "F"));
        planList.add(new PlanVo(0, null, "deptId", "2006,2008", "in", "G"));
        planList.add(new PlanVo(0, null, "groupName", "Skincare", "is", "H"));
        command.setPlanList(planList);
        Expression<String> expression = ExprParser.parse(command.getExpression());
//        Expression<String> simplify = RuleSet.simplify(expression);
        Expression<String> cnf = RuleSet.toCNF(expression);
        String exp = cnf.toString().substring(1, cnf.toString().length() - 1);
        String prefix = exp;
        String suffix = "";
        Map<String, PlanVo> planMap = ListUtils.toMap(planList, PlanVo::getTag);

        if(exp.indexOf("(") != -1) {
            prefix = exp.substring(0, exp.indexOf("(") - 1);
            suffix = exp.substring(exp.indexOf("("));
        }
        if (StringUtils.isNotBlank(suffix)) {
            Expression<String> secondExpression = RuleSet.toDNF(ExprParser.parse(suffix));
            String formula0 = String.format("(%s)", prefix.substring(0, prefix.lastIndexOf("&") - 1));
            logger.info("简化后公式: {} & {}", formula0, secondExpression.toString());
            plans.addAll(NearExpirationVo.convertPlanList(planId,0, planMap, getExpressVar(formula0.replaceAll("\\(", "").replaceAll("\\)", ""))));
            logger.info("conditionLevel0 : {}", formula0);
            String[] expList = secondExpression.toString().substring(1, secondExpression.toString().length() - 1).split("\\|");
            int i = 1;
            for (String s : expList) {
                String formulaI = s.substring(s.indexOf("(") > 0 ? 1 : 0);
                plans.addAll(NearExpirationVo.convertPlanList(planId, i, planMap, getExpressVar(formulaI.replaceAll("\\(", "").replaceAll("\\)", ""))));
                logger.info("conditionLevel{} : {}", i++, formulaI);
            }
        } else {
            logger.info("conditionLevel0 : {}", String.format("(%s)", prefix));
            plans.addAll(NearExpirationVo.convertPlanList(planId,0, planMap, getExpressVar(prefix.replaceAll("\\(", "").replaceAll("\\)", ""))));
        }
        String json = JsonHelper.stringify(plans);
        tickTock.tock();
        logger.info("近效期sop: {},简化耗时: {}ms", json, tickTock.toMillis());

    }

    @org.junit.Test
    public void getVarTest() {
        String content = "A&!B&E&";
        if (content.lastIndexOf("&") == content.length() - 1) {
            content = content.substring(0, content.length() - 1);
        }
        List<String> varList = getExpressVar(content);
        logger.info("变量列表为:{}", varList.toString());
    }

    private List<String> getExpressVar(String str) {
        List<String> varList = new ArrayList<>();
        Expression<String> expression = ExprParser.parse(str);
        if (expression instanceof Variable || expression instanceof Not) {
            varList.add(expression.toString());
        } else if (expression instanceof And) {
            And andExpression = (And) expression;
            List<Expression<String>> children = andExpression.getChildren();
            for (Expression<String> child : children) {
                varList.add(child.toString());
            }
        }
        return varList;
    }

    @org.junit.Test
    public void expressTest() {
        String formula = "A&!(B|(C&D)|!E|(F&(G|H)))";
        if (LogicalExpressionEvaluator.evaluate(formula)) {
            logger.info("校验通过!");
        } else {
            logger.info("校验失败!");
        }

    }

}
