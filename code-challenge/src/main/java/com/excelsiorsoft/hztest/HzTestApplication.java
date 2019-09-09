package com.excelsiorsoft.hztest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;

@SpringBootApplication
public class HzTestApplication {
	
	private static Logger logger = LoggerFactory.getLogger(HzTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HzTestApplication.class, args);
	}
	
	@Bean(name = "clientConfig")
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().setConnectionAttemptLimit(0);
        return clientConfig;
    }
	
	@Bean(name = "clientInstance")
    public HazelcastInstance clientInstance(NodeFactory storageNodeFactory, ClientConfig config) throws Exception {
        //Client needs at least one running node to connect to
        storageNodeFactory.ensureClusterSize(1);
        
        logger.info("We are started!!");
        
        return HazelcastClient.newHazelcastClient(config);
    }

	/*@Bean(destroyMethod = "shutdown")
	public HazelcastInstance createNode(@Qualifier("nodeConfig") Config config) throws Exception {
		return Hazelcast.newHazelcastInstance(config);
	}*/

	@Bean(name="nodeConfig")
    public Config config() throws Exception {
        Config config = new Config();

        return config;
	}
	

}
