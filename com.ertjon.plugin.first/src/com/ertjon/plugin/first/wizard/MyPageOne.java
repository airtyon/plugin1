package com.ertjon.plugin.first.wizard;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class MyPageOne extends WizardPage
{

	private Text text1;
	private Composite container;

	FileDialog fd;

	Button b1;

	public MyPageOne() 
	{
		super("First Page");
		setTitle("First Page");
		setDescription("Fake Wizard: First page");

	}

	@Override
	public void createControl(Composite parent) 
	{
		container=new Composite(parent, SWT.NONE);

		GridLayout layout=new GridLayout();
		container.setLayout(layout);
		layout.numColumns=3;
		Label label1=new Label(container, SWT.NONE);
		label1.setText("Zgjidhe filen");


		text1=new Text(container, SWT.BORDER|SWT.SINGLE);
		text1.setText("");
		text1.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				if (!text1.getText().isEmpty())
				{
					setPageComplete(true);
				}
			}

		});

		b1=new Button(this.container,SWT.NONE);
		b1.setText("Hapu");
		fd=new FileDialog(b1.getShell(), SWT.OPEN);
		b1.addListener(SWT.Selection, new BListener());

		GridData gd=new GridData(GridData.FILL_HORIZONTAL);
		text1.setLayoutData(gd);
		setControl(container);
		setPageComplete(false);
	}

	public String getText1() 
	{
		return text1.getText();
	}

	private class BListener implements Listener
	{
		private String extension;
		private String path;



		public BListener() 
		{
		}

		public BListener(String extension, String path)
		{
			this.extension=extension;
			this.path=path;
		}

		public String getExtension() {
			return extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public void handleEvent(Event event) 
		{
			if (event.type==SWT.Selection)
			{
				fd.setFilterExtensions(new String [] {"*"});
				fd.setFilterPath("c:\\temp");
				String result = fd.open();
				text1.setText(result);

				IProject project=ResourcesPlugin.getWorkspace().getRoot().getProject("Test");
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try 
				{
					project.create(null);
					project.open(null);
				} 
				catch (CoreException e) 
				{
					e.printStackTrace();
				}

				try
				{
					IProjectDescription description=project.getDescription();
					description.setNatureIds(new String[] {JavaCore.NATURE_ID});
					project.setDescription(description, null);

					IJavaProject javaProject=JavaCore.create(project);
					IClasspathEntry[] cpentry= new IClasspathEntry[] {
							JavaCore.newSourceEntry(javaProject.getPath()), 
							JavaRuntime.getDefaultJREContainerEntry()
					};

					javaProject.setRawClasspath(cpentry, javaProject.getPath(),null);
					HashMap options=new HashMap<>();
					options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
					options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, "4");
					javaProject.setOptions(options);

					IPackageFragmentRoot root=javaProject.getPackageFragmentRoot(project);
					IPackageFragment pack1=root.createPackageFragment("test1", false, null);
					StringBuffer buf=new StringBuffer();

					buf.append("package test1;\n");
					buf.append("public class E {\n");
					buf.append("    public void foo(int i) {\n");
					buf.append("        while (--i > 0) {\n");
					buf.append("            System.beep();\n");
					buf.append("        }\n");		
					buf.append("    }\n");
					buf.append("}\n");	

					ICompilationUnit cu=pack1.createCompilationUnit("E.java", buf.toString(), false, null);

					/*ASTParser parser= ASTParser.newParser(AST.JLS3);
					parser.setSource(cu);
					parser.setResolveBindings(false);
					CompilationUnit astRoot= (CompilationUnit) parser.createAST(null);
					AST ast= astRoot.getAST();

					// create the descriptive ast rewriter
					ASTRewrite rewrite= ASTRewrite.create(ast);

					// get the block node that contains the statements in the method body
					TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
					MethodDeclaration methodDecl= typeDecl.getMethods()[0];
					Block block= methodDecl.getBody();

					// create new statements to insert
					MethodInvocation newInv1= ast.newMethodInvocation();
					newInv1.setName(ast.newSimpleName("bar1"));
					Statement newStatement1= ast.newExpressionStatement(newInv1);

					MethodInvocation newInv2= ast.newMethodInvocation();
					newInv2.setName(ast.newSimpleName("bar2"));
					Statement newStatement2= ast.newExpressionStatement(newInv2);

					// describe that the first node is inserted as first statement in block, the other one as last statement
					// note: AST is not modified by this
					ListRewrite listRewrite= rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
					listRewrite.insertFirst(newStatement1, null);
					listRewrite.insertLast(newStatement2, null);

					// evaluate the text edits corresponding to the described changes. AST and CU still unmodified.
					TextEdit res= rewrite.rewriteAST();

					// apply the text edits to the compilation unit
					Document document= new Document(cu.getSource());
					res.apply(document);
					cu.getBuffer().setContents(document.get());

					// test result
					String preview= cu.getSource();

					buf= new StringBuffer();
					buf.append("package test1;\n");
					buf.append("public class E {\n");
					buf.append("    public void foo(int i) {\n");
					buf.append("        bar1();\n");
					buf.append("        while (--i > 0) {\n");
					buf.append("            System.beep();\n");
					buf.append("        }\n");
					buf.append("        bar2();\n");
					buf.append("    }\n");
					buf.append("}\n");	
					//assertEquals(preview, buf.toString());*/
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally {
				}
			}
		}

	}

	private void createPackage(IProject project) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		IFolder folder = project.getFolder("src");
		// folder.create(true, true, null);
		IPackageFragmentRoot srcFolder = javaProject
				.getPackageFragmentRoot(folder);
		IPackageFragment fragment = srcFolder.createPackageFragment(project.getName(), true, null);
	}

	private void createJavaElementsFrom(IProject myProject, IFolder myFolder, IFile myFile) 
	{
		IJavaProject myJavaProject = JavaCore.create(myProject);
		if (myJavaProject == null)
			return;
		IJavaElement myPackageFragment = JavaCore.create(myFolder);
		IJavaElement myJavaFile = JavaCore.create(myFile);
	}
}

