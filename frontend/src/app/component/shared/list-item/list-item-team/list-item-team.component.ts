import {Component, Input} from '@angular/core';
import {ListItemComponent} from "../list-item.component";
import {TeamDetailedDto} from "../../../../service/team/team.service";

@Component({
  selector: 'app-list-item-team',
  standalone: true,
  imports: [
    ListItemComponent
  ],
  templateUrl: './list-item-team.component.html',
  styleUrl: './list-item-team.component.scss'
})
export class ListItemTeamComponent {

  @Input()
  team: TeamDetailedDto = {
    name: '',
    owner: '',
    uuid: '',
    totalScore: 0,
    maxMembers: 0,
    currentMembers: 0,
  };

}
