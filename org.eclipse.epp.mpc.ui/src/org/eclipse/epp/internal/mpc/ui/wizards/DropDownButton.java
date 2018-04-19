/*******************************************************************************
 * Copyright (c) 2010, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DropDownButton {

	private boolean showArrow;

	private Rectangle arrowBounds;

	private String padding = null;

	private final Button button;

	private List<DropDownSelectionListenerWrapper> selectionListenerWrappers;

	private final PaintListener paintListener = new PaintListener() {
		Color shadowColor;

		Color black;

		@Override
		public void paintControl(PaintEvent e) {

			if (shadowColor == null) {
				shadowColor = e.display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			}
			if (black == null) {
				black = e.display.getSystemColor(SWT.COLOR_BLACK);
			}

			Rectangle buttonBounds = button.getBounds();
			int arrowAreaWidth = 20;
			arrowBounds = new Rectangle(e.x + buttonBounds.width - arrowAreaWidth, e.y, arrowAreaWidth,
					buttonBounds.height);

			GC gc = e.gc;
			Color oldForeground = gc.getForeground();
			Color oldBackground = gc.getBackground();

			try {
				int inset = 3;
				int lineX = arrowBounds.x;
				gc.setLineWidth(1);
				gc.setForeground(shadowColor);
				gc.setBackground(shadowColor);
				gc.drawLine(lineX, arrowBounds.y + inset - 1, lineX, e.y + buttonBounds.height - inset);

				gc.setForeground(black);
				gc.setBackground(black);
				int arrowWidth = 7;
				int arrowHeight = 4;
				int arrowX = lineX + (arrowAreaWidth - arrowWidth) / 2;
				int arrowY = arrowBounds.height / 2 - arrowHeight / 2 + 1;
				gc.fillPolygon(new int[] { arrowX, arrowY, arrowX + arrowWidth, arrowY, arrowX + arrowWidth / 2,
						arrowY + arrowHeight });
			} finally {
				gc.setForeground(oldForeground);
				gc.setBackground(oldBackground);
			}
		}
	};

	public DropDownButton(Composite parent, int style) {
		button = new Button(parent, SWT.PUSH);
	}

	private boolean isShowMenu(int x, int y) {
		return showArrow && arrowBounds != null && arrowBounds.contains(x, y);
	}

	public void setShowArrow(boolean showArrow) {
		this.showArrow = showArrow;
		updatePadding();
		if (showArrow) {
			button.addPaintListener(paintListener);
		} else {
			button.removePaintListener(paintListener);
		}
	}

	public boolean isShowArrow() {
		return showArrow;
	}

	public void setText(String string) {
		button.setText(pad(string));
	}

	private String pad(String string) {
		return string == null ? null : padding == null ? string : string + padding;
	}

	private String unpad(String string) {
		return string == null ? null : padding == null || !string.endsWith(padding) ? string : string.substring(0,
				string.length() - padding.length());
	}

	public String getText() {
		String text = button.getText();
		return unpad(text);
	}

	public void setFont(Font font) {
		button.setFont(font);
		updatePadding();
	}

	private void updatePadding() {
		String text = getText();
		String currentPadding = padding;
		String newPadding = showArrow ? calculatePadding(22) : null;
		if ((newPadding == null && currentPadding != null)
				|| (newPadding != null && !newPadding.equals(currentPadding))) {
			this.padding = newPadding;
			setText(text);
		}
	}

	private String calculatePadding(int width) {
		int padSpaceWidth = calculateSpaceWidth();
		int count = (2 * width + padSpaceWidth - 1) / (2 * padSpaceWidth); //round up on 0.5
		switch (count) {
		case 0:
			return null;
		case 1:
			return " "; //$NON-NLS-1$
		case 2:
			return "  "; //$NON-NLS-1$
		case 3:
			return "    "; //$NON-NLS-1$
		case 4:
			return "     "; //$NON-NLS-1$
		case 5:
			return "      "; //$NON-NLS-1$
		case 6:
			return "       "; //$NON-NLS-1$
		case 7:
			return "        "; //$NON-NLS-1$
		case 8:
			return "         "; //$NON-NLS-1$
		case 9:
			return "          "; //$NON-NLS-1$
		case 10:
			return "           "; //$NON-NLS-1$
		default:
			//fall-through
		}
		StringBuilder bldr = new StringBuilder("           "); //$NON-NLS-1$
		for (int i = 10; i < count; i++) {
			bldr.append(' ');
		}
		return bldr.toString();
	}

	private int calculateSpaceWidth() {
		GC gc = new GC(button);
		try {
			gc.setFont(button.getFont());
			return gc.getAdvanceWidth(' ');
		} finally {
			gc.dispose();
		}
	}

	public Button getButton() {
		return button;
	}

	public void dispose() {
		button.dispose();
	}

	public Image getImage() {
		return button.getImage();
	}

	public void setImage(Image image) {
		button.setImage(image);
	}

	public Shell getShell() {
		return button.getShell();
	}

	public boolean isEnabled() {
		return button.isEnabled();
	}

	public boolean isVisible() {
		return button.isVisible();
	}

	public void addSelectionListener(final SelectionListener listener) {
		DropDownSelectionListenerWrapper wrapper = findWrapper(listener);
		if (wrapper == null) {
			wrapper = new DropDownSelectionListenerWrapper(listener);
			selectionListenerWrappers.add(wrapper);
		}
		button.addSelectionListener(wrapper);
		button.addMouseListener(wrapper);
	}

	private DropDownSelectionListenerWrapper findWrapper(final SelectionListener listener) {
		DropDownSelectionListenerWrapper wrapper = null;
		if (selectionListenerWrappers == null) {
			selectionListenerWrappers = new ArrayList<>();
		}
		for (DropDownSelectionListenerWrapper existingWrapper : selectionListenerWrappers) {
			if (existingWrapper.getDelegate() == listener) {
				wrapper = existingWrapper;
				break;
			}
		}
		return wrapper;
	}

	public void removeSelectionListener(SelectionListener listener) {
		DropDownSelectionListenerWrapper wrapper = findWrapper(listener);
		if (wrapper != null) {
			button.removeSelectionListener(wrapper);
		}
		button.removeSelectionListener(listener);
	}

	private final class DropDownSelectionListenerWrapper extends MouseAdapter implements SelectionListener {

		private final SelectionListener delegate;

		private boolean isShowMenu;

		public DropDownSelectionListenerWrapper(SelectionListener delegate) {
			this.delegate = delegate;
		}

		public SelectionListener getDelegate() {
			return delegate;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			setArrowDetail(e);
			delegate.widgetSelected(e);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			setArrowDetail(e);
			delegate.widgetDefaultSelected(e);
		}

		private void setArrowDetail(SelectionEvent e) {
			e.detail = isShowMenu ? SWT.ARROW : 0;
			isShowMenu = false;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			isShowMenu = isShowMenu(e.x, e.y);
		}
	}
}
