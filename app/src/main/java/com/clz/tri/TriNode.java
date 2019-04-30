package com.clz.tri;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by stevcao on 2019/4/30
 */
public class TriNode<T> {

    int level;
    private TriNode parentNode;
    private List<TriNode<T>> subNodes = new ArrayList<>();

    T content;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TriNode) {
            return ((TriNode) obj).content.equals(content) && ((TriNode) obj).level == level;
        }
        return false;
    }

    public List<TriNode<T>> getSubNodes() {
        return subNodes;
    }

    public boolean hasSub() {
        return subNodes.size() != 0;
    }

    public boolean containSub(TriNode node) {
        return subNodes.contains(node);
    }

    public TriNode<T> findSubNode(T content) {
        for (TriNode<T> node : subNodes) {
            if (node.content.equals(content)) {
                return node;
            }
        }
        return null;
    }

    public TriNode getParentNode() {
        return parentNode;
    }

    public void addSub(TriNode node) {
        if (!containSub(node)) {
            subNodes.add(node);
            node.parentNode = this;
        }
    }
}
