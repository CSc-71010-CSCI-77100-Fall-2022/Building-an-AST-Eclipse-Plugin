package asttrav.handlers;

import java.util.ArrayList;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.core.runtime.*;

public class InfoGatherer {

	// ---
	// This method returns the number of user defined methods in the
	// workspace on which this plug-in was invoked
	// ---
	public int getNumberOfUserMethods() {
		int total = 0;
		// ---
		// This gets the list of projects in the "Package Explorer" frame in the
		// instance of eclipse on which this plug-in is invoked
		// ----
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		// ---
		// Go through all the projects return above and save only the ones that
		// are Java-capable.
		// ---
		List javaprojects = new ArrayList();
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			//
			try {// throws exceptions if the project is not open or does not exist
					// ---
					// Check and see if we can access the project and if the project might
					// contain java code
					// ---
				if (project.isAccessible() && project.hasNature(JavaCore.NATURE_ID)) {
					javaprojects.add(JavaCore.create(project));
				}
			} catch (CoreException ex) {
				ex.printStackTrace();
			}
		} // ends for(int i = 0; i<projects.length; i++)

		// ---
		// Now that we have all the projects that we can access and that might also
		// contain Java code we need to go through them and count the number of user
		// defined methods.
		// ---
		try {
			for (int i = 0; i < javaprojects.size(); i++) {

				// ---
				// Now we must get all the Packages in the current project. To do this we
				// get the package fragments. A package fragment is a portion of the workspace
				// corresponding to an entire package, or to a portion thereof. The distinction
				// between a package fragment and a package is that a package with some name is
				// the union of all package fragments in the class path which have the same
				// name.
				// Note the casting to a IJavaProject we know this is a safe action because
				// we checked the projects nature above
				// ---
				IPackageFragment[] pfrag = ((IJavaProject) javaprojects.get(i)).getPackageFragments();

				// ---
				// now we iterator over the packages contained in the current project
				// ---
				for (int k = 0; k < pfrag.length; k++) {

					// ---
					// If the package does not contain Java source code we don't want to look
					// at it. IPackageFragmentRoot.K_SOURCE indicates that the package does contain
					// source IPackageFragmentRoot.K_BINARY would indicate that the package
					// contained
					// binary files (EX a package in the Java library).
					// ---
					if (pfrag[k].getKind() != IPackageFragmentRoot.K_SOURCE)
						continue;

					// ---
					// Now that we've determined that we've determined that the fragment contains
					// source we need to get the compilation units contained in it.
					// An ICompilationUnit represents an entire Java compilation unit (.java source
					// file).
					// ---
					ICompilationUnit[] icu = pfrag[k].getCompilationUnits();

					// ---
					// Now we iterate over all the .java files contained in this package
					// ---
					for (int y = 0; y < icu.length; y++) {

						// ---
						// Each java file (compilationUnit) may define multiple types (ie classes).
						// So we must get each type and iterate through them to find the methods.
						// From the types
						// ---
						IType[] allTypes = icu[y].getAllTypes();
						for (int tc = 0; tc < allTypes.length; tc++) {

							// ---
							// once we have a type be can get all the methods in the type
							// ---
							IMethod[] im = allTypes[tc].getMethods();

							// ----
							// Here we iterate through the methods, we double check and make sure they
							// are user defined print their name and increase our total
							// ---
							for (int mc = 0; mc < im.length; mc++) {

								if (im[mc].isBinary())
									continue;
								// System.out.println(im[mc].getElementName());
								total++;
							} // end for(int mc = 0; mc<im.length; mc++)
						} // end for(int tc = 0; tc<allTypes.length; tc++)
					} // end for(int y = 0; y<icu.length; y++)
				} // end for(int k = 0; k<pfrag.length; k++)
			} // end for(int i=0; i<javaprojects.size(); i++)
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return total;
	}// end getNumberOfUserMethods()

	// ---
	// This method takes an IMethod and returns the number of method
	// calls made from all the methods in the declaring class of the method.
	// To do this the method creates an Abstract Syntax Tree (AST) for the
	// Declaring Class of the IMethod and traverses it looking for methods
	// invocations.
	// It also prints the number of nodes in the AST for the class.
	// ---
	public int getNumberOfMethodCalls(IMethod method) {
		// sanity check just ensures that method isn't null
		if (method == null) {
			System.out.println("The IMethod pasted to getNumberOfMethodCalls is null");
			return -1;
		}

		// ---
		// This is a Java language parser for creating abstract syntax trees (ASTs).
		// The AST.JLS2 actual specifies that the parser will should work with
		// JLS2 (J2SE 1.4)
		// ---
		ASTParser parser = ASTParser.newParser(AST.JLS2);

		// ---
		// If setResolveBindings(true), the various names and types appearing in the
		// AST can be resolved to "bindings" by calling the resolveBinding methods.
		// These bindings draw connections between the different parts of a program,
		// and generally afford a more powerful vantage point for clients who wish
		// to analyze a program's structure more deeply. However they are very
		// expensive.
		// ---
		parser.setResolveBindings(true);

		// ---
		// Since the IMethod passed in is expected to be user defined we
		// get the ICompilationUnit (.java) however this would work just as
		// well for IMethods that are Binary since but insted of getting the
		// ICompilationUnit which is null for binary methods we could get the
		// IClassFile (.class) and set that as the source for the parser
		// ---
		ICompilationUnit icu = method.getCompilationUnit();
		// sanity check to make sure that icu is not null;
		if (icu == null) {
			System.out.println("The IMethod past to getNumberOfMethodCalls is not user defined");
			return -1;
		}

		// ---
		// The source can be an IClassFile or an ICompilationUnit.
		// ---
		parser.setSource(icu);
		// ---
		// Creates an abstract syntax tree. We pass null for the progressmonitor
		// ---
		ASTNode node = parser.createAST(null);
		// ---
		// ASTs support the visitor pattern. Read the notes in the MyVisitor.java file.
		// ---
		MyVisitor visitor = new MyVisitor();
		node.accept(visitor);

		System.out.println("There were " + visitor.getNumberOfNodes() + " node in "
				+ method.getCompilationUnit().getElementName());
		return visitor.getNumberOfMethodCalls();

	}// end getNumberOfMethodCalls(IMethod method)

}// end InfoGatherer