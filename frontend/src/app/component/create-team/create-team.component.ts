import {Component} from '@angular/core';
import {MatAnchor, MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {CustomErrorStateMatcher} from "../../service/custom-error-state-matcher/custom-error-state-matcher";
import {TeamService} from "../../service/team/team.service";
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {AuthenticationService} from "../../service/authentication/authentication.service";

@Component({
  selector: 'app-create-team',
  standalone: true,
  imports: [
    MatAnchor,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    AppBarComponent
  ],
  templateUrl: './create-team.component.html',
  styleUrl: './create-team.component.scss'
})
export class CreateTeamComponent {

  createTeamForm: FormGroup;
  matcher = new CustomErrorStateMatcher();
  displayError: boolean = false;

  constructor(private fb: FormBuilder,
              private router: Router,
              private teamService: TeamService,
              private authService: AuthenticationService) {
    this.createTeamForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
      maxMembers: ['', [Validators.required, Validators.min(10), Validators.pattern("^[0-9]*$")]],
    });
  }

  createTeam() {

    this.createTeamForm.markAllAsTouched()

    if (this.createTeamForm.invalid) {
      return;
    }

    this.teamService.createTeam(this.createTeamForm.value).subscribe(
      {
        next: (response) => {
          if (this.authService.currentUser) {
            const user = this.authService.currentUser;
            user.team = response
            this.authService.currentUser = user;
            this.authService.teamOwner = response.uuid
          }
          this.router.navigate([`/teams/${response.uuid}`]);
        },
        error: () => this.displayError = true
      }
    )
  }
}
