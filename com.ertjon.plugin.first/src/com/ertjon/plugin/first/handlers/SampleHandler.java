package com.ertjon.plugin.first.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler implements IHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"First",
				"Hello, Eclipse world");*/
		
		final Shell shell = HandlerUtil.getActiveShell(event);
        final ISelection sel = HandlerUtil.getActiveMenuSelection(event);
        final IStructuredSelection selection = (IStructuredSelection) sel;

        final Object firstElement = selection.getFirstElement();
        if (firstElement instanceof ICompilationUnit) {
            //createOutput(shell, (ICompilationUnit) firstElement);
        } else {
            MessageDialog.openWarning(shell, "Companion Object Generation Warning", "Please select an entity object for generating a corresponding companion.");
        }
		return null;
	}
	
	
}
