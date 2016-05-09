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
package de.adrodoc55.minecraft.mpl.placement;

import java.util.List;

import de.adrodoc55.minecraft.coordinate.Coordinate3D;
import de.adrodoc55.minecraft.mpl.chain.ChainContainer;
import de.adrodoc55.minecraft.mpl.chain.CommandBlockChain;
import de.adrodoc55.minecraft.mpl.chain.CommandChain;
import de.adrodoc55.minecraft.mpl.commands.Mode;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.ChainLink;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.Command;
import de.adrodoc55.minecraft.mpl.compilation.CompilerOptions;
import de.kussm.direction.Directions;

/**
 * @author Adrodoc55
 */
public class MplDebugProjectPlacer extends MplChainPlacer {

  private Coordinate3D start = new Coordinate3D().plus(4, getOrientation().getC().getAxis());

  public MplDebugProjectPlacer(ChainContainer container, CompilerOptions options) {
    super(container, options);
  }

  @Override
  public List<CommandBlockChain> place() {
    for (CommandChain chain : container.getChains()) {
      addChain(chain);
    }
    addUnInstallation();
    return chains;
  }

  public void addChain(CommandChain chain) {
    Coordinate3D max = new Coordinate3D(1, 1, 1).plus(chain.getCommands().size() + 1,
        getOrientation().getA().getAxis());
    Directions template = newDirectionsTemplate(max, getOrientation());

    CommandBlockChain result = generateFlat(chain, start, template);
    chains.add(result);
    start = start.plus(getOrientation().getC().toCoordinate().mult(2));
  }

  protected void addUnInstallation() {
    start = new Coordinate3D();
    List<ChainLink> installation = container.getInstall().getCommands();
    List<ChainLink> uninstallation = container.getUninstall().getCommands();

    for (CommandBlockChain chain : chains) {
      Coordinate3D chainStart = chain.getBlocks().get(0).getCoordinate();
      // TODO: Alle ArmorStands taggen, damit nur ein uninstallation command notwendig
      installation.add(0,
          new Command("/summon ArmorStand ${origin + (" + chainStart.toAbsoluteString()
              + ")} {CustomName:" + chain.getName()
              + ",NoGravity:1,Invisible:1,Invulnerable:1,Marker:1,CustomNameVisible:1}"));
      uninstallation.add(new Command("/kill @e[type=ArmorStand,name=" + chain.getName() + "]"));
      // uninstallation
      // .add(0,new Command("/kill @e[type=ArmorStand,name=" + name + "_NOTIFY]"));
      // uninstallation
      // .add(0,new Command("/kill @e[type=ArmorStand,name=" + name + "_INTERCEPTED]"));
    }

    if (!installation.isEmpty()) {
      installation.add(0, new Command("/setblock ${this - 1} stone", Mode.IMPULSE, false));
    }
    if (!uninstallation.isEmpty()) {
      uninstallation.add(0, new Command("/setblock ${this - 1} stone", Mode.IMPULSE, false));
    }
    generateUnInstallation();
  }

  @Override
  protected void generateUnInstallation() {
    addChain(container.getInstall());
    addChain(container.getUninstall());
  }

  @Override
  protected Coordinate3D getOptimalSize() {
    throw new UnsupportedOperationException();
  }

}
