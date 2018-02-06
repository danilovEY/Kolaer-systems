package ru.kolaer.client.chat.view;

/**
 * Created by danilovey on 11.12.2017.
 */
//public class NotificationMessagePane implements StaticView, NotificationMessage {
//    private final TabChatVc tabChatVc;
//    private BorderPane mainPane;
//    private Label dateLabel;
//    private Label messageLabel;
//    private Label titleLabel;
//
//    public NotificationMessagePane(TabChatVc tabChatVc) {
//        this.tabChatVc = tabChatVc;
//    }
//
//    @Override
//    public void initView(Consumer<StaticView> viewVisit) {
//        mainPane = new BorderPane();
//        mainPane.setStyle("-fx-background-color: #b5f8ff");
//
//        titleLabel = new Label();
//        titleLabel.setAlignment(Pos.CENTER);
//        titleLabel.setTextAlignment(TextAlignment.CENTER);
//        titleLabel.setWrapText(true);
//        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
//        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
//
//        messageLabel = new Label();
//        messageLabel.setAlignment(Pos.CENTER);
//        messageLabel.setTextAlignment(TextAlignment.CENTER);
//        messageLabel.setWrapText(true);
//        messageLabel.setMinHeight(Region.USE_PREF_SIZE);
//        messageLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
//
//        dateLabel = new Label();
//        dateLabel.setAlignment(Pos.CENTER);
//        dateLabel.setTextAlignment(TextAlignment.CENTER);
//        dateLabel.setWrapText(true);
//        dateLabel.setFont(Font.font(null, FontWeight.BOLD, 9));
//
//        mainPane.setPadding(new Insets(10));
//        mainPane.setTop(titleLabel);
//        mainPane.setCenter(messageLabel);
//        mainPane.setBottom(dateLabel);
//
//        BorderPane.setAlignment(titleLabel, Pos.CENTER);
//        BorderPane.setAlignment(messageLabel, Pos.CENTER);
//        BorderPane.setAlignment(dateLabel, Pos.CENTER);
//    }
//
//    @Override
//    public void getMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto) {
//        Tools.runOnWithOutThreadFX(() -> {
//            if(!isViewInit()) {
//                this.initView(BaseView::empty);
//            }
//
//            titleLabel.setText("У вас новое сообщение!");
//            messageLabel.setText(chatRoomDto.getName());
//            dateLabel.setText(Tools.dateTimeToString(chatMessageDto.getCreateMessage()));
//
//            UniformSystemEditorKitSingleton.getInstance()
//                    .getUISystemUS()
//                    .getStatic()
//                    .addStaticView(this);
//
//            Stage mainStage = UniformSystemEditorKitSingleton.getInstance()
//                    .getUISystemUS()
//                    .getMainStage();
//
//            if(!mainStage.isShowing()) {
//                mainStage.show();
//                mainStage.setIconified(true);
//            }
//
//            mainStage.toFront();
//        });
//    }
//
//    @Override
//    public void clear() {
//        Tools.runOnWithOutThreadFX(() -> {
//            UniformSystemEditorKitSingleton.getInstance()
//                    .getUISystemUS()
//                    .getStatic()
//                    .removeStaticView(this);
//        });
//    }
//
//    @Override
//    public Node getContent() {
//        return mainPane;
//    }
//}
