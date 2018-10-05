import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DepartmentModel} from '../../../../../@core/models/department.model';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {ToasterConfig, ToasterService} from 'angular2-toaster';
import {NgbCalendar, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {ReportFilterModel} from '../../model/report-filter.model';
import {SelectItem} from 'primeng/api';
import {EmployeeModel} from '../../../../../@core/models/employee.model';
import {PostModel} from '../../../../../@core/models/post.model';
import {TypeWorkModel} from '../../../../../@core/models/type-work.model';
import {DepartmentService} from '../../../../../@core/services/department.service';
import {EmployeeService} from '../../../../../@core/services/employee.service';
import {PostService} from '../../../../../@core/services/post.service';
import {TypeWorkService} from '../../../../../@core/services/type-work.service';
import {FindDepartmentPageRequest} from '../../../../../@core/models/department/find-department-page-request';
import {FindEmployeeRequestModel} from '../../../../../@core/models/find-employee-request.model';
import {FindPostRequestModel} from '../../../../../@core/models/post/find-post-request.model';
import {FindTypeWorkRequest} from '../../../../../@core/models/find-type-work-request';

@Component({
    selector: 'report-filter',
    templateUrl: './report-filter.component.html',
    styleUrls: ['./report-filter.component.scss']
})
export class ReportFilterComponent implements OnInit {
    public readonly TYPE_SELECTED_PERIOD_CUSTOM = 'custom';
    public readonly TYPE_SELECTED_PERIOD_YEAR = 'year';

    @Input()
    @Output()
    filterModel: ReportFilterModel = new ReportFilterModel();

    @Output()
    onChangeFilter = new EventEmitter<ReportFilterModel>();

    @Output()
    onAcceptFilter = new EventEmitter<ReportFilterModel>();

    @Input()
    maxMonth: number = 0;

    @Input()
    actionTitle: string = 'Поиск';

    @Input()
    pipeCharts: boolean = false;

    @Input()
    calculateIntersections: boolean = false;

    @Input()
    selectAllDepartments: boolean = false;

    @Input()
    groupByDepartments: boolean = false;

    @Input()
    multiSelectDepartment: boolean = true;

    @Input()
    requireDepartments: boolean = false;

    @Input()
    selectDepartments: boolean = false;

    @Input()
    selectEmployees: boolean = true;

    @Input()
    selectPosts: boolean = true;

    @Input()
    selectTypeWork: boolean = true;

    departmentsResult: DepartmentModel[] = [];
    employeesResult: EmployeeModel[] = [];
    postsResult: PostModel[] = [];
    typeWorksResult: TypeWorkModel[] = [];


    hoveredDate: NgbDate;

    fromDate: NgbDate;
    toDate: NgbDate;

    periodRadioModel = 'year';

    years: SelectItem[] = [];
    selectedYear: number;


    config: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-top-right',
        timeout: 5000,
        newestOnTop: true,
        tapToDismiss: true,
        preventDuplicates: false,
        animation: 'fade',
        limit: 5,
    });

    constructor(private toasterService: ToasterService,
                private departmentService: DepartmentService,
                private employeeService: EmployeeService,
                private postService: PostService,
                private typeWorkService: TypeWorkService,
                private calendar: NgbCalendar) {
        this.fromDate = calendar.getToday();
        this.toDate = calendar.getNext(calendar.getToday(), 'd', 3);

        const currentYear = new Date().getFullYear();

        this.years.push(
            { label: String(currentYear + 1), value: String(currentYear + 1)},
            { label: String(currentYear), value: String(currentYear)},
            { label: String(currentYear - 1), value: String(currentYear - 1)}
        );
    }

    ngOnInit() {
        this.selectYear(this.years[1]);
    }

    searchDepartment(event) {
        this.departmentService.find(new FindDepartmentPageRequest(event.query))
            .subscribe(departmentPage => this.departmentsResult = departmentPage.data);
    }

    searchEmployee(event) {
        this.employeeService.findAllEmployees(new FindEmployeeRequestModel(event.query, this.getIdsDepartments()))
            .subscribe(employeePage => this.employeesResult = employeePage.data);
    }

    searchPost(event) {
        this.postService.find(new FindPostRequestModel(event.query, this.getIdsDepartments()))
            .subscribe(postPage => this.postsResult = postPage.data);
    }

    searchTypeWork(event) {
        this.typeWorkService.getAll(new FindTypeWorkRequest(event.query, this.getIdsDepartments()))
            .subscribe(typeWorkPage => this.typeWorksResult = typeWorkPage.data);
    }

    private getIdsDepartments(): number[] {
        return this.filterModel.selectedDepartments.map(d => d.id);
    }

    setPeriodRadioModel(value: any) {
        this.periodRadioModel = value.srcElement.value;

        if (this.periodRadioModel === this.TYPE_SELECTED_PERIOD_CUSTOM) {

        } else if (this.periodRadioModel === this.TYPE_SELECTED_PERIOD_YEAR) {

        }
    }

    selectYear(selected: any) {
        this.selectedYear = selected.value;

        this.fromDate = NgbDate.from({year: this.selectedYear, month: 1, day: 1});
        this.toDate = NgbDate.from({year: this.selectedYear, month: 12, day: 31});

        this.filterModel.from = this.convertToDate(this.fromDate);
        this.filterModel.to = this.convertToDate(this.toDate);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    // onSelectDepartments(multiSelect: any) {
    //     this.filterModel.selectedDepartments = multiSelect.value;
    //     if (this.onChangeFilter) {
    //         this.onChangeFilter.emit(this.filterModel);
    //     }
    // }
    //
    // onSelectDepartment(selected: any) {
    //     this.filterModel.selectedDepartments = [];
    //     this.filterModel.selectedDepartment = selected.value;
    //     if (this.onChangeFilter) {
    //         this.onChangeFilter.emit(this.filterModel);
    //     }
    // }

    onSelectTypeWork(selected: TypeWorkModel) {
        this.filterModel.selectedTypeWorks.push(selected);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onUnSelectTypeWork(selected: TypeWorkModel) {
        const index = this.filterModel.selectedTypeWorks.indexOf(selected);
        if (index !== -1) {
            this.filterModel.selectedTypeWorks.splice(index, 1);
        }

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onSelectPost(selected: PostModel) {
        this.filterModel.selectedPosts.push(selected);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onUnSelectPost(selected: PostModel) {
        const index = this.filterModel.selectedPosts.indexOf(selected);
        if (index !== -1) {
            this.filterModel.selectedPosts.splice(index, 1);
        }

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onSelectEmployee(selected: EmployeeModel) {
        this.filterModel.selectedEmployees.push(selected);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onUnSelectEmployee(selected: EmployeeModel) {
        const index = this.filterModel.selectedEmployees.indexOf(selected);
        if (index !== -1) {
            this.filterModel.selectedEmployees.splice(index, 1);
        }

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onSelectDepartment(selected: DepartmentModel) {
        this.filterModel.selectedDepartments.push(selected);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onUnSelectDepartment(selected: DepartmentModel) {
        const index = this.filterModel.selectedDepartments.indexOf(selected);
        if (index !== -1) {
            this.filterModel.selectedDepartments.splice(index, 1);
        }

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onDateSelection(date: NgbDate) {
        if (!this.fromDate && !this.toDate) {
            this.fromDate = date;
        } else if (this.fromDate && !this.toDate &&
            this.isAfterNgbDate(date, this.fromDate) &&
            this.isRangeNgbDate(this.fromDate, date, this.maxMonth)) {
            this.toDate = date;
        } else {
            this.toDate = null;
            this.fromDate = date;
        }

        this.filterModel.from = this.convertToDate(this.fromDate);
        this.filterModel.to = this.convertToDate(this.toDate);

        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onSelectedAllDepartment(selected: Event) {
        this.filterModel.selectedAllDepartments = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onGroupByDepartments(selected: Event) {
        this.filterModel.groupByDepartments = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onPipeCharts(selected: Event) {
        this.filterModel.pipeCharts = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    onCalculateIntersections(selected: Event) {
        this.filterModel.calculateIntersections = selected.returnValue;
        if (this.onChangeFilter) {
            this.onChangeFilter.emit(this.filterModel);
        }
    }

    generateReport() {
        if (this.onAcceptFilter) {
            this.onAcceptFilter.emit(this.filterModel);
        }
    }

    private convertToDate(ngbDate: NgbDate): Date {
        return ngbDate
            ? new Date(ngbDate.year, ngbDate.month - 1, ngbDate.day)
            : undefined;
    }

    isHovered(date: NgbDate) {
        return this.fromDate && !this.toDate &&
            this.hoveredDate && this.isAfterNgbDate(date, this.fromDate) &&
            this.isBeforeNgbDate(date, this.hoveredDate) && this.isRangeNgbDate(this.fromDate, this.hoveredDate, this.maxMonth);
    }

    isInside(date: NgbDate) {
        return this.isAfterNgbDate(date, this.fromDate) && this.isBeforeNgbDate(date, this.toDate);
    }

    isRange(date: NgbDate) {
        return this.isEqualsNgbDate(date, this.fromDate) ||
            this.isEqualsNgbDate(date, this.toDate) || this.isInside(date) || this.isHovered(date);
    }

    isFrom(date) {
        return this.isEqualsNgbDate(date, this.fromDate);
    }

    isTo(date) {
        this.isEqualsNgbDate(date, this.toDate);
    }

    isRangeNgbDate = (one: NgbDateStruct, two: NgbDateStruct, month: number) =>
        this.maxMonth > 0
            ? two.year - one.year === 0 && two.month - one.month < month
            : true;

    isEqualsNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        one && two && two.year === one.year && two.month === one.month && two.day === one.day;

    isBeforeNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day < two.day : one.month < two.month : one.year < two.year;

    isAfterNgbDate = (one: NgbDateStruct, two: NgbDateStruct) =>
        !one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
            ? false : one.day > two.day : one.month > two.month : one.year > two.year;
}
