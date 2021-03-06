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
package de.adrodoc55.minecraft.mpl.ide.gui.dialog.content;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.swing.BnButton;
import org.beanfabrics.swing.BnLabel;
import org.beanfabrics.swing.BnTextArea;

/**
 * The {@link ContentPanel} is a {@link View} on a {@link ContentPM}.
 *
 * @author Adrodoc55
 * @created by the Beanfabrics Component Wizard, www.beanfabrics.org
 */
@SuppressWarnings("serial")
public class ContentPanel extends JPanel implements View<ContentPM>, ModelSubscriber {
  private final Link link = new Link(this);
  private ModelProvider localModelProvider;
  private JPanel buttonPanel;
  private BnButton bnbtnCopyToClipboard;
  private BnTextArea bnTextArea;
  private JScrollPane scrollPane;
  private BnLabel bnlblTitle;
  private BnButton bnbtnClose;
  private BnButton bnbtnCopyAndClose;

  /**
   * Constructs a new {@link ContentPanel}.
   */
  public ContentPanel() {
    super();
    setBorder(new CompoundBorder(new LineBorder(new Color(192, 192, 192), 1, true),
        new EmptyBorder(5, 5, 5, 5)));
    setLayout(new BorderLayout());
    add(getScrollPane(), BorderLayout.CENTER);
    add(getButtonPanel(), BorderLayout.SOUTH);
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
      localModelProvider.setPresentationModelType(ContentPM.class);
    }
    return localModelProvider;
  }

  /** {@inheritDoc} */
  @Override
  public ContentPM getPresentationModel() {
    return getLocalModelProvider().getPresentationModel();
  }

  /** {@inheritDoc} */
  @Override
  public void setPresentationModel(ContentPM pModel) {
    getLocalModelProvider().setPresentationModel(pModel);
  }

  /** {@inheritDoc} */
  @Override
  public IModelProvider getModelProvider() {
    return this.link.getModelProvider();
  }

  /** {@inheritDoc} */
  @Override
  public void setModelProvider(IModelProvider modelProvider) {
    this.link.setModelProvider(modelProvider);
  }

  /** {@inheritDoc} */
  @Override
  public Path getPath() {
    return this.link.getPath();
  }

  /** {@inheritDoc} */
  @Override
  public void setPath(Path path) {
    this.link.setPath(path);
  }

  private JPanel getButtonPanel() {
    if (buttonPanel == null) {
      buttonPanel = new JPanel();
      buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      GridBagLayout gbl_buttonPanel = new GridBagLayout();
      gbl_buttonPanel.rowHeights = new int[] {0};
      gbl_buttonPanel.columnWidths = new int[] {75, 0, 0, 0, 0};
      gbl_buttonPanel.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
      gbl_buttonPanel.rowWeights = new double[] {0.0};
      buttonPanel.setLayout(gbl_buttonPanel);
      GridBagConstraints gbc_bnlblTitle = new GridBagConstraints();
      gbc_bnlblTitle.anchor = GridBagConstraints.WEST;
      gbc_bnlblTitle.insets = new Insets(0, 0, 0, 5);
      gbc_bnlblTitle.gridx = 0;
      gbc_bnlblTitle.gridy = 0;
      buttonPanel.add(getBnlblTitle(), gbc_bnlblTitle);
      GridBagConstraints gbc_bnbtnCopyToClipboard = new GridBagConstraints();
      gbc_bnbtnCopyToClipboard.insets = new Insets(0, 0, 0, 5);
      gbc_bnbtnCopyToClipboard.fill = GridBagConstraints.HORIZONTAL;
      gbc_bnbtnCopyToClipboard.anchor = GridBagConstraints.NORTH;
      gbc_bnbtnCopyToClipboard.gridx = 1;
      gbc_bnbtnCopyToClipboard.gridy = 0;
      buttonPanel.add(getBnbtnCopyToClipboard(), gbc_bnbtnCopyToClipboard);
      GridBagConstraints gbc_bnbtnCopyAndClose = new GridBagConstraints();
      gbc_bnbtnCopyAndClose.insets = new Insets(0, 0, 0, 5);
      gbc_bnbtnCopyAndClose.gridx = 2;
      gbc_bnbtnCopyAndClose.gridy = 0;
      buttonPanel.add(getBnbtnCopyAndClose(), gbc_bnbtnCopyAndClose);
      GridBagConstraints gbc_bnbtnClose = new GridBagConstraints();
      gbc_bnbtnClose.insets = new Insets(0, 0, 0, 5);
      gbc_bnbtnClose.gridx = 3;
      gbc_bnbtnClose.gridy = 0;
      buttonPanel.add(getBnbtnClose(), gbc_bnbtnClose);
    }
    return buttonPanel;
  }

  private BnTextArea getBnTextArea() {
    if (bnTextArea == null) {
      bnTextArea = new BnTextArea();
      bnTextArea.setSelectAllOnFocusGainedEnabled(true);
      bnTextArea.setRows(5);
      bnTextArea.setLineWrap(true);
      bnTextArea.setPath(new Path("this.content"));
      bnTextArea.setModelProvider(getLocalModelProvider());
    }
    return bnTextArea;
  }

  private JScrollPane getScrollPane() {
    if (scrollPane == null) {
      scrollPane = new JScrollPane();
      scrollPane.setViewportView(getBnTextArea());
    }
    return scrollPane;
  }

  private BnLabel getBnlblTitle() {
    if (bnlblTitle == null) {
      bnlblTitle = new BnLabel();
      bnlblTitle.setPath(new Path("this.title"));
      bnlblTitle.setModelProvider(getLocalModelProvider());
    }
    return bnlblTitle;
  }

  private BnButton getBnbtnCopyToClipboard() {
    if (bnbtnCopyToClipboard == null) {
      bnbtnCopyToClipboard = new BnButton();
      bnbtnCopyToClipboard.setPath(new Path("this.copyToClipboard"));
      bnbtnCopyToClipboard.setModelProvider(getLocalModelProvider());
      bnbtnCopyToClipboard.setText("Copy to Clipboard");
    }
    return bnbtnCopyToClipboard;
  }

  BnButton getBnbtnCopyAndClose() {
    if (bnbtnCopyAndClose == null) {
      bnbtnCopyAndClose = new BnButton();
      bnbtnCopyAndClose.setPath(new Path("this.copyAndClose"));
      bnbtnCopyAndClose.setModelProvider(getLocalModelProvider());
      bnbtnCopyAndClose.setText("Copy and Close");
    }
    return bnbtnCopyAndClose;
  }

  private BnButton getBnbtnClose() {
    if (bnbtnClose == null) {
      bnbtnClose = new BnButton();
      bnbtnClose.setPath(new Path("this.close"));
      bnbtnClose.setModelProvider(getLocalModelProvider());
      bnbtnClose.setText("Close");
    }
    return bnbtnClose;
  }

}
