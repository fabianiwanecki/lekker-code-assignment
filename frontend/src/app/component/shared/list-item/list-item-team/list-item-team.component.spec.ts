import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListItemTeamComponent } from './list-item-team.component';

describe('ListItemTeamComponent', () => {
  let component: ListItemTeamComponent;
  let fixture: ComponentFixture<ListItemTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListItemTeamComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListItemTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
