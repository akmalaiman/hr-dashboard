import {AfterViewChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import 'datatables.net';
import {NgForOf, NgIf} from "@angular/common";
import Swal from "sweetalert2";
import {StaffService} from "../service/staff.service";
import {NgbModal, NgbModalConfig, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
    selector: 'app-user-management',
    standalone: true,
    imports: [
        RouterLink,
        NgForOf,
        NgIf,
        NgbTooltip,
        ReactiveFormsModule
    ],
    templateUrl: './staff-home.component.html',
    styleUrl: './staff-home.component.css'
})
export class StaffHomeComponent implements OnInit, AfterViewChecked, OnDestroy {

    pageName: string = "Staff Management";
    staffList: any[] = [];
    loading: boolean = true;
    private dataTable: any;
    private isDataTableInit = false;
    resetPasswordForm!: FormGroup;
    userId: number = 0;

    constructor(private http: HttpClient, private staffService: StaffService, config: NgbModalConfig, private modalService: NgbModal, private formBuilder: FormBuilder) {
        config.backdrop = "static";
        config.keyboard = false;
    }

    ngOnInit(): void {
        this.fetchStaffData();

        this.resetPasswordForm = this.formBuilder.group({
            password: ['', [Validators.required, Validators.minLength(8)]]
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

    fetchStaffData(): void {
        this.staffService.getStaffList().subscribe({
            next: (data: any[]) => {
                if (!data) {
                    this.loading = false;
                    this.staffList = [];
                    return;
                }

                this.staffList = data.map(item => ({
                    id: item.id,
                    firstName: item.firstName,
                    lastName: item.lastName,
                    username: item.username,
                    email: item.email,
                    jobPosition: item.jobPositionId ? item.jobPositionId.name : 'N/A',
                    role: item.roles && item.roles.length > 0 ? item.roles[0].name : 'N/A',
                    department: item.departmentId ? item.departmentId.name : 'N/A'
                }));
                this.loading = false;
            },
            error: (error) => {
                console.error("Failed to fetch staff data: ", error);
                this.loading = false;
            }
        });
    }

    deleteStaff(id: number): void {
        Swal.fire({
            title: 'Are you sure?',
            text: 'You won\'t be able to undo this action!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'Cancel',
            allowOutsideClick: false,
            allowEscapeKey: false,
        }).then((result) => {
            if (result.isConfirmed) {
                this.staffService.deleteUser(id).subscribe({
                    next: data => {
                        Swal.fire({
                            title: 'Deleted Successfully!',
                            text: 'The staff record has been removed from the system.',
                            icon: 'success',
                            timer: 2000,
                            showConfirmButton: false
                        });
                        this.refreshdata();
                    },
                    error: error => {
                        Swal.fire({
                            title: 'Oops!',
                            text: 'An error occurred while deleting the staff record. Please try again.',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    }
                });
            }
        });
    }

    refreshdata(): void {
        if (this.dataTable) {
            this.dataTable.destroy();
            this.isDataTableInit = false;
        }
        this.loading = true;
        this.fetchStaffData();
    }

    initDataTable(): void {
        if (this.dataTable) {
            this.dataTable.destroy();
        }

        this.dataTable = $('#staffListTable').DataTable({
            scrollY: "500px",
            scrollX: true,
            scrollCollapse: true,
            paging: true,
            lengthChange: false,
            searching: true,
            info: false,
            language: {
                "emptyTable": "There is no active staff found",
            }
        });
    }

    openModal(content: any, id: number): void {
        this.modalService.open(content, {centered: true});
        this.userId = id;
    }

    onSubmit(): void {

        if (this.resetPasswordForm?.valid) {
            const password = this.resetPasswordForm.value.password;

            this.staffService.updatePassword(this.userId, password).subscribe({
                next: (data: any) => {
                    Swal.fire({
                        icon: "success",
                        text: "Password Successfully Updated!",
                        confirmButtonText: "Close",
                        allowOutsideClick: false,
                        allowEscapeKey: false
                    });
                },
                error: (error: any) => {
                    Swal.fire({
                        icon: "error",
                        title: "Oops!",
                        text: "An error occurred while updating the password. Please try again.",
                        confirmButtonText: "OK",
                        allowOutsideClick: false,
                        allowEscapeKey: false
                    });
                    console.log("Error updating password: ", error);
                }
            });

            this.modalService.dismissAll();
            this.resetPasswordForm.reset();
        }

    }

}
