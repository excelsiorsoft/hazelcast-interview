package com.excelsiorsoft.hztest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HzTestApplication.class })
public class HzTestApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(HzTestApplicationTests.class);
	
	@Autowired
    NodeFactory clusterNodeFactory;

	
	@Test
	public void clusterLifecycle() throws Exception {
		
		
		int nodes = 5;
		logger.info("===EXPANDING to {}===", nodes);
		clusterNodeFactory.ensureClusterSize(nodes);
		
		Thread.sleep(5000);
		
		nodes = 4;
		logger.info("===CONTRACTING to {}===", nodes);
		clusterNodeFactory.ensureClusterSize(nodes);
		
		Thread.sleep(5000);
		
		nodes = 7;
		logger.info("===EXPANDING to {}===", nodes);
		clusterNodeFactory.ensureClusterSize(nodes);
		
		Thread.sleep(5000);
				
		nodes = 0;
		logger.info("===SHUTTING THE CLUSTER DOWN===");
		clusterNodeFactory.ensureClusterSize(nodes);
		
	}

}
