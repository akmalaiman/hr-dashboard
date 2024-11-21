import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {UploadService} from "./service/upload.service";
import {NgClass, NgIf} from "@angular/common";

@Component({
        selector: 'app-upload',
        standalone: true,
        imports: [
                RouterLink,
                NgClass,
                NgIf
        ],
        templateUrl: './upload.component.html',
        styleUrl: './upload.component.css'
})
export class UploadComponent {

        pageName: string = "Upload File";
        toastMessage: string = "";
        toastType: 'success' | 'danger' = 'success';
        showToast: boolean = false;

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
                        error: error => {
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

}
