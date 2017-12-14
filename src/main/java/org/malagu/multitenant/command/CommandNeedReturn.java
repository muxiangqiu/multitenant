package org.malagu.multitenant.command;
/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@FunctionalInterface
public interface CommandNeedReturn<T> {
	T execute();
}
