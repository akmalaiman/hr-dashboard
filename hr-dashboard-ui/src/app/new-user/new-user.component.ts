import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {UserService} from "./service/user.service";
import {JobPosition} from "./model/job-position.model";
import {Role} from "./model/role.model";
import {User} from "./model/user.model";
import Swal from "sweetalert2";

@Component({
        selector: 'app-new-user',
        standalone: true,
        imports: [
                RouterLink,
                ReactiveFormsModule,
                NgIf,
                NgForOf
        ],
        templateUrl: './new-user.component.html',
        styleUrl: './new-user.component.css',
        providers: [UserService]
})
export class NewUserComponent implements OnInit {

        pageName: string = "New Staff Form";
        newStaffForm!: FormGroup;
        jobPositions: JobPosition[] = [];
        roles: Role[] = [];
        isUsernameExist: boolean = false;
        isEmailExist: boolean = false;

        constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
        }

        ngOnInit(): void {
                this.newStaffForm = this.formBuilder.group({
                        firstName: ['', [Validators.required]],
                        lastName: ['', [Validators.required]],
                        username: ['', [Validators.required]],
                        email: ['', [Validators.required, Validators.email]],
                        password: ['', [Validators.required]],
                        jobPositionId: ['', [Validators.required]],
                        roleId: ['', [Validators.required]],
                        address: [''],
                        city: [''],
                        country: [''],
                        state: [''],
                        postalCode: ['']
                });

                this.loadJobPositions();
                this.loadRoles();

                this.newStaffForm.get("username")?.valueChanges.subscribe(value => {
                        this.checkUsername(value);
                });

                this.newStaffForm.get("email")?.valueChanges.subscribe(value => {
                        this.checkEmail(value);
                });

        }

        loadJobPositions(): void {
                this.userService.getJobPosition().subscribe({
                        next: data => {
                                this.jobPositions = data;
                        },
                        error: error => {
                                console.error("Error fetching job positions: ", error);
                        }
                });
        }

        loadRoles(): void {
                this.userService.getRole().subscribe({
                        next: data => {
                                this.roles = data;
                        },
                        error: error => {
                                console.error("Error fetching roles: ", error);
                        }
                });
        }

        checkUsername(username: string): void {
                this.userService.getUsername(username).subscribe({
                        next: data => {
                                this.isUsernameExist = true;
                        },
                        error: error => {
                                this.isUsernameExist = false;
                        }
                });
        }

        checkEmail(email: string): void {
                this.userService.getEmail(email).subscribe({
                        next: data => {
                                this.isEmailExist = true;
                        },
                        error: error => {
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

                        const jobPosition = this.jobPositions.find(value => value.id == selectedJobPosition);
                        const role = this.roles.find(value => value.id == selectedRole);

                        const userData: User = {
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
                        };

                        this.userService.createUser(userData).subscribe({
                                next: User => {
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
                                error: error => {
                                        console.error("Error creating new user: ", error);
                                }
                        });
                }
        }

}
