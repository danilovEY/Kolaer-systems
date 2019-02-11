import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";

@Injectable()
export class EmployeeCardService {
    readonly selectedEmployeeId: BehaviorSubject<number> = new BehaviorSubject<number>(1);

    public setSelectedEmployeeId(employeeId: number): void {
        this.selectedEmployeeId.next(employeeId);
    }

}
