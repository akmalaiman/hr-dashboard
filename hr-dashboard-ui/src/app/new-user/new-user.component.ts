import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
        selector: 'app-new-user',
        standalone: true,
        imports: [
                RouterLink
        ],
        templateUrl: './new-user.component.html',
        styleUrl: './new-user.component.css'
})
export class NewUserComponent {
        pageName: string = "New Staff Form";
}
