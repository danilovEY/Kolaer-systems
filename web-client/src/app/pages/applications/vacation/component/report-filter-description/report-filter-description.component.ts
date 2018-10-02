import {Component, Input} from '@angular/core';
import {ReportFilterModel} from '../../model/report-filter.model';

@Component({
    selector: 'report-filter-description',
    template: `
        <h2>Фильтры</h2>

        <div class="row" *ngIf="filterModel">
            <div class="col-2">
                <h6>Период:</h6>
                <label>{{ filterModel.from | date: 'dd.MM.yyyy' }} - {{ filterModel.to | date: 'dd.MM.yyyy' }}</label>
            </div>

            <div class="col-2" *ngIf="!filterModel.selectedAllDepartments && filterModel.selectedDepartments.length > 0">
                <h6>Подразделения:</h6>
                <ul>
                    <li *ngFor="let department of filterModel.selectedDepartments"> {{ department.abbreviatedName }}</li>
                </ul>
            </div>

            <div class="col-2" *ngIf="filterModel.selectedAllDepartments">
                <h6>Подразделения:</h6>
                <label>Все подразделения</label>
            </div>

            <div class="col-2" *ngIf="filterModel.selectedEmployees.length > 0">
                <h6>Сотрудники:</h6>
                <ul>
                    <li *ngFor="let employee of filterModel.selectedEmployees"> {{ employee.initials }}</li>
                </ul>
            </div>

            <div class="col-2" *ngIf="filterModel.selectedPosts.length > 0">
                <h6>Должности:</h6>
                <ul>
                    <li *ngFor="let post of filterModel.selectedPosts"> {{ post.abbreviatedName }}</li>
                </ul>
            </div>

            <div class="col-2" *ngIf="filterModel.selectedTypeWorks.length > 0">
                <h6>Виды работ:</h6>
                <ul>
                    <li *ngFor="let typeWork of filterModel.selectedTypeWorks"> {{ typeWork.name }}</li>
                </ul>
            </div>
        </div>
    `
})
export class ReportFilterDescriptionComponent {

    @Input()
    filterModel: ReportFilterModel;

}
