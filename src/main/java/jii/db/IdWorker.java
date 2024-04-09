package jii.db;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * snowflake
 *
 * @author wolanx 1629191974013 1427560723827242019 (1629191974013, 2, 27, 1059)
 */
@Slf4j
public class IdWorker {

    private static final long TIME = 1288834974657L;

    private static final int MAX_A = 1 << 5;
    private static final int MAX_B = 1 << 5;
    private static final int MAX_C = 1 << 12;

    private static final int POS_A = 5 + 5 + 12;
    private static final int POS_B = 5 + 12;
    private static final int POS_C = 12;

    private final int workerId;
    private final int datacenterId;

    public static final IdWorker INS = new IdWorker(66, 123);

    private static int seqId = 0;
    private static long lastMs = 0;

    /*public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            long ret = IdWorker.nextId();
            String x = String.valueOf(ret);
            log.info(x);
        }
        // IdWorker idWorker = new IdWorker(66, 123);
        // long a = idWorker.encode(1629191974013L);
        // List<Long> b = idWorker.decode(a);
    }*/

    public IdWorker(int datacenterId, int workerId) {
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    public static synchronized long nextId() {
        long ms = System.currentTimeMillis();
        if (lastMs == ms) {
            seqId++;
        } else {
            lastMs = ms;
            seqId = 0;
        }
        return INS.encode(ms, seqId);
    }

    private long encode(long ms, long seqId) {
        return ((ms - TIME) << POS_A) | (datacenterId % MAX_A << POS_B) | (workerId % MAX_B << POS_C) | (seqId % MAX_C);
    }

    private List<Long> decode(long id) {
        List<Long> ret = new ArrayList<>();
        ret.add((id >> POS_A) + TIME);
        ret.add((id >> POS_B) & (MAX_A - 1));
        ret.add((id >> POS_C) & (MAX_B - 1));
        ret.add(id & (MAX_C - 1));
        return ret;
    }

    // only use in sql where
    public static long filterByMs(long ms) {
        return INS.encode(ms, 0);
    }

}
