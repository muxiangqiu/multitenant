package org.malagu.multitenant.service;

import org.malagu.multitenant.command.Command;
import org.malagu.multitenant.command.CommandNeedReturn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
@Service
@Transactional(readOnly = true)
public class CommandServiceImpl implements CommandService {
	
	@Override
	public <T> T executeQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}
	
	@Override
	public void executeQueryCommand(Command command) {
		command.execute();
	}
	
	@Override
	@Transactional
	public <T> T executeNonQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}
	
	@Override
	@Transactional
	public void executeNonQueryCommand(Command command) {
		command.execute();
	}

}
