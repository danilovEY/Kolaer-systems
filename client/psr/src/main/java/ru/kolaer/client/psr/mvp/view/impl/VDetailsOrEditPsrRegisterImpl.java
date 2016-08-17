package ru.kolaer.client.psr.mvp.view.impl;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterBase;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStateBase;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.view.VDetailsOrEditPsrRegister;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private DatePicker datePickerState;
    private TextArea stateComment;
    private DatePicker datePickerPlan;
    private TextArea planComment;

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
                vs.registerValidator(namePsr, Validator.createEmptyValidator("Поле не может быть пустым!"));
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

        //============3============
        /*final FlowPane statePane = new FlowPane();
        final Button addState = new Button("Добавить состояние реализации...");
        addState.setOnAction(e -> {

            final HBox pane = new HBox()
        });*/
        final BorderPane mainStatePane = new BorderPane();
        this.datePickerState = new DatePicker();
        this.stateComment = new TextArea();

        mainStatePane.setTop(new HBox(new Label("Состояние реализации на дату: "), this.datePickerState));
        mainStatePane.setCenter(this.stateComment);

        final WizardPane psrStatePane = new WizardPane();
        psrStatePane.setContent(mainStatePane);

        //=============4==============
        final BorderPane mainPlanPane = new BorderPane();
        this.datePickerPlan = new DatePicker();
        this.planComment = new TextArea();

        mainPlanPane.setTop(new HBox(new Label("План реализации на дату: "), this.datePickerPlan));
        mainPlanPane.setCenter(this.planComment);

        final WizardPane psrPlanPane = new WizardPane();
        psrPlanPane.setContent(mainPlanPane);

        //=============5==================
        final WizardPane psrFinishPane = new WizardPane();
        psrFinishPane.setContent(new BorderPane(new Label("Завершена настройка ПСР проекта!")));

        if(this.psrRegister != null) {
            this.namePsr.setText(this.psrRegister.getName());
            this.htmlEditor.setHtmlText(this.psrRegister.getComment());

            this.datePickerState.setValue(Tools.convertToLocalDate(this.psrRegister.getStateList().get(0).getDate()));
            this.stateComment.setText(this.psrRegister.getStateList().get(0).getComment());

            this.datePickerPlan.setValue(Tools.convertToLocalDate(this.psrRegister.getStateList().get(1).getDate()));
            this.planComment.setText(this.psrRegister.getStateList().get(1).getComment());

            this.wizard = new Wizard(null, "Редактирование проекта");

        } else {
            final WizardPane welcomePane = new WizardPane();
            welcomePane.setContent(new BorderPane(new Label("Добро пожаловать в меню создания ПСР проекта!")));

            wizardPaneList.add(welcomePane);

            wizard = new Wizard(null, "Создание ПСР проекта");
        }

        wizardPaneList.add(psrNamedPane);
        wizardPaneList.add(psrDescripPane);
        wizardPaneList.add(psrStatePane);
        wizardPaneList.add(psrPlanPane);
        wizardPaneList.add(psrFinishPane);

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

                final List<PsrState> states = new ArrayList<>(this.psrRegister.getStateList());

                if(!this.stateComment.getText().isEmpty() && this.datePickerState.getValue() != null) {
                    PsrState psrState;
                    if(states.size() == 0) {
                        psrState = new PsrStateBase();
                        states.add(psrState);

                    } else {
                        psrState = states.get(0);
                    }

                    psrState.setComment(this.stateComment.getText());
                    psrState.setDate(Tools.convertToDate(this.datePickerState.getValue()));
                    psrState.setPlan(false);
                }

                if(!this.planComment.getText().isEmpty() && this.datePickerPlan.getValue() != null) {
                    PsrState psrStatePlan;
                    if(this.psrRegister.getStateList().size() < 2) {
                        psrStatePlan = new PsrStateBase();
                        states.add(psrStatePlan);

                    } else {
                        psrStatePlan = states.get(1);
                    }
                    psrStatePlan.setComment(this.planComment.getText());
                    psrStatePlan.setDate(Tools.convertToDate(this.datePickerPlan.getValue()));
                    psrStatePlan.setPlan(true);
                }

                this.psrRegister.setStateList(states);
                this.psrRegister.setAttachments(Collections.emptyList());
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
