package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @ author   lyg
 * @ version  1.0
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPALCEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ){
            String keyword;
            while((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if(StringUtils.isBlank(text)) {
            return null;
        }

        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while(begin < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if(isSymbol(c)) {
                if(tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if(tempNode == null) {
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if(tempNode.isKeywordEnd()) {
                sb.append(REPALCEMENT);
                begin = ++position;
            } else {
                if(position < text.length()) {
                    position++;
                }
            }
        }
        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //将一个敏感词添加到前缀树中
    private void  addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            //指针指向子节点，进入下一轮循环
            tempNode = subNode;

            //设置结束标记
            if(i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    //前缀树
    private class TrieNode {

        //关键词结束标记
        private boolean isKeywordEnd = false;

        //子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
