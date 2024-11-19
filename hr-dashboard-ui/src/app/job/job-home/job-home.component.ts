import {AfterViewChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {JobPosition} from "../../common/model/job-position.model";
import {JobService} from "../service/job.service";

@Component({
        selector: 'app-job-home',
        standalone: true,
        imports: [
                RouterLink,
                NgIf,
                NgForOf,
                NgbTooltip
        ],
        templateUrl: './job-home.component.html',
        styleUrl: './job-home.component.css'
})
export class JobHomeComponent implements OnInit, AfterViewChecked, OnDestroy {

        pageName: string = "Job Position Management";
        loading: boolean = true;
        jobPositionList: JobPosition[] = [];
        private dataTable: any;
        private isDataTableInit = false;

        constructor(private jobService: JobService) {
        }

        ngOnInit(): void {
                this.fetchData();
        }

        ngAfterViewChecked() {
                if (!this.isDataTableInit && !this.loading) {
                        this.initDataTable();
                        this.isDataTableInit = true;
                }
        }

        ngOnDestroy() {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }
        }

        fetchData() {
                this.jobService.getJobList().subscribe({
                        next: (data: any[]) => {
                                this.jobPositionList = data.map(item => ({
                                        id: item.id,
                                        name: item.name,
                                        status: item.status,
                                        createdAt: new Date(item.createdAt),
                                        createdBy: item.createdBy,
                                        updatedAt: new Date(item.updatedAt),
                                        updatedBy: item.updatedBy
                                }));
                                this.loading = false;
                        },
                        error: (err: any) => {
                                console.error("Failed to fetch job position data: ", err);
                                this.loading = false;
                        }
                });
        }

        initDataTable(): void {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }

                this.dataTable = $('#jobListTable').DataTable({
                        scrollY: "500px",
                        scrollX: true,
                        scrollCollapse: true,
                        paging: true,
                        lengthChange: false,
                        searching: false,
                        info: false,
                        language: {
                                "emptyTable": "There is no active job position found",
                        }
                });
        }

        refreshData(): void {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }
                this.loading = true;
                this.fetchData();
        }

}
