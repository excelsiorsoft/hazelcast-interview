/**
 * 
 */
package com.excelsiorsoft.hztest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author Simeon
 *
 */
@Service
public class NodeFactory {
	
	private Config config;
    List<HazelcastInstance> instances = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService executorService = Executors.newCachedThreadPool();	
    
    @Autowired
    public NodeFactory(@Qualifier("nodeConfig") Config config) {
        this.config = config;
    }
    
	public void ensureClusterSize(int size) throws InterruptedException {
		
		boolean expand = instances.size() < size;
		boolean contract = !expand;
		
		if (expand) {
			
			int diff = size - instances.size();
			CountDownLatch latch = new CountDownLatch(diff);
			
			for (int x = 0; x < diff; x++) {
				executorService.submit(new CreateInstanceTask(latch, config));
			}
			
			latch.await(6, TimeUnit.SECONDS);
			// System.out.println("We are started!");
		} else if (contract) {
			for (int x = instances.size() - 1; x >= size; x--) {
				HazelcastInstance instance = instances.remove(x);
				instance.shutdown();
			}
		}
	}

    private final class CreateInstanceTask implements Callable<HazelcastInstance> {
    	
    	private final Logger logger = LoggerFactory.getLogger(CreateInstanceTask.class);

        private Config config;
        private CountDownLatch latch;

        public CreateInstanceTask(CountDownLatch latch, Config config) {
            this.config = config;
            this.latch = latch;
        }

        @Override
        public HazelcastInstance call() throws Exception {
            HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
            logger.info("Created instance {}", instance.getName());
            instances.add(instance);
            if (latch != null) {
                latch.countDown();
            }
            return instance;
        }
    }

}
