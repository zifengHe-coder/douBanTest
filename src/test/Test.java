import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import com.web.spirder.demo.PacongApplication;
import com.web.spirder.demo.service.MovieSpiderService;
import com.web.spirder.demo.service.impl.MovieSpiderServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
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
            logger.info("conditionLevel0 : {}", String.format("(%s)",prefix.substring(0, prefix.lastIndexOf("&") - 1)));
            String[] expList = secondExpression.toString().substring(1, secondExpression.toString().length() - 1).split("\\|");
            int i = 1;
            for (String s : expList) {
                logger.info("conditionLevel{} : {}", i++, s.substring(s.indexOf("(") > 0 ? 1 : 0));
            }
        } else {
            logger.info("conditionLevel0 : {}", String.format("(%s)", prefix));
        }
    }

}
