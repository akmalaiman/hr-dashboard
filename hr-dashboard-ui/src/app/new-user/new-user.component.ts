import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
        selector: 'app-new-user',
        standalone: true,
        imports: [
                RouterLink,
                ReactiveFormsModule
        ],
        templateUrl: './new-user.component.html',
        styleUrl: './new-user.component.css'
})
export class NewUserComponent {
        pageName: string = "New Staff Form";
}
