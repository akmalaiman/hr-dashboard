import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {StaffService} from "../service/staff.service";
import {JobPosition} from "../../common/model/job-position.model";
import {Role} from "../../common/model/role.model";
import {Department} from "../../common/model/department.model";
import {Staff} from "../../common/model/staff.model";

@Component({
        selector: 'app-staff-edit',
        standalone: true,
        imports: [
                RouterLink,
                ReactiveFormsModule,
                NgIf,
                NgForOf
        ],
        templateUrl: './staff-edit.component.html',
        styleUrl: './staff-edit.component.css'
})
export class StaffEditComponent implements OnInit {

        pageName: string = "Edit Staff Form";
        editStaffForm!: FormGroup;
        jobPositions: JobPosition[] = [];
        roles: Role[] = [];
        departments: Department[] = [];

        constructor(private formBuilder: FormBuilder, private staffService: StaffService, private router: Router, private activatedRoute: ActivatedRoute) {
        }

        ngOnInit(): void {

                this.editStaffForm = this.formBuilder.group({
                        firstName: ['', Validators.required],
                        lastName: ['', Validators.required],
                        username: [{value: '', disabled: true}, Validators.required],
                        email: [{value: '', disabled: true}, Validators.required],
                        jobPositionId: ['', Validators.required],
                        departmentId: [''],
                        roleId: ['', Validators.required],
                        address: [''],
                        city: [''],
                        country: [''],
                        state: [''],
                        postalCode: ['']
                });

                this.activatedRoute.params.subscribe(params => {
                        const id = params['id'];
                        this.fetchStaffById(id);
                });

                this.loadJobPositions();
                this.loadRoles();
                this.loadDepartments();

        }

        fetchStaffById(id: number): void {
                this.staffService.getStaff(id).subscribe({
                        next: (data: Staff) => {
                                this.editStaffForm.patchValue({
                                        firstName: data.firstName,
                                        lastName: data.lastName,
                                        username: data.username,
                                        email: data.email,
                                        jobPositionId: data.jobPositionId.id,
                                        departmentId: data.departmentId.id,
                                        roleId: data.roles[0].id,
                                        address: data.address,
                                        city: data.city,
                                        country: data.country,
                                        state: data.state,
                                        postalCode: data.postalCode
                                });
                        },
                        error: (error: any) => {
                                console.log("Error fetching staff by id: ", error);
                        }
                });
        }

        loadJobPositions(): void {
                this.staffService.getJobPosition().subscribe({
                        next: (data: JobPosition[]) => {
                                this.jobPositions = data;
                        },
                        error: (error: any) => {
                                console.log("Error fetching job positions: ", error);
                        }
                });
        }

        loadRoles(): void {
                this.staffService.getRole().subscribe({
                        next: (data: Role[]) => {
                                this.roles = data;
                        },
                        error: (error: any) => {
                                console.log("Error fetching roles: ", error);
                        }
                });
        }

        loadDepartments(): void {
                this.staffService.getDepartment().subscribe({
                        next: (data: Department[]) => {
                                this.departments = data;
                        },
                        error: (error: any) => {
                                console.log("Error fetching departments: ", error);
                        }
                });
        }

        onSubmit(): void {}

}
