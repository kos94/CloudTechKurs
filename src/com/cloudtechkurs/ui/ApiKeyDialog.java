package com.cloudtechkurs.ui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ApiKeyDialog extends BaseDialog {
	private Text mKey;
	private String mKeyValue;
	
	public ApiKeyDialog(Shell parent) {
		super(parent, "API key", false);
	}
	
	public String open(String initialValue) {
        Shell shell = createShell();

        addLabel(shell, "API key");
        mKey = createText(shell);
        addOkButton(shell);

        runLoop(shell);
        
        return mKeyValue;
	}
	
	@Override
	protected void onOkPressed(SelectionEvent event) {
		mKeyValue = mKey.getText();
		event.widget.getDisplay().getActiveShell().dispose();
	}
}
