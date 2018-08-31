import {Injectable} from '@angular/core';
import {CustomActionModel} from '../../@theme/components/table/custom-action.model';

@Injectable()
export class SmartTableService {
    public static readonly DEFAULT_TABLE_NAME = 'default';
    public static readonly DELETE_ACTION_NAME: string = 'delete';
    public static readonly EDIT_ACTION_NAME: string = 'edit';

    private readonly actionsMap: Map<String, CustomActionModel[]> = new Map<String, CustomActionModel[]>();
    private readonly editAction: CustomActionModel = new CustomActionModel();
    private readonly deleteAction: CustomActionModel = new CustomActionModel();

    constructor() {
        this.editAction.name = SmartTableService.EDIT_ACTION_NAME;
        this.editAction.content = '<i class="fa fa-edit"></i>';
        this.editAction.description = 'Редактировать';

        this.deleteAction.name = SmartTableService.DELETE_ACTION_NAME;
        this.deleteAction.content = '<i class="fa fa-trash"></i>';
        this.deleteAction.description = 'Удалить';
    }

    public addEditAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME) {
        this.addAction(tableName, this.editAction);
    }

    public addDeleteAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME) {
        this.addAction(tableName, this.deleteAction);
    }

    public removeEditAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME) {
        this.removeAction(tableName, this.editAction);
    }

    public removeDeleteAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME) {
        this.removeAction(tableName, this.deleteAction);
    }

    public addActions(tableName: string, actions: CustomActionModel[] = []) {
        for (const action of actions) {
            this.addAction(tableName, action);
        }
    }

    public removeAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME, action: CustomActionModel) {
        let actions: CustomActionModel[] = this.actionsMap.get(tableName);

        if (!actions) {
            actions = [];
        }

        if (actions.includes(action)) {
            actions.splice(actions.indexOf(action));
        }

        this.actionsMap.set(tableName, actions);
    }

    public addAction(tableName: string = SmartTableService.DEFAULT_TABLE_NAME, action: CustomActionModel) {
        let actions: CustomActionModel[] = this.actionsMap.get(tableName);

        if (!actions) {
            actions = [];
        }

        if (!actions.includes(action)) {
            actions.push(action);
        }

        this.actionsMap.set(tableName, actions);
    }

    public getAllActions(): Map<String, CustomActionModel[]> {
        return this.actionsMap;
    }

    public getActions(tableName: string = SmartTableService.DEFAULT_TABLE_NAME): CustomActionModel[] {
        const actions: CustomActionModel[] = this.actionsMap.get(tableName);

        return actions ? actions : [];
    }

    public clearActions(tableName: string = SmartTableService.DEFAULT_TABLE_NAME): void {
        this.actionsMap.delete(tableName);
    }

}
