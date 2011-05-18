package be.mira.adastra3.server.website.status;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBreak;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WText;

/*
 * A simple hello world application class which demonstrates how to react
 * to events, read input, and give feed-back.
 */
public class StatusApplication extends WApplication {
    public StatusApplication(WEnvironment env) {
        super(env);
        
        setTitle("Hello world");

        getRoot().addWidget(new WText("Your name, please ? "));
        final WLineEdit nameEdit = new WLineEdit(getRoot());
        nameEdit.setFocus();

        WPushButton button = new WPushButton("Greet me.", getRoot());
        button.setMargin(5, Side.Left);

        getRoot().addWidget(new WBreak());

        final WText greeting = new WText(getRoot());

        button.clicked().addListener(this, new Signal.Listener() {
            @Override
            public void trigger() {
                greeting.setText("Hello there, " + nameEdit.getText());
            }
        });
    }
}
