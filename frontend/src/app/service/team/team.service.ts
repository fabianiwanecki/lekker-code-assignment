import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {UserWithRankAndTeamDto, UserWithRankDto} from "../user/user.service";

export interface TeamDetailedDto {
  uuid: string;
  name: string;
  maxMembers: number;
  currentMembers: number
  owner: string;
  totalScore: number;
}

export interface TeamDetailedWithMembersDto {
  uuid: string;
  name: string;
  maxMembers: number;
  currentMembers: number
  owner: string;
  totalScore: number;
  members: UserWithRankDto[];
}

export interface TeamRequestDto {
  user: UserWithRankDto;
}

export interface TeamDto {
  uuid: string;
  name: string;
  maxMembers: number;
}

export interface PageDto<T> {
  page: number;
  elements: number;
  totalElements: number;
  data: T[];
}

export interface CreateTeamReqDto {
  name: string;
  maxMembers: number;
}

export interface UpdateTeamReqDto {
  name: string;
  maxMembers: number;
}

export interface AnswerRequestReqDto {
  userUuid: string;
  acceptRequest: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private httpClient: HttpClient) {
  }

  fetchTeams(page: number, size: number): Observable<PageDto<TeamDetailedDto>> {
    let params = new HttpParams();
    params = params.set('page', page);
    params = params.set('size', size);

    return this.httpClient.get<PageDto<TeamDetailedDto>>(
      environment.baseUrl + environment.teamsUrl,
      {params});
  }

  createTeam(createTeamReq: CreateTeamReqDto): Observable<TeamDto> {
    return this.httpClient.post<TeamDto>(environment.baseUrl + environment.createTeamUrl, createTeamReq);
  }

  fetchTeamDetails(teamUuid: string):Observable<TeamDetailedWithMembersDto> {
    return this.httpClient.get<TeamDetailedWithMembersDto>(`${environment.baseUrl}${environment.teamDetailsUrl}${teamUuid}`);
  }

  listTeamRequests(teamUuid: string):Observable<TeamRequestDto[]> {
    return this.httpClient.get<TeamRequestDto[]>(`${environment.baseUrl}${environment.teamsUrl}/${teamUuid}/${environment.memberRequestsUrl}`);
  }

  createMemberRequest(teamUuid: string) {
    return this.httpClient.post<TeamDto>(`${environment.baseUrl}${environment.teamsUrl}/${teamUuid}/${environment.memberRequestsUrl}`, {});
  }

  deleteTeam(teamUuid: string): Observable<void> {
    return this.httpClient.delete<void>(`${environment.baseUrl}${environment.teamsUrl}/${teamUuid}`);
  }

  updateTeam(teamUuid: string, updateTeamReq: UpdateTeamReqDto): Observable<TeamDto> {
    return this.httpClient.put<TeamDto>(`${environment.baseUrl}${environment.editTeamUrl}${teamUuid}`, updateTeamReq);
  }

  answerMemberRequest(teamUuid: string, answerRequestReq: AnswerRequestReqDto) {
    return this.httpClient.put<TeamDto>(`${environment.baseUrl}${environment.teamDetailsUrl}${teamUuid}/${environment.memberRequestsUrl}`, answerRequestReq);
  }
}
