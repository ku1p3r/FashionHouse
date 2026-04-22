package common.base;

/**
 * Command interface — encapsulates a single executable action.
 * Used by MenuInvoker and CommandInvoker to dispatch user selections
 * without switch/if-else chains.
 */
public interface Command {
    void execute();
}

