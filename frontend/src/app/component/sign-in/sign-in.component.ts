import {Component} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CustomErrorStateMatcher} from "../../service/custom-error-state-matcher/custom-error-state-matcher";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {NgIf} from "@angular/common";
import {UserService} from "../../service/user/user.service";

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    RouterLink,
    ReactiveFormsModule,
    MatButtonModule,
    NgIf
  ],
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent {

  signInForm: FormGroup;
  matcher = new CustomErrorStateMatcher();
  displayInvalidCredentials: boolean = false;

  constructor(private fb: FormBuilder,
              private router: Router,
              private authService: AuthenticationService,
              private userService: UserService) {
    this.signInForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  signIn() {
    this.signInForm.markAllAsTouched()

    if (this.signInForm.invalid) {
      return;
    }

    this.authService.signIn(this.signInForm.value).subscribe(
      {
        next: (response) => {
          this.authService.storeToken(response.token);

          this.fetchUser(response.userUuid);
        },
        error: () => this.displayInvalidCredentials = true
      }
    )
  }

  private fetchUser(userUuid: string) {
    this.userService.fetchUser(userUuid).subscribe({
      next: response => {
        this.authService.currentUser = response
        this.authService.teamOwner = response.team?.uuid
        this.router.navigate(['/dashboard']);
      },
    })
  }
}
