import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {UploadService} from "../service/upload.service";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
        selector: 'app-upload',
        standalone: true,
        imports: [
                RouterLink,
                NgClass,
                NgIf,
                ReactiveFormsModule,
                FormsModule,
                NgForOf
        ],
        templateUrl: './upload-home.component.html',
        styleUrl: './upload-home.component.css'
})
export class UploadHomeComponent {

        pageName: string = "Upload File";
        toastMessage: string = "";
        toastType: 'success' | 'danger' = 'success';
        showToast: boolean = false;
        selectedFile: File | null = null;
        isUploading: boolean = false;
        uploadResult: { savedCount: number; duplicateCount: number; entity: string } = {
                savedCount: 0,
                duplicateCount: 0,
                entity: ''
        };
        templateList: any[] = [
                {filename: "staff", displayName: "Staff"},
                {filename: "jobPosition", displayName: "Job Position"},
                {filename: "department", displayName: "Department"},
        ];

        constructor(private http: HttpClient, private uploadService: UploadService) {
        }

        downloadTemplate(filename: string, displayName: string) {
                this.uploadService.getTemplate(filename).subscribe({
                        next: (res: Blob) => {
                                const blobUrl = window.URL.createObjectURL(res);
                                const anchor = document.createElement("a");
                                anchor.href = blobUrl;
                                anchor.download = `${filename}.csv`;
                                anchor.click();
                                window.URL.revokeObjectURL(blobUrl);

                                this.toastMessage = `The ${displayName} template is being downloaded!`;
                                this.toastType = "success";
                                this.showToast = true;
                                this.hideToastAfterDelay();
                        },
                        error: (error) => {
                                console.error("Failed to download template: ", error);

                                this.toastMessage = `Failed to download ${displayName} template. Please try again.`;
                                this.toastType = "danger";
                                this.showToast = true;
                                this.hideToastAfterDelay();
                        }
                });
        }

        private hideToastAfterDelay(): void {
                setTimeout(() => {
                        this.showToast = false;
                }, 5000);
        }

        onFileSelected(event: Event): void {
                const input = event.target as HTMLInputElement;
                if (input?.files?.length) {
                        const file = input.files[0];

                        if (!file.name.endsWith(".csv")) {
                                Swal.fire({
                                        title: 'Oops!',
                                        text: 'Only .csv files are allowed',
                                        icon: 'error',
                                        showConfirmButton: false,
                                        showCloseButton: true
                                });
                                return;
                        }

                        if (file.size > 5 * 1024 * 1024) {
                                Swal.fire({
                                        title: 'Oops!',
                                        text: 'Your file size exceeds 5MB.',
                                        icon: 'warning',
                                        showConfirmButton: false,
                                        showCloseButton: true
                                });
                                return;
                        }

                        this.selectedFile = file;
                }
        }

        onSubmit(): void {
                if (!this.selectedFile) {
                        Swal.fire({
                                title: 'Oops!',
                                text: 'No file is selected. Please choose a file to upload',
                                icon: 'warning',
                                showConfirmButton: false,
                                showCloseButton: true
                        });
                        return;
                }

                this.isUploading = true;

                this.uploadService.uploadFile(this.selectedFile).subscribe({
                        next: (response) => {
                                this.uploadResult = response;
                                console.log("upload result: ", this.uploadResult);
                                this.isUploading = false;
                                this.selectedFile = null;

                                const input = document.getElementById("attachment") as HTMLInputElement;
                                if (input) {
                                        input.value = "";
                                }
                        },
                        error: (error) => {
                                Swal.fire({
                                        title: 'Oops!',
                                        text: 'An error occurred while uploading the file.',
                                        icon: 'warning',
                                        showConfirmButton: false,
                                        showCloseButton: true
                                });
                                this.isUploading = false;
                        }
                });
        }
}
