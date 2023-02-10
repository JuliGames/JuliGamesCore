package net.juligames.core.cluster;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import net.juligames.core.api.TODO;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
@SuppressWarnings("RedundantThrows")
@TODO(doNotcall = true) //Planned for 1.4 at least
public abstract class DataRequest<T, R> implements Callable<R>, Serializable, HazelcastInstanceAware {

    final T data;

    public DataRequest(final T t) {
        data = t;
    }


    /**
     * Gets the HazelcastInstance reference when submitting a Runnable/Callable using Hazelcast ExecutorService.
     *
     * @param hazelcastInstance the HazelcastInstance reference
     */
    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {

    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public R call() throws Exception {
        return null;
    }

    public abstract R request(T input);
}
