import {AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import 'datatables.net';
import {NgForOf, NgIf} from "@angular/common";
import Swal from "sweetalert2";
import {StaffService} from "../staff-new/service/staff.service";
import {NgbTooltip} from "@ng-bootstrap/ng-bootstrap";

@Component({
        selector: 'app-user-management',
        standalone: true,
        imports: [
                RouterLink,
                NgForOf,
                NgIf,
                NgbTooltip
        ],
        templateUrl: './staff-home.component.html',
        styleUrl: './staff-home.component.css'
})
export class StaffHomeComponent implements OnInit, AfterViewChecked, OnDestroy{

        pageName: string = "Staff Management";
        staffList: Staff[] = [];
        loading: boolean = true;
        private dataTable: any;
        private isDataTableInit = false;

        constructor(private http: HttpClient, private userService: StaffService) {
        }

        ngOnInit(): void {
                this.fetchStaffData();
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
                const token = localStorage.getItem("access_token");

                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}` ,
                });

                this.http.get<any[]>("http://localhost:8080/api/user/all", {headers}).subscribe((data) => {
                        this.staffList = data.map(item => new Staff(
                                item.id,
                                item.firstName,
                                item.lastName,
                                item.email,
                                item.jobPositionId ? item.jobPositionId.name : 'N/A',
                                item.roles && item.roles.length > 0 ? item.roles[0].name : 'N/A'
                        ));
                        this.loading = false;
                }, error => {
                        console.error("Failed to fetch staff data: ", error);
                        this.loading = false;
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
                                this.userService.deleteUser(id).subscribe({
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
                        searching: false,
                        info: false,
                        language: {
                                "emptyTable": "There is no active user found",
                        }
                });
        }

}

export class Staff {

        id: number;
        firstName: string;
        lastName: string;
        email: string;
        jobPosition: string;
        role: string

        constructor(id: number, firstName: string, lastName: string, email: string, jobPosition: string, role: string) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.jobPosition = jobPosition;
                this.role = role;
        }

}
