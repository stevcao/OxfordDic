package com.clz.tri;

import com.clz.oxforddic.model.entity.Word;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by stevcao on 2019/4/30
 */
public class TriTreeHelper {

    TriTree<Character> triTree;

    public TriTreeHelper() {
        triTree = new TriTree<Character>('0');
    }

    private void addWord(Word word) {
        TriNode root = triTree.getRoot();
        TriNode cur = root;
        for (int i = 0; i < word.word.length(); i++) {
            char ch = word.word.charAt(i);
            TriNode<Character> node = new TriNode();
            node.content = ch;
            node.level = i + 1;
            cur.addSub(node);
            cur = cur.findSubNode(ch);//可能没有加进去
        }
    }

    public void parse(List<Word> words) {
        for (Word word : words) {
            addWord(word);
        }
    }

    public TriNode<Character> match(String key) {
        TriNode cur = null;
        TriNode root = null;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            TriNode<Character> node = new TriNode();
            node.content = ch;
            node.level = i + 1;
            if (cur == null) {
                root = cur = node;
            } else {
                cur.addSub(node);
                cur = node;
            }
        }
        TriNode node = triTree.match(root);
        return node;
    }

    public List<String> getAssociatedKeys(String key, int count) {
        TriNode<Character> node = match(key);
        List<String> strs = new ArrayList<>();
        if (node != null) {
            List<TriNode<Character>> allLeaves = new ArrayList<>();
            getAllLeaves(node, allLeaves, count);
            for (int i = 0; i < allLeaves.size() && i < count; i++) {
                strs.add(nodeToWord(allLeaves.get(i)));
            }
        }
        return strs;
    }

    private String nodeToWord(TriNode<Character> leafNode) {
        if (leafNode.level <= 0) return "";
        TriNode<Character> curNode = leafNode;
        char[] chars = new char[leafNode.level];
        while(curNode.getParentNode() != null) {
            chars[curNode.level - 1] = curNode.content;
            curNode = curNode.getParentNode();
        }
        return new String(chars);
    }

    private void getAllLeaves(TriNode<Character> node, List<TriNode<Character>> resultList, int count) {
        if (resultList.size() > count) return;
        if (node.hasSub()) {
            for (TriNode<Character> subNodes : node.getSubNodes()) {
                getAllLeaves(subNodes, resultList, count);
            }
        } else {
            resultList.add(node);
        }
    }

}
