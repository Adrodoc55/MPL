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
package de.adrodoc55.minecraft.mpl.ide.fx.dialog.multicontent;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Window;

/**
 * @author Adrodoc55
 */
public class ContentController {
  @FXML
  private Node root;
  @FXML
  private TextArea contentTextArea;
  @FXML
  private Label outputLabel;
  @FXML
  private Button defaultButton;

  @FXML
  public void copyToClipboard() {
    String content = contentTextArea.getText();
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent clipboardContent = new ClipboardContent();
    clipboardContent.putString(content);
    clipboard.setContent(clipboardContent);
  }

  @FXML
  public void copyAndClose() {
    copyToClipboard();
    close();
  }

  @FXML
  public void close() {
    Window window = root.getScene().getWindow();
    if (window instanceof MultiContentDialog) {
      MultiContentDialog dialog = (MultiContentDialog) window;
      dialog.remove(root);
    }
  }

  public TextArea getContentTextArea() {
    return contentTextArea;
  }

  public Label getOutputLabel() {
    return outputLabel;
  }
}
