import {Component, Input} from '@angular/core';
import {UserWithRankAndTeamDto} from "../../../../service/user/user.service";
import {ListItemComponent} from "../list-item.component";

@Component({
  selector: 'app-list-item-user',
  standalone: true,
  imports: [
    ListItemComponent
  ],
  templateUrl: './list-item-user.component.html',
  styleUrl: './list-item-user.component.scss'
})
export class ListItemUserComponent {

  @Input()
  user: UserWithRankAndTeamDto = {
    rank: 0,
    score: 0,
    username: "",
    uuid: "",
  };

}
