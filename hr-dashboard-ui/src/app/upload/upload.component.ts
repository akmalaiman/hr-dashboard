import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {UploadService} from "./service/upload.service";

@Component({
        selector: 'app-upload',
        standalone: true,
        imports: [
                RouterLink
        ],
        templateUrl: './upload.component.html',
        styleUrl: './upload.component.css'
})
export class UploadComponent {
        pageName: string = "Upload File";

        constructor(private http: HttpClient, private uploadService: UploadService) {
        }

        downloadTemplate(filename: string) {
                this.uploadService.getTemplate(filename).subscribe({
                        next: (res: Blob) => {
                                const blobUrl = window.URL.createObjectURL(res);
                                const anchor = document.createElement("a");
                                anchor.href = blobUrl;
                                anchor.download = `${filename}.csv`;
                                anchor.click();
                                window.URL.revokeObjectURL(blobUrl);
                        },
                        error: error => {
                                console.error("Failed to download template: ", error);
                        }
                });
        }
}
