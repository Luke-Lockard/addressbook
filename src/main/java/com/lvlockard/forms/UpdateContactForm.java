package com.lvlockard.forms;

import com.lvlockard.utils.Entry;
import com.lvlockard.utils.Redis;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

public class UpdateContactForm extends Form {

    private String userEmail, contactEmail;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField addressField;
    private TextField phoneNumberField;


    public UpdateContactForm(String id, String userEmail, Entry entry) {
        super(id);

        this.userEmail = userEmail;
        this.contactEmail = entry.getEmail();

        firstNameField = new TextField("firstName", Model.of(entry.getFirstName()));
        firstNameField.setRequired(true);

        lastNameField = new TextField("lastName", Model.of(entry.getLastName()));
        lastNameField.setRequired(true);

        emailField = new TextField("email", Model.of(entry.getEmail()));
        emailField.setRequired(true);

        addressField = new TextField("address", Model.of(entry.getAddress()));
        addressField.setRequired(true);

        phoneNumberField = new TextField("phoneNumber", Model.of(entry.getPhoneNumber()));
        phoneNumberField.setRequired(true);

        add(firstNameField);
        add(lastNameField);
        add(emailField);
        add(addressField);
        add(phoneNumberField);
    }

    public final void onSubmit() {
        String firstName = (String) firstNameField.getDefaultModelObject();
        String lastName = (String) lastNameField.getDefaultModelObject();
        String email = (String) emailField.getDefaultModelObject();
        String address = (String) addressField.getDefaultModelObject();
        String phoneNumber = (String) phoneNumberField.getDefaultModelObject();

        Entry entry = new Entry(firstName, lastName, email, address, phoneNumber);

        Redis.updateEntry(this.userEmail,  contactEmail, entry);

//        firstNameField.clearInput();
//        lastNameField.clearInput();
//        emailField.clearInput();
//        addressField.clearInput();
//        phoneNumberField.clearInput();

        // TODO: reset home page

    }
}
