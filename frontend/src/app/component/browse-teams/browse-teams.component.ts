import {Component, OnInit} from '@angular/core';
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {ListItemComponent} from "../shared/list-item/list-item.component";
import {MatFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TitleHeaderComponent} from "../shared/title-header/title-header.component";
import {NgForOf, NgIf} from "@angular/common";
import {ListItemTeamComponent} from "../shared/list-item/list-item-team/list-item-team.component";
import {TeamDetailedDto, TeamService} from "../../service/team/team.service";
import {Router, RouterLink} from "@angular/router";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-browse-teams',
  standalone: true,
  imports: [
    AppBarComponent,
    ListItemComponent,
    MatFabButton,
    MatIcon,
    TitleHeaderComponent,
    NgForOf,
    ListItemTeamComponent,
    RouterLink,
    NgIf,
    MatPaginator
  ],
  templateUrl: './browse-teams.component.html',
  styleUrl: './browse-teams.component.scss'
})
export class BrowseTeamsComponent implements OnInit {
  teams: TeamDetailedDto[] = [];
  totalElements: number = 0;
  pageSize: number = 5;
  page: number = 0;


  constructor(private teamService: TeamService, public authService: AuthenticationService, private router: Router) {
  }

  ngOnInit(): void {
    this.teamService.fetchTeams(this.page + 1, this.pageSize).subscribe({
      next: response => {
        this.teams = response.data
        this.totalElements = response.totalElements
      },
    })
  }

  showTeamDetails(uuid: string) {
    this.router.navigate([`/teams/${uuid}`])
  }
  handlePageEvent(e: PageEvent) {
    this.totalElements = e.length;
    this.pageSize = e.pageSize;
    this.page = e.pageIndex;

    this.teamService.fetchTeams(this.page + 1, this.pageSize).subscribe({
      next: response => {
        this.teams = response.data
        this.totalElements = response.totalElements
      }
    })
  }
}
