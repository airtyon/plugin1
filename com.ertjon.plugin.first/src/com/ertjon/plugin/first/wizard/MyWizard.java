package com.ertjon.plugin.first.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.ertjon.plugin.first.tests.NewModuleWizardPage;

public class MyWizard extends Wizard
{
	protected MyPageOne one;
	protected MyPageTwo two;
	protected NewModuleWizardPage tt;
	
	public MyWizard() 
	{
		super();
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public String getWindowTitle()
	{
		return "Export My Data";
	}
	
	@Override
	public void addPages()
	{
		one=new MyPageOne();
		two=new MyPageTwo();
		tt=new NewModuleWizardPage();
		addPage(one);
		addPage(two);
		addPage(tt);
	}

	@Override
	public boolean performFinish() 
	{
		System.out.println(one.getText1());
	    System.out.println(two.getText1());
		return true;
	}

}
