package com.liwei.ruiyi.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    /**
     * 将列表拆分成多个子列表，每个子列表最多包含 chunkSize 个元素
     * @param list 原始列表
     * @param chunkSize 每个子列表的最大元素数量
     * @return 包含子列表的列表
     */
    public static <T> List<List<T>> splitIntoChunks(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        int totalSize = list.size();
        int numOfChunks = (totalSize + chunkSize - 1) / chunkSize; // 计算总块数（向上取整）

        for (int i = 0; i < numOfChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalSize);
            chunks.add(list.subList(start, end)); // 获取子列表视图
        }
        //供货商
        return chunks;
    }
}
