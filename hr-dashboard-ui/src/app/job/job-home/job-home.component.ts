import {AfterViewChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {NgbModal, NgbModalConfig, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {JobPosition} from "../../common/model/job-position.model";
import {JobService} from "../service/job.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
        selector: 'app-job-home',
        standalone: true,
        imports: [
                RouterLink,
                NgIf,
                NgForOf,
                NgbTooltip,
                ReactiveFormsModule
        ],
        templateUrl: './job-home.component.html',
        styleUrl: './job-home.component.css'
})
export class JobHomeComponent implements OnInit, AfterViewChecked, OnDestroy {

        pageName: string = "Job Position Management";
        loading: boolean = true;
        jobPositionList: any[] = [];
        private dataTable: any;
        private isDataTableInit = false;
        newJobPositionForm!: FormGroup;
        isJobNameExists = false;

        constructor(private jobService: JobService, config: NgbModalConfig, private modalService: NgbModal, private formBuilder: FormBuilder) {
                config.backdrop = "static";
                config.keyboard = false;
        }

        ngOnInit(): void {
                this.fetchData();

                this.newJobPositionForm = this.formBuilder.group({
                        name: ['', [Validators.required]]
                });

                this.newJobPositionForm.get("name")?.valueChanges.subscribe(value => {
                        this.checkJobPositionName(value);
                });
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
                this.jobService.getJobNameWithStaffCount().subscribe({
                        next: (data: any[]) => {
                                this.jobPositionList = data.map(item => ({
                                        id: item.id,
                                        name: item.name,
                                        staffCount: item.staffCount
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
                        this.isDataTableInit = false;
                }
                this.loading = true;
                this.fetchData();
        }

        openModal(content: any): void {
                this.modalService.open(content, {centered: true});
        }

        checkJobPositionName(name: string): void {
                this.jobService.getJobPositionName(name).subscribe({
                        next: (data: any[]) => {
                                this.isJobNameExists = true;
                        },
                        error: error => {
                                this.isJobNameExists = false;
                        }
                });
        }

        onSubmit() {

                if (this.newJobPositionForm?.valid) {
                        const jobPositionData: JobPosition = {
                                ...this.newJobPositionForm.value,
                                id: 0,
                                name: this.newJobPositionForm.value.name,
                                status: "",
                                createdAt: new Date(),
                                createdBy: 0,
                                updatedAt: new Date(),
                                updatedBy: 0
                        };

                        this.jobService.createJobPosition(jobPositionData).subscribe({
                                next: JobPosition => {
                                        this.modalService.dismissAll();
                                        this.newJobPositionForm.reset();
                                        Swal.fire({
                                                icon: "success",
                                                text: "New Job Position Successfully Created!",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false,
                                                showConfirmButton: false,
                                                timer: 2000,
                                        });
                                        this.refreshData();
                                },
                                error: error => {
                                        Swal.fire({
                                                title: 'Oops!',
                                                text: 'An error occurred while creating the new Job Position. Please try again.',
                                                icon: 'error',
                                                confirmButtonText: 'OK'
                                        });
                                }
                        });
                }

        }

        deleteJobPosition(jobPositionId: number, staffCount: number): void {

                if (staffCount > 0) {
                        Swal.fire({
                                icon: "warning",
                                title: "Job Position cannot be deleted!",
                                text: "There are active staff with the Job Position.",
                                allowOutsideClick: false,
                                allowEscapeKey: false,
                                showConfirmButton: false,
                                showCloseButton: true,
                                showCancelButton: true,
                                customClass: {
                                        cancelButton: 'btn btn-danger'
                                }
                        });
                } else {
                        this.jobService.deleteJobPosition(jobPositionId).subscribe({
                                next: (data: any) => {
                                        Swal.fire({
                                                icon: "success",
                                                text: "Job Position Successfully Deleted!!",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false,
                                                showConfirmButton: false,
                                                timer: 2000,
                                        });
                                        this.refreshData();
                                },
                                error: error => {
                                        Swal.fire({
                                                title: 'Oops!',
                                                text: 'An error occurred while deleting the Job Position. Please try again.',
                                                icon: 'error',
                                                confirmButtonText: 'OK'
                                        });
                                }
                        });
                }

        }

}
