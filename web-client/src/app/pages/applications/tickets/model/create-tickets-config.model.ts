import {TypeOperationEnum} from '../main/type-operation.enum';

export class CreateTicketsConfigModel {
    constructor(public typeOperationForAll: TypeOperationEnum, public countForAll: number) {}
}
