package com.clz.tri;

import java.util.List;

/**
 * Create by stevcao on 2019/4/30
 */
public class TriTree<T> {
    TriNode<T> root;

    public TriTree(T rootContent) {
        root = new TriNode<>();
        root.level = 0;
        root.content = rootContent;
    }

    public TriNode<T> getRoot() {
        return root;
    }

    public TriNode<T> match(TriNode node) {
        TriNode cur = node;
        TriNode curFromRoot = root;
        while (cur != null) {
            List<TriNode> nodes = curFromRoot.getSubNodes();
            boolean isFind = false;
            for (TriNode temp : nodes) {
                if (temp.equals(cur)) {
                    curFromRoot = temp;
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {//未找到匹配
                return null;
            } else {
                if (cur.hasSub()) {
                    cur = (TriNode) cur.getSubNodes().get(0);
                } else {
                    return curFromRoot;
                }
            }
        }

        return null;
    }
}
