package ru.kolaer.client.psr.mvp.view.impl;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterBase;
import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 04.08.2016.
 */
public class VDetailsOrEditPsrRegisterImpl implements VDetailsOrEditPsrRegister {
    private PsrRegister psrRegister;
    private Wizard wizard;
    private boolean isInit = false;
    private TextField namePsr;
    private HTMLEditor htmlEditor;

    public VDetailsOrEditPsrRegisterImpl(final PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
    }

    public VDetailsOrEditPsrRegisterImpl() {

    }

    @Override
    public void initializationView() {
        List<WizardPane> wizardPaneList = new ArrayList<>();

        //============1===========
        final Label namePsrLabel = new Label("Название ПСР проекта: ");

        final WizardPane psrNamedPane = new WizardPane() {

            ValidationSupport vs = new ValidationSupport();
            {
                vs.initInitialDecoration();

                namePsr = TextFields.createClearableTextField();
                vs.registerValidator(namePsr, Validator.createEmptyValidator("EMPTY!"));
                setContent(new VBox(namePsrLabel, namePsr));
            }

            @Override
            public void onEnteringPage(Wizard wizard) {
                wizard.invalidProperty().unbind();
                wizard.invalidProperty().bind(vs.invalidProperty());
            }
        };
        //============2============
        this.htmlEditor = new HTMLEditor();
        final Label desLabel = new Label("Введите описание и цель проекта:");
        final VBox descPanel = new VBox(desLabel, this.htmlEditor);
        descPanel.setAlignment(Pos.CENTER);

        final WizardPane psrDescripPane = new WizardPane();
        psrDescripPane.setContent(descPanel);

        if(this.psrRegister != null) {
            namePsr.setText(this.psrRegister.getName());
            wizard = new Wizard(null, "Редактирование проекта");
        } else {
            final WizardPane welcomePane = new WizardPane();
            welcomePane.setContent(new BorderPane(new Label("Добро пожаловать в меню создания ПСР проекта!")));

            wizardPaneList.add(welcomePane);

            wizard = new Wizard(null, "Создание ПСР проекта");
        }

        wizardPaneList.add(psrNamedPane);
        wizardPaneList.add(psrDescripPane);

        wizard.setFlow(new Wizard.LinearFlow(wizardPaneList));

        this.isInit = true;
    }

    @Override
    public boolean isInitializationView() {
        return this.isInit;
    }


    @Override
    public void showAndWait() {
        this.wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                if(this.psrRegister == null)
                    this.psrRegister = new PsrRegisterBase();

                this.psrRegister.setName(this.namePsr.getText());
                this.psrRegister.setComment(this.htmlEditor.getHtmlText());
            }
        });
    }

    @Override
    public PsrRegister getPsrRegister() {
        return this.psrRegister;
    }

    @Override
    public void setPsrRegister(PsrRegister psrRegister) {
        this.psrRegister = psrRegister;
    }
}
