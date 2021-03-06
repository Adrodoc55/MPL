/*
 * Minecraft Programming Language (MPL): A language for easy development of command block
 * applications including an IDE.
 *
 * © Copyright (C) 2016 Adrodoc55
 *
 * This file is part of MPL.
 *
 * MPL is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MPL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with MPL. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 *
 *
 * Minecraft Programming Language (MPL): Eine Sprache für die einfache Entwicklung von Commandoblock
 * Anwendungen, inklusive einer IDE.
 *
 * © Copyright (C) 2016 Adrodoc55
 *
 * Diese Datei ist Teil von MPL.
 *
 * MPL ist freie Software: Sie können diese unter den Bedingungen der GNU General Public License,
 * wie von der Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * MPL wird in der Hoffnung, dass es nützlich sein wird, aber OHNE JEDE GEWÄHRLEISTUNG,
 * bereitgestellt; sogar ohne die implizite Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN
 * BESTIMMTEN ZWECK. Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit MPL erhalten haben. Wenn
 * nicht, siehe <http://www.gnu.org/licenses/>.
 */
package de.adrodoc55.minecraft.mpl.commands.chainlinks;

import static de.adrodoc55.minecraft.mpl.ast.Conditional.CONDITIONAL;
import static de.adrodoc55.minecraft.mpl.interpretation.ModifierBuffer.modifier;

import de.adrodoc55.minecraft.mpl.ast.Conditional;
import de.adrodoc55.minecraft.mpl.commands.Dependable;
import de.adrodoc55.minecraft.mpl.commands.Mode;
import de.adrodoc55.minecraft.mpl.commands.Modifiable;
import de.adrodoc55.minecraft.mpl.interpretation.CommandPartBuffer;
import de.adrodoc55.minecraft.mpl.interpretation.insert.RelativeThisInsert;
import de.adrodoc55.minecraft.mpl.interpretation.insert.TargetedThisInsert;

/**
 * @author Adrodoc55
 */
public class Commands {
  protected Commands() throws Exception {
    throw new Exception("Utils Classes cannot be instantiated!");
  }

  public static Command newCommand() {
    return newCommand(new CommandPartBuffer(), modifier());
  }

  public static Command newCommand(String command) {
    return newCommand(command, modifier());
  }

  public static Command newCommand(String command, Modifiable modifier) {
    return newCommand(new CommandPartBuffer(command), modifier);
  }

  public static Command newCommand(CommandPartBuffer commandParts, Modifiable modifier) {
    return new Command(commandParts, modifier, GeneratedBy.USER);
  }

  public static Command newInternalCommand() {
    return newInternalCommand(new CommandPartBuffer(), modifier());
  }

  public static Command newInternalCommand(String command) {
    return newInternalCommand(command, modifier());
  }

  public static Command newInternalCommand(String command, Modifiable modifier) {
    return newInternalCommand(new CommandPartBuffer(command), modifier);
  }

  public static Command newInternalCommand(CommandPartBuffer commandParts, Modifiable modifier) {
    return new Command(commandParts, modifier, GeneratedBy.MATERIALIZER);
  }

  public static Command newNoOperationCommand() {
    return new Command(new CommandPartBuffer(), modifier(), GeneratedBy.PLACER);
  }

  public static Command newNormalizingCommand() {
    return newInternalCommand("testforblock ~ ~ ~ chain_command_block", modifier(CONDITIONAL));
  }

  /**
   * Constructs a {@link Dependable}, wich's success is always the opposite of the specified
   * {@link Dependable}, if the constructed command is placed directly after the given
   * {@link Dependable}.
   *
   * @param previous the {@link Dependable} to invert
   * @return a new inverting {@link Command}
   */
  public static Command newInvertingCommand(Dependable previous) {
    return newInvertingCommand(previous.getMode());
  }

  /**
   * Constructs a {@link Command}, wich's success is always the opposite of the previous command, if
   * the previous command has the specified {@link Mode}.
   *
   * @param previousMode the {@link Mode} to invert
   * @return a new inverting {@link Command}
   */
  public static Command newInvertingCommand(Mode previousMode) {
    return newTestforSuccessCommand(-1, previousMode, false);
  }

  public static Command newTestforSuccessCommand(int relative, Mode referencedMode,
      boolean success) {
    return newTestforSuccessCommand(relative, referencedMode, success, false);
  }

  public static Command newTestforSuccessCommand(int relative, Mode targetMode, boolean success,
      boolean conditional) {
    CommandPartBuffer cpb = new CommandPartBuffer();
    cpb.add(getTestforSuccessHeader());
    cpb.add(new RelativeThisInsert(relative));
    cpb.add(getTestforSuccessTrailer(success, targetMode));
    return newInternalCommand(cpb, modifier(Conditional.valueOf(conditional)));
  }

  public static Command newTestforSuccessCommand(Dependable target, boolean success) {
    CommandPartBuffer cpb = new CommandPartBuffer();
    cpb.add(getTestforSuccessHeader());
    cpb.add(new TargetedThisInsert(target));
    cpb.add(getTestforSuccessTrailer(success, target.getMode()));
    return newInternalCommand(cpb, modifier());
  }

  private static String getTestforSuccessHeader() {
    return "testforblock ";
  }

  private static String getTestforSuccessTrailer(boolean success, Mode targetMode) {
    return " " + targetMode.getStringBlockId() + " -1 {SuccessCount:" + (success ? 1 : 0) + "}";
  }
}
