/*
 * A visitor for abstract syntax trees. 

For each different concrete AST node type T there are a pair of methods: 

public boolean visit(T node) - Visits the given node to perform some arbitrary operation. 
If true is returned, the given node's child nodes will be visited next; however, if false 
is returned, the given node's child nodes will not be visited. The default implementation 
provided by this class does nothing and returns true (with the exception of ASTVisitor.visit
(Javadoc)). Subclasses may reimplement this method as needed. 
public void endVisit(T node) - Visits the given node to perform some arbitrary operation.
When used in the conventional way, this method is called after all of the given node's 
children have been visited (or immediately, if visit returned false). The default 
implementation provided by this class does nothing. Subclasses may reimplement this method
as needed. 

In addition, there are a pair of methods for visiting AST nodes in the abstract, regardless 
of node type: 
public void preVisit(ASTNode node) - Visits the given node to perform some arbitrary 
operation. This method is invoked prior to the appropriate type-specific visit method. 
The default implementation of this method does nothing. Subclasses may reimplement this 
method as needed. 
public void postVisit(ASTNode node) - Visits the given node to perform some 
arbitrary operation. This method is invoked after the appropriate type-specific 
endVisit method. The default implementation of this method does nothing. Subclasses 
may reimplement this method as needed. 
For nodes with list-valued properties, the child nodes within the list are visited in order. 
For nodes with multiple properties, the child nodes are visited in the order that most 
closely corresponds to the lexical reading order of the source program. For instance, for 
a type declaration node, the child ordering is: name, superclass, superinterfaces, and body 
declarations. 

While it is possible to modify the tree in the visitor, care is required to ensure that 
the consequences are as expected and desirable. During the course of an ordinary visit 
starting at a given node, every node in the subtree is visited exactly twice, first with 
visit and then with endVisit. During a traversal of a stationary tree, each node is either 
behind (after endVisit), ahead (before visit), or in progress (between visit and the matching 
endVisit). Changes to the "behind" region of the tree are of no consequence to the visit in 
progress. Changes to the "ahead" region will be taken in stride. Changes to the "in progress" 
portion are the more interesting cases. With a node, the various properties are arranged in a 
linear list, with a cursor that separates the properties that have been visited from the ones 
that are still to be visited (the cursor is between the elements, rather than on an element). 
The cursor moves from the head to the tail of this list, advancing to the next position just 
before visit if called for that child. After the child subtree has been completely visited, 
the visit moves on the child immediately after the cursor. Removing a child while it is being 
visited does not alter the course of the visit. But any children added at positions after the 
cursor are considered in the "ahead" portion and will be visited. 


 */
package asttrav.handlers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.IMethod;

public class MyVisitor extends ASTVisitor {
	private IMethod mainMeth;
	private int numCalls = 0;
	private int numNodes = 0;

	public MyVisitor() {
		super();
	}

	// ---
	// This returns the number of nodes that was encountered by
	// this visitor
	// ---
	public int getNumberOfNodes() {
		return this.numNodes;
	}

	// ---
	// This returns the number of method invocations
	// counted by this visitor
	// ----
	public int getNumberOfMethodCalls() {
		return this.numCalls;
	}

	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
	}

	@Override
	public void postVisit(ASTNode node) {
		this.numNodes++;
		super.postVisit(node);
	}

	public boolean visitNode(ASTNode node) {
		return true;
	}

	// ======================================================

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ArrayAccess node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ArrayCreation node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ArrayType node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(Assignment node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(CastExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(CatchClause node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(CompilationUnit node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		return visitNode(node);
	}

	// ---
	// We are going to count constructors as
	// methods thus we need to increment numCalls
	// ---
	@Override
	public boolean visit(ConstructorInvocation node) {
		this.numCalls++;
		return visitNode(node);
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(FieldAccess node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(InfixExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(Initializer node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		return visitNode(node);
	}

	// ---
	// This node is of type MethodInvocation thus
	// this is one method call for this class file
	// ---
	@Override
	public boolean visit(MethodInvocation node) {
		this.numCalls++;
		return visitNode(node);
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(QualifiedName node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(QualifiedType node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(ReturnStatement node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(SimpleType node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		return visitNode(node);
	}

	// ---
	// Yup we're counting calls to super() as well
	// ---
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		this.numCalls++;
		return visitNode(node);
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		return visitNode(node);
	}

	// ---
	// super.X() counts as well
	// ---
	@Override
	public boolean visit(SuperMethodInvocation node) {
		this.numCalls++;
		return visitNode(node);
	}

	@Override
	public boolean visit(ThisExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(TypeLiteral node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		return visitNode(node);
	}

	@Override
	public boolean visit(AssertStatement node) {
		return true; // recursive eval
	}

	@Override
	public boolean visit(WhileStatement node) {
		return true;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		return true;
	}

	@Override
	public boolean visit(Block node) {
		return true; // recursive
	}

	@Override
	public boolean visit(LabeledStatement node) {
		return true; // recursive
	}

	@Override
	public boolean visit(SwitchCase node) {
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		return true;
	}

	@Override
	public boolean visit(DoStatement node) {
		return true;
	}

	@Override
	public boolean visit(ThrowStatement node) {
		return true;
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		return true;
	}

	@Override
	public boolean visit(TryStatement node) {
		return true;
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		return true;
	}

	@Override
	public boolean visit(StringLiteral node) {
		return false;
	}

	@Override
	public boolean visit(PrimitiveType node) {
		return false;
	}

	@Override
	public boolean visit(NullLiteral node) {
		return false;
	}

	@Override
	public boolean visit(NumberLiteral node) {
		return false;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(BlockComment node) {
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		return false;
	}

	@Override
	public boolean visit(TagElement node) {
		return false; // comments; don't care
	}

	@Override
	public boolean visit(TextElement node) {
		return false; // comments; don't care
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		return false;
	}

	@Override
	public boolean visit(BreakStatement node) {
		return false;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		return false;
	}

	@Override
	public boolean visit(Modifier node) {
		return false;
	}

	@Override
	public boolean visit(Javadoc node) {
		return false;
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		return false;
	}

	@Override
	public boolean visit(LineComment node) {
		return false;
	}

	@Override
	public boolean visit(MethodRef node) {
		return false; // comments
	}

	@Override
	public boolean visit(MethodRefParameter node) {
		return false; // comments
	}

	@Override
	public boolean visit(MemberRef node) {
		return false; // comments
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(WildcardType node) {
		return false; // Java2 1.5
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		return false; // Java2 1.5
	}

	@Override
	public boolean visit(ParameterizedType node) {
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(TypeParameter node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(MemberValuePair node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(EnumConstantDeclaration node) {
		return false; // 1.5
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		return false; // 1.5
	}

	// There are corresponding endVisit methods
}