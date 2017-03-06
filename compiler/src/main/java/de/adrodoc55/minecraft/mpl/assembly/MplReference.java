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
package de.adrodoc55.minecraft.mpl.assembly;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import de.adrodoc55.commons.FileUtils;
import de.adrodoc55.minecraft.mpl.compilation.CompilerException;
import de.adrodoc55.minecraft.mpl.compilation.MplSource;
import de.adrodoc55.minecraft.mpl.interpretation.MplInterpreter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Adrodoc55
 */
@Getter
@EqualsAndHashCode
@ToString
public abstract class MplReference {
  protected final @Nonnull Set<File> imports = new HashSet<>();
  protected final @Nonnull MplSource source;

  /**
   * Constructs a reference.
   *
   * @param imports the imported files that are expected to contain the reference
   * @param source the source that requires {@code this} reference
   * @throws IllegalArgumentException if one of the {@code imports} is not a file
   */
  public MplReference(@Nonnull Collection<? extends File> imports, @Nonnull MplSource source)
      throws IllegalArgumentException {
    setImports(imports);
    this.source = checkNotNull(source, "source == null!");
  }

  public @Nonnull Set<File> getImports() {
    return Collections.unmodifiableSet(imports);
  }

  private void setImports(Collection<? extends File> imports) throws IllegalArgumentException {
    for (File file : imports) {
      if (!file.isFile()) {
        throw new IllegalArgumentException(
            "The import '" + FileUtils.getCanonicalPath(file) + "' is not a file!");
      }
    }
    this.imports.clear();
    this.imports.addAll(imports);
  }

  public abstract boolean isContainedIn(MplInterpreter interpreter);

  public abstract void resolve(MplInterpreter interpreter);

  public abstract CompilerException createAmbigiousException(List<File> found);

  public abstract void handleNotFound();
}