package mw.shared.servercommands;

import java.util.UUID;

import mw.server.network.exceptions.IllegalCommandException;
import mw.server.network.mappers.AccountMapper;

/**
 * @author cbloom7
 * All commands that are sent after account verification should extend this class to avoid needing to ask the AccountMapper for
 * the account id associated with tthe paramter client id.
 */
public abstract class AbstractAuthenticatedServerCommand extends AbstractServerCommand {

	@Override
	public final void execute(Integer pClientID) throws IllegalCommandException {
		doExecution(AccountMapper.getInstance().getAccountID(pClientID));
	}
	
	protected abstract void doExecution(UUID pAccountID) throws IllegalCommandException;

}
