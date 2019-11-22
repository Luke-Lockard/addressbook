package com.lvlockard;

import com.lvlockard.components.ContactsTable.ContactsTablePanel;
import com.lvlockard.components.EntryForm.EntryFormPanel;
import com.lvlockard.components.Footer.FooterPanel;
import com.lvlockard.components.Header.HeaderPanel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private Component headerPanel;
	private Component entryFormPanel;
	private Component contactsTablePanel;
	private Component footerPanel;

	public HomePage(final PageParameters parameters) {
		super(parameters);

//		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here
		add(headerPanel = new HeaderPanel("headerPanel"));
		add(entryFormPanel = new EntryFormPanel("entryFormPanel"));
		add(contactsTablePanel = new ContactsTablePanel("contactsTablePanel"));
		add(footerPanel = new FooterPanel("footerPanel"));
	}
}
