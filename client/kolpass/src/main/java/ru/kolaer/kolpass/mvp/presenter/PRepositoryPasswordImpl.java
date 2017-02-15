package ru.kolaer.kolpass.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryBase;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;
import ru.kolaer.kolpass.mvp.view.VRepositoryPasswordImpl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPasswordImpl implements PRepositoryPassword {
    private static final Logger log = LoggerFactory.getLogger(PRepositoryPasswordImpl.class);
    private final UniformSystemEditorKit editorKit;
    private VRepositoryPassword vRepositoryPassword;
    private RepositoryPassword passwordDto;
    private PPasswordHistory lastPassword;
    private PPasswordHistory firstPassword;
    private PPasswordHistory prevPassword;

    public PRepositoryPasswordImpl(UniformSystemEditorKit editorKit) {
        this.vRepositoryPassword = new VRepositoryPasswordImpl();
        this.editorKit = editorKit;
    }

    public PRepositoryPasswordImpl(UniformSystemEditorKit editorKit, RepositoryPassword passwordDto) {
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
                final RepositoryPasswordHistoryBase passwordHistory = new RepositoryPasswordHistoryBase();
                passwordHistory.setLogin(Optional.ofNullable(view.getLogin()).orElse(""));
                passwordHistory.setPassword(Optional.ofNullable(view.getPassword()).orElse(""));

                this.setModel(this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase()
                        .getKolpassTable().addHistoryPasswordToRepository(this.passwordDto.getId(), passwordHistory));

                this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Успешная операция!", "Сохранение прошло успешно!");
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
                this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Успешная операция!",
                        "Изменение имени с \"" + oldName + "\" на \"" + name + "\"!");
            }
            return null;
        });

        final String url = this.passwordDto.getUrlImage() == null
                ? this.getClass().getResource("/icon-security1.png").toString()
                : this.passwordDto.getUrlImage();

        this.vRepositoryPassword.setImageUrl(url);

        this.lastPassword = this.setIfExist(this.lastPassword, this.passwordDto.getLastPassword(), true);
        this.vRepositoryPassword.setLastPassword(this.lastPassword.getView());

        Optional.ofNullable(this.passwordDto.getPrevPassword())
                .filter(Objects::nonNull)
                .map(dto -> this.prevPassword = this.setIfExist(this.prevPassword, dto, false))
                .ifPresent(pass -> this.vRepositoryPassword.setPrevPassword(pass.getView()));

        Optional.ofNullable(this.passwordDto.getFirstPassword())
                .filter(Objects::nonNull)
                .map(dto -> this.firstPassword = this.setIfExist(this.firstPassword, dto, false))
                .ifPresent(pass -> this.vRepositoryPassword.setFirstPassword(pass.getView()));

    }

    @Override
    public RepositoryPassword getModel() {
        return this.passwordDto;
    }

    @Override
    public void setModel(RepositoryPassword model) {
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

    private PPasswordHistory setIfExist(PPasswordHistory pPass, RepositoryPasswordHistory dto, boolean edit) {
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

            this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Успешная операция!",
                    "Удален репозиторий: \"" + this.passwordDto.getName() + "\"");


            return function.apply(this);
        });
    }
}
