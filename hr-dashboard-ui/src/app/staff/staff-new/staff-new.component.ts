import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {StaffService} from "../service/staff.service";
import {JobPosition} from "../../common/model/job-position.model";
import {Role} from "../../common/model/role.model";
import {Staff} from "../../common/model/staff.model";
import Swal from "sweetalert2";
import {Department} from "../../common/model/department.model";

@Component({
    selector: 'app-new-user',
    standalone: true,
    imports: [
        RouterLink,
        ReactiveFormsModule,
        NgIf,
        NgForOf
    ],
    templateUrl: './staff-new.component.html',
    styleUrl: './staff-new.component.css',
    providers: [StaffService]
})
export class StaffNewComponent implements OnInit {

    pageName: string = "New Staff Form";
    newStaffForm!: FormGroup;
    jobPositions: JobPosition[] = [];
    roles: Role[] = [];
    departments: Department[] = [];
    managerList: Staff[] = [];
    isUsernameExist: boolean = false;
    isEmailExist: boolean = false;

    constructor(
        private formBuilder: FormBuilder,
        private staffService: StaffService,
        private router: Router
    ) {
    }

    ngOnInit(): void {

        this.newStaffForm = this.formBuilder.group({
            firstName: ['', [Validators.required]],
            lastName: ['', [Validators.required]],
            username: ['', [Validators.required]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required]],
            jobPositionId: ['', [Validators.required]],
            departmentId: [''],
            reportingToId: [''],
            roleId: ['', [Validators.required]],
            address: [''],
            city: [''],
            country: [''],
            state: [''],
            postalCode: ['']
        });

        this.loadJobPositions();
        this.loadRoles();
        this.loadDepartments();

        this.newStaffForm.get("username")?.valueChanges.subscribe(value => {
            this.checkUsername(value);
        });

        this.newStaffForm.get("email")?.valueChanges.subscribe(value => {
            this.checkEmail(value);
        });

    }

    loadJobPositions(): void {
        this.staffService.getJobPosition().subscribe({
            next: (data: JobPosition[]) => {
                this.jobPositions = data;
            },
            error: (error: any) => {
                console.error("Error fetching job positions: ", error);
            }
        });
    }

    loadRoles(): void {
        this.staffService.getRole().subscribe({
            next: (data: Role[]) => {
                this.roles = data;
            },
            error: (error: any) => {
                console.error("Error fetching roles: ", error);
            }
        });
    }

    loadDepartments(): void {
        this.staffService.getDepartment().subscribe({
            next: (data: Department[]) => {
                this.departments = data;
            },
            error: (error: any) => {
                console.error("Error fetching departments: ", error);
            }
        });
    }

    loadManagerList(departmentId: number) {
        this.staffService.getManagerByDepartmentId(departmentId).subscribe({
            next: (data: Staff[]) => {
                this.managerList = data;
            }, error: (error: any) => {
                console.error("Error fetching manager list: ", error);
            }
        });
    }

    checkUsername(username: string): void {
        this.staffService.getUsername(username).subscribe({
            next: (data: Staff) => {
                this.isUsernameExist = true;
            },
            error: (error: any) => {
                this.isUsernameExist = false;
            }
        });
    }

    checkEmail(email: string): void {
        this.staffService.getEmail(email).subscribe({
            next: (data: Staff) => {
                this.isEmailExist = true;
            },
            error: (error: any) => {
                this.isEmailExist = false;
            }
        });
    }

    onSubmit(): void {

        Object.keys(this.newStaffForm.controls).forEach(field => {
            const control = this.newStaffForm.get(field);

            if (control?.hasValidator(Validators.required) && control?.value === '') {
                control.markAsTouched();
            }
        });

        if (this.newStaffForm?.valid) {
            const selectedJobPosition = this.newStaffForm.value.jobPositionId;
            const selectedRole = this.newStaffForm.value.roleId;
            const selectedDepartment = this.newStaffForm.value.departmentId;

            const jobPosition = this.jobPositions.find(value => value.id == selectedJobPosition);
            const role = this.roles.find(value => value.id == selectedRole);
            const department = this.departments.find(value => value.id == selectedDepartment);

            const userData: Staff = {
                ...this.newStaffForm.value,
                id: 0,
                postalCode: this.newStaffForm.value.postalCode ? Number(this.newStaffForm.value.postalCode) : 0,
                status: "",
                createdAt: new Date(),
                createdBy: 0,
                updatedAt: new Date(),
                updatedBy: 0,
                jobPositionId: jobPosition,
                roles: role ? [{id: role.id, name: role.name}] : [],
                departmentId: department,
                reportingToId: this.newStaffForm.value.reportingToId ? Number(this.newStaffForm.value.reportingToId) : 0
            };

            this.staffService.createUser(userData).subscribe({
                next: (data: Staff) => {
                    Swal.fire({
                        icon: "success",
                        text: "New User Successfully Created!",
                        confirmButtonText: "Close",
                        showCancelButton: true,
                        cancelButtonText: "Add more staff",
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        customClass: {
                            confirmButton: "btn btn-danger",
                            cancelButton: "btn btn-primary",
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            this.router.navigate(['/user']);
                        } else {
                            this.newStaffForm.reset();
                        }
                    });
                },
                error: (error: any) => {
                    console.error("Error creating new user: ", error);
                }
            });
        }
    }

    protected readonly Number = Number;
}
