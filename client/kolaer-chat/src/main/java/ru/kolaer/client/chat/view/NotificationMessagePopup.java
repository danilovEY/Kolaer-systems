package ru.kolaer.client.chat.view;

/**
 * Created by danilovey on 11.12.2017.
 */
//public class NotificationMessagePopup implements NotificationMessage {
//    private final TabChatVc tabChatVc;
//    private final UniformSystemPlugin uniformSystemPlugin;
//
//    public NotificationMessagePopup(TabChatVc tabChatVc,
//                                    UniformSystemPlugin uniformSystemPlugin) {
//        this.tabChatVc = tabChatVc;
//        this.uniformSystemPlugin = uniformSystemPlugin;
//    }
//
//    @Override
//    public void getMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto) {
//        TabChatRoomVc chatRoom = tabChatVc.getChatRoom(chatRoomDto);
//
//        Stage mainStage = UniformSystemEditorKitSingleton.getInstance()
//                .getUISystemUS()
//                .getMainStage();
//
//        if(mainStage.isIconified() || !tabChatVc.roomIsFocus(chatRoom)) {
//            Tools.runOnWithOutThreadFX(() -> {
//                NotifyAction notifyAction = new NotifyAction("Перейти в " + chatRoomDto.getName(), actionEvent -> {
//                    Tools.runOnWithOutThreadFX(() -> {
//                        if(!mainStage.isShowing()) {
//                            mainStage.show();
//                        }
//
//                        if(mainStage.isIconified()) {
//                            mainStage.setIconified(false);
//                        }
//
//                        mainStage.toFront();
//                        mainStage.requestFocus();
//
//                        UniformSystemEditorKitSingleton.getInstance()
//                                .getPluginsUS()
//                                .showPlugin(uniformSystemPlugin);
//
//                        tabChatVc.showChatRoom(chatRoom, true);
//                    });
//                });
//
//                UniformSystemEditorKitSingleton.getInstance()
//                        .getUISystemUS()
//                        .getPopupNotification()
//                        .showSimpleNotify("У вас новое сообщение!",
//                                null,
//                                Duration.seconds(10),
//                                Collections.singletonList(notifyAction));
//
//                if(!mainStage.isShowing()) {
//                    mainStage.show();
//                    mainStage.setIconified(true);
//                }
//
//                mainStage.toFront();
//            });
//        }
//    }
//
//    @Override
//    public void clear() {
//
//    }
//}
