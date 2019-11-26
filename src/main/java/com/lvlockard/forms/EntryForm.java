package com.lvlockard.forms;

import com.lvlockard.utils.Entry;
import com.lvlockard.utils.Redis;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

public class EntryForm extends Form {

    private String userEmail;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField addressField;
    private TextField phoneNumberField;


    public EntryForm(String id, String userEmail) {
        super(id);

        this.userEmail = userEmail;

        firstNameField = new TextField("firstName", Model.of(""));
        firstNameField.setRequired(true);

        lastNameField = new TextField("lastName", Model.of(""));
        lastNameField.setRequired(true);

        emailField = new TextField("email", Model.of(""));
        emailField.setRequired(true);

        addressField = new TextField("address", Model.of(""));
        addressField.setRequired(true);

        phoneNumberField = new TextField("phoneNumber", Model.of(""));
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

        Redis.enterAddress(this.userEmail, entry);

        firstNameField.clearInput();
        lastNameField.clearInput();
        emailField.clearInput();
        addressField.clearInput();
        phoneNumberField.clearInput();

        // TODO: refresh the contact list

    }
}
