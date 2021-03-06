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

import static de.adrodoc55.minecraft.mpl.ide.gui.utils.BnJaggedEditorKit.JaggedLabelView.UnderlineColor;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.beanfabrics.BnModelObserver;
import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.BnPropertyChangeEvent;

import de.adrodoc55.minecraft.mpl.antlr.MplLexer;
import de.adrodoc55.minecraft.mpl.ide.gui.MplSyntaxFilterPM.CompilerExceptionWrapper;

/**
 * The MplSyntaxFilter is a {@link View} on a
 * {@link de.adrodoc55.minecraft.mpl.ide.gui.MplSyntaxFilterPM}.
 *
 * @author Adrodoc55
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
public class MplSyntaxFilter extends DocumentFilter implements View<MplSyntaxFilterPM> {
  private final Link link = new Link(this);
  private ModelProvider localModelProvider;
  private BnModelObserver modelObserver = getModelObserver();

  public BnModelObserver getModelObserver() {
    if (modelObserver == null) {
      modelObserver = new BnModelObserver();
      modelObserver.setPath(new Path());
      modelObserver.setModelProvider(getLocalModelProvider());
      modelObserver.addPropertyChangeListener(e -> {
        // FIXME: Beanfabrics bug
        if (!(e instanceof BnPropertyChangeEvent)) {
          return;
        }
        EventObject cause = ((BnPropertyChangeEvent) e).getCause();
        if (!(cause instanceof PropertyChangeEvent)) {
          return;
        }
        String propertyName = ((PropertyChangeEvent) cause).getPropertyName();
        if (!"errors".equals(propertyName) && !"warnings".equals(propertyName)) {
          return;
        }
        recolor();
      });
    }
    return modelObserver;
  }

  @Override
  public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
    super.remove(fb, offset, length);
    correctExceptionIndicies(offset, -length);
    Document document = fb.getDocument();
    if (document instanceof StyledDocument) {
      recolor((StyledDocument) document);
    }
  }

  @Override
  public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
      throws BadLocationException {
    super.insertString(fb, offset, string, attr);
    correctExceptionIndicies(offset, string.length());
    Document document = fb.getDocument();
    if (document instanceof StyledDocument) {
      recolor((StyledDocument) document);
    }
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
      throws BadLocationException {
    super.replace(fb, offset, length, text, attrs);
    correctExceptionIndicies(offset, text.length() - length);
    Document document = fb.getDocument();
    if (document instanceof StyledDocument) {
      recolor((StyledDocument) document);
    }
  }

  private StyledDocument doc;

  private void recolor(StyledDocument doc) {
    this.doc = doc;
    recolor();
  }

  public void recolor() {
    if (doc == null) {
      return;
    }
    String text;
    try {
      // text = FileUtils.toUnixLineEnding(doc.getText(0, doc.getLength()));
      text = doc.getText(0, doc.getLength());
    } catch (BadLocationException ex) {
      throw new RuntimeException("Encountered unexpected BadLocationException in MplSyntaxFilter",
          ex);
    }
    resetStyling();
    colorTokens(text);
    // Color warnings first, so that errors override them
    colorWarnings();
    colorErrors();
  }

  private void resetStyling() {
    doc.setCharacterAttributes(0, doc.getLength(), getDefaultStyle(), true);
  }

  private void correctExceptionIndicies(int startIndex, int offset) {
    MplSyntaxFilterPM pModel = getPresentationModel();
    if (pModel == null) {
      return;
    }
    List<CompilerExceptionWrapper> exceptions = pModel.getErrors();
    if (exceptions == null) {
      return;
    }
    for (CompilerExceptionWrapper ex : exceptions) {
      if (ex.getStartIndex() >= startIndex) {
        ex.addStartOffset(offset);
      }
      if (ex.getStopIndex() >= startIndex) {
        ex.addStopOffset(offset);
      }
    }
  }

  private void colorErrors() {
    MplSyntaxFilterPM pModel = getPresentationModel();
    if (pModel != null) {
      List<CompilerExceptionWrapper> errors = pModel.getErrors();
      for (CompilerExceptionWrapper ex : errors) {
        styleToken(ex.getStartIndex(), ex.getStopIndex(), getErrorAttributes(), false);
      }
    }
  }

  private void colorWarnings() {
    MplSyntaxFilterPM pModel = getPresentationModel();
    if (pModel != null) {
      List<CompilerExceptionWrapper> warnings = pModel.getWarnings();
      for (CompilerExceptionWrapper ex : warnings) {
        styleToken(ex.getStartIndex(), ex.getStopIndex(), getWarningAttributes(), false);
      }
    }
  }

  public void colorTokens(String text) {
    MplLexer lexer = new MplLexer(new ANTLRInputStream(text));
    loop: while (true) {
      Token token = lexer.nextToken();
      switch (token.getType()) {
        case MplLexer.EOF:
          break loop;
        case MplLexer.ALWAYS_ACTIVE:
        case MplLexer.UNCONDITIONAL:
          styleToken(token, getLowFocusKeywordStyle());
          break;
        case MplLexer.BREAKPOINT:
        case MplLexer.CONDITIONAL:
        case MplLexer.ELSE:
        case MplLexer.IF:
        case MplLexer.IMPORT:
        case MplLexer.INCLUDE:
        case MplLexer.INLINE:
        case MplLexer.INSTALL:
        case MplLexer.INTERCEPT:
        case MplLexer.INVERT:
        case MplLexer.NOT:
        case MplLexer.NOTIFY:
        case MplLexer.ORIENTATION:
        case MplLexer.PROCESS:
        case MplLexer.PROJECT:
        case MplLexer.REMOTE:
        case MplLexer.SKIP_TOKEN:
        case MplLexer.START:
        case MplLexer.STOP:
        case MplLexer.THEN:
        case MplLexer.UNINSTALL:
        case MplLexer.WAITFOR:
          styleToken(token, getHighFocusKeywordStyle());
          break;
        case MplLexer.IMPULSE:
          styleToken(token, getImpulseStyle());
          break;
        case MplLexer.CHAIN:
          styleToken(token, getChainStyle());
          break;
        case MplLexer.BREAK:
        case MplLexer.CONTINUE:
        case MplLexer.DO:
        case MplLexer.REPEAT:
        case MplLexer.WHILE:
          styleToken(token, getRepeatStyle());
          break;
        case MplLexer.NEEDS_REDSTONE:
          styleToken(token, getNeedsRedstoneStyle());
          break;
        case MplLexer.IDENTIFIER:
          styleToken(token, getIdentifierStyle());
          break;
        case MplLexer.COMMAND:
        case MplLexer.SELECTOR:
        case MplLexer.STRING:
        case MplLexer.TAG:
        case MplLexer.UNRECOGNIZED:
        case MplLexer.UNSIGNED_INTEGER:
          styleToken(token, getDefaultStyle());
          break;
        case MplLexer.DOLLAR:
        case MplLexer.INSERT_CLOSING_BRACKET:
        case MplLexer.INSERT_CLOSING_CURLY_BRACKET:
        case MplLexer.INSERT_DOT:
        case MplLexer.INSERT_IDENTIFIER:
        case MplLexer.INSERT_MINUS:
        case MplLexer.INSERT_OPENING_BRACKET:
        case MplLexer.INSERT_OPENING_CURLY_BRACKET:
        case MplLexer.INSERT_ORIGIN:
        case MplLexer.INSERT_PLUS:
        case MplLexer.INSERT_THIS:
        case MplLexer.INSERT_UNSIGNED_INTEGER:
        case MplLexer.INSERT_WS:
          styleToken(token, getInsertStyle());
          break;
        case MplLexer.COMMENT:
        case MplLexer.MULTILINE_COMMENT:
          styleToken(token, getCommentStyle());
          break;
        default:
          styleToken(token, getDefaultStyle());
      }
    }
  }

  private void styleToken(Token token, AttributeSet style) {
    styleToken(token, style, true);
  }

  private void styleToken(Token token, AttributeSet style, boolean replace) {
    styleToken(token.getStartIndex(), token.getStopIndex() + 1, style, replace);
  }

  private void styleToken(int start, int stop, AttributeSet style, boolean replace) {
    int length = stop - start;
    doc.setCharacterAttributes(start, length, style, replace);
  }

  private Style getDefaultStyle() {
    return doc.getStyle(StyleContext.DEFAULT_STYLE);
  }

  private Style getLowFocusKeywordStyle() {
    Style lowFocusKeywordStyle = doc.getStyle("lowFocusKeyword");
    if (lowFocusKeywordStyle == null) {
      lowFocusKeywordStyle = doc.addStyle("lowFocusKeyword", getDefaultStyle());
      StyleConstants.setBold(lowFocusKeywordStyle, true);
      StyleConstants.setForeground(lowFocusKeywordStyle, new Color(128, 128, 128));
    }
    return lowFocusKeywordStyle;
  }

  private Style getHighFocusKeywordStyle() {
    Style highFocusKeywordStyle = doc.getStyle("highFocusKeyword");
    if (highFocusKeywordStyle == null) {
      highFocusKeywordStyle = doc.addStyle("highFocusKeyword", getDefaultStyle());
      StyleConstants.setBold(highFocusKeywordStyle, true);
      StyleConstants.setForeground(highFocusKeywordStyle, new Color(128, 0, 0));
    }
    return highFocusKeywordStyle;
  }

  private Style getImpulseStyle() {
    Style impulseStyle = doc.getStyle("impulse");
    if (impulseStyle == null) {
      impulseStyle = doc.addStyle("impulse", getDefaultStyle());
      StyleConstants.setBold(impulseStyle, true);
      StyleConstants.setForeground(impulseStyle, new Color(255, 127, 80));
    }
    return impulseStyle;
  }

  private Style getChainStyle() {
    Style chainStyle = doc.getStyle("chain");
    if (chainStyle == null) {
      chainStyle = doc.addStyle("chain", getDefaultStyle());
      StyleConstants.setBold(chainStyle, true);
      StyleConstants.setForeground(chainStyle, new Color(60, 179, 113));
    }
    return chainStyle;
  }

  private Style getRepeatStyle() {
    Style repeatStyle = doc.getStyle("repeat");
    if (repeatStyle == null) {
      repeatStyle = doc.addStyle("repeat", getDefaultStyle());
      StyleConstants.setBold(repeatStyle, true);
      StyleConstants.setForeground(repeatStyle, new Color(106, 90, 205));
    }
    return repeatStyle;
  }

  private Style getNeedsRedstoneStyle() {
    Style needsRedstoneStyle = doc.getStyle("needsRedstone");
    if (needsRedstoneStyle == null) {
      needsRedstoneStyle = doc.addStyle("needsRedstone", getDefaultStyle());
      StyleConstants.setBold(needsRedstoneStyle, true);
      StyleConstants.setForeground(needsRedstoneStyle, Color.RED);
    }
    return needsRedstoneStyle;
  }

  private Style getIdentifierStyle() {
    Style identifierStyle = doc.getStyle("identifier");
    if (identifierStyle == null) {
      identifierStyle = doc.addStyle("identifier", getDefaultStyle());
      StyleConstants.setBold(identifierStyle, true);
      StyleConstants.setForeground(identifierStyle, new Color(128, 128, 0));
    }
    return identifierStyle;
  }

  private Style getCommentStyle() {
    Style commentStyle = doc.getStyle("comment");
    if (commentStyle == null) {
      commentStyle = doc.addStyle("comment", getDefaultStyle());
      StyleConstants.setForeground(commentStyle, new Color(0, 128, 0));
    }
    return commentStyle;
  }

  private Style getInsertStyle() {
    Style insertStyle = doc.getStyle("insert");
    if (insertStyle == null) {
      insertStyle = doc.addStyle("insert", getDefaultStyle());
      StyleConstants.setForeground(insertStyle, new Color(128, 0, 0));
      StyleConstants.setBackground(insertStyle, new Color(240, 230, 140));
    }
    return insertStyle;
  }

  private Style getErrorAttributes() {
    Style errorStyle = doc.getStyle("error");
    if (errorStyle == null) {
      errorStyle = doc.addStyle("error", getDefaultStyle());
      StyleConstants.setUnderline(errorStyle, true);
      errorStyle.addAttribute(UnderlineColor, Color.RED);
    }
    return errorStyle;
  }

  private Style getWarningAttributes() {
    Style warningStyle = doc.getStyle("warning");
    if (warningStyle == null) {
      warningStyle = doc.addStyle("warning", getDefaultStyle());
      StyleConstants.setUnderline(warningStyle, true);
      warningStyle.addAttribute(UnderlineColor, new Color(255, 215, 0));
    }
    return warningStyle;
  }

  /**
   * Returns the local {@link ModelProvider} for this class.
   *
   * @return the local <code>ModelProvider</code>
   * @wbp.nonvisual location=10,430
   */
  protected ModelProvider getLocalModelProvider() {
    if (localModelProvider == null) {
      localModelProvider = new ModelProvider(); // @wb:location=10,430
      localModelProvider.setPresentationModelType(MplSyntaxFilterPM.class);
    }
    return localModelProvider;
  }

  /** {@inheritDoc} */
  @Override
  public MplSyntaxFilterPM getPresentationModel() {
    return getLocalModelProvider().getPresentationModel();
  }

  /** {@inheritDoc} */
  @Override
  public void setPresentationModel(MplSyntaxFilterPM pModel) {
    getLocalModelProvider().setPresentationModel(pModel);
  }

  /** {@inheritDoc} */
  public IModelProvider getModelProvider() {
    return this.link.getModelProvider();
  }

  /** {@inheritDoc} */
  public void setModelProvider(IModelProvider modelProvider) {
    this.link.setModelProvider(modelProvider);
  }

  /** {@inheritDoc} */
  public Path getPath() {
    return this.link.getPath();
  }

  /** {@inheritDoc} */
  public void setPath(Path path) {
    this.link.setPath(path);
  }

}
