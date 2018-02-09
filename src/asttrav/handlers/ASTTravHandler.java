package asttrav.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.jface.viewers.*;
import org.eclipse.jdt.core.*;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ASTTravHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		// Get the IMethod that this action was invoked on
		IStructuredSelection selection = HandlerUtil.getCurrentStructuredSelection(event);

		// Get the IMethod that this action was invoked on
		Object element = getSingleElement(selection);
		IMethod method = getSelectedMethod(element);

		// Get the info on the method
		InfoGatherer ig = new InfoGatherer();

		int x = ig.getNumberOfUserMethods();
		int y = ig.getNumberOfMethodCalls(method);

		MessageDialog.openInformation(window.getShell(), "ASTTrav Plug-in",
				"The number of user defined methods = " + x + ". There were " + y + " method calls made from "
						+ method.getCompilationUnit().getElementName() + " which is the declaring class of "
						+ method.getElementName() + ", the method you selected.");

		return null;
	}

	// This method converts a Selection to an object
	public static Object getSingleElement(ISelection s) {
		if (!(s instanceof IStructuredSelection))
			return null;
		IStructuredSelection selection = (IStructuredSelection) s;
		if (selection.size() != 1)
			return null;
		return selection.getFirstElement();
	}

	// Converts an object to an IMethod
	private static IMethod getSelectedMethod(Object element) {
		IMethod method = null;
		if (element instanceof IMethod) {
			method = (IMethod) element;
		}
		return method;
	}
}
