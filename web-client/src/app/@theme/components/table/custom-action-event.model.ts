import {CustomActionModel} from './custom-action.model';

export class CustomActionEventModel<T> {
    constructor(public action: CustomActionModel,
                public data: T) {}
}
