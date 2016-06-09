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
	private Button mStop;
	private Button mDelete;
	private Button mAdd;
	
	private TaskRegistry mTaskRegistry = new TaskRegistry();

	MainWindow() {
		mTaskRegistry.load();
		
		initUI();
		
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
		
		System.out.println("FINISH");
	}
	
	private void initUI() {
		display = new Display();
		mShell = new Shell(display);
		mShell.setMinimumSize(new Point(800, 600));
		mShell.setText("FlyElephant Client");
		mShell.setLayout(new GridLayout(5, false));

		mTable = new Table(mShell, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, true, 5, 1);
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
			addTask(mTaskRegistry.get(i));
		}
		
		mApiKey = new Button(mShell, SWT.PUSH);
		mApiKey.setText("Set API key");
		gd = new GridData(SWT.BOTTOM, SWT.LEFT, false, false);
		mApiKey.setLayoutData(gd);

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
				System.out.println("DOUBLE CLICK");
			}
		});
		
		mDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String id = getSelectedId();
				if(id != null) {
					deleteTask(id);
				}
			}
		});
		
		mStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String id = getSelectedId();
				if(id != null) {
					stopTask(id);
				}
			}
		});
		
		mAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TaskInfoDialog dialog = new TaskInfoDialog(mShell);
				dialog.open();
				//TODO call dialog and return object from it...
			}
		});
	}
	
	private String getSelectedId() {
		TableItem[] items = mTable.getSelection();
		if(items.length > 0) {
			return items[0].getText();
		}
		return null;
	}

	// add task to UI
	public void addTask(Task task) {
		TableItem item = new TableItem(mTable, SWT.NONE);
		String[] content = new String[] { task.getTaskId(), task.getTaskName(), 
				task.getStatus(), task.getSoftwareType().getName() };
		item.setText(content);
	}
	
	public void deleteTask(String id) {
		try {
			mTaskRegistry.deleteTask(id);
			mTable.remove(mTable.getSelectionIndex());
		} catch (Exception e) {
			showMessage("Removal error", e.getMessage());
		}
	}
	
	public void stopTask(String id) { //TODO test
		try {
			mTaskRegistry.stopTask(id);
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
	
	public static void main(String[] argv) {
		new MainWindow();
	}
}
