package org.malagu.multitenant;

import java.util.Stack;

import org.malagu.linq.JpaUtil;
import org.malagu.multitenant.command.Command;
import org.malagu.multitenant.command.CommandNeedReturn;
import org.malagu.multitenant.domain.Organization;
import org.malagu.multitenant.service.CommandService;
import org.malagu.multitenant.strategy.CurrentOrganizationStrategy;


/**
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月24日
 */
public abstract class MultitenantUtils {
	
	private static ThreadLocal<Stack<Organization>> threadLocal = new ThreadLocal<Stack<Organization>>();
	private static CommandService commandService;
	private static CurrentOrganizationStrategy currentOrganizationStrategy;

	
	public static final void pushOrganization(String organizationId) {
		Organization organization = new Organization();
		organization.setId(organizationId);
		pushOrganization(organization);
	}
	
	public static final void pushMasterSecurityContext() {
		pushOrganization(Constants.MASTER);
	}

	
	public static final void pushOrganization(Organization organization) {
		Organization tempOrganization = new Organization();
		tempOrganization.setId(organization.getId());
		Stack<Organization> stack = threadLocal.get();
		if (stack == null) {
			stack = new Stack<Organization>();
			threadLocal.set(stack);
		}
		stack.push(organization);
	}
	
	public static final Organization popOrganization() {
		Stack<Organization> stack = threadLocal.get();
		if (!stack.isEmpty()) {
			return stack.pop();
		}
		if (stack != null && stack.isEmpty()) {
			threadLocal.remove();
		}
		return getCurrentOrganizationStrategy().getCurrent();
	}
	
	public static final Organization peekOrganization() {
		Stack<Organization> stack = threadLocal.get();
		if (stack == null || stack.isEmpty()) {
			try {
				return getCurrentOrganizationStrategy().getCurrent();
			} catch (Exception e) {
				return null;
			}
		}
		return stack.peek();
	}
	
	public static <T> T doQuery(String organizationId, CommandNeedReturn<T> command) {
		try {
			pushOrganization(organizationId);
			return getCommandService().executeQueryCommand(command);
		} finally {
			popOrganization();
		}
	}
	
	public static <T> T doNonQuery(String organizationId, CommandNeedReturn<T> command) {
		try {
			pushOrganization(organizationId);
			return getCommandService().executeNonQueryCommand(command);
		} finally {
			popOrganization();
		}
	}
	
	public static void doQuery(String organizationId, Command command) {
		try {
			pushOrganization(organizationId);
			getCommandService().executeQueryCommand(command);
		} finally {
			popOrganization();
		}
	}
	
	public static void doNonQuery(String organizationId, Command command) {
		try {
			pushOrganization(organizationId);
			getCommandService().executeNonQueryCommand(command);
		} finally {
			popOrganization();
		}
	}
	
	public static <T> T doQuery(CommandNeedReturn<T> command) {
		return doQuery(Constants.MASTER, command);
	}
	
	public static <T> T doNonQuery(CommandNeedReturn<T> command) {
		return doNonQuery(Constants.MASTER, command);
	}
	
	public static void doQuery(Command command) {
		doQuery(Constants.MASTER, command);
	}
	
	public static void doNonQuery(Command command) {
		doNonQuery(Constants.MASTER, command);
	}
	
	public static Organization getLoginOrg() {
		return getCurrentOrganizationStrategy().getCurrent();
	}
	
	public static String getLoginOrgId() {
		Organization org = getLoginOrg();
		if (org != null) {
			return org.getId();
		}
		return null;
	}
	
	private static CommandService getCommandService() {
		if (commandService == null) {
			commandService = JpaUtil.getApplicationContext().getBean(CommandService.class);
		}
		return commandService;
	}

	private static CurrentOrganizationStrategy getCurrentOrganizationStrategy() {
		if (currentOrganizationStrategy == null) {
			currentOrganizationStrategy = JpaUtil.getApplicationContext().getBean(CurrentOrganizationStrategy.class);
		}
		return currentOrganizationStrategy;
	}







}
