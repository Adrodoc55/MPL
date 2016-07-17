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
package de.adrodoc55.minecraft.mpl;

import static de.adrodoc55.minecraft.coordinate.Direction3D.DOWN;
import static de.adrodoc55.minecraft.coordinate.Direction3D.EAST;
import static de.adrodoc55.minecraft.coordinate.Direction3D.NORTH;
import static de.adrodoc55.minecraft.coordinate.Direction3D.SOUTH;
import static de.adrodoc55.minecraft.coordinate.Direction3D.UP;
import static de.adrodoc55.minecraft.coordinate.Direction3D.WEST;
import static de.adrodoc55.minecraft.mpl.commands.Mode.IMPULSE;
import static de.adrodoc55.minecraft.mpl.compilation.CompilerOptions.CompilerOption.TRANSMITTER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.adrodoc55.TestBase;
import de.adrodoc55.minecraft.coordinate.Coordinate3DBuilder;
import de.adrodoc55.minecraft.coordinate.Orientation3DBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplBreakpointBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplCommandBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplIfBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplInterceptBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplNotifyBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplStartBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplStopBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.MplWaitforBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.loop.MplBreakBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.loop.MplContinueBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.loop.MplWhileBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.program.MplProcessBuilder;
import de.adrodoc55.minecraft.mpl.ast.chainparts.program.MplProgramBuilder;
import de.adrodoc55.minecraft.mpl.chain.ChainContainerBuilder;
import de.adrodoc55.minecraft.mpl.chain.CommandChainBuilder;
import de.adrodoc55.minecraft.mpl.commands.Conditional;
import de.adrodoc55.minecraft.mpl.commands.Mode;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.ChainLink;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.Command;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.CommandBuilder;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.MplSkip;
import de.adrodoc55.minecraft.mpl.commands.chainlinks.MplSkipBuilder;
import de.adrodoc55.minecraft.mpl.compilation.CompilerOptions;
import de.adrodoc55.minecraft.mpl.compilation.CompilerOptions.CompilerOption;
import de.adrodoc55.minecraft.mpl.interpretation.ModifierBufferBuilder;
import net.karneim.pojobuilder.Builder;

public class MplTestBase extends TestBase {

  public static Builder<String> $Identifier() {
    return new Builder<String>() {
      @Override
      public String build() {
        return "Identifier_" + somePositiveInt();
      }
    };
  }

  public static Builder<String> $CommandString() {
    return new Builder<String>() {
      @Override
      public String build() {
        return "/command " + some($String());
      }
    };
  }

  public static Builder<String> $Selector() {
    return new Builder<String>() {
      @Override
      public String build() {
        return "@e[name=" + some($Identifier()) + "]";
      }
    };
  }

  public static CommandBuilder $Command() {
    return new CommandBuilder()//
        .withCommand($CommandString())//
        .withMode($Enum(Mode.class))//
        .withConditional($boolean())//
        .withNeedsRedstone($boolean())//
        ;
  }

  public static MplCommandBuilder $MplCommand() {
    return new MplCommandBuilder()//
        .withModifier($ModifierBuffer())//
        .withCommand($CommandString())//
        ;
  }

  private static ModifierBufferBuilder $ModifierBuffer() {
    return new ModifierBufferBuilder()//
        .withMode($Enum(Mode.class))//
        .withConditional($Enum(Conditional.class))//
        .withNeedsRedstone($boolean())//
        ;
  }

  public static MplProgramBuilder $MplProgram() {
    return new MplProgramBuilder()//
        .withName($String())//
        .withScript($boolean())//
        .withOrientation($Orientation3D())//
        .withInstall($MplProcess()//
            .withRepeating(false))//
        .withUninstall($MplProcess()//
            .withRepeating(false))//
        .withMax($Coordinate3D())//
        ;
  }

  public static MplProcessBuilder $MplProcess() {
    return new MplProcessBuilder()//
        .withName($String())//
        .withRepeating($boolean())//
        .withTags(new ArrayList<>())//
        ;
  }

  public static MplStartBuilder $MplStart() {
    return new MplStartBuilder()//
        .withModifier($ModifierBuffer())//
        .withSelector($Selector())//
        ;
  }

  public static MplStopBuilder $MplStop() {
    return new MplStopBuilder()//
        .withModifier($ModifierBuffer())//
        .withSelector($Selector())//
        ;
  }

  public static MplWaitforBuilder $MplWaitfor() {
    return new MplWaitforBuilder()//
        .withModifier($ModifierBuffer())//
        .withEvent($String())//
        ;
  }

  public static MplNotifyBuilder $MplNotify() {
    return new MplNotifyBuilder()//
        .withModifier($ModifierBuffer())//
        .withProcess($String())//
        ;
  }

