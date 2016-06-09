package com.cloudtechkurs.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cloudtechkurs.core.Task;
import com.cloudtechkurs.core.TaskRegistry;

class ColumnInfo {
	public String mName;
	public int mWidth;
	
	public ColumnInfo(String name, int width) {
		mName = name;
		mWidth = width;
	}
}

public class MainWindow {
	
	static List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
    static { 
    	columns.add(new ColumnInfo("Task ID", 100));
    	columns.add(new ColumnInfo("Task name", 200));
        columns.add(new ColumnInfo("Status", 200));
        columns.add(new ColumnInfo("Software", 300));
    }

	Display display;

	Shell mShell;

	private Table mTable;
	private Button mApiKey;
	private Button mRefresh;
	private Button mStop;
	private Button mDelete;
	private Button mAdd;
	
	private TaskRegistry mTaskRegistry = new TaskRegistry();

	public MainWindow() {
		mTaskRegistry.load();
		
		initUI();
		
		refreshTaskStatus();
		
		addListeners();

		mShell.pack();
		Rectangle screenSize = mShell.getDisplay().getBounds();
		mShell.setLocation((screenSize.width - mShell.getBounds().width) / 2, 
        		(screenSize.height - mShell.getBounds().height) / 2);
		mShell.open();
		while (!mShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		
		mTaskRegistry.store();
	}
	
	private void initUI() {
		display = new Display();
		mShell = new Shell(display);
		mShell.setMinimumSize(new Point(800, 600));
		mShell.setText("FlyElephant Client");
		mShell.setLayout(new GridLayout(6, false));

		mTable = new Table(mShell, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, true, 6, 1);
		gd.minimumHeight = 500;
		mTable.setLayoutData(gd);
		mTable.setHeaderVisible(true);
		for (ColumnInfo info : columns) {
			TableColumn column = new TableColumn(mTable, SWT.CENTER);
			column.setText(info.mName);
			column.setWidth(info.mWidth);
		}
		//add tasks from the registry
		for(int i=0; i<mTaskRegistry.getTaskCount(); i++) {
			addTaskToUI(mTaskRegistry.get(i));
		}
		
		mApiKey = new Button(mShell, SWT.PUSH);
		mApiKey.setText("Set API key");
		gd = new GridData(SWT.BOTTOM, SWT.LEFT, false, false);
		gd.widthHint = 80;
		mApiKey.setLayoutData(gd);
		
		mRefresh = new Button(mShell, SWT.PUSH);
		mRefresh.setText("Refresh");
		mRefresh.setLayoutData(gd);

		Label emptySpace = new Label(mShell, SWT.NONE);
		gd = new GridData(SWT.BOTTOM, SWT.RIGHT, true, false);
		emptySpace.setLayoutData(gd);

		mStop = new Button(mShell, SWT.PUSH);
		mStop.setText("Stop");
		gd = new GridData(SWT.BOTTOM, SWT.RIGHT, false, false);
		mStop.setLayoutData(gd);
		mStop.setEnabled(false);
		
		mDelete = new Button(mShell, SWT.PUSH);
		mDelete.setText("Delete");
		mDelete.setLayoutData(gd);
		mDelete.setEnabled(false);

		mAdd = new Button(mShell, SWT.PUSH);
		mAdd.setText("New task");
		mAdd.setLayoutData(gd);
	}
	
	private void addListeners() {
		mTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("MOUSE UP");
				System.out.println(mTable.getSelection().length);
				if(mTable.getSelection().length > 0) {
					mStop.setEnabled(true);
					mDelete.setEnabled(true);
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				showTaskInfo(mTable.getSelectionIndex());
			}
		});
		
		mApiKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ApiKeyDialog dialog = new ApiKeyDialog(mShell);
				String result = dialog.open(mTaskRegistry.getApiKey());
				if(result != null) {
					mTaskRegistry.setApiKey(result);
				}
			}
		});
		
		mRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTaskStatus();
			}
		});
		
		mDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteTask(mTable.getSelectionIndex());
			}
		});
		
		mStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stopTask(mTable.getSelectionIndex());
			}
		});
		
		mAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TaskInfoDialog dialog = new TaskInfoDialog(mShell, false);
				Task result = dialog.open(new Task());
				if(result != null) {
					addTask(result);
				}
			}
		});
	}

	// add task to UI
	public void addTaskToUI(Task task) {
		TableItem item = new TableItem(mTable, SWT.NONE);
		String[] content = new String[] { task.getTaskId(), task.getTaskName(), 
				task.getStatus(), task.getSoftwareType().getName() };
		item.setText(content);
	}
	
	public void showTaskInfo(int index) {
		Task task = mTaskRegistry.get(index);
		if(task != null) {
			TaskInfoDialog dialog = new TaskInfoDialog(mShell, true);
			dialog.open(task);
		}
	}
	
	public void addTask(Task task) {
		try {
			mTaskRegistry.addTask(task);
			addTaskToUI(task);
		} catch (Exception e) {
			showMessage("Addition error", e.getMessage());
		}
	}
	
	public void deleteTask(int index) {
		try {
			mTaskRegistry.deleteTask(index);
			mTable.remove(index);
			if(mTable.getItemCount() == 0) {
				mStop.setEnabled(false);
				mDelete.setEnabled(false);
			}
		} catch (Exception e) {
			showMessage("Removal error", e.getMessage());
		}
	}
	
	public void stopTask(int index) {
		try {
			mTaskRegistry.stopTask(index);
			updateLabels(index, mTaskRegistry.get(index));
		}  catch (Exception e) {
			showMessage("Error", e.getMessage());
		}
	}
	
	private void showMessage(String title, String message) {
		MessageBox box = new MessageBox(mShell, SWT.CANCEL | SWT.OK);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}
	
	private void updateLabels(int rowIndex, Task task) {
		TableItem item = mTable.getItem(rowIndex);
		
		String[] content = new String[] { task.getTaskId(), task.getTaskName(), 
				task.getStatus(), task.getSoftwareType().getName() };
		item.setText(content);
	}
	
	private void refreshTaskStatus() {
		mTaskRegistry.refreshTasksStatus();
		for(int i=0; i<mTaskRegistry.getTaskCount(); i++) {
			updateLabels(i, mTaskRegistry.get(i));
		}
	}
	
	public static void main(String[] argv) {
		new MainWindow();
	}
}
