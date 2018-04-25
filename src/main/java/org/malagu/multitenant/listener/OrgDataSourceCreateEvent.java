package org.malagu.multitenant.listener;

import javax.sql.DataSource;

import org.springframework.context.ApplicationEvent;

/**
 * 公司注册，给公司创建一个数据源，此事件为数据源创建完成后触发，通过此事件，程序员可以对数据源对象自定义修改其属性
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2018年4月25日
 */
public class OrgDataSourceCreateEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public OrgDataSourceCreateEvent(DataSource dataSouce) {
		super(dataSouce);
	}

}
