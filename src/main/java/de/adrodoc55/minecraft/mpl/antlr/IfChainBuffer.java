package de.adrodoc55.minecraft.mpl.antlr;

import java.util.LinkedList;

import de.adrodoc55.minecraft.mpl.Command;
import de.adrodoc55.minecraft.mpl.InvertingCommand;
import de.adrodoc55.minecraft.mpl.MplConverter;

public class IfChainBuffer extends ChainBuffer {

  private final ChainBuffer original;
  private final boolean not;

  private boolean thenBlock = true;
  private int added = 0;
  private boolean normalizerAdded = false;
  /**
   * True if the last command invert's the next one.
   */
  private boolean lastWasInverting = false;

  public IfChainBuffer(boolean not, ChainBuffer original) {
    this.not = not;
    this.original = original;
  }

  public void switchToElseBlock() {
    thenBlock = false;
  }

  public ChainBuffer getOriginal() {
    return original;
  }

  @Override
  public void add(int index, Command e) {
    throw new UnsupportedOperationException("Adding with index is not supported in IfChainBuffer");
  }

  @Override
  public boolean add(Command command) {
    boolean isInverting = command instanceof InvertingCommand;
    if ((!lastWasInverting && command.isConditional()) || isInverting) {
      lastWasInverting = isInverting;
      return addToOriginal(command);
    } else {
      if (added == 0 && !not) {
        command.setConditional(true);
        return addToOriginal(command);
      } else {
        Command refCommand = getReferenceCommand();
        if (lastWasInverting) {
          refCommand.setConditional(true);
          lastWasInverting = false;
        }
        addToOriginal(refCommand);
        command.setConditional(true);
        return addToOriginal(command);
      }
    }
  }

  private boolean addToOriginal(Command command) {
    added++;
    return original.add(command);
  }

  private Command getReferenceCommand() {
    boolean testforSuccess = thenBlock ^ not;
    if (testforSuccess && !normalizerAdded) {
      // if we reference the success of the condition, it needs to be normalized to 1.
      // Therefor we silently add the normalizer without incrementing the counter 'added',
      // so that all other references will be shifted from the condition to the normalizer
      Command normalizer = new Command("/testforblock ~ ~ ~ chain_command_block", true);
      // This is the index after the condition command
      int index = original.getCommands().size() - added;
      original.add(index, normalizer);
      normalizerAdded = true;
    }
    int successCount;
    if (testforSuccess) {
      successCount = 1;
    } else {
      successCount = 0;
    }
    Command previous = original.getCommands().peekLast();
    String blockId = MplConverter.toBlockId(previous.getMode());
    return new Command("/testforblock ${this - " + (added + 1) + "} " + blockId
        + " -1 {SuccessCount:" + successCount + "}");
  }

  public String getName() throws IllegalStateException {
    return original.getName();
  }

  public void setName(String name) {
    original.setName(name);
  }

  public boolean isInstall() {
    return original.isInstall();
  }

  public void setInstall(boolean install) {
    original.setInstall(install);
  }

  public boolean isUninstall() {
    return original.isUninstall();
  }

  public void setUninstall(boolean uninstall) {
    original.setUninstall(uninstall);
  }

  public boolean isProcess() {
    return original.isProcess();
  }

  public void setProcess(boolean process) {
    original.setProcess(process);
  }

  public boolean isScript() {
    return original.isScript();
  }

  public void setScript(boolean script) {
    original.setScript(script);
  }

  public boolean isRepeatingProcess() {
    return original.isRepeatingProcess();
  }

  public void setRepeatingProcess(boolean repeatingProcess) {
    original.setRepeatingProcess(repeatingProcess);
  }

  public boolean isRepeatingContext() {
    return original.isRepeatingContext();
  }

  public void setRepeatingContext(boolean repeatingContext) {
    original.setRepeatingContext(repeatingContext);
  }

  public LinkedList<Command> getCommands() {
    return original.getCommands();
  }

  // Delegate Methods

}