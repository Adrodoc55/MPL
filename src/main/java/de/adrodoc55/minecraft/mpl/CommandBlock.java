/*
 * MPL (Minecraft Programming Language): A language for easy development of commandblock
 * applications including and IDE.
 *
 * © Copyright (C) 2016 Adrodoc55
 *
 * This file is part of MPL (Minecraft Programming Language).
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
 * MPL (Minecraft Programming Language): Eine Sprache für die einfache Entwicklung von Commandoblock
 * Anwendungen, beinhaltet eine IDE.
 *
 * © Copyright (C) 2016 Adrodoc55
 *
 * Diese Datei ist Teil von MPL (Minecraft Programming Language).
 *
 * MPL ist Freie Software: Sie können es unter den Bedingungen der GNU General Public License, wie
 * von der Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * MPL wird in der Hoffnung, dass es nützlich sein wird, aber OHNE JEDE GEWÄHRLEISTUNG,
 * bereitgestellt; sogar ohne die implizite Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN
 * BESTIMMTEN ZWECK. Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit MPL erhalten haben. Wenn
 * nicht, siehe <http://www.gnu.org/licenses/>.
 */
package de.adrodoc55.minecraft.mpl;

import de.adrodoc55.minecraft.coordinate.Coordinate3D;
import de.adrodoc55.minecraft.coordinate.Direction3D;
import de.adrodoc55.minecraft.mpl.Command.Mode;

public class CommandBlock {

  private Command command;
  private Direction3D direction;
  private Coordinate3D coordinate;

  public CommandBlock(Command command, Direction3D direction, Coordinate3D coordinate) {
    this.command = command;
    this.direction = direction;
    this.coordinate = coordinate;
  }

  public Command toCommand() {
    return command;
  }

  public void setCommand(Command command) {
    this.command = command;
  }

  public Coordinate3D getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(Coordinate3D coordinate) {
    this.coordinate = coordinate;
  }

  public String getCommand() {
    return command.getCommand();
  }

  public void setCommand(String command) {
    this.command.setCommand(command);
  }

  public boolean isConditional() {
    return command != null ? command.isConditional() : false;
  }

  public void setConditional(boolean conditional) {
    command.setConditional(conditional);
  }

  public Mode getMode() {
    return command != null ? command.getMode() : null;
  }

  public void setMode(Mode mode) {
    command.setMode(mode);
  }

  public boolean needsRedstone() {
    return command != null ? command.needsRedstone() : false;
  }

  public void setNeedsRedstone(boolean needsRedstone) {
    command.setNeedsRedstone(needsRedstone);
  }

  public Direction3D getDirection() {
    return direction;
  }

  public void setDirection(Direction3D direction) {
    this.direction = direction;
  }

  public int getX() {
    return coordinate.getX();
  }

  public int getY() {
    return coordinate.getY();
  }

  public int getZ() {
    return coordinate.getZ();
  }

  @Override
  public String toString() {
    return "CommandBlock [command=" + command + ", coordinate=" + coordinate + "]";
  }

}
