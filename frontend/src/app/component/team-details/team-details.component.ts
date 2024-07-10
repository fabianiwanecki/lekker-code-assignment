import {Component, OnInit} from '@angular/core';
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {ListItemComponent} from "../shared/list-item/list-item.component";
import {MatFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TitleHeaderComponent} from "../shared/title-header/title-header.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {TeamDetailedWithMembersDto, TeamRequestDto, TeamService} from "../../service/team/team.service";
import {ListItemTeamComponent} from "../shared/list-item/list-item-team/list-item-team.component";
import {ListItemUserComponent} from "../shared/list-item/list-item-user/list-item-user.component";
import {NgForOf, NgIf} from "@angular/common";
import {MatError} from "@angular/material/form-field";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {routes} from "../../app.routes";
import {MatDialog} from "@angular/material/dialog";
import {DialogMemberRequestComponent} from "../dialog-member-request/dialog-member-request.component";
import {UserWithRankDto} from "../../service/user/user.service";

@Component({
  selector: 'app-team-details',
  standalone: true,
  imports: [
    AppBarComponent,
    ListItemComponent,
    MatFabButton,
    MatIcon,
    TitleHeaderComponent,
    RouterLink,
    ListItemTeamComponent,
    ListItemUserComponent,
    NgForOf,
    MatError,
    NgIf
  ],
  templateUrl: './team-details.component.html',
  styleUrl: './team-details.component.scss'
})
export class TeamDetailsComponent implements OnInit {

  teamDetails: TeamDetailedWithMembersDto = {
    members: [],
    currentMembers: 0,
    maxMembers: 0,
    name: '',
    owner: '',
    uuid: '',
    totalScore: 0
  };

  memberRequestError: string = '';
  memberRequestSuccess: string = '';
  answerMemberRequestError: string = '';

  teamRequests: TeamRequestDto[] = [];

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private dialog: MatDialog,
              private teamService: TeamService,
              public authService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.updateTeam();
    this.updateMemberRequests();
  }

  updateMemberRequests() {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    this.teamService.listTeamRequests(teamUuid).subscribe({
      next: response => this.teamRequests = response,
    })
  }

  updateTeam() {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    this.teamService.fetchTeamDetails(teamUuid).subscribe({
      next: response => this.teamDetails = response,
    })
  }

  createMemberRequest() {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    this.teamService.createMemberRequest(teamUuid).subscribe({
      next: () => {
        this.memberRequestSuccess = 'Successful!'
        this.memberRequestError = ''
      },
      error: err => {
        this.memberRequestError = err.error.message
        this.memberRequestSuccess = ''
      }
    });
  }

  isTeamOwner(): boolean {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return false;
    }

    return this.authService.teamOwner === teamUuid;
  }

  deleteTeam() {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    this.teamService.deleteTeam(teamUuid).subscribe({
      next: () => {
        if (this.authService.currentUser != undefined) {
          const user = this.authService.currentUser;
          user.team = undefined
          this.authService.currentUser = user;
          this.authService.teamOwner = undefined;
        }
        this.router.navigate(['/dashboard'])
      }
    });
  }

  openRequestModal(user: UserWithRankDto) {
    const teamUuid = this.activatedRoute.snapshot.paramMap.get('id');

    if (teamUuid === null) {
      return;
    }

    const dialogRef = this.dialog.open(DialogMemberRequestComponent, {
      data: {user},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.teamService.answerMemberRequest(teamUuid, {userUuid: user.uuid, acceptRequest: result}).subscribe({
          next: () => {
            this.updateMemberRequests();
            if (result === true) {
              this.updateTeam();
            }
          },
          error: err => this.answerMemberRequestError = err.error.message
        })
      }
    });
  }
}
