package com.cloudtechkurs.ui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudtechkurs.core.InstanceType;
import com.cloudtechkurs.core.SoftwareType;
import com.cloudtechkurs.core.Task;

public class TaskInfoDialog extends BaseDialog {
	
	private Text mTaskName;
	private Text mResultName;
	private Text mRepository;
	private Combo mSoftwareType;
	private Combo mInstanceType;
	private Text mRunCommand;
	
	private Task mTask;
	
	public TaskInfoDialog(Shell parent, boolean isReadOnly) {
		super(parent, "Task info", isReadOnly);
	}
	
	public Task open(Task initialValues) {
        Shell shell = createShell();
        
        initUI(shell);
        
        initWidgetValues(initialValues);
        
        runLoop(shell);
        
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
        mSoftwareType = new Combo(parent, mWidgetStyle);
        String[] swTypes = new String[SoftwareType.values().length];
        int i = 0;
        for(SoftwareType type : SoftwareType.values()) {
        	swTypes[i++] = type.getName();
        }
        mSoftwareType.setItems(swTypes);
        
        addLabel(parent, "Instance type");
        mInstanceType = new Combo(parent, mWidgetStyle);
        String[] instanceTypes = new String[InstanceType.values().length];
        i = 0;
        for(InstanceType type : InstanceType.values()) {
        	instanceTypes[i++] = type.getName();
        }
        mInstanceType.setItems(instanceTypes);
        
        addLabel(parent, "Run command");
        mRunCommand = createText(parent);
        
        addOkButton(parent);
	}
	
	private void initWidgetValues(Task task) {
		mTaskName.setText(task.getTaskName());
		mResultName.setText(task.getResultName());
		mRepository.setText(task.getRepository());
		mSoftwareType.select(task.getSoftwareType().ordinal());
		mInstanceType.select(task.getInstanceType().ordinal());
		mRunCommand.setText(task.getRunCommand());
	}

	@Override
	protected void onOkPressed(SelectionEvent event) {
		mTask = new Task(mTaskName.getText(),
				mResultName.getText(),
				mRepository.getText(),
				SoftwareType.values()[ mSoftwareType.getSelectionIndex() ],
				InstanceType.values()[ mInstanceType.getSelectionIndex() ],
				mRunCommand.getText());
		
		event.widget.getDisplay().getActiveShell().dispose();
	}
}
