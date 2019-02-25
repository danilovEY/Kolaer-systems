export enum ContactTypeModel {
    MAIN,
    OTHER
}

function getName(contactType: ContactTypeModel): string {
    switch (contactType) {
        case ContactTypeModel.MAIN : return 'Основной';
        case ContactTypeModel.OTHER : return 'Дополнительный';
        default: return 'Неизвестно';
    }
}