  public static MplInterceptBuilder $MplIntercept() {
    return new MplInterceptBuilder()//
        .withModifier($ModifierBuffer())//
        .withEvent($String())//
        ;
  }

  public static MplBreakpointBuilder $MplBreakpoint() {
    return new MplBreakpointBuilder()//
        .withModifier($ModifierBuffer())//
        .withMessage($String())//
        ;
  }

  public static MplSkipBuilder $MplSkip() {
    return new MplSkipBuilder()//
        .withInternal($boolean())//
        ;
  }

  public static MplIfBuilder $MplIf() {
    return new MplIfBuilder()//
        .withNot($boolean())//
        .withCondition($CommandString())//
        ;
  }

  public static MplWhileBuilder $MplWhile() {
    return new MplWhileBuilder()//
        .withNot($boolean())//
        .withTrailing($boolean())//
        .withCondition($CommandString())//
        ;
  }

  public static MplBreakBuilder $MplBreak() {
    return new MplBreakBuilder()//
        .withModifier($ModifierBuffer())//
        .withLabel($String())//
        ;
  }

  public static MplContinueBuilder $MplContinue() {
    return new MplContinueBuilder()//
        .withModifier($ModifierBuffer())//
        .withLabel($String())//
        ;
  }

  public static Orientation3DBuilder $Orientation3D() {
    return new Orientation3DBuilder()//
        .withA($oneOf(EAST, WEST))//
        .withB($oneOf(UP, DOWN))//
        .withC($oneOf(SOUTH, NORTH))//
        ;
  }

  public static Coordinate3DBuilder $Coordinate3D() {
    return new Coordinate3DBuilder()//
        .withX(many())//
        .withY(many())//
        .withZ(many())//
        ;
  }

  public static ChainContainerBuilder $ChainContainer(CompilerOption... options) {
    return $ChainContainer(new CompilerOptions(options));
  }

  public static ChainContainerBuilder $ChainContainer(CompilerOptions options) {
    return new ChainContainerBuilder()//
        .withOrientation($Orientation3D())//
        .withMax($Coordinate3D().withX(-1).withY(-1).withZ(-1))//
        .withInstall($CommandChain(options)//
            .withName("install")//
            .withCommands($validChainCommands(options)//
                .withCommands(new ArrayList<>())))//
        .withUninstall($CommandChain(options)//
            .withName("uninstall")//
            .withCommands($validChainCommands(options)//
                .withCommands(new ArrayList<>())))//
        .withChains(new ArrayList<>())//
        .withHashCode($String())//
        ;
  }

  public static CommandChainBuilder $CommandChain(CompilerOption... options) {
    return $CommandChain(new CompilerOptions(options));
  }

  public static CommandChainBuilder $CommandChain(CompilerOptions options) {
    return new CommandChainBuilder()//
        .withName($String())//
        .withCommands($validChainCommands(options))//
        ;
  }

  public static ValidCommandChainBuilder $validChainCommands(CompilerOption... options) {
    return $validChainCommands(new CompilerOptions(options));
  }

  public static ValidCommandChainBuilder $validChainCommands(CompilerOptions options) {
    return new ValidCommandChainBuilder(options)//
        .withCommands($chainLinkCollectionOf(several(),
            $Command()//
                .withConditional(false)//
                .withNeedsRedstone(false)))//
                ;
  }

  public static class ValidCommandChainBuilder implements Builder<Collection<ChainLink>> {
    private final CompilerOptions options;
    private Collection<ChainLink> value$commands$java$util$Collection;
    private boolean isSet$commands$java$util$Collection;
    private Builder<Collection<ChainLink>> builder$commands$java$util$Collection;

    public ValidCommandChainBuilder(CompilerOptions options) {
      this.options = options;
    }

    public ValidCommandChainBuilder withCommands(Collection<ChainLink> value) {
      this.value$commands$java$util$Collection = value;
      this.isSet$commands$java$util$Collection = true;
      return this;
    }

    public ValidCommandChainBuilder withCommands(Builder<Collection<ChainLink>> builder) {
      this.builder$commands$java$util$Collection = builder;
      this.isSet$commands$java$util$Collection = false;
      return this;
    }

    @Override
    public List<ChainLink> build() {
      List<ChainLink> result = new ArrayList<>();
      if (options.hasOption(TRANSMITTER)) {
        result.add(new MplSkip());
        result.add(new Command("setblock ${this - 1} stone", IMPULSE));
      } else {
        result.add(new Command("blockdata ${this - 1} {auto:0b}", IMPULSE));
      }
      Collection<ChainLink> _commands =
          !isSet$commands$java$util$Collection && builder$commands$java$util$Collection != null
              ? builder$commands$java$util$Collection.build() : value$commands$java$util$Collection;
      if (_commands != null && !_commands.isEmpty()) {
        result.add(new Command(some($CommandString())));
        result.addAll(_commands);
      }
      return result;
    }
  }

}
