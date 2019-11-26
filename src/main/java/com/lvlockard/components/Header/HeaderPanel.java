package com.lvlockard.components.Header;

import com.lvlockard.LoginPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);

        add(new Link<Void>("logout") {
            @Override
            public void onClick() {
                setResponsePage(LoginPage.class);
            }
        });
    }
}
