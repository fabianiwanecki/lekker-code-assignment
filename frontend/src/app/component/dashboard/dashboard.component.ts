import {Component, OnInit} from '@angular/core';
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {TitleHeaderComponent} from "../shared/title-header/title-header.component";
import {ListItemComponent} from "../shared/list-item/list-item.component";
import {MatButton, MatFabAnchor, MatFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {Router, RouterLink} from "@angular/router";
import {TeamDetailedDto, TeamService} from "../../service/team/team.service";
import {NgForOf, NgIf} from "@angular/common";
import {ListItemTeamComponent} from "../shared/list-item/list-item-team/list-item-team.component";
import {ListItemUserComponent} from "../shared/list-item/list-item-user/list-item-user.component";
import {UserService, UserWithRankAndTeamDto} from "../../service/user/user.service";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {MatInput} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {routes} from "../../app.routes";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    AppBarComponent,
    TitleHeaderComponent,
    ListItemComponent,
    MatFabButton,
    MatIcon,
    RouterLink,
    MatFabAnchor,
    NgForOf,
    ListItemTeamComponent,
    ListItemUserComponent,
    MatInput,
    ReactiveFormsModule,
    NgIf,
    MatButton
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  teams: TeamDetailedDto[] = [];
  users: UserWithRankAndTeamDto[] = [];

  constructor(private router: Router, private teamService: TeamService, private userService: UserService, public authService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.teamService.fetchTeams(1, 3).subscribe({
      next: response => {
        this.teams = response.data;
      },
    });

    this.userService.fetchUsers(1, 3).subscribe({
      next: response => {
        this.users = response.data;
      },
    });
  }

  showTeamDetails(uuid: string) {
    this.router.navigate([`/teams/${uuid}`])
  }

  logOut() {
    this.authService.logOut();
    this.router.navigate([''])
  }
}
