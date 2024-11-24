import {AfterViewChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {DepartmentService} from "../service/department.service";
import {NgbModal, NgbModalConfig, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {Department} from "../../common/model/department.model";
import Swal from "sweetalert2";

@Component({
        selector: 'app-department-home',
        standalone: true,
        imports: [
                RouterLink,
                FormsModule,
                NgIf,
                ReactiveFormsModule,
                NgForOf,
                NgbTooltip
        ],
        templateUrl: './department-home.component.html',
        styleUrl: './department-home.component.css'
})
export class DepartmentHomeComponent implements OnInit, AfterViewChecked, OnDestroy {

        pageName: string = "Department Management";
        newDepartmentForm!: FormGroup;
        isDepartmentNameExists: boolean = false;
        loading: boolean = true;
        departmentList: any[] = [];
        private dataTable: any;
        private isDataTableInit: boolean = false;

        constructor(private departmentService: DepartmentService, config: NgbModalConfig, private modalService: NgbModal, private formBuilder: FormBuilder) {
                config.backdrop = "static";
                config.keyboard = false;
        }

        ngOnInit(): void {
                this.fetchData();

                this.newDepartmentForm = this.formBuilder.group({
                        name: ['', [Validators.required]]
                });

                this.newDepartmentForm.get("name")?.valueChanges.subscribe(value => {
                        this.checkDepartmentName(value);
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

        fetchData(): void {
                this.departmentService.getDepartmentWithStaffCount().subscribe({
                        next: (data: any[]) => {
                                if (!data) {
                                        this.loading = false;
                                        this.departmentList = [];
                                        return;
                                }

                                this.departmentList = data.map(item => ({
                                        id: item.id,
                                        name: item.name,
                                        staffCount: item.staffCount
                                }));
                                this.loading = false;
                        },
                        error: (error) => {
                                console.error("Failed to fetch department data: ", error);
                                this.loading = false;
                        }
                });
        }

        initDataTable(): void {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }

                this.dataTable = $('#departmentListTable').DataTable({
                        scrollY: "500px",
                        scrollX: true,
                        scrollCollapse: true,
                        paging: true,
                        lengthChange: false,
                        searching: false,
                        info: false,
                        language: {
                                "emptyTable": "There is no active department found",
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
                this.initDataTable()
        }

        openModal(content: any): void {
                this.modalService.open(content, {centered: true});
        }

        checkDepartmentName(departmentName: string): void {
                this.departmentService.getDepartmentName(departmentName).subscribe({
                        next: (data: any) => {
                                this.isDepartmentNameExists = true;
                        },
                        error: (error) => {
                                this.isDepartmentNameExists = false;
                        }
                });
        }

        onSubmit(): void {

                if (this.newDepartmentForm?.valid) {
                        const departmentData: Department = {
                                ...this.newDepartmentForm.value,
                                id: 0,
                                name: this.newDepartmentForm.value.name,
                                status: "",
                                createdAt: new Date(),
                                createdBy: 0,
                                updatedAt: new Date(),
                                updatedBy: 0
                        };

                        this.departmentService.createDepartment(departmentData).subscribe({
                                next: (data: Department) => {
                                        this.modalService.dismissAll();
                                        this.newDepartmentForm.reset();
                                        Swal.fire({
                                                icon: "success",
                                                text: "New Department Successfully Created!",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false,
                                                showConfirmButton: false,
                                                timer: 2000,
                                        });
                                        this.refreshData();
                                },
                                error: (error) => {
                                        Swal.fire({
                                                title: 'Oops!',
                                                text: 'An error occurred while creating the new Department. Please try again.',
                                                icon: 'error',
                                                confirmButtonText: 'OK'
                                        });
                                }
                        });
                }

        }

        deleteDepartment(departmentId: number, staffCount: number): void {

                if (staffCount > 0) {
                        Swal.fire({
                                icon: "warning",
                                title: "Department cannot be deleted!",
                                text: "There are active staff with the Department.",
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
                        this.departmentService.deleteDepartment(departmentId).subscribe({
                                next: (data: any) => {
                                        Swal.fire({
                                                icon: "success",
                                                text: "Department Successfully Deleted!",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false,
                                                showConfirmButton: false,
                                                timer: 2000,
                                        });
                                        this.refreshData();
                                },
                                error: (error) => {
                                        Swal.fire({
                                                title: 'Oops!',
                                                text: 'An error occurred while deleting the Department. Please try again.',
                                                icon: 'error',
                                                confirmButtonText: 'OK'
                                        });
                                }
                        });
                }

        }

}
