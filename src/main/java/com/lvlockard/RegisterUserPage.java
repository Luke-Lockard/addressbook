package com.lvlockard;

import com.lvlockard.forms.RegisterUserForm;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class RegisterUserPage extends WebPage {

    public RegisterUserPage(final PageParameters parameters) {
        super(parameters);

        add(new RegisterUserForm("registerForm"));
    }
}
