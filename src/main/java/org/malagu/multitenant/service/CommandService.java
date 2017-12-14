package org.malagu.multitenant.service;


import org.malagu.multitenant.command.Command;
import org.malagu.multitenant.command.CommandNeedReturn;

/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public interface CommandService {

	<T> T executeQueryCommand(CommandNeedReturn<T> command);

	void executeQueryCommand(Command command);

	<T> T executeNonQueryCommand(CommandNeedReturn<T> command);

	void executeNonQueryCommand(Command command);


}
