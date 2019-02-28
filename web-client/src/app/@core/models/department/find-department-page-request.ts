import {PageRequestModel} from '../page-request.model';
import {DepartmentSortTypeEnum} from "./department-sort-type.enum";
import {DirectionTypeEnum} from "../direction-type.enum";

export class FindDepartmentPageRequest extends PageRequestModel {
    public query: string;

    public deleted: boolean;

    public direction: DirectionTypeEnum;
    public sort: DepartmentSortTypeEnum;

    public static findAllRequest(): FindDepartmentPageRequest {
        return FindDepartmentPageRequest.findRequest(1, 1000);
    }

    public static searchDepartmentRequest(searchText: string): FindDepartmentPageRequest {
        const request = FindDepartmentPageRequest.findRequest(1, 1000);
        request.query = searchText;

        return request;
    }

    public static findRequest(pageNum: number, pageSize: number): FindDepartmentPageRequest {
        const findDepartmentPageRequest = new FindDepartmentPageRequest();
        findDepartmentPageRequest.pageNum = pageNum;
        findDepartmentPageRequest.pageSize = pageSize;
        findDepartmentPageRequest.deleted = false;
        findDepartmentPageRequest.sort = DepartmentSortTypeEnum.NAME;
        findDepartmentPageRequest.direction = DirectionTypeEnum.ASC;

        return findDepartmentPageRequest;
    }
}
