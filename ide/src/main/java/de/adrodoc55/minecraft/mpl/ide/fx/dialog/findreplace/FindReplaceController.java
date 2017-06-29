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
package de.adrodoc55.minecraft.mpl.ide.fx.dialog.findreplace;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;

import java.awt.Toolkit;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nullable;

import org.eclipse.fx.text.ui.source.SourceViewer;
import org.eclipse.fx.ui.controls.styledtext.StyledTextArea;
import org.eclipse.fx.ui.controls.styledtext.TextSelection;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import de.adrodoc55.commons.RegexUtils;
import de.adrodoc55.minecraft.mpl.ide.fx.ExceptionHandler;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * @author Adrodoc55
 */
public class FindReplaceController {
  @FXML
  private Label findLabel;
  @FXML
  private Label replaceLabel;
  @FXML
  private ComboBox<String> findComboBox;
  @FXML
  private ComboBox<String> replaceComboBox;
  @FXML
  private CheckBox caseSensitive;
  @FXML
  private CheckBox wrapSearch;
  @FXML
  private CheckBox wholeWord;
  @FXML
  private CheckBox incremental;
  @FXML
  private CheckBox regularExpression;
  @FXML
  private Button replaceButton;
  @FXML
  private Button replaceFindButton;
  @FXML
  private Label messageLabel;

  private ExceptionHandler exceptionHandler;

