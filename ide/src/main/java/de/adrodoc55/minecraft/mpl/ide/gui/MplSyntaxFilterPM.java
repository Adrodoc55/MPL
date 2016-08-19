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
package de.adrodoc55.minecraft.mpl.ide.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.PMManager;

import de.adrodoc55.minecraft.mpl.compilation.CompilerException;
import lombok.Getter;

/**
 * @author Adrodoc55
 */
public class MplSyntaxFilterPM extends AbstractPM {

  private final List<CompilerExceptionWrapper> errors = new ArrayList<>();
  private final List<CompilerExceptionWrapper> warnings = new ArrayList<>();

  public MplSyntaxFilterPM() {
    PMManager.setup(this);
  }

  public List<CompilerExceptionWrapper> getErrors() {
    return Collections.unmodifiableList(errors);
  }

  public void setErrors(List<CompilerException> errors) {
    this.errors.clear();
    for (CompilerException ex : errors) {
      this.errors.add(new CompilerExceptionWrapper(ex));
    }
    getPropertyChangeSupport().firePropertyChange("errors", null, null);
  }

  public List<CompilerExceptionWrapper> getWarnings() {
    return Collections.unmodifiableList(warnings);
  }

  public void setWarnings(List<CompilerException> warnings) {
    this.warnings.clear();
    for (CompilerException ex : warnings) {
      this.warnings.add(new CompilerExceptionWrapper(ex));
    }
    getPropertyChangeSupport().firePropertyChange("warnings", null, null);
  }

  /**
   * @author Adrodoc55
   */
  static class CompilerExceptionWrapper {
    private Token token;

    private int startOffset;
    private int stopOffset;

    @Getter
    private final String message;

    public CompilerExceptionWrapper(CompilerException ex) {
      this.token = ex.getSource().token;
      this.startOffset = 0;
      this.stopOffset = 0;
      this.message = ex.getLocalizedMessage();
    }

    public int getStartIndex() {
      return token.getStartIndex() + startOffset;
    }

    public int getStopIndex() {
      return token.getStopIndex() + 1 + stopOffset;
    }

    public void addStartOffset(int offset) {
      this.startOffset += offset;
    }

    public void addStopOffset(int offset) {
      this.stopOffset += offset;
    }
  }

}
