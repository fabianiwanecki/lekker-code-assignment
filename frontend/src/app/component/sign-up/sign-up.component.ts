import {Component} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {CustomErrorStateMatcher} from "../../service/custom-error-state-matcher/custom-error-state-matcher";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})

export class SignUpComponent {
  signUpForm: FormGroup;
  matcher = new CustomErrorStateMatcher();
  displayError: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthenticationService, private router: Router) {
    this.signUpForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
    });
  }

  signUp() {
    this.signUpForm.markAllAsTouched()

    if (this.signUpForm.invalid) {
      return;
    }

    this.authService.signUp(this.signUpForm.value).subscribe(
      {
        next: (response) => {
          this.router.navigate(['/sign-in']);
        },
        error: () => this.displayError = true
      }
    )
  }
}
