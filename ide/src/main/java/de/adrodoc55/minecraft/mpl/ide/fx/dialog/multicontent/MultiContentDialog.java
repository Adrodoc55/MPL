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

import java.io.IOException;
import java.util.List;

import de.adrodoc55.minecraft.mpl.ide.fx.JavaFxUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Adrodoc55
 */
public class MultiContentDialog extends Stage {
  private final VBox container;

  public MultiContentDialog(Window owner, String title, List<String> contents) {
    initOwner(owner);
    initModality(Modality.WINDOW_MODAL);
    setTitle(title);
    setOnShown(e -> JavaFxUtils.centerOnOwner(this, getOwner()));
    addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

    int commandCount = contents.size();
    updateMaxHeight(commandCount);
    int width = 500;
    setMinWidth(width);
    setMaxWidth(width);

    container = new VBox();

    int i = 0;
    for (String content : contents) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog/multi-content.fxml"));
        Node importCommandNode = loader.load();
        ContentController controller = loader.getController();

        if (commandCount > 1) {
          Label outputLabel = controller.getOutputLabel();
          outputLabel.setText("Output " + ++i);
        }

        TextArea contentTextArea = controller.getContentTextArea();
        contentTextArea.setText(content);
        contentTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null && newValue.booleanValue()) {
            Platform.runLater(contentTextArea::selectAll);
          }
        });

        container.getChildren().add(importCommandNode);
      } catch (IOException ex) {
        throw new IllegalStateException("Unable to load FXML file", ex);
      }
    }

    ScrollPane root = new ScrollPane(container);
    root.setFitToHeight(true);
    setScene(new Scene(root));
  }

  public void remove(Node importCommandNode) {
    ObservableList<Node> children = container.getChildren();
    children.remove(importCommandNode);
    updateMaxHeight(children.size());
    if (children.isEmpty()) {
      close();
    }
  }

  private void updateMaxHeight(int commandCount) {
    int heigth = commandCount < 2 ? 250 : 500;
    setMaxHeight(heigth);
  }

  private void handleKeyPressed(KeyEvent e) {
    if (e.isConsumed()) {
      return;
    }
    if (new KeyCodeCombination(KeyCode.ESCAPE).match(e)) {
      close();
      e.consume();
      return;
    }
    if (new KeyCodeCombination(KeyCode.ENTER).match(e)) {
      Button defaultButton = (Button) container.lookup("#defaultButton");
      defaultButton.arm();
      e.consume();
      return;
    }
  }

  private void handleKeyReleased(KeyEvent e) {
    if (e.isConsumed()) {
      return;
    }
    if (new KeyCodeCombination(KeyCode.ENTER).match(e)) {
      Button defaultButton = (Button) container.lookup("#defaultButton");
      defaultButton.disarm();
      defaultButton.fire();
      e.consume();
      return;
    }
  }
}
