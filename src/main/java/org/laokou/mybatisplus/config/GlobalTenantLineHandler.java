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

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

/**
 * @author laokou
 */
public class GlobalTenantLineHandler implements TenantLineHandler {

	@Override
	public boolean ignoreTable(String tableName) {
		return false;
	}

	@Override
	public Expression getTenantId() {
		return new LongValue(0L);
	}

	@Override
	public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
		// https://baomidou.com/plugins/tenant/#_top
		return true;
	}

}
