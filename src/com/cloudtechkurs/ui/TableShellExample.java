package com.cloudtechkurs.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cloudtechkurs.core.Task;

class ColumnInfo {
	public String mName;
	public int mWidth;
	
	public ColumnInfo(String name, int width) {
		mName = name;
		mWidth = width;
	}
}

public class TableShellExample {
	
	static List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
    static { 
    	columns.add(new ColumnInfo("Task ID", 100));
    	columns.add(new ColumnInfo("Task name", 200));
        columns.add(new ColumnInfo("Status", 200));
        columns.add(new ColumnInfo("Software", 300));
    }
    
	
  Display display;

  Shell shell;
  
  private Table mTable;

  TableShellExample() {
    display = new Display();
    shell = new Shell(display);

    shell.setMinimumSize(new Point(800, 600));
    
    shell.setText("A Table Shell Example");
    shell.setLayout(new GridLayout(1, false));
    

    mTable = new Table(shell, SWT.BORDER);
    GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, true);
    gd.minimumHeight = 400;
    mTable.setLayoutData(gd);
    
    mTable.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(e.item instanceof TableItem) {
				String selectedId = ((TableItem) e.item).getText();
				addTask(new Task());
			}
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
    
    for(ColumnInfo info : columns) {
    	 TableColumn column = new TableColumn(mTable, SWT.CENTER);
    	 column.setText(info.mName);
    	 column.setWidth(info.mWidth);
    }
    
    mTable.setHeaderVisible(true);
    
    addTask(new Task());

    shell.pack();
    shell.open();
    while (!shell.isDisposed() ){
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  public static void main(String[] argv) {
    new TableShellExample();
  }
  
  public void addTask(Task task) {
	  TableItem item = new TableItem(mTable, SWT.NONE);
	  item.setText(new String[] { task.getTaskId(), task.getTaskName(), task.getStatus(), 
	    		task.getSoftwareType().getName()});
  }
}
