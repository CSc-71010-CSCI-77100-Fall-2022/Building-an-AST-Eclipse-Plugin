# Building-an-AST-Eclipse-Plugin

## Introduction

This document describes how to build a relatively useless eclipse plug-in. The plug-in adds an option to the drop down menu that appears when right clicking on a user defined method (i.e., not a library method) in the Project Explorer frame. When the option is selected a dialog box will appear that states the total number of user defined methods in the project along with the number of method calls (both to library and to user defined methods) made by the defining class of the method the option was invoked on. The goal is to show an interface with some of the entities that Eclipse makes available, such as `IMethod`s, `IClassFile`s, `ICompilationUnit`s, etc. It also demonstrates how to build an Abstract Syntax Tree (AST) and how to utilize the visitor pattern to traverse its nodes.

**Note**: This document is meant to be used in conjunction with `InfoGatherer.java` and `MyVisitor.java`. You should read and understand both of these files.

## Building AST Traverse Plug-in

1. If you haven't done so already, download Eclipse. There are different configurations of Eclipse available; I recommend [Eclipse for RCP and RAP Developers][eclipse]. There's also an [installer][installer] you can use but be sure to pick the RCP and RAP developer package. If you already have Eclipse and want to use that installation, you may need to install additional plug-ins.

1. Open Eclipse.

1. In the Workspace Launcher window create a new workspace (e.g., `C:\sandbox\ASTTrav`). This will be the folder that contains all the code and classes for the plug-in.

1. If the Welcome screen is open close it.

1. Select Window -> Perspective -> Open Perspective -> Other.

1. In the "Open Perspective" dialog box select "Plug-in Development." This tells Eclipse that you will be creating a plug-in rather then a Java application or other entity. Eclipse has a unique look and tool set for each perspective.

1. Select File -> New -> Project.

1. In the "New Project" dialog window select Plug-in Development -> Plug-in Project.

1. In "New Plug-in Project" enter "ASTTrav" as the project name and hit next.

1. The Plug-in Content page should be in focus hit next.

1. In the Templates window ensure the "Create a ..." check box is checked and select "Hello, World Command," hit next.

1. In the Sample Command Contribution form, change the value of the "Handler Class Name" field to ASTTravHandler.

1. Upon hitting finish you should see the ASTTrav project in the Project Explorer frame Expand it and double click the "plugin.xml."

1. You should now see a page in the main window with multiple tabs along the bottom. Select the "Extensions" tab, thus bringing the Extensions page into focus.

1. In the "All Extensions" pane of this window:

    1. Expand the "org.eclipse.ui.commands" extension and select "Sample Command" extension element. In the form on the right, change the "name" field value to "Traverse AST." 

    1. Right-click "org.eclipse.ui.menus" extension then select New -> menuContribution. This will create a new menu contribution extension element and select it automatically. You are now contributing a new menu to Eclipse.

    1. In the "Extension Element Details" form on the right, change the field value of "locationURI" to "popup:org.eclipse.jdt.ui.source.menu?after=additions". This tells Eclipse that your new menu items will go under the existing "Source" menu, which already has operations that deal with source code (e.g., formatting). 

    1. Right-click the new menu contribution you just created and select New -> command. This element associates a "command" that will execute when the user selects your item.

    1. In the "Extension Element Details" form on the right: 
    
        1. Select the "Browse" button associated with the commandId field and select the (first) entry "ASTTrav.commands.sampleCommand". This is the identifier of a command that was created when you invoked the Hello World wizard. 

        1. Change the label field value to "Traverse AST."

    1. Right-click command element you just created (on the left pane) and, this time, select New > visibleWhen. That will create a new element. Right-click that element and select New -> with.

    1. In the new "with" element, change its "variable" field value on the right to "selection." This generates a variable that captures what the user selected in the UI, but we'll get at this element a simpler way.

    1. Right-click the new "with" element and select New -> iterate. This allows us to tell Eclipse the *kinds* of elements our menu option will be applicable for.

    1. For the newly created "with" element, on the right Extension Element Details pane, set a value of "or" for the operator field and a value of "false" for the ifEmpty field.

    1. Right-click "iterate" and select New -> instanceof. Here, we'll specify the type of the element in the Java Model for which our command will apply. On the right, change the "value" field value to "org.eclipse.jdt.core.IMethod". This means that our command will apply to Java methods. 
    
1. Now select the "Dependencies" tab at the bottom of the main window pane. Add "org.eclipse.jdt.core", "org.eclipse.core.resources", and "org.eclipse.core.runtime" to the "Required Plug-ins" list if they are not already there.

1. Save all of this by either pressing CTRL-S or selecting File -> Save from the toolbar menu.

1. Add the included `MyVisitor.java` file and the `InfoGatherer.java` file to the `asttrav.handlers` package (this can be done by creating new classes of the same name and coping the code or copying the files right clicking on the package in the "Project Explorer" pane and selecting paste.) **NOTE**: These files are heavily commented and should be reviewed carefully.

1. In the "Project Explorer" pane, open `asttrav.handlers.ASTTravHandler.java`. The source code should be in the main window pane.

1. Import the following packages:
    - `org.eclipse.jface.viewers.*`
    - `org.eclipse.jdt.core.*`

    and add the collowing code to the class:
    ```java
    //This method converts a Selection to an object
    public static Object getSingleElement(ISelection s) {
        if (!(s instanceof IStructuredSelection))
            return null;
        IStructuredSelection selection = (IStructuredSelection) s;
        if (selection.size() != 1)
            return null;
        return selection.getFirstElement();
    }

    //Converts an object to an IMethod
    private static IMethod getSelectedMethod(Object element) {
        IMethod method = null;
        if (element instanceof IMethod) {
            method = (IMethod) element;
        }
        return method;
    }
    ```

1. Delete the current `execute()` method and replace it with:
    ```java
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
    ```

## Testing Your Newly Created Plug-in

1. In the "Project Explorer" frame, again select the "plugin.xml" file.

1. Select the "Overview" tab.

1. Under "Testing," select "Launch an Eclipse Application." This should open a new eclipse window with a clean workspace. If you would like to load a different workspace (like one that already has projects), you can close the new Eclipse instance, right-click the "Project Explorer" in the original instance, and select Run As -> Run Configurations.... You can change the "Workspace Data" location field to point to your desired workspace. You may also wish to change arguments, such as VM arguments that would increase your program's memory. For example, you can select the "Arguments" tab and enter -Xmx8g in the "VM Arguments" field to set your maximum heap size 8GB of RAM.

1. In the newly opened instance of Eclipse, use the "Project Explorer" frame, browse to a user-defined method. In this same frame, right-click on a method and select the Source -> Traverse AST option.

[eclipse]: https://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/oxygenr
[installer]: https://www.eclipse.org/downloads/
