package com.cloudtechkurs.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudtechkurs.core.InstanceType;
import com.cloudtechkurs.core.SoftwareType;
import com.cloudtechkurs.core.Task;

public class TaskInfoDialog extends Dialog {
	
	private Text mTaskName;
	private Text mResultName;
	private Text mRepository;
	private Combo mSoftwareType;
	private Combo mInstanceType;
	private Text mRunCommand;
	
	private Task mTask;
	
	public TaskInfoDialog(Shell parent, Task task) {
		super(parent);
		mTask = task;
		//TODO 
//		setLayout(new GridLayout(2, false));
	}
	
	public Task open() {
		
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        shell.setLayout(new GridLayout(2, false));
        
        addLabel(shell, "Task Name");
        mTaskName = createText(shell);
        
        addLabel(shell, "Result Name");
        mResultName = createText(shell);
        
        addLabel(shell, "Repository");
        mRepository = createText(shell);
        
        addLabel(shell, "Software type");
        mSoftwareType = new Combo(shell, SWT.NONE);
        String[] swTypes = new String[SoftwareType.values().length];
        int i = 0;
        for(SoftwareType type : SoftwareType.values()) {
        	swTypes[i++] = type.getName();
        }
        mSoftwareType.setItems(swTypes);
        
        addLabel(shell, "Instance type");
        mInstanceType = new Combo(shell, SWT.NONE);
        String[] instanceTypes = new String[InstanceType.values().length];
        i = 0;
        for(InstanceType type : InstanceType.values()) {
        	instanceTypes[i++] = type.getName();
        }
        mInstanceType.setItems(instanceTypes);
        
        addLabel(shell, "Run command");
        mRunCommand = createText(shell);
        
        initWidgetValues();
        
        shell.pack();
        Rectangle screenSize = shell.getDisplay().getBounds();
        shell.setLocation((screenSize.width - shell.getBounds().width) / 2, 
        		(screenSize.height - shell.getBounds().height) / 2);
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
        
        //TODO collect result
        
        return null;
	}
	
	private void initWidgetValues() {
		
	}
	
	private void addLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}
	
	private Text createText(Composite parent) {
		Text text = new Text(parent, SWT.NONE);
        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gd.widthHint = 300;
        text.setLayoutData(gd);
        return text;
	}
}
