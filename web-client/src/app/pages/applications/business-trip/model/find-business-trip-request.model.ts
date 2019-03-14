import {PageRequestModel} from "../../../../@core/models/page-request.model";

export class FindBusinessTripRequestModel extends PageRequestModel {
    public businessTripIds: number[] = [];
}
