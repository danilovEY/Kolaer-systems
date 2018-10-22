package ru.kolaer.kolpass.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;
import ru.kolaer.kolpass.mvp.view.VPasswordHistoryImpl;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Random;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PPasswordHistoryImpl implements PPasswordHistory {
    private static final Logger log = LoggerFactory.getLogger(PPasswordHistoryImpl.class);
    private static final char[] choices = ("abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "!$#_").toCharArray();
    private static final int MAX_SIZE_PASS_GENERATE = 8;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static final Random random = new Random();

    private VPasswordHistory vPasswordHistory;
    private PasswordHistoryDto passwordHistoryDto;


    public PPasswordHistoryImpl() {
        this.vPasswordHistory = new VPasswordHistoryImpl();
    }

    public PPasswordHistoryImpl(PasswordHistoryDto passwordHistoryDto) {
        this();
        this.setModel(passwordHistoryDto);
    }

    @Override
    public void updateView() {
        this.vPasswordHistory.setDate(null);
        this.vPasswordHistory.setPassword(null);
        this.vPasswordHistory.setLogin(null);
        this.vPasswordHistory.setOnGeneratePass(e -> {
            this.vPasswordHistory.setPassword(this.getSalt(MAX_SIZE_PASS_GENERATE));
            return null;
        });
        Optional.ofNullable(this.passwordHistoryDto).ifPresent(pass -> {
            Optional.ofNullable(pass.getPasswordWriteDate()).ifPresent(date ->
                    this.vPasswordHistory.setDate(sdf.format(date))
            );
            this.vPasswordHistory.setPassword(pass.getPassword());
            this.vPasswordHistory.setLogin(pass.getLogin());
        });
    }

    private String getSalt(int len) {
        StringBuilder salt = new StringBuilder(len);
        for (int i = 0; i<len; ++i)
            salt.append(choices[random.nextInt(choices.length)]);
        return salt.toString();
    }

    @Override
    public PasswordHistoryDto getModel() {
        return this.passwordHistoryDto;
    }

    @Override
    public void setModel(PasswordHistoryDto model) {
        this.passwordHistoryDto = model;
        this.updateView();
    }

    @Override
    public VPasswordHistory getView() {
        return this.vPasswordHistory;
    }

    @Override
    public void setView(VPasswordHistory view) {
        this.vPasswordHistory = view;
        this.updateView();
    }

    @Override
    public void setEditable(boolean edit) {
        this.vPasswordHistory.setEditable(edit);
    }

    @Override
    public boolean isEditable() {
        return this.vPasswordHistory.isEditable();
    }
}
