package ru.kolaer.kolpass.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.core.system.UniformSystemEditorKit;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;
import ru.kolaer.kolpass.mvp.view.VRepositoryPasswordImpl;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPasswordImpl implements PRepositoryPassword {
    private static final Logger log = LoggerFactory.getLogger(PRepositoryPasswordImpl.class);
    private final UniformSystemEditorKit editorKit;
    private VRepositoryPassword vRepositoryPassword;
    private PasswordRepositoryDto passwordDto;
    private PPasswordHistory lastPassword;
    private PPasswordHistory firstPassword;
    private PPasswordHistory prevPassword;

    public PRepositoryPasswordImpl(UniformSystemEditorKit editorKit) {
        this.vRepositoryPassword = new VRepositoryPasswordImpl();
        this.editorKit = editorKit;
    }

    public PRepositoryPasswordImpl(UniformSystemEditorKit editorKit, PasswordRepositoryDto passwordDto) {
        this(editorKit);
        this.passwordDto = passwordDto;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.vRepositoryPassword.setName(this.passwordDto.getName());
        this.vRepositoryPassword.setOnReturnDefault(e -> {
            Optional.ofNullable(this.lastPassword).ifPresent(PPasswordHistory::updateView);
            Optional.ofNullable(this.prevPassword).ifPresent(PPasswordHistory::updateView);
            Optional.ofNullable(this.firstPassword).ifPresent(PPasswordHistory::updateView);
            return null;
        });

        this.vRepositoryPassword.setOnSaveData(o -> {
            if(this.lastPassword.getView().isChangeData(lastPassword.getModel())) {
                final VPasswordHistory view = this.lastPassword.getView();
                final PasswordHistoryDto passwordHistory = new PasswordHistoryDto();
                passwordHistory.setLogin(Optional.ofNullable(view.getLogin()).orElse(""));
                passwordHistory.setPassword(Optional.ofNullable(view.getPassword()).orElse(""));

                //this.setModel(this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase() // TODO!!
                //        .getKolpassTable().addHistoryPasswordToRepository(this.passwordDto.getId(), passwordHistory));

                this.editorKit.getUISystemUS().getNotification().showInformationNotify("Успешная операция!", "Сохранение прошло успешно!");
            }

            return null;
        });

        this.vRepositoryPassword.setOnEditName(name -> {
            if(name != null && !name.isEmpty() && !name.equals(this.passwordDto.getName())) {
                final String oldName = this.passwordDto.getName();
                this.passwordDto.setName(name);

                this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase()
                        .getKolpassTable().updateRepositoryPassword(this.passwordDto);

                this.vRepositoryPassword.setName(name);
                this.editorKit.getUISystemUS().getNotification().showInformationNotify("Успешная операция!",
                        "Изменение имени с \"" + oldName + "\" на \"" + name + "\"!");
            }
            return null;
        });

        final String url = this.passwordDto.getUrlImage() == null
                ? this.getClass().getResource("/icon-security1.png").toString()
                : this.passwordDto.getUrlImage();

        this.vRepositoryPassword.setImageUrl(url);

        this.vRepositoryPassword.setLastPassword(this.lastPassword.getView());

    }

    @Override
    public PasswordRepositoryDto getModel() {
        return this.passwordDto;
    }

    @Override
    public void setModel(PasswordRepositoryDto model) {
        this.passwordDto = model;
        this.updateView();
    }

    @Override
    public VRepositoryPassword getView() {
        return this.vRepositoryPassword;
    }

    @Override
    public void setView(VRepositoryPassword view) {
        this.vRepositoryPassword = view;
    }

    private PPasswordHistory setIfExist(PPasswordHistory pPass, PasswordHistoryDto dto, boolean edit) {
        PPasswordHistory pPasswordHistory = Optional.ofNullable(pPass).orElse(new PPasswordHistoryImpl());
        pPasswordHistory.setEditable(edit);
        pPasswordHistory.setModel(dto);
        return pPasswordHistory;
    }

    @Override
    public void setOnSaveData(Function function) {

    }

    @Override
    public void setOnEditName(Function function) {

    }

    @Override
    public void setOnDelete(Function<PRepositoryPassword, Void> function) {
        this.vRepositoryPassword.setOnDelete(e -> {
            this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase()
                    .getKolpassTable().deleteRepositoryPassword(this.passwordDto);

            this.editorKit.getUISystemUS().getNotification().showInformationNotify("Успешная операция!",
                    "Удален репозиторий: \"" + this.passwordDto.getName() + "\"");


            return function.apply(this);
        });
    }
}
