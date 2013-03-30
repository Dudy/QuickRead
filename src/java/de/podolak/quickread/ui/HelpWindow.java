package de.podolak.quickread.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class HelpWindow extends Window {

    private static final String HELP_HTML_SNIPPET =
            "Die Anwendung befindet sich noch in sehr frühem Stadium, deswegen gibt es noch keine Hilfe.<br>" +
            "Autor: <a href=\"mailto:dirk.podolak@gmail.com\">Dirk Podolak</a><br><br><br>" +
            "<br>" +
            "Was geht bereits:<br>" +
            "- Ansehen eines Dokuments<br>" +
            "- Navigieren im Baum links<br>" +
            "- Editieren eines Knotens rechts<br>" +
            "- Abbruch der Editieraktion<br>" +
            "- Hinzufügen eines Knotens<br>" +
            "- Entfernen eines Knotens (mit Rückfrage)<br>" +
            "- Speichern<br>" +
            "- Laden<br>" +
            "- Anmelden als Benutzer<br>" +
            "- einfache Autorisierung (z.B. Editieren nur als angemeldeter Benutzer, noch keine benutzerspezifischen Rechte)<br>" +
            "- Hilfefenster ;-)<br>" +
            "<br>" +
            "Was kommt als nächstes:<br>" +
            "- Suche<br>" +
            "- Projektverwaltung<br>" +
            "- Tastatursteuerung<br>" +
            "<br>" +
            "<br>" +
            "<br>" +
            "<br>" +
            "<br>" +
            "<br>" +
            "<br>";

    public HelpWindow() {
        setCaption("Quickread Hilfe");
        addComponent(new Label(HELP_HTML_SNIPPET, Label.CONTENT_XHTML));
    }
}
