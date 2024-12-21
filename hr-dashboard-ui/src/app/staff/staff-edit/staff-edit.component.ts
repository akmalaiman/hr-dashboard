import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {StaffService} from "../service/staff.service";
import {JobPosition} from "../../common/model/job-position.model";
import {Role} from "../../common/model/role.model";
import {Department} from "../../common/model/department.model";
import {Staff} from "../../common/model/staff.model";
import Swal from "sweetalert2";

@Component({
        selector: 'app-staff-edit',
        standalone: true,
        imports: [
                RouterLink,
                ReactiveFormsModule,
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
        userId: number = 0;

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
                        this.userId = id;
                });

                this.fetchStaffById(this.userId);
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

        onSubmit(): void {

                Object.keys(this.editStaffForm.controls).forEach(field => {
                        const control = this.editStaffForm.get(field);

                        if (control?.hasValidator(Validators.required) && control?.value === '') {
                                control.markAsTouched();
                        }
                });

                if (this.editStaffForm?.valid) {
                        const selectedJobPosition = this.editStaffForm.value.jobPositionId;
                        const selectedRoleId = this.editStaffForm.value.roleId;
                        const selectedDepartmentId = this.editStaffForm.value.departmentId;

                        const jobPosition = this.jobPositions.find(value => value.id == selectedJobPosition);
                        const role = this.roles.find(value => value.id == selectedRoleId);
                        const department = this.departments.find(value => value.id == selectedDepartmentId);

                        const staffData: Staff = {
                                ...this.editStaffForm.value,
                                id: Number(this.userId),
                                postalCode: this.editStaffForm.value.postalCode ? Number(this.editStaffForm.value.postalCode) : 0,
                                status: "",
                                createdAt: new Date(),
                                createdBy: 0,
                                updatedAt: new Date(),
                                updatedBy: 0,
                                jobPositionId: jobPosition,
                                roles: role ? [{id: role.id, name: role.name}] : [],
                                departmentId: department
                        };

                        this.staffService.updateStaff(this.userId, staffData).subscribe({
                                next: (data: Staff) => {
                                        Swal.fire({
                                                icon: "success",
                                                text: "New User Successfully Created!",
                                                confirmButtonText: "Close",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false
                                        }).then((result) => {
                                                this.router.navigate(['/user']);
                                        });
                                },
                                error: (error: any) => {
                                        Swal.fire({
                                                icon: "error",
                                                title: "Oops!",
                                                text: "An error occurred while updating the staff. Please try again.",
                                                confirmButtonText: "OK",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false
                                        });
                                        console.log("Error updating staff: ", error);
                                }
                        });
                }

        }

}
