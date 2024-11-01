import {AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import 'datatables.net';
import {NgForOf, NgIf} from "@angular/common";

@Component({
        selector: 'app-user-management',
        standalone: true,
        imports: [
                RouterLink,
                NgForOf,
                NgIf
        ],
        templateUrl: './user-management.component.html',
        styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit, AfterViewChecked, OnDestroy{

        pageName: string = "User Management";
        staffList: Staff[] = [];
        loading: boolean = true;
        private dataTable: any;
        private isDataTableInit = false;

        constructor(private http: HttpClient) {
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
