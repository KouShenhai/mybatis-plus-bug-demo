/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.mybatisplus.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * mybatis-plus配置.
 *
 * @author laokou
 */
@Configuration
public class MybatisPlusAutoConfig {


	// @formatter:off
	/**
	 * 注意: 使用多个功能需要注意顺序关系,建议使用如下顺序
	 * 											- 多租户，动态表名
	 * 											- 分页，乐观锁
	 * 											- sql性能规范，防止全表更新与删除
	 * 总结：对 sql进行单次改造的优先放入,不对 sql 进行改造的最后放入.
	 */
	// @formatter:on
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new GlobalTenantLineHandler()));
		// 分页插件
		interceptor.addInnerInterceptor(paginationInnerInterceptor());
		// 乐观锁插件
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		// 防止全表更新与删除插件
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		return interceptor;
	}

    @Bean
    public ConfigurationCustomizer slowSqlConfigurationCustomizer() {
        return configuration -> {
            // 慢SQL
            SqlMonitorInterceptor sqlMonitorInterceptor = new SqlMonitorInterceptor();
            configuration.addInterceptor(sqlMonitorInterceptor);
        };
    }

	@Bean(name = "transactionTemplate")
	@ConditionalOnMissingBean(TransactionOperations.class)
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		// 只读事务
		transactionTemplate.setReadOnly(false);
		// 新建事务
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 数据库隔离级别 => 读已提交
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		// 事务超时时间，单位s
		transactionTemplate.setTimeout(180);
		// 事务名称
		transactionTemplate.setName("aixot-transaction-template");
		return transactionTemplate;
	}

	/**
	 * 异步分页. 解除每页500条限制.
	 */
	private PaginationInnerInterceptor paginationInnerInterceptor() {
		// 使用postgresql，如果使用其他数据库，需要修改DbType
		// 使用postgresql，如果使用其他数据库，需要修改DbType
		// 使用postgresql，如果使用其他数据库，需要修改DbType
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		// -1表示不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
		// 溢出总页数后是进行处理，查看源码就知道是干啥的
        paginationInnerInterceptor.setOverflow(true);
		return paginationInnerInterceptor;
	}

}