  @FXML
  private void initialize() {
    findLabel.setLabelFor(findComboBox);
    replaceLabel.setLabelFor(replaceComboBox);
    wholeWord.disableProperty().bind(regularExpression.selectedProperty());
    wrapSearch.setSelected(true);
    incremental.setSelected(true);
    findComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
      if (isSet(incremental)) {
        int startIndex = sourceViewer.getTextWidget().getSelection().offset;
        findIncremental(startIndex, newValue);
      }
    });
  }

  public void initialize(ExceptionHandler exceptionHandler) {
    this.exceptionHandler = checkNotNull(exceptionHandler, "exceptionHandler == null!");
  }

  private boolean isSet(CheckBox checkBox) {
    return !checkBox.isDisabled() && checkBox.isSelected();
  }

  private SourceViewer sourceViewer;
  private FindReplaceDocumentAdapter adapter;

  public void setSourceViewer(SourceViewer sourceViewer) {
    this.sourceViewer = checkNotNull(sourceViewer, "sourceViewer == null!");
    IDocument document = sourceViewer.getDocument();
    adapter = new FindReplaceDocumentAdapter(document);
    TextSelection selection = sourceViewer.getTextWidget().getSelection();
    String selectedText =
        adapter.subSequence(selection.offset, selection.offset + selection.length).toString();
    if (isSet(regularExpression)) {
      selectedText = RegexUtils.escape(selectedText);
    }
    findComboBox.setValue(selectedText);
  }

  private void clearMessage() {
    messageLabel.setText("");
  }

  private void showInfo(String message) {
    messageLabel.setTextFill(Color.BLACK);
    messageLabel.setText(message);
  }

  private void showError(String message) {
    messageLabel.setTextFill(Color.RED);
    messageLabel.setText(message);
  }

  private void handlePatternSyntaxException(PatternSyntaxException ex) {
    showError(ex.getDescription());
    Toolkit.getDefaultToolkit().beep();
  }

  @FXML
  public void replaceAll() throws BadLocationException {
    try {
      disableReplaceButtons(true);
      updateHistory(findComboBox);
      String findString = nullToEmpty(findComboBox.getValue());

      IDocument document = sourceViewer.getDocument();
      Document copy = new Document(document.get());

      int count = 0;
      int startOffset = -1;
      IRegion lastReplaceRegion = null;
      FindReplaceDocumentAdapter adapter = new FindReplaceDocumentAdapter(copy);
      while (findStartingAt(startOffset, findString, adapter) != null) {
        lastReplaceRegion = replace(adapter);
        startOffset = lastReplaceRegion.getOffset() + lastReplaceRegion.getLength();
        count++;
      }

      if (count == 0) {
        showInfo("String not found");
      } else {
        document.set(copy.get());
        selectRegionOrBeep(lastReplaceRegion);
        showInfo(count + " Occurences replaced");
      }
    } catch (PatternSyntaxException ex) {
      handlePatternSyntaxException(ex);
    }
  }

  @FXML
  public void replaceFind() throws BadLocationException {
    try {
      replace(adapter);
      find();
    } catch (PatternSyntaxException ex) {
      handlePatternSyntaxException(ex);
    }
  }

  @FXML
  public void replace() throws BadLocationException {
    try {
      IRegion replaced = replace(adapter);
      selectRegionOrBeep(replaced);
    } catch (PatternSyntaxException ex) {
      handlePatternSyntaxException(ex);
    }
  }

  private @Nullable IRegion replace(FindReplaceDocumentAdapter adapter)
      throws BadLocationException, PatternSyntaxException {
    disableReplaceButtons(true);

    updateHistory(replaceComboBox);
    String replaceString = nullToEmpty(replaceComboBox.getValue());
    return adapter.replace(replaceString, isSet(regularExpression));
  }

  @FXML
  public void find() {
    TextSelection initialSelection = sourceViewer.getTextWidget().getSelection();
    int startIndex = initialSelection.offset + initialSelection.length;

    updateHistory(findComboBox);
    String findString = nullToEmpty(findComboBox.getValue());
    findIncremental(startIndex, findString);
  }

  private void findIncremental(int startIndex, String findString) {
    try {
      clearMessage();
      TextSelection initialSelection = sourceViewer.getTextWidget().getSelection();

      IRegion found = findStartingAt(startIndex, findString, adapter);
      if (found == null && isSet(wrapSearch)) {
        found = findStartingAt(-1, findString, adapter);
        if (found != null) {
          showInfo("Wrapped search");
          if (initialSelection.offset == found.getOffset()
              && initialSelection.length == found.getLength()) {
            Toolkit.getDefaultToolkit().beep();
          }
        }
      }
      disableReplaceButtons(found == null);
      selectRegionOrBeep(found);
      if (found == null) {
        showInfo("String not found");
      }
    } catch (PatternSyntaxException ex) {
      handlePatternSyntaxException(ex);
    } catch (BadLocationException ex) {
      exceptionHandler.handleException(ex);
    }
  }

  private void disableReplaceButtons(boolean disable) {
    replaceButton.setDisable(disable);
    replaceFindButton.setDisable(disable);
  }

  private void selectRegionOrBeep(IRegion found) {
    if (found != null) {
      int offset = found.getOffset();
      int length = found.getLength();
      StyledTextArea textWidget = sourceViewer.getTextWidget();
      textWidget.setCaretOffset(offset);
      textWidget.setSelectionRange(offset, length);
    } else {
      Toolkit.getDefaultToolkit().beep();
    }
  }

  private IRegion findStartingAt(int startOffset, String findString,
      FindReplaceDocumentAdapter adapter) throws BadLocationException, PatternSyntaxException {
    boolean forwardSearch = true;
    boolean caseSensitive = isSet(this.caseSensitive);
    boolean wholeWord = isSet(this.wholeWord);
    boolean regExSearch = isSet(regularExpression);
    return adapter.find(startOffset, findString, forwardSearch, caseSensitive, wholeWord,
        regExSearch);
  }

  private void updateHistory(ComboBox<String> findComboBox) {
    String value = findComboBox.getValue();
    if (value != null) {
      ObservableList<String> items = findComboBox.getItems();
      if (!items.contains(value)) {
        items.add(0, value);
        if (items.size() > 31) {
          items.remove(31, items.size());
        }
        findComboBox.getSelectionModel().select(value);
      }
    }
  }
}
