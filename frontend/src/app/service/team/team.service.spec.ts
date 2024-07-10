import { TestBed } from '@angular/core/testing';

import {
  AnswerRequestReqDto,
  CreateTeamReqDto,
  PageDto,
  TeamDetailedDto,
  TeamDetailedWithMembersDto,
  TeamDto, TeamRequestDto,
  TeamService, UpdateTeamReqDto
} from './team.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {environment} from "../../../environments/environment.development";

describe('TeamService', () => {
  let service: TeamService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(TeamService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch teams', () => {
    const mockResponse: PageDto<TeamDetailedDto> = {
      page: 0,
      elements: 2,
      totalElements: 2,
      data: [
        { uuid: '1', name: 'Team 1', maxMembers: 5, currentMembers: 3, owner: 'Owner1', totalScore: 100 },
        { uuid: '2', name: 'Team 2', maxMembers: 5, currentMembers: 4, owner: 'Owner2', totalScore: 200 }
      ]
    };

    service.fetchTeams(0, 10).subscribe(teams => {
      expect(teams).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamsUrl}?page=0&size=10`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should create a team', () => {
    const createTeamReq: CreateTeamReqDto = { name: 'New Team', maxMembers: 5 };
    const mockResponse: TeamDto = { uuid: '3', name: 'New Team', maxMembers: 5 };

    service.createTeam(createTeamReq).subscribe(team => {
      expect(team).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.createTeamUrl}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(createTeamReq);
    req.flush(mockResponse);
  });

  it('should fetch team details', () => {
    const mockResponse: TeamDetailedWithMembersDto = {
      uuid: '1',
      name: 'Team 1',
      maxMembers: 5,
      currentMembers: 3,
      owner: 'Owner1',
      totalScore: 100,
      members: [
        { uuid: 'user1', username: 'User 1', score: 50, rank: 1 },
        { uuid: 'user2', username: 'User 2', score: 30, rank: 2 },
        { uuid: 'user3', username: 'User 3', score: 20, rank: 3 }
      ]
    };

    service.fetchTeamDetails('1').subscribe(team => {
      expect(team).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamDetailsUrl}1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should list team requests', () => {
    const mockResponse: TeamRequestDto[] = [
      { user: { uuid: 'user4', username: 'User 4', score: 40, rank: 4 } },
      { user: { uuid: 'user5', username: 'User 5', score: 35, rank: 5 } }
    ];

    service.listTeamRequests('1').subscribe(requests => {
      expect(requests).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamsUrl}/1/${environment.memberRequestsUrl}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should create a member request', () => {
    const mockResponse: TeamDto = { uuid: '1', name: 'Team 1', maxMembers: 5 };

    service.createMemberRequest('1').subscribe(team => {
      expect(team).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamsUrl}/1/${environment.memberRequestsUrl}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({});
    req.flush(mockResponse);
  });

  it('should delete a team', () => {
    service.deleteTeam('1').subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamsUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should update a team', () => {
    const updateTeamReq: UpdateTeamReqDto = { name: 'Updated Team', maxMembers: 6 };
    const mockResponse: TeamDto = { uuid: '1', name: 'Updated Team', maxMembers: 6 };

    service.updateTeam('1', updateTeamReq).subscribe(team => {
      expect(team).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.editTeamUrl}1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updateTeamReq);
    req.flush(mockResponse);
  });

  it('should answer a member request', () => {
    const answerRequestReq: AnswerRequestReqDto = { userUuid: 'user4', acceptRequest: true };
    const mockResponse: TeamDto = { uuid: '1', name: 'Team 1', maxMembers: 5 };

    service.answerMemberRequest('1', answerRequestReq).subscribe(team => {
      expect(team).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.teamDetailsUrl}1/${environment.memberRequestsUrl}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(answerRequestReq);
    req.flush(mockResponse);
  });
});
