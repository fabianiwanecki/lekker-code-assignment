import {Component, Input} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {MatIcon} from "@angular/material/icon";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-list-item',
  standalone: true,
  imports: [
    MatDivider,
    MatIcon,
    NgForOf
  ],
  templateUrl: './list-item.component.html',
  styleUrl: './list-item.component.scss'
})
export class ListItemComponent {
  @Input()
  headline: string = '';

  @Input()
  supportingText: string = '';

  @Input()
  trailingText: string[] = [];

}
