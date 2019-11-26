package com.lvlockard.forms;

import com.lvlockard.HomePage;
import com.lvlockard.LoginPage;
import com.lvlockard.utils.Redis;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;

public class RegisterUserForm extends Form {

    private TextField emailField;
    private PasswordTextField passwordField;
    private PasswordTextField confirmPasswordField;
    private Label registeredStatus;

    public RegisterUserForm(String id) {
        super(id);

        emailField = new TextField("email", Model.of(""));
        emailField.setRequired(true);
        emailField.add(EmailAddressValidator.getInstance());

        passwordField = new PasswordTextField("password", Model.of(""));
        passwordField.setRequired(true);
        confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
        confirmPasswordField.setRequired(true);
        add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

        registeredStatus = new Label("registeredStatus", Model.of(""));

        add(emailField);
        add(passwordField);
        add(confirmPasswordField);
        add(registeredStatus);

        add(new Link<Void>("loginLink") {
            @Override
            public void onClick() {
                setResponsePage(LoginPage.class);
            }
        });
    }


    /*protected void onValidate() {
        super.onValidate();

        if (hasError()) {
            System.out.println("Validation error");
            return;
        }

        String email = (String)emailField.getDefaultModelObject();
        String password = (String)passwordField.getDefaultModelObject();
    }*/

    // TODO: make the page not refresh when the registration fails (any of the else statements is triggered)
    public final void onSubmit() {
        String email = (String)emailField.getDefaultModelObject();
        String password = (String)passwordField.getDefaultModelObject();
        String confirmPassword = (String)confirmPasswordField.getDefaultModelObject();

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        // need further validation for email type?

        // if fields are not empty and passwords match
        // not sure if email.setRequired(true) stops this, so these conditions are unnecessary?
        if (!email.equals("") && !password.equals("") && !confirmPassword.equals("")) {
            // check passwords match
            if (confirmPassword.equals(password)) {
                if (Redis.createUser(email, password)) {
                    registeredStatus.setDefaultModelObject("Registered");
                    PageParameters parameters = new PageParameters();
                    parameters.add("userEmail", email);
                    setResponsePage(new HomePage(parameters));
                } else {
                    registeredStatus.setDefaultModelObject("That email is already in use.");
                }
            } else {
                registeredStatus.setDefaultModelObject("Passwords do not match");
            }
        } else {
            registeredStatus.setDefaultModelObject("Field cannot be empty");
        }
    }
}
