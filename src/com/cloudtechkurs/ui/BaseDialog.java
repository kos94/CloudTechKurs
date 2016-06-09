package com.cloudtechkurs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class BaseDialog extends Dialog {
	protected final int mWidgetStyle;
	
	public BaseDialog(Shell parent, String title, boolean isReadOnly) {
		super(parent);
		setText(title);
		mWidgetStyle = isReadOnly? SWT.READ_ONLY : SWT.NONE;
	}
	
	protected void addOkButton(Composite parent) {
		Button okButton = new Button(parent, SWT.PUSH);
        okButton.setText("OK");
        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
        gd.widthHint = 80;
        okButton.setLayoutData(gd);
        
        okButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				onOkPressed(e);
			}
		});
	}
	
	protected void addLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}
	
	protected Text createText(Composite parent) {
		Text text = new Text(parent, mWidgetStyle);
        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gd.widthHint = 300;
        text.setLayoutData(gd);
        return text;
	}
	
	protected Shell createShell() {
		Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 15;
		layout.verticalSpacing = 20;
		layout.marginWidth = layout.marginHeight = 15;
        shell.setLayout(layout);
        return shell;
	}
	
	protected void runLoop(Shell shell) {
		shell.pack();
        Rectangle screenSize = shell.getDisplay().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, 
        		(screenSize.height - shell.getBounds().height) / 2);
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
	}
	
	protected abstract void onOkPressed(SelectionEvent event);
}
