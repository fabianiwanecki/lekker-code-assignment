import { Component } from '@angular/core';
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {TeamService} from "../../service/team/team.service";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {CustomErrorStateMatcher} from "../../service/custom-error-state-matcher/custom-error-state-matcher";

@Component({
  selector: 'app-edit-team',
  standalone: true,
    imports: [
        AppBarComponent,
        MatButton,
        MatError,
        MatFormField,
        MatInput,
        MatLabel,
        NgIf,
        ReactiveFormsModule
    ],
  templateUrl: './edit-team.component.html',
  styleUrl: './edit-team.component.scss'
})
export class EditTeamComponent {


  editTeamForm: FormGroup;
  matcher = new CustomErrorStateMatcher();
  displayError: boolean = false;

  constructor(private fb: FormBuilder,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private teamService: TeamService,
              private authService: AuthenticationService) {
    this.editTeamForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
      maxMembers: ['', [Validators.required, Validators.min(10), Validators.pattern("^[0-9]*$")]],
    });
  }

  updateTeam() {
    this.editTeamForm.markAllAsTouched()

    if (this.editTeamForm.invalid) {
      return;
    }

    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    this.teamService.updateTeam(teamUuid, this.editTeamForm.value).subscribe({
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
