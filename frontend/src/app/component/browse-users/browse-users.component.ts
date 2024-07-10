import {Component, OnInit} from '@angular/core';
import {AppBarComponent} from "../shared/app-bar/app-bar.component";
import {ListItemComponent} from "../shared/list-item/list-item.component";
import {MatButtonModule, MatFabButton} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {NgForOf} from "@angular/common";
import {ListItemUserComponent} from "../shared/list-item/list-item-user/list-item-user.component";
import {UserService, UserWithRankAndTeamDto} from "../../service/user/user.service";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-browse-users',
  standalone: true,
  imports: [
    AppBarComponent,
    ListItemComponent,
    MatButtonModule,
    MatIconModule,
    NgForOf,
    ListItemUserComponent,
    MatPaginatorModule
  ],
  templateUrl: './browse-users.component.html',
  styleUrl: './browse-users.component.scss'
})
export class BrowseUsersComponent implements OnInit {
  users: UserWithRankAndTeamDto[] = [];
  totalElements: number = 0;
  pageSize: number = 5;
  page: number = 0;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.fetchUsers(this.page + 1, this.pageSize).subscribe({
      next: response => {
        this.users = response.data
        this.totalElements = response.totalElements
      }
    })
  }

  handlePageEvent(e: PageEvent) {
    this.totalElements = e.length;
    this.pageSize = e.pageSize;
    this.page = e.pageIndex;

    this.userService.fetchUsers(this.page + 1, this.pageSize).subscribe({
      next: response => {
        this.users = response.data
        this.totalElements = response.totalElements
      }
    })
  }
}
