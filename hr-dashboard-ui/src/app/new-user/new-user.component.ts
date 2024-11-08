import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {UserService} from "./service/user.service";
import {JobPosition} from "./model/job-position.model";
import {Role} from "./model/role.model";
import {User} from "./model/user.model";

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

        constructor(private formBuilder: FormBuilder, private userService: UserService) {
        }

        ngOnInit(): void {
                this.newStaffForm = this.formBuilder.group({
                        firstName: ['', [Validators.required]],
                        lastName: ['', [Validators.required]],
                        username: ['', [Validators.required]],
                        email: ['', [Validators.required]],
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
        }

        loadJobPositions(): void {
                this.userService.getJobPosition().subscribe(
                        (data) => {
                                this.jobPositions = data;
                        },
                        (error) => {
                                console.error("Error fetching job positions: ", error);
                        }
                );
        }

        loadRoles(): void {
                this.userService.getRole().subscribe(
                        (data) => {
                                this.roles = data;
                        },
                        (error) => {
                                console.error("Error fetching roles: ", error);
                        }
                );
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
                                roleId: role,
                        };

                        this.userService.createUser(userData).subscribe(
                                (response) => {
                                        console.log("User created successfully!");
                                },
                                (error) => {
                                        console.error("Error creating new user: ", error);
                                }
                        );
                }
        }

}
