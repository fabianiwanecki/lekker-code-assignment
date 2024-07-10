import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseTeamsComponent } from './browse-teams.component';

describe('BrowseTeamsComponent', () => {
  let component: BrowseTeamsComponent;
  let fixture: ComponentFixture<BrowseTeamsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseTeamsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrowseTeamsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
