package com.cloudtechkurs.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
		setText("Task info");
		mTask = task;
		//TODO 
//		setLayout(new GridLayout(2, false));
	}
	
	public Task open(Task initialValues) {
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 15;
		layout.verticalSpacing = 20;
		layout.marginWidth = layout.marginHeight = 15;
        shell.setLayout(layout);
        
        initUI(shell);
        
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
        
        return mTask;
	}
	
	private void initUI(Composite parent) {
		addLabel(parent, "Task Name");
        mTaskName = createText(parent);
        
        addLabel(parent, "Result Name");
        mResultName = createText(parent);
        
        addLabel(parent, "Repository");
        mRepository = createText(parent);
        
        addLabel(parent, "Software type");
        mSoftwareType = new Combo(parent, SWT.NONE);
        String[] swTypes = new String[SoftwareType.values().length];
        int i = 0;
        for(SoftwareType type : SoftwareType.values()) {
        	swTypes[i++] = type.getName();
        }
        mSoftwareType.setItems(swTypes);
        
        addLabel(parent, "Instance type");
        mInstanceType = new Combo(parent, SWT.NONE);
        String[] instanceTypes = new String[InstanceType.values().length];
        i = 0;
        for(InstanceType type : InstanceType.values()) {
        	instanceTypes[i++] = type.getName();
        }
        mInstanceType.setItems(instanceTypes);
        
        addLabel(parent, "Run command");
        mRunCommand = createText(parent);
        
        Button okButton = new Button(parent, SWT.PUSH);
        okButton.setText("OK");
        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
        gd.widthHint = 80;
        okButton.setLayoutData(gd);
        
        okButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateTask();
				e.widget.getDisplay().getActiveShell().dispose();
			}
		});
	}
	
	private void initWidgetValues() {
		mTaskName.setText(mTask.getTaskName());
		mResultName.setText(mTask.getResultName());
		mRepository.setText(mTask.getRepository());
		mSoftwareType.select(mTask.getSoftwareType().ordinal());
		mInstanceType.select(mTask.getInstanceType().ordinal());
		mRunCommand.setText(mTask.getRunCommand());
	}
	
	private void updateTask() {
		mTask = new Task(mTaskName.getText(),
				mResultName.getText(),
				mRepository.getText(),
				SoftwareType.values()[ mSoftwareType.getSelectionIndex() ],
				InstanceType.values()[ mInstanceType.getSelectionIndex() ],
				mRunCommand.getText());
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
