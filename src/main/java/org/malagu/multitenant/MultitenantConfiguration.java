package org.malagu.multitenant;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan
public class MultitenantConfiguration {
	
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager() {
		return new MultitenantJpaTransactionManager();
	}

}
