package com.hridoykrisnadas.leadstodatabase.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PersonRowPartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int min = 0, max = 1000000;
        int targetSize = (max - min) / gridSize + 1;
        Map<String, ExecutionContext> result = new HashMap<>();

        int num = 0;
        int start = min;
        int end = start + targetSize - 1;
        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + num, value);

            if (end >= max) {
                end = max;
            }
            value.putInt("mainRowNo", start);
            value.putInt("maxRowValue", end);
            start += targetSize;
            end += targetSize;

            num++;
        }

        log.info("Partition Result: " + result);
        return result;
    }
}
