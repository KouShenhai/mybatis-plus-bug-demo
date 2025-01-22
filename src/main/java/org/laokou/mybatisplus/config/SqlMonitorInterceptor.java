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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.laokou.mybatisplus.JacksonUtil;
import org.springframework.util.StopWatch;

import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.SPACE;

// @formatter:off

/**
 * 4种@Signature type.
 * Executor 		=> 拦截内部SQL执行.
 * ParameterHandler => 拦截参数的处理.
 * StatementHandler => 拦截SQL的构建.
 * ResultSetHandler => 拦截结果的处理.
 * <p>
 * method => 真实的方法，请自行查看并复制
 * <p>
 * args => method入参
 *
 * @author laokou
 * @see PreparedStatementLogger
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class SqlMonitorInterceptor implements Interceptor {

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);  // 绑定目标对象
    }

    @Override
	public Object intercept(Invocation invocation) throws Throwable {
		StopWatch stopWatch = new StopWatch("SQL查询");
		stopWatch.start();
		Object obj = invocation.proceed();
		stopWatch.stop();
		Object target = invocation.getTarget();
        if (target instanceof StatementHandler statementHandler) {
			// 替换空格、制表符、换页符
			String sql = getSql(statementHandler);
            System.out.println("=====>>>>>>>>>>>>" + sql);
        }
		return obj;
	}

	private String getSql(StatementHandler statementHandler) throws JsonProcessingException {
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        if (sql.indexOf("?") > 0) {
            String parameter = JacksonUtil.toJsonStr(boundSql.getParameterObject());
            JsonNode jsonNode = JacksonUtil.readTree(parameter);
            String json = jsonNode.get("ew").get("paramNameValuePairs").toString();
            Map<String, String> map = JacksonUtil.toMap(json, String.class, String.class);
            List<String> list = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).toList();
            for (String str : list) {
                sql = sql.replaceFirst("\\?", "'" + str + "'");
            }
        }
        return sql;
    }

}
