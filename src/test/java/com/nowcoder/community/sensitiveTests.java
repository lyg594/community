package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @ author   lyg
 * @ version  1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class sensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter() {
        String text = "这里可以吸毒，可以%嫖%娼%，可以赌博，可以开票，哈哈哈！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
