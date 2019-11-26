package com.lvlockard.forms;

import com.lvlockard.HomePage;
import com.lvlockard.RegisterUserPage;
import com.lvlockard.utils.Redis;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;

public class LoginForm extends Form {

    private TextField emailField;
    private PasswordTextField passwordField;
    private Label loginStatus;

    public LoginForm(String id) {
        super(id);

        emailField = new TextField("email", Model.of(""));
        emailField.setRequired(true);
        emailField.add(EmailAddressValidator.getInstance());

        passwordField = new PasswordTextField("password", Model.of(""));
        passwordField.setRequired(true);

        loginStatus = new Label("loginStatus", Model.of(""));

        add(emailField);
        add(passwordField);
        add(loginStatus);

        add(new Link<Void>("createAccountLink") {
            @Override
            public void onClick() {
                setResponsePage(RegisterUserPage.class);
            }
        });
    }

    public final void onSubmit() {
        String email = (String)emailField.getDefaultModelObject();
        String password = (String)passwordField.getDefaultModelObject();

        System.out.println("email: " + email);
        System.out.println("password: " + password);

        if (Redis.loginUser(email, password)) {
            PageParameters parameters = new PageParameters();
            parameters.add("userEmail", email);
            setResponsePage(new HomePage(parameters));
        } else {
            loginStatus.setDefaultModelObject("Wrong email or password");
        }


    }

}
