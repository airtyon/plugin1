package com.ertjon.plugin.first.handlers;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.ertjon.plugin.first.wizard.MyWizard;

public class SampleE4Handler 
{
	@Execute
	public void execute(Shell shell)
	{
		WizardDialog wd=new WizardDialog(shell, new MyWizard());
		
		if (wd.open()==Window.OK)
		{
			System.out.println("OK");
		}
		else
		{
			System.out.println("Cancel");
			/*IWorkbenchWindow window =
				    PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ISelection selection = window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");*/
			
		        
		    
		}
	}
}
