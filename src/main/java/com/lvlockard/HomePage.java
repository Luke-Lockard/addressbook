package com.lvlockard;


import com.lvlockard.components.AddContact.AddContactPanel;
import com.lvlockard.components.ContactList.ContactListPanel;
import com.lvlockard.components.Header.HeaderPanel;
import com.lvlockard.components.UpdateContact.UpdateContactPanel;
import com.lvlockard.utils.Entry;
import com.lvlockard.utils.Redis;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private Component headerPanel;
	private Component addContactPanel;
	private Component contactListPanel;
	private Component updateContactPanel;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		// user's email is passed from login page or register page
		// used to identify the user and get the contacts specific to the user
		String userEmail = parameters.get("userEmail").toString();
		System.out.println("User " + userEmail);

		// Initialize entries to empty array. Gets reassigned if the user has entries
		Entry[] entries = new Entry[0];

		// Get the user's address book
		String[] contacts = Redis.getUserContacts(userEmail);
		if (contacts.length > 0) {
			System.out.println("First contact: " + contacts[0]);
			 entries = Redis.getUserEntries(userEmail, contacts);

			System.out.println("Number of entries: " + entries.length);
			if (entries.length > 0) {
				Entry entry1 = entries[0];
				entry1.displayEntry();
			}
		}


		add(headerPanel = new HeaderPanel("headerPanel"));
		add(addContactPanel = new AddContactPanel("addContactPanel", userEmail));
		add(contactListPanel = new ContactListPanel("contactListPanel", entries, userEmail));


		// for test
//		if (entries.length > 0)
//			add(updateContactPanel = new UpdateContactPanel("updateContactPanel", userEmail, entries[0]));


	}


	// Doesn't work. Need this to be static so I can call it from the form? But it breaks
	public void updateContactList(Entry[] entries) {
//		remove(contactListPanel);
//		add(contactListPanel = new ContactListPanel("contactListPanel", entries));
	}
}
