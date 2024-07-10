import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseTeamsComponent } from './browse-teams.component';
import {provideHttpClient} from "@angular/common/http";
import {provideRouter, Router, Routes} from "@angular/router";
import {provideAnimations} from "@angular/platform-browser/animations";
import {TeamService} from "../../service/team/team.service";
import {AuthenticationService} from "../../service/authentication/authentication.service";
import {of} from "rxjs";
import {PageEvent} from "@angular/material/paginator";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('BrowseTeamsComponent', () => {
  let component: BrowseTeamsComponent;
  let fixture: ComponentFixture<BrowseTeamsComponent>;
  let teamServiceSpy: jasmine.SpyObj<TeamService>;
  let authServiceSpy: jasmine.SpyObj<AuthenticationService>;

  beforeEach(async () => {
    const teamSpy = jasmine.createSpyObj('TeamService', ['fetchTeams']);
    const authSpy = jasmine.createSpyObj('AuthenticationService', ['']);

    await TestBed.configureTestingModule({
      imports: [BrowseTeamsComponent],
      providers: [
        { provide: TeamService, useValue: teamSpy },
        { provide: AuthenticationService, useValue: authSpy },
        provideRouter(routes),
        provideAnimations()
      ]
    }).compileComponents();

    teamServiceSpy = TestBed.inject(TeamService) as jasmine.SpyObj<TeamService>;
    authServiceSpy = TestBed.inject(AuthenticationService) as jasmine.SpyObj<AuthenticationService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseTeamsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch teams on init', () => {
    const mockResponse = {
      totalElements: 2,
      elements: 1,
      page: 1,
      data: [
        {
          uuid: '',
          name: '',
          maxMembers: 10,
          currentMembers: 2,
          owner: '',
          totalScore: 102
        }
      ]
    };
    teamServiceSpy.fetchTeams.and.returnValue(of(mockResponse));

    fixture.detectChanges(); // This will call ngOnInit

    expect(teamServiceSpy.fetchTeams).toHaveBeenCalledWith(1, 5);
    expect(component.teams).toEqual(mockResponse.data);
    expect(component.totalElements).toBe(2);
  });

  it('should handle page event', () => {
    const pageEvent: PageEvent = {
      pageIndex: 1,
      pageSize: 10,
      length: 20
    };

    const mockResponse = {
      totalElements: 20,
      elements: 1,
      page: 1,
      data: [
        {
          uuid: '',
          name: '',
          maxMembers: 10,
          currentMembers: 2,
          owner: '',
          totalScore: 102
        }
      ]
    };

    teamServiceSpy.fetchTeams.and.returnValue(of(mockResponse));

    component.handlePageEvent(pageEvent);

    expect(teamServiceSpy.fetchTeams).toHaveBeenCalledWith(2, 10);
    expect(component.teams).toEqual(mockResponse.data);
    expect(component.totalElements).toBe(20);
    expect(component.page).toBe(1);
    expect(component.pageSize).toBe(10);
  });
});
